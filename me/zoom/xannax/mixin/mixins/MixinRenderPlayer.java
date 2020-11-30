// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import java.awt.Color;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.module.modules.render.HandColor;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderPlayer.class })
public class MixinRenderPlayer
{
    @Inject(method = { "renderRightArm" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;swingProgress:F", opcode = 181) }, cancellable = true)
    public void renderRightArmBegin(final AbstractClientPlayer clientPlayer, final CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && ModuleManager.isModuleEnabled("HandColor")) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            GL11.glEnable(10754);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
            final Color rainbowColor1 = HandColor.rainbow.getValue() ? new Color(RenderUtil.getRainbow(HandColor.speed.getValue() * 100, 0, HandColor.saturation.getValue() / 100.0f, HandColor.brightness.getValue() / 100.0f)) : new Color(HandColor.redh.getValue(), HandColor.greenh.getValue(), HandColor.blueh.getValue());
            final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
            final Color color = new Color(HandColor.redh.getValue(), HandColor.greenh.getValue(), HandColor.blueh.getValue(), HandColor.alphah.getValue());
            GL11.glColor4f(rainbowColor2.getRed() / 255.0f, rainbowColor2.getGreen() / 255.0f, rainbowColor2.getBlue() / 255.0f, HandColor.alphah.getValue() / 255.0f);
        }
    }
    
    @Inject(method = { "renderRightArm" }, at = { @At("RETURN") }, cancellable = true)
    public void renderRightArmReturn(final AbstractClientPlayer clientPlayer, final CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && ModuleManager.isModuleEnabled("HandColor")) {
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }
    }
    
    @Inject(method = { "renderLeftArm" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;swingProgress:F", opcode = 181) }, cancellable = true)
    public void renderLeftArmBegin(final AbstractClientPlayer clientPlayer, final CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && ModuleManager.isModuleEnabled("HandColor")) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            GL11.glEnable(10754);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
            final Color color = new Color(HandColor.redh.getValue(), HandColor.greenh.getValue(), HandColor.blueh.getValue(), HandColor.alphah.getValue());
            final Color rainbowColor1 = HandColor.rainbow.getValue() ? new Color(RenderUtil.getRainbow(HandColor.speed.getValue() * 100, 0, HandColor.saturation.getValue() / 100.0f, HandColor.brightness.getValue() / 100.0f)) : new Color(HandColor.redh.getValue(), HandColor.greenh.getValue(), HandColor.blueh.getValue());
            final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
            GL11.glColor4f(rainbowColor2.getRed() / 255.0f, rainbowColor2.getGreen() / 255.0f, rainbowColor2.getBlue() / 255.0f, HandColor.alphah.getValue() / 255.0f);
        }
    }
    
    @Inject(method = { "renderLeftArm" }, at = { @At("RETURN") }, cancellable = true)
    public void renderLeftArmReturn(final AbstractClientPlayer clientPlayer, final CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && ModuleManager.isModuleEnabled("HandColor")) {
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }
    }
    
    @Inject(method = { "renderEntityName" }, at = { @At("HEAD") }, cancellable = true)
    public void renderEntityNameHook(final AbstractClientPlayer entityIn, final double x, final double y, final double z, final String name, final double distanceSq, final CallbackInfo info) {
        if (ModuleManager.isModuleEnabled("Nametags")) {
            info.cancel();
        }
    }
}
