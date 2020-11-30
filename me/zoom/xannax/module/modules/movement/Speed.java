// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Objects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import me.zoom.xannax.util.EntityUtil;
import me.zoom.xannax.event.events.MoveEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.zoom.xannax.event.events.UpdateWalkingPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Speed extends Module
{
    Setting.Mode mode;
    Setting.Boolean limiter;
    Setting.Boolean limiter2;
    Setting.Integer specialMoveSpeed;
    Setting.Integer potionSpeed;
    Setting.Integer potionSpeed2;
    Setting.Integer acceleration;
    Setting.Boolean potion;
    Setting.Boolean step;
    Setting.Boolean toggleMsg;
    private int stage;
    private double moveSpeed;
    private double lastDist;
    private int cooldownHops;
    
    public Speed() {
        super("Speed", "AirControl etc.", Category.Movement);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("NONE");
        modes.add("NCP");
        modes.add("Strafe");
        this.mode = this.registerMode("Mode", "Mode", modes, "NCP");
        this.limiter = this.registerBoolean("SetGround", "SetGround", true);
        this.limiter2 = this.registerBoolean("Bhop", "Bhop", false);
        this.specialMoveSpeed = this.registerInteger("Speed", "Speed", 100, 0, 150);
        this.potionSpeed = this.registerInteger("Speed1", "Speed1", 130, 0, 150);
        this.potionSpeed2 = this.registerInteger("Speed2", "Speed2", 125, 0, 150);
        this.acceleration = this.registerInteger("Accel", "Accel", 2149, 1000, 2500);
        this.potion = this.registerBoolean("Potion", "Potion", false);
        this.step = this.registerBoolean("SetStep", "SetStep", true);
        this.toggleMsg = this.registerBoolean("ToggleMSG", "ToggleMSG", true);
        this.stage = 1;
        this.cooldownHops = 0;
    }
    
    public void onEnable() {
        this.moveSpeed = getBaseMoveSpeed();
        if (this.toggleMsg.getValue() && Speed.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.GREEN + "Speed has been toggled on!");
        }
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    public void onDisable() {
        this.moveSpeed = 0.0;
        this.stage = 2;
        if (this.toggleMsg.getValue() && Speed.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.RED + "Speed has been toggled off!");
        }
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0) {
            this.lastDist = Math.sqrt((Speed.mc.player.posX - Speed.mc.player.prevPosX) * (Speed.mc.player.posX - Speed.mc.player.prevPosX) + (Speed.mc.player.posZ - Speed.mc.player.prevPosZ) * (Speed.mc.player.posZ - Speed.mc.player.prevPosZ));
        }
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (event.getStage() != 0) {
            return;
        }
        if (this.mode.getValue().equalsIgnoreCase("NCP")) {
            this.doNCP(event);
        }
        else if (this.mode.getValue().equalsIgnoreCase("Strafe")) {
            float moveForward = Speed.mc.player.movementInput.moveForward;
            float moveStrafe = Speed.mc.player.movementInput.moveStrafe;
            float rotationYaw = Speed.mc.player.rotationYaw;
            if (this.limiter2.getValue() && Speed.mc.player.onGround) {
                this.stage = 2;
            }
            if (this.limiter.getValue() && round(Speed.mc.player.posY - (int)Speed.mc.player.posY, 3) == round(0.138, 3)) {
                final EntityPlayerSP player3;
                final EntityPlayerSP player = player3 = Speed.mc.player;
                player3.motionY -= 0.13;
                event.setY(event.getY() - 0.13);
                final EntityPlayerSP player4;
                final EntityPlayerSP player2 = player4 = Speed.mc.player;
                player4.posY -= 0.13;
            }
            if (this.stage == 1 && EntityUtil.isMoving()) {
                this.stage = 2;
                this.moveSpeed = this.getMultiplier() * getBaseMoveSpeed() - 0.01;
            }
            else if (this.stage == 2) {
                this.stage = 3;
                if (EntityUtil.isMoving()) {
                    event.setY(Speed.mc.player.motionY = 0.4);
                    if (this.cooldownHops > 0) {
                        --this.cooldownHops;
                    }
                    this.moveSpeed *= this.acceleration.getValue() / 1000.0;
                }
            }
            else if (this.stage == 3) {
                this.stage = 4;
                final double difference = 0.66 * (this.lastDist - getBaseMoveSpeed());
                this.moveSpeed = this.lastDist - difference;
            }
            else {
                if (Speed.mc.world.getCollisionBoxes((Entity)Speed.mc.player, Speed.mc.player.getEntityBoundingBox().offset(0.0, Speed.mc.player.motionY, 0.0)).size() > 0 || Speed.mc.player.collidedVertically) {
                    this.stage = 1;
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
            }
            this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                event.setX(0.0);
                event.setZ(0.0);
                this.moveSpeed = 0.0;
            }
            else if (moveForward != 0.0f) {
                if (moveStrafe >= 1.0f) {
                    rotationYaw += ((moveForward > 0.0f) ? -45.0f : 45.0f);
                    moveStrafe = 0.0f;
                }
                else if (moveStrafe <= -1.0f) {
                    rotationYaw += ((moveForward > 0.0f) ? 45.0f : -45.0f);
                    moveStrafe = 0.0f;
                }
                if (moveForward > 0.0f) {
                    moveForward = 1.0f;
                }
                else if (moveForward < 0.0f) {
                    moveForward = -1.0f;
                }
            }
            final double motionX = Math.cos(Math.toRadians(rotationYaw + 90.0f));
            final double motionZ = Math.sin(Math.toRadians(rotationYaw + 90.0f));
            if (this.cooldownHops == 0) {
                event.setX(moveForward * this.moveSpeed * motionX + moveStrafe * this.moveSpeed * motionZ);
                event.setZ(moveForward * this.moveSpeed * motionZ - moveStrafe * this.moveSpeed * motionX);
            }
            if (this.step.getValue()) {
                Speed.mc.player.stepHeight = 0.6f;
            }
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                event.setX(0.0);
                event.setZ(0.0);
            }
        }
    }
    
    private void doNCP(final MoveEvent event) {
        if (!this.limiter.getValue() && Speed.mc.player.onGround) {
            this.stage = 2;
        }
        switch (this.stage) {
            case 0: {
                ++this.stage;
                this.lastDist = 0.0;
                break;
            }
            case 2: {
                double motionY = 0.40123128;
                if ((Speed.mc.player.moveForward != 0.0f || Speed.mc.player.moveStrafing != 0.0f) && Speed.mc.player.onGround) {
                    if (Speed.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                        motionY += (Speed.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
                    }
                    event.setY(Speed.mc.player.motionY = motionY);
                    this.moveSpeed *= 2.149;
                    break;
                }
                break;
            }
            case 3: {
                this.moveSpeed = this.lastDist - 0.76 * (this.lastDist - getBaseMoveSpeed());
                break;
            }
            default: {
                if (((this.limiter2.getValue() && Speed.mc.world.getCollisionBoxes((Entity)Speed.mc.player, Speed.mc.player.getEntityBoundingBox().offset(0.0, Speed.mc.player.motionY, 0.0)).size() > 0) || Speed.mc.player.collidedVertically) && this.stage > 0) {
                    this.stage = ((Speed.mc.player.moveForward != 0.0f || Speed.mc.player.moveStrafing != 0.0f) ? 1 : 0);
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                break;
            }
        }
        this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
        double forward = Speed.mc.player.movementInput.moveForward;
        double strafe = Speed.mc.player.movementInput.moveStrafe;
        final double yaw = Speed.mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else if (forward != 0.0 && strafe != 0.0) {
            forward *= Math.sin(0.7853981633974483);
            strafe *= Math.cos(0.7853981633974483);
        }
        event.setX((forward * this.moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99);
        event.setZ((forward * this.moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * this.moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99);
        ++this.stage;
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.272;
        if (Speed.mc.player.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = Objects.requireNonNull(Speed.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * amplifier;
        }
        return baseSpeed;
    }
    
    private float getMultiplier() {
        float baseSpeed = (float)this.specialMoveSpeed.getValue();
        if (this.potion.getValue() && Speed.mc.player.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = Objects.requireNonNull(Speed.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 1;
            if (amplifier >= 2) {
                baseSpeed = (float)this.potionSpeed2.getValue();
            }
            else {
                baseSpeed = (float)this.potionSpeed.getValue();
            }
        }
        return baseSpeed / 100.0f;
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        final BigDecimal bigDecimal = new BigDecimal(value).setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
    
    @Override
    public String getHudInfo() {
        String t = "";
        t = "[" + ChatFormatting.WHITE + this.mode.getValue() + ChatFormatting.GRAY + "]";
        return t;
    }
}
