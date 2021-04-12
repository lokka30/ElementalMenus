package me.lokka30.elementalmenus.menus;

import me.lokka30.elementalmenus.menus.actions.Action;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unused")
public class Icon {

    final int[] slots;
    final String displayName;
    final Material material;
    int amount;
    List<String> lore;
    final HashMap<IconInteractionType, Action> actionMap;

    public Icon(int[] slots, String displayName, Material material, int amount, List<String> lore, HashMap<IconInteractionType, Action> actionMap) {
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

    public enum IconInteractionType {
        LEFT_CLICK,
        MIDDLE_CLICK,
        RIGHT_CLICK,
        DROP,
        ALL
    }

    public static class DetectableItem {
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
}
