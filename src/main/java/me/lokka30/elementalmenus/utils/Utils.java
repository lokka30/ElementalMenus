package me.lokka30.elementalmenus.utils;

import me.lokka30.microlib.MicroLogger;

public class Utils {
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
}
