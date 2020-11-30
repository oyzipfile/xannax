// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import java.awt.Color;
import me.zoom.xannax.module.modules.client.HUD;

public class RainbowUtil
{
    public static int y;
    
    public static int rainbow(final int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360.0;
        return Color.getHSBColor((float)(rainbowState / 360.0), HUD.arraySat.getValue() / 100.0f, HUD.arrayBri.getValue() / 100.0f).getRGB();
    }
    
    public static Color rainbowColor(final int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360.0;
        return Color.getHSBColor((float)(rainbowState / 360.0), 0.8f, 0.7f);
    }
    
    public int color(final int index, final int count) {
        final float[] hsb = new float[3];
        Color.RGBtoHSB(255, 255, 255, hsb);
        float brightness = Math.abs((this.getOffset() + index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.4f * brightness;
        hsb[2] = brightness % 1.0f;
        return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }
    
    private float getOffset() {
        return System.currentTimeMillis() % 2000L / 1000.0f;
    }
}
