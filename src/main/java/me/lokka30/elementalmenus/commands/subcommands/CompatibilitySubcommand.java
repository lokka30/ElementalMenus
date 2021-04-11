package me.lokka30.elementalmenus.commands.subcommands;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.commands.ElementalMenusCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CompatibilitySubcommand implements ElementalMenusCommand.Subcommand {

    @Override
    public void parseCommand(ElementalMenus main, CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("elementalmenus.command.compatibility")) {
            sender.sendMessage("You don't have access to that.");
            return;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /" + label + " compatibility");
            return;
        }

        sender.sendMessage("Fetching compatibility scan results...");
        if (main.compatibilityManager.getIncompatibilities().isEmpty()) {
            sender.sendMessage("No possible incompatibilities were detected.");
        } else {
            main.compatibilityManager.getIncompatibilities().forEach(incompatibility -> sender.sendMessage("(" + incompatibility.getType() + "): " + incompatibility.getReason()));
        }
    }

    @Override
    public List<String> parseTabCompletions(ElementalMenus main, CommandSender sender, String label, String[] args) {
        return new ArrayList<>();
    }
}
