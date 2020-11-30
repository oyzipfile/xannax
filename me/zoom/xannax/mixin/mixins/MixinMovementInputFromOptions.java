// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import me.zoom.xannax.clickgui.ClickGUI;
import net.minecraft.client.gui.GuiIngameMenu;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import me.zoom.xannax.util.Wrapper;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mutable;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.util.MovementInput;

@Mixin(value = { MovementInputFromOptions.class }, priority = 10000)
public abstract class MixinMovementInputFromOptions extends MovementInput
{
    @Mutable
    @Shadow
    @Final
    private final GameSettings gameSettings;
    
    protected MixinMovementInputFromOptions(final GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }
    
    @Overwrite
    public void updatePlayerMoveState() {
        this.moveStrafe = 0.0f;
        this.moveForward = 0.0f;
        if (this.isKeyHeld(this.gameSettings.keyBindForward)) {
            ++this.moveForward;
            this.forwardKeyDown = true;
        }
        else {
            this.forwardKeyDown = false;
        }
        if (this.isKeyHeld(this.gameSettings.keyBindBack)) {
            --this.moveForward;
            this.backKeyDown = true;
        }
        else {
            this.backKeyDown = false;
        }
        if (this.isKeyHeld(this.gameSettings.keyBindLeft)) {
            ++this.moveStrafe;
            this.leftKeyDown = true;
        }
        else {
            this.leftKeyDown = false;
        }
        if (this.isKeyHeld(this.gameSettings.keyBindRight)) {
            --this.moveStrafe;
            this.rightKeyDown = true;
        }
        else {
            this.rightKeyDown = false;
        }
        this.jump = this.isKeyHeld(this.gameSettings.keyBindJump);
        this.sneak = this.isKeyHeld(this.gameSettings.keyBindSneak);
        if (this.sneak) {
            this.moveStrafe *= (float)0.3;
            this.moveForward *= (float)0.3;
        }
    }
    
    public boolean isKeyHeld(final KeyBinding keyBinding) {
        if (ModuleManager.isModuleEnabled("GuiMove") && Wrapper.getMinecraft().currentScreen != null) {
            if (Wrapper.getMinecraft().currentScreen instanceof InventoryEffectRenderer) {
                return Keyboard.isKeyDown(keyBinding.getKeyCode());
            }
            if (Wrapper.getMinecraft().world.isRemote && Wrapper.getMinecraft().currentScreen instanceof GuiIngameMenu) {
                return Keyboard.isKeyDown(keyBinding.getKeyCode());
            }
            if (Wrapper.getMinecraft().currentScreen instanceof ClickGUI) {
                return Keyboard.isKeyDown(keyBinding.getKeyCode());
            }
        }
        return keyBinding.isKeyDown();
    }
}
