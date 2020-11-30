// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event.events;

import net.minecraft.entity.player.EntityPlayer;
import java.util.UUID;
import me.zoom.xannax.event.Event;

public class PlayerLeaveEvent extends Event
{
    private final String name;
    private final UUID uuid;
    private final EntityPlayer entity;
    
    public PlayerLeaveEvent(final String n, final UUID id, final EntityPlayer ent) {
        this.name = n;
        this.uuid = id;
        this.entity = ent;
    }
    
    public String getName() {
        return this.name;
    }
    
    public EntityPlayer getEntity() {
        return this.entity;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
}
