// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3i;
import java.awt.Color;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.event.events.RenderEvent;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import me.zoom.xannax.util.BlockUtils;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.util.math.BlockPos;
import io.netty.util.internal.ConcurrentSet;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class VoidESP extends Module
{
    Setting.Boolean rainbow;
    Setting.Integer saturation;
    Setting.Integer brightness;
    Setting.Integer speed;
    Setting.Integer renderDistance;
    Setting.Integer activeYValue;
    Setting.Mode renderType;
    Setting.Mode renderMode;
    Setting.Integer rV;
    Setting.Integer gV;
    Setting.Integer bV;
    Setting.Integer oW;
    Setting.Integer alpha;
    private ConcurrentSet<BlockPos> voidHoles;
    
    public VoidESP() {
        super("VoidESP", "VoidESP", Category.Render);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> render = new ArrayList<String>();
        render.add("Outline");
        render.add("Fill");
        render.add("Both");
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Box");
        modes.add("Flat");
        this.rV = this.registerInteger("Red", "RedVoid", 255, 0, 255);
        this.gV = this.registerInteger("Green", "GreenVoid", 255, 0, 255);
        this.bV = this.registerInteger("Blue", "BlueVoid", 255, 0, 255);
        this.rainbow = this.registerBoolean("Rainbow", "Rainbow", false);
        this.saturation = this.registerInteger("Saturation", "Saturation", 50, 0, 100);
        this.brightness = this.registerInteger("Brightness", "Brightness", 50, 0, 100);
        this.speed = this.registerInteger("Speed", "Speed", 50, 1, 100);
        this.alpha = this.registerInteger("Alpha", "Alpha", 50, 0, 255);
        this.oW = this.registerInteger("OutlineW", "OutlineWVoid", 2, 1, 10);
        this.renderDistance = this.registerInteger("Distance", "Distance", 10, 1, 40);
        this.activeYValue = this.registerInteger("Activate Y", "ActivateY", 20, 0, 256);
        this.renderType = this.registerMode("Render", "Render", render, "Both");
        this.renderMode = this.registerMode("Mode", "Mode", modes, "Flat");
    }
    
    @Override
    public void onUpdate() {
        if (VoidESP.mc.player.dimension == 1) {
            return;
        }
        if (VoidESP.mc.player.getPosition().getY() > this.activeYValue.getValue()) {
            return;
        }
        if (this.voidHoles == null) {
            this.voidHoles = (ConcurrentSet<BlockPos>)new ConcurrentSet();
        }
        else {
            this.voidHoles.clear();
        }
        final List<BlockPos> blockPosList = BlockUtils.getCircle(getPlayerPos(), 0, (float)this.renderDistance.getValue(), false);
        for (final BlockPos blockPos : blockPosList) {
            if (VoidESP.mc.world.getBlockState(blockPos).getBlock().equals(Blocks.BEDROCK)) {
                continue;
            }
            if (this.isAnyBedrock(blockPos, Offsets.center)) {
                continue;
            }
            this.voidHoles.add((Object)blockPos);
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final Color rainbowColor1 = this.rainbow.getValue() ? new Color(RenderUtil.getRainbow(this.speed.getValue() * 100, 0, this.saturation.getValue() / 100.0f, this.brightness.getValue() / 100.0f)) : new Color(this.rV.getValue(), this.gV.getValue(), this.bV.getValue());
        final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
        if (VoidESP.mc.player == null || this.voidHoles == null) {
            return;
        }
        if (VoidESP.mc.player.getPosition().getY() > this.activeYValue.getValue()) {
            return;
        }
        if (this.voidHoles.isEmpty()) {
            return;
        }
        final Color color;
        this.voidHoles.forEach(blockPos -> {
            RenderUtil.prepare(7);
            if (this.renderMode.getValue().equalsIgnoreCase("Box")) {
                this.drawBox(blockPos, color.getRed(), color.getGreen(), color.getBlue());
            }
            else {
                this.drawFlat(blockPos, color.getRed(), color.getGreen(), color.getBlue());
            }
            RenderUtil.release();
            RenderUtil.prepare(7);
            this.drawOutline(blockPos, this.oW.getValue(), color.getRed(), color.getGreen(), color.getBlue());
            RenderUtil.release();
        });
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(VoidESP.mc.player.posX), Math.floor(VoidESP.mc.player.posY), Math.floor(VoidESP.mc.player.posZ));
    }
    
    private boolean isAnyBedrock(final BlockPos origin, final BlockPos[] offset) {
        for (final BlockPos pos : offset) {
            if (VoidESP.mc.world.getBlockState(origin.add((Vec3i)pos)).getBlock().equals(Blocks.BEDROCK)) {
                return true;
            }
        }
        return false;
    }
    
    public void drawFlat(final BlockPos blockPos, final int r, final int g, final int b) {
        if (this.renderType.getValue().equalsIgnoreCase("Fill") || this.renderType.getValue().equalsIgnoreCase("Both")) {
            final AxisAlignedBB bb = VoidESP.mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)VoidESP.mc.world, blockPos);
            if (this.renderMode.getValue().equalsIgnoreCase("Flat")) {
                final Color color = new Color(r, g, b, this.alpha.getValue());
                RenderUtil.drawBox(blockPos, color.getRGB(), 1);
            }
        }
    }
    
    private void drawBox(final BlockPos blockPos, final int r, final int g, final int b) {
        if (this.renderType.getValue().equalsIgnoreCase("Fill") || this.renderType.getValue().equalsIgnoreCase("Both")) {
            final AxisAlignedBB bb = VoidESP.mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)VoidESP.mc.world, blockPos);
            final Color color = new Color(r, g, b, this.alpha.getValue());
            RenderUtil.drawBox(blockPos, color.getRGB(), 63);
        }
    }
    
    public void drawOutline(final BlockPos blockPos, final int width, final int r, final int g, final int b) {
        if (this.renderType.getValue().equalsIgnoreCase("Outline") || this.renderType.getValue().equalsIgnoreCase("Both")) {
            final float[] array;
            final float[] hue = array = new float[] { System.currentTimeMillis() % 11520L / 11520.0f };
            final int n = 0;
            array[n] += 0.02f;
            if (this.renderMode.getValue().equalsIgnoreCase("Box")) {
                RenderUtil.drawBoundingBoxBlockPos(blockPos, (float)width, r, g, b, 255);
            }
            if (this.renderMode.getValue().equalsIgnoreCase("Flat")) {
                RenderUtil.drawBoundingBoxBottom2(blockPos, (float)width, r, g, b, 255);
            }
        }
    }
    
    private static class Offsets
    {
        static final BlockPos[] center;
        
        static {
            center = new BlockPos[] { new BlockPos(0, 0, 0), new BlockPos(0, 1, 0), new BlockPos(0, 2, 0) };
        }
    }
}
