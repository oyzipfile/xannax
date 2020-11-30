// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.util.font.FontUtils;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import java.awt.Color;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.event.events.RenderEvent;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.init.Items;
import me.zoom.xannax.util.DamageUtils;
import me.zoom.xannax.util.friend.Friends;
import me.zoom.xannax.util.CrystalUtils;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.init.MobEffects;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.server.SPacketSoundEffect;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.util.TimerUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.concurrent.ConcurrentHashMap;
import me.zoom.xannax.module.Module;

public class AutoCrystal extends Module
{
    private final ConcurrentHashMap<EntityEnderCrystal, Integer> attackedCrystals;
    private int oldSlot;
    private int newSlot;
    boolean mainhand;
    boolean offhand;
    public static EntityPlayer target;
    private boolean switchCooldown;
    private boolean isRotating;
    private boolean isAttacking;
    private boolean isPlacing;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    BlockPos render;
    BlockPos position;
    TimerUtils breakTimer;
    TimerUtils placeTimer;
    Setting.Boolean explode;
    Setting.Integer hitDelay;
    Setting.Integer breakAttempts;
    Setting.Mode handBreak;
    Setting.Double hitRange;
    Setting.Boolean antiWeakness;
    Setting.Boolean place;
    Setting.Boolean placeUnderBlock;
    Setting.Boolean autoSwitch;
    Setting.Integer placeDelay;
    Setting.Double placeRange;
    Setting.Double wallsRange;
    Setting.Boolean noSuicide;
    Setting.Boolean rotate;
    Setting.Boolean spoofRotations;
    Setting.Double minDmg;
    Setting.Double maxSelfDmg;
    Setting.Boolean cancelCrystal;
    Setting.Boolean swingExploit;
    Setting.Boolean pauseWhileEating;
    Setting.Boolean pauseWhileMining;
    Setting.Mode timer;
    Setting.Boolean toggleMsg;
    Setting.Boolean facePlace;
    Setting.Boolean facePlaceCheck;
    Setting.Double facePlaceHp;
    Setting.Boolean armorDestroy;
    Setting.Boolean armorCheck;
    Setting.Integer armorPercent;
    Setting.Integer checkValue;
    Setting.Boolean renderPlacement;
    Setting.Boolean renderCustomFont;
    Setting.Boolean renderRainbow;
    Setting.Mode renderMode;
    Setting.Boolean renderFill;
    Setting.Boolean renderOutline;
    Setting.Boolean customOutline;
    Setting.Boolean renderDamage;
    Setting.Integer fillRed;
    Setting.Integer fillGreen;
    Setting.Integer fillBlue;
    Setting.Integer fillAlpha;
    Setting.Double lineWidth;
    Setting.Integer outlineRed;
    Setting.Integer outlineGreen;
    Setting.Integer outlineBlue;
    Setting.Integer outlineAlpha;
    Setting.Integer saturation;
    Setting.Integer brightness;
    Setting.Integer speed;
    @EventHandler
    private Listener<PacketEvent.Send> packetSendListener;
    @EventHandler
    private Listener<PacketEvent.Receive> packetReceiveListener;
    
    public AutoCrystal() {
        super("AutoCrystal", "AutoCrystal", Category.Combat);
        this.attackedCrystals = new ConcurrentHashMap<EntityEnderCrystal, Integer>();
        this.oldSlot = -1;
        this.newSlot = -1;
        this.mainhand = false;
        this.offhand = false;
        this.switchCooldown = false;
        this.isRotating = false;
        this.isAttacking = false;
        this.isPlacing = false;
        this.position = null;
        this.breakTimer = new TimerUtils();
        this.placeTimer = new TimerUtils();
        final Packet packet;
        this.packetSendListener = new Listener<PacketEvent.Send>(event -> {
            packet = event.getPacket();
            if (packet instanceof CPacketPlayer && this.spoofRotations.getValue() && AutoCrystal.isSpoofingAngles) {
                ((CPacketPlayer)packet).yaw = (float)AutoCrystal.yaw;
                ((CPacketPlayer)packet).pitch = (float)AutoCrystal.pitch;
            }
            return;
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
        SPacketSoundEffect packet2;
        final Iterator<Entity> iterator;
        Entity e;
        this.packetReceiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (event.getPacket() instanceof SPacketSoundEffect) {
                packet2 = (SPacketSoundEffect)event.getPacket();
                if (packet2.getCategory() == SoundCategory.BLOCKS && packet2.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    AutoCrystal.mc.world.loadedEntityList.iterator();
                    while (iterator.hasNext()) {
                        e = iterator.next();
                        if (e instanceof EntityEnderCrystal && e.getDistance(packet2.getX(), packet2.getY(), packet2.getZ()) <= 6.0) {
                            e.setDead();
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> logics = new ArrayList<String>();
        logics.add("Breakplace");
        logics.add("Placebreak");
        final ArrayList<String> hands = new ArrayList<String>();
        hands.add("Mainhand");
        hands.add("Offhand");
        hands.add("Both");
        this.explode = this.registerBoolean("Explode", "Explode", true);
        this.hitDelay = this.registerInteger("HitDelay", "HitDelay", 0, 0, 1000);
        this.breakAttempts = this.registerInteger("Attempts", "Attempts", 1, 1, 6);
        this.handBreak = this.registerMode("Hand", "Hand", hands, "Mainhand");
        this.hitRange = this.registerDouble("HitRange", "HitRange", 5.0, 0.0, 10.0);
        this.antiWeakness = this.registerBoolean("AntiWeakness", "AntiWeakness", true);
        this.place = this.registerBoolean("Place", "Place", true);
        this.placeUnderBlock = this.registerBoolean("PlaceUnderBlock", "PlaceUnderBlock", false);
        this.autoSwitch = this.registerBoolean("AutoSwitch", "AutoSwitch", true);
        this.placeDelay = this.registerInteger("PlaceDelay", "PlaceDelay", 0, 0, 1000);
        this.placeRange = this.registerDouble("PlaceRange", "PlaceRange", 5.0, 0.0, 10.0);
        this.wallsRange = this.registerDouble("WallsRange", "WallsRange", 3.5, 0.0, 10.0);
        this.noSuicide = this.registerBoolean("NoSuicide", "NoSuicide", true);
        this.rotate = this.registerBoolean("Rotate", "Rotate", true);
        this.spoofRotations = this.registerBoolean("SpoofRotations", "SpoofRotations", true);
        this.minDmg = this.registerDouble("MinDMG", "MinDMG", 5.0, 0.0, 20.0);
        this.maxSelfDmg = this.registerDouble("MaxSelfDMG", "MaxSelfDMG", 10.0, 0.0, 36.0);
        this.cancelCrystal = this.registerBoolean("Cancel", "Cancel", true);
        this.swingExploit = this.registerBoolean("SwingExploit", "SwingExploit", true);
        this.pauseWhileEating = this.registerBoolean("PauseWhileEating", "PauseWhileEating", true);
        this.pauseWhileMining = this.registerBoolean("PauseWhileMining", "PauseWhileMining", true);
        this.timer = this.registerMode("Timer", "Timer", logics, "Breakplace");
        this.toggleMsg = this.registerBoolean("ToggleMSG", "ToggleMSG", true);
        this.facePlace = this.registerBoolean("Faceplace", "Faceplace", true);
        this.facePlaceCheck = this.registerBoolean("FaceplaceCheck", "FaceplaceCheck", true);
        this.facePlaceHp = this.registerDouble("FaceplaceHP", "FaceplaceHP", 12.0, 0.0, 36.0);
        this.armorDestroy = this.registerBoolean("ArmorDestroy", "ArmorDestroy", true);
        this.armorCheck = this.registerBoolean("ArmorCheck", "ArmorCheck", true);
        this.armorPercent = this.registerInteger("ArmorPercent", "ArmorPercent", 25, 0, 100);
        this.checkValue = this.registerInteger("CheckValue", "CheckValue", 30, 0, 100);
        this.renderPlacement = this.registerBoolean("Render", "Render", true);
        this.renderCustomFont = this.registerBoolean("CustomFont", "CustomFont", true);
        this.renderRainbow = this.registerBoolean("Rainbow", "Rainbow", false);
        this.saturation = this.registerInteger("Saturation", "Saturation", 50, 0, 100);
        this.brightness = this.registerInteger("Brightness", "Brightness", 50, 0, 100);
        this.speed = this.registerInteger("Speed", "Speed", 50, 1, 100);
        this.renderFill = this.registerBoolean("Fill", "Fill", true);
        this.renderOutline = this.registerBoolean("Outline", "Outline", true);
        this.customOutline = this.registerBoolean("CustomOutline", "CustomOutline", true);
        this.renderDamage = this.registerBoolean("RenderDamage", "RenderDamage", true);
        this.fillRed = this.registerInteger("FillRed", "FillRed", 186, 0, 255);
        this.fillGreen = this.registerInteger("FillGreen", "FillGreen", 85, 0, 255);
        this.fillBlue = this.registerInteger("FillBlue", "FillBlue", 211, 0, 255);
        this.fillAlpha = this.registerInteger("FillAlpha", "FillAlpha", 53, 0, 255);
        this.lineWidth = this.registerDouble("LineWidth", "LineWidth", 1.5, 0.0, 5.0);
        this.outlineRed = this.registerInteger("OutlineRed", "OutlineRed", 255, 0, 255);
        this.outlineGreen = this.registerInteger("OutlineGreen", "OutlineGreen", 255, 0, 255);
        this.outlineBlue = this.registerInteger("OutlineBlue", "OutlineBlue", 255, 0, 255);
        this.outlineAlpha = this.registerInteger("OutlineAlpha", "OutlineAlpha", 255, 0, 255);
    }
    
    @Override
    public void onUpdate() {
        this.doAutoCrystal();
    }
    
    public void doAutoCrystal() {
        final String value = this.timer.getValue();
        switch (value) {
            case "Breakplace": {
                this.explodeCrystal();
                this.placeCrystal();
                break;
            }
            case "Placebreak": {
                this.placeCrystal();
                this.explodeCrystal();
                break;
            }
        }
    }
    
    public void explodeCrystal() {
        final EntityEnderCrystal crystal = this.getBestCrystal();
        if (this.explode.getValue() && crystal != null && AutoCrystal.mc.player.getDistance((Entity)crystal) <= this.hitRange.getValue()) {
            if (this.breakTimer.passedMs(this.hitDelay.getValue())) {
                if (this.antiWeakness.getValue() && AutoCrystal.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    if (!this.isAttacking) {
                        this.oldSlot = AutoCrystal.mc.player.inventory.currentItem;
                        this.isAttacking = true;
                    }
                    for (int i = 0; i < 9; ++i) {
                        final ItemStack stack = AutoCrystal.mc.player.inventory.getStackInSlot(i);
                        if (stack != ItemStack.EMPTY) {
                            if (stack.getItem() instanceof ItemSword) {
                                this.newSlot = i;
                                break;
                            }
                            if (stack.getItem() instanceof ItemTool) {
                                this.newSlot = i;
                                break;
                            }
                        }
                    }
                    if (this.newSlot != -1) {
                        AutoCrystal.mc.player.inventory.currentItem = this.newSlot;
                        this.switchCooldown = true;
                    }
                }
                this.rotateToPos(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)AutoCrystal.mc.player);
                for (int i = 0; i < this.breakAttempts.getValue(); ++i) {
                    AutoCrystal.mc.playerController.attackEntity((EntityPlayer)AutoCrystal.mc.player, (Entity)crystal);
                }
                this.addAttackedCrystal(crystal);
                this.getSwingingHand(crystal);
                if (this.cancelCrystal.getValue()) {
                    crystal.setDead();
                }
                this.breakTimer.reset();
                this.isAttacking = false;
            }
        }
        else {
            resetRotation();
            if (this.oldSlot != -1) {
                AutoCrystal.mc.player.inventory.currentItem = this.oldSlot;
                this.oldSlot = -1;
            }
            this.isAttacking = false;
        }
    }
    
    public void placeCrystal() {
        double damage = 0.5;
        this.mainhand = (AutoCrystal.mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal);
        this.offhand = (AutoCrystal.mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal);
        final List<BlockPos> possiblePositions = CrystalUtils.possiblePlacePositions((float)this.placeRange.getValue(), this.placeUnderBlock.getValue(), true);
        for (final EntityPlayer entity : AutoCrystal.mc.world.playerEntities) {
            if (Friends.isFriend(entity.getName())) {
                continue;
            }
            if (entity == AutoCrystal.mc.player) {
                continue;
            }
            if (!(entity instanceof EntityPlayer)) {
                continue;
            }
            if (entity.getDistance((Entity)AutoCrystal.mc.player) >= 11.0f) {
                continue;
            }
            if (entity.isDead) {
                continue;
            }
            if (entity.getHealth() <= 0.0f) {
                continue;
            }
            for (final BlockPos blockPos : possiblePositions) {
                final double targetDamage = DamageUtils.calculateDamage(blockPos, (Entity)entity);
                final double selfDamage = DamageUtils.calculateDamage(blockPos, (Entity)AutoCrystal.mc.player);
                final double maximumSelfDamage = this.maxSelfDmg.getValue();
                final boolean noPlace = this.facePlaceCheck.getValue() && AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                double minimumDamage;
                if (this.facePlace.getValue() && entity.getHealth() + entity.getAbsorptionAmount() <= this.facePlaceHp.getValue() && !noPlace && !this.shouldActionArmor((EntityPlayer)AutoCrystal.mc.player, true)) {
                    minimumDamage = 2.0;
                }
                else if (this.shouldActionArmor(entity, false) && !noPlace && !this.shouldActionArmor((EntityPlayer)AutoCrystal.mc.player, true)) {
                    minimumDamage = 2.0;
                }
                else {
                    minimumDamage = this.minDmg.getValue();
                }
                if (targetDamage > minimumDamage && maximumSelfDamage > selfDamage) {
                    if (targetDamage <= damage) {
                        continue;
                    }
                    damage = targetDamage;
                    this.position = blockPos;
                    AutoCrystal.target = entity;
                }
            }
        }
        if (damage == 0.5) {
            this.render = null;
            return;
        }
        if (this.place.getValue() && (this.offhand || (this.mainhand && this.placeTimer.passedMs(this.placeDelay.getValue())))) {
            final boolean shouldPauseEating = this.pauseWhileEating.getValue() && this.isEatingGap();
            final boolean shouldPauseMining = this.pauseWhileMining.getValue() && this.isHittingBlock();
            this.render = this.position;
            final RayTraceResult result = AutoCrystal.mc.world.rayTraceBlocks(new Vec3d(AutoCrystal.mc.player.posX, AutoCrystal.mc.player.posY + AutoCrystal.mc.player.getEyeHeight(), AutoCrystal.mc.player.posZ), new Vec3d(this.position.getX() + 0.5, this.position.getY() - 0.5, this.position.getZ() + 0.5));
            final EnumFacing facing = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
            this.isPlacing = true;
            AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.position, facing, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.isPlacing = false;
        }
    }
    
    private boolean isEatingGap() {
        return AutoCrystal.mc.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold && AutoCrystal.mc.player.isHandActive();
    }
    
    private boolean isHittingBlock() {
        return AutoCrystal.mc.playerController.isHittingBlock;
    }
    
    private int findCrystalsHotbar() {
        for (int i = 0; i < 9; ++i) {
            if (AutoCrystal.mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean shouldActionArmor(final EntityPlayer player, final boolean safe) {
        if (safe) {
            for (final ItemStack stack : player.getArmorInventoryList()) {
                if (stack == null || stack.getItem() == Items.AIR) {
                    return true;
                }
                final float percentArmor = (stack.getMaxDamage() - stack.getItemDamage()) / (float)stack.getMaxDamage() * 100.0f;
                if (this.armorCheck.getValue() && this.checkValue.getValue() >= percentArmor) {
                    return true;
                }
            }
        }
        else {
            for (final ItemStack stack : player.getArmorInventoryList()) {
                if (stack == null || stack.getItem() == Items.AIR) {
                    return true;
                }
                final float percentArmor = (stack.getMaxDamage() - stack.getItemDamage()) / (float)stack.getMaxDamage() * 100.0f;
                if (this.armorDestroy.getValue() && this.armorPercent.getValue() >= percentArmor) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void rotateToPos(final double px, final double py, final double pz, final EntityPlayer player) {
        final double[] angle = CrystalUtils.calculateLookAt(px, py, pz, player);
        if (this.rotate.getValue()) {
            this.isRotating = true;
            setYawAndPitch((float)angle[0], (float)angle[1]);
            this.isRotating = false;
        }
    }
    
    private static void setYawAndPitch(final float yawValue, final float pitchValue) {
        AutoCrystal.yaw = yawValue;
        AutoCrystal.pitch = pitchValue;
        AutoCrystal.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (AutoCrystal.isSpoofingAngles) {
            AutoCrystal.yaw = AutoCrystal.mc.player.rotationYaw;
            AutoCrystal.pitch = AutoCrystal.mc.player.rotationPitch;
            AutoCrystal.isSpoofingAngles = false;
        }
    }
    
    public void addAttackedCrystal(final EntityEnderCrystal crystal) {
        if (this.attackedCrystals.containsKey(crystal)) {
            final int value = this.attackedCrystals.get(crystal);
            this.attackedCrystals.put(crystal, value + 1);
        }
        else {
            this.attackedCrystals.put(crystal, 1);
        }
    }
    
    public void getSwingingHand(final EntityEnderCrystal crystal) {
        if (this.handBreak.getValue().equalsIgnoreCase("Mainhand")) {
            if (this.swingExploit.getValue()) {
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
            else {
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
        else if (this.handBreak.getValue().equalsIgnoreCase("Offhand")) {
            AutoCrystal.mc.player.swingArm(EnumHand.OFF_HAND);
        }
        else if (ModuleManager.isModuleEnabled("OffhandSwing")) {
            AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
        else {
            AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
            AutoCrystal.mc.player.swingArm(EnumHand.OFF_HAND);
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null && this.renderPlacement.getValue()) {
            final Color rainbowColor1 = new Color(RenderUtil.getRainbow(this.speed.getValue() * 100, 0, this.saturation.getValue() / 100.0f, this.brightness.getValue() / 100.0f));
            final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
            final float[] hue = { System.currentTimeMillis() % 2520L / 2520.0f };
            final int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
            final int r = rgb >> 16 & 0xFF;
            final int g = rgb >> 8 & 0xFF;
            final int b = rgb & 0xFF;
            final AxisAlignedBB bb = new AxisAlignedBB(this.render.getX() - AutoCrystal.mc.getRenderManager().viewerPosX, this.render.getY() - AutoCrystal.mc.getRenderManager().viewerPosY + 1.0, this.render.getZ() - AutoCrystal.mc.getRenderManager().viewerPosZ, this.render.getX() + 1 - AutoCrystal.mc.getRenderManager().viewerPosX, this.render.getY() - AutoCrystal.mc.getRenderManager().viewerPosY, this.render.getZ() + 1 - AutoCrystal.mc.getRenderManager().viewerPosZ);
            if (AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                if (this.renderRainbow.getValue()) {
                    if (this.renderFill.getValue()) {
                        RenderUtil.drawESP(bb, (float)rainbowColor2.getRed(), (float)rainbowColor2.getGreen(), (float)rainbowColor2.getBlue(), (float)this.fillAlpha.getValue());
                    }
                    if (this.renderOutline.getValue()) {
                        if (this.customOutline.getValue()) {
                            RenderUtil.drawESPOutline(bb, (float)rainbowColor2.getRed(), (float)rainbowColor2.getGreen(), (float)rainbowColor2.getBlue(), (float)this.outlineAlpha.getValue(), 1.0f);
                        }
                        else {
                            RenderUtil.drawESPOutline(bb, (float)rainbowColor2.getRed(), (float)rainbowColor2.getGreen(), (float)rainbowColor2.getBlue(), 255.0f, 1.0f);
                        }
                    }
                    GlStateManager.enableDepth();
                    GlStateManager.depthMask(true);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    RenderHelper.disableStandardItemLighting();
                }
                else {
                    if (this.renderFill.getValue()) {
                        RenderUtil.drawESP(bb, (float)this.fillRed.getValue(), (float)this.fillGreen.getValue(), (float)this.fillBlue.getValue(), (float)this.fillAlpha.getValue());
                    }
                    if (this.renderOutline.getValue()) {
                        if (this.customOutline.getValue()) {
                            RenderUtil.drawESPOutline(bb, (float)this.outlineRed.getValue(), (float)this.outlineGreen.getValue(), (float)this.outlineBlue.getValue(), (float)this.outlineAlpha.getValue(), 1.0f);
                        }
                        else {
                            RenderUtil.drawESPOutline(bb, (float)this.fillRed.getValue(), (float)this.fillGreen.getValue(), (float)this.fillBlue.getValue(), 255.0f, 1.0f);
                        }
                    }
                    GlStateManager.enableDepth();
                    GlStateManager.depthMask(true);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    RenderHelper.disableStandardItemLighting();
                }
                if (this.renderDamage.getValue()) {
                    GlStateManager.pushMatrix();
                    RenderUtil.glBillboardDistanceScaled(this.render.getX() + 0.5f, this.render.getY() + 0.5f, this.render.getZ() + 0.5f, (EntityPlayer)AutoCrystal.mc.player, 1.0f);
                    final double damage = DamageUtils.calculateDamage(this.render.getX() + 0.5, this.render.getY() + 1, this.render.getZ() + 0.5, (Entity)AutoCrystal.target);
                    final String damageText = ((Math.floor(damage) == damage) ? Integer.valueOf((int)damage) : String.format("%.1f", damage)) + "";
                    GlStateManager.disableDepth();
                    GlStateManager.translate(-(AutoCrystal.mc.fontRenderer.getStringWidth(damageText) / 2.0), 0.0, 0.0);
                    FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), damageText, 0, 0, new Color(140, 140, 140, 255).getRGB());
                    GlStateManager.popMatrix();
                }
            }
        }
    }
    
    public EntityEnderCrystal getBestCrystal() {
        double bestDamage = 0.0;
        final double maximumDamageSelf = this.maxSelfDmg.getValue();
        EntityEnderCrystal bestCrystal = null;
        for (final Entity c : AutoCrystal.mc.world.loadedEntityList) {
            if (!(c instanceof EntityEnderCrystal)) {
                continue;
            }
            final EntityEnderCrystal crystal = (EntityEnderCrystal)c;
            if (AutoCrystal.mc.player.getDistance((Entity)crystal) > (AutoCrystal.mc.player.canEntityBeSeen((Entity)crystal) ? this.hitRange.getValue() : this.wallsRange.getValue())) {
                continue;
            }
            if (crystal.isDead) {
                continue;
            }
            if (this.attackedCrystals.containsKey(crystal) && this.attackedCrystals.get(crystal) > 5) {
                continue;
            }
            for (final Entity player : AutoCrystal.mc.world.playerEntities) {
                if (player != AutoCrystal.mc.player) {
                    if (!(player instanceof EntityPlayer)) {
                        continue;
                    }
                    if (Friends.isFriend(player.getName())) {
                        continue;
                    }
                    if (player.getDistance((Entity)AutoCrystal.mc.player) >= 11.0f) {
                        continue;
                    }
                    final EntityPlayer target = (EntityPlayer)player;
                    if (target.isDead) {
                        continue;
                    }
                    if (target.getHealth() <= 0.0f) {
                        continue;
                    }
                    final boolean noBreak = this.facePlaceCheck.getValue() && AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                    double minimumDamage;
                    if (this.facePlace.getValue() && target.getHealth() + target.getAbsorptionAmount() <= this.facePlaceHp.getValue() && !noBreak) {
                        minimumDamage = 2.0;
                    }
                    else if (this.shouldActionArmor(target, false) && !noBreak && !this.shouldActionArmor((EntityPlayer)AutoCrystal.mc.player, true)) {
                        minimumDamage = 2.0;
                    }
                    else {
                        minimumDamage = this.minDmg.getValue();
                    }
                    final double targetDamage = DamageUtils.calculateDamage((Entity)crystal, (Entity)target);
                    final double selfDamage = DamageUtils.calculateDamage((Entity)crystal, (Entity)AutoCrystal.mc.player);
                    if (targetDamage < minimumDamage) {
                        continue;
                    }
                    if (selfDamage > maximumDamageSelf) {
                        continue;
                    }
                    if (this.noSuicide.getValue() && AutoCrystal.mc.player.getHealth() + AutoCrystal.mc.player.getAbsorptionAmount() - selfDamage <= 0.5) {
                        continue;
                    }
                    if (targetDamage <= bestDamage) {
                        continue;
                    }
                    bestDamage = targetDamage;
                    bestCrystal = crystal;
                }
            }
        }
        return bestCrystal;
    }
    
    public void onEnable() {
        this.attackedCrystals.clear();
        if (this.toggleMsg.getValue() && AutoCrystal.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.GREEN + "AutoCrystal has been toggled on!");
        }
    }
    
    public void onDisable() {
        this.attackedCrystals.clear();
        resetRotation();
        if (this.toggleMsg.getValue() && AutoCrystal.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.RED + "AutoCrystal has been toggled off!");
        }
    }
    
    @Override
    public String getHudInfo() {
        if (AutoCrystal.target != null) {
            return "[" + ChatFormatting.GREEN + AutoCrystal.target.getName() + ChatFormatting.GRAY + "]";
        }
        return "[" + ChatFormatting.GREEN + "No target!" + ChatFormatting.GRAY + "]";
    }
}
