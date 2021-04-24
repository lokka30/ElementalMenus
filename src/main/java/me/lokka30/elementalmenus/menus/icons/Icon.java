package me.lokka30.elementalmenus.menus.icons;

import me.lokka30.elementalmenus.menus.actions.Action;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @since v0.0.0
 */
public class Icon {

    final HashSet<Integer> slots;
    final ItemStack itemStack;
    final HashMap<IconInteractionType, HashSet<Action>> actionMap;

    public Icon(HashSet<Integer> slots, ItemStack itemStack, HashMap<IconInteractionType, HashSet<Action>> actionMap) {
        this.slots = slots;
        this.itemStack = itemStack;
        this.actionMap = actionMap;
    }

    /**
     * TODO Describe...
     *
     * @author lokka30
     * @since v0.0.0
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * TODO Describe...
     *
     * @author lokka30
     * @since v0.0.0
     */
    public HashSet<Integer> getSlots() {
        return slots;
    }

    /**
     * TODO Describe...
     *
     * @author lokka30
     * @since v0.0.0
     */
    public HashMap<IconInteractionType, HashSet<Action>> getActionMap() {
        return actionMap;
    }
}
