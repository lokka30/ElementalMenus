package me.lokka30.elementalmenus.misc;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.microlib.MicroLogger;

public class Utils {

    /**
     * The plugin's logging system (send messages to console)
     */
    public static final MicroLogger LOGGER = new MicroLogger("&b&lElementalMenus: &7");

    /**
     * Ensure 'current' is between 'min' and 'max'
     *
     * @param min     min value
     * @param current current value
     * @param max     max value
     * @return current value, at lowest 'min' and at most 'max'.
     */
    public static int bound(int min, int current, int max) {
        if (current > max) {
            return max;
        }
        return Math.max(current, min);
    }

    //TODO Check if this is required.
    public static Object getDefaultIfNull(Object obj1, Object obj2) {
        if (obj1 == null) {
            return obj2;
        } else {
            return obj1;
        }
    }

    public static void sendDebugLog(final DebugCategory category, final String msg) {
        if (ElementalMenus.getInstance().advancedSettingsCfg.getConfig().getStringList("debug-categories").contains(category.toString())) {
            Utils.LOGGER.info("&8(&bDEBUG &8- &f" + category + "&8): &7" + msg);
        }
    }
}
