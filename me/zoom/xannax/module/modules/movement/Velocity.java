// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import me.zoom.xannax.Xannax;
import java.util.function.Predicate;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import me.zoom.xannax.event.events.WaterPushEvent;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Velocity extends Module
{
    public Setting.Boolean noPush;
    Setting.Boolean antiKnockBack;
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener;
    @EventHandler
    private final Listener<WaterPushEvent> waterPushEventListener;
    
    public Velocity() {
        super("Velocity", "Velocity", Category.Movement);
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (this.antiKnockBack.getValue()) {
                if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
                    event.cancel();
                }
                if (event.getPacket() instanceof SPacketExplosion) {
                    event.cancel();
                }
            }
            return;
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        this.waterPushEventListener = new Listener<WaterPushEvent>(event -> {
            if (this.noPush.getValue()) {
                event.cancel();
            }
        }, (Predicate<WaterPushEvent>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        this.noPush = this.registerBoolean("No Push", "NoPush", false);
        this.antiKnockBack = this.registerBoolean("Velocity", "Velocity", false);
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}
