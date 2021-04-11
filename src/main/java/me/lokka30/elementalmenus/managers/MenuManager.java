package me.lokka30.elementalmenus.managers;

import com.sun.istack.internal.Nullable;
import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.objects.Menu;
import org.bukkit.event.Listener;

public class MenuManager implements Listener {

    private final ElementalMenus main;
    public MenuManager(final ElementalMenus main) { this.main = main; }

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
