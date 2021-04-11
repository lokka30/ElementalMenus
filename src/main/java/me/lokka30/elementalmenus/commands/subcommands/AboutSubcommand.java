package me.lokka30.elementalmenus.commands.subcommands;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.commands.ElementalMenusCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class AboutSubcommand implements ElementalMenusCommand.Subcommand {

    @Override
    public void parseCommand(ElementalMenus main, CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("elementalmenus.command.about")) {
            sender.sendMessage("You don't have access to that.");
            return;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /" + label + " about");
            return;
        }

        sender.sendMessage("This server is running ElementalMenus v" + main.getDescription().getVersion() + ".");
        sender.sendMessage("Developers: " + String.join(", ", main.developers));
        sender.sendMessage("Contributors: " + String.join(", ", main.contributors));
    }

    @Override
    public List<String> parseTabCompletions(ElementalMenus main, CommandSender sender, String label, String[] args) {
        return new ArrayList<>();
    }
}
