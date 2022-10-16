package net.denanu.amazia.pathing;

public class PathStep {
    private final PathingNode node;
    private final PathStep parentStep;
    private int totalPathDistance;
    private int distanceToHere;
    private int heuristic;
    private PathStep previous;
    private PathStep nextStep;
    
    public PathStep(final PathingNode node, final PathStep neighbor, final PathingNode target, final PathStep parentStep) {
        this.distanceToHere = 0;
        this.previous = null;
        this.nextStep = null;
        this.node = node;
        this.heuristic = this.calcHeuristic(target, node);
        this.previous = neighbor;
        this.parentStep = parentStep;
        if (neighbor != null) {
            this.distanceToHere = this.neighborAdjacent(neighbor);
        }
        this.totalPathDistance = this.distanceToHere + this.heuristic;
    }
    
    public int calcManhattanDistance(final PathingNode target, final PathingNode here) {
        return Math.abs(target.cell.x - here.cell.x) + Math.abs(target.cell.y - here.cell.y) + Math.abs(target.cell.z - here.cell.z);
    }
    
    public int calcHeuristic(final PathingNode target, final PathingNode here) {
        return (int)Math.sqrt(Math.pow(Math.abs(target.cell.x - here.cell.x), 2.0) + Math.pow(Math.abs(target.cell.y - here.cell.y), 2.0) + Math.pow(Math.abs(target.cell.z - here.cell.z), 2.0));
    }
    
    public PathStep getParentStep() {
        return this.parentStep;
    }
    
    public PathingNode getNode() {
        return this.node;
    }
    
    public PathStep getNextStep() {
        return this.nextStep;
    }
    
    private int neighborAdjacent(final PathStep neighbor) {
        int newDist = neighbor.distanceToHere + 1;
        if (neighbor.node.cell.y != this.node.cell.y) {
            newDist += 8;
        }
        return newDist;
    }
    
    public boolean updateDistance(final PathStep neighbor) {
        final int newDist = this.neighborAdjacent(neighbor);
        if (newDist < this.distanceToHere) {
            this.distanceToHere = newDist;
            this.totalPathDistance = this.distanceToHere + this.heuristic;
            this.previous = neighbor;
            return true;
        }
        return false;
    }
    
    public PathStep reverseSteps() {
        PathStep step;
        for (step = this; step.previous != null; step = step.previous) {
            step.previous.nextStep = step;
        }
        return step;
    }
    
    public int getDistanceToHere() {
        return this.distanceToHere;
    }
    
    public int getTotalPathDistance() {
        return this.totalPathDistance;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof PathStep)) {
            return false;
        }
        final PathStep otherStep = (PathStep)other;
        return this.node == otherStep.node;
    }
}
