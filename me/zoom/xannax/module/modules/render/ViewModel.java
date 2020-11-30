// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.Xannax;
import java.util.function.Predicate;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.TransformSideFirstPersonEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class ViewModel extends Module
{
    public Setting.Boolean cancelEating;
    Setting.Double xRight;
    Setting.Double yRight;
    Setting.Double zRight;
    Setting.Double xLeft;
    Setting.Double yLeft;
    Setting.Double zLeft;
    @EventHandler
    private final Listener<TransformSideFirstPersonEvent> eventListener;
    
    public ViewModel() {
        super("ViewModel", "ViewModel", Category.Render);
        this.eventListener = new Listener<TransformSideFirstPersonEvent>(event -> {
            if (event.getHandSide() == EnumHandSide.RIGHT) {
                GlStateManager.translate(this.xRight.getValue(), this.yRight.getValue(), this.zRight.getValue());
            }
            else if (event.getHandSide() == EnumHandSide.LEFT) {
                GlStateManager.translate(this.xLeft.getValue(), this.yLeft.getValue(), this.zLeft.getValue());
            }
        }, (Predicate<TransformSideFirstPersonEvent>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        this.cancelEating = this.registerBoolean("No Eat", "NoEat", false);
        this.xLeft = this.registerDouble("Left X", "LeftX", 0.0, -2.0, 2.0);
        this.yLeft = this.registerDouble("Left Y", "LeftY", 0.2, -2.0, 2.0);
        this.zLeft = this.registerDouble("Left Z", "LeftZ", -1.2, -2.0, 2.0);
        this.xRight = this.registerDouble("Right X", "RightX", 0.0, -2.0, 2.0);
        this.yRight = this.registerDouble("Right Y", "RightY", 0.2, -2.0, 2.0);
        this.zRight = this.registerDouble("Right Z", "RightZ", -1.2, -2.0, 2.0);
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}
