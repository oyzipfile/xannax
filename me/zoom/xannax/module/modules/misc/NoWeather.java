// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.module.Module;

public class NoWeather extends Module
{
    public NoWeather() {
        super("NoWeather", "Rain rain go away", Category.Misc);
    }
    
    @Override
    public void onUpdate() {
        if (NoWeather.mc.world == null) {
            return;
        }
        if (NoWeather.mc.world.isRaining()) {
            NoWeather.mc.world.setRainStrength(0.0f);
        }
    }
}
