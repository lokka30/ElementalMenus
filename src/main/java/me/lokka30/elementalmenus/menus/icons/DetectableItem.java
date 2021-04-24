package me.lokka30.elementalmenus.menus.icons;

import org.bukkit.Material;

import java.util.HashSet;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @since v0.0.0
 */
public class DetectableItem {

    private final Material material;
    private final int amount;
    private final String displayName;
    private final HashSet<String> lore;

    public DetectableItem(Material material, int amount, String displayName, HashSet<String> lore) {
        this.material = material;
        this.amount = amount;
        this.displayName = displayName;
        this.lore = lore;
    }

    /**
     * TODO Describe...
     *
     * @author lokka30
     * @since v0.0.0
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * TODO Describe...
     *
     * @author lokka30
     * @since v0.0.0
     */
    public int getAmount() {
        return amount;
    }

    /**
     * TODO Describe...
     *
     * @author lokka30
     * @since v0.0.0
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * TODO Describe...
     *
     * @author lokka30
     * @since v0.0.0
     */
    public HashSet<String> getLore() {
        return lore;
    }
}
