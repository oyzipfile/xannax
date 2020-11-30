// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class HandColor extends Module
{
    public static Setting.Integer redh;
    public static Setting.Integer greenh;
    public static Setting.Integer blueh;
    public static Setting.Integer alphah;
    public static Setting.Boolean rainbow;
    public static Setting.Integer saturation;
    public static Setting.Integer brightness;
    public static Setting.Integer speed;
    
    public HandColor() {
        super("HandColor", "HandColor", Category.Render);
    }
    
    @Override
    public void setup() {
        HandColor.redh = this.registerInteger("Red", "RedH", 255, 0, 255);
        HandColor.greenh = this.registerInteger("Green", "GreenH", 255, 0, 255);
        HandColor.blueh = this.registerInteger("Blue", "BlueH", 255, 0, 255);
        HandColor.alphah = this.registerInteger("Alpha", "AlphaH", 50, 0, 255);
        HandColor.rainbow = this.registerBoolean("Rainbow", "Rainbow", false);
        HandColor.saturation = this.registerInteger("Saturation", "Saturation", 50, 0, 100);
        HandColor.brightness = this.registerInteger("Brightness", "Brightness", 50, 0, 100);
        HandColor.speed = this.registerInteger("Speed", "Speed", 50, 1, 100);
    }
}
