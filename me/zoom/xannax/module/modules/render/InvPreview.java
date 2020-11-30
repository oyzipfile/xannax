// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import net.minecraft.item.ItemStack;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.util.ColorHolder;
import me.zoom.xannax.module.modules.client.ClickGuiModule;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class InvPreview extends Module
{
    Setting.Integer xSetting;
    Setting.Integer ySetting;
    
    public InvPreview() {
        super("InvPreview", "InvPreview", Category.Render);
        this.xSetting = this.registerInteger("X", "X", 784, 0, 1000);
        this.ySetting = this.registerInteger("Y", "Y", 46, 0, 1000);
    }
    
    @Override
    public void onRender() {
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        RenderUtil.drawBorderedRect(this.xSetting.getValue(), this.ySetting.getValue(), this.xSetting.getValue() + 145, this.ySetting.getValue() + 48, 1.0, 1963986960, ColorHolder.toHex(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue()));
        for (int i = 0; i < 27; ++i) {
            final ItemStack itemStack = (ItemStack)InvPreview.mc.player.inventory.mainInventory.get(i + 9);
            final int offsetX = this.xSetting.getValue() + i % 9 * 16;
            final int offsetY = this.ySetting.getValue() + i / 9 * 16;
            InvPreview.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, offsetX, offsetY);
            InvPreview.mc.getRenderItem().renderItemOverlayIntoGUI(InvPreview.mc.fontRenderer, itemStack, offsetX, offsetY, (String)null);
        }
        RenderHelper.disableStandardItemLighting();
        InvPreview.mc.getRenderItem().zLevel = 0.0f;
        GlStateManager.popMatrix();
    }
}
