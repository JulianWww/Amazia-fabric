package net.denanu.amazia.pathing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.entities.village.server.AmaziaEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PathFinder  extends MobNavigation {
	protected AmaziaEntity entity;
    protected World blockAccess;
    protected PriorityQueue<PathStep> openSteps;
    protected Set<PathingNode> closedNodes;
    
    public PathFinder(final AmaziaEntity entityNav) {
        super(entityNav, entityNav.world);
        this.openSteps = new PriorityQueue<PathStep>(Comparator.comparingInt(a -> a.getTotalPathDistance()));
        this.closedNodes = new HashSet<PathingNode>();
        this.entity = entityNav;
        //this.blockAccess = (IBlockAccess)entityNav.getBlo;
    }
    
    public PathingGraph getGraph() {
        if (this.entity.hasVillage()) {
            return this.entity.getVillage().getPathingGraph();
        }
        return null;
    }
    
    @Nullable
    public PathingPath findPath(final World worldIn, final Entity entitylivingIn, final Entity targetEntity, final float maxDistance) {
        return this.findPath(worldIn, entitylivingIn, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ());
    }
    
    @Nullable
    public PathingPath findPath(final World worldIn, final Entity entitylivingIn, final BlockPos targetPos, final float maxDistance) {
        return this.findPath(worldIn, entitylivingIn, targetPos.getX() + 0.5f, targetPos.getY() + 0.5f, targetPos.getZ() + 0.5f);
    }
    
    private PathingPath findPath(final World worldIn, final Entity entityLivingIn, final double x, final double y, final double z) {
        this.blockAccess = worldIn;
        final PathingGraph graph = this.getGraph();
        if (graph != null) {
            final PathingNode endNode = graph.getBaseNode(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
            final PathingNode startNode = this.getStart(graph);
            return this.findPath(worldIn, startNode, endNode);
        }
        return null;
    }
    
    public PathingPath findPath(final World worldIn, final PathingNode startNode, final PathingNode endNode) {
        if (endNode == null || startNode == null) {
            return null;
        }
        PathStep firstStep = null;
        int level;
        for (level = endNode.getTopParent().getCell().level; level >= 0; --level) {
            final PathingNode localStart = startNode.getParent(level);
            final PathingNode localEnd = endNode.getParent(level);
            firstStep = this.findLevelPath(new PathStep(localStart, null, localEnd, firstStep), localEnd);
        }
        return this.finalizePath(firstStep, endNode.getBlockPos());
    }
    
    private PathStep findLevelPath(final PathStep startPoint, final PathingNode endNode) {
        this.openSteps.clear();
        this.closedNodes.clear();
        this.openSteps.add(startPoint);
        while (true) {
            final PathStep current = this.openSteps.poll();
            if (current == null) {
                return null;
            }
            this.closedNodes.add(current.getNode());
            if (current.getNode() == endNode) {
                return current.reverseSteps();
            }
            for (final PathingNode connectedNode : current.getNode().connections) {
                if (!this.closedNodes.contains(connectedNode)) {
                    boolean isOpen = false;
                    PathStep stepToAdd = null;
                    final Iterator<PathStep> itr = this.openSteps.iterator();
                    while (itr.hasNext()) {
                        final PathStep step = itr.next();
                        if (step.getNode().equals(connectedNode)) {
                            if (step.updateDistance(current)) {
                                itr.remove();
                                stepToAdd = step;
                            }
                            isOpen = true;
                            break;
                        }
                    }
                    if (!isOpen) {
                        if (current.getParentStep() == null) {
                            stepToAdd = new PathStep(connectedNode, current, endNode, null);
                        }
                        else if (connectedNode.getParent() == current.getParentStep().getNode()) {
                            stepToAdd = new PathStep(connectedNode, current, endNode, current.getParentStep());
                        }
                        else if (current.getParentStep().getNextStep() != null && connectedNode.getParent() == current.getParentStep().getNextStep().getNode()) {
                            stepToAdd = new PathStep(connectedNode, current, endNode, current.getParentStep().getNextStep());
                        }
                    }
                    if (stepToAdd == null) {
                        continue;
                    }
                    this.openSteps.add(stepToAdd);
                }
            }
        }
    }
    
    private PathingPath finalizePath(final PathStep firstStep, final BlockPos pos) {
        final ArrayList<PathNode> list = new ArrayList<PathNode>();
        for (PathStep step = firstStep; step != null; step = step.getNextStep()) {
            final PathingCell cell = step.getNode().getCell();
            list.add(new PathNode(cell.x, cell.y, cell.z));
        }
        return new PathingPath(list, pos, true);
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
        	return null;
        }
        /* falling code ?? else {
            BlockPos blockpos;
            for (blockpos = new BlockPos((Entity)this.entity); (this.blockAccess.func_180495_p(blockpos).func_185904_a() == Material.field_151579_a || this.blockAccess.func_180495_p(blockpos).func_177230_c().func_176205_b(this.blockAccess, blockpos)) && blockpos.func_177956_o() > 0; blockpos = blockpos.func_177977_b()) {}
            i = blockpos.func_177984_a().func_177956_o();
        }*/
        final BlockPos blockpos2 = new BlockPos(this.entity.getPos());
        PathingNode node = graph.getBaseNode(blockpos2.getX(), i, blockpos2.getY());
        if (node == null) {
            node = graph.getBaseNode(blockpos2.getX(), i + 1, blockpos2.getY());
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
