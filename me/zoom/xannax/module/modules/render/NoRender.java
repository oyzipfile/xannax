// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.Xannax;
import net.minecraft.init.MobEffects;
import net.minecraft.block.material.Material;
import java.util.function.Predicate;
import me.zoom.xannax.event.events.BossbarEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class NoRender extends Module
{
    public Setting.Boolean armor;
    Setting.Boolean fire;
    Setting.Boolean blind;
    Setting.Boolean nausea;
    public Setting.Boolean hurtCam;
    public Setting.Boolean noOverlay;
    Setting.Boolean noBossBar;
    @EventHandler
    public Listener<RenderBlockOverlayEvent> blockOverlayEventListener;
    @EventHandler
    private final Listener<EntityViewRenderEvent.FogDensity> fogDensityListener;
    @EventHandler
    private final Listener<RenderBlockOverlayEvent> renderBlockOverlayEventListener;
    @EventHandler
    private final Listener<RenderGameOverlayEvent> renderGameOverlayEventListener;
    @EventHandler
    private final Listener<BossbarEvent> bossbarEventListener;
    
    public NoRender() {
        super("NoRender", "NoRender", Category.Render);
        this.blockOverlayEventListener = new Listener<RenderBlockOverlayEvent>(event -> {
            if (this.fire.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) {
                event.setCanceled(true);
            }
            if (this.noOverlay.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.WATER) {
                event.setCanceled(true);
            }
            if (this.noOverlay.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.BLOCK) {
                event.setCanceled(true);
            }
            return;
        }, (Predicate<RenderBlockOverlayEvent>[])new Predicate[0]);
        this.fogDensityListener = new Listener<EntityViewRenderEvent.FogDensity>(event -> {
            if (this.noOverlay.getValue() && (event.getState().getMaterial().equals(Material.WATER) || event.getState().getMaterial().equals(Material.LAVA))) {
                event.setDensity(0.0f);
                event.setCanceled(true);
            }
            return;
        }, (Predicate<EntityViewRenderEvent.FogDensity>[])new Predicate[0]);
        this.renderBlockOverlayEventListener = new Listener<RenderBlockOverlayEvent>(event -> event.setCanceled(true), (Predicate<RenderBlockOverlayEvent>[])new Predicate[0]);
        this.renderGameOverlayEventListener = new Listener<RenderGameOverlayEvent>(event -> {
            if (this.noOverlay.getValue()) {
                if (event.getType().equals((Object)RenderGameOverlayEvent.ElementType.HELMET)) {
                    event.setCanceled(true);
                }
                if (event.getType().equals((Object)RenderGameOverlayEvent.ElementType.PORTAL)) {
                    event.setCanceled(true);
                }
            }
            return;
        }, (Predicate<RenderGameOverlayEvent>[])new Predicate[0]);
        this.bossbarEventListener = new Listener<BossbarEvent>(event -> {
            if (this.noBossBar.getValue()) {
                event.cancel();
            }
        }, (Predicate<BossbarEvent>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        this.armor = this.registerBoolean("Armor", "Armor", false);
        this.fire = this.registerBoolean("Fire", "Fire", false);
        this.blind = this.registerBoolean("Blind", "Blind", false);
        this.nausea = this.registerBoolean("Nausea", "Nausea", false);
        this.hurtCam = this.registerBoolean("HurtCam", "HurtCam", false);
        this.noOverlay = this.registerBoolean("No Overlay", "NoOverlay", false);
        this.noBossBar = this.registerBoolean("No Boss Bar", "NoBossBar", false);
    }
    
    @Override
    public void onUpdate() {
        if (this.blind.getValue() && NoRender.mc.player.isPotionActive(MobEffects.BLINDNESS)) {
            NoRender.mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (this.nausea.getValue() && NoRender.mc.player.isPotionActive(MobEffects.NAUSEA)) {
            NoRender.mc.player.removePotionEffect(MobEffects.NAUSEA);
        }
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}
