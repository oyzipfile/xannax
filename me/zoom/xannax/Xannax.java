// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax;

import me.zero.alpine.EventManager;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import me.zoom.xannax.command.CommandManager;
import me.zoom.xannax.util.config.Stopper;
import me.zoom.xannax.util.TpsUtils;
import java.awt.Font;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import me.zero.alpine.EventBus;
import me.zoom.xannax.util.enemy.Enemies;
import me.zoom.xannax.util.font.CFontRenderer;
import me.zoom.xannax.event.EventProcessor;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.setting.SettingsManager;
import me.zoom.xannax.util.friend.Friends;
import me.zoom.xannax.clickgui.ClickGUI;
import me.zoom.xannax.util.config.LoadModules;
import me.zoom.xannax.util.config.SaveModules;
import me.zoom.xannax.util.config.LoadConfiguration;
import me.zoom.xannax.util.config.SaveConfiguration;
import me.zoom.xannax.util.CapeUtils;
import me.zoom.xannax.macro.MacroManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "xannax", name = "XannaX", version = "0.8.5")
public class Xannax
{
    public static final String MOD_ID = "xannax";
    public static final String MOD_NAME = "XannaX";
    public static final String VERSION = "0.8.5";
    public static Minecraft mc;
    public static final Logger log;
    public MacroManager macroManager;
    public CapeUtils capeUtils;
    public SaveConfiguration saveConfiguration;
    public LoadConfiguration loadConfiguration;
    public SaveModules saveModules;
    public LoadModules loadModules;
    public ClickGUI clickGUI;
    public Friends friends;
    public SettingsManager settingsManager;
    public ModuleManager moduleManager;
    EventProcessor eventProcessor;
    public static CFontRenderer fontRenderer;
    public static Enemies enemies;
    public static final EventBus EVENT_BUS;
    @Mod.Instance
    private static Xannax INSTANCE;
    
    public Xannax() {
        Xannax.INSTANCE = this;
    }
    
    @Mod.EventHandler
    public void preinit(final FMLPreInitializationEvent event) {
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Xannax.mc = Minecraft.getMinecraft();
        (this.eventProcessor = new EventProcessor()).init();
        Xannax.fontRenderer = new CFontRenderer(new Font("Ariel", 0, 18), true, false);
        final TpsUtils tpsUtils = new TpsUtils();
        this.settingsManager = new SettingsManager();
        Xannax.log.info("Settings initialized!");
        this.friends = new Friends();
        Xannax.enemies = new Enemies();
        Xannax.log.info("Friends and enemies initialized!");
        this.moduleManager = new ModuleManager();
        Xannax.log.info("Modules initialized!");
        this.clickGUI = new ClickGUI();
        Xannax.log.info("ClickGUI initialized!");
        this.macroManager = new MacroManager();
        Xannax.log.info("Macros initialized!");
        this.saveConfiguration = new SaveConfiguration();
        Runtime.getRuntime().addShutdownHook(new Stopper());
        Xannax.log.info("Config Saved!");
        this.loadConfiguration = new LoadConfiguration();
        Xannax.log.info("Config Loaded!");
        this.saveModules = new SaveModules();
        Runtime.getRuntime().addShutdownHook(new Stopper());
        Xannax.log.info("Modules Saved!");
        this.loadModules = new LoadModules();
        Xannax.log.info("Modules Loaded!");
        CommandManager.initCommands();
        Xannax.log.info("Commands initialized!");
        Xannax.log.info("Initialization complete!\n");
    }
    
    @Mod.EventHandler
    public void postinit(final FMLPostInitializationEvent event) {
        Display.setTitle("XannaX 0.8.5");
        this.capeUtils = new CapeUtils();
        Xannax.log.info("Capes initialised!");
        Xannax.log.info("PostInitialization complete!\n");
    }
    
    public static Xannax getInstance() {
        return Xannax.INSTANCE;
    }
    
    static {
        log = LogManager.getLogger("XannaX");
        EVENT_BUS = new EventManager();
    }
}
