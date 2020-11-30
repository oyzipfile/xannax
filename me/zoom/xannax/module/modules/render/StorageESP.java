// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntityChest;
import org.lwjgl.opengl.GL11;
import me.zoom.xannax.util.RenderUtil;
import java.awt.Color;
import me.zoom.xannax.event.events.RenderEvent;
import net.minecraft.tileentity.TileEntity;
import java.util.concurrent.ConcurrentHashMap;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class StorageESP extends Module
{
    Setting.Integer w;
    ConcurrentHashMap<TileEntity, String> chests;
    
    public StorageESP() {
        super("StorageESP", "StorageESP", Category.Render);
        this.chests = new ConcurrentHashMap<TileEntity, String>();
    }
    
    @Override
    public void setup() {
        this.w = this.registerInteger("Width", "Width", 2, 1, 10);
    }
    
    @Override
    public void onUpdate() {
        StorageESP.mc.world.loadedTileEntityList.forEach(e -> this.chests.put(e, ""));
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final Color c2 = new Color(255, 255, 0, 255);
        final Color c3 = new Color(180, 70, 200, 255);
        final Color c4 = new Color(150, 150, 150, 255);
        final Color c5 = new Color(255, 0, 0, 255);
        if (this.chests != null && this.chests.size() > 0) {
            RenderUtil.prepareGL();
            GL11.glEnable(2848);
            final Color color;
            final Color color2;
            final Color color3;
            final Color color4;
            this.chests.forEach((c, t) -> {
                if (StorageESP.mc.world.loadedTileEntityList.contains(c)) {
                    if (c instanceof TileEntityChest) {
                        RenderUtil.drawBoundingBox(StorageESP.mc.world.getBlockState(c.getPos()).getSelectedBoundingBox((World)StorageESP.mc.world, c.getPos()), (float)this.w.getValue(), color.getRGB());
                    }
                    if (c instanceof TileEntityEnderChest) {
                        RenderUtil.drawBoundingBox(StorageESP.mc.world.getBlockState(c.getPos()).getSelectedBoundingBox((World)StorageESP.mc.world, c.getPos()), (float)this.w.getValue(), color2.getRGB());
                    }
                    if (c instanceof TileEntityShulkerBox) {
                        RenderUtil.drawBoundingBox(StorageESP.mc.world.getBlockState(c.getPos()).getSelectedBoundingBox((World)StorageESP.mc.world, c.getPos()), (float)this.w.getValue(), color3.getRGB());
                    }
                    if (c instanceof TileEntityDispenser || c instanceof TileEntityFurnace || c instanceof TileEntityHopper) {
                        RenderUtil.drawBoundingBox(StorageESP.mc.world.getBlockState(c.getPos()).getSelectedBoundingBox((World)StorageESP.mc.world, c.getPos()), (float)this.w.getValue(), color4.getRGB());
                    }
                }
                return;
            });
            RenderUtil.releaseGL();
        }
    }
}
