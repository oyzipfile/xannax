// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.client.gui.inventory.GuiContainer;
import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.setting.Setting;
import net.minecraft.item.Item;
import me.zoom.xannax.module.Module;

public class OffhandUtils extends Module
{
    public int totems;
    int crystals;
    boolean moving;
    boolean returnI;
    Item item;
    Setting.Mode mode;
    Setting.Integer health;
    Setting.Double fallDist;
    
    public OffhandUtils() {
        super("OffhandUtils", "OffhandUtils", Category.Combat);
        this.moving = false;
        this.returnI = false;
    }
    
    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Gapple");
        modes.add("Crystal");
        this.mode = this.registerMode("Mode", "ModeOH", modes, "Crystal");
        this.health = this.registerInteger("Health", "Health", 15, 0, 36);
        this.fallDist = this.registerDouble("FallDistance", "FallDistance", 15.0, 0.0, 30.0);
    }
    
    public void onDisable() {
        if (OffhandUtils.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        this.crystals = OffhandUtils.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::func_190916_E).sum();
        if (OffhandUtils.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
            if (this.crystals == 0) {
                return;
            }
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (OffhandUtils.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            OffhandUtils.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
            OffhandUtils.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
            OffhandUtils.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
        }
    }
    
    @Override
    public void onUpdate() {
        this.item = this.ittem();
        if (OffhandUtils.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.returnI) {
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (OffhandUtils.mc.player.inventory.getStackInSlot(i).isEmpty()) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            OffhandUtils.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
            this.returnI = false;
        }
        this.totems = OffhandUtils.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::func_190916_E).sum();
        this.crystals = OffhandUtils.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == this.item).mapToInt(ItemStack::func_190916_E).sum();
        if (this.shouldTotem() && OffhandUtils.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++this.totems;
        }
        else if (!this.shouldTotem() && OffhandUtils.mc.player.getHeldItemOffhand().getItem() == this.item) {
            this.crystals += OffhandUtils.mc.player.getHeldItemOffhand().getCount();
        }
        else {
            if (this.moving) {
                OffhandUtils.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
                this.moving = false;
                this.returnI = true;
                return;
            }
            if (OffhandUtils.mc.player.inventory.getItemStack().isEmpty()) {
                if (!this.shouldTotem() && OffhandUtils.mc.player.getHeldItemOffhand().getItem() == this.item) {
                    return;
                }
                if (this.shouldTotem() && OffhandUtils.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                    return;
                }
                if (!this.shouldTotem()) {
                    if (this.crystals == 0) {
                        return;
                    }
                    int t = -1;
                    for (int i = 0; i < 45; ++i) {
                        if (OffhandUtils.mc.player.inventory.getStackInSlot(i).getItem() == this.item) {
                            t = i;
                            break;
                        }
                    }
                    if (t == -1) {
                        return;
                    }
                    OffhandUtils.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
                    this.moving = true;
                }
                else {
                    if (this.totems == 0) {
                        return;
                    }
                    int t = -1;
                    for (int i = 0; i < 45; ++i) {
                        if (OffhandUtils.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                            t = i;
                            break;
                        }
                    }
                    if (t == -1) {
                        return;
                    }
                    OffhandUtils.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
                    this.moving = true;
                }
            }
            else {
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (OffhandUtils.mc.player.inventory.getStackInSlot(i).isEmpty()) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                OffhandUtils.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
            }
        }
    }
    
    private boolean shouldTotem() {
        final boolean hp = OffhandUtils.mc.player.getHealth() + OffhandUtils.mc.player.getAbsorptionAmount() <= this.health.getValue() || OffhandUtils.mc.player.fallDistance >= this.fallDist.getValue();
        final boolean endcrystal = !this.isCrystalsAABBEmpty();
        return hp;
    }
    
    public Item ittem() {
        Item p;
        if (this.mode.getValue().equalsIgnoreCase("Crystal")) {
            p = Items.END_CRYSTAL;
        }
        else {
            p = Items.GOLDEN_APPLE;
        }
        return p;
    }
    
    private boolean isEmpty(final BlockPos pos) {
        final List<Entity> crystalsInAABB = (List<Entity>)OffhandUtils.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos)).stream().filter(e -> e instanceof EntityEnderCrystal).collect(Collectors.toList());
        return crystalsInAABB.isEmpty();
    }
    
    private boolean isCrystalsAABBEmpty() {
        return this.isEmpty(OffhandUtils.mc.player.getPosition().add(1, 0, 0)) && this.isEmpty(OffhandUtils.mc.player.getPosition().add(-1, 0, 0)) && this.isEmpty(OffhandUtils.mc.player.getPosition().add(0, 0, 1)) && this.isEmpty(OffhandUtils.mc.player.getPosition().add(0, 0, -1)) && this.isEmpty(OffhandUtils.mc.player.getPosition());
    }
    
    @Override
    public String getHudInfo() {
        String t = "";
        if (this.mode.getValue().equalsIgnoreCase("Crystal")) {
            t = "[" + ChatFormatting.WHITE + "C" + ChatFormatting.GRAY + "]";
        }
        else {
            t = "[" + ChatFormatting.WHITE + "G" + ChatFormatting.GRAY + "]";
        }
        return t;
    }
}
