// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.Xannax;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.EnumHand;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import java.util.function.Predicate;
import me.zoom.xannax.command.Command;
import java.util.Random;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemFood;
import java.util.concurrent.ThreadLocalRandom;
import me.zoom.xannax.event.events.PlayerJumpEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import me.zoom.xannax.event.events.DestroyBlockEvent;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Announcer extends Module
{
    public static String walkMessage;
    public static String placeMessage;
    public static String jumpMessage;
    public static String breakMessage;
    public static String attackMessage;
    public static String eatMessage;
    public static String guiMessage;
    public static int blockBrokeDelay;
    static int blockPlacedDelay;
    static int jumpDelay;
    static int attackDelay;
    static int eattingDelay;
    static long lastPositionUpdate;
    static double lastPositionX;
    static double lastPositionY;
    static double lastPositionZ;
    private static double speed;
    String heldItem;
    int blocksPlaced;
    int blocksBroken;
    int eaten;
    public Setting.Boolean clientSide;
    Setting.Mode language;
    Setting.Boolean walk;
    Setting.Boolean place;
    Setting.Boolean jump;
    Setting.Boolean breaking;
    Setting.Boolean attack;
    Setting.Boolean eat;
    public Setting.Boolean clickGui;
    Setting.Integer delay;
    @EventHandler
    private final Listener<LivingEntityUseItemEvent.Finish> eatListener;
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener;
    @EventHandler
    private final Listener<DestroyBlockEvent> destroyListener;
    @EventHandler
    private final Listener<AttackEntityEvent> attackListener;
    @EventHandler
    private final Listener<PlayerJumpEvent> jumpListener;
    
    public Announcer() {
        super("Announcer", "Be annoying!", Category.Misc);
        this.heldItem = "";
        this.blocksPlaced = 0;
        this.blocksBroken = 0;
        this.eaten = 0;
        final int randomNum;
        Random random;
        this.eatListener = new Listener<LivingEntityUseItemEvent.Finish>(event -> {
            randomNum = ThreadLocalRandom.current().nextInt(1, 11);
            if (event.getEntity() == Announcer.mc.player && (event.getItem().getItem() instanceof ItemFood || event.getItem().getItem() instanceof ItemAppleGold)) {
                ++this.eaten;
                if (Announcer.eattingDelay >= 300 * this.delay.getValue() && this.eat.getValue() && this.eaten > randomNum) {
                    random = new Random();
                    if (this.clientSide.getValue()) {
                        Command.sendClientMessage(Announcer.eatMessage.replace("{amount}", this.eaten + "").replace("{name}", Announcer.mc.player.getHeldItemMainhand().getDisplayName()));
                    }
                    else {
                        Announcer.mc.player.sendChatMessage(Announcer.eatMessage.replace("{amount}", this.eaten + "").replace("{name}", Announcer.mc.player.getHeldItemMainhand().getDisplayName()));
                    }
                    this.eaten = 0;
                    Announcer.eattingDelay = 0;
                }
            }
            return;
        }, (Predicate<LivingEntityUseItemEvent.Finish>[])new Predicate[0]);
        int randomNum2;
        Random random2;
        String msg;
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && Announcer.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock) {
                ++this.blocksPlaced;
                randomNum2 = ThreadLocalRandom.current().nextInt(1, 11);
                if (Announcer.blockPlacedDelay >= 150 * this.delay.getValue() && this.place.getValue() && this.blocksPlaced > randomNum2) {
                    random2 = new Random();
                    msg = Announcer.placeMessage.replace("{amount}", this.blocksPlaced + "").replace("{name}", Announcer.mc.player.getHeldItemMainhand().getDisplayName());
                    if (this.clientSide.getValue()) {
                        Command.sendClientMessage(msg);
                    }
                    else {
                        Announcer.mc.player.sendChatMessage(msg);
                    }
                    this.blocksPlaced = 0;
                    Announcer.blockPlacedDelay = 0;
                }
            }
            return;
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
        final int randomNum3;
        Random random3;
        String msg2;
        this.destroyListener = new Listener<DestroyBlockEvent>(event -> {
            ++this.blocksBroken;
            randomNum3 = ThreadLocalRandom.current().nextInt(1, 11);
            if (Announcer.blockBrokeDelay >= 300 * this.delay.getValue() && this.breaking.getValue() && this.blocksBroken > randomNum3) {
                random3 = new Random();
                msg2 = Announcer.breakMessage.replace("{amount}", this.blocksBroken + "").replace("{name}", Announcer.mc.world.getBlockState(event.getBlockPos()).getBlock().getLocalizedName());
                if (this.clientSide.getValue()) {
                    Command.sendClientMessage(msg2);
                }
                else {
                    Announcer.mc.player.sendChatMessage(msg2);
                }
                this.blocksBroken = 0;
                Announcer.blockBrokeDelay = 0;
            }
            return;
        }, (Predicate<DestroyBlockEvent>[])new Predicate[0]);
        String msg3;
        this.attackListener = new Listener<AttackEntityEvent>(event -> {
            if (this.attack.getValue() && !(event.getTarget() instanceof EntityEnderCrystal) && Announcer.attackDelay >= 300 * this.delay.getValue()) {
                msg3 = Announcer.attackMessage.replace("{name}", event.getTarget().getName()).replace("{item}", Announcer.mc.player.getHeldItemMainhand().getDisplayName());
                if (this.clientSide.getValue()) {
                    Command.sendClientMessage(msg3);
                }
                else {
                    Announcer.mc.player.sendChatMessage(msg3);
                }
                Announcer.attackDelay = 0;
            }
            return;
        }, (Predicate<AttackEntityEvent>[])new Predicate[0]);
        Random random4;
        Random random5;
        this.jumpListener = new Listener<PlayerJumpEvent>(event -> {
            if (this.jump.getValue() && Announcer.jumpDelay >= 300 * this.delay.getValue()) {
                if (this.clientSide.getValue()) {
                    random4 = new Random();
                    Command.sendClientMessage(Announcer.jumpMessage);
                }
                else {
                    random5 = new Random();
                    Announcer.mc.player.sendChatMessage(Announcer.jumpMessage);
                }
                Announcer.jumpDelay = 0;
            }
        }, (Predicate<PlayerJumpEvent>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("English");
        modes.add("Hebrew");
        modes.add("German");
        modes.add("Spanish");
        modes.add("Swag");
        modes.add("Dutch");
        modes.add("Portuguese");
        this.language = this.registerMode("Language", "Language", modes, "English");
        this.clientSide = this.registerBoolean("Client Side", "ClientSide", false);
        this.walk = this.registerBoolean("Walk", "Walk", true);
        this.place = this.registerBoolean("Place", "Place", true);
        this.jump = this.registerBoolean("Jump", "Jump", true);
        this.breaking = this.registerBoolean("Breaking", "Breaking", true);
        this.attack = this.registerBoolean("Attack", "Attack", true);
        this.eat = this.registerBoolean("Eat", "Eat", true);
        this.clickGui = this.registerBoolean("GUI", "GUI", true);
        this.delay = this.registerInteger("Delay", "Delay", 1, 1, 20);
    }
    
    @Override
    public void onUpdate() {
        ++Announcer.blockBrokeDelay;
        ++Announcer.blockPlacedDelay;
        ++Announcer.jumpDelay;
        ++Announcer.attackDelay;
        ++Announcer.eattingDelay;
        this.heldItem = Announcer.mc.player.getHeldItemMainhand().getDisplayName();
        if (this.language.getValue().equalsIgnoreCase("English")) {
            Announcer.walkMessage = "I just walked {blocks} meters thanks to XannaX!";
            Announcer.placeMessage = "I just placed {amount} {name} thanks to XannaX!";
            Announcer.jumpMessage = "I just jumped thanks to XannaX!";
            Announcer.breakMessage = "I just mined {amount} {name} thanks to XannaX!";
            Announcer.attackMessage = "I just attacked {name} with a {item} thanks to XannaX!";
            Announcer.eatMessage = "I just ate {amount} {name} thanks to XannaX!";
            Announcer.guiMessage = "I just opened my advanced GUI thanks to XannaX!";
        }
        if (this.language.getValue().equalsIgnoreCase("Hebrew")) {
            Announcer.walkMessage = "\u05d4\u05e8\u05d2\u05e2 \u05d4\u05dc\u05db\u05ea\u05d9 {blocks} \u05de\u05d8\u05e8\u05d9\u05dd \u05ea\u05d5\u05d3\u05d5\u05ea \u05dcXannaX!";
            Announcer.placeMessage = "\u05d4\u05e8\u05d2\u05e2 \u05e9\u05de\u05ea\u05d9 {amount} {name} \u05ea\u05d5\u05d3\u05d5\u05ea \u05dcXannaX!";
            Announcer.jumpMessage = "\u05d4\u05e8\u05d2\u05e2 \u05e7\u05e4\u05e6\u05ea\u05d9 \u05ea\u05d5\u05d3\u05d5\u05ea \u05dcXannaX!";
            Announcer.breakMessage = "\u05d4\u05e8\u05d2\u05e2 \u05e9\u05d1\u05e8\u05ea\u05d9 {amount} {name} \u05ea\u05d5\u05d3\u05d5\u05ea \u05dcXannaX!";
            Announcer.attackMessage = "\u05d4\u05e8\u05d2\u05e2 \u05d4\u05db\u05d9\u05ea\u05d9 \u05d0\u05ea {name} \u05e2\u05dd {item} \u05ea\u05d5\u05d3\u05d5\u05ea \u05dcXannaX!";
            Announcer.eatMessage = "\u05d4\u05e8\u05d2\u05e2 \u05d0\u05db\u05dc\u05ea\u05d9 {amount} {name} \u05ea\u05d5\u05d3\u05d5\u05ea \u05dcXannaX!";
            Announcer.guiMessage = "\u05d4\u05e8\u05d2\u05e2 \u05e4\u05ea\u05d7\u05ea\u05d9 \u05d0\u05ea \u05d4 GUI \u05d4\u05de\u05ea\u05e7\u05d3\u05dd \u05e9\u05dc\u05d9 \u05ea\u05d5\u05d3\u05d5\u05ea \u05dcXannaX!";
        }
        if (this.language.getValue().equalsIgnoreCase("German")) {
            Announcer.walkMessage = "Ich bin grade {blocks} meter gelaufen dank XannaX!";
            Announcer.placeMessage = "Ich habe grade {amount}{name} plaziert dank XannaX!";
            Announcer.jumpMessage = "Ich bin grade gesprungen dank XannaX!";
            Announcer.breakMessage = "Ich habe grade {amount}{name} gemined dank XannaX!";
            Announcer.attackMessage = "Ich habe grade {name} mit einem {item} attackiert dank XannaX!";
            Announcer.eatMessage = "Ich habe grade {amount} {name} gegessen dank XannaX!";
            Announcer.guiMessage = "Ich habe grade mein erweitertes GUI ge\u00f6ffnet dank XannaX!";
        }
        if (this.language.getValue().equalsIgnoreCase("Spanish")) {
            Announcer.walkMessage = "Acabo de andar {blocks} metros gracias a XannaX!";
            Announcer.placeMessage = "Acabo de colocar {amount} {name} gracias a XannaX!";
            Announcer.jumpMessage = "Acabo de saltar gracias a XannaX!";
            Announcer.breakMessage = "Acabo de minar {amount} {name} gracias a XannaX!";
            Announcer.attackMessage = "Acabo de atacar a {name} con {item} gracias a XannaX!";
            Announcer.eatMessage = "Acabo de comer {amount} {name} gracias a XannaX!";
            Announcer.guiMessage = "Acabo de abrir mi GUI avanzado gracias a XannaX!";
        }
        if (this.language.getValue().equalsIgnoreCase("Swag")) {
            Announcer.walkMessage = "a young nigga jus stepped {blocks} meters because of muffukin XannaX!";
            Announcer.placeMessage = "a young nigga jus placed {amount} {name} because of muffukin XannaX!";
            Announcer.jumpMessage = "a young nigga jus jumped because of muffukin XannaX!";
            Announcer.breakMessage = "a young nigga jus mined {amount} {name} because of muffukin XannaX!";
            Announcer.attackMessage = "a young nigga jus jumped {name} with a muffukin {item} because of muffukin XannaX!";
            Announcer.eatMessage = "a young nigga jus smoked {amount} weed because of muffukin XannaX!";
            Announcer.guiMessage = "a young nigga just open da muffukin advanced GUI because of muffukin XannaX!";
        }
        if (this.language.getValue().equalsIgnoreCase("Dutch")) {
            Announcer.walkMessage = "Ik heb net {blocks} gelopen door XannaX!";
            Announcer.placeMessage = "Ik heb net {amount} {name} geplaatst door XannaX!";
            Announcer.jumpMessage = "Ik sprong door XannaX!";
            Announcer.breakMessage = "Ik heb {amount} {name} gebroken door XannaX!";
            Announcer.attackMessage = "Ik heb zojuist {naam} aangevallen met een {item} dankzij XannaX!";
            Announcer.eatMessage = "Ik heb net {amount} {name} gegeten door XannaX!";
            Announcer.guiMessage = "Ik heb mijn hackerman menu geopend door XannaX!";
        }
        if (this.language.getValue().equalsIgnoreCase("Portuguese")) {
            Announcer.walkMessage = "Eu acabei de andar {blocks} gracas a XannaX!";
            Announcer.placeMessage = "Eu acabei de colocar {amount} {name} gracas a XannaX!";
            Announcer.jumpMessage = "Eu acabei de pular gracas a XannaX!";
            Announcer.breakMessage = "Eu acabei de minerar {amount} {name} gracas a XannaX!";
            Announcer.attackMessage = "Eu acabei de atacar {name} com {item} gracas a XannaX!";
            Announcer.eatMessage = "Eu acabei de comer {amount} {name} gracas a XannaX!";
            Announcer.guiMessage = "Eu acabei de abrir a minha avan\u00c3§ada GUI gracas a XannaX!";
        }
        if (this.walk.getValue() && Announcer.lastPositionUpdate + 5000L * this.delay.getValue() < System.currentTimeMillis()) {
            final double d0 = Announcer.lastPositionX - Announcer.mc.player.lastTickPosX;
            final double d2 = Announcer.lastPositionY - Announcer.mc.player.lastTickPosY;
            final double d3 = Announcer.lastPositionZ - Announcer.mc.player.lastTickPosZ;
            Announcer.speed = Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
            if (Announcer.speed > 1.0 && Announcer.speed <= 5000.0) {
                final String walkAmount = new DecimalFormat("0.00").format(Announcer.speed);
                final Random random = new Random();
                if (this.clientSide.getValue()) {
                    Command.sendClientMessage(Announcer.walkMessage.replace("{blocks}", walkAmount));
                }
                else {
                    Announcer.mc.player.sendChatMessage(Announcer.walkMessage.replace("{blocks}", walkAmount));
                }
                Announcer.lastPositionUpdate = System.currentTimeMillis();
                Announcer.lastPositionX = Announcer.mc.player.lastTickPosX;
                Announcer.lastPositionY = Announcer.mc.player.lastTickPosY;
                Announcer.lastPositionZ = Announcer.mc.player.lastTickPosZ;
            }
        }
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
        this.blocksPlaced = 0;
        this.blocksBroken = 0;
        this.eaten = 0;
        Announcer.speed = 0.0;
        Announcer.blockBrokeDelay = 0;
        Announcer.blockPlacedDelay = 0;
        Announcer.jumpDelay = 0;
        Announcer.attackDelay = 0;
        Announcer.eattingDelay = 0;
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
    
    static {
        Announcer.blockBrokeDelay = 0;
        Announcer.blockPlacedDelay = 0;
        Announcer.jumpDelay = 0;
        Announcer.attackDelay = 0;
        Announcer.eattingDelay = 0;
    }
}
