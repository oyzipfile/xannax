// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.zoom.xannax.util.friend.Friends;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import me.zoom.xannax.util.TpsUtils;
import net.minecraft.item.ItemSword;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.util.EnumHand;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Aura extends Module
{
    Setting.Mode mode;
    Setting.Boolean player;
    Setting.Boolean hostile;
    Setting.Boolean sword;
    Setting.Boolean sync_tps;
    Setting.Double range;
    Setting.Integer delay;
    Setting.Boolean toggleMsg;
    boolean start_verify;
    EnumHand actual_hand;
    double tick;
    
    public Aura() {
        super("Aura", "aura :D", Category.Combat);
        this.start_verify = true;
        this.actual_hand = EnumHand.MAIN_HAND;
        this.tick = 0.0;
    }
    
    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Normal");
        modes.add("A32k");
        this.mode = this.registerMode("Mode", "Mode", modes, "Normal");
        this.player = this.registerBoolean("Player", "Player", true);
        this.hostile = this.registerBoolean("Hostile", "Hostile", false);
        this.sword = this.registerBoolean("Sword", "Sword", true);
        this.sync_tps = this.registerBoolean("SyncTPS", "SyncTPS", true);
        this.range = this.registerDouble("Range", "Range", 5.0, 0.5, 6.0);
        this.delay = this.registerInteger("Delay", "Delay", 2, 0, 10);
        this.toggleMsg = this.registerBoolean("ToggleMSG", "ToggleMSG", true);
    }
    
    @Override
    protected void onEnable() {
        this.tick = 0.0;
        if (this.toggleMsg.getValue() && Aura.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.GREEN + "Aura has been toggled on!");
        }
    }
    
    public void onDisable() {
        if (this.toggleMsg.getValue() && Aura.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.RED + "Aura has been toggled off!");
        }
    }
    
    @Override
    public void onUpdate() {
        if (Aura.mc.player != null && Aura.mc.world != null) {
            ++this.tick;
            if (Aura.mc.player.isDead | Aura.mc.player.getHealth() <= 0.0f) {
                return;
            }
            if (this.mode.getValue().equalsIgnoreCase("Normal")) {
                if (!(Aura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && this.sword.getValue()) {
                    this.start_verify = false;
                }
                else if (Aura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && this.sword.getValue()) {
                    this.start_verify = true;
                }
                else if (!this.sword.getValue()) {
                    this.start_verify = true;
                }
                final Entity entity = this.find_entity();
                if (entity != null && this.start_verify) {
                    final float tick_to_hit = 20.0f - TpsUtils.getTickRate();
                    final boolean is_possible_attack = Aura.mc.player.getCooledAttackStrength(this.sync_tps.getValue() ? (-tick_to_hit) : 0.0f) >= 1.0f;
                    if (is_possible_attack) {
                        this.attack_entity(entity);
                    }
                }
            }
            else {
                if (!(Aura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) {
                    return;
                }
                if (this.tick < this.delay.getValue()) {
                    return;
                }
                this.tick = 0.0;
                final Entity entity = this.find_entity();
                if (entity != null) {
                    this.attack_entity(entity);
                }
            }
        }
    }
    
    public void attack_entity(final Entity entity) {
        if (this.mode.getValue().equalsIgnoreCase("A32k")) {
            int newSlot = -1;
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = Aura.mc.player.inventory.getStackInSlot(i);
                if (stack != ItemStack.EMPTY) {
                    if (this.checkSharpness(stack)) {
                        newSlot = i;
                        break;
                    }
                }
            }
            if (newSlot != -1) {
                Aura.mc.player.inventory.currentItem = newSlot;
            }
        }
        final ItemStack off_hand_item = Aura.mc.player.getHeldItemOffhand();
        if (off_hand_item.getItem() == Items.SHIELD) {
            Aura.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Aura.mc.player.getHorizontalFacing()));
        }
        Aura.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
        Aura.mc.player.swingArm(this.actual_hand);
        Aura.mc.player.resetCooldown();
    }
    
    public Entity find_entity() {
        Entity entity_requested = null;
        for (final Entity player : (List)Aura.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList())) {
            if (player != null && this.is_compatible(player) && Aura.mc.player.getDistance(player) <= this.range.getValue()) {
                entity_requested = player;
            }
        }
        return entity_requested;
    }
    
    public boolean is_compatible(final Entity entity) {
        if (this.player.getValue() && entity instanceof EntityPlayer && entity != Aura.mc.player && !entity.getName().equals(Aura.mc.player.getName())) {
            return true;
        }
        if (this.hostile.getValue() && entity instanceof IMob) {
            return true;
        }
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entity_living_base = (EntityLivingBase)entity;
            if (entity_living_base.getHealth() <= 0.0f) {
                return false;
            }
        }
        return false;
    }
    
    private boolean checkSharpness(final ItemStack stack) {
        if (stack.getTagCompound() == null) {
            return false;
        }
        final NBTTagList enchants = (NBTTagList)stack.getTagCompound().getTag("ench");
        if (enchants == null) {
            return false;
        }
        int i = 0;
        while (i < enchants.tagCount()) {
            final NBTTagCompound enchant = enchants.getCompoundTagAt(i);
            if (enchant.getInteger("id") == 16) {
                final int lvl = enchant.getInteger("lvl");
                if (lvl > 5) {
                    return true;
                }
                break;
            }
            else {
                ++i;
            }
        }
        return false;
    }
    
    @Override
    public String getHudInfo() {
        String t = "";
        if (this.mode.getValue().equalsIgnoreCase("Normal")) {
            t = "[" + ChatFormatting.WHITE + "Single" + ChatFormatting.GRAY + "]";
        }
        else {
            t = "[" + ChatFormatting.WHITE + "A32k" + ChatFormatting.GRAY + "]";
        }
        return t;
    }
}
