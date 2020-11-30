// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class EnchantColor extends Module
{
    public static Setting.Integer red;
    public static Setting.Integer green;
    public static Setting.Integer blue;
    
    public EnchantColor() {
        super("EnchantColor", "Changes the color of the enchantment effect", Category.Render);
        EnchantColor.red = this.registerInteger("Red", "Red", 255, 0, 255);
        EnchantColor.green = this.registerInteger("Green", "Green", 255, 0, 255);
        EnchantColor.blue = this.registerInteger("Blue", "Blue", 255, 0, 255);
    }
}
