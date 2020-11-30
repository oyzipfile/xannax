// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import java.util.List;
import me.zoom.xannax.util.friend.Friends;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.init.Items;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3i;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.item.ItemBed;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import java.util.Comparator;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.text.TextFormatting;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class BedAura extends Module
{
    Setting.Integer range;
    Setting.Boolean announceUsage;
    Setting.Integer placedelay;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private EntityPlayer closestTarget;
    private String lastTickTargetName;
    private int bedSlot;
    private BlockPos placeTarget;
    private float rotVar;
    private int blocksPlaced;
    private double diffXZ;
    private boolean firstRun;
    private boolean nowTop;
    
    public BedAura() {
        super("BedAura", "CA but for beds", Category.Combat);
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.bedSlot = -1;
        this.nowTop = false;
    }
    
    @Override
    public void setup() {
        this.range = this.registerInteger("Range", "Range", 7, 0, 9);
        this.placedelay = this.registerInteger("Place Delay", "PlaceDelay", 15, 8, 20);
        this.announceUsage = this.registerBoolean("ToggleMSG", "ToggleMSG", true);
    }
    
    public void onEnable() {
        if (BedAura.mc.player == null) {
            this.toggle();
            return;
        }
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.firstRun = true;
        this.blocksPlaced = 0;
        this.playerHotbarSlot = BedAura.mc.player.inventory.currentItem;
        this.lastHotbarSlot = -1;
    }
    
    public void onDisable() {
        if (BedAura.mc.player == null) {
            return;
        }
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            BedAura.mc.player.inventory.currentItem = this.playerHotbarSlot;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        if (this.announceUsage.getValue()) {
            Command.sendClientMessage(ChatFormatting.RED + "BedAura has been toggled off!");
        }
        this.blocksPlaced = 0;
    }
    
    @Override
    public void onUpdate() {
        if (BedAura.mc.player == null) {
            return;
        }
        if (BedAura.mc.player.dimension == 0) {
            Command.sendClientMessage("You are in the overworld!");
            this.toggle();
        }
        try {
            this.findClosestTarget();
        }
        catch (NullPointerException ex) {}
        if (this.closestTarget == null && BedAura.mc.player.dimension != 0 && this.firstRun) {
            this.firstRun = false;
            if (this.announceUsage.getValue()) {
                Command.sendClientMessage(ChatFormatting.GREEN + "BedAura has been toggled on! Waiting for target...");
            }
        }
        if (this.firstRun && this.closestTarget != null && BedAura.mc.player.dimension != 0) {
            this.firstRun = false;
            this.lastTickTargetName = this.closestTarget.getName();
            if (this.announceUsage.getValue()) {
                Command.sendClientMessage(ChatFormatting.GREEN + "BedAura has been toggled on! Target: " + this.lastTickTargetName);
            }
        }
        if (this.closestTarget != null && this.lastTickTargetName != null && !this.lastTickTargetName.equals(this.closestTarget.getName())) {
            this.lastTickTargetName = this.closestTarget.getName();
            if (this.announceUsage.getValue()) {
                Command.sendClientMessage(TextFormatting.GREEN + "BedAura new target:" + this.lastTickTargetName);
            }
        }
        try {
            this.diffXZ = BedAura.mc.player.getPositionVector().distanceTo(this.closestTarget.getPositionVector());
        }
        catch (NullPointerException ex2) {}
        try {
            if (this.closestTarget != null) {
                this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(1.0, 1.0, 0.0));
                this.nowTop = false;
                this.rotVar = 90.0f;
                final BlockPos block1 = this.placeTarget;
                if (!this.canPlaceBed(block1)) {
                    this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(-1.0, 1.0, 0.0));
                    this.rotVar = -90.0f;
                    this.nowTop = false;
                }
                final BlockPos block2 = this.placeTarget;
                if (!this.canPlaceBed(block2)) {
                    this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(0.0, 1.0, 1.0));
                    this.rotVar = 180.0f;
                    this.nowTop = false;
                }
                final BlockPos block3 = this.placeTarget;
                if (!this.canPlaceBed(block3)) {
                    this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(0.0, 1.0, -1.0));
                    this.rotVar = 0.0f;
                    this.nowTop = false;
                }
                final BlockPos block4 = this.placeTarget;
                if (!this.canPlaceBed(block4)) {
                    this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(0.0, 2.0, -1.0));
                    this.rotVar = 0.0f;
                    this.nowTop = true;
                }
                final BlockPos blockt1 = this.placeTarget;
                if (this.nowTop && !this.canPlaceBed(blockt1)) {
                    this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(-1.0, 2.0, 0.0));
                    this.rotVar = -90.0f;
                }
                final BlockPos blockt2 = this.placeTarget;
                if (this.nowTop && !this.canPlaceBed(blockt2)) {
                    this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(0.0, 2.0, 1.0));
                    this.rotVar = 180.0f;
                }
                final BlockPos blockt3 = this.placeTarget;
                if (this.nowTop && !this.canPlaceBed(blockt3)) {
                    this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(1.0, 2.0, 0.0));
                    this.rotVar = 90.0f;
                }
            }
            BedAura.mc.world.loadedTileEntityList.stream().filter(e -> e instanceof TileEntityBed).filter(e -> BedAura.mc.player.getDistance((double)e.getPos().getX(), (double)e.getPos().getY(), (double)e.getPos().getZ()) <= this.range.getValue()).sorted(Comparator.comparing(e -> BedAura.mc.player.getDistance((double)e.getPos().getX(), (double)e.getPos().getY(), (double)e.getPos().getZ()))).forEach(bed -> {
                if (BedAura.mc.player.dimension != 0) {
                    BedAura.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(bed.getPos(), EnumFacing.UP, EnumHand.OFF_HAND, 0.0f, 0.0f, 0.0f));
                }
                return;
            });
            if (BedAura.mc.player.ticksExisted % this.placedelay.getValue() == 0 && this.closestTarget != null) {
                this.findBeds();
                final EntityPlayerSP player = BedAura.mc.player;
                ++player.ticksExisted;
                this.doDaMagic();
            }
        }
        catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }
    
    private void doDaMagic() {
        if (this.diffXZ <= this.range.getValue()) {
            int i = 0;
            while (i < 9) {
                if (this.bedSlot != -1) {
                    break;
                }
                final ItemStack stack = BedAura.mc.player.inventory.getStackInSlot(i);
                if (stack.getItem() instanceof ItemBed) {
                    if ((this.bedSlot = i) != -1) {
                        BedAura.mc.player.inventory.currentItem = this.bedSlot;
                        break;
                    }
                    break;
                }
                else {
                    ++i;
                }
            }
            this.bedSlot = -1;
            if (this.blocksPlaced == 0 && BedAura.mc.player.inventory.getStackInSlot(BedAura.mc.player.inventory.currentItem).getItem() instanceof ItemBed) {
                BedAura.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BedAura.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                BedAura.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(this.rotVar, 0.0f, BedAura.mc.player.onGround));
                this.placeBlock(new BlockPos((Vec3i)this.placeTarget), EnumFacing.DOWN);
                BedAura.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BedAura.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.blocksPlaced = 1;
                this.nowTop = false;
            }
            this.blocksPlaced = 0;
        }
    }
    
    private void findBeds() {
        if ((BedAura.mc.currentScreen == null || !(BedAura.mc.currentScreen instanceof GuiContainer)) && BedAura.mc.player.inventory.getStackInSlot(0).getItem() != Items.BED) {
            for (int i = 9; i < 36; ++i) {
                if (BedAura.mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                    BedAura.mc.playerController.windowClick(BedAura.mc.player.inventoryContainer.windowId, i, 0, ClickType.SWAP, (EntityPlayer)BedAura.mc.player);
                    break;
                }
            }
        }
    }
    
    private boolean canPlaceBed(final BlockPos pos) {
        return (BedAura.mc.world.getBlockState(pos).getBlock() == Blocks.AIR || BedAura.mc.world.getBlockState(pos).getBlock() == Blocks.BED) && BedAura.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(pos)).isEmpty();
    }
    
    private void findClosestTarget() {
        final List<EntityPlayer> playerList = (List<EntityPlayer>)BedAura.mc.world.playerEntities;
        this.closestTarget = null;
        for (final EntityPlayer target : playerList) {
            if (target == BedAura.mc.player) {
                continue;
            }
            if (Friends.isFriend(target.getName())) {
                continue;
            }
            if (!isLiving((Entity)target)) {
                continue;
            }
            if (target.getHealth() <= 0.0f) {
                continue;
            }
            if (this.closestTarget == null) {
                this.closestTarget = target;
            }
            else {
                if (BedAura.mc.player.getDistance((Entity)target) >= BedAura.mc.player.getDistance((Entity)this.closestTarget)) {
                    continue;
                }
                this.closestTarget = target;
            }
        }
    }
    
    private void placeBlock(final BlockPos pos, final EnumFacing side) {
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        BedAura.mc.playerController.processRightClickBlock(BedAura.mc.player, BedAura.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        BedAura.mc.player.swingArm(EnumHand.MAIN_HAND);
    }
    
    public static boolean isLiving(final Entity e) {
        return e instanceof EntityLivingBase;
    }
}
