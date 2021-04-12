package me.lokka30.elementalmenus.menus.actions;

import org.bukkit.entity.Player;

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