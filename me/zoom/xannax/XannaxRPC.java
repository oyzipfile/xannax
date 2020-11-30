// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;
import net.minecraft.client.Minecraft;

public class XannaxRPC
{
    private static final String ClientId = "773651992160894978";
    private static final Minecraft mc;
    private static final DiscordRPC rpc;
    public static DiscordRichPresence presence;
    private static String details;
    private static String state;
    
    public static void init() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + String.valueOf(var1) + ", var2: " + var2));
        XannaxRPC.rpc.Discord_Initialize("773651992160894978", handlers, true, "");
        XannaxRPC.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        XannaxRPC.presence.details = "XannaX0.8.5";
        XannaxRPC.presence.state = "vibin'";
        XannaxRPC.presence.largeImageKey = "large";
        XannaxRPC.presence.largeImageText = "XannaX 0.8.5";
        XannaxRPC.presence.smallImageKey = "small";
        XannaxRPC.presence.smallImageText = XannaxRPC.mc.getSession().getUsername();
        XannaxRPC.rpc.Discord_UpdatePresence(XannaxRPC.presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    XannaxRPC.rpc.Discord_RunCallbacks();
                    XannaxRPC.details = "XannaX0.8.5";
                    XannaxRPC.state = "vibin'";
                    if (XannaxRPC.mc.isIntegratedServerRunning()) {
                        XannaxRPC.details = "Singleplayer";
                    }
                    else if (XannaxRPC.mc.getCurrentServerData() != null) {
                        if (!XannaxRPC.mc.getCurrentServerData().serverIP.equals("")) {
                            XannaxRPC.details = XannaxRPC.mc.getCurrentServerData().serverIP;
                        }
                    }
                    else {
                        XannaxRPC.details = "Main Menu";
                    }
                    if (!XannaxRPC.details.equals(XannaxRPC.presence.details) || !XannaxRPC.state.equals(XannaxRPC.presence.state)) {
                        XannaxRPC.presence.startTimestamp = System.currentTimeMillis() / 1000L;
                    }
                    XannaxRPC.presence.details = XannaxRPC.details;
                    XannaxRPC.presence.state = XannaxRPC.state;
                    XannaxRPC.rpc.Discord_UpdatePresence(XannaxRPC.presence);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    Thread.sleep(5000L);
                }
                catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
        }, "Discord-RPC-Callback-Handler").start();
    }
    
    public static void shutdown() {
        XannaxRPC.rpc.Discord_Shutdown();
    }
    
    static {
        mc = Minecraft.getMinecraft();
        rpc = DiscordRPC.INSTANCE;
        XannaxRPC.presence = new DiscordRichPresence();
    }
}
