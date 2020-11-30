// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemAppleGold;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class AutoWeed extends Module
{
    private boolean isEating;
    Setting.Boolean chorus;
    
    public AutoWeed() {
        super("AutoWeed", "AutoWeed", Category.Combat);
        this.isEating = false;
    }
    
    @Override
    public void setup() {
        this.chorus = this.registerBoolean("Disable On Chorus", "Disable On Chorus", true);
    }
    
    public void onEnable() {
        AutoWeed.mc.player.inventory.currentItem = this.findGapple();
    }
    
    @Override
    public void onUpdate() {
        final Item itemMainHand = AutoWeed.mc.player.getHeldItemMainhand().getItem();
        final Item itemONotMainHand = AutoWeed.mc.player.getHeldItemOffhand().getItem();
        final boolean gapInMainHand = itemMainHand instanceof ItemAppleGold;
        final boolean gapNotInMainHand = itemONotMainHand instanceof ItemAppleGold;
        this.isEating = true;
        KeyBinding.setKeyBindState(AutoWeed.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
        AutoWeed.mc.rightClickMouse();
    }
    
    private int findGapple() {
        int slot = 0;
        for (int i = 0; i < 9; ++i) {
            if (AutoWeed.mc.player.inventory.getStackInSlot(i).getItem() == Items.GOLDEN_APPLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }
}
