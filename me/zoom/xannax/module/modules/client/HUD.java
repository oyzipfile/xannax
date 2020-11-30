// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.client;

import net.minecraft.potion.PotionEffect;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.OpenGlHelper;
import java.util.function.ToIntFunction;
import net.minecraft.init.Items;
import java.util.Objects;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import me.zoom.xannax.util.BlockUtils;
import me.zoom.xannax.Xannax;
import java.util.Iterator;
import me.zoom.xannax.util.ColorHolder;
import net.minecraft.client.renderer.GlStateManager;
import me.zoom.xannax.util.Animation;
import me.zoom.xannax.util.RenderUtil;
import net.minecraft.client.resources.I18n;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import me.zoom.xannax.util.TpsUtils;
import me.zoom.xannax.util.MathUtil;
import net.minecraft.entity.Entity;
import me.zoom.xannax.util.friend.Friends;
import net.minecraft.entity.EntityLivingBase;
import java.util.Comparator;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.util.RainbowUtil;
import me.zoom.xannax.util.font.FontUtils;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderItem;
import java.text.DecimalFormat;
import java.awt.Color;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class HUD extends Module
{
    Setting.Boolean Greeter;
    Setting.Integer GreeterX;
    Setting.Integer GreeterY;
    Setting.Boolean Watermark;
    Setting.Integer WatermarkY;
    Setting.Boolean ArrayList;
    Setting.Boolean ArrayListHot;
    Setting.Boolean sortUp;
    Setting.Boolean Potions;
    Setting.Boolean pSortUp;
    Setting.Boolean Hole;
    Setting.Integer holex;
    Setting.Integer holey;
    Setting.Boolean XYZ;
    Setting.Boolean xyzNoGray;
    Setting.Boolean Direction;
    Setting.Boolean ArmorHud;
    Setting.Boolean Ping;
    Setting.Boolean FPS;
    Setting.Boolean TPS;
    Setting.Boolean durability;
    Setting.Boolean Speed;
    Setting.Boolean Time;
    Setting.Boolean tots;
    Setting.Boolean playerViewer;
    Setting.Integer playerViewerX;
    Setting.Integer playerViewerY;
    Setting.Double playerScale;
    Setting.Boolean rainbow;
    Setting.Integer saturation;
    Setting.Integer brightness;
    Setting.Integer speed;
    Setting.Integer red;
    Setting.Integer green;
    Setting.Integer blue;
    public static Setting.Boolean rainbowArray;
    Setting.Integer animS;
    Setting.Boolean textRadar;
    Setting.Integer textRadarY;
    public static Setting.Integer deelay;
    public static Setting.Integer arraySat;
    public static Setting.Integer arrayBri;
    public static Setting.Boolean potionIcons;
    Color color;
    int modCount;
    int sort;
    int potCount;
    int counter;
    private static final float[] tickRates;
    DecimalFormat format2;
    private static final RenderItem itemRender;
    private static final ItemStack totemm;
    
    public HUD() {
        super("HUD", "HUD", Category.Client);
        this.format2 = new DecimalFormat("00");
    }
    
    @Override
    public void setup() {
        this.ArrayList = this.registerBoolean("ArrayList", "ArrayList", false);
        this.animS = this.registerInteger("AnimSpeed", "AnimSpeed", 1, 0, 10);
        this.ArrayListHot = this.registerBoolean("ArrayList Hot", "ArrayListHot", true);
        this.sortUp = this.registerBoolean("Array Sort Up", "ArraySortUp", false);
        HUD.rainbowArray = this.registerBoolean("RainbowArray", "RainbowArray", false);
        HUD.arraySat = this.registerInteger("ArraySat", "ArraySat", 100, 0, 100);
        HUD.arrayBri = this.registerInteger("ArrayBri", "ArrayBri", 100, 0, 100);
        this.Potions = this.registerBoolean("Potions", "Potions", false);
        this.pSortUp = this.registerBoolean("Potions Sort Up", "PotionsSortUp", false);
        this.Watermark = this.registerBoolean("Watermark", "Watermark", false);
        this.WatermarkY = this.registerInteger("Watermark Y", "WatermarkY", 2, 2, 1000);
        this.Greeter = this.registerBoolean("Greeter", "Greeter", false);
        this.GreeterX = this.registerInteger("Greeter X", "GreeterX", 100, 0, 1000);
        this.GreeterY = this.registerInteger("Greeter Y", "GreeterY", 100, 0, 1000);
        this.ArmorHud = this.registerBoolean("ArmorHud", "ArmorHud", false);
        this.Hole = this.registerBoolean("Hole", "Hole", false);
        this.holex = this.registerInteger("Hole X", "HoleX", 0, 0, 1000);
        this.holey = this.registerInteger("Hole Y", "HoleY", 0, 0, 1000);
        this.tots = this.registerBoolean("Totems", "TotemsHUD", false);
        this.Ping = this.registerBoolean("Ping", "Ping", false);
        this.FPS = this.registerBoolean("FPS", "FPS", false);
        this.TPS = this.registerBoolean("TPS", "TPS", false);
        this.Time = this.registerBoolean("Time", "Time", false);
        this.Speed = this.registerBoolean("Speed", "Speed", false);
        this.durability = this.registerBoolean("Durability", "Durability", false);
        this.XYZ = this.registerBoolean("XYZ", "XYZ", false);
        this.Direction = this.registerBoolean("Direction", "Direction", false);
        this.xyzNoGray = this.registerBoolean("Coords No Gray", "XYZNoGray", false);
        this.textRadar = this.registerBoolean("TextRadar", "TextRadar", false);
        this.textRadarY = this.registerInteger("TextRadarY", "TextRadarY", 100, 0, 1000);
        this.playerViewer = this.registerBoolean("Player Viewer", "PlayerViewer", false);
        this.playerViewerX = this.registerInteger("Player Viewer X", "PlayerViewerX", 0, 0, 1000);
        this.playerViewerY = this.registerInteger("Player Viewer Y", "PlayerViewerY", 0, 0, 1000);
        this.playerScale = this.registerDouble("Player Scale", "PlayerScale", 1.0, 0.1, 2.0);
        HUD.potionIcons = this.registerBoolean("PotionIcons", "PotionIcons", false);
        HUD.deelay = this.registerInteger("hue", "hue", 240, 0, 600);
    }
    
    @Override
    public void onRender() {
        final boolean inHell = HUD.mc.world.getBiome(HUD.mc.player.getPosition()).getBiomeName().equals("Hell");
        final int posX = (int)HUD.mc.player.posX;
        final int posY = (int)HUD.mc.player.posY;
        final int posZ = (int)HUD.mc.player.posZ;
        final float nether = inHell ? 8.0f : 0.125f;
        final int hposX = (int)(HUD.mc.player.posX * nether);
        final int hposZ = (int)(HUD.mc.player.posZ * nether);
        this.counter = 0;
        this.color = new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue());
        if (this.XYZ.getValue()) {
            if (this.xyzNoGray.getValue()) {
                final String coords = "XYZ " + posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]";
                final int[] counter4 = { 1 };
                final char[] stringToCharArray3 = coords.toCharArray();
                float u = 0.0f;
                for (final char c3 : stringToCharArray3) {
                    this.drawStringWithShadow(String.valueOf(c3), (int)(2.0f + u), new ScaledResolution(HUD.mc).getScaledHeight() - (HUD.mc.ingameGUI.getChatGUI().getChatOpen() ? (FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 14) : (FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2)), HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(counter4[0] * HUD.deelay.getValue()) : this.color.getRGB());
                    u += FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), String.valueOf(c3));
                    final int[] array2 = counter4;
                    final int n = 0;
                    ++array2[n];
                }
            }
            else {
                this.drawStringWithShadow("XYZ " + ChatFormatting.GRAY + posX + ", " + posY + ", " + posZ + " " + ChatFormatting.RESET + "[" + ChatFormatting.GRAY + hposX + ", " + hposZ + ChatFormatting.RESET + "]", 2, new ScaledResolution(HUD.mc).getScaledHeight() - (HUD.mc.ingameGUI.getChatGUI().getChatOpen() ? (FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 14) : (FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2)), HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(1 * HUD.deelay.getValue()) : this.color.getRGB());
            }
        }
        if (this.Direction.getValue()) {
            if (this.xyzNoGray.getValue()) {
                final String direction = this.getFacing() + " [" + this.getTowards() + "]";
                final int[] counter5 = { 1 };
                final char[] stringToCharArray3 = direction.toCharArray();
                float u = 0.0f;
                for (final char c4 : stringToCharArray3) {
                    this.drawStringWithShadow(String.valueOf(c4), (int)(2.0f + u), new ScaledResolution(HUD.mc).getScaledHeight() - (HUD.mc.ingameGUI.getChatGUI().getChatOpen() ? (FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 14) : (FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2)) - (this.XYZ.getValue() ? (FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2) : 0), HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(counter5[0] * HUD.deelay.getValue()) : this.color.getRGB());
                    u += FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), String.valueOf(c4));
                    final int[] array4 = counter5;
                    final int n2 = 0;
                    ++array4[n2];
                }
            }
            else {
                this.drawStringWithShadow(ChatFormatting.GRAY + this.getFacing() + " " + ChatFormatting.RESET + "[" + ChatFormatting.GRAY + this.getTowards() + ChatFormatting.RESET + "]", 2, new ScaledResolution(HUD.mc).getScaledHeight() - (HUD.mc.ingameGUI.getChatGUI().getChatOpen() ? (FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 14) : (FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2)) - (this.XYZ.getValue() ? (FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2) : 0), HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(2 * HUD.deelay.getValue()) : this.color.getRGB());
            }
        }
        if (this.Watermark.getValue()) {
            final String string = "XannaX 0.8.5";
            final int[] counter6 = { 1 };
            final char[] stringToCharArray4 = string.toCharArray();
            float i = 0.0f;
            for (final char c5 : stringToCharArray4) {
                this.drawStringWithShadow(String.valueOf(c5), (int)(2.0f + i), this.WatermarkY.getValue(), HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(counter6[0] * HUD.deelay.getValue()) : this.color.getRGB());
                i += FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), String.valueOf(c5));
                final int[] array6 = counter6;
                final int n4 = 0;
                ++array6[n4];
            }
        }
        if (this.textRadar.getValue()) {
            final int[] pee = { 1 };
            if (HUD.mc.world == null || HUD.mc.player == null) {
                return;
            }
            float offset = 0.0f;
            final ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();
            for (final EntityPlayer player2 : HUD.mc.world.playerEntities) {
                if (player2 != HUD.mc.player && player2.isEntityAlive()) {
                    if (player2.getEntityId() == -1488) {
                        continue;
                    }
                    players.add(player2);
                }
            }
            players.sort(Comparator.comparingDouble(player -> HUD.mc.player.getDistance(player)));
            for (final EntityPlayer player2 : players) {
                this.drawStringWithShadow("" + this.getHealthThing((EntityLivingBase)player2) + (int)(player2.getHealth() + player2.getAbsorptionAmount()) + " " + (Friends.isFriend(player2.getName()) ? ChatFormatting.AQUA : ChatFormatting.RESET) + player2.getName() + " " + this.getDistanceThing((EntityLivingBase)player2) + (int)HUD.mc.player.getDistance((Entity)player2), 2, this.textRadarY.getValue() + (int)offset - players.size() / 2, HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(pee[0] * HUD.deelay.getValue()) : this.color.getRGB());
                offset += FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2;
                final int[] array7 = pee;
                final int n5 = 0;
                ++array7[n5];
            }
        }
        if (this.Greeter.getValue()) {
            this.drawStringWithShadow(MathUtil.getTimeOfDay() + HUD.mc.player.getName(), this.GreeterX.getValue(), this.GreeterY.getValue(), HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(1 * HUD.deelay.getValue()) : this.color.getRGB());
        }
        if (this.Hole.getValue()) {
            this.renderHole(this.holex.getValue(), this.holey.getValue());
        }
        if (this.tots.getValue()) {
            this.renderTotemHUD();
        }
        if (this.Ping.getValue()) {
            if (this.pSortUp.getValue()) {
                this.drawStringWithShadow("Ping " + ChatFormatting.GRAY + this.getPing() + "ms", new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Ping " + ChatFormatting.GRAY + this.getPing() + "ms"), 0 + (this.Potions.getValue() ? (this.potCount * 10) : (this.counter * 10)), HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(this.counter * HUD.deelay.getValue()) : this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
            else {
                this.drawStringWithShadow("Ping " + ChatFormatting.GRAY + this.getPing() + "ms", new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Ping " + ChatFormatting.GRAY + this.getPing() + "ms"), new ScaledResolution(HUD.mc).getScaledHeight() + (this.Potions.getValue() ? (this.potCount * -10) : (this.counter * -10)) - 10, HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(this.counter * HUD.deelay.getValue()) : this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
        }
        if (this.TPS.getValue()) {
            if (this.pSortUp.getValue()) {
                this.drawStringWithShadow("TPS " + ChatFormatting.GRAY + String.format("%.2f", TpsUtils.getTickRate()), new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "TPS " + ChatFormatting.GRAY + String.format("%.2f", TpsUtils.getTickRate())), 0 + (this.Potions.getValue() ? (this.potCount * 10) : (this.counter * 10)), HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(this.counter * HUD.deelay.getValue()) : this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
            else {
                this.drawStringWithShadow("TPS " + ChatFormatting.GRAY + String.format("%.2f", TpsUtils.getTickRate()), new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "TPS " + ChatFormatting.GRAY + String.format("%.2f", TpsUtils.getTickRate())), new ScaledResolution(HUD.mc).getScaledHeight() + (this.Potions.getValue() ? (this.potCount * -10) : (this.counter * -10)) - 10, HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(this.counter * HUD.deelay.getValue()) : this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
        }
        if (this.Speed.getValue()) {
            final DecimalFormat df = new DecimalFormat("#.#");
            final double deltaX = Minecraft.getMinecraft().player.posX - Minecraft.getMinecraft().player.prevPosX;
            final double deltaZ = Minecraft.getMinecraft().player.posZ - Minecraft.getMinecraft().player.prevPosZ;
            final float tickRate = Minecraft.getMinecraft().timer.tickLength / 1000.0f;
            final String BPSText = df.format(MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) / tickRate * 3.6);
            if (this.pSortUp.getValue()) {
                this.drawStringWithShadow("Speed " + ChatFormatting.GRAY + BPSText + "km/h", new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Speed " + ChatFormatting.GRAY + BPSText + "km/h"), 0 + (this.Potions.getValue() ? (this.potCount * 10) : (this.counter * 10)), HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(this.counter * HUD.deelay.getValue()) : this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
            else {
                this.drawStringWithShadow("Speed " + ChatFormatting.GRAY + BPSText + "km/h", new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Speed " + ChatFormatting.GRAY + BPSText + "km/h"), new ScaledResolution(HUD.mc).getScaledHeight() + (this.Potions.getValue() ? (this.potCount * -10) : (this.counter * -10)) - 10, HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(this.counter * HUD.deelay.getValue()) : this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
        }
        if (this.durability.getValue() && this.isItemTool(HUD.mc.player.getHeldItemMainhand().getItem())) {
            if (this.pSortUp.getValue()) {
                this.drawStringWithShadow("Durability " + ChatFormatting.GRAY + (HUD.mc.player.getHeldItemMainhand().getMaxDamage() - HUD.mc.player.getHeldItemMainhand().getItemDamage()), new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Durability " + ChatFormatting.GRAY + (HUD.mc.player.getHeldItemMainhand().getMaxDamage() - HUD.mc.player.getHeldItemMainhand().getItemDamage())), 0 + (this.Potions.getValue() ? (this.potCount * 10) : (this.counter * 10)), HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(this.counter * HUD.deelay.getValue()) : this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
            else {
                this.drawStringWithShadow("Durability " + ChatFormatting.GRAY + (HUD.mc.player.getHeldItemMainhand().getMaxDamage() - HUD.mc.player.getHeldItemMainhand().getItemDamage()), new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Durability " + ChatFormatting.GRAY + (HUD.mc.player.getHeldItemMainhand().getMaxDamage() - HUD.mc.player.getHeldItemMainhand().getItemDamage())), new ScaledResolution(HUD.mc).getScaledHeight() + (this.Potions.getValue() ? (this.potCount * -10) : (this.counter * -10)) - 10, HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(this.counter * HUD.deelay.getValue()) : this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
        }
        if (this.Time.getValue()) {
            if (this.pSortUp.getValue()) {
                this.drawStringWithShadow("Time " + ChatFormatting.GRAY + new SimpleDateFormat("h:mm a").format(new Date()), new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Time " + ChatFormatting.GRAY + new SimpleDateFormat("h:mm a").format(new Date())), 0 + (this.Potions.getValue() ? (this.potCount * 10) : (this.counter * 10)), HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(this.counter * HUD.deelay.getValue()) : this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
            else {
                this.drawStringWithShadow("Time " + ChatFormatting.GRAY + new SimpleDateFormat("h:mm a").format(new Date()), new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Time " + ChatFormatting.GRAY + new SimpleDateFormat("h:mm a").format(new Date())), new ScaledResolution(HUD.mc).getScaledHeight() + (this.Potions.getValue() ? (this.potCount * -10) : (this.counter * -10)) - 10, HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(this.counter * HUD.deelay.getValue()) : this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
        }
        if (this.playerViewer.getValue()) {
            this.drawPlayer();
        }
        if (this.FPS.getValue()) {
            if (this.pSortUp.getValue()) {
                this.drawStringWithShadow("FPS " + ChatFormatting.GRAY + Minecraft.getDebugFPS(), new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "FPS " + ChatFormatting.GRAY + Minecraft.getDebugFPS()), 0 + (this.Potions.getValue() ? (this.potCount * 10) : (this.counter * 10)), HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(this.counter * HUD.deelay.getValue()) : this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
            else {
                this.drawStringWithShadow("FPS " + ChatFormatting.GRAY + Minecraft.getDebugFPS(), new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "FPS " + ChatFormatting.GRAY + Minecraft.getDebugFPS()), new ScaledResolution(HUD.mc).getScaledHeight() + (this.Potions.getValue() ? (this.potCount * -10) : (this.counter * -10)) - 10, HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(this.counter * HUD.deelay.getValue()) : this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
        }
        final float[] hue = { System.currentTimeMillis() % 11520L / 11520.0f };
        if (this.Potions.getValue()) {
            this.potCount = 0;
            final ScaledResolution resolution = new ScaledResolution(HUD.mc);
            try {
                final String name;
                final double duration;
                final int amplifier;
                final int color;
                final double p1;
                final String seconds;
                final String s;
                final ScaledResolution scaledResolution;
                HUD.mc.player.getActivePotionEffects().forEach(effect -> {
                    name = I18n.format(effect.getPotion().getName(), new Object[0]);
                    duration = effect.getDuration() / 19.99f;
                    amplifier = effect.getAmplifier() + 1;
                    color = effect.getPotion().getLiquidColor();
                    p1 = duration % 60.0;
                    seconds = this.format2.format(p1);
                    s = name + " " + amplifier + ChatFormatting.GRAY + " " + (int)duration / 60 + ":" + seconds;
                    if (this.pSortUp.getValue()) {
                        this.drawStringWithShadow(s, scaledResolution.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), s), 0 + this.potCount * 10, color);
                        ++this.potCount;
                    }
                    else {
                        this.drawStringWithShadow(s, scaledResolution.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), s), scaledResolution.getScaledHeight() + this.potCount * -10 - 10, color);
                        ++this.potCount;
                    }
                    return;
                });
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        if (this.ArrayList.getValue()) {
            final ScaledResolution resolution = new ScaledResolution(HUD.mc);
            final int[] counter7 = { 1 };
            if (this.sortUp.getValue()) {
                this.sort = -1;
            }
            else {
                this.sort = 1;
            }
            this.modCount = 0;
            final ScaledResolution scaledResolution2;
            int x;
            int lWidth;
            int x2;
            final Object o;
            final Object o2;
            final int n6;
            final int n7;
            int x3;
            int lWidth2;
            int x4;
            final int n8;
            final int n9;
            ModuleManager.getModules().stream().filter(Module::isEnabled).filter(Module::isDrawn).sorted(Comparator.comparing(module -> FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), module.getName() + ChatFormatting.GRAY + " " + module.getHudInfo()) * -1)).forEach(m -> {
                if (this.sortUp.getValue()) {
                    if (this.ArrayListHot.getValue()) {
                        RenderUtil.drawRect((float)(scaledResolution2.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), m.getName() + ChatFormatting.GRAY + m.getHudInfo()) - 4), (float)(0 + this.modCount * 10 - 1), (float)(this.getWidth(m.getName() + ChatFormatting.GRAY + m.getHudInfo()) + 4), 11.0f, new Color(21, 21, 21, 100).getRGB());
                        RenderUtil.drawRect((float)(scaledResolution2.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), m.getName() + ChatFormatting.GRAY + m.getHudInfo()) - 4), (float)(0 + this.modCount * 10 - 1), 2.0f, 11.0f, this.color.getRGB());
                    }
                    x = scaledResolution2.getScaledWidth();
                    lWidth = FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), m.getName() + ChatFormatting.GRAY + " " + m.getHudInfo());
                    if (m.animPos < lWidth && m.isEnabled()) {
                        m.animPos = Animation.moveTowards(m.animPos, (float)(lWidth + 1), 0.01f + this.animS.getValue() / 30, 0.1f);
                    }
                    else if (m.animPos > 1.5f && !m.isEnabled()) {
                        m.animPos = Animation.moveTowards(m.animPos, -1.5f, 0.01f + this.animS.getValue() / 30, 0.1f);
                    }
                    else if (m.animPos <= 1.5f && !m.isEnabled()) {
                        m.animPos = -1.0f;
                    }
                    if (m.animPos > lWidth && m.isEnabled()) {
                        m.animPos = (float)lWidth;
                    }
                    x2 = (int)(x - m.animPos);
                    this.drawStringWithShadow(m.getName() + ChatFormatting.GRAY + " " + m.getHudInfo(), x2, 0 + this.modCount * 10, HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(o[0] * HUD.deelay.getValue()) : this.color.getRGB());
                    o2[n6] += 0.02f;
                    ++this.modCount;
                    ++o[n7];
                }
                else {
                    if (this.ArrayListHot.getValue()) {
                        RenderUtil.drawRect((float)(scaledResolution2.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), m.getName() + ChatFormatting.GRAY + m.getHudInfo()) - 4), (float)(scaledResolution2.getScaledHeight() + this.modCount * -10 - 1 - 10), (float)(this.getWidth(m.getName() + ChatFormatting.GRAY + m.getHudInfo()) + 4), 11.0f, new Color(21, 21, 21, 100).getRGB());
                        RenderUtil.drawRect((float)(scaledResolution2.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), m.getName() + ChatFormatting.GRAY + m.getHudInfo()) - 4), (float)(scaledResolution2.getScaledHeight() + this.modCount * -10 - 1 - 10), 2.0f, 11.0f, this.color.getRGB());
                    }
                    x3 = scaledResolution2.getScaledWidth();
                    lWidth2 = FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), m.getName() + ChatFormatting.GRAY + " " + m.getHudInfo());
                    if (m.animPos < lWidth2 && m.isEnabled()) {
                        m.animPos = Animation.moveTowards(m.animPos, (float)(lWidth2 + 1), 0.01f + this.animS.getValue() / 30, 0.1f);
                    }
                    else if (m.animPos > 1.5f && !m.isEnabled()) {
                        m.animPos = Animation.moveTowards(m.animPos, -1.5f, 0.01f + this.animS.getValue() / 30, 0.1f);
                    }
                    else if (m.animPos <= 1.5f && !m.isEnabled()) {
                        m.animPos = -1.0f;
                    }
                    if (m.animPos > lWidth2 && m.isEnabled()) {
                        m.animPos = (float)lWidth2;
                    }
                    x4 = (int)(x3 - m.animPos);
                    this.drawStringWithShadow(m.getName() + ChatFormatting.GRAY + " " + m.getHudInfo(), x4, scaledResolution2.getScaledHeight() + this.modCount * -10 - 10, HUD.rainbowArray.getValue() ? RainbowUtil.rainbow(o[0] * HUD.deelay.getValue()) : this.color.getRGB());
                    o2[n8] += 0.02f;
                    ++this.modCount;
                    ++o[n9];
                }
                return;
            });
        }
        if (this.ArmorHud.getValue()) {
            GlStateManager.enableTexture2D();
            final ScaledResolution resolution = new ScaledResolution(HUD.mc);
            final int j = resolution.getScaledWidth() / 2;
            int iteration = 0;
            final int y = resolution.getScaledHeight() - 55 - (HUD.mc.player.isInWater() ? 10 : 0);
            for (final ItemStack is : HUD.mc.player.inventory.armorInventory) {
                ++iteration;
                if (is.isEmpty()) {
                    continue;
                }
                final int x5 = j - 90 + (9 - iteration) * 20 + 2;
                GlStateManager.enableDepth();
                HUD.itemRender.zLevel = 200.0f;
                HUD.itemRender.renderItemAndEffectIntoGUI(is, x5, y);
                HUD.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, is, x5, y, "");
                HUD.itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                final String s2 = (is.getCount() > 1) ? (is.getCount() + "") : "";
                HUD.mc.fontRenderer.drawStringWithShadow(s2, (float)(x5 + 19 - 2 - HUD.mc.fontRenderer.getStringWidth(s2)), (float)(y + 9), 16777215);
                final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
                final float red = 1.0f - green;
                final int dmg = 100 - (int)(red * 100.0f);
                this.drawStringWithShadow(dmg + "", x5 + 8 - HUD.mc.fontRenderer.getStringWidth(dmg + "") / 2, y - 11, ColorHolder.toHex((int)(red * 255.0f), (int)(green * 255.0f), 0));
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    
    private void drawStringWithShadow(final String text, final int x, final int y, final int color) {
        if (ModuleManager.isModuleEnabled("CustomFont")) {
            Xannax.fontRenderer.drawStringWithShadow(text, x, y, color);
        }
        else {
            HUD.mc.fontRenderer.drawStringWithShadow(text, (float)x, (float)y, color);
        }
    }
    
    private int getWidth(final String s) {
        if (ModuleManager.isModuleEnabled("CustomFont")) {
            return Xannax.fontRenderer.getStringWidth(s);
        }
        return HUD.mc.fontRenderer.getStringWidth(s);
    }
    
    private void renderHole(final double holex, final double holey) {
        final double leftX = holex;
        final double leftY = holey + 16.0;
        final double upX = holex + 16.0;
        final double upY = holey;
        final double rightX = holex + 32.0;
        final double rightY = holey + 16.0;
        final double bottomX = holex + 16.0;
        final double bottomY = holey + 32.0;
        final Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f);
        final BlockPos playerPos = new BlockPos(vec3d);
        switch (HUD.mc.getRenderViewEntity().getHorizontalFacing()) {
            case NORTH: {
                if (this.northObby() || this.northBrock()) {
                    this.renderItem(upX, upY, new ItemStack(HUD.mc.world.getBlockState(playerPos.north()).getBlock()));
                }
                if (this.westObby() || this.westBrock()) {
                    this.renderItem(leftX, leftY, new ItemStack(HUD.mc.world.getBlockState(playerPos.west()).getBlock()));
                }
                if (this.eastObby() || this.eastBrock()) {
                    this.renderItem(rightX, rightY, new ItemStack(HUD.mc.world.getBlockState(playerPos.east()).getBlock()));
                }
                if (this.southObby() || this.southBrock()) {
                    this.renderItem(bottomX, bottomY, new ItemStack(HUD.mc.world.getBlockState(playerPos.south()).getBlock()));
                    break;
                }
                break;
            }
            case SOUTH: {
                if (this.southObby() || this.southBrock()) {
                    this.renderItem(upX, upY, new ItemStack(HUD.mc.world.getBlockState(playerPos.south()).getBlock()));
                }
                if (this.eastObby() || this.eastBrock()) {
                    this.renderItem(leftX, leftY, new ItemStack(HUD.mc.world.getBlockState(playerPos.east()).getBlock()));
                }
                if (this.westObby() || this.westBrock()) {
                    this.renderItem(rightX, rightY, new ItemStack(HUD.mc.world.getBlockState(playerPos.west()).getBlock()));
                }
                if (this.northObby() || this.northBrock()) {
                    this.renderItem(bottomX, bottomY, new ItemStack(HUD.mc.world.getBlockState(playerPos.north()).getBlock()));
                    break;
                }
                break;
            }
            case WEST: {
                if (this.westObby() || this.westBrock()) {
                    this.renderItem(upX, upY, new ItemStack(HUD.mc.world.getBlockState(playerPos.west()).getBlock()));
                }
                if (this.southObby() || this.southBrock()) {
                    this.renderItem(leftX, leftY, new ItemStack(HUD.mc.world.getBlockState(playerPos.south()).getBlock()));
                }
                if (this.northObby() || this.northBrock()) {
                    this.renderItem(rightX, rightY, new ItemStack(HUD.mc.world.getBlockState(playerPos.north()).getBlock()));
                }
                if (this.eastObby() || this.eastBrock()) {
                    this.renderItem(bottomX, bottomY, new ItemStack(HUD.mc.world.getBlockState(playerPos.east()).getBlock()));
                    break;
                }
                break;
            }
            case EAST: {
                if (this.eastObby() || this.eastBrock()) {
                    this.renderItem(upX, upY, new ItemStack(HUD.mc.world.getBlockState(playerPos.east()).getBlock()));
                }
                if (this.northObby() || this.northBrock()) {
                    this.renderItem(leftX, leftY, new ItemStack(HUD.mc.world.getBlockState(playerPos.north()).getBlock()));
                }
                if (this.southObby() || this.southBrock()) {
                    this.renderItem(rightX, rightY, new ItemStack(HUD.mc.world.getBlockState(playerPos.south()).getBlock()));
                }
                if (this.westObby() || this.westBrock()) {
                    this.renderItem(bottomX, bottomY, new ItemStack(HUD.mc.world.getBlockState(playerPos.west()).getBlock()));
                    break;
                }
                break;
            }
        }
    }
    
    private void renderItem(final double x, final double y, final ItemStack is) {
        RenderHelper.enableGUIStandardItemLighting();
        HUD.mc.getRenderItem().renderItemAndEffectIntoGUI(is, (int)x, (int)y);
        RenderHelper.disableStandardItemLighting();
    }
    
    private boolean northObby() {
        final Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f);
        final BlockPos playerPos = new BlockPos(vec3d);
        return HUD.mc.world.getBlockState(playerPos.north()).getBlock() == Blocks.OBSIDIAN;
    }
    
    private boolean eastObby() {
        final Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f);
        final BlockPos playerPos = new BlockPos(vec3d);
        return HUD.mc.world.getBlockState(playerPos.east()).getBlock() == Blocks.OBSIDIAN;
    }
    
    private boolean southObby() {
        final Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f);
        final BlockPos playerPos = new BlockPos(vec3d);
        return HUD.mc.world.getBlockState(playerPos.south()).getBlock() == Blocks.OBSIDIAN;
    }
    
    private boolean westObby() {
        final Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f);
        final BlockPos playerPos = new BlockPos(vec3d);
        return HUD.mc.world.getBlockState(playerPos.west()).getBlock() == Blocks.OBSIDIAN;
    }
    
    private boolean northBrock() {
        final Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f);
        final BlockPos playerPos = new BlockPos(vec3d);
        return HUD.mc.world.getBlockState(playerPos.north()).getBlock() == Blocks.BEDROCK;
    }
    
    private boolean eastBrock() {
        final Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f);
        final BlockPos playerPos = new BlockPos(vec3d);
        return HUD.mc.world.getBlockState(playerPos.east()).getBlock() == Blocks.BEDROCK;
    }
    
    private boolean southBrock() {
        final Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f);
        final BlockPos playerPos = new BlockPos(vec3d);
        return HUD.mc.world.getBlockState(playerPos.south()).getBlock() == Blocks.BEDROCK;
    }
    
    private boolean westBrock() {
        final Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f);
        final BlockPos playerPos = new BlockPos(vec3d);
        return HUD.mc.world.getBlockState(playerPos.west()).getBlock() == Blocks.BEDROCK;
    }
    
    public int getPing() {
        int p;
        if (HUD.mc.player == null || HUD.mc.getConnection() == null || HUD.mc.getConnection().getPlayerInfo(HUD.mc.player.getName()) == null) {
            p = -1;
        }
        else {
            HUD.mc.player.getName();
            p = Objects.requireNonNull(HUD.mc.getConnection().getPlayerInfo(HUD.mc.player.getName())).getResponseTime();
        }
        return p;
    }
    
    public void renderTotemHUD() {
        final ScaledResolution resolution = new ScaledResolution(HUD.mc);
        final int width = resolution.getScaledWidth();
        final int height = resolution.getScaledHeight();
        int totems = HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::func_190916_E).sum();
        if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            totems += HUD.mc.player.getHeldItemOffhand().getCount();
        }
        if (totems > 0) {
            GlStateManager.enableTexture2D();
            final int i = width / 2;
            final int iteration = 0;
            final int y = height - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            final int x = i - 189 + 180 + 2;
            GlStateManager.enableDepth();
            HUD.itemRender.zLevel = 200.0f;
            HUD.itemRender.renderItemAndEffectIntoGUI(HUD.totemm, x, y);
            HUD.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, HUD.totemm, x, y, "");
            HUD.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.drawStringWithShadow(totems + "", x + 19 - 2 - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), totems + ""), y + 9, 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    
    public void drawPlayer() {
        final EntityPlayer ent = (EntityPlayer)HUD.mc.player;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate(0.0f, 0.0f, 5.0f, 0.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.playerViewerX.getValue() + 25), (float)(this.playerViewerY.getValue() + 25), 50.0f);
        GlStateManager.scale(-50.0 * this.playerScale.getValue(), 50.0 * this.playerScale.getValue(), 50.0 * this.playerScale.getValue());
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(this.playerViewerY.getValue() / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager rendermanager = HUD.mc.getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        try {
            rendermanager.renderEntity((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        }
        catch (Exception ex) {}
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }
    
    public static void sexyRainbow() {
        final String watermark2 = "XannaX 0.1";
        int x1 = 0;
        for (int i = 0; i < watermark2.length(); ++i) {
            final char ch = watermark2.charAt(i);
            FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), String.valueOf(ch), 2 + x1, 2, RenderUtil.generateRainbowFadingColor(watermark2.length() - 1 - i, true));
            x1 += FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), String.valueOf(ch));
        }
    }
    
    public String getFacing() {
        switch (MathHelper.floor(HUD.mc.player.rotationYaw * 8.0f / 360.0f + 0.5) & 0x7) {
            case 0: {
                return "South";
            }
            case 1: {
                return "South";
            }
            case 2: {
                return "West";
            }
            case 3: {
                return "West";
            }
            case 4: {
                return "North";
            }
            case 5: {
                return "North";
            }
            case 6: {
                return "East";
            }
            case 7: {
                return "East";
            }
            default: {
                return "Invalid";
            }
        }
    }
    
    public String getTowards() {
        switch (MathHelper.floor(HUD.mc.player.rotationYaw * 8.0f / 360.0f + 0.5) & 0x7) {
            case 0: {
                return "+Z";
            }
            case 1: {
                return "+Z";
            }
            case 2: {
                return "-X";
            }
            case 3: {
                return "-X";
            }
            case 4: {
                return "-Z";
            }
            case 5: {
                return "-Z";
            }
            case 6: {
                return "+X";
            }
            case 7: {
                return "+X";
            }
            default: {
                return "Invalid";
            }
        }
    }
    
    private int getHealthColor(final EntityLivingBase player) {
        return Color.HSBtoRGB(Math.max(0.0f, Math.min(player.getHealth(), player.getMaxHealth()) / player.getMaxHealth()) / 3.0f, 1.0f, 0.8f) | 0xFF000000;
    }
    
    public ChatFormatting getHealthThing(final EntityLivingBase player) {
        if (player.getHealth() + player.getAbsorptionAmount() <= 5.0f) {
            return ChatFormatting.RED;
        }
        if (player.getHealth() + player.getAbsorptionAmount() > 5.0f && player.getHealth() + player.getAbsorptionAmount() < 15.0f) {
            return ChatFormatting.YELLOW;
        }
        if (player.getHealth() + player.getAbsorptionAmount() >= 15.0f) {
            return ChatFormatting.GREEN;
        }
        return ChatFormatting.WHITE;
    }
    
    public ChatFormatting getDistanceThing(final EntityLivingBase player) {
        if (HUD.mc.player.getDistance((Entity)player) < 20.0f) {
            return ChatFormatting.RED;
        }
        if (HUD.mc.player.getDistance((Entity)player) >= 20.0f && HUD.mc.player.getDistance((Entity)player) < 50.0f) {
            return ChatFormatting.YELLOW;
        }
        if (HUD.mc.player.getDistance((Entity)player) >= 50.0f) {
            return ChatFormatting.GREEN;
        }
        return ChatFormatting.WHITE;
    }
    
    public boolean isItemTool(final Item item) {
        return item instanceof ItemArmor || item == Items.DIAMOND_SWORD || item == Items.DIAMOND_PICKAXE || item == Items.DIAMOND_AXE || item == Items.DIAMOND_SHOVEL || item == Items.DIAMOND_HOE || item == Items.IRON_SWORD || item == Items.IRON_PICKAXE || item == Items.IRON_AXE || item == Items.IRON_SHOVEL || item == Items.IRON_HOE || item == Items.GOLDEN_SWORD || item == Items.GOLDEN_PICKAXE || item == Items.GOLDEN_AXE || item == Items.GOLDEN_SHOVEL || item == Items.GOLDEN_HOE || item == Items.STONE_SWORD || item == Items.STONE_PICKAXE || item == Items.STONE_AXE || item == Items.STONE_SHOVEL || item == Items.STONE_HOE || item == Items.WOODEN_SWORD || item == Items.WOODEN_PICKAXE || item == Items.WOODEN_AXE || item == Items.WOODEN_SHOVEL || item == Items.WOODEN_HOE;
    }
    
    static {
        tickRates = new float[20];
        itemRender = Minecraft.getMinecraft().getRenderItem();
        totemm = new ItemStack(Items.TOTEM_OF_UNDYING);
    }
}
