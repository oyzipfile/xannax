// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.util.math.Vec3i;
import net.minecraft.init.Blocks;
import me.zoom.xannax.util.PlayerUtil;
import java.util.Iterator;
import me.zoom.xannax.util.BlockUtils;
import me.zoom.xannax.util.BlockInteractionHelper;
import java.util.Collection;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class HoleFill extends Module
{
    Setting.Boolean hole_toggle;
    Setting.Boolean hole_rotate;
    Setting.Integer hole_range;
    private final ArrayList<BlockPos> holes;
    
    public HoleFill() {
        super("HoleFill", "Fill Holes", Category.Combat);
        this.holes = new ArrayList<BlockPos>();
    }
    
    @Override
    public void setup() {
        final ArrayList<String> hands = new ArrayList<String>();
        hands.add("Mainhand");
        hands.add("Offhand");
        hands.add("Both");
        this.hole_toggle = this.registerBoolean("Toggle", "HoleFillToggle", true);
        this.hole_rotate = this.registerBoolean("Rotate", "HoleFillRotate", true);
        this.hole_range = this.registerInteger("Range", "HoleFillRange", 4, 1, 6);
    }
    
    public void onEnable() {
        if (this.find_in_hotbar() == -1) {
            this.disable();
        }
        this.find_new_holes();
    }
    
    public void onDisable() {
        this.holes.clear();
    }
    
    @Override
    public void onUpdate() {
        if (this.find_in_hotbar() == -1) {
            this.disable();
            return;
        }
        if (this.holes.isEmpty()) {
            if (!this.hole_toggle.getValue()) {
                this.disable();
                return;
            }
            this.find_new_holes();
        }
        BlockPos pos_to_fill = null;
        for (final BlockPos pos : new ArrayList<BlockPos>(this.holes)) {
            if (pos == null) {
                continue;
            }
            final BlockInteractionHelper.ValidResult result = BlockInteractionHelper.valid(pos);
            if (result == BlockInteractionHelper.ValidResult.Ok) {
                pos_to_fill = pos;
                break;
            }
            this.holes.remove(pos);
        }
        if (this.find_in_hotbar() == -1) {
            this.disable();
            return;
        }
        if (pos_to_fill != null && BlockUtils.placeBlock(pos_to_fill, this.find_in_hotbar(), this.hole_rotate.getValue(), this.hole_rotate.getValue())) {
            this.holes.remove(pos_to_fill);
        }
    }
    
    public void find_new_holes() {
        this.holes.clear();
        for (final BlockPos pos : BlockInteractionHelper.getSphere(PlayerUtil.GetLocalPlayerPosFloored(), (float)this.hole_range.getValue(), this.hole_range.getValue(), false, true, 0)) {
            if (!HoleFill.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!HoleFill.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!HoleFill.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            boolean possible = true;
            for (final BlockPos seems_blocks : new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) }) {
                final Block block = HoleFill.mc.world.getBlockState(pos.add((Vec3i)seems_blocks)).getBlock();
                if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                    possible = false;
                    break;
                }
            }
            if (!possible) {
                continue;
            }
            this.holes.add(pos);
        }
    }
    
    private int find_in_hotbar() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = HoleFill.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock)stack.getItem()).getBlock();
                if (block instanceof BlockEnderChest) {
                    return i;
                }
                if (block instanceof BlockObsidian) {
                    return i;
                }
            }
        }
        return -1;
    }
}
