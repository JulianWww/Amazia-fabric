package net.denanu.amazia.pathing;

import java.util.Set;
import java.util.TreeSet;

import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Random;

public class PathingCellMap {
    private final int defaultCapacity;
    private int nodeCount;
    private BasePathingNode firstNode;
    private Map<Integer, Map<Integer, Set<BasePathingNode>>> baseNodes;
    private NavigableSet<BasePathingNode> edgeNodes;
    private Random rnd;
    
    public PathingCellMap(final int defaultMapCapacity) {
        this.nodeCount = 0;
        this.firstNode = null;
        this.edgeNodes = new TreeSet<BasePathingNode>(Comparator.comparingInt(a -> a.getBlockPos().getManhattanDistance((Vec3i)this.firstNode.getBlockPos())));
        this.rnd = new Random();
        this.defaultCapacity = defaultMapCapacity;
        this.baseNodes = new HashMap<Integer, Map<Integer, Set<BasePathingNode>>>(this.defaultCapacity);
    }
    
    public void putNode(final BasePathingNode node, final World world) {
        if (this.firstNode == null) {
            this.firstNode = node;
            this.edgeNodes.add(node);
        }
        Map<Integer, Set<BasePathingNode>> zMap = this.baseNodes.get(node.getCell().x);
        if (zMap == null) {
            zMap = new HashMap<Integer, Set<BasePathingNode>>(this.defaultCapacity);
            this.baseNodes.put(node.getCell().x, zMap);
        }
        Set<BasePathingNode> nodeSet = zMap.get(node.getCell().z);
        if (nodeSet == null) {
            nodeSet = new HashSet<BasePathingNode>();
            zMap.put(node.getCell().z, nodeSet);
        }
        if (this.rnd.nextInt(30) == 0) {
            final int edgeDist = this.getAxisDistance(this.firstNode.getBlockPos(), node.getBlockPos());
            if (edgeDist < 115 && world.isAir(node.getBlockPos())) {
                this.edgeNodes.add(node);
                if (this.edgeNodes.size() > 10) {
                    this.edgeNodes.pollFirst();
                }
            }
        }
        if (!nodeSet.add(node)) {
            throw new IllegalArgumentException("Duplicate BasePathingNode encountered");
        }
        ++this.nodeCount;
    }
    
    private int getAxisDistance(final BlockPos bp1, final BlockPos bp2) {
        return Math.max(Math.abs(bp1.getX() - bp2.getX()), Math.abs(bp1.getZ() - bp2.getZ()));
    }
    
    public void removeNode(final BasePathingNode node, final PathingGraph graph) {
        final Set<BasePathingNode> nodeSet = this.getXZSet(node.getCell().x, node.getCell().z);
        if (nodeSet != null && nodeSet.remove(node)) {
            node.destroy(graph);
            --this.nodeCount;
        }
    }
    
    public int nodeCount() {
        return this.nodeCount;
    }
    
    public BasePathingNode getEdgeNode(final BlockPos origin, final double minDist) {
        if (!this.edgeNodes.isEmpty()) {
            final int index = this.rnd.nextInt(this.edgeNodes.size());
            int i = 0;
            for (final BasePathingNode edgeNode : this.edgeNodes) {
                if (i == index) {
                    return edgeNode;
                }
                ++i;
            }
        }
        return null;
    }
    
    public void debugEdgeNodes(final World world) {
        for (final BasePathingNode node : this.edgeNodes) {
            System.out.println("Edge Node at " + node.getBlockPos());
            final ArmorStandEntity ent = new ArmorStandEntity(world, (double)node.getBlockPos().getX(), (double)node.getBlockPos().getY(), (double)node.getBlockPos().getZ());
            //ent.func_70690_d(new PotionEffect(MobEffects.field_188423_x, 200));
            //ent.func_70606_j(0.0f);
            //ent.field_70725_aQ = -200;
            world.spawnEntity(ent);
        }
    }
    
    public BasePathingNode getNode(final int x, final int y, final int z) {
        return this.getNodeYRange(x, y, y, z);
    }
    
    public BasePathingNode getNodeYRange(final int x, final int y1, final int y2, final int z) {
        final Set<BasePathingNode> nodeSet = this.getXZSet(x, z);
        if (nodeSet != null) {
            for (final BasePathingNode node : nodeSet) {
                if (node.getCell().y >= y1 && node.getCell().y <= y2) {
                    return node;
                }
            }
        }
        return null;
    }
    
    public void updateNodes(final int x, final int y1, final int y2, final int z, final PathingGraph graph) {
        final Set<BasePathingNode> nodeSet = this.getXZSet(x, z);
        if (nodeSet != null) {
            for (final BasePathingNode node : nodeSet) {
                if (node.getCell().y >= y1 && node.getCell().y <= y2) {
                    graph.addFirstNode(node);
                }
            }
        }
    }
    
    private Set<BasePathingNode> getXZSet(final int x, final int z) {
        final Map<Integer, Set<BasePathingNode>> zMap = this.baseNodes.get(x);
        if (zMap != null) {
            return zMap.get(z);
        }
        return null;
    }
    
    public Set<PathingNode> getTopNodes() {
        final PathingNode topNode = this.firstNode.getTopParent();
        final Set<PathingNode> outNodes = new HashSet<PathingNode>();
        this.fillConnections(topNode, outNodes);
        return outNodes;
    }
    
    /*public void notifyListenerInitial(final World world, final EntityPlayerMP player) {
        final List<EntityPlayerMP> listeners = new ArrayList<EntityPlayerMP>(1);
        listeners.add(player);
        for (final Map<Integer, Set<BasePathingNode>> zMap : this.baseNodes.values()) {
            for (final Set<BasePathingNode> nodeSet : zMap.values()) {
                for (final BasePathingNode node : nodeSet) {
                    node.notifyListeners(world, listeners);
                }
            }
        }
    }*/
    
    private void fillConnections(final PathingNode node, final Set<PathingNode> outNodes) {
        if (!outNodes.contains(node)) {
            outNodes.add(node);
            for (final PathingNode peer : node.connections) {
                this.fillConnections(peer, outNodes);
            }
        }
    }
    
    public BasePathingNode randomNode() {
        int numX = (int)(Math.random() * this.baseNodes.size());
        for (final Map<Integer, Set<BasePathingNode>> xMap : this.baseNodes.values()) {
            if (--numX < 0) {
                int numZ = (int)(Math.random() * xMap.size());
                for (final Set<BasePathingNode> zSet : xMap.values()) {
                    if (--numZ < 0) {
                        int numY = (int)(Math.random() * zSet.size());
                        for (final BasePathingNode node : zSet) {
                            if (--numY < 0) {
                                return node;
                            }
                        }
                    }
                }
            }
        }
        throw new AssertionError();
    }
}
