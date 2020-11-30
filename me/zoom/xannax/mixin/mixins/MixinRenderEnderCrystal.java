// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import java.awt.Color;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.util.OutlineUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.math.MathHelper;
import me.zoom.xannax.util.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.zoom.xannax.module.modules.render.ESP;
import me.zoom.xannax.module.modules.render.Chams;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderEnderCrystal.class })
public abstract class MixinRenderEnderCrystal
{
    @Shadow
    public ModelBase modelEnderCrystal;
    @Shadow
    public ModelBase modelEnderCrystalNoBase;
    @Final
    @Shadow
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;
    
    @Shadow
    public abstract void doRender(final EntityEnderCrystal p0, final double p1, final double p2, final double p3, final float p4, final float p5);
    
    @Redirect(method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void render1(final ModelBase var1, final Entity var2, final float var3, final float var4, final float var5, final float var6, final float var7, final float var8) {
        if ((!ModuleManager.isModuleEnabled("Chams") || !Chams.crystal.getValue()) && (ESP.mode.getValue().equalsIgnoreCase("Wireframe") || (boolean)ESP.mode.getValue().equalsIgnoreCase("Outline"))) {
            var1.render(var2, var3, var4, var5, var6, var7, var8);
        }
    }
    
    @Redirect(method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V", ordinal = 1))
    private void render2(final ModelBase var1, final Entity var2, final float var3, final float var4, final float var5, final float var6, final float var7, final float var8) {
        if ((!ModuleManager.isModuleEnabled("Chams") || !Chams.crystal.getValue()) && (ESP.mode.getValue().equalsIgnoreCase("Wireframe") || (boolean)ESP.mode.getValue().equalsIgnoreCase("Outline"))) {
            var1.render(var2, var3, var4, var5, var6, var7, var8);
        }
    }
    
    @Inject(method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V" }, at = { @At("RETURN") }, cancellable = true)
    public void IdoRender(final EntityEnderCrystal var1, final double var2, final double var4, final double var6, final float var8, final float var9, final CallbackInfo var10) {
        if (ModuleManager.isModuleEnabled("ESP")) {
            if (ESP.crystal.getValue() && ESP.mode.getValue().equalsIgnoreCase("Outline")) {
                final float var11 = var1.innerRotation + var9;
                GlStateManager.pushMatrix();
                GlStateManager.translate(var2, var4, var6);
                Wrapper.getMinecraft().renderManager.renderEngine.bindTexture(MixinRenderEnderCrystal.ENDER_CRYSTAL_TEXTURES);
                float var12 = MathHelper.sin(var11 * 0.2f) / 2.0f + 0.5f;
                var12 += var12 * var12;
                GL11.glLineWidth(5.0f);
                if (var1.shouldShowBottom()) {
                    this.modelEnderCrystal.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                else {
                    this.modelEnderCrystalNoBase.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                OutlineUtils.renderOne((float)ESP.Cwidth.getValue());
                if (var1.shouldShowBottom()) {
                    this.modelEnderCrystal.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                else {
                    this.modelEnderCrystalNoBase.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                OutlineUtils.renderTwo();
                if (var1.shouldShowBottom()) {
                    this.modelEnderCrystal.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                else {
                    this.modelEnderCrystalNoBase.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                final Color rainbowColor1 = ESP.Crainbow.getValue() ? new Color(RenderUtil.getRainbow(ESP.Cspeed.getValue() * 100, 0, ESP.Csaturation.getValue() / 100.0f, ESP.Cbrightness.getValue() / 100.0f)) : new Color(ESP.Credd.getValue(), ESP.Cgreenn.getValue(), ESP.Cbluee.getValue());
                final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
                final Color n = new Color(rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue());
                OutlineUtils.renderThree();
                OutlineUtils.renderFour();
                OutlineUtils.setColor(n);
                if (var1.shouldShowBottom()) {
                    this.modelEnderCrystal.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                else {
                    this.modelEnderCrystalNoBase.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                OutlineUtils.renderFive();
                GlStateManager.popMatrix();
            }
            else if (ESP.crystal.getValue() && ESP.mode.getValue().equalsIgnoreCase("Wireframe")) {
                final float var11 = var1.innerRotation + var9;
                GlStateManager.pushMatrix();
                GlStateManager.translate(var2, var4, var6);
                Wrapper.getMinecraft().renderManager.renderEngine.bindTexture(MixinRenderEnderCrystal.ENDER_CRYSTAL_TEXTURES);
                float var12 = MathHelper.sin(var11 * 0.2f) / 2.0f + 0.5f;
                var12 += var12 * var12;
                GL11.glPushAttrib(1048575);
                GL11.glPolygonMode(1032, 6913);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                final Color rainbowColor1 = ESP.Crainbow.getValue() ? new Color(RenderUtil.getRainbow(ESP.Cspeed.getValue() * 100, 0, ESP.Csaturation.getValue() / 100.0f, ESP.Cbrightness.getValue() / 100.0f)) : new Color(ESP.Credd.getValue(), ESP.Cgreenn.getValue(), ESP.Cbluee.getValue());
                final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
                final Color n = new Color(rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue());
                OutlineUtils.setColor(n);
                GL11.glLineWidth((float)ESP.Cwidth.getValue());
                if (var1.shouldShowBottom()) {
                    this.modelEnderCrystal.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                else {
                    this.modelEnderCrystalNoBase.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
        }
        if (ModuleManager.isModuleEnabled("Chams") && Chams.crystal.getValue()) {
            final Color rainbowColor3 = Chams.Crainbow.getValue() ? new Color(RenderUtil.getRainbow(Chams.Cspeed.getValue() * 100, 0, Chams.Csaturation.getValue() / 100.0f, Chams.Cbrightness.getValue() / 100.0f)) : new Color(Chams.Cred.getValue(), Chams.Cgreen.getValue(), Chams.Cblue.getValue());
            final Color var13 = new Color(rainbowColor3.getRed(), rainbowColor3.getGreen(), rainbowColor3.getBlue());
            GL11.glPushMatrix();
            final float var14 = var1.innerRotation + var9;
            GlStateManager.translate(var2, var4, var6);
            Wrapper.getMinecraft().renderManager.renderEngine.bindTexture(MixinRenderEnderCrystal.ENDER_CRYSTAL_TEXTURES);
            float var15 = MathHelper.sin(var14 * 0.2f) / 2.0f + 0.5f;
            var15 += var15 * var15;
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1.0E7f);
            GL11.glPushAttrib(1048575);
            if (!(boolean)Chams.lines.getValue()) {
                GL11.glPolygonMode(1028, 6914);
            }
            else {
                GL11.glPolygonMode(1028, 6913);
            }
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(var13.getRed() / 255.0f, var13.getGreen() / 255.0f, var13.getBlue() / 255.0f, Chams.Calpha.getValue() / 255.0f);
            if ((boolean)Chams.lines.getValue()) {
                GL11.glLineWidth((float)Chams.width.getValue());
            }
            if (var1.shouldShowBottom()) {
                this.modelEnderCrystal.render((Entity)var1, 0.0f, var14 * 3.0f, var15 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            else {
                this.modelEnderCrystalNoBase.render((Entity)var1, 0.0f, var14 * 3.0f, var15 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            GL11.glPopAttrib();
            GL11.glPolygonOffset(1.0f, 100000.0f);
            GL11.glDisable(32823);
            GL11.glPopMatrix();
        }
    }
}
