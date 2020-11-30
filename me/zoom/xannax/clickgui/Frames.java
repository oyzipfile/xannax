// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui;

import org.lwjgl.input.Mouse;
import me.zoom.xannax.util.font.FontUtils;
import me.zoom.xannax.clickgui2.frame.Renderer;
import me.zoom.xannax.Xannax;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.FontRenderer;
import java.util.Iterator;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.module.Module;
import java.util.ArrayList;

public class Frames
{
    public ArrayList<Component> guicomponents;
    public Module.Category category;
    private final int width;
    private final int barHeight;
    private int height;
    public int x;
    public int y;
    public int dragX;
    public int dragY;
    private boolean isDragging;
    public boolean open;
    public int buttonHeightProper;
    boolean font;
    int mouseX;
    int mouseY;
    boolean hovering;
    int animation;
    
    public Frames(final Module.Category catg) {
        this.guicomponents = new ArrayList<Component>();
        this.category = catg;
        this.open = true;
        this.isDragging = false;
        this.x = 5;
        this.y = 5;
        this.dragX = 0;
        this.width = 100;
        this.barHeight = 16;
        int tY = this.barHeight;
        this.buttonHeightProper = 0;
        this.mouseX = 0;
        this.mouseY = 0;
        this.hovering = false;
        this.animation = 50;
        for (final Module mod : ModuleManager.getModulesInCategory(catg)) {
            final Buttons devmodButton = new Buttons(mod, this, tY);
            this.guicomponents.add(devmodButton);
            tY += 16;
        }
        this.refresh();
    }
    
    public ArrayList<Component> getComponents() {
        return this.guicomponents;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setX(final int newX) {
        this.x = newX;
    }
    
    public void setY(final int newY) {
        this.y = newY;
    }
    
    public void renderGUIFrame(final FontRenderer fontRenderer) {
        this.hovering = this.isWithinFrame(this.mouseX, this.mouseY);
        if (this.hovering && this.animation > 0) {
            this.animation -= 2;
        }
        if (!this.hovering && this.animation < 50) {
            this.animation += 2;
        }
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, ClickGUI.color);
        if (this.font) {
            Xannax.fontRenderer.drawStringWithShadow(this.category.name(), (float)(this.x + 2), (float)(this.y + 3), -1);
        }
        else {
            FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.category.name(), this.x + 2, this.y + 3, Renderer.getFontColor(this.hovering, this.animation).getRGB());
        }
        if (this.open && !this.guicomponents.isEmpty()) {
            for (final Component component : this.guicomponents) {
                component.renderComponent();
            }
        }
    }
    
    public int getAnimation() {
        return this.animation;
    }
    
    public boolean isHovering() {
        return this.hovering;
    }
    
    public void setHovering(final boolean hovering) {
        this.hovering = hovering;
    }
    
    public int getMouseX() {
        return this.mouseX;
    }
    
    public int getMouseY() {
        return this.mouseY;
    }
    
    public boolean isWithinFrame(final int x, final int y) {
        return this.isWithinHeader(this.mouseX, this.mouseY) || (x >= this.x && x <= this.x + this.width && y >= this.y - 1 && y <= this.y + this.barHeight + this.buttonHeightProper);
    }
    
    public void updatePosition(final int mouseX, final int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
    }
    
    public boolean isWithinHeader(final int x, final int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }
    
    public void setDrag(final boolean drag) {
        this.isDragging = drag;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public void refresh() {
        int off = this.barHeight;
        for (final Component comp : this.guicomponents) {
            comp.setOff(off);
            off += comp.getHeight();
        }
        this.height = off;
    }
    
    public void updateMouseWheel() {
        final int scrollWheel = Mouse.getDWheel();
        for (final Frames frames : ClickGUI.frames) {
            if (scrollWheel < 0) {
                frames.setY(frames.getY() - 10);
            }
            else {
                if (scrollWheel <= 0) {
                    continue;
                }
                frames.setY(frames.getY() + 10);
            }
        }
    }
}
