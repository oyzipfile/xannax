// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class FovSlider extends Module
{
    private final Setting.Integer FOV;
    private final Setting.Mode mode;
    private float fov;
    
    public FovSlider() {
        super("FovSlider", "Better FOV slider", Category.Render);
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("FovChanger");
        modes.add("HandChanger");
        this.FOV = this.registerInteger("FOV", "FOV", 110, 90, 200);
        this.mode = this.registerMode("Mode", "Mode", modes, "FovChanger");
    }
    
    @SubscribeEvent
    public void fovOn(final EntityViewRenderEvent.FOVModifier e) {
        if (this.mode.getValue().equalsIgnoreCase("HandChanger")) {
            e.setFOV((float)this.FOV.getValue());
        }
    }
    
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.fov = FovSlider.mc.gameSettings.fovSetting;
    }
    
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        FovSlider.mc.gameSettings.fovSetting = this.fov;
    }
    
    @Override
    public void onUpdate() {
        if (!this.isEnabled() || FovSlider.mc.world == null) {
            return;
        }
        if (this.mode.getValue().equalsIgnoreCase("FovChanger")) {
            FovSlider.mc.gameSettings.fovSetting = (float)this.FOV.getValue();
        }
    }
}
