package me.lokka30.elementalmenus.menus.actions;

import me.lokka30.elementalmenus.menus.icons.DetectableItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @since v0.0.0
 */
public class RemoveItemsAction implements Action {

    HashSet<DetectableItem> itemsToRemove;

    public RemoveItemsAction(HashSet<DetectableItem> itemsToRemove) {
        this.itemsToRemove = itemsToRemove;
    }

    @Override
    public void parse(Player player) {
        itemsToRemove.forEach(detectableItem -> {
            for (ItemStack itemInInventory : player.getInventory().getContents()) {
                if (itemInInventory.getType() != detectableItem.getMaterial()) continue;

                if (!detectableItem.getDisplayName().isEmpty()) {
                    //noinspection ConstantConditions
                    if (!(itemInInventory.hasItemMeta() && itemInInventory.getItemMeta().hasDisplayName()))
                        continue;
                    if (!itemInInventory.getItemMeta().getDisplayName().equals(detectableItem.getDisplayName()))
                        continue;
                }

                if (!detectableItem.getLore().isEmpty()) {
                    //noinspection ConstantConditions
                    if (!(itemInInventory.hasItemMeta() && itemInInventory.getItemMeta().hasLore())) continue;

                    int detections = 0;
                    //noinspection ConstantConditions
                    for (String loreInItem : itemInInventory.getItemMeta().getLore()) {
                        if (detectableItem.getLore().contains(loreInItem)) {
                            detections++;
                        }
                    }

                    if (detections != detectableItem.getLore().size()) continue;
                }

                final int newAmount = itemInInventory.getAmount() - detectableItem.getAmount();
                if (newAmount <= 0) {
                    itemInInventory.setType(Material.AIR);
                } else {
                    itemInInventory.setAmount(newAmount);
                }
            }
        });
    }
}