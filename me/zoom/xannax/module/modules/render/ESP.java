// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class ESP extends Module
{
    public static Setting.Mode mode;
    public static Setting.Double width;
    public static Setting.Double Cwidth;
    public static Setting.Integer redd;
    public static Setting.Integer greenn;
    public static Setting.Integer bluee;
    public static Setting.Integer Credd;
    public static Setting.Integer Cgreenn;
    public static Setting.Integer Cbluee;
    public static Setting.Boolean self;
    public static Setting.Boolean crystal;
    public static Setting.Boolean rainbow;
    public static Setting.Integer saturation;
    public static Setting.Integer brightness;
    public static Setting.Integer speed;
    public static Setting.Boolean Crainbow;
    public static Setting.Integer Csaturation;
    public static Setting.Integer Cbrightness;
    public static Setting.Integer Cspeed;
    public static Setting.Boolean player;
    
    public ESP() {
        super("ESP", "ESP", Category.Render);
    }
    
    @Override
    public void setup() {
        ESP.width = this.registerDouble("Width", "Width", 3.0, 0.1, 10.0);
        ESP.Cwidth = this.registerDouble("C Width", "CWidth", 3.0, 0.1, 10.0);
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("OutLine");
        modes.add("WireFrame");
        ESP.mode = this.registerMode("RenderMode", "RenderMode", modes, "WireFrame");
        ESP.self = this.registerBoolean("Self", "Self", false);
        ESP.player = this.registerBoolean("Player", "Player", true);
        ESP.crystal = this.registerBoolean("Crystal", "Crystal", false);
        ESP.redd = this.registerInteger("Red", "RedESP", 255, 0, 255);
        ESP.greenn = this.registerInteger("Green", "GreenESP", 255, 0, 255);
        ESP.bluee = this.registerInteger("Blue", "BlueESP", 255, 0, 255);
        ESP.rainbow = this.registerBoolean("Rainbow", "Rainbow", false);
        ESP.saturation = this.registerInteger("Saturation", "Saturation", 50, 0, 100);
        ESP.brightness = this.registerInteger("Brightness", "Brightness", 50, 0, 100);
        ESP.speed = this.registerInteger("Speed", "Speed", 50, 1, 100);
        ESP.Credd = this.registerInteger("CRed", "CRedESP", 255, 0, 255);
        ESP.Cgreenn = this.registerInteger("C Green", "CGreenESP", 255, 0, 255);
        ESP.Cbluee = this.registerInteger("C Blue", "CBlueESP", 255, 0, 255);
        ESP.Crainbow = this.registerBoolean("C Rainbow", "CRainbow", false);
        ESP.Csaturation = this.registerInteger("C Saturation", "CSaturation", 50, 0, 100);
        ESP.Cbrightness = this.registerInteger("C Brightness", "CBrightness", 50, 0, 100);
        ESP.Cspeed = this.registerInteger("C Speed", "CSpeed", 50, 1, 100);
    }
}
