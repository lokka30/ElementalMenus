package me.lokka30.elementalmenus.menus.icons;

import org.bukkit.Material;

import java.util.HashSet;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @contributors none
 * @since v0.0
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

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public String getDisplayName() {
        return displayName;
    }

    public HashSet<String> getLore() {
        return lore;
    }
}
