package me.lokka30.elementalmenus.commands.subcommands;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.commands.ElementalMenusCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @since v0.0.0
 */
public class ReloadSubcommand implements ElementalMenusCommand.Subcommand {

    @Override
    public void parseCommand(ElementalMenus main, CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("elementalmenus.command.reload")) {
            sender.sendMessage("You don't have access to that.");
            return;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /%label% reload"
                    .replace("%label%", label));
            return;
        }

        sender.sendMessage("*** Reload Started ***");

        sender.sendMessage("Reloading files...");
        main.fileManager.loadFiles();
        sender.sendMessage("Files reloaded.");

        sender.sendMessage("Reloading menus...");
        main.menuManager.loadMenus();
        sender.sendMessage("Menus reloaded.");

        sender.sendMessage("*** Reload Complete ***");
    }

    @Override
    public List<String> parseTabCompletions(ElementalMenus main, CommandSender sender, String label, String[] args) {
        return new ArrayList<>();
    }
}
