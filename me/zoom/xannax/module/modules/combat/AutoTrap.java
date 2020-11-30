// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import me.zoom.xannax.util.EntityUtil;
import me.zoom.xannax.util.friend.Friends;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.world.GameType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import me.zoom.xannax.util.BlockInteractionHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import java.util.Collection;
import java.util.Collections;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import net.minecraft.world.World;
import me.zoom.xannax.util.MathUtil;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.event.events.RenderEvent;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import java.util.HashSet;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import me.zoom.xannax.setting.Setting;
import java.util.ArrayList;
import me.zoom.xannax.module.Module;

public class AutoTrap extends Module
{
    private ArrayList<String> options;
    Setting.Double range;
    Setting.Integer blocksPerTick;
    Setting.Integer tickDelay;
    Setting.Mode cage;
    Setting.Boolean rotate;
    Setting.Boolean noGlitchBlocks;
    Setting.Boolean activeInFreecam;
    Setting.Boolean announceUsage;
    Setting.Boolean toggleoff;
    Setting.Boolean turnOffCauras;
    Setting.Boolean esp;
    Setting.Mode mode;
    Setting.Integer red;
    Setting.Integer green;
    Setting.Integer blue;
    Setting.Integer alpha;
    Setting.Integer oalpha;
    private EntityPlayer closestTarget;
    private String lastTickTargetName;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private int delayStep;
    private boolean isSneaking;
    private int offsetStep;
    private boolean firstRun;
    private int test;
    private Set<BlockPos> placeList;
    int cDelay;
    String caura;
    boolean isDisabling;
    boolean hasDisabled;
    
    public AutoTrap() {
        super("AutoTrapX", "Automatically traps people", Category.Combat);
        final ArrayList<String> cages = new ArrayList<String>();
        cages.add("Trap");
        cages.add("TrapTop");
        cages.add("TrapFullRoof");
        cages.add("TrapFullRoofTop");
        cages.add("Crystalexa");
        cages.add("Crystal");
        cages.add("CrystalFullRoof");
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Solid");
        modes.add("Outline");
        modes.add("Full");
        this.range = this.registerDouble("Range", "Range", 4.5, 3.5, 32.0);
        this.blocksPerTick = this.registerInteger("BlocksPerTick", "BlocksPerTick", 2, 1, 23);
        this.tickDelay = this.registerInteger("TickDelay", "TickDelay", 2, 0, 10);
        this.cage = this.registerMode("Cage", "Cage", cages, "Trap");
        this.rotate = this.registerBoolean("Rotate", "Rotate", false);
        this.noGlitchBlocks = this.registerBoolean("NoGlitchBlocks", "NoGlitchBlocks", true);
        this.activeInFreecam = this.registerBoolean("ActiveInFreecam", "ActiveInFreecam", true);
        this.announceUsage = this.registerBoolean("AnnounceUsage", "AnnounceUsage", true);
        this.toggleoff = this.registerBoolean("Toggle Off", "ToggleOff", false);
        this.turnOffCauras = this.registerBoolean("Toggle Other Cauras", "ToggleOtherCauras", false);
        this.esp = this.registerBoolean("Show esp", "Showesp", false);
        this.mode = this.registerMode("Esp Mode", "EspMode", modes, "Solid");
        this.red = this.registerInteger("Red", "Red", 0, 0, 255);
        this.green = this.registerInteger("Green", "Green", 255, 0, 255);
        this.blue = this.registerInteger("Blue", "Blue", 255, 0, 255);
        this.alpha = this.registerInteger("Alpha", "Alpha", 70, 0, 255);
        this.oalpha = this.registerInteger("Outline Alpha", "OutlineAlpha", 70, 0, 255);
        this.placeList = new HashSet<BlockPos>();
        this.cDelay = 0;
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.delayStep = 0;
        this.isSneaking = false;
        this.offsetStep = 0;
    }
    
    public void onEnable() {
        this.test = 0;
        if (AutoTrapO.mc.player == null) {
            this.disable();
            return;
        }
        this.hasDisabled = false;
        this.firstRun = true;
        this.playerHotbarSlot = AutoTrapO.mc.player.inventory.currentItem;
        this.lastHotbarSlot = -1;
    }
    
    public void onDisable() {
        if (AutoTrapO.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            AutoTrapO.mc.player.inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            AutoTrapO.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoTrapO.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.placeList.clear();
        if (this.announceUsage.getValue()) {
            Command.sendClientMessage(ChatFormatting.RED.toString() + "AutoTrap has been toggle off!");
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.esp.getValue()) {
            final int color1 = this.red.getValue();
            final int color2 = this.green.getValue();
            final int color3 = this.blue.getValue();
            for (final BlockPos pos : this.placeList) {
                if (this.mode.getValue().equalsIgnoreCase("Solid")) {
                    RenderUtil.prepare(7);
                    RenderUtil.drawBox(pos, color1, color2, color3, this.alpha.getValue(), 63);
                    RenderUtil.release();
                }
                else if (this.mode.getValue().equalsIgnoreCase("Outline")) {
                    final IBlockState iBlockState2 = AutoTrapO.mc.world.getBlockState(pos);
                    final Vec3d interp2 = MathUtil.interpolateEntity((Entity)AutoTrapO.mc.player, AutoTrapO.mc.getRenderPartialTicks());
                    RenderUtil.drawBoundingBox(iBlockState2.getSelectedBoundingBox((World)AutoTrapO.mc.world, pos).grow(0.0020000000949949026).offset(-interp2.x, -interp2.y, -interp2.z), 1.5f, color1, color2, color3, this.alpha.getValue());
                }
                else {
                    if (!this.mode.getValue().equalsIgnoreCase("Full")) {
                        continue;
                    }
                    final IBlockState iBlockState3 = AutoTrapO.mc.world.getBlockState(pos);
                    final Vec3d interp3 = MathUtil.interpolateEntity((Entity)AutoTrapO.mc.player, AutoTrapO.mc.getRenderPartialTicks());
                    RenderUtil.drawFullBox(iBlockState3.getSelectedBoundingBox((World)AutoTrapO.mc.world, pos).grow(0.0020000000949949026).offset(-interp3.x, -interp3.y, -interp3.z), pos, 1.5f, color1, color2, color3, this.alpha.getValue(), this.oalpha.getValue());
                }
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.cDelay > 0) {
            --this.cDelay;
        }
        if (this.cDelay == 0 && this.isDisabling && ModuleManager.getModuleByName(this.caura) != null) {
            ModuleManager.getModuleByName(this.caura).toggle();
            this.isDisabling = false;
            this.hasDisabled = true;
        }
        if (ModuleManager.getModuleByName("AutoCrystal") != null && ModuleManager.isModuleEnabled("AutoCrystal") && this.turnOffCauras.getValue() && !this.hasDisabled) {
            this.caura = "AutoCrystal";
            this.cDelay = 19;
            this.isDisabling = true;
            ModuleManager.getModuleByName(this.caura).toggle();
        }
        if (this.toggleoff.getValue()) {
            ++this.test;
            if (this.test == 20) {
                super.toggle();
            }
        }
        if (AutoTrapO.mc.player == null) {
            return;
        }
        if (!this.activeInFreecam.getValue() && ModuleManager.isModuleEnabled("Freecam")) {
            return;
        }
        if (!this.firstRun) {
            if (this.delayStep < this.tickDelay.getValue()) {
                ++this.delayStep;
                return;
            }
            this.delayStep = 0;
        }
        this.findClosestTarget();
        if (this.closestTarget == null) {
            if (this.firstRun) {
                this.firstRun = false;
                if (this.announceUsage.getValue()) {
                    Command.sendClientMessage(ChatFormatting.GREEN + "AutoTrap has been toggle on! waiting for target.");
                }
            }
            return;
        }
        if (this.firstRun) {
            this.firstRun = false;
            this.lastTickTargetName = this.closestTarget.getName();
            if (this.announceUsage.getValue()) {
                Command.sendClientMessage(ChatFormatting.GREEN + "AutoTrap has been toggle on! target: " + this.lastTickTargetName);
            }
        }
        else if (!this.lastTickTargetName.equals(this.closestTarget.getName())) {
            this.lastTickTargetName = this.closestTarget.getName();
            this.offsetStep = 0;
            if (this.announceUsage.getValue()) {
                Command.sendClientMessage(ChatFormatting.GREEN + "AutoTrap new target: " + this.lastTickTargetName);
            }
        }
        final List<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (this.cage.getValue().equalsIgnoreCase("Trap")) {
            Collections.addAll(placeTargets, Offsets.TRAP);
        }
        if (this.cage.getValue().equalsIgnoreCase("TrapTop")) {
            Collections.addAll(placeTargets, Offsets.TRAPTOP);
        }
        if (this.cage.getValue().equalsIgnoreCase("TrapFullRoof")) {
            Collections.addAll(placeTargets, Offsets.TRAPFULLROOF);
        }
        if (this.cage.getValue().equalsIgnoreCase("TrapFullRoofTop")) {
            Collections.addAll(placeTargets, Offsets.TRAPFULLROOFTOP);
        }
        if (this.cage.getValue().equalsIgnoreCase("Crystalexa")) {
            Collections.addAll(placeTargets, Offsets.CRYSTALEXA);
        }
        if (this.cage.getValue().equalsIgnoreCase("Crystal")) {
            Collections.addAll(placeTargets, Offsets.CRYSTAL);
        }
        if (this.cage.getValue().equalsIgnoreCase("CrystalFullRoof")) {
            Collections.addAll(placeTargets, Offsets.CRYSTALFULLROOF);
        }
        int blocksPlaced = 0;
        while (blocksPlaced < this.blocksPerTick.getValue()) {
            if (this.offsetStep >= placeTargets.size()) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos offsetPos = new BlockPos((Vec3d)placeTargets.get(this.offsetStep));
            final BlockPos targetPos = new BlockPos(this.closestTarget.getPositionVector()).down().add(offsetPos.x, offsetPos.y, offsetPos.z);
            this.placeList.add(targetPos);
            if (this.placeBlockInRange(targetPos, this.range.getValue())) {
                ++blocksPlaced;
            }
            ++this.offsetStep;
        }
        if (blocksPlaced > 0) {
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                AutoTrapO.mc.player.inventory.currentItem = this.playerHotbarSlot;
                this.lastHotbarSlot = this.playerHotbarSlot;
            }
            if (this.isSneaking) {
                AutoTrapO.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoTrapO.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.isSneaking = false;
            }
        }
    }
    
    private boolean placeBlockInRange(final BlockPos pos, final double range) {
        final Block block = AutoTrapO.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            this.placeList.remove(pos);
            return false;
        }
        for (final Entity entity : AutoTrapO.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                return false;
            }
        }
        final EnumFacing side = BlockInteractionHelper.getPlaceableSide(pos);
        if (side == null) {
            return false;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        if (!BlockInteractionHelper.canBeClicked(neighbour)) {
            return false;
        }
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = AutoTrapO.mc.world.getBlockState(neighbour).getBlock();
        if (AutoTrapO.mc.player.getPositionVector().distanceTo(hitVec) > range) {
            return false;
        }
        final int obiSlot = this.findObiInHotbar();
        if (obiSlot == -1) {
            this.disable();
        }
        if (this.lastHotbarSlot != obiSlot) {
            AutoTrapO.mc.player.inventory.currentItem = obiSlot;
            this.lastHotbarSlot = obiSlot;
        }
        if ((!this.isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock)) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
            AutoTrapO.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoTrapO.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        if (this.rotate.getValue()) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }
        AutoTrapO.mc.playerController.processRightClickBlock(AutoTrapO.mc.player, AutoTrapO.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        AutoTrapO.mc.player.swingArm(EnumHand.MAIN_HAND);
        AutoTrapO.mc.rightClickDelayTimer = 4;
        if (this.noGlitchBlocks.getValue() && !AutoTrapO.mc.playerController.getCurrentGameType().equals((Object)GameType.CREATIVE)) {
            AutoTrapO.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, neighbour, opposite));
        }
        return true;
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoTrapO.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock)stack.getItem()).getBlock();
                if (block instanceof BlockObsidian) {
                    slot = i;
                    break;
                }
            }
        }
        return slot;
    }
    
    private void findClosestTarget() {
        final List<EntityPlayer> playerList = (List<EntityPlayer>)AutoTrapO.mc.world.playerEntities;
        this.closestTarget = null;
        for (final EntityPlayer target : playerList) {
            if (target == AutoTrapO.mc.player) {
                continue;
            }
            if (Friends.isFriend(target.getName())) {
                continue;
            }
            if (!EntityUtil.isLiving((Entity)target)) {
                continue;
            }
            if (target.getHealth() <= 0.0f) {
                continue;
            }
            if (this.closestTarget == null) {
                this.closestTarget = target;
            }
            else {
                if (AutoTrapO.mc.player.getDistance((Entity)target) >= AutoTrapO.mc.player.getDistance((Entity)this.closestTarget)) {
                    continue;
                }
                this.closestTarget = target;
            }
        }
    }
    
    private enum Cage
    {
        TRAP, 
        TRAPTOP, 
        TRAPFULLROOF, 
        TRAPFULLROOFTOP, 
        CRYSTALEXA, 
        CRYSTAL, 
        CRYSTALFULLROOF;
    }
    
    private static class Offsets
    {
        private static final Vec3d[] TRAP;
        private static final Vec3d[] TRAPTOP;
        private static final Vec3d[] TRAPFULLROOF;
        private static final Vec3d[] TRAPFULLROOFTOP;
        private static final Vec3d[] CRYSTALEXA;
        private static final Vec3d[] CRYSTAL;
        private static final Vec3d[] CRYSTALFULLROOF;
        
        static {
            TRAP = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0) };
            TRAPTOP = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0), new Vec3d(0.0, 4.0, 0.0) };
            TRAPFULLROOF = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 1.0), new Vec3d(-1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 0.0) };
            TRAPFULLROOFTOP = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 1.0), new Vec3d(-1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 0.0), new Vec3d(0.0, 4.0, 0.0) };
            CRYSTALEXA = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(-1.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 1.0), new Vec3d(1.0, 2.0, -1.0), new Vec3d(-1.0, 2.0, 1.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0) };
            CRYSTAL = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(-1.0, 0.0, 1.0), new Vec3d(1.0, 0.0, -1.0), new Vec3d(-1.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 1.0), new Vec3d(1.0, 1.0, -1.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(-1.0, 2.0, 1.0), new Vec3d(1.0, 2.0, -1.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0) };
            CRYSTALFULLROOF = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(-1.0, 0.0, 1.0), new Vec3d(1.0, 0.0, -1.0), new Vec3d(-1.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 1.0), new Vec3d(1.0, 1.0, -1.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(-1.0, 2.0, 1.0), new Vec3d(1.0, 2.0, -1.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 1.0), new Vec3d(-1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 0.0) };
        }
    }
}
