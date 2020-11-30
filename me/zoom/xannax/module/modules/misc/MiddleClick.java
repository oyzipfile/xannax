// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import java.util.function.Predicate;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.util.friend.Friends;
import org.lwjgl.input.Mouse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.module.Module;

public class MiddleClick extends Module
{
    @EventHandler
    private final Listener<InputEvent.MouseInputEvent> listener;
    
    public MiddleClick() {
        super("MiddleClick", "Ez add friends", Category.Misc);
        this.listener = new Listener<InputEvent.MouseInputEvent>(event -> {
            if (MiddleClick.mc.objectMouseOver.typeOfHit.equals((Object)RayTraceResult.Type.ENTITY) && MiddleClick.mc.objectMouseOver.entityHit instanceof EntityPlayer && Mouse.getEventButton() == 2) {
                if (Friends.isFriend(MiddleClick.mc.objectMouseOver.entityHit.getName())) {
                    Xannax.getInstance().friends.delFriend(MiddleClick.mc.objectMouseOver.entityHit.getName());
                    Command.sendClientMessage(ChatFormatting.RED + "Removed " + MiddleClick.mc.objectMouseOver.entityHit.getName() + " from friends list");
                }
                else {
                    Xannax.getInstance().friends.addFriend(MiddleClick.mc.objectMouseOver.entityHit.getName());
                    Command.sendClientMessage(ChatFormatting.GREEN + "Added " + MiddleClick.mc.objectMouseOver.entityHit.getName() + " to friends list");
                }
            }
        }, (Predicate<InputEvent.MouseInputEvent>[])new Predicate[0]);
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}
