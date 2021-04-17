package me.lokka30.elementalmenus.managers;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.misc.Utils;
import me.lokka30.microlib.VersionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @contributors none
 * @since v0.0.0
 */
public class CompatibilityManager {

    private final ElementalMenus main;

    public CompatibilityManager(final ElementalMenus main) { this.main = main; }

    private final HashSet<Incompatibility> incompatibilities = new HashSet<>();

    /**
     * Checks over things such as the server's version to notify
     * any administrators if the plugin may be incompatible with
     * their server configuration
     */
    public void checkCompatibility() {
        Utils.LOGGER.info("Checking compatibility...");

        checkServerVersion();
        checkServerSoftware();

        if(incompatibilities.size() == 0) {
            Utils.LOGGER.info("Compatibility checks completed, no possible incompatibilities found.");
        } else {
            Utils.LOGGER.warning("Compatibility checks completed, &b" + incompatibilities.size() + "&7 possible incompatibilities were found:");
            incompatibilities.forEach(incompatibility -> Utils.LOGGER.warning("&8 - &b" + incompatibility.getType().toString() + "&7 check flagged: " + incompatibility.getReason()));
        }
    }

    public static class Incompatibility {
        enum Type {
            SERVER_VERSION,
            SERVER_SOFTWARE
        }

        private final Type type;
        private final String reason;

        public Incompatibility(final Type type, final String reason) {
            this.type = type;
            this.reason = reason;
        }

        public Type getType() { return type; }

        public String getReason() { return reason; }
    }

    public boolean isNotSuppressed(final Incompatibility.Type incompatibilityType) {
        return !main.advancedSettingsCfg.getConfig().getBoolean("compatibility-manager.suppressions." + incompatibilityType.toString());
    }

    public void parseJoin(final PlayerJoinEvent event) {
        //TODO print incompatibilities to admins.
        // incompatibilities.size() incompatibilities were found
        if (incompatibilities.isEmpty()) return;

        final Player player = event.getPlayer();

        if (!player.hasPermission("elementalmenu.receive-compatibility-reports")) return;

        player.sendMessage("The compatibility checker found " + incompatibilities.size() + " possible incompatibilities with your server configuration.");
        player.sendMessage("Please run '/menu compatibility' to view these possible incompatibilities.");
    }

    private void checkServerVersion() {
        if (isNotSuppressed(Incompatibility.Type.SERVER_VERSION)) {
            if (!VersionUtils.isOneThirteen()) {
                incompatibilities.add(new Incompatibility(Incompatibility.Type.SERVER_VERSION, "Oldest supported Minecraft server version of the plugin is &b1.13&7."));
            }
        }
    }

    /**
     * The server should be running a Spigot (or derivative)
     * as ElementalMenus utilises Spigot code, such as sending
     * action bar messages to players.
     */
    private void checkServerSoftware() {
        try {
            Class.forName("org.bukkit.entity.Player.Spigot");
        } catch (ClassNotFoundException e) {
            if (isNotSuppressed(Incompatibility.Type.SERVER_SOFTWARE)) {
                incompatibilities.add(new Incompatibility(Incompatibility.Type.SERVER_SOFTWARE, "ElenmentalMenus requires the &bSpigotMC&7 software to be used (or a derivative such as PaperMC) to provide full functionality. Server software such as CraftBukkit is unsupported by the ElementalMenus team."));
            }
        }
    }

    public HashSet<Incompatibility> getIncompatibilities() {
        return incompatibilities;
    }
}
