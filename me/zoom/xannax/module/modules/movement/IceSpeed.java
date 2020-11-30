// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import net.minecraft.init.Blocks;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class IceSpeed extends Module
{
    Setting.Double speed;
    
    public IceSpeed() {
        super("IceSpeed", "SPEED", Category.Movement);
    }
    
    @Override
    public void setup() {
        this.speed = this.registerDouble("Speed", "Speed", 0.4, 0.0, 1.0);
    }
    
    @Override
    public void onUpdate() {
        Blocks.ICE.slipperiness = (float)this.speed.getValue();
        Blocks.PACKED_ICE.slipperiness = (float)this.speed.getValue();
        Blocks.FROSTED_ICE.slipperiness = (float)this.speed.getValue();
    }
    
    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }
}
