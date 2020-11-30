// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui2.frame;

import me.zoom.xannax.clickgui2.ClickGUI2;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.util.font.FontUtils;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import me.zoom.xannax.clickgui.ClickGUI;
import net.minecraft.util.ResourceLocation;
import me.zoom.xannax.module.modules.client.Rainbow;
import net.minecraft.client.gui.FontRenderer;
import java.util.Iterator;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.client.Minecraft;
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
    int nameWidth;
    int centeredNameCoords;
    boolean font;
    int rainbowOffset;
    int scrolloff;
    int buttonHeight;
    public int buttonHeightProper;
    int mouseX;
    int mouseY;
    boolean hovering;
    int animation;
    Minecraft mc;
    
    public Frames(final Module.Category catg) {
        this.mc = Minecraft.getMinecraft();
        this.guicomponents = new ArrayList<Component>();
        this.category = catg;
        this.open = true;
        this.isDragging = false;
        this.x = 10;
        this.y = 34;
        this.dragX = 0;
        this.width = 100;
        this.barHeight = 16;
        int tY = this.barHeight;
        this.nameWidth = 0;
        this.centeredNameCoords = 0;
        this.rainbowOffset = 0;
        this.buttonHeight = 0;
        this.buttonHeightProper = 0;
        this.mouseX = 0;
        this.mouseY = 0;
        this.hovering = false;
        this.animation = 50;
        this.scrolloff = 0;
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
        this.rainbowOffset = Rainbow.getRainbowOffset();
        this.mc.renderEngine.bindTexture(new ResourceLocation("minecraft:rainbow.png"));
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, ClickGUI.color);
        Renderer.renderImage(this.x - 1, this.y - 1, this.x + 1, this.y + this.rainbowOffset, this.width + 1, this.barHeight, 1920.0f, 1080.0f);
        GlStateManager.pushMatrix();
        Renderer.RenderBoxOutline(3.0, this.x - 1, this.y - 1, this.x + this.width, this.y + this.barHeight - 1, new Color(0, 0, 0));
        this.nameWidth = FontUtils.getStringWidth(false, this.category.name());
        this.centeredNameCoords = (this.width - this.nameWidth) / 2;
        GlStateManager.popMatrix();
        FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.category.name(), this.x + this.centeredNameCoords, this.y + 3, Renderer.getFontColor(this.hovering, this.animation).getRGB());
        if (this.open && !this.guicomponents.isEmpty()) {
            Renderer.scissorBox(this.x, this.y + this.barHeight, this.width, 100, Xannax.mc.displayHeight);
            GL11.glEnable(3089);
            for (final Component component : this.guicomponents) {
                component.renderComponent();
                this.buttonHeight += component.getButtonHeight();
            }
            GL11.glDisable(3089);
            this.buttonHeightProper = this.buttonHeight;
            this.buttonHeight = 0;
        }
    }
    
    public int getRainbowOffset() {
        return this.rainbowOffset;
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
    
    public void updatePosition(final int mouseX, final int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }
    
    public boolean isWithinHeader(final int x, final int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y - 1 && y <= this.y + this.barHeight + 1;
    }
    
    public boolean isWithinFrame(final int x, final int y) {
        return this.isWithinHeader(this.mouseX, this.mouseY) || (x >= this.x && x <= this.x + this.width && y >= this.y - 1 && y <= this.y + this.barHeight + this.buttonHeightProper);
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
        if (this.isMouseInFrame(this.mouseX, this.mouseY)) {
            for (final Frames frames : ClickGUI2.frames) {
                if (scrollWheel < 0) {
                    frames.setY(frames.getY() - 5);
                }
                else {
                    if (scrollWheel <= 0) {
                        continue;
                    }
                    frames.setY(frames.getY() + 5);
                }
            }
        }
    }
    
    public boolean isMouseInFrame(final int x, final int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y + this.barHeight && y <= this.y + this.barHeight + 100;
    }
}
