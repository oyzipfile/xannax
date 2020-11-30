// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.Xannax;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.client.Minecraft;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.DamageBlockEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class SpeedMine extends Module
{
    Setting.Mode mode;
    Setting.Boolean haste;
    @EventHandler
    private final Listener<DamageBlockEvent> listener;
    
    public SpeedMine() {
        super("SpeedMine", "SpeedMine", Category.Player);
        this.listener = new Listener<DamageBlockEvent>(event -> {
            if (SpeedMine.mc.world != null && SpeedMine.mc.player != null) {
                if (this.canBreak(event.getPos())) {
                    if (this.mode.getValue().equalsIgnoreCase("Packet")) {
                        SpeedMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
                        event.cancel();
                    }
                    if (this.mode.getValue().equalsIgnoreCase("Damage") && SpeedMine.mc.playerController.curBlockDamageMP >= 0.7f) {
                        SpeedMine.mc.playerController.curBlockDamageMP = 1.0f;
                    }
                    if (this.mode.getValue().equalsIgnoreCase("Instant")) {
                        SpeedMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
                        SpeedMine.mc.playerController.onPlayerDestroyBlock(event.getPos());
                        SpeedMine.mc.world.setBlockToAir(event.getPos());
                    }
                }
            }
        }, (Predicate<DamageBlockEvent>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> Modes = new ArrayList<String>();
        Modes.add("Packet");
        Modes.add("Damage");
        Modes.add("Instant");
        this.mode = this.registerMode("Mode", "Mode", Modes, "Packet");
        this.haste = this.registerBoolean("Haste", "Haste", false);
    }
    
    @Override
    public void onUpdate() {
        Minecraft.getMinecraft().playerController.blockHitDelay = 0;
        if (this.haste.getValue()) {
            final PotionEffect effect = new PotionEffect(MobEffects.HASTE, 80950, 1, false, false);
            SpeedMine.mc.player.addPotionEffect(new PotionEffect(effect));
        }
        if (!this.haste.getValue() && SpeedMine.mc.player.isPotionActive(MobEffects.HASTE)) {
            SpeedMine.mc.player.removePotionEffect(MobEffects.HASTE);
        }
    }
    
    private boolean canBreak(final BlockPos pos) {
        final IBlockState blockState = SpeedMine.mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, (World)SpeedMine.mc.world, pos) != -1.0f;
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
        SpeedMine.mc.player.removePotionEffect(MobEffects.HASTE);
    }
    
    @Override
    public String getHudInfo() {
        String t = "";
        t = "[" + ChatFormatting.WHITE + this.mode.getValue() + ChatFormatting.GRAY + "]";
        return t;
    }
}
