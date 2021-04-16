package me.lokka30.elementalmenus.menus;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.menus.icons.Icon;
import me.lokka30.elementalmenus.menus.icons.IconInteractionType;
import me.lokka30.elementalmenus.misc.Utils;
import me.lokka30.microlib.MessageUtils;
import me.lokka30.microlib.YamlConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashSet;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @contributors none
 * @since v0.0
 */
public class Menu {
    public final String name;
    public final YamlConfigFile config;

    public Menu(final String name, final YamlConfigFile config) {
        this.name = name;
        this.config = config;
    }

    public Icon fillerIcon;
    public final HashSet<Icon> icons = new HashSet<>();

    private Inventory inventory;

    protected void load() {
        inventory = Bukkit.createInventory(null,
                Utils.bound(1, config.getConfig().getInt("menu.rows", 3), 6) * 9,
                MessageUtils.colorizeAll(config.getConfig().getString("menu.title", "Menu")));

        loadIcons();
    }

    public void open(final Player player) {
        ElementalMenus.getInstance().menuManager.menusCurrentlyOpen.put(player.getUniqueId(), name);

        player.openInventory(inventory);

        //TODO open actions
    }

    public void processInteraction(final Player player, final int slot, final IconInteractionType type) {
        //TODO
    }

    public enum MenuCloseEventType {
        /**
         * When the player closes the
         * inventory by normal means,
         * or if a plugin or the server
         * force-closes the inventory.
         */
        CLOSE_INVENTORY,

        /**
         * When the user disconnects
         * whilst the menu is open.
         * Limited actions are applicable
         * to players concerning this
         * menu event.
         */
        CLOSE_DISCONNECT
    }

    public void processEvent(final MenuCloseEventType menuCloseEventType, final Player player) {
        switch (menuCloseEventType) {
            case CLOSE_DISCONNECT:
                ElementalMenus.getInstance().menuManager.menusCurrentlyOpen.remove(player.getUniqueId());
                //TODO Actions
                break;
            case CLOSE_INVENTORY:
                ElementalMenus.getInstance().menuManager.menusCurrentlyOpen.remove(player.getUniqueId());
                //TODO Actions
                break;
            default:
                throw new IllegalStateException(menuCloseEventType + " is an unexpected MenuCloseEventType");
        }
    }

    public boolean isOpen(Player player) {
        return ElementalMenus.getInstance().menuManager.menusCurrentlyOpen.getOrDefault(player.getUniqueId(), "impossible.menu.name ;)").equals(name);
    }

    private void loadIcons() {
        /*
        Load filler icon
         */
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, fillerIcon.getItemStack());
        }

        /*
        Load other icons
         */
        //TODO
    }
}
