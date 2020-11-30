// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Reach extends Module
{
    public static Setting.Boolean override;
    public static Setting.Double add;
    public static Setting.Double reach;
    
    public Reach() {
        super("Reach", "Reach", Category.Player);
    }
    
    @Override
    public void setup() {
        Reach.override = this.registerBoolean("Override", "Override", false);
        Reach.add = this.registerDouble("Add", "AddR", 1.0, 0.0, 3.0);
        Reach.reach = this.registerDouble("Reach", "Reach", 1.0, 0.0, 6.0);
    }
    
    @Override
    public String getHudInfo() {
        String t = "";
        if (Reach.override.getValue()) {
            t = "[" + ChatFormatting.WHITE + Reach.reach.getValue() + ChatFormatting.GRAY + "]";
        }
        else {
            t = "[" + ChatFormatting.WHITE + Reach.add.getValue() + ChatFormatting.GRAY + "]";
        }
        return t;
    }
}
