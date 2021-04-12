package me.lokka30.elementalmenus.menus.actions;

import org.bukkit.entity.Player;

public class ChangeServerAction implements Action {

    String server;

    public ChangeServerAction(String server) {
        this.server = server;
    }

    @Override
    public void act(Player player) {
        //TODO
    }
}