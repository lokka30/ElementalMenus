package me.lokka30.elementalmenus.managers;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.microlib.YamlConfigFile;
import org.bukkit.event.Listener;

public class MenuManager implements Listener {

    private final ElementalMenus main;
    public MenuManager(final ElementalMenus main) { this.main = main; }

    //todo use HashSet<Menu> instead of HashMap in main class.
    public static class Menu {
        private String fileName;
        private String fileExtension;
        public YamlConfigFile menuConfig;

        public Menu(final String fileName, final String fileExtension, YamlConfigFile menuConfig) {
            this.fileName = fileName; this.fileExtension = fileExtension; this.menuConfig = menuConfig;
        }

        public String getFileName() { return fileName; }
        public String getFileExtension() { return fileExtension; }
    }

    public void loadMenus() {
        main.getServer().getPluginManager().registerEvents(this, main);
    }
}
