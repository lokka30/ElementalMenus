package me.lokka30.elementalmenus.menus.actions;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBarMessageAction implements Action {
    private final String message;
    private final boolean broadcast;

    public ActionBarMessageAction(String message, boolean broadcast) {
        this.message = message;
        this.broadcast = broadcast;
    }

    @Override
    public void act(Player player) {
        if (broadcast) {
            Bukkit.getOnlinePlayers().forEach(this::sendActionBarMessage);
        } else {
            sendActionBarMessage(player);
        }
    }

    private void sendActionBarMessage(Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}