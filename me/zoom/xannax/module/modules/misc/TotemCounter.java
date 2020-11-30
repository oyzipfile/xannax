// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import me.zoom.xannax.Xannax;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import java.util.function.Predicate;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.TotemPopEvent;
import me.zero.alpine.listener.Listener;
import java.util.HashMap;
import me.zoom.xannax.module.Module;

public class TotemCounter extends Module
{
    private HashMap<String, Integer> popList;
    @EventHandler
    public Listener<TotemPopEvent> totemPopEvent;
    @EventHandler
    public Listener<PacketEvent.Receive> totemPopListener;
    
    public TotemCounter() {
        super("PopCounter", "PopCounter", Category.Misc);
        this.popList = new HashMap<String, Integer>();
        int popCounter;
        int newPopCounter;
        this.totemPopEvent = new Listener<TotemPopEvent>(event -> {
            if (this.popList == null) {
                this.popList = new HashMap<String, Integer>();
            }
            if (this.popList.get(event.getEntity().getName()) == null) {
                this.popList.put(event.getEntity().getName(), 1);
                Command.sendClientMessage(ChatFormatting.RED + event.getEntity().getName() + ChatFormatting.RED + " popped " + ChatFormatting.GREEN + 1 + ChatFormatting.RED + " totem!");
            }
            else if (this.popList.get(event.getEntity().getName()) != null) {
                popCounter = this.popList.get(event.getEntity().getName());
                newPopCounter = ++popCounter;
                this.popList.put(event.getEntity().getName(), newPopCounter);
                Command.sendClientMessage(ChatFormatting.RED + event.getEntity().getName() + ChatFormatting.RED + " popped " + ChatFormatting.GREEN + newPopCounter + ChatFormatting.RED + " totems!");
            }
            return;
        }, (Predicate<TotemPopEvent>[])new Predicate[0]);
        SPacketEntityStatus packet;
        Entity entity;
        this.totemPopListener = new Listener<PacketEvent.Receive>(event -> {
            if (TotemCounter.mc.world != null && TotemCounter.mc.player != null) {
                if (event.getPacket() instanceof SPacketEntityStatus) {
                    packet = (SPacketEntityStatus)event.getPacket();
                    if (packet.getOpCode() == 35) {
                        entity = packet.getEntity((World)TotemCounter.mc.world);
                        Xannax.EVENT_BUS.post(new TotemPopEvent(entity));
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        for (final EntityPlayer player : TotemCounter.mc.world.playerEntities) {
            if (player.getHealth() <= 0.0f && this.popList.containsKey(player.getName())) {
                Command.sendClientMessage(ChatFormatting.RED + player.getName() + ChatFormatting.RED + " died after popping " + ChatFormatting.GREEN + this.popList.get(player.getName()) + ChatFormatting.RED + " totems!");
                this.popList.remove(player.getName(), this.popList.get(player.getName()));
            }
        }
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
        this.popList = new HashMap<String, Integer>();
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}
