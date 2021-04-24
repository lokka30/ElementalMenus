package me.lokka30.elementalmenus;

import me.lokka30.elementalmenus.commands.ElementalMenusCommand;
import me.lokka30.elementalmenus.managers.*;
import me.lokka30.elementalmenus.menus.Menu;
import me.lokka30.elementalmenus.misc.Utils;
import me.lokka30.microlib.QuickTimer;
import me.lokka30.microlib.YamlConfigFile;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * The main class of the plugin. Bukkit calls onEnable
 * when it loads the plugin, and onDisable when it
 * disables the plugin.
 *
 * @author lokka30
 * @contributors none
 * @since v0.0.0
 */
public class ElementalMenus extends JavaPlugin implements PluginMessageListener {

    /**
     * Credits!
     * <p>
     * Developers:   Those who regularly develop the resource.
     * Don't touch this.
     * <p>
     * Contributors: Those who have contributed code to the
     * resource.
     * <p>
     * Feel free to add yourself to the contributors list if
     * you have changed code in the plugin.
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
     *
     * @author lokka30
     * @since v0.0.0
     */
    @Override
    public void onEnable() {
        QuickTimer timer = new QuickTimer();
        Utils.LOGGER.info("&fInitiating start-up procedure...");

        /*
        Do not move these two please.
         */
        instance = this;
        fileManager.loadFiles();

        compatibilityManager.checkCompatibility();
        setupEconomy();
        menuManager.loadMenus();
        commandManager.registerCommand("elementalmenus", new ElementalMenusCommand(this));
        updateManager.checkForUpdates();

        Utils.LOGGER.info("&fStart-up complete &8(&7took &b" + timer.getTimer() + "ms&8)");
    }

    /**
     * This method is called when Bukkit disables the plugin.
     *
     * @author lokka30
     * @since v0.0.0
     */
    @Override
    public void onDisable() {
        QuickTimer timer = new QuickTimer();
        Utils.LOGGER.info("&fInitiating shut-down procedure...");

        Set<UUID> set = menuManager.menusCurrentlyOpen.keySet(); // don't want to modify the map whilst iterating through it.
        set.forEach(uuid -> {
            if (Bukkit.getPlayer(uuid) == null) {
                menuManager.menusCurrentlyOpen.remove(uuid);
            } else {
                //noinspection ConstantConditions
                menuManager.getMenu(menuManager.menusCurrentlyOpen.get(uuid)).processEvent(Menu.MenuCloseEventType.CLOSE_DISCONNECT, Bukkit.getPlayer(uuid));
            }
        });

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

    /*
    Economy
     */
    public Economy economy;

    /**
     * TODO Describe...
     *
     * @author lokka30
     * @since v0.0.0
     */
    private void setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) return;

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            Utils.LOGGER.error("Unable to connect to Vault economy provider!");
        } else {
            economy = rsp.getProvider();
        }
    }

    /*
    Leave as empty.
     */
    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
    }
}
