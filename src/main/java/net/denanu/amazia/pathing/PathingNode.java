package net.denanu.amazia.pathing;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PathingNode {
    protected final PathingCell cell;
    protected PathingNode parent;
    protected boolean queued;
    protected boolean destroyed;
    public Set<PathingNode> connections;
    public Set<PathingNode> children;
    
    public PathingNode(final PathingCell cell) {
        this.parent = null;
        this.queued = false;
        this.destroyed = false;
        this.connections = new HashSet<PathingNode>();
        this.children = new HashSet<PathingNode>();
        this.cell = cell;
    }
    
    public PathingCell getCell() {
        return this.cell;
    }
    
    public BlockPos getBlockPos() {
        return this.getCell().getBlockPos();
    }
    
    public void process(final World world, final PathingCellMap cellMap, final PathingGraph graph) {
        this.queued = false;
        this.updateConnections(world, cellMap, graph);
    }
    
    public int updateConnections(final World world, final PathingCellMap cellMap, final PathingGraph graph) {
        final Set<PathingNode> lastConnections = new HashSet<PathingNode>(this.connections);
        for (final PathingNode child : this.children) {
            for (final PathingNode childConnection : child.connections) {
                if (childConnection.parent != null && childConnection.parent != this) {
                    lastConnections.remove(childConnection.parent);
                    if (this.connections.contains(childConnection.parent)) {
                        this.checkParentLink(childConnection.parent);
                    }
                    else {
                        connectNodes(this, childConnection.parent, graph);
                    }
                }
            }
        }
        for (final PathingNode toBreak : lastConnections) {
            this.breakConnection(toBreak, graph);
        }
        if (this.parent == null && this.cell.level < 4) {
            (this.parent = new PathingNode(this.getCell().up())).addChild(this);
            graph.addLastNode(this.parent);
        }
        return this.connections.size();
    }
    
    public void queue() {
        this.queued = true;
    }
    
    public boolean isQueued() {
        return this.queued;
    }
    
    public PathingNode getConnection(final int x, final int z) {
        for (final PathingNode node : this.connections) {
            if (node.getCell().x == this.cell.x + x && node.getCell().z == this.cell.z + z) {
                return node;
            }
        }
        return null;
    }
    
    public boolean isConnected(final PathingNode node) {
        return this.connections.contains(node);
    }
    
    protected static void connectNodes(final PathingNode node1, final PathingNode node2, final PathingGraph graph) {
        node1.connections.add(node2);
        node1.checkParentLink(node2);
        node2.connections.add(node1);
        node2.checkParentLink(node1);
        if (node1.parent != node2.parent) {
            if (node1.parent != null && node1.connections.size() > 0) {
                graph.addLastNode(node1.parent);
            }
            if (node2.parent != null && node2.connections.size() > 0) {
                graph.addLastNode(node2.parent);
            }
        }
    }
    
    /*protected void notifyListeners(final World world, final List<EntityPlayerMP> listeners) {
        for (final PathingNode child : this.children) {
            child.notifyListeners(world, listeners);
        }
    }*/
    
    protected void breakConnection(final PathingNode node2, final PathingGraph graph) {
        //this.connections.remove(node2);
        node2.connections.remove(this);
        if (this.parent != node2.parent && node2.parent != null) {
            graph.addLastNode(node2.parent);
        }
    }
    protected void breakAllOutgoingConnections() {
    	this.connections.clear();
    }
    
    protected void checkParentLink(final PathingNode node) {
        if (this.parent == null && node.parent != null && node.parent.cell.equals(this.cell.up())) {
            node.parent.addChild(this);
        }
    }
    
    protected void removeChild(final PathingNode child) {
        child.parent = null;
        this.children.remove(child);
    }
    
    protected void addChild(final PathingNode child) {
        child.parent = this;
        this.children.add(child);
    }
    
    public PathingNode getParent() {
        return this.parent;
    }
    
    public PathingNode getParent(int levels) {
        PathingNode p;
        for (p = this; p.parent != null && levels > 0; --levels, p = p.parent) {}
        return p;
    }
    
    public PathingNode getTopParent() {
        PathingNode p;
        for (p = this; p.parent != null; p = p.parent) {}
        return p;
    }
    
    public boolean isDestroyed() {
        return this.destroyed;
    }
    
    public void destroy(final PathingGraph graph) {
        this.destroyed = true;
        //new HashSet(this.connections).forEach(c -> this.breakConnection(c, graph));
        for (PathingNode node: this.connections) {
        	this.breakConnection(node, graph);
        }
        
        graph.getEventEmiter().sendDestroy(this.getBlockPos());
        
        this.breakAllOutgoingConnections();
        
        if (this.parent != null) {
            final PathingNode par = this.parent;
            this.parent.removeChild(this);
            if (par.children.size() <= 0) {
                par.destroy(graph);
            }
        }
    }
    
    @Override
    public String toString() {
        return this.cell.toString();
    }
}
