// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import me.zoom.xannax.event.Event;

public class DamageBlockEvent extends Event
{
    private BlockPos pos;
    private EnumFacing face;
    
    public DamageBlockEvent(final BlockPos pos, final EnumFacing face) {
        this.pos = pos;
        this.face = face;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public void setPos(final BlockPos pos) {
        this.pos = pos;
    }
    
    public EnumFacing getFace() {
        return this.face;
    }
    
    public void setFace(final EnumFacing face) {
        this.face = face;
    }
}
