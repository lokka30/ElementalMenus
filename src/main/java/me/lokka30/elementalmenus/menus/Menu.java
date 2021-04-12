package me.lokka30.elementalmenus.menus;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.utils.Utils;
import me.lokka30.microlib.MessageUtils;
import me.lokka30.microlib.YamlConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashSet;

@SuppressWarnings("unused")
public class Menu {
    public final String name;
    public final YamlConfigFile config;

    public Menu(final String name, final YamlConfigFile config) {
        this.name = name;
        this.config = config;
    }

    public Icon fillerIcon;
    public HashSet<Icon> icons = new HashSet<>();

    private Inventory inventory;

    protected void load() {
        inventory = Bukkit.createInventory(null,
                Utils.bound(1, config.getConfig().getInt("menu.rows", 3), 6) * 9,
                MessageUtils.colorizeAll(config.getConfig().getString("menu.title", "Menu")));

        loadIcons();
    }

    public void open(Player player) {
        ElementalMenus.getInstance().menuManager.menusCurrentlyOpen.put(player.getUniqueId(), name);

        player.openInventory(inventory);

        //TODO open actions
    }

    public void registerClick(Player player, int slot) {
        //TODO
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
