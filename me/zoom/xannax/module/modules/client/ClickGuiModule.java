// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.module.modules.misc.Announcer;
import net.minecraft.client.gui.GuiScreen;
import me.zoom.xannax.Xannax;
import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class ClickGuiModule extends Module
{
    public static Setting.Integer opacity;
    public static Setting.Integer red;
    public static Setting.Integer green;
    public static Setting.Integer blue;
    public static Setting.Mode CommandColor;
    public static Setting.Mode BracketColor;
    
    public ClickGuiModule() {
        super("ClickGUI", "ClickGUI", Category.Client);
        this.setDrawn(false);
        this.setBind(23);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> colors = new ArrayList<String>();
        colors.add("Black");
        colors.add("Dark Green");
        colors.add("Dark Red");
        colors.add("Gold");
        colors.add("Dark Gray");
        colors.add("Green");
        colors.add("Red");
        colors.add("Yellow");
        colors.add("Dark Blue");
        colors.add("Dark Aqua");
        colors.add("Dark Purple");
        colors.add("Gray");
        colors.add("Blue");
        colors.add("Aqua");
        colors.add("Light Purple");
        colors.add("White");
        final ArrayList<String> brackets = new ArrayList<String>();
        brackets.add("Black");
        brackets.add("Dark Green");
        brackets.add("Dark Red");
        brackets.add("Gold");
        brackets.add("Dark Gray");
        brackets.add("Green");
        brackets.add("Red");
        brackets.add("Yellow");
        brackets.add("Dark Blue");
        brackets.add("Dark Aqua");
        brackets.add("Dark Purple");
        brackets.add("Gray");
        brackets.add("Blue");
        brackets.add("Aqua");
        brackets.add("Light Purple");
        brackets.add("White");
        ClickGuiModule.red = this.registerInteger("Red", "RedHUD", 255, 0, 255);
        ClickGuiModule.green = this.registerInteger("Green", "GreenHUD", 255, 0, 255);
        ClickGuiModule.blue = this.registerInteger("Blue", "BlueHUD", 255, 0, 255);
        ClickGuiModule.opacity = this.registerInteger("Opacity", "Opacity", 200, 50, 255);
        ClickGuiModule.CommandColor = this.registerMode("Text", "Color", colors, "Light Purple");
        ClickGuiModule.BracketColor = this.registerMode("Brackets", "BracketColor", brackets, "Light Purple");
    }
    
    public void onEnable() {
        ClickGuiModule.mc.displayGuiScreen((GuiScreen)Xannax.getInstance().clickGUI);
        if (((Announcer)ModuleManager.getModuleByName("Announcer")).clickGui.getValue() && ModuleManager.isModuleEnabled("Announcer") && ClickGuiModule.mc.player != null) {
            if (((Announcer)ModuleManager.getModuleByName("Announcer")).clientSide.getValue()) {
                Command.sendClientMessage(Announcer.guiMessage);
            }
            else {
                ClickGuiModule.mc.player.sendChatMessage(Announcer.guiMessage);
            }
        }
        this.disable();
    }
    
    public static ChatFormatting getTextColor() {
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("Black")) {
            return ChatFormatting.BLACK;
        }
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("Dark Green")) {
            return ChatFormatting.DARK_GREEN;
        }
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("Dark Red")) {
            return ChatFormatting.DARK_RED;
        }
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("Gold")) {
            return ChatFormatting.GOLD;
        }
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("Dark Gray")) {
            return ChatFormatting.DARK_GRAY;
        }
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("Green")) {
            return ChatFormatting.GREEN;
        }
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("Red")) {
            return ChatFormatting.RED;
        }
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("Yellow")) {
            return ChatFormatting.YELLOW;
        }
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("Dark Blue")) {
            return ChatFormatting.DARK_BLUE;
        }
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("Dark Aqua")) {
            return ChatFormatting.DARK_AQUA;
        }
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("Dark Purple")) {
            return ChatFormatting.DARK_PURPLE;
        }
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("Gray")) {
            return ChatFormatting.GRAY;
        }
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("Blue")) {
            return ChatFormatting.BLUE;
        }
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("Light Purple")) {
            return ChatFormatting.LIGHT_PURPLE;
        }
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("White")) {
            return ChatFormatting.WHITE;
        }
        if (ClickGuiModule.CommandColor.getValue().equalsIgnoreCase("Aqua")) {
            return ChatFormatting.AQUA;
        }
        return null;
    }
    
    public static ChatFormatting getBrackets() {
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("Black")) {
            return ChatFormatting.BLACK;
        }
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("Dark Green")) {
            return ChatFormatting.DARK_GREEN;
        }
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("Dark Red")) {
            return ChatFormatting.DARK_RED;
        }
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("Gold")) {
            return ChatFormatting.GOLD;
        }
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("Dark Gray")) {
            return ChatFormatting.DARK_GRAY;
        }
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("Green")) {
            return ChatFormatting.GREEN;
        }
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("Red")) {
            return ChatFormatting.RED;
        }
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("Yellow")) {
            return ChatFormatting.YELLOW;
        }
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("Dark Blue")) {
            return ChatFormatting.DARK_BLUE;
        }
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("Dark Aqua")) {
            return ChatFormatting.DARK_AQUA;
        }
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("Dark Purple")) {
            return ChatFormatting.DARK_PURPLE;
        }
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("Gray")) {
            return ChatFormatting.GRAY;
        }
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("Blue")) {
            return ChatFormatting.BLUE;
        }
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("Light Purple")) {
            return ChatFormatting.LIGHT_PURPLE;
        }
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("White")) {
            return ChatFormatting.WHITE;
        }
        if (ClickGuiModule.BracketColor.getValue().equalsIgnoreCase("Aqua")) {
            return ChatFormatting.AQUA;
        }
        return null;
    }
}
