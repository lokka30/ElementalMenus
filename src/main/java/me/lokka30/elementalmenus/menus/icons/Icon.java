package me.lokka30.elementalmenus.menus.icons;

import me.lokka30.elementalmenus.menus.actions.Action;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @contributors none
 * @since v0.0
 */
public class Icon {

    final int[] slots;
    final String displayName;
    final Material material;
    int amount;
    List<String> lore;
    final HashMap<IconInteractionType, HashSet<Action>> actionMap;

    public Icon(int[] slots, String displayName, Material material, int amount, List<String> lore, HashMap<IconInteractionType, HashSet<Action>> actionMap) {
        this.slots = slots;
        this.displayName = displayName;
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.actionMap = actionMap;
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(material, amount);

        if (itemStack.hasItemMeta()) {
            if (!displayName.isEmpty()) //noinspection ConstantConditions
                itemStack.getItemMeta().setDisplayName(displayName);
            if (!lore.isEmpty()) //noinspection ConstantConditions
                itemStack.getItemMeta().setLore(lore);
        }

        return itemStack;
    }
}
