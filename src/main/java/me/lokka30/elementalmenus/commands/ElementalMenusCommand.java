package me.lokka30.elementalmenus.commands;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
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
 * @since v0.0.0
 */
public class ElementalMenusCommand implements TabExecutor {

    private final ElementalMenus main;

    public ElementalMenusCommand(@NotNull final ElementalMenus main) {
        this.main = main;
    }

    /**
     * This interface is used to associate all
     * 'xyzSubcommand' classes together, using
     * the same methods as each other.
     *
     * @author lokka30
     * @since v0.0.0
     */
    @SuppressWarnings({"unused", "UnusedReturnValue"}) // IntelliJ bugs, these need to be suppressed.
    public interface Subcommand {

        /**
         * Run the subcommand.
         *
         * @param main   a link back to the main class
         * @param sender who ran the command
         * @param label  alias used to run the command
         * @param args   arguments specified, including the base command
         * @author lokka30
         * @since v0.0.0
         */
        void parseCommand(@NotNull ElementalMenus main, @NotNull CommandSender sender, @NotNull String label, @Nullable String[] args);

        /**
         * Retrieve a list of tab completion suggestions for the subcommand.
         *
         * @param main   a link back to the main class
         * @param sender who ran the command
         * @param label  alias used to run the command
         * @param args   arguments specified, including the base command
         * @return a list of suggestions, can be empty but non-null
         * @author lokka30
         * @since v0.0.0
         */
        @NotNull
        List<String> parseTabCompletions(@NotNull ElementalMenus main, @NotNull CommandSender sender, @NotNull String label, @Nullable String[] args);
    }

    final ReloadSubcommand reloadSubcommand = new ReloadSubcommand();
    final BackupSubcommand backupSubcommand = new BackupSubcommand();
    final CompatibilitySubcommand compatibilitySubcommand = new CompatibilitySubcommand();
    final AboutSubcommand aboutSubcommand = new AboutSubcommand();

    /**
     * Run the code concerning the operations of the entire command.
     * Forwards subcommands to their own separate classes.
     *
     * @param sender who ran the command
     * @param cmd    the command that was ran
     * @param label  alias used to run the command
     * @param args   arguments specified
     * @return if the command was able to run
     * @author lokka30
     * @since v0.0.0
     */
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

    /**
     * Run the code concerning the tab completions of the entire command.
     * Forwards subcommands to their own separate classes.
     *
     * @param sender who ran the command
     * @param cmd    the command that was ran
     * @param label  alias used to run the command
     * @param args   arguments specified
     * @return a list of tab completion suggestions for the user
     * @author lokka30
     * @since v0.0.0
     */
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

    /**
     * TODO Describe...
     *
     * @param sender who ran the command
     * @param label  alias used to run the command
     * @author lokka30
     * @since v0.0.0
     */
    void parseUsageList(@NotNull CommandSender sender, @NotNull String label) {
        Arrays.asList(
                "Available commands:",
                "- /" + label + " reload",
                "- /" + label + " backup",
                "- /" + label + " compatibility",
                "- /" + label + " about"
        ).forEach(sender::sendMessage);
    }

    /**
     * TODO Describe...
     *
     * @param sender who ran the command
     * @param label  alias used to run the command
     * @author lokka30
     * @since v0.0.0
     */
    void parseUsage(@NotNull CommandSender sender, @NotNull String label) {
        sender.sendMessage("Usage: /" + label + " <reload/backup/compatibility/about> ...");
    }
}
