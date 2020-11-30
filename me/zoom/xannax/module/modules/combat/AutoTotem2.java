// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import me.zoom.xannax.module.ModuleManager;
import net.minecraft.init.Items;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.item.Item;
import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.inventory.GuiContainer;
import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class AutoTotem2 extends Module
{
    int totems;
    boolean moving;
    boolean returnI;
    Setting.Boolean soft;
    Setting.Mode OffhandItem;
    Setting.Integer TotemHealth;
    Setting.Boolean CrystalAura;
    Setting.Boolean AuraGap;
    
    public AutoTotem2() {
        super("AutoTotem2", "AutoTotem2", Category.Combat);
        this.moving = false;
        this.returnI = false;
    }
    
    @Override
    public void setup() {
        this.soft = this.registerBoolean("Soft", "Soft", true);
        this.TotemHealth = this.registerInteger("Health", "health", 10, 1, 20);
        this.CrystalAura = this.registerBoolean("Crystal on CA", "crystalaura", false);
        this.AuraGap = this.registerBoolean("Gap on Aura", "auraGap", true);
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("totem");
        modes.add("crystal");
        modes.add("golden apple");
        this.OffhandItem = this.registerMode("Item", "item", modes, "totem");
    }
    
    @Override
    public void onUpdate() {
        final Item item = this.getItem();
        if (AutoTotem2.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.returnI) {
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (AutoTotem2.mc.player.inventory.getStackInSlot(i).isEmpty()) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            AutoTotem2.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem2.mc.player);
            this.returnI = false;
        }
        this.totems = AutoTotem2.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == item).mapToInt(ItemStack::func_190916_E).sum();
        if (AutoTotem2.mc.player.getHeldItemOffhand().getItem() == item) {
            ++this.totems;
        }
        else {
            if (this.soft.getValue() && !AutoTotem2.mc.player.getHeldItemOffhand().isEmpty()) {
                return;
            }
            if (this.moving) {
                AutoTotem2.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem2.mc.player);
                this.moving = false;
                if (!AutoTotem2.mc.player.inventory.getItemStack().isEmpty()) {
                    this.returnI = true;
                }
                return;
            }
            if (AutoTotem2.mc.player.inventory.getItemStack().isEmpty()) {
                if (this.totems == 0) {
                    return;
                }
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (AutoTotem2.mc.player.inventory.getStackInSlot(i).getItem() == item) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                AutoTotem2.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem2.mc.player);
                this.moving = true;
            }
            else if (!this.soft.getValue()) {
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (AutoTotem2.mc.player.inventory.getStackInSlot(i).isEmpty()) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                AutoTotem2.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem2.mc.player);
            }
        }
    }
    
    @Override
    public String getHudInfo() {
        final String t = "[" + ChatFormatting.WHITE + this.OffhandItem.getValue() + ChatFormatting.GRAY + "]";
        return t;
    }
    
    public Item getItem() {
        Item item = Items.TOTEM_OF_UNDYING;
        Item mainItem = null;
        boolean Crystal = false;
        if (AutoTotem2.mc.player.getHealth() < this.TotemHealth.getValue()) {
            item = Items.TOTEM_OF_UNDYING;
        }
        else if (this.CrystalAura.getValue() && ModuleManager.isModuleEnabled("AutoCrystal")) {
            item = Items.END_CRYSTAL;
            Crystal = true;
        }
        else if (this.AuraGap.getValue() && ModuleManager.isModuleEnabled("KillAura") && !Crystal) {
            item = Items.GOLDEN_APPLE;
        }
        else if (this.OffhandItem.getValue() == "totem") {
            item = Items.TOTEM_OF_UNDYING;
            mainItem = Items.TOTEM_OF_UNDYING;
        }
        else if (this.OffhandItem.getValue() == "crystal") {
            item = Items.END_CRYSTAL;
            mainItem = Items.END_CRYSTAL;
        }
        else if (this.OffhandItem.getValue() == "golden apple") {
            item = Items.GOLDEN_APPLE;
            mainItem = Items.GOLDEN_APPLE;
        }
        return item;
    }
}
