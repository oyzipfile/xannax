// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import java.util.UUID;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.module.modules.render.CapesModule;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ AbstractClientPlayer.class })
public abstract class MixinAbstractClientPlayer
{
    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo getPlayerInfo();
    
    @Inject(method = { "getLocationCape" }, at = { @At("HEAD") }, cancellable = true)
    public void getLocationCape(final CallbackInfoReturnable<ResourceLocation> cir) {
        final UUID uuid = this.getPlayerInfo().getGameProfile().getId();
        final CapesModule capesModule = (CapesModule)ModuleManager.getModuleByName("Capes");
        if (ModuleManager.isModuleEnabled("Capes") && Xannax.getInstance().capeUtils.hasCape(uuid)) {
            cir.setReturnValue(new ResourceLocation("minecraft:capeblack.png"));
        }
    }
}
