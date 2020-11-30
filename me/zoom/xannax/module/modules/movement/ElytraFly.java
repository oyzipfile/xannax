// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class ElytraFly extends Module
{
    Setting.Double speed;
    
    public ElytraFly() {
        super("ElytraFly", "Elytrafly module", Category.Movement);
    }
    
    @Override
    public void setup() {
        this.speed = this.registerDouble("Speed", "Speed", 1.8, 0.0, 100.0);
    }
    
    @Override
    public void onUpdate() {
        if (ElytraFly.mc.player.capabilities.isFlying || ElytraFly.mc.player.isElytraFlying()) {
            ElytraFly.mc.player.setSprinting(false);
        }
        if (ElytraFly.mc.player.capabilities.isFlying) {
            ElytraFly.mc.player.setVelocity(0.0, 0.0, 0.0);
            ElytraFly.mc.player.setPosition(ElytraFly.mc.player.posX, ElytraFly.mc.player.posY - 5.0000002374872565E-5, ElytraFly.mc.player.posZ);
            ElytraFly.mc.player.capabilities.setFlySpeed((float)this.speed.getValue());
            ElytraFly.mc.player.setSprinting(false);
        }
        if (ElytraFly.mc.player.onGround) {
            ElytraFly.mc.player.capabilities.allowFlying = false;
        }
        if (ElytraFly.mc.player.isElytraFlying()) {
            ElytraFly.mc.player.capabilities.setFlySpeed(0.915f);
            ElytraFly.mc.player.capabilities.isFlying = true;
            if (!ElytraFly.mc.player.capabilities.isCreativeMode) {
                ElytraFly.mc.player.capabilities.allowFlying = true;
            }
        }
    }
    
    @Override
    protected void onDisable() {
        ElytraFly.mc.player.capabilities.isFlying = false;
        ElytraFly.mc.player.capabilities.setFlySpeed(0.05f);
        if (!ElytraFly.mc.player.capabilities.isCreativeMode) {
            ElytraFly.mc.player.capabilities.allowFlying = false;
        }
    }
}
