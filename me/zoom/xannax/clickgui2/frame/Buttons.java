// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui2.frame;

import org.lwjgl.opengl.GL11;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.util.font.FontUtils;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import me.zoom.xannax.module.modules.client.ClickGuiModule;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import me.zoom.xannax.clickgui2.buttons.KeybindComponent;
import me.zoom.xannax.clickgui2.buttons.IntegerComponent;
import me.zoom.xannax.clickgui2.buttons.DoubleComponent;
import me.zoom.xannax.clickgui2.buttons.BooleanComponent;
import me.zoom.xannax.clickgui2.buttons.ModeComponent;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.Xannax;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import me.zoom.xannax.module.Module;

public class Buttons extends Component
{
    public Module mod;
    public Frames parent;
    public int offset;
    private boolean isHovered;
    private final ArrayList<Component> subcomponents;
    public boolean open;
    private final int height;
    int nameWidth;
    int centeredNameCoords;
    public int settingHeight;
    public int animating;
    boolean hovering;
    Minecraft mc;
    private static final ResourceLocation opengui;
    
    public Buttons(final Module mod, final Frames parent, final int offset) {
        this.mc = Minecraft.getMinecraft();
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.subcomponents = new ArrayList<Component>();
        this.open = false;
        int opY = offset + 12;
        this.nameWidth = 0;
        this.centeredNameCoords = 0;
        this.settingHeight = 12;
        this.hovering = false;
        this.animating = 0;
        if (Xannax.getInstance().settingsManager.getSettingsForMod(mod) != null && !Xannax.getInstance().settingsManager.getSettingsForMod(mod).isEmpty()) {
            for (final Setting s : Xannax.getInstance().settingsManager.getSettingsForMod(mod)) {
                switch (s.getType()) {
                    case MODE: {
                        this.subcomponents.add(new ModeComponent((Setting.Mode)s, this, mod, opY));
                        break;
                    }
                    case BOOLEAN: {
                        this.subcomponents.add(new BooleanComponent((Setting.Boolean)s, this, opY));
                        break;
                    }
                    case DOUBLE: {
                        this.subcomponents.add(new DoubleComponent((Setting.Double)s, this, opY));
                        break;
                    }
                    case INT: {
                        this.subcomponents.add(new IntegerComponent((Setting.Integer)s, this, opY));
                        break;
                    }
                }
                opY += 12;
                this.settingHeight += 12;
            }
        }
        this.subcomponents.add(new KeybindComponent(this, opY));
        this.height = opY + 12 - offset;
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
        int opY = this.offset + 12;
        for (final Component comp : this.subcomponents) {
            comp.setOff(opY);
            opY += 12;
        }
    }
    
    @Override
    public void renderComponent() {
        if (this.mod.isEnabled()) {
            GlStateManager.pushMatrix();
            this.mc.renderEngine.bindTexture(new ResourceLocation("minecraft:rainbow.png"));
            Gui.drawRect(this.parent.getX(), this.parent.getY() + this.offset + this.parent.scrolloff, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + this.offset + 1 + this.parent.scrolloff, new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).getRGB());
            Renderer.renderImage(this.parent.getX(), this.parent.getY() + this.offset + this.parent.scrolloff, this.parent.getX(), this.parent.getY() + this.offset + this.parent.getRainbowOffset() + this.parent.scrolloff, this.parent.getWidth(), 12, 1920.0f, 1080.0f);
            Renderer.RenderBoxOutline(1.5, this.parent.getX(), this.parent.getY() + this.offset + this.parent.scrolloff, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 12 + this.offset + this.parent.scrolloff, new Color(0, 0, 0));
            GlStateManager.popMatrix();
        }
        else {
            Renderer.drawRectStatic(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 12 + this.offset, Renderer.getTransColor(false));
        }
        this.nameWidth = FontUtils.getStringWidth(false, this.mod.getName());
        this.centeredNameCoords = (this.parent.getWidth() - this.nameWidth) / 2;
        FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.mod.getName(), this.parent.getX() + 2, this.parent.getY() + this.offset + 2 + this.parent.scrolloff, Renderer.getFontColor(this.parent.isHovering(), this.parent.getAnimation()).getRGB());
        this.drawOpenRender(this.parent.getX() + this.parent.getWidth() - 12, this.parent.getY() + this.offset + 2 + this.parent.scrolloff);
        if (this.isMouseOnButton(this.parent.getMouseX(), this.parent.getMouseY() + this.parent.scrolloff) && this.mod.getDescription() != null) {
            Renderer.drawRectStatic(0, 0, FontUtils.getStringWidth(false, this.mod.getDescription()) + 5, 12, new Color(0, 0, 0, 150));
            FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.mod.getDescription(), 2, 2, new Color(255, 255, 255, 255).getRGB());
        }
        if (this.open && !this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.renderComponent();
            }
        }
    }
    
    @Override
    public int getHeight() {
        if (this.open) {
            return this.height;
        }
        return 12;
    }
    
    @Override
    public int getButtonHeight() {
        if (this.open) {
            return this.height;
        }
        return 12;
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.isHovered = this.isMouseOnButton(mouseX, mouseY);
        if (!this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.toggle();
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 1) {
            this.open = !this.open;
            this.parent.refresh();
        }
        for (final Component comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        for (final Component comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int key) {
        for (final Component comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
    }
    
    public void drawOpenRender(final int x, final int y) {
        GlStateManager.enableAlpha();
        this.mc.getTextureManager().bindTexture(Buttons.opengui);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        Gui.drawScaledCustomSizeModalRect(x, y, 0.0f, 0.0f, 512, 512, 8, 8, 512.0f, 512.0f);
        GL11.glPopMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
    }
    
    static {
        opengui = new ResourceLocation("minecraft:opengui.png");
    }
}
