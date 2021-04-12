package me.lokka30.elementalmenus.menus.actions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class StandardMessageAction implements Action {

    private final List<String> messages;
    private final boolean broadcast;

    public StandardMessageAction(List<String> messages, boolean broadcast) {
        this.messages = messages;
        this.broadcast = broadcast;
    }

    @Override
    public void act(Player player) {
        if (broadcast) {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> messages.forEach(onlinePlayer::sendMessage));
        } else {
            messages.forEach(player::sendMessage);
        }
    }
}