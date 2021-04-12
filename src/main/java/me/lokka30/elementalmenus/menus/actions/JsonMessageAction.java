package me.lokka30.elementalmenus.menus.actions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class JsonMessageAction implements Action {

    private final List<String> messages;
    private final boolean broadcast;

    public JsonMessageAction(List<String> messages, boolean broadcast) {
        this.messages = messages;
        this.broadcast = broadcast;
    }

    @Override
    public void act(Player player) {
        if (broadcast) {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> messages.forEach(message -> sendJsonMessage(onlinePlayer, message)));
        } else {
            messages.forEach(message -> sendJsonMessage(player, message));
        }
    }

    private void sendJsonMessage(Player player, String message) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + message);
    }
}