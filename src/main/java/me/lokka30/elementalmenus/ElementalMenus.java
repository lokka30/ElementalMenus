package me.lokka30.elementalmenus;

import me.lokka30.elementalmenus.managers.CompatibilityManager;
import me.lokka30.elementalmenus.managers.FileManager;
import me.lokka30.elementalmenus.managers.MenuManager;
import me.lokka30.elementalmenus.managers.UpdateManager;
import me.lokka30.elementalmenus.utils.Utils;
import me.lokka30.microlib.QuickTimer;
import me.lokka30.microlib.YamlConfigFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public class ElementalMenus extends JavaPlugin {

    /*
    Manager classes. These classes contain most of the
    functionality they are associated with. For example,
    the FileManager is used to load and check config files.
     */
    public final FileManager fileManager = new FileManager(this);
    public final MenuManager menuManager = new MenuManager(this);
    public final CompatibilityManager compatibilityManager = new CompatibilityManager(this);
    public final UpdateManager updateManager = new UpdateManager(this);

    /*
    These are the configuration files that ElementalMenus provides.
    Server administrators may configure many aspects of this plugin,
    and ElementalMenus accesses such changes through these files.
     */
    public final YamlConfigFile settingsCfg = new YamlConfigFile(this, new File(getDataFolder(), "settings.yml"));
    public final YamlConfigFile advancedSettingsCfg = new YamlConfigFile(this, new File(getDataFolder(), "advancedSettings.yml"));
    public final YamlConfigFile messagesCfg = new YamlConfigFile(this, new File(getDataFolder(), "messages.yml"));
    public final HashMap<String, YamlConfigFile> menus = new HashMap<>();

    /**
     * This method is called when Bukkit enables the plugin.
     */
    @Override
    public void onEnable() {
        QuickTimer timer = new QuickTimer();
        Utils.LOGGER.info("&fInitiating start-up procedure...");

        fileManager.loadFiles();
        menuManager.loadMenus();
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

        //TODO

        Utils.LOGGER.info("&fShut-down complete &8(&7took &b" + timer.getTimer() + "ms&8)");
    }
}