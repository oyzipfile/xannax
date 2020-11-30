// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Chams extends Module
{
    public static Setting.Integer red;
    public static Setting.Integer green;
    public static Setting.Integer blue;
    public static Setting.Integer alpha;
    public static Setting.Integer Cred;
    public static Setting.Integer Cgreen;
    public static Setting.Integer Cblue;
    public static Setting.Integer Calpha;
    public static Setting.Boolean crystal;
    public static Setting.Boolean players;
    public static Setting.Boolean rainbow;
    public static Setting.Integer saturation;
    public static Setting.Integer brightness;
    public static Setting.Integer speed;
    public static Setting.Boolean Crainbow;
    public static Setting.Integer Csaturation;
    public static Setting.Integer Cbrightness;
    public static Setting.Integer Cspeed;
    public static Setting.Boolean lines;
    public static Setting.Integer width;
    
    public Chams() {
        super("Chams", "Chams", Category.Render);
    }
    
    @Override
    public void setup() {
        Chams.players = this.registerBoolean("Players", "Players", false);
        Chams.crystal = this.registerBoolean("Crystal", "Crystal", false);
        Chams.lines = this.registerBoolean("Lines", "Lines", false);
        Chams.width = this.registerInteger("Width", "Width", 1, 0, 10);
        Chams.red = this.registerInteger("Red", "Red", 255, 0, 255);
        Chams.green = this.registerInteger("Green", "Green", 255, 0, 255);
        Chams.blue = this.registerInteger("Blue", "Blue", 255, 0, 255);
        Chams.rainbow = this.registerBoolean("Rainbow", "Rainbow", false);
        Chams.saturation = this.registerInteger("Saturation", "Saturation", 50, 0, 100);
        Chams.brightness = this.registerInteger("Brightness", "Brightness", 50, 0, 100);
        Chams.speed = this.registerInteger("Speed", "Speed", 50, 1, 100);
        Chams.alpha = this.registerInteger("Alpha", "Alpha", 50, 0, 255);
        Chams.Cred = this.registerInteger("C Red", "CRed", 255, 0, 255);
        Chams.Cgreen = this.registerInteger("C Green", "CGreen", 255, 0, 255);
        Chams.Cblue = this.registerInteger("C Blue", "CBlue", 255, 0, 255);
        Chams.Crainbow = this.registerBoolean("C Rainbow", "CRainbow", false);
        Chams.Csaturation = this.registerInteger("C Saturation", "CSaturation", 50, 0, 100);
        Chams.Cbrightness = this.registerInteger("C Brightness", "CBrightness", 50, 0, 100);
        Chams.Cspeed = this.registerInteger("C Speed", "CSpeed", 50, 1, 100);
        Chams.Calpha = this.registerInteger("C Alpha", "CAlpha", 50, 0, 255);
    }
}
