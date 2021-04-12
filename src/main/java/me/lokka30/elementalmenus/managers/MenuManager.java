package me.lokka30.elementalmenus.managers;

import com.sun.istack.internal.Nullable;
import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.menus.Menu;
import org.bukkit.boss.BossBar;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class MenuManager implements Listener {

    private final ElementalMenus main;

    public MenuManager(final ElementalMenus main) {
        this.main = main;
    }

    public void loadMenus() {
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @Nullable
    public Menu getMenu(String menuName) {
        for (Menu menu : main.menus) {
            if (menu.name.equalsIgnoreCase(menuName)) {
                return menu;
            }
        }
        return null;
    }

    public final HashMap<UUID, BossBar> bossBarsToRemoveOnQuit = new HashMap<>(); //TODO
    public final HashMap<UUID, String> menusCurrentlyOpen = new HashMap<>(); //TODO
}
