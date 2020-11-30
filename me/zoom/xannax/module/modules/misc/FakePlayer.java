// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import me.zoom.xannax.module.Module;

public class FakePlayer extends Module
{
    public FakePlayer() {
        super("FakePlayer", "FakePlayer", Category.Misc);
    }
    
    public void onEnable() {
        if (FakePlayer.mc.world == null) {
            return;
        }
        final EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.world, new GameProfile(UUID.fromString("0f75a81d-70e5-43c5-b892-f33c524284f2"), "popbob"));
        fakePlayer.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
        fakePlayer.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
        FakePlayer.mc.world.addEntityToWorld(-100, (Entity)fakePlayer);
    }
    
    public void onDisable() {
        FakePlayer.mc.world.removeEntityFromWorld(-100);
    }
}
