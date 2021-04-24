package me.lokka30.elementalmenus.managers;

import com.sun.istack.internal.Nullable;
import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.menus.Menu;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @since v0.0.0
 */
public class MenuManager implements Listener {

    private final ElementalMenus main;

    public MenuManager(final ElementalMenus main) {
        this.main = main;
    }

    /**
     * TODO Describe...
     *
     * @author lokka30
     * @since v0.0.0
     */
    public void loadMenus() {
        main.getServer().getPluginManager().registerEvents(this, main);
        main.menus.forEach(Menu::load);
    }

    /**
     * TODO Describe...
     *
     * @param menuName name of the menu to get
     * @return menu object (null if not found)
     * @author lokka30
     * @since v0.0.0
     */
    @Nullable
    public Menu getMenu(final String menuName) {
        for (Menu menu : main.menus) {
            if (menu.name.equalsIgnoreCase(menuName)) {
                return menu;
            }
        }
        throw new NullPointerException("No menu named " + menuName + " is loaded.");
    }

    /**
     * Need to ensure that the plugin removes
     * any boss bars that it shows to players
     * when they leave the server. Otherwise
     * they will stick and the plugin will
     * assume the boss bar is already gone.
     */
    public final HashMap<UUID, BossBar> bossBarsToRemoveOnQuit = new HashMap<>();

    /**
     * A map containing players who have
     * menus currently open. The value
     * (String) is the name of the menu
     * that the player has open.
     */
    public final HashMap<UUID, String> menusCurrentlyOpen = new HashMap<>();

    /**
     * TODO Describe...
     *
     * @param event TODO Describe...
     * @author lokka30
     * @since v0.0.0
     */
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;

        final Player player = (Player) event.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (menusCurrentlyOpen.containsKey(uuid)) {
            getMenu(menusCurrentlyOpen.get(uuid)).processEvent(Menu.MenuCloseEventType.CLOSE_INVENTORY, player);
        }
    }

    /**
     * This event handler ensures that if the player opens
     * another inventory whilst they already have a menu
     * open, the plugin will process the close event actions
     * for the player.
     *
     * @param event InventoryOpenEvent
     * @author lokka30
     * @since v0.0.0
     */
    @EventHandler
    public void onInventoryOpen(final InventoryOpenEvent event) {
        // IntelliJ says it's impossible for event.getPlayer() to not be a Player. coolio
        final Player player = (Player) event.getPlayer();

        if (!menusCurrentlyOpen.containsKey(player.getUniqueId())) return;

        final Menu menu = getMenu(menusCurrentlyOpen.get(player.getUniqueId()));

        if (menu.inventory == event.getInventory()) return;

        menu.processEvent(Menu.MenuCloseEventType.CLOSE_INVENTORY, player);
    }

    /**
     * TODO Describe...
     *
     * @param event PlayerQuitEvent
     * @author lokka30
     * @since v0.0.0
     */
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
