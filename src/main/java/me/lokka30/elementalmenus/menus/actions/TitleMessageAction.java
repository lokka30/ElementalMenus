package me.lokka30.elementalmenus.menus.actions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TitleMessageAction implements Action {
    private final String title, subtitle;
    private final boolean broadcast;
    int fadeIn, stay, fadeOut;

    public TitleMessageAction(String title, String subtitle, int fadeIn, int stay, int fadeOut, boolean broadcast) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        this.broadcast = broadcast;
    }

    @Override
    public void act(Player player) {
        if (broadcast) {
            Bukkit.getOnlinePlayers().forEach(this::sendTitleMessage);
        } else {
            sendTitleMessage(player);
        }
    }

    private void sendTitleMessage(Player player) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
}