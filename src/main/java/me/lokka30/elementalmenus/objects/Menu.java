package me.lokka30.elementalmenus.objects;

import me.lokka30.microlib.YamlConfigFile;
import org.bukkit.entity.Player;

public class Menu {
    public final String name;
    public final YamlConfigFile config;

    public Menu(final String name, final YamlConfigFile config) {
        this.name = name;
        this.config = config;
    }

    public void open(Player player) {
        //TODO
    }

    public void registerClick(Player player, int slot) {
        //TODO
    }

    public boolean isOpen(Player player) {
        //TODO
        return false;
    }

    public void close(Player player) {
        //TODO
    }
}
