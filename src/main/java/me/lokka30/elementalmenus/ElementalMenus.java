package me.lokka30.elementalmenus;

import me.lokka30.elementalmenus.commands.ElementalMenusCommand;
import me.lokka30.elementalmenus.managers.*;
import me.lokka30.elementalmenus.menus.Menu;
import me.lokka30.elementalmenus.misc.Utils;
import me.lokka30.microlib.QuickTimer;
import me.lokka30.microlib.YamlConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;

/**
 * The main class of the plugin. Bukkit calls onEnable
 * when it loads the plugin, and onDisable when it
 * disables the plugin.
 *
 * @author lokka30
 * @contributors none
 * @since v0.0
 */
public class ElementalMenus extends JavaPlugin {

    /**
     * Credits.
     * <p>
     * Developers:   Those who regularly develop the resource.
     * Don't touch this.
     * Contributors: Those who have contributed code to the
     * resource.
     */
    public final HashSet<String> developers = new HashSet<>(Collections.singletonList("lokka30"));
    public final HashSet<String> contributors = new HashSet<>();

    /*
    Manager classes. These classes contain most of the
    functionality they are associated with. For example,
    the FileManager is used to load and check config files.
     */
    public final FileManager fileManager = new FileManager(this);
    public final MenuManager menuManager = new MenuManager(this);
    public final CompatibilityManager compatibilityManager = new CompatibilityManager(this);
    public final UpdateManager updateManager = new UpdateManager(this);
    public final CommandManager commandManager = new CommandManager(this);

    /*
    These are the configuration files that ElementalMenus provides.
    Server administrators may configure many aspects of this plugin,
    and ElementalMenus accesses such changes through these files.
     */
    public final YamlConfigFile settingsCfg = new YamlConfigFile(this, new File(getDataFolder(), "settings.yml"));
    public final YamlConfigFile advancedSettingsCfg = new YamlConfigFile(this, new File(getDataFolder(), "advancedSettings.yml"));
    public final YamlConfigFile messagesCfg = new YamlConfigFile(this, new File(getDataFolder(), "messages.yml"));
    public final HashSet<Menu> menus = new HashSet<>();

    /**
     * This method is called when Bukkit enables the plugin.
     */
    @Override
    public void onEnable() {
        QuickTimer timer = new QuickTimer();
        Utils.LOGGER.info("&fInitiating start-up procedure...");

        instance = this;

        fileManager.loadFiles();
        menuManager.loadMenus();
        commandManager.registerCommand("elementalmenus", new ElementalMenusCommand(this));
        compatibilityManager.checkCompatibility();
        updateManager.checkForUpdates();

        Utils.LOGGER.info("&fStart-up complete &8(&7took &b" + timer.getTimer() + "ms&8)");
    }

    /**
     * This method is called when Bukkit disables the plugin.
     */
    @Override
    public void onDisable() {
        QuickTimer timer = new QuickTimer();
        Utils.LOGGER.info("&fInitiating shut-down procedure...");

        menuManager.menusCurrentlyOpen.keySet().forEach(uuid -> menuManager.getMenu(menuManager.menusCurrentlyOpen.get(uuid))
                .processEvent(Menu.MenuCloseEventType.CLOSE_DISCONNECT, Bukkit.getPlayer(uuid)));

        Utils.LOGGER.info("&fShut-down complete &8(&7took &b" + timer.getTimer() + "ms&8)");
    }

    /*
    Use dependency injection wherever possible
    For small things such as Menu Icon Actions getting a value from
    the settings file, it's fine to use getInstance().
     */
    private static ElementalMenus instance;

    public static ElementalMenus getInstance() {
        return instance;
    }
}
