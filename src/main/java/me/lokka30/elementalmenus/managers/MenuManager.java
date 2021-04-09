package me.lokka30.elementalmenus.managers;

import com.sun.istack.internal.Nullable;
import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.microlib.YamlConfigFile;
import org.bukkit.event.Listener;

public class MenuManager implements Listener {

    private final ElementalMenus main;
    public MenuManager(final ElementalMenus main) { this.main = main; }

    public static class Menu {
        public final String name;
        public final YamlConfigFile config;

        public Menu(final String name, final YamlConfigFile config) { this.name = name; this.config = config; }
    }

    public void loadMenus() {
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @Nullable
    public Menu getMenu(String menuName) {
        for(Menu menu : main.menus) {
            if(menu.name.equalsIgnoreCase(menuName)) {
                return menu;
            }
        }
        return null;
    }
}
