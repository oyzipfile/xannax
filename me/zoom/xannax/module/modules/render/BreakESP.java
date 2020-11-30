// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import me.zoom.xannax.util.MathUtil;
import me.zoom.xannax.util.RenderUtil;
import net.minecraft.init.Blocks;
import me.zoom.xannax.event.events.RenderEvent;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import net.minecraft.util.math.BlockPos;
import io.netty.util.internal.ConcurrentSet;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class BreakESP extends Module
{
    Setting.Integer alpha;
    Setting.Boolean ignoreSelf;
    Setting.Integer alphaF;
    Setting.Integer red;
    Setting.Boolean fade;
    Setting.Integer blue;
    Setting.Integer green;
    Setting.Mode mode;
    Setting.Boolean onlyObby;
    private ConcurrentSet test;
    public ConcurrentSet breaking;
    float inc;
    BlockPos pos;
    public static BreakESP INSTANCE;
    private Map alphaMap;
    private ArrayList options;
    
    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Solid");
        modes.add("Outline");
        modes.add("Full");
        this.ignoreSelf = this.registerBoolean("IgnoreSelf", "IgnoreSelf", false);
        this.onlyObby = this.registerBoolean("OnlyObi", "OnlyObi", true);
        this.alpha = this.registerInteger("Alpha", "Alpha", 50, 0, 255);
        this.alphaF = this.registerInteger("AlphaF", "AlphaF", 50, 0, 255);
        this.fade = this.registerBoolean("Fade", "Fade", false);
        this.red = this.registerInteger("Red", "Red", 255, 0, 255);
        this.green = this.registerInteger("Green", "Green", 255, 0, 255);
        this.blue = this.registerInteger("Blue", "Blue", 255, 0, 255);
        this.mode = this.registerMode("Mode", "Mode", modes, "Solid");
    }
    
    public BreakESP() {
        super("BreakESP", "BreakESP", Category.Render);
        this.test = new ConcurrentSet();
        this.breaking = new ConcurrentSet();
        (this.alphaMap = new HashMap()).put(0, 28);
        this.alphaMap.put(1, 56);
        this.alphaMap.put(2, 84);
        this.alphaMap.put(3, 112);
        this.alphaMap.put(4, 140);
        this.alphaMap.put(5, 168);
        this.alphaMap.put(6, 196);
        this.alphaMap.put(7, 224);
        this.alphaMap.put(8, 255);
        this.alphaMap.put(9, 255);
    }
    
    @Override
    public void onWorldRender(final RenderEvent var1) {
        int var3;
        IBlockState var4;
        Vec3d var5;
        IBlockState var6;
        Vec3d var7;
        BreakESP.mc.renderGlobal.damagedBlocks.forEach((var1x, var2) -> {
            if (var2 != null && (!(boolean)this.ignoreSelf.getValue() || BreakESP.mc.world.getEntityByID((int)var1x) != BreakESP.mc.player) && (!(boolean)this.onlyObby.getValue() || BreakESP.mc.world.getBlockState(var2.getPosition()).getBlock() == Blocks.OBSIDIAN)) {
                var3 = (int)(((boolean)this.fade.getValue()) ? this.alphaMap.get(var2.getPartialBlockDamage()) : Integer.valueOf(this.alpha.getValue()));
                if (this.mode.getValue().equalsIgnoreCase("Solid")) {
                    RenderUtil.prepare(7);
                    RenderUtil.drawBox(var2.getPosition(), this.red.getValue(), this.green.getValue(), this.blue.getValue(), var3, 63);
                    RenderUtil.release();
                }
                else if (this.mode.getValue().equalsIgnoreCase("Full")) {
                    var4 = BreakESP.mc.world.getBlockState(var2.getPosition());
                    var5 = MathUtil.interpolateEntity((Entity)BreakESP.mc.player, BreakESP.mc.getRenderPartialTicks());
                    RenderUtil.drawFullBox(var4.getSelectedBoundingBox((World)BreakESP.mc.world, var2.getPosition()).grow(0.0020000000949949026).offset(-var5.x, -var5.y, -var5.z), var2.getPosition(), 1.5f, this.red.getValue(), this.green.getValue(), this.blue.getValue(), var3, this.alphaF.getValue());
                }
                else if (this.mode.getValue().equalsIgnoreCase("Outline")) {
                    var6 = BreakESP.mc.world.getBlockState(var2.getPosition());
                    var7 = MathUtil.interpolateEntity((Entity)BreakESP.mc.player, BreakESP.mc.getRenderPartialTicks());
                    RenderUtil.drawBoundingBox(var6.getSelectedBoundingBox((World)BreakESP.mc.world, var2.getPosition()).grow(0.0020000000949949026).offset(-var7.x, -var7.y, -var7.z), 1.5f, this.red.getValue(), this.green.getValue(), this.blue.getValue(), var3);
                }
                else {
                    RenderUtil.prepare(7);
                    RenderUtil.drawBox(var2.getPosition(), this.red.getValue(), this.green.getValue(), this.blue.getValue(), var3, 63);
                    RenderUtil.release();
                }
            }
        });
    }
}
