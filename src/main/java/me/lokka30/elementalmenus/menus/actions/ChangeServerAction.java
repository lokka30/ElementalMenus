package me.lokka30.elementalmenus.menus.actions;

import org.bukkit.entity.Player;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @contributors none
 * @since v0.0.0
 */
public class ChangeServerAction implements Action {

    String server;

    public ChangeServerAction(String server) {
        this.server = server;
    }

    @Override
    public void parse(Player player) {
        //TODO
    }
}