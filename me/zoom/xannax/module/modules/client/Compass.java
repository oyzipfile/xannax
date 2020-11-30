// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.client;

import me.zoom.xannax.Xannax;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.util.math.MathHelper;
import me.zoom.xannax.util.Wrapper;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.Color;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Compass extends Module
{
    Setting.Double scale;
    Color c;
    private static final double HALF_PI = 1.5707963267948966;
    ScaledResolution resolution;
    
    public Compass() {
        super("Compass", "Compass", Category.Client);
        this.resolution = new ScaledResolution(Compass.mc);
    }
    
    @Override
    public void setup() {
        this.scale = this.registerDouble("Scale", "Scale", 3.0, 1.0, 5.0);
    }
    
    @Override
    public void onRender() {
        final double centerX = this.resolution.getScaledWidth() * 1.11;
        final double centerY = this.resolution.getScaledHeight_double() * 1.8;
        for (final Direction dir : Direction.values()) {
            final double rad = getPosOnCompass(dir);
            this.drawStringWithShadow(dir.name(), (int)(centerX + this.getX(rad)), (int)(centerY + this.getY(rad)), (dir == Direction.N) ? new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue(), 255).getRGB() : new Color(255, 255, 255, 255).getRGB());
        }
    }
    
    private double getX(final double rad) {
        return Math.sin(rad) * (this.scale.getValue() * 10.0);
    }
    
    private double getY(final double rad) {
        final double epicPitch = MathHelper.clamp(Wrapper.getRenderEntity().rotationPitch + 30.0f, -90.0f, 90.0f);
        final double pitchRadians = Math.toRadians(epicPitch);
        return Math.cos(rad) * Math.sin(pitchRadians) * (this.scale.getValue() * 10.0);
    }
    
    private static double getPosOnCompass(final Direction dir) {
        final double yaw = Math.toRadians(MathHelper.wrapDegrees(Wrapper.getRenderEntity().rotationYaw));
        final int index = dir.ordinal();
        return yaw + index * 1.5707963267948966;
    }
    
    private void drawStringWithShadow(final String text, final int x, final int y, final int color) {
        if (ModuleManager.isModuleEnabled("CustomFont")) {
            Xannax.fontRenderer.drawStringWithShadow(text, x, y, color);
        }
        else {
            Compass.mc.fontRenderer.drawStringWithShadow(text, (float)x, (float)y, color);
        }
    }
    
    private enum Direction
    {
        N, 
        W, 
        S, 
        E;
    }
}
