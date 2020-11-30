// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import net.minecraft.init.Items;
import me.zoom.xannax.module.Module;

public class EXPFast extends Module
{
    public EXPFast() {
        super("EXPFast", "EXPFast", Category.Combat);
    }
    
    @Override
    public void onUpdate() {
        if (EXPFast.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE || EXPFast.mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE) {
            EXPFast.mc.rightClickDelayTimer = 0;
        }
    }
}
