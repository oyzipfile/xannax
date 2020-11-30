// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.util.MotionUtils;
import me.zoom.xannax.event.events.JumpEvent;
import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Sprint extends Module
{
    Setting.Mode Mode;
    
    public Sprint() {
        super("Sprint", "Sprint", Category.Movement);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> sprintModes = new ArrayList<String>();
        sprintModes.add("Legit");
        sprintModes.add("Rage");
        this.Mode = this.registerMode("Mode", "Mode", sprintModes, "Legit");
    }
    
    @Override
    public void onUpdate() {
        if (Sprint.mc.gameSettings.keyBindSneak.isKeyDown()) {
            Sprint.mc.player.setSprinting(false);
            return;
        }
        if (Sprint.mc.player.getFoodStats().getFoodLevel() > 6 && this.Mode.getValue().equalsIgnoreCase("Rage")) {
            if (Sprint.mc.player.moveForward == 0.0f) {
                if (Sprint.mc.player.moveStrafing == 0.0f) {
                    return;
                }
            }
        }
        else if (Sprint.mc.player.moveForward <= 0.0f) {
            return;
        }
        Sprint.mc.player.setSprinting(true);
    }
    
    public void onJump(final JumpEvent event) {
        if (this.Mode.getValue().equalsIgnoreCase("Rage")) {
            final double[] dir = MotionUtils.forward(0.01745329238474369);
            event.getLocation().setX(dir[0] * 0.20000000298023224);
            event.getLocation().setZ(dir[1] * 0.20000000298023224);
        }
    }
    
    @Override
    public String getHudInfo() {
        String t = "";
        t = "[" + ChatFormatting.WHITE + this.Mode.getValue() + ChatFormatting.GRAY + "]";
        return t;
    }
}
