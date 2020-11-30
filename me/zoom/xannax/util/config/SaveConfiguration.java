// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util.config;

import me.zoom.xannax.module.Module;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.util.enemy.Enemy;
import me.zoom.xannax.util.enemy.Enemies;
import me.zoom.xannax.util.friend.Friend;
import me.zoom.xannax.util.friend.Friends;
import org.lwjgl.input.Keyboard;
import me.zoom.xannax.macro.Macro;
import me.zoom.xannax.Xannax;
import java.util.Iterator;
import me.zoom.xannax.clickgui.Frames;
import me.zoom.xannax.clickgui.ClickGUI;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import net.minecraft.client.Minecraft;

public class SaveConfiguration
{
    Minecraft mc;
    public static File GameSenseDev;
    public static File Modules;
    public static File Messages;
    public static File Miscellaneous;
    public static File Combat;
    public static File Player;
    public static File Client;
    public static File Misc;
    public static File Movement;
    public static File Render;
    
    public SaveConfiguration() {
        this.mc = Minecraft.getMinecraft();
        SaveConfiguration.GameSenseDev = new File(this.mc.gameDir + File.separator + "XannaX");
        if (!SaveConfiguration.GameSenseDev.exists()) {
            SaveConfiguration.GameSenseDev.mkdirs();
        }
        SaveConfiguration.Modules = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Modules");
        if (!SaveConfiguration.Modules.exists()) {
            SaveConfiguration.Modules.mkdirs();
        }
        SaveConfiguration.Messages = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Messages");
        if (!SaveConfiguration.Messages.exists()) {
            SaveConfiguration.Messages.mkdirs();
        }
        SaveConfiguration.Miscellaneous = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Miscellaneous");
        if (!SaveConfiguration.Miscellaneous.exists()) {
            SaveConfiguration.Miscellaneous.mkdirs();
        }
        SaveConfiguration.Combat = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Modules" + File.separator + "Combat");
        if (!SaveConfiguration.Combat.exists()) {
            SaveConfiguration.Combat.mkdirs();
        }
        SaveConfiguration.Player = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Modules" + File.separator + "Player");
        if (!SaveConfiguration.Player.exists()) {
            SaveConfiguration.Player.mkdirs();
        }
        SaveConfiguration.Client = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Modules" + File.separator + "Client");
        if (!SaveConfiguration.Client.exists()) {
            SaveConfiguration.Client.mkdirs();
        }
        SaveConfiguration.Misc = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Modules" + File.separator + "Misc");
        if (!SaveConfiguration.Misc.exists()) {
            SaveConfiguration.Misc.mkdirs();
        }
        SaveConfiguration.Movement = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Modules" + File.separator + "Movement");
        if (!SaveConfiguration.Movement.exists()) {
            SaveConfiguration.Movement.mkdirs();
        }
        SaveConfiguration.Render = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Modules" + File.separator + "Render");
        if (!SaveConfiguration.Render.exists()) {
            SaveConfiguration.Render.mkdirs();
        }
    }
    
    public static void saveGUI() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "ClickGUI.json");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Frames frames : ClickGUI.frames) {
                out.write(frames.category + ":" + frames.getX() + ":" + frames.getY() + ":" + frames.isOpen());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveMacros() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "ClientMacros.json");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Macro m : Xannax.getInstance().macroManager.getMacros()) {
                out.write(Keyboard.getKeyName(m.getKey()) + ":" + m.getValue().replace(" ", "_"));
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveFriends() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "Friends.json");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Friend f : Friends.getFriends()) {
                out.write(f.getName());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveEnemies() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "Enemies.json");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Enemy e : Enemies.getEnemies()) {
                out.write(e.getName());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public static void savePrefix() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "CommandPrefix.json");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(Command.getPrefix());
            out.write("\r\n");
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveFont() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "CustomFont.json");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(Xannax.fontRenderer.getFontName() + ":" + Xannax.fontRenderer.getFontSize());
            out.write("\r\n");
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveMessages() {
        try {
            final File file = new File(SaveConfiguration.Messages.getAbsolutePath(), "ClientMessages.json");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(Command.MsgWaterMark + "");
            out.write(",");
            out.write(Command.cf.getName());
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveDrawn() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "DrawnModules.json");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Module module : ModuleManager.getModules()) {
                out.write(module.getName() + ":" + module.isDrawn());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveEnabled() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "EnabledModules.json");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Module module : ModuleManager.getModules()) {
                if (module.isEnabled()) {
                    out.write(module.getName());
                    out.write("\r\n");
                }
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveBinds() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "ModuleBinds.json");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Module module : ModuleManager.getModules()) {
                out.write(module.getName() + ":" + Keyboard.getKeyName(module.getBind()));
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
}
