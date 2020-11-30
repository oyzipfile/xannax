// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import net.minecraft.util.EnumHand;
import java.util.Iterator;
import me.zoom.xannax.util.MathUtil;
import me.zoom.xannax.util.friend.Friends;
import net.minecraft.entity.Entity;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class KillAura extends Module
{
    Setting.Double range;
    Setting.Boolean criticals;
    Setting.Boolean rotate;
    Setting.Mode aimMode;
    Setting.Boolean toggleMsg;
    boolean rotating;
    public static EntityPlayer target;
    
    public KillAura() {
        super("KillAura", "KillAura", Category.Combat);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> aimModes = new ArrayList<String>();
        aimModes.add("Leg");
        this.aimMode = this.registerMode("Mode", "Mode", aimModes, "Leg");
        final boolean swordOnly = true;
        final boolean caCheck = true;
        final boolean tpsSync = false;
        final boolean isAttacking = false;
        this.range = this.registerDouble("Range", "Range", 4.5, 0.0, 10.0);
        this.criticals = this.registerBoolean("Criticals", "Criticals", true);
        this.rotate = this.registerBoolean("Rotate", "Rotate", true);
        this.toggleMsg = this.registerBoolean("ToggleMSG", "ToggleMSG", true);
    }
    
    public void onDisable() {
        this.rotating = false;
        if (this.toggleMsg.getValue() && KillAura.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.RED + "Killaura has been toggled off!");
        }
    }
    
    public void onEnable() {
        if (this.toggleMsg.getValue() && KillAura.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.GREEN + "Killaura has been toggled on!");
        }
    }
    
    @Override
    public void onUpdate() {
        if (KillAura.mc.player != null || KillAura.mc.world != null) {
            for (final EntityPlayer player : KillAura.mc.world.playerEntities) {
                if (player != KillAura.mc.player) {
                    if (KillAura.mc.player.getDistance((Entity)player) < this.range.getValue()) {
                        if (Friends.isFriend(player.getName())) {
                            return;
                        }
                        if (player.isDead || player.getHealth() > 0.0f) {
                            if (this.rotating && this.rotate.getValue()) {
                                final float[] angle = MathUtil.calcAngle(KillAura.mc.player.getPositionEyes(KillAura.mc.getRenderPartialTicks()), player.getPositionVector());
                                KillAura.mc.player.rotationYaw = angle[0];
                                final String value = this.aimMode.getValue();
                                switch (value) {
                                    case "Leg": {
                                        KillAura.mc.player.rotationPitch = angle[1];
                                        break;
                                    }
                                }
                            }
                            this.attackPlayer(player);
                        }
                        KillAura.target = player;
                    }
                    else {
                        this.rotating = false;
                    }
                }
            }
        }
    }
    
    public void attackPlayer(final EntityPlayer player) {
        if (player != null) {
            if (player != KillAura.mc.player && KillAura.mc.player.getCooledAttackStrength(0.0f) >= 1.0f) {
                this.rotating = true;
                KillAura.mc.playerController.attackEntity((EntityPlayer)KillAura.mc.player, (Entity)player);
                KillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
        else {
            this.rotating = false;
        }
    }
}
