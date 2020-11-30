// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.command.commands;

import me.zoom.xannax.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.command.Command;

public class ToggleCommand extends Command
{
    boolean found;
    
    @Override
    public String[] getAlias() {
        return new String[] { "toggle", "t" };
    }
    
    @Override
    public String getSyntax() {
        return "toggle <Module>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        this.found = false;
        ModuleManager.getModules().forEach(m -> {
            if (m.getName().equalsIgnoreCase(args[0])) {
                if (m.isEnabled()) {
                    m.disable();
                    this.found = true;
                    Command.sendClientMessage(ChatFormatting.RED + m.getName() + " disabled!");
                }
                else if (!m.isEnabled()) {
                    m.enable();
                    this.found = true;
                    Command.sendClientMessage(ChatFormatting.GREEN + m.getName() + " enabled!");
                }
            }
            return;
        });
        if (!this.found && args.length == 1) {
            Command.sendClientMessage(ChatFormatting.GRAY + "Module not found!");
        }
    }
}
