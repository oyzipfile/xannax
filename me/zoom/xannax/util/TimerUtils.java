// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

public class TimerUtils
{
    private long time;
    
    public TimerUtils() {
        this.time = -1L;
    }
    
    public boolean passed(final long ms) {
        return this.getTime(System.nanoTime() - this.time) >= ms;
    }
    
    public void reset() {
        this.time = System.nanoTime();
    }
    
    public long getTime(final long time) {
        return time / 1000000L;
    }
    
    public boolean passedMs(final long ms) {
        return this.getMs(System.nanoTime() - this.time) >= ms;
    }
    
    public long getMs(final long time) {
        return time / 1000000L;
    }
}
