// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.player;

import net.minecraft.item.ItemPickaxe;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class NoEntityTrace extends Module
{
    Setting.Boolean pickaxeOnly;
    boolean isHoldingPickaxe;
    
    public NoEntityTrace() {
        super("NoEntityTrace", "NoEntityTrace", Category.Player);
        this.isHoldingPickaxe = false;
    }
    
    @Override
    public void setup() {
        this.pickaxeOnly = this.registerBoolean("Pickaxe Only", "PickaxeOnly", true);
    }
    
    @Override
    public void onUpdate() {
        this.isHoldingPickaxe = (NoEntityTrace.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe);
    }
    
    public boolean noTrace() {
        if (this.pickaxeOnly.getValue()) {
            return this.isEnabled() && this.isHoldingPickaxe;
        }
        return this.isEnabled();
    }
}
