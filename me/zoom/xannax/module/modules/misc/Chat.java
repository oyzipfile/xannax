// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.Xannax;
import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.command.Command;
import net.minecraft.network.play.client.CPacketChatMessage;
import java.util.function.Predicate;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.module.modules.client.ClickGuiModule;
import net.minecraft.util.text.TextComponentString;
import java.util.Date;
import java.text.SimpleDateFormat;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Chat extends Module
{
    public Setting.Boolean clearBkg;
    Setting.Boolean chattimestamps;
    Setting.Mode format;
    Setting.Mode color;
    Setting.Mode decoration;
    public static Setting.Boolean customFont;
    public static Setting.Boolean noChatShadow;
    Setting.Boolean space;
    Setting.Boolean greentext;
    @EventHandler
    private final Listener<ClientChatReceivedEvent> chatReceivedEventListener;
    @EventHandler
    private final Listener<PacketEvent.Send> listener;
    
    public Chat() {
        super("Chat", "Chat", Category.Misc);
        String dateFormat;
        String date;
        final TextComponentString textComponentString;
        TextComponentString time;
        this.chatReceivedEventListener = new Listener<ClientChatReceivedEvent>(event -> {
            if (this.chattimestamps.getValue()) {
                dateFormat = this.format.getValue().replace("H24", "k").replace("H12", "h");
                date = new SimpleDateFormat(dateFormat).format(new Date());
                new TextComponentString(ClickGuiModule.getTextColor() + "<" + date + ">" + ChatFormatting.RESET);
                time = textComponentString;
                event.setMessage(time.appendSibling(event.getMessage()));
            }
            return;
        }, (Predicate<ClientChatReceivedEvent>[])new Predicate[0]);
        String message;
        String prefix;
        String prefix2;
        String s;
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (this.greentext.getValue() && event.getPacket() instanceof CPacketChatMessage) {
                if (!((CPacketChatMessage)event.getPacket()).getMessage().startsWith("/") && !((CPacketChatMessage)event.getPacket()).getMessage().startsWith(Command.getPrefix())) {
                    message = ((CPacketChatMessage)event.getPacket()).getMessage();
                    prefix = "";
                    prefix2 = ">";
                    s = prefix2 + message;
                    if (s.length() <= 255) {
                        ((CPacketChatMessage)event.getPacket()).message = s;
                    }
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> formats = new ArrayList<String>();
        formats.add("H24:mm");
        formats.add("H12:mm");
        formats.add("H12:mm a");
        formats.add("H24:mm:ss");
        formats.add("H12:mm:ss");
        formats.add("H12:mm:ss a");
        this.clearBkg = this.registerBoolean("Clear Chat", "ClearChat", false);
        this.greentext = this.registerBoolean("Green Text", "GreenText", false);
        this.chattimestamps = this.registerBoolean("Chat Time Stamps", "ChatTimeStamps", false);
        this.format = this.registerMode("Format", "Format", formats, "H24:mm");
        this.space = this.registerBoolean("Space", "Space", false);
        Chat.customFont = this.registerBoolean("CustomFont", "CustomFont", false);
        Chat.noChatShadow = this.registerBoolean("NoChatShadow", "NoChatShadow", false);
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
    
    public String toUnicode(final String s) {
        return s.toLowerCase().replace("a", "\u1d00").replace("b", "\u0299").replace("c", "\u1d04").replace("d", "\u1d05").replace("e", "\u1d07").replace("f", "\ua730").replace("g", "\u0262").replace("h", "\u029c").replace("i", "\u026a").replace("j", "\u1d0a").replace("k", "\u1d0b").replace("l", "\u029f").replace("m", "\u1d0d").replace("n", "\u0274").replace("o", "\u1d0f").replace("p", "\u1d18").replace("q", "\u01eb").replace("r", "\u0280").replace("s", "\ua731").replace("t", "\u1d1b").replace("u", "\u1d1c").replace("v", "\u1d20").replace("w", "\u1d21").replace("x", "\u02e3").replace("y", "\u028f").replace("z", "\u1d22");
    }
}
