// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.setting.Setting;
import net.minecraft.client.renderer.ItemRenderer;
import me.zoom.xannax.module.Module;

public class LowHands extends Module
{
    ItemRenderer itemRenderer;
    Setting.Double off;
    
    public LowHands() {
        super("LowOffhand", "LowOffhand", Category.Render);
        this.itemRenderer = LowHands.mc.entityRenderer.itemRenderer;
    }
    
    @Override
    public void setup() {
        this.off = this.registerDouble("Height", "LowOffhandHeight", 0.5, 0.0, 1.0);
    }
    
    @Override
    public void onUpdate() {
        this.itemRenderer.equippedProgressOffHand = (float)this.off.getValue();
    }
}
