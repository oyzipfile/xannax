// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event.events;

import java.util.UUID;
import me.zoom.xannax.event.Event;

public class PlayerJoinEvent extends Event
{
    private final String name;
    private final UUID uuid;
    
    public PlayerJoinEvent(final String n, final UUID id) {
        this.name = n;
        this.uuid = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
}
