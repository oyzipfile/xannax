// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.item.ItemBow;
import me.zoom.xannax.module.Module;

public class AutoSchoolShooter extends Module
{
    public AutoSchoolShooter() {
        super("AutoSchoolShooter", "AutoSchoolShooter", Category.Combat);
    }
    
    @Override
    public void onUpdate() {
        if (AutoSchoolShooter.mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && AutoSchoolShooter.mc.player.isHandActive() && AutoSchoolShooter.mc.player.getItemInUseMaxCount() >= 3) {
            AutoSchoolShooter.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, AutoSchoolShooter.mc.player.getHorizontalFacing()));
            AutoSchoolShooter.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(AutoSchoolShooter.mc.player.getActiveHand()));
            AutoSchoolShooter.mc.player.stopActiveHand();
        }
    }
}
