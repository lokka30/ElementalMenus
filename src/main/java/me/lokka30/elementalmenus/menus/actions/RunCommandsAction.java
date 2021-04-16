package me.lokka30.elementalmenus.menus.actions;

import me.lokka30.elementalmenus.ElementalMenus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @contributors none
 * @since v0.0
 */
public class RunCommandsAction implements Action {

    List<String> commands;
    boolean ranByConsole;

    public RunCommandsAction(List<String> commands, boolean ranByConsole) {
        this.commands = commands;
        this.ranByConsole = ranByConsole;
    }

    @Override
    public void parse(Player player) {
        final boolean prefixedWithSlash = ElementalMenus.getInstance().advancedSettingsCfg.getConfig().getBoolean("menus.commands-prefixed-with-slash", true);

        commands.forEach(command -> {
            if (prefixedWithSlash) command = command.replaceFirst("/", "");

            command = command
                    .replace("%username%", player.getName())
                    .replace("%displayname%", player.getDisplayName());

            if (ranByConsole) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            } else {
                Bukkit.dispatchCommand(player, command);
            }
        });
    }
}