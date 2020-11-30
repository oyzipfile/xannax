// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.command.commands;

import me.zoom.xannax.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.command.Command;

public class DrawnCommand extends Command
{
    boolean found;
    
    @Override
    public String[] getAlias() {
        return new String[] { "drawn", "visible", "d" };
    }
    
    @Override
    public String getSyntax() {
        return "drawn <Module>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        this.found = false;
        ModuleManager.getModules().forEach(m -> {
            if (m.getName().equalsIgnoreCase(args[0])) {
                if (m.isDrawn()) {
                    m.setDrawn(false);
                    this.found = true;
                    Command.sendClientMessage(m.getName() + ChatFormatting.RED + " drawn = false");
                }
                else if (!m.isDrawn()) {
                    m.setDrawn(true);
                    this.found = true;
                    Command.sendClientMessage(m.getName() + ChatFormatting.GREEN + " drawn = true");
                }
            }
            return;
        });
        if (!this.found && args.length == 1) {
            Command.sendClientMessage(ChatFormatting.GRAY + "Module not found!");
        }
    }
}
