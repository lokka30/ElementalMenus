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
 * @contributors none
 * @since v0.0
 */
public class BackupSubcommand implements ElementalMenusCommand.Subcommand {

    @Override
    public void parseCommand(ElementalMenus main, CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("elementalmenus.command.backup")) {
            sender.sendMessage("You don't have access to that.");
            return;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /" + label + " backup");
            return;
        }

        sender.sendMessage("Backing up ElementalMenus directory...");
        main.fileManager.backupFiles();
        sender.sendMessage("Backup complete.");
    }

    @Override
    public List<String> parseTabCompletions(ElementalMenus main, CommandSender sender, String label, String[] args) {
        return new ArrayList<>();
    }
}
