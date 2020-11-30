// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.command.commands;

import me.zoom.xannax.Xannax;
import me.zoom.xannax.util.font.CFontRenderer;
import java.awt.Font;
import me.zoom.xannax.command.Command;

public class FontCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "font", "setfont" };
    }
    
    @Override
    public String getSyntax() {
        return "font <Name> <Size>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        final String font = args[0].replace("_", " ");
        final int size = Integer.parseInt(args[1]);
        (Xannax.fontRenderer = new CFontRenderer(new Font(font, 0, size), true, false)).setFontName(font);
        Xannax.fontRenderer.setFontSize(size);
    }
}
