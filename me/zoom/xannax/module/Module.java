// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module;

import java.util.List;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.event.events.RenderEvent;
import me.zoom.xannax.setting.Setting;
import net.minecraft.client.Minecraft;

public class Module
{
    protected static final Minecraft mc;
    String name;
    Category category;
    int bind;
    boolean enabled;
    public Setting.Boolean drawn;
    String description;
    public float animPos;
    
    public Module(final String n, final String desc, final Category c) {
        this.animPos = -1.0f;
        this.name = n;
        this.category = c;
        this.bind = 0;
        this.enabled = false;
        this.description = desc;
        this.setup();
        this.drawn = this.registerBoolean("Drawn", "Drawn", true);
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String n) {
        this.name = n;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public void setCategory(final Category c) {
        this.category = c;
    }
    
    public int getBind() {
        return this.bind;
    }
    
    public void setBind(final int b) {
        this.bind = b;
    }
    
    protected void onEnable() {
    }
    
    protected void onDisable() {
    }
    
    public void onUpdate() {
    }
    
    public void onRender() {
    }
    
    public void onWorldRender(final RenderEvent event) {
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean e) {
        this.enabled = e;
    }
    
    public void enable() {
        this.setEnabled(true);
        this.animPos = -1.0f;
        this.onEnable();
    }
    
    public void disable() {
        this.setEnabled(false);
        this.onDisable();
    }
    
    public void toggle() {
        if (this.isEnabled()) {
            this.disable();
        }
        else if (!this.isEnabled()) {
            this.enable();
        }
    }
    
    public String getHudInfo() {
        return "";
    }
    
    public void setup() {
    }
    
    public boolean isDrawn() {
        return this.drawn.getValue();
    }
    
    public void setDrawn(final boolean d) {
        this.drawn.setValue(d);
    }
    
    protected Setting.Integer registerInteger(final String name, final String configname, final int value, final int min, final int max) {
        final Setting.Integer s = new Setting.Integer(name, configname, this, this.getCategory(), value, min, max);
        Xannax.getInstance().settingsManager.addSetting(s);
        return s;
    }
    
    protected Setting.Double registerDouble(final String name, final String configname, final double value, final double min, final double max) {
        final Setting.Double s = new Setting.Double(name, configname, this, this.getCategory(), value, min, max);
        Xannax.getInstance().settingsManager.addSetting(s);
        return s;
    }
    
    protected Setting.Boolean registerBoolean(final String name, final String configname, final boolean value) {
        final Setting.Boolean s = new Setting.Boolean(name, configname, this, this.getCategory(), value);
        Xannax.getInstance().settingsManager.addSetting(s);
        return s;
    }
    
    protected Setting.Mode registerMode(final String name, final String configname, final List<String> modes, final String value) {
        final Setting.Mode s = new Setting.Mode(name, configname, this, this.getCategory(), modes, value);
        Xannax.getInstance().settingsManager.addSetting(s);
        return s;
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public enum Category
    {
        Combat, 
        Player, 
        Movement, 
        Misc, 
        Render, 
        Client;
    }
}
