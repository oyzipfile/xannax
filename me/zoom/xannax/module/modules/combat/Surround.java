// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.EnumFacing;
import me.zoom.xannax.util.BlockInteractionHelper;
import java.util.Iterator;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.util.Wrapper;
import net.minecraft.util.math.Vec3d;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Surround extends Module
{
    private Setting.Boolean triggerable;
    private Setting.Integer timeoutTicks;
    private Setting.Integer blocksPerTick;
    private Setting.Boolean rotate;
    private Setting.Boolean hybrid;
    private Setting.Boolean announceUsage;
    private final Vec3d[] surroundTargets;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private int offsetStep;
    private int totalTickRuns;
    private boolean isSneaking;
    private boolean flag;
    private int yHeight;
    
    public Surround() {
        super("Surround", "Surround", Category.Combat);
        this.surroundTargets = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, -1.0), new Vec3d(0.0, -1.0, 0.0) };
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.offsetStep = 0;
        this.totalTickRuns = 0;
        this.isSneaking = false;
        this.flag = false;
    }
    
    @Override
    public void setup() {
        this.triggerable = this.registerBoolean("Triggerable", "Triggerable", true);
        this.timeoutTicks = this.registerInteger("TimeoutTicks", "TimeoutTicks", 20, 1, 100);
        this.blocksPerTick = this.registerInteger("Blocks per Tick", "BlocksperTick", 4, 1, 9);
        this.rotate = this.registerBoolean("Rotate", "Rotate", true);
        this.hybrid = this.registerBoolean("Hybrid", "Hybrid", true);
        this.announceUsage = this.registerBoolean("ToggleMSG", "ToggleMSG", true);
    }
    
    @Override
    protected void onEnable() {
        this.flag = false;
        if (Surround.mc.player == null) {
            this.disable();
            return;
        }
        this.playerHotbarSlot = Wrapper.getPlayer().inventory.currentItem;
        this.lastHotbarSlot = -1;
        this.yHeight = (int)Math.round(Surround.mc.player.posY);
        if (this.announceUsage.getValue()) {
            Command.sendClientMessage(ChatFormatting.GREEN + "Surround has been toggled on!");
        }
    }
    
    @Override
    protected void onDisable() {
        if (Surround.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            Wrapper.getPlayer().inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            Surround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Surround.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        if (this.announceUsage.getValue()) {
            Command.sendClientMessage(ChatFormatting.RED + "Surround has been toggled off!");
        }
    }
    
    @Override
    public void onUpdate() {
        if (Surround.mc.player == null) {
            return;
        }
        if (this.hybrid.getValue() && (int)Math.round(Surround.mc.player.posY) != this.yHeight) {
            this.disable();
        }
        if (this.triggerable.getValue() && this.totalTickRuns >= this.timeoutTicks.getValue()) {
            this.totalTickRuns = 0;
            this.disable();
            return;
        }
        int blocksPlaced = 0;
        while (blocksPlaced < this.blocksPerTick.getValue()) {
            if (this.offsetStep >= this.surroundTargets.length) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos offsetPos = new BlockPos(this.surroundTargets[this.offsetStep]);
            final BlockPos targetPos = new BlockPos(Surround.mc.player.getPositionVector()).add(offsetPos.x, offsetPos.y, offsetPos.z);
            boolean shouldTryToPlace = true;
            if (!Wrapper.getWorld().getBlockState(targetPos).getMaterial().isReplaceable()) {
                shouldTryToPlace = false;
            }
            for (final Entity entity : Surround.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(targetPos))) {
                if (!(entity instanceof EntityItem)) {
                    if (entity instanceof EntityXPOrb) {
                        continue;
                    }
                    shouldTryToPlace = false;
                    break;
                }
            }
            if (shouldTryToPlace && this.placeBlock(targetPos)) {
                ++blocksPlaced;
            }
            ++this.offsetStep;
        }
        if (blocksPlaced > 0 && this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            Wrapper.getPlayer().inventory.currentItem = this.playerHotbarSlot;
            this.lastHotbarSlot = this.playerHotbarSlot;
        }
        ++this.totalTickRuns;
    }
    
    private boolean placeBlock(final BlockPos pos) {
        if (!Surround.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            return false;
        }
        if (!BlockInteractionHelper.checkForNeighbours(pos)) {
            return false;
        }
        final EnumFacing[] values = EnumFacing.values();
        final int length = values.length;
        int i = 0;
        while (i < length) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (!BlockInteractionHelper.canBeClicked(neighbor)) {
                ++i;
            }
            else {
                final int obiSlot = this.findObiInHotbar();
                if (obiSlot == -1) {
                    this.disable();
                    return false;
                }
                if (this.lastHotbarSlot != obiSlot) {
                    Wrapper.getPlayer().inventory.currentItem = obiSlot;
                    this.lastHotbarSlot = obiSlot;
                }
                final Block neighborPos;
                if (BlockInteractionHelper.blackList.contains(neighborPos = Surround.mc.world.getBlockState(neighbor).getBlock())) {
                    Surround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Surround.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    this.isSneaking = true;
                }
                final Vec3d hitVec = new Vec3d((Vec3i)neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
                if (this.rotate.getValue()) {
                    BlockInteractionHelper.faceVectorPacketInstant(hitVec);
                }
                Surround.mc.playerController.processRightClickBlock(Surround.mc.player, Surround.mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
                Surround.mc.player.swingArm(EnumHand.MAIN_HAND);
                return true;
            }
        }
        return false;
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
            final Block block;
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock && (block = ((ItemBlock)stack.getItem()).getBlock()) instanceof BlockObsidian) {
                slot = i;
                break;
            }
        }
        return slot;
    }
}
