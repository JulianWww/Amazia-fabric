package net.denanu.amazia.pathing;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.AmaziaEntity;
import net.denanu.amazia.pathing.edge.PathingEdge;
import net.denanu.amazia.pathing.node.BasePathingNode;
import net.denanu.amazia.pathing.node.PathingNode;
import net.denanu.amazia.utils.exceptions.UnimplementedException;
import net.denanu.amazia.utils.queue.PriorityElement;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.ChunkCache;

public class PathFinder extends EntityNavigation {
	protected AmaziaEntity entity;
	protected PriorityQueue<PriorityElement<PathingNode>> nodeQueue = new PriorityQueue<PriorityElement<PathingNode>>(PriorityElement.comparator);
	protected BlockPos currentLoc = BlockPos.ORIGIN;
	
	public PathFinder(final AmaziaEntity entityNav) {
		super(entityNav, entityNav.getWorld());
        this.entity = entityNav;
    }
	
	public PathingGraph getGraph() {
		if (this.entity.hasVillage()) {
			return this.entity.getVillage().getPathingGraph();
			}
		return null;
	}
	
	@Nullable
    public PathingPath findPath(final Entity targetEntity) {
        return null;
    }
    
	@Override
    @Nullable
    public PathingPath findPathTo(final BlockPos targetPos, int distance) {
        return this.findPathTo(targetPos);
    }
	
	@Override
	@Nullable
    public PathingPath findPathTo(BlockPos target, int minDistance, int maxDistance) {
        return this.findPathTo(target);
    }
	
	@Nullable
    public PathingPath findPathTo(BlockPos targetPos) {
        return this.findPathTo(targetPos.getX() + 0.5f, targetPos.getY() + 0.5f, targetPos.getZ() + 0.5f);
    }
    
    @Override
    @Nullable
    public Path findPathTo(Entity entity, int distance) {
        return this.findPathTo(entity.getBlockPos());
    }
    
    private PathingPath findPathTo(final double x, final double y, final double z) {
        //this.getGraph().debugEdgeNodes(worldIn);
        final PathingGraph graph = this.getGraph();
        if (graph != null) {
            final BasePathingNode endNode = graph.getNode(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
            final BasePathingNode startNode = this.getStart(graph);
            if (endNode == startNode) {
            	return null;
            }
            return this.findPathTo(startNode, endNode, graph);
        }
        return null;
    }

	public PathingPath findPathTo(final BasePathingNode startNode, final BasePathingNode endNode, PathingGraph graph) {
		if (endNode == null || startNode == null) {
            return null;
        }
		
		Amazia.LOGGER.info("Scanned for path " + endNode.getBlockPos());
		
		if (graph.isSetupDone()) {
			return finalizeHirarchicalPath( this.findHirarchicalPath(startNode, endNode, graph));
		} 
		Amazia.LOGGER.info("classic");
		return finalizeBasePath( this.basePathFinder(startNode, endNode, graph));
	}

	private static PathingPath finalizeHirarchicalPath(PathingNode end) {
		if (end == null) {
			return null;
		}
		BlockPos targetPos = end.getBlockPos();
		List<PathingNode> path = new LinkedList<PathingNode>();
		path.add(end);
		for (; end.getBlockPos().from != null; end = end.getBlockPos().from.to(end)) {
			path.addAll(end.getBlockPos().from.toPath(end));
		}
		return new PathingPath(path, targetPos);
	}
	private static PathingPath finalizeBasePath(BasePathingNode end) {
		if (end == null) {
			return null;
		}
		BlockPos targetPos = end.getBlockPos();
		List<PathingNode> path = new LinkedList<PathingNode>();
		while (end != null) {
			path.add(end);
			end = end.movedFromNode;
		}
		return new PathingPath(path, targetPos);
	}

	private PathingNode findHirarchicalPath(BasePathingNode startNode, BasePathingNode endNode, PathingGraph graph) {
		graph.nextEval();
		int currentEval = graph.getEvalIndex();
		
		this.nodeQueue.clear();
		this.nodeQueue.add(new PriorityElement<PathingNode>(estimateDistance(startNode, endNode), startNode));
		
		startNode.distance = 0;
		startNode.getBlockPos().updateConnectivity(null, currentEval);
		startNode.lastEvaluation = currentEval;
		
		PriorityElement<PathingNode> current;
		PathingNode next;
		int currentLvl = 0;
		
		while (!nodeQueue.isEmpty()) {
			current = this.nodeQueue.poll();
			//current.getRight().debugPlace(this.world);
			if (current.getRight().getParent() != null && startNode.inSameSuperCluster(current.getRight()) && currentEval!=current.getRight().getParent().lastEvaluation) {
				next = current.getRight().getTopParent();
				next.lastEvaluation = currentEval;
				this.nodeQueue.add(new PriorityElement<PathingNode>(0, next));
			}
			if (current.getRight().getChild() != null && endNode.inSameSubCluster(current.getRight()) && currentEval!=current.getRight().getChild().lastEvaluation) {
				next = current.getRight().getChild();
				next.lastEvaluation = currentEval;
				this.nodeQueue.add(new PriorityElement<PathingNode>(0, next));
				currentLvl = next.getLvl();
			}
			if (current.getRight().getLvl() >= currentLvl) { // maybe remove
				for (PathingEdge edge : current.getRight().edges) {
					next = edge.to(current.getRight());
					if ((next.getParent() == null || current.getRight().getParent() == null) && currentEval!=next.lastEvaluation) {
						next.lastEvaluation = currentEval;
						next.getBlockPos().updateConnectivity(edge, currentEval);
						
						if (next.lvllessEquals(endNode)) {
							return next;
						}
						
						next.distance = current.getRight().distance + edge.getLength();
						
						this.nodeQueue.add(new PriorityElement<PathingNode>(estimateDistance(next, endNode) + next.distance, next));
					}
				}
			}
		}
		return null;
	}
	
	public BasePathingNode basePathFinder(BasePathingNode startNode, BasePathingNode endNode, PathingGraph graph) {
		graph.nextEval();
		int currentEval = graph.getEvalIndex();
		
		PriorityQueue<PriorityElement<BasePathingNode>> nodeQueue = new PriorityQueue<PriorityElement<BasePathingNode>>(PriorityElement.comparator);
		nodeQueue.add(new PriorityElement<BasePathingNode>(estimateHyperGreadyDistance(startNode, endNode), startNode));
		startNode.lastEvaluation = currentEval;
		
		startNode.distance = 0;
		startNode.movedFromNode = null;
		PriorityElement<BasePathingNode> current;
		
		while (!nodeQueue.isEmpty()) {
			current = nodeQueue.poll();
			for (BasePathingNode next : current.getRight().ajacentNodes) {
				if (currentEval!=next.lastEvaluation) {
					next.movedFromNode = current.getRight();
					if (next == endNode) {
						return next;
					}
					
					next.distance = current.getRight().distance + 1;
					
					next.lastEvaluation = currentEval;
					nodeQueue.add(new PriorityElement<BasePathingNode>(next.distance + estimateHyperGreadyDistance(next, endNode), next));
				}
			}
		}
		return null;
	}
	
	private static int estimateDistance(PathingNode n1, PathingNode n2) {
		return n1.manhattenDistance(n2);
	}
	
	private static int estimateHyperGreadyDistance(PathingNode n1, PathingNode n2) {
		return n1.getSquaredDistance(n2);
	}
	
	// Minecraft pathfinding override requirements (arg)
	
	private BasePathingNode getStart(final PathingGraph graph) {
    	int i;
        /*if (this.entity.func_70090_H()) {
            i = (int)this.entity.func_174813_aQ().field_72338_b;
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.func_76128_c(this.entity.field_70165_t), i, MathHelper.func_76128_c(this.entity.field_70161_v));
            for (Block block = this.blockAccess.func_180495_p((BlockPos)blockpos$mutableblockpos).func_177230_c(); block == Blocks.field_150358_i || block == Blocks.field_150355_j; block = this.blockAccess.func_180495_p((BlockPos)blockpos$mutableblockpos).func_177230_c()) {
                ++i;
                blockpos$mutableblockpos.func_181079_c(MathHelper.func_76128_c(this.entity.field_70165_t), i, MathHelper.func_76128_c(this.entity.field_70161_v));
            }
        }*/ // idk
        if (this.entity.isOnGround()) { // if else (actually)
            i = MathHelper.floor(this.entity.getY() + 0.5);
        }
        else {
        	BlockPos pos;
        	for (pos = new BlockPos(this.entity.getPos()); !BasePathingNode.isPassable(graph.getWorld(), pos) && pos.getY() >= Amazia.LOWER_WORLD_BORDER; pos = pos.down()) {}
        	i = pos.getY();
        }
        /* falling code ?? else {
            BlockPos blockpos;
            for (blockpos = new BlockPos((Entity)this.entity); (this.blockAccess.func_180495_p(blockpos).func_185904_a() == Material.field_151579_a || this.blockAccess.func_180495_p(blockpos).func_177230_c().func_176205_b(this.blockAccess, blockpos)) && blockpos.func_177956_o() > 0; blockpos = blockpos.func_177977_b()) {}
            i = blockpos.func_177984_a().func_177956_o();
        }*/
        final BlockPos blockpos2 = new BlockPos(this.entity.getPos());
        BasePathingNode node = graph.getNode(blockpos2.getX(), i, blockpos2.getZ());
        if (node == null) {
            node = graph.getNode(blockpos2.getX(), i + 1, blockpos2.getZ());
        }
        if (node == null) {
        	node = graph.getNode(blockpos2.getX(), i - 1, blockpos2.getZ());
        }
        if (node == null) {
            final Set<BlockPos> set = new HashSet<BlockPos>();
            set.add(new BlockPos(this.entity.getX() + 1, (double)i, this.entity.getZ() + 1));
            set.add(new BlockPos(this.entity.getX() + 1, (double)i, this.entity.getZ() - 1));
            set.add(new BlockPos(this.entity.getX() - 1, (double)i, this.entity.getZ() + 1));
            set.add(new BlockPos(this.entity.getX() - 1, (double)i, this.entity.getZ() - 1));
            for (final BlockPos blockpos3 : set) {
                node = graph.getNodeYRange(blockpos3.getX(), blockpos3.getY() - 1, blockpos3.getY(), blockpos3.getZ());
                if (node != null) {
                    return node;
                }
            }
        }
        return node;
    }

	@Override
	protected PathNodeNavigator createPathNodeNavigator(int var1) {
		return null;
	}

	@Override
	protected Vec3d getPos() {
		return new Vec3d(this.entity.getX(), this.getPathfindingY(), this.entity.getZ());
	}
	
	private int getPathfindingY() {
        if (!this.entity.isTouchingWater() || !this.canSwim()) {
            return MathHelper.floor(this.entity.getY() + 0.5);
        }
        int i = this.entity.getBlockY();
        BlockState blockState = this.world.getBlockState(new BlockPos(this.entity.getX(), (double)i, this.entity.getZ()));
        int j = 0;
        while (blockState.isOf(Blocks.WATER)) {
            blockState = this.world.getBlockState(new BlockPos(this.entity.getX(), (double)(++i), this.entity.getZ()));
            if (++j <= 16) continue;
            return this.entity.getBlockY();
        }
        return i;
    }
	

	@Override
	protected boolean isAtValidPosition() {
		return this.entity.isOnGround() && this.entity.getVillage().isInVillage(this.entity.getBlockPos());
	}
	
	@Override
	public void recalculatePath() {
		if (this.getCurrentPath() != null && this.getTargetPos() != null) {
            this.currentPath = this.findPathTo(this.getTargetPos());
        }
    }
	
	@Override
    @Nullable
    public PathingPath findPathToAny(Stream<BlockPos> positions, int distance) throws RuntimeException {
        throw new RuntimeException("not implmented as I dont see the use");
    }
    
    @Override
    @Nullable
    public PathingPath findPathTo(Set<BlockPos> positions, int distance) throws RuntimeException {
    	throw new RuntimeException("not implmented as I dont see the use");
    }
    
    @Override
    @Nullable
    protected Path findPathTo(Set<BlockPos> positions, int range, boolean useHeadPos, int distance) throws RuntimeException {
    	throw new RuntimeException("not implmented as I dont see the use");
    }
    
    @Override
    @Nullable
    protected Path findPathToAny(Set<BlockPos> positions, int range, boolean useHeadPos, int distance, float followRange) throws RuntimeException {
    	throw new RuntimeException("not implmented as I dont see the use");
    }
	
	@Override
	public boolean startMovingTo(double x, double y, double z, double speed) throws RuntimeException {
		throw new RuntimeException("not implmented as I dont see the use");
    }

	@Override
    public boolean startMovingTo(Entity entity, double speed) throws RuntimeException{
		throw new RuntimeException("not implmented as I dont see the use");
    }
	
	@Override
	protected void checkTimeouts(Vec3d currentPos) {
        if (this.tickCount - this.pathStartTime > 100) {
            if (currentPos.squaredDistanceTo(this.pathStartPos) < 2.25) {
                //this.nearPathStartPos = true;
                this.stop();
            } /*else {
                this.nearPathStartPos = false;
            }*/
            this.pathStartTime = this.tickCount;
            this.pathStartPos = currentPos;
        }
        /*if (this.currentPath != null && !this.currentPath.isFinished()) {
            BlockPos vec3i = this.currentPath.getCurrentNodePos();
            if (vec3i.equals(this.lastNodePosition)) {
                this.currentNodeMs += Util.getMeasuringTimeMs() - this.lastActiveTickMs;
            } else {
                this.lastNodePosition = vec3i;
                double d = currentPos.distanceTo(Vec3d.ofBottomCenter(this.lastNodePosition));
                double d2 = this.currentNodeTimeout = this.entity.getMovementSpeed() > 0.0f ? d / (double)this.entity.getMovementSpeed() * 1000.0 : 0.0;
            }
            if (this.currentNodeTimeout > 0.0 && (double)this.currentNodeMs > this.currentNodeTimeout * 3.0) {
                this.resetNodeAndStop();
            }
            this.lastActiveTickMs = Util.getMeasuringTimeMs();
        }*/
    }
	
	@Override
	 protected void continueFollowingPath() {
		if (this.entity.getBlockPos() != this.currentLoc) {
			this.closeBlock();
		}
		this.currentLoc = this.entity.getBlockPos();
		
        boolean bl;
        Vec3d vec3d = this.getPos();
        this.nodeReachProximity = this.entity.getWidth() > 0.75f ? this.entity.getWidth() / 2.0f : 0.75f - this.entity.getWidth() / 2.0f;
        BlockPos vec3i = this.currentPath.getCurrentNodePos();
        double d = Math.abs(this.entity.getX() - ((double)vec3i.getX() + 0.5));
        double e = Math.abs(this.entity.getY() - (double)vec3i.getY());
        double f = Math.abs(this.entity.getZ() - ((double)vec3i.getZ() + 0.5));
        
        ((ServerWorld)this.world).spawnParticles(ParticleTypes.HAPPY_VILLAGER, vec3i.getX(), vec3i.getY(), vec3i.getZ(), 2, 0,0,0,0);
        
        bl = d < (double)this.nodeReachProximity && f < (double)this.nodeReachProximity && e < 1.0;
        if (bl || this.entity.canJumpToNextPathNode(this.currentPath.getCurrentNode().type) && this.shouldJumpToNextNode(vec3d)) {
            this.currentPath.next();
            this.openBlock();
        }
        this.checkTimeouts(vec3d);
    }
	
	
	private boolean shouldJumpToNextNode(Vec3d currentPos) {
        if (this.currentPath.getCurrentNodeIndex() + 1 >= this.currentPath.getLength()) {
            return false;
        }
        Vec3d vec3d = Vec3d.ofBottomCenter(this.currentPath.getCurrentNodePos());
        if (Math.abs(vec3d.getX() - currentPos.getX()) + Math.abs(vec3d.getZ() - currentPos.getZ()) < 0.5) {
            return true;
        }
        return false;
    }
	
	private void openBlock() {
		if (this.currentPath.getCurrentNodeIndex() < this.currentPath.getLength()) {
			this.setBlocOpenState(true, this.currentPath.getCurrentNode().getBlockPos());
			this.setBlocOpenState(true, this.entity.getBlockPos());
		}
	}
	
	private void closeBlock() {
		this.setBlocOpenState(false, this.currentLoc);
	}
	
	private void setBlocOpenState(boolean value, BlockPos pos) {
		BlockState state = ((ServerWorld)this.entity.world).getBlockState(pos);
		
		if (state.getBlock() instanceof DoorBlock) {
			((DoorBlock)state.getBlock()).setOpen(this.entity, this.entity.world, state, pos, value);
		}
		else if (state.getBlock() instanceof FenceGateBlock) {
			state = state.with(FenceGateBlock.OPEN, value);
            world.setBlockState(pos, state, Block.NOTIFY_LISTENERS | Block.REDRAW_ON_MAIN_THREAD);
		}
	}
}
