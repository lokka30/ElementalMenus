package me.lokka30.elementalmenus.commands;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.commands.subcommands.AboutSubcommand;
import me.lokka30.elementalmenus.commands.subcommands.BackupSubcommand;
import me.lokka30.elementalmenus.commands.subcommands.CompatibilitySubcommand;
import me.lokka30.elementalmenus.commands.subcommands.ReloadSubcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @contributors none
 * @since v0.0.0
 */
public class ElementalMenusCommand implements TabExecutor {

    private final ElementalMenus main;

    public ElementalMenusCommand(final ElementalMenus main) {
        this.main = main;
    }

    @SuppressWarnings({"unused", "UnusedReturnValue"}) // IntelliJ bugs, these need to be suppressed.
    public interface Subcommand {
        void parseCommand(ElementalMenus main, CommandSender sender, String label, String[] args);

        List<String> parseTabCompletions(ElementalMenus main, CommandSender sender, String label, String[] args);
    }

    ReloadSubcommand reloadSubcommand = new ReloadSubcommand();
    BackupSubcommand backupSubcommand = new BackupSubcommand();
    CompatibilitySubcommand compatibilitySubcommand = new CompatibilitySubcommand();
    AboutSubcommand aboutSubcommand = new AboutSubcommand();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            parseUsageList(sender, label);
        } else {
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "reload":
                    reloadSubcommand.parseCommand(main, sender, label, args);
                    break;
                case "backup":
                    backupSubcommand.parseCommand(main, sender, label, args);
                    break;
                case "compatibility":
                    compatibilitySubcommand.parseCommand(main, sender, label, args);
                    break;
                case "info":
                    aboutSubcommand.parseCommand(main, sender, label, args);
                    break;
                default:
                    parseUsage(sender, label);
                    break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            return Arrays.asList("reload", "backup", "compatibility", "info");
        } else {
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "reload":
                    reloadSubcommand.parseTabCompletions(main, sender, label, args);
                    break;
                case "backup":
                    backupSubcommand.parseTabCompletions(main, sender, label, args);
                    break;
                case "compatibility":
                    compatibilitySubcommand.parseTabCompletions(main, sender, label, args);
                    break;
                case "about":
                    aboutSubcommand.parseTabCompletions(main, sender, label, args);
                    break;
                default:
                    break;
            }
        }

        return new ArrayList<>();
    }

    void parseUsageList(CommandSender sender, String label) {
        Arrays.asList(
                "Available commands:",
                "- /" + label + " reload",
                "- /" + label + " backup",
                "- /" + label + " compatibility",
                "- /" + label + " about"
        ).forEach(sender::sendMessage);
    }

    void parseUsage(CommandSender sender, String label) {
        sender.sendMessage("Usage: /" + label + " <reload/backup/compatibility/about> ...");
    }
}
