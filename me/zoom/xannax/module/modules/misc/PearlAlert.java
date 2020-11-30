// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import java.util.Iterator;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import java.util.concurrent.ConcurrentHashMap;
import me.zoom.xannax.module.Module;

public class PearlAlert extends Module
{
    ConcurrentHashMap uuidMap;
    
    public PearlAlert() {
        super("PearlAlert", "PearlAlert", Category.Misc);
    }
    
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    public String getTitle(final String var1) {
        if (var1.equalsIgnoreCase("west")) {
            return "east";
        }
        return var1.equalsIgnoreCase("east") ? "west" : var1;
    }
    
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onUpdate() {
        for (final Entity var2 : PearlAlert.mc.world.loadedEntityList) {
            if (var2 instanceof EntityEnderPearl) {
                EntityPlayer var3 = null;
                for (final EntityPlayer var5 : PearlAlert.mc.world.playerEntities) {
                    if (var3 == null || var2.getDistance((Entity)var5) < var2.getDistance((Entity)var3)) {
                        var3 = var5;
                    }
                }
                if (var3 == null || var3.getDistance(var2) >= 2.0f || this.uuidMap.containsKey(var2.getUniqueID()) || var3.getName().equalsIgnoreCase(PearlAlert.mc.player.getName())) {
                    continue;
                }
                this.uuidMap.put(var2.getUniqueID(), 200);
                Command.sendClientMessage(String.valueOf(new StringBuilder().append(ChatFormatting.RED).append(var3.getName()).append(" threw a pearl towards ").append(this.getTitle(var2.getHorizontalFacing().getName())).append("!")));
            }
        }
        this.uuidMap.forEach((var1x, var2x) -> {
            if (var2x <= 0) {
                this.uuidMap.remove(var1x);
            }
            else {
                this.uuidMap.put(var1x, var2x - 1);
            }
        });
    }
    
    @Override
    public void setup() {
        this.uuidMap = new ConcurrentHashMap();
    }
}
