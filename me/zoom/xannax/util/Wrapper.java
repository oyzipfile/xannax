// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import org.lwjgl.input.Keyboard;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class Wrapper
{
    private static FontRenderer fontRenderer;
    public static Minecraft mc;
    
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
    
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }
    
    public static Entity getRenderEntity() {
        return Wrapper.mc.getRenderViewEntity();
    }
    
    public static World getWorld() {
        return (World)getMinecraft().world;
    }
    
    public static int getKey(final String keyname) {
        return Keyboard.getKeyIndex(keyname.toUpperCase());
    }
    
    public static FontRenderer getFontRenderer() {
        return Wrapper.fontRenderer;
    }
    
    static {
        Wrapper.mc = Minecraft.getMinecraft();
    }
}
