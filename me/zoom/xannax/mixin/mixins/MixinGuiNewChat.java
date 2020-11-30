// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import me.zoom.xannax.Xannax;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.gui.Gui;
import me.zoom.xannax.module.modules.misc.Chat;
import me.zoom.xannax.module.ModuleManager;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.gui.ChatLine;
import java.util.List;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiNewChat.class })
public abstract class MixinGuiNewChat
{
    @Shadow
    private int scrollPos;
    @Shadow
    @Final
    private List<ChatLine> drawnChatLines;
    
    @Shadow
    public abstract int getLineCount();
    
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectBackgroundClean(final int left, final int top, final int right, final int bottom, final int color) {
        if (!ModuleManager.isModuleEnabled("Chat") || !((Chat)ModuleManager.getModuleByName("Chat")).clearBkg.getValue()) {
            Gui.drawRect(left, top, right, bottom, color);
        }
    }
    
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawStringWithShadowMaybe(final FontRenderer fontRenderer, final String message, final float x, final float y, final int color) {
        if (!ModuleManager.isModuleEnabled("Chat")) {
            return fontRenderer.drawStringWithShadow(message, x, y, color);
        }
        if (Chat.customFont.getValue()) {
            if (Chat.noChatShadow.getValue()) {
                return (int)Xannax.fontRenderer.drawString(message, x, y, color);
            }
            return (int)Xannax.fontRenderer.drawStringWithShadow(message, x, y, color);
        }
        else {
            if (Chat.noChatShadow.getValue()) {
                return fontRenderer.drawString(message, (int)x, (int)y, color);
            }
            return fontRenderer.drawStringWithShadow(message, x, y, color);
        }
    }
}
