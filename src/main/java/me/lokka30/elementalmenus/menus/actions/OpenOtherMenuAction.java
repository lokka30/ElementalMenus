package me.lokka30.elementalmenus.menus.actions;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class OpenOtherMenuAction implements Action {

    Menu otherMenu;

    public OpenOtherMenuAction(Menu otherMenu) {
        this.otherMenu = otherMenu;
    }

    @Override
    public void parse(Player player) {
        player.closeInventory();

        // 1 tick delay to avoid issues
        new BukkitRunnable() {
            @Override
            public void run() {
                otherMenu.open(player);
            }
        }.runTaskLater(ElementalMenus.getInstance(), 1);
    }
}