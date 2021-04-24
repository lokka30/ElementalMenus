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
 * @since v0.0.0
 */
public class CompatibilityManager {

    private final ElementalMenus main;

    public CompatibilityManager(final ElementalMenus main) {
        this.main = main;
    }

    /**
     * A list of incompatibilities that were found from the latest scan.
     */
    private final HashSet<Incompatibility> incompatibilities = new HashSet<>();

    /**
     * Checks over things such as the server's version to notify
     * any administrators if the plugin may be incompatible with
     * their server configuration
     *
     * @author lokka30
     * @since v0.0.0
     */
    public void checkCompatibility() {
        Utils.LOGGER.info("Checking compatibility...");

        checkServerVersion();
        checkServerSoftware();

        if (incompatibilities.size() == 0) {
            Utils.LOGGER.info("Compatibility checks completed, no possible incompatibilities found.");
        } else {
            Utils.LOGGER.warning("Compatibility checks completed, &b" + incompatibilities.size() + "&7 possible incompatibilities were found:");
            incompatibilities.forEach(incompatibility -> Utils.LOGGER.warning("&8 - &b" + incompatibility.getType().toString() + "&7 check flagged: " + incompatibility.getReason()));
        }
    }

    /**
     * Incompatibility object used to store information
     * about an incompatibility that was detected
     *
     * @author lokka30
     * @since v0.0.0
     */
    public static class Incompatibility {

        /**
         * The type of incompatibility detected
         *
         * @author lokka30
         * @since v0.0.0
         */
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

        /**
         * Get the Type of incompatibility it is
         *
         * @return type
         * @author lokka30
         * @since v0.0.0
         */
        public Type getType() {
            return type;
        }

        /**
         * Get the reason that the incompatibility was triggered
         *
         * @return sentence as to why the reason was triggered
         * @author lokka30
         * @since v0.0.0
         */
        public String getReason() {
            return reason;
        }
    }

    /**
     * Check if an incompatibility is not suppressed
     * on the advanced settings file.
     *
     * @param incompatibilityType type of incompatibility to check
     * @return if it is not suppressed by configuration
     * @author lokka30
     * @since v0.0.0
     */
    public boolean isNotSuppressed(final Incompatibility.Type incompatibilityType) {
        return !main.advancedSettingsCfg.getConfig().getBoolean("compatibility-manager.suppressions." + incompatibilityType.toString());
    }

    /**
     * TODO Describe ...
     *
     * @param event PlayerJoinEvent
     * @author lokka30
     * @since v0.0.0
     */
    public void parseJoin(final PlayerJoinEvent event) {
        //TODO print incompatibilities to admins.
        // incompatibilities.size() incompatibilities were found
        if (incompatibilities.isEmpty()) return;

        final Player player = event.getPlayer();

        if (!player.hasPermission("elementalmenu.receive-compatibility-reports")) return;

        player.sendMessage("The compatibility checker found " + incompatibilities.size() + " possible incompatibilities with your server configuration.");
        player.sendMessage("Please run '/menu compatibility' to view these possible incompatibilities.");
    }

    /**
     * TODO Describe ...
     *
     * @author lokka30
     * @since v0.0.0
     */
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
     *
     * @author lokka30
     * @since v0.0.0
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

    /**
     * TODO Describe...
     *
     * @return incompatibilities detected
     * @author lokka30
     * @since v0.0.0
     */
    public HashSet<Incompatibility> getIncompatibilities() {
        return incompatibilities;
    }
}
