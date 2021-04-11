package me.lokka30.elementalmenus.managers;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.objects.Menu;
import me.lokka30.elementalmenus.utils.Utils;
import me.lokka30.microlib.YamlConfigFile;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private final ElementalMenus main;
    public FileManager(final ElementalMenus main) { this.main = main; }

    public void loadFiles() {
        // Save and load 'settings.yml'
        try {
            main.settingsCfg.load();
        } catch(IOException ex) {
            Utils.LOGGER.error("Unable to load &bsettings.yml&7! Stack trace:");
            ex.printStackTrace();
        }

        // Save and load 'advancedSettings.yml'
        try {
            main.advancedSettingsCfg.load();
        } catch(IOException ex) {
            Utils.LOGGER.error("Unable to load &badvancedSettings.yml&7! Stack trace:");
            ex.printStackTrace();
        }

        // Save and load 'messages.yml'
        try {
            main.messagesCfg.load();
        } catch(IOException ex) {
            Utils.LOGGER.error("Unable to load &bmessages.yml&7! Stack trace:");
            ex.printStackTrace();
        }

        // Save 'menus' folder
        File menuFolder = new File(main.getDataFolder() + File.separator + "menus");
        if(!menuFolder.exists()) {
            Utils.LOGGER.info("'&bmenus&7' folder didn't exist. Saving folder '&bmenus&7'...");
            main.saveResource("menus", false);
            Utils.LOGGER.info("Folder '&bmenus&7' saved.");
        }

        // Load all menus
        String[] filesInDirectory = menuFolder.list();

        if(filesInDirectory != null) {
            for(String menuFileName : filesInDirectory) {
                if(menuFileName.endsWith(".yml") || menuFileName.endsWith(".yaml")) {
                    //Check the name of the file
                    final String[] menuNameParts = menuFileName.split("\\.");
                    if(menuFileName.contains(" ")) {
                        Utils.LOGGER.error("Unable to load menu '&b" + menuFileName + "&7', file name may not contain spaces &8(&7' '&8)&7.");
                        continue;
                    }
                    if(menuNameParts.length > 2) {
                        Utils.LOGGER.error("Unable to load menu '&b" + menuFileName + "&7', file name may not contain periods &8('&b.&7'&8)&7, with exception to the file extension.");
                        continue;
                    }

                    //File and YamlConfigFile of the menu
                    File menuFile = new File(main.getDataFolder() + File.separator + "menus" + File.separator + menuFileName);
                    YamlConfigFile menuCfg = new YamlConfigFile(main, menuFile);
                    final String menuName = menuNameParts[0];

                    // attempt to load it.
                    try {
                        menuCfg.load();
                    } catch(IOException ex) {
                        Utils.LOGGER.error("Unable to load menu '&b" + menuName + "&7'! Stack trace:");
                        ex.printStackTrace();
                        continue;
                    }

                    // if it's enabled, add it to the loaded menus list
                    if(menuCfg.getConfig().getBoolean("menu.enabled", false)) {
                        main.menus.add(new Menu(menuFileName, menuCfg));
                        Utils.LOGGER.info("Loaded menu '&b" + menuName + "&7'.");
                    } else {
                        Utils.LOGGER.info("Skipped loading of menu '&b" + menuName + "&7' as it is disabled at path '&bmenus.enabled&7'.");
                    }
                } else {
                    Utils.LOGGER.warning("File '&b" + menuFileName + "&7' found in the '&bmenus&7' folder, but it isn't a valid menu - skipping.");
                }
            }
        }

        Utils.LOGGER.info("Loaded a total of &b" + main.menus.size() + "&7 menus.");
    }


    public void backupFiles() {
        //TODO
    }
}
