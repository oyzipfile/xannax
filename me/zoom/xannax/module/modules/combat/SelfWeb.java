// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import me.zoom.xannax.setting.Setting;
import net.minecraft.util.math.BlockPos;
import me.zoom.xannax.module.Module;

public class SelfWeb extends Module
{
    BlockPos feet;
    int d;
    public static float yaw;
    public static float pitch;
    private Setting.Boolean announceUsage;
    private Setting.Integer delay;
    
    public SelfWeb() {
        super("SelfWeb", "SelfWeb", Category.Combat);
    }
    
    public boolean isInBlockRange(final Entity target) {
        return target.getDistance((Entity)SelfWeb.mc.player) <= 4.0f;
    }
    
    @Override
    public void setup() {
        final ArrayList<String> displayModes = new ArrayList<String>();
        this.delay = this.registerInteger("Delay", "Delay", 3, 0, 10);
        this.announceUsage = this.registerBoolean("ToggleMSG", "ToggleMSG", true);
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return SelfWeb.mc.world.getBlockState(pos).getBlock().canCollideCheck(SelfWeb.mc.world.getBlockState(pos), false);
    }
    
    private boolean isStackObby(final ItemStack stack) {
        return stack != null && stack.getItem() == Item.getItemById(30);
    }
    
    private boolean doesHotbarHaveWeb() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack stack = SelfWeb.mc.player.inventoryContainer.getSlot(i).getStack();
            if (stack != null && this.isStackObby(stack)) {
                return true;
            }
        }
        return false;
    }
    
    public static Block getBlock(final BlockPos pos) {
        return getState(pos).getBlock();
    }
    
    public static IBlockState getState(final BlockPos pos) {
        return SelfWeb.mc.world.getBlockState(pos);
    }
    
    public static boolean placeBlockLegit(final BlockPos pos) {
        final Vec3d eyesPos = new Vec3d(SelfWeb.mc.player.posX, SelfWeb.mc.player.posY + SelfWeb.mc.player.getEyeHeight(), SelfWeb.mc.player.posZ);
        final Vec3d posVec = new Vec3d((Vec3i)pos).add(0.5, 0.5, 0.5);
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.offset(side);
            if (canBeClicked(neighbor)) {
                final Vec3d hitVec = posVec.add(new Vec3d(side.getDirectionVec()).scale(0.5));
                if (eyesPos.squareDistanceTo(hitVec) <= 36.0) {
                    SelfWeb.mc.playerController.processRightClickBlock(SelfWeb.mc.player, SelfWeb.mc.world, neighbor, side.getOpposite(), hitVec, EnumHand.MAIN_HAND);
                    SelfWeb.mc.player.swingArm(EnumHand.MAIN_HAND);
                    try {
                        TimeUnit.MILLISECONDS.sleep(10L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void onUpdate() {
        if (SelfWeb.mc.player.isHandActive()) {
            return;
        }
        this.trap((EntityPlayer)SelfWeb.mc.player);
    }
    
    public static double roundToHalf(final double d) {
        return Math.round(d * 2.0) / 2.0;
    }
    
    public void onEnable() {
        if (SelfWeb.mc.player == null) {
            this.disable();
            return;
        }
        if (this.announceUsage.getValue()) {
            Command.sendClientMessage(ChatFormatting.GREEN + "SelfWeb has been toggled on!");
        }
        this.d = 0;
    }
    
    private void trap(final EntityPlayer player) {
        if (player.moveForward == 0.0 && player.moveStrafing == 0.0 && player.moveForward == 0.0) {
            ++this.d;
        }
        if (player.moveForward != 0.0 || player.moveStrafing != 0.0 || player.moveForward != 0.0) {
            this.d = 0;
        }
        if (!this.doesHotbarHaveWeb()) {
            this.d = 0;
        }
        if (this.d == this.delay.getValue() && this.doesHotbarHaveWeb()) {
            this.feet = new BlockPos(player.posX, player.posY, player.posZ);
            for (int i = 36; i < 45; ++i) {
                final ItemStack stack = SelfWeb.mc.player.inventoryContainer.getSlot(i).getStack();
                if (stack != null && this.isStackObby(stack)) {
                    final int oldSlot = SelfWeb.mc.player.inventory.currentItem;
                    if (SelfWeb.mc.world.getBlockState(this.feet).getMaterial().isReplaceable()) {
                        SelfWeb.mc.player.inventory.currentItem = i - 36;
                        if (SelfWeb.mc.world.getBlockState(this.feet).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.feet);
                        }
                        SelfWeb.mc.player.inventory.currentItem = oldSlot;
                        this.d = 0;
                        break;
                    }
                    this.d = 0;
                }
                this.d = 0;
            }
        }
    }
    
    public void onDisable() {
        this.d = 0;
        if (this.announceUsage.getValue()) {
            Command.sendClientMessage(ChatFormatting.RED + "SelfWeb has been toggled on!");
        }
        SelfWeb.yaw = SelfWeb.mc.player.rotationYaw;
        SelfWeb.pitch = SelfWeb.mc.player.rotationPitch;
    }
    
    public EnumFacing getEnumFacing(final float posX, final float posY, final float posZ) {
        return EnumFacing.getFacingFromVector(posX, posY, posZ);
    }
    
    public BlockPos getBlockPos(final double x, final double y, final double z) {
        return new BlockPos(x, y, z);
    }
}
