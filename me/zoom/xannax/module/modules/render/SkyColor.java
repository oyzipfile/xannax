// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class SkyColor extends Module
{
    Setting.Integer r;
    Setting.Integer g;
    Setting.Integer b;
    Setting.Boolean rainbow;
    
    public SkyColor() {
        super("SkyColor", "SkyColor", Category.Render);
        this.r = this.registerInteger("Red", "Red", 255, 0, 255);
        this.g = this.registerInteger("Green", "Green", 0, 0, 255);
        this.b = this.registerInteger("Blue", "Blue", 255, 0, 255);
        this.rainbow = this.registerBoolean("Rainbow", "Rainbow", true);
    }
    
    @Override
    public void onUpdate() {
        if (this.isEnabled()) {
            MinecraftForge.EVENT_BUS.register((Object)this);
        }
        else {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
        }
    }
    
    @SubscribeEvent
    public void fogColors(final EntityViewRenderEvent.FogColors event) {
        event.setRed(this.r.getValue() / 255.0f);
        event.setGreen(this.g.getValue() / 255.0f);
        event.setBlue(this.b.getValue() / 255.0f);
    }
    
    @SubscribeEvent
    public void fogDensity(final EntityViewRenderEvent.FogDensity event) {
        event.setDensity(0.0f);
        event.setCanceled(true);
    }
    
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
}
