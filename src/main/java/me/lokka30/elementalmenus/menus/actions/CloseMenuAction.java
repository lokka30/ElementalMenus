package me.lokka30.elementalmenus.menus.actions;

import org.bukkit.entity.Player;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @contributors none
 * @since v0.0
 */
public class CloseMenuAction implements Action {

    boolean closeMenu;

    public CloseMenuAction(boolean closeMenu) {
        this.closeMenu = closeMenu;
    }

    @Override
    public void parse(Player player) {
        if (closeMenu) player.closeInventory();
    }
}