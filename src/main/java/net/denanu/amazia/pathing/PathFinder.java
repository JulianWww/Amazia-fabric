package net.denanu.amazia.pathing;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.AmaziaEntity;
import net.denanu.amazia.pathing.edge.PathingEdge;
import net.denanu.amazia.pathing.node.BasePathingNode;
import net.denanu.amazia.pathing.node.PathingNode;
import net.denanu.amazia.utils.queue.PriorityElement;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PathFinder {
	protected AmaziaEntity entity;
	protected ServerWorld world;
	protected PriorityQueue<PriorityElement<PathingNode>> nodeQueue = new PriorityQueue<PriorityElement<PathingNode>>(PriorityElement.comparator);
	protected HashSet<PathingNode> visited = new HashSet<PathingNode>();
	
	public PathFinder(final AmaziaEntity entityNav) {
        this.entity = entityNav;
    }
	
	public PathingGraph getGraph() {
		if (this.entity.hasVillage()) {
			return this.entity.getVillage().getPathingGraph();
			}
		return null;
	}
	
	@Nullable
    public PathingPath findPath(final World worldIn, final Entity targetEntity) {
        return null;
    }
    
    @Nullable
    public PathingPath findPath(final World worldIn, final BlockPos targetPos) {
        return this.findPath(worldIn, targetPos.getX() + 0.5f, targetPos.getY() + 0.5f, targetPos.getZ() + 0.5f);
    }
    
    private PathingPath findPath(final World worldIn, final double x, final double y, final double z) {
        this.world = (ServerWorld)worldIn;
        //this.getGraph().debugEdgeNodes(worldIn);
        final PathingGraph graph = this.getGraph();
        if (graph != null) {
            final BasePathingNode endNode = graph.getNode(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
            final BasePathingNode startNode = this.getStart(graph);
            return this.findPath(startNode, endNode, graph);
        }
        return null;
    }

	public PathingPath findPath(final BasePathingNode startNode, final BasePathingNode endNode, PathingGraph graph) {
		if (endNode == null || startNode == null) {
            return null;
        }
		
		if (graph.isSetupDone()) {
			return finalizeHirarchicalPath( this.findHirarchicalPath(startNode, endNode, graph));
		} 
		return finalizeBasePath( this.basePathFinder(startNode, endNode));
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
		this.visited.clear();
		this.nodeQueue.clear();
		this.nodeQueue.add(new PriorityElement<PathingNode>(estimateDistance(startNode, endNode), startNode));
		this.visited.add(startNode);
		
		startNode.distance = 0;
		startNode.getBlockPos().from = null;
		PriorityElement<PathingNode> current;
		PathingNode next;
		int currentLvl = 0;
		
		while (!nodeQueue.isEmpty()) {
			current = this.nodeQueue.poll();			
			if (current.getRight().getParent() != null && startNode.inSameSuperCluster(current.getRight()) && !visited.contains(current.getRight().getParent())) {
				next = current.getRight().getTopParent();
				visited.add(next);
				this.nodeQueue.add(new PriorityElement<PathingNode>(0, next));
			}
			if (current.getRight().getChild() != null && endNode.inSameCluster(current.getRight()) && !visited.contains(current.getRight().getChild())) {
				next = current.getRight().getChild();
				visited.add(current.getRight().getChild());
				this.nodeQueue.add(new PriorityElement<PathingNode>(0, next));
				currentLvl = next.getLvl();
			}
			if (current.getRight().getLvl() >= currentLvl) { // maybe remove
				for (PathingEdge edge : current.getRight().edges) {
					next = edge.to(current.getRight());
					if ((next.getParent() == null || current.getRight().getParent() == null) && !this.visited.contains(next)) {
						this.visited.add(next);
						next.getBlockPos().from = edge;
						
						if (next == endNode) {
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
	
	public BasePathingNode basePathFinder(BasePathingNode startNode, BasePathingNode endNode) {
		PriorityQueue<PriorityElement<BasePathingNode>> nodeQueue = new PriorityQueue<PriorityElement<BasePathingNode>>(PriorityElement.comparator);
		this.visited.clear();
		nodeQueue.add(new PriorityElement<BasePathingNode>(estimateHyperGreadyDistance(startNode, endNode), startNode));
		this.visited.add(startNode);
		
		startNode.distance = 0;
		startNode.movedFromNode = null;
		PriorityElement<BasePathingNode> current;
		
		while (!nodeQueue.isEmpty()) {
			current = nodeQueue.poll();
			
			for (BasePathingNode next : current.getRight().ajacentNodes) {
				if (!this.visited.contains(next)) {
					next.movedFromNode = current.getRight();
					if (next ==endNode) {
						return next;
					}
					
					next.distance = current.getRight().distance + 1;
					
					this.visited.add(next);
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
            node = graph.getNode(blockpos2.getX(), i + 1, blockpos2.getY());
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
}
