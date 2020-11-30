// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.player;

import me.zoom.xannax.Xannax;
import java.util.function.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.module.Module;

public class BuildHeight extends Module
{
    @EventHandler
    private Listener<PacketEvent.Send> send_listener;
    
    public BuildHeight() {
        super("BuildHeight", "Lets you place blocks at build height", Category.Player);
        CPacketPlayerTryUseItemOnBlock p;
        this.send_listener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                p = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
                if (p.getPos().getY() >= 255 && p.getDirection() == EnumFacing.UP) {
                    p.placedBlockDirection = EnumFacing.DOWN;
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}
