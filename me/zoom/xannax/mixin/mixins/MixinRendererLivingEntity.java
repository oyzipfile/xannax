// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import org.spongepowered.asm.mixin.Overwrite;
import me.zoom.xannax.module.modules.render.Chams;
import me.zoom.xannax.util.OutlineUtils;
import me.zoom.xannax.util.friend.Friends;
import java.awt.Color;
import me.zoom.xannax.util.RenderUtil;
import org.lwjgl.opengl.GL11;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.entity.Entity;
import me.zoom.xannax.module.modules.render.ESP;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;

@Mixin({ RenderLivingBase.class })
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends Render<T>
{
    @Shadow
    protected ModelBase mainModel;
    
    protected MixinRendererLivingEntity() {
        super((RenderManager)null);
    }
    
    @Overwrite
    protected void renderModel(final T entitylivingbaseIn, final float p_77036_2_, final float p_77036_3_, final float p_77036_4_, final float p_77036_5_, final float p_77036_6_, final float scaleFactor) {
        boolean isPlayer = entitylivingbaseIn instanceof EntityPlayer && entitylivingbaseIn != Minecraft.getMinecraft().player;
        if (ESP.self.getValue()) {
            isPlayer = (entitylivingbaseIn instanceof EntityPlayer);
        }
        if (!this.bindEntityTexture((Entity)entitylivingbaseIn)) {
            return;
        }
        final Minecraft mc = Minecraft.getMinecraft();
        final boolean fancyGraphics = mc.gameSettings.fancyGraphics;
        mc.gameSettings.fancyGraphics = false;
        final float gamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 100000.0f;
        if (ModuleManager.isModuleEnabled("ESP")) {
            final String value = ESP.mode.getValue();
            switch (value) {
                case "WireFrame": {
                    if (ESP.player.getValue() && isPlayer) {
                        this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        final Color rainbowColor1 = ESP.rainbow.getValue() ? new Color(RenderUtil.getRainbow(ESP.speed.getValue() * 100, 0, ESP.saturation.getValue() / 100.0f, ESP.brightness.getValue() / 100.0f)) : new Color(ESP.redd.getValue(), ESP.greenn.getValue(), ESP.bluee.getValue());
                        final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
                        Color n = new Color(rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue());
                        if (Friends.isFriend(entitylivingbaseIn.getName())) {
                            n = new Color(5, 218, 255, 255);
                        }
                        RenderUtil.color(n.getRGB());
                        GL11.glLineWidth((float)ESP.width.getValue());
                        this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                        break;
                    }
                }
                case "OutLine": {
                    if (!ESP.player.getValue()) {
                        break;
                    }
                    boolean player = entitylivingbaseIn instanceof EntityPlayer && entitylivingbaseIn != Minecraft.getMinecraft().player;
                    if (ESP.self.getValue()) {
                        player = (entitylivingbaseIn instanceof EntityPlayer);
                    }
                    if (player) {
                        final Color rainbowColor3 = ESP.rainbow.getValue() ? new Color(RenderUtil.getRainbow(ESP.speed.getValue() * 100, 0, ESP.saturation.getValue() / 100.0f, ESP.brightness.getValue() / 100.0f)) : new Color(ESP.redd.getValue(), ESP.greenn.getValue(), ESP.bluee.getValue());
                        final Color rainbowColor4 = new Color(rainbowColor3.getRed(), rainbowColor3.getGreen(), rainbowColor3.getBlue());
                        Color n2 = new Color(rainbowColor4.getRed(), rainbowColor4.getGreen(), rainbowColor4.getBlue());
                        if (Friends.isFriend(entitylivingbaseIn.getName())) {
                            n2 = new Color(5, 218, 255, 255);
                        }
                        OutlineUtils.setColor(n2);
                        this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.renderOne((float)ESP.width.getValue());
                        this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.renderTwo();
                        this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.renderThree();
                        OutlineUtils.renderFour();
                        OutlineUtils.setColor(n2);
                        this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.renderFive();
                        OutlineUtils.setColor(Color.WHITE);
                        break;
                    }
                    break;
                }
            }
        }
        mc.gameSettings.fancyGraphics = fancyGraphics;
        mc.gameSettings.gammaSetting = gamma;
        if (ModuleManager.isModuleEnabled("Chams") && Chams.players.getValue() && isPlayer) {
            final Color rainbowColor5 = Chams.rainbow.getValue() ? new Color(RenderUtil.getRainbow(Chams.speed.getValue() * 100, 0, Chams.saturation.getValue() / 100.0f, Chams.brightness.getValue() / 100.0f)) : new Color(Chams.red.getValue(), Chams.green.getValue(), Chams.blue.getValue());
            final Color rainbowColor6 = new Color(rainbowColor5.getRed(), rainbowColor5.getGreen(), rainbowColor5.getBlue());
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glEnable(10754);
            GL11.glColor4f(rainbowColor6.getRed() / 255.0f, rainbowColor6.getGreen() / 255.0f, rainbowColor6.getBlue() / 255.0f, Chams.alpha.getValue() / 255.0f);
            this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }
        if ((!ModuleManager.isModuleEnabled("Chams") || !Chams.players.getValue()) && (!ESP.mode.getValue().equalsIgnoreCase("Wireframe") || !ModuleManager.isModuleEnabled("ESP") || !isPlayer || !ESP.player.getValue() || ESP.mode.getValue().equalsIgnoreCase("Outline"))) {
            this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
        }
    }
}
