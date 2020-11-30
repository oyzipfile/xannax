// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.XannaxRPC;
import me.zoom.xannax.module.Module;

public class RPCModule extends Module
{
    public RPCModule() {
        super("DiscordRPC", "DiscordRPC", Category.Misc);
    }
    
    public void onEnable() {
        XannaxRPC.init();
        if (RPCModule.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.GREEN + "DiscordRPC started!");
        }
    }
    
    public void onDisable() {
        XannaxRPC.shutdown();
        Command.sendClientMessage(ChatFormatting.RED + "DiscordRPC shutdown!");
    }
}
