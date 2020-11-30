// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import me.zoom.xannax.Xannax;
import net.minecraft.util.MovementInput;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.client.event.InputUpdateEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class NoSlow extends Module
{
    public Setting.Boolean guiMove;
    public Setting.Boolean noSlow;
    @EventHandler
    private final Listener<InputUpdateEvent> eventListener;
    
    public NoSlow() {
        super("NoSlow", "NoSlow", Category.Movement);
        final MovementInput movementInput;
        final MovementInput movementInput2;
        this.eventListener = new Listener<InputUpdateEvent>(event -> {
            if (NoSlow.mc.player.isHandActive() && !NoSlow.mc.player.isRiding()) {
                event.getMovementInput();
                movementInput.moveStrafe *= 5.0f;
                event.getMovementInput();
                movementInput2.moveForward *= 5.0f;
            }
        }, (Predicate<InputUpdateEvent>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        this.noSlow = this.registerBoolean("SoulSand", "SoulSand", false);
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}
