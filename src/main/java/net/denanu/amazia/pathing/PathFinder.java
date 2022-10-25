package net.denanu.amazia.pathing;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import javax.annotation.Nullable;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.AmaziaEntity;
import net.denanu.amazia.utils.queue.PriorityElement;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PathFinder {
	private AmaziaEntity entity;
	private ServerWorld world;
	protected PriorityQueue<PriorityElement<PathingNode>> nodeQueue = new PriorityQueue<PriorityElement<PathingNode>>(PriorityElement.comparator);
	protected HashSet<PathingNode> visited = new HashSet<PathingNode>();
	
	public PathFinder(AmaziaEntity e) {
		this.entity = e;
	}
	
	public PathingGraph getGraph() {
		if (this.entity.hasVillage()) {
			return this.entity.getVillage().getPathingGraph();
			}
		return null;
	}

	@Nullable
    public PathingPath findPath(final World worldIn, final Entity targetEntity) {
        return findPath(worldIn, targetEntity.getBlockPos());
    }
    
    @Nullable
    public PathingPath findPath(final World worldIn, final BlockPos targetPos) {
        return this.findPath(worldIn, targetPos.getX() + 0.5f, targetPos.getY() + 0.5f, targetPos.getZ() + 0.5f);
    }
    
    private PathingPath findPath(final World worldIn, final double x, final double y, final double z) {
        this.world = (ServerWorld)worldIn;

        final PathingGraph graph = this.getGraph();
        if (graph != null) {
            final PathingNode endNode = graph.getNode(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
            final PathingNode startNode = this.getStart(graph);
            if (endNode == startNode) {
            	return null;
            }
            return this.findPath(startNode, endNode, graph);
        }
        return null;
    }

	public PathingPath findPath(final PathingNode startNode, final PathingNode endNode, PathingGraph graph) {
		if (endNode == null || startNode == null) {
            return null;
        }
		
		return finalizeBasePath( this.basePathFinder(startNode, endNode));
	}
	
	private PathingPath finalizeBasePath(PathingNode end) {
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
	
	private static int estimateHyperGreadyDistance(PathingNode n1, PathingNode n2) {
		return n1.getSquaredDistance(n2);
	}

	public PathingNode basePathFinder(PathingNode startNode, PathingNode endNode) {
		PriorityQueue<PriorityElement<PathingNode>> nodeQueue = new PriorityQueue<PriorityElement<PathingNode>>(PriorityElement.comparator);
		this.visited.clear();
		nodeQueue.add(new PriorityElement<PathingNode>(estimateHyperGreadyDistance(startNode, endNode), startNode));
		this.visited.add(startNode);
		
		startNode.distance = 0;
		startNode.movedFromNode = null;
		PriorityElement<PathingNode> current;
		
		while (!nodeQueue.isEmpty()) {
			current = nodeQueue.poll();
			//current.getRight().debug(this.getGraph());
			for (PathingNode next : current.getRight().edges) {
				if (!this.visited.contains(next)) {
					next.movedFromNode = current.getRight();
					if (next == endNode) {
						return next;
					}
					
					next.distance = current.getRight().distance + 1;
					
					this.visited.add(next);
					nodeQueue.add(new PriorityElement<PathingNode>(next.distance + estimateHyperGreadyDistance(next, endNode), next));
				}
			}
		}
		return null;
	}

	private PathingNode getStart(final PathingGraph graph) {
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
        	for (pos = new BlockPos(this.entity.getPos()); !PathingNode.isPassable(graph.getWorld(), pos) && pos.getY() >= Amazia.LOWER_WORLD_BORDER; pos = pos.down()) {}
        	i = pos.getY();
        }
        /* falling code ?? else {
            BlockPos blockpos;
            for (blockpos = new BlockPos((Entity)this.entity); (this.blockAccess.func_180495_p(blockpos).func_185904_a() == Material.field_151579_a || this.blockAccess.func_180495_p(blockpos).func_177230_c().func_176205_b(this.blockAccess, blockpos)) && blockpos.func_177956_o() > 0; blockpos = blockpos.func_177977_b()) {}
            i = blockpos.func_177984_a().func_177956_o();
        }*/
        final BlockPos blockpos2 = new BlockPos(this.entity.getPos());
        PathingNode node = graph.getNode(blockpos2.getX(), i, blockpos2.getZ());
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
                node = graph.getNode(blockpos3);
                if (node != null) {
                    return node;
                }
            }
        }
        return node;
    }
}
