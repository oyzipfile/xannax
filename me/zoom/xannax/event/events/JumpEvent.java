// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event.events;

import me.zoom.xannax.util.Location;
import me.zoom.xannax.event.Event;

public class JumpEvent extends Event
{
    private Location location;
    
    public JumpEvent(final Location location) {
        this.location = location;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public void setLocation(final Location location) {
        this.location = location;
    }
}
