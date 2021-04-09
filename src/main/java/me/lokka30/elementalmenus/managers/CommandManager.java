package me.lokka30.elementalmenus.managers;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.utils.Utils;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

public class CommandManager {

    private final ElementalMenus main;
    public CommandManager(final ElementalMenus main) { this.main = main; }

    public void registerCommand(String command, TabExecutor clazz) {
        final PluginCommand pluginCommand = main.getCommand(command);

        if(pluginCommand == null) {
            Utils.LOGGER.error("Unable to register command '&b" + command + "&7', PluginCommand is null");
        } else {
            Utils.LOGGER.info("Registering command '&b/" + command + "&7'...");
            pluginCommand.setExecutor(clazz);
        }
    }
}