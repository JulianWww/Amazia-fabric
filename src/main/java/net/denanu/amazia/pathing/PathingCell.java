package net.denanu.amazia.pathing;

import net.minecraft.util.math.BlockPos;

public class PathingCell {
    public final int x;
    public final int y;
    public final int z;
    public final byte level;
    
    public PathingCell(final BlockPos bp, final byte level) {
        this(bp.getX(), bp.getY(), bp.getZ(), level);
    }
    
    private PathingCell(final int x, final int y, final int z, final byte level) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.level = level;
    }
    
    public BlockPos getBlockPos() {
        return new BlockPos(this.x, this.y, this.z);
    }
    
    public PathingCell up() {
        return this.up((byte)1);
    }
    
    public PathingCell up(final byte levels) {
        return new PathingCell(this.x >> levels, this.y >> levels, this.z >> levels, (byte)(this.level + levels));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof PathingCell)) {
            return false;
        }
        final PathingCell other = (PathingCell)o;
        return this.x == other.x && this.y == other.y && this.z == other.z && this.level == other.level;
    }
    
    @Override
    public String toString() {
        return "[" + this.level + "][" + this.x + ", " + this.y + ", " + this.z + "]";
    }
    
    public static int hashCode(final int x, final int z) {
        int result = x;
        result = 31 * result + z;
        return result;
    }
    
    @Override
    public int hashCode() {
        return hashCode(this.x, this.z);
    }
}
