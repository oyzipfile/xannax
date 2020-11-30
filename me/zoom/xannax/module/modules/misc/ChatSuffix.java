// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.Xannax;
import java.util.function.Predicate;
import me.zoom.xannax.command.Command;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.module.Module;

public class ChatSuffix extends Module
{
    @EventHandler
    private final Listener<PacketEvent.Send> listener;
    
    public ChatSuffix() {
        super("ChatSuffix", "Show off your client!", Category.Misc);
        String Separator2;
        String old;
        String suffix;
        String s;
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketChatMessage) {
                if (!((CPacketChatMessage)event.getPacket()).getMessage().startsWith("/") && !((CPacketChatMessage)event.getPacket()).getMessage().startsWith(Command.getPrefix()) && !((CPacketChatMessage)event.getPacket()).getMessage().startsWith("!")) {
                    Separator2 = " \u23d0 ";
                    old = ((CPacketChatMessage)event.getPacket()).getMessage();
                    suffix = Separator2 + "X\u1d00\u0274\u0274\u1d00X";
                    s = old + suffix;
                    if (s.length() <= 255) {
                        ((CPacketChatMessage)event.getPacket()).message = s;
                    }
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
