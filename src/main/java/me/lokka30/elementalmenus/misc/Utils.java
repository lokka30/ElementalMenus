package me.lokka30.elementalmenus.misc;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.microlib.MicroLogger;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @contributors none
 * @since v0.0
 */
public class Utils {

    /**
     * The plugin's logging system (send messages to console)
     */
    public static final MicroLogger LOGGER = new MicroLogger("&b&lElementalMenus: &7");

    /**
     * Ensures that 'current' is between 'min' and 'max'
     *
     * @param min     min value
     * @param current current value
     * @param max     max value
     * @return current value, at lowest 'min' and at most 'max'.
     * @author lokka30
     * @contributors none
     * @since v0.0
     */
    public static int bound(int min, int current, int max) {
        if (current > max) {
            return max;
        }
        return Math.max(current, min);
    }

    /**
     * Send a debug message to console, if enabled in
     * the 'advancedSettings' configuration.
     *
     * @param category category of the debug message, allowing
     *                 server owners to only listen to certain
     *                 categories of debug logs, making the
     *                 plugin easier to debug.
     * @param msg      the message to send pertaining to the
     *                 debug category, e.g., 'Notch opened a
     *                 menu'.
     * @author lokka30
     * @contributors none
     * @since v0.0
     */
    public static void sendDebugLog(final DebugCategory category, final String msg) {
        if (ElementalMenus.getInstance().advancedSettingsCfg.getConfig().getStringList("debug.categories").contains(category.toString())) {
            Utils.LOGGER.info("&8(&bDEBUG &8- &f" + category + "&8): &7" + msg);
        }
    }

    public static Object warnAndReturn(final Object object, final String warning) {
        Utils.LOGGER.warning(warning);
        return object;
    }

    /**
     * Return 'def' object if 'object' is null, otherwise return 'object'
     *
     * @param object object to check if null
     * @param def    def object to rely on
     * @return def if 'object' is null, otherwise return 'object'
     */
    public static Object getDefaultIfNull(final Object object, final Object def) {
        return object == null ? def : object;
    }
}
