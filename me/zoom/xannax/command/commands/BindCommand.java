// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.command.commands;

import me.zoom.xannax.module.Module;
import me.zoom.xannax.module.ModuleManager;
import org.lwjgl.input.Keyboard;
import me.zoom.xannax.command.Command;

public class BindCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "bind", "b" };
    }
    
    @Override
    public String getSyntax() {
        return "bind <Module> <Key>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        final int key = Keyboard.getKeyIndex(args[1].toUpperCase());
        final int bind;
        ModuleManager.getModules().forEach(m -> {
            if (args[0].equalsIgnoreCase(m.getName())) {
                m.setBind(bind);
                Command.sendClientMessage(args[0] + " bound to " + args[1].toUpperCase());
            }
        });
    }
}
