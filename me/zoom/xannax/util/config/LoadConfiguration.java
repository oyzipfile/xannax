// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util.config;

import java.util.Iterator;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.module.ModuleManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.util.font.CFontRenderer;
import java.awt.Font;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.util.enemy.Enemies;
import me.zoom.xannax.util.friend.Friends;
import me.zoom.xannax.macro.Macro;
import org.lwjgl.input.Keyboard;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.clickgui.Frames;
import me.zoom.xannax.clickgui.ClickGUI;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.File;

public class LoadConfiguration
{
    public LoadConfiguration() {
        this.loadBinds();
        this.loadDrawn();
        this.loadEnabled();
        this.loadEnemies();
        this.loadFont();
        this.loadFriends();
        this.loadGUI();
        this.loadMacros();
        this.loadMessages();
        this.loadPrefix();
    }
    
    public void loadGUI() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "ClickGUI.json");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String name = curLine.split(":")[0];
                final String x = curLine.split(":")[1];
                final String y = curLine.split(":")[2];
                final String e = curLine.split(":")[3];
                final int x2 = Integer.parseInt(x);
                final int y2 = Integer.parseInt(y);
                final boolean open = Boolean.parseBoolean(e);
                final Frames frames = ClickGUI.getFrameByName(name);
                if (frames != null) {
                    frames.x = x2;
                    frames.y = y2;
                    frames.open = open;
                }
            }
            br.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
            SaveConfiguration.saveGUI();
        }
    }
    
    public void loadMacros() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "ClientMacros.json");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String bind = curLine.split(":")[0];
                final String value = curLine.split(":")[1];
                Xannax.getInstance().macroManager.addMacro(new Macro(Keyboard.getKeyIndex(bind), value.replace("_", " ")));
            }
            br.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
            SaveConfiguration.saveMacros();
        }
    }
    
    public void loadFriends() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "Friends.json");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Friends.friends.clear();
            String line;
            while ((line = br.readLine()) != null) {
                Xannax.getInstance().friends.addFriend(line);
            }
            br.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
            SaveConfiguration.saveFriends();
        }
    }
    
    public void loadEnemies() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "Enemies.json");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Enemies.enemies.clear();
            String line;
            while ((line = br.readLine()) != null) {
                Enemies.addEnemy(line);
            }
            br.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
            SaveConfiguration.saveEnemies();
        }
    }
    
    public void loadPrefix() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "CommandPrefix.json");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                Command.setPrefix(line);
            }
            br.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
            SaveConfiguration.savePrefix();
        }
    }
    
    public void loadFont() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "CustomFont.json");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String name = line.split(":")[0];
                final String size = line.split(":")[1];
                final int sizeInt = Integer.parseInt(size);
                (Xannax.fontRenderer = new CFontRenderer(new Font(name, 0, sizeInt), true, false)).setFont(new Font(name, 0, sizeInt));
                Xannax.fontRenderer.setAntiAlias(true);
                Xannax.fontRenderer.setFractionalMetrics(false);
                Xannax.fontRenderer.setFontName(name);
                Xannax.fontRenderer.setFontSize(sizeInt);
            }
            br.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
            SaveConfiguration.saveFont();
        }
    }
    
    public void loadMessages() {
        try {
            final File file = new File(SaveConfiguration.Messages.getAbsolutePath(), "ClientMessages.json");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String watermark = curLine.split(",")[0];
                final String color = curLine.split(",")[1];
                final boolean w = Boolean.parseBoolean(watermark);
                final ChatFormatting c = Command.cf = ChatFormatting.getByName(color);
                Command.MsgWaterMark = w;
            }
            br.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
            SaveConfiguration.saveMessages();
        }
    }
    
    public void loadDrawn() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "DrawnModules.json");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String name = curLine.split(":")[0];
                final String isOn = curLine.split(":")[1];
                final boolean drawn = Boolean.parseBoolean(isOn);
                for (final Module m : ModuleManager.getModules()) {
                    if (m.getName().equalsIgnoreCase(name)) {
                        m.setDrawn(drawn);
                    }
                }
            }
            br.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
            SaveConfiguration.saveDrawn();
        }
    }
    
    public void loadEnabled() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "EnabledModules.json");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                for (final Module m : ModuleManager.getModules()) {
                    if (m.getName().equals(line)) {
                        m.enable();
                    }
                }
            }
            br.close();
        }
        catch (Exception var7) {
            var7.printStackTrace();
            SaveConfiguration.saveEnabled();
        }
    }
    
    public void loadBinds() {
        try {
            final File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "ModuleBinds.json");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String name = curLine.split(":")[0];
                final String bind = curLine.split(":")[1];
                for (final Module m : ModuleManager.getModules()) {
                    if (m != null && m.getName().equalsIgnoreCase(name)) {
                        m.setBind(Keyboard.getKeyIndex(bind));
                    }
                }
            }
            br.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
            SaveConfiguration.saveBinds();
        }
    }
}
