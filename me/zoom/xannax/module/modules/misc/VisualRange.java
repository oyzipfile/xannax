// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import java.util.Iterator;
import me.zoom.xannax.command.Command;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import java.util.List;
import me.zoom.xannax.module.Module;

public class VisualRange extends Module
{
    List<Entity> knownPlayers;
    List<Entity> players;
    
    public VisualRange() {
        super("VisualRange", "Sends a message when someone enters your visual range", Category.Misc);
        this.knownPlayers = new ArrayList<Entity>();
    }
    
    @Override
    public void onUpdate() {
        if (VisualRange.mc.player == null) {
            return;
        }
        this.players = (List<Entity>)VisualRange.mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityPlayer).collect(Collectors.toList());
        try {
            for (final Entity e2 : this.players) {
                if (e2 instanceof EntityPlayer && !e2.getName().equalsIgnoreCase(VisualRange.mc.player.getName()) && !this.knownPlayers.contains(e2)) {
                    this.knownPlayers.add(e2);
                    Command.sendClientMessage(e2.getName() + " entered visual range.");
                }
            }
        }
        catch (Exception ex) {}
        try {
            for (final Entity e2 : this.knownPlayers) {
                if (e2 instanceof EntityPlayer && !e2.getName().equalsIgnoreCase(VisualRange.mc.player.getName()) && !this.players.contains(e2)) {
                    this.knownPlayers.remove(e2);
                }
            }
        }
        catch (Exception ex2) {}
    }
    
    public void onDisable() {
        this.knownPlayers.clear();
    }
}
