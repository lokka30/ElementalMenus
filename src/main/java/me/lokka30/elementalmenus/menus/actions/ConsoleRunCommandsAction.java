package me.lokka30.elementalmenus.menus.actions;

import me.lokka30.elementalmenus.ElementalMenus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class ConsoleRunCommandsAction implements Action {
    List<String> commands;

    public ConsoleRunCommandsAction(List<String> commands) {
        this.commands = commands;
    }

    @Override
    public void act(Player player) {
        final boolean prefixedWithSlash = ElementalMenus.getInstance().advancedSettingsCfg.getConfig().getBoolean("menus.commands-prefixed-with-slash", true);

        commands.forEach(command -> {
            if (prefixedWithSlash) command = command.replaceFirst("/", "");

            command = command
                    .replace("%username%", player.getName())
                    .replace("%displayname%", player.getDisplayName());

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }
}