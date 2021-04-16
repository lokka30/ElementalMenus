package me.lokka30.elementalmenus.managers;

import com.sun.istack.internal.Nullable;
import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.menus.Menu;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @contributors none
 * @since v0.0
 */
public class MenuManager implements Listener {

    private final ElementalMenus main;

    public MenuManager(final ElementalMenus main) {
        this.main = main;
    }

    public void loadMenus() {
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @Nullable
    public Menu getMenu(final String menuName) {
        for (Menu menu : main.menus) {
            if (menu.name.equalsIgnoreCase(menuName)) {
                return menu;
            }
        }
        throw new NullPointerException("No menu named " + menuName + " is loaded.");
    }

    public final HashMap<UUID, BossBar> bossBarsToRemoveOnQuit = new HashMap<>();
    public final HashMap<UUID, String> menusCurrentlyOpen = new HashMap<>();

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;

        final Player player = (Player) event.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (menusCurrentlyOpen.containsKey(uuid)) {
            getMenu(menusCurrentlyOpen.get(uuid)).processEvent(Menu.MenuCloseEventType.CLOSE_INVENTORY, player);
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        /*
        Process menus open
         */
        if (menusCurrentlyOpen.containsKey(uuid)) {
            getMenu(menusCurrentlyOpen.get(uuid)).processEvent(Menu.MenuCloseEventType.CLOSE_DISCONNECT, player);
        }

        /*
        Process boss bars
         */
        if (bossBarsToRemoveOnQuit.containsKey(uuid)) {
            bossBarsToRemoveOnQuit.get(uuid).removePlayer(player);
        }
    }
}
