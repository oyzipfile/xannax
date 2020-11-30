// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

public class Animation
{
    public static float moveTowards(final float current, final float end, final float minSpeed) {
        final float defaultSpeed = 0.125f;
        return moveTowards(current, end, defaultSpeed, minSpeed);
    }
    
    public static float moveTowards(final float current, final float end, final float smoothSpeed, final float minSpeed) {
        float movement = (end - current) * smoothSpeed;
        if (movement > 0.0f) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        }
        else if (movement < 0.0f) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }
        return current + movement;
    }
}
