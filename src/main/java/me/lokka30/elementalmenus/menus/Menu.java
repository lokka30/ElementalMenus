package me.lokka30.elementalmenus.menus;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.menus.actions.Action;
import me.lokka30.elementalmenus.menus.icons.Icon;
import me.lokka30.elementalmenus.menus.icons.IconInteractionType;
import me.lokka30.elementalmenus.misc.Utils;
import me.lokka30.microlib.MessageUtils;
import me.lokka30.microlib.YamlConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.util.*;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @contributors none
 * @since v0.0.0
 */
public class Menu {
    public final String name;
    public final YamlConfigFile config;
    public final HashSet<Icon> icons = new HashSet<>();
    public Icon fillerIcon;
    private Inventory inventory;

    public Menu(final String name, final YamlConfigFile config) {
        this.name = name;
        this.config = config;
    }

    protected void load() {
        inventory = Bukkit.createInventory(null,
                Utils.bound(1, config.getConfig().getInt("menu.rows", 3), 6) * 9,
                MessageUtils.colorizeAll(config.getConfig().getString("menu.title", "Menu")));

        loadIcons();

        setIcons();
    }

    public void open(final Player player) {
        ElementalMenus.getInstance().menuManager.menusCurrentlyOpen.put(player.getUniqueId(), name);

        player.openInventory(inventory);

        //TODO open actions
    }

    public void processInteraction(final Player player, final int slot, final IconInteractionType type) {
        //TODO Process actions and conditions for interaction type and slot
    }

    public void processEvent(final MenuCloseEventType menuCloseEventType, final Player player) {
        ElementalMenus.getInstance().menuManager.menusCurrentlyOpen.remove(player.getUniqueId());
        switch (menuCloseEventType) {
            case CLOSE_DISCONNECT:
                //TODO process actions for CLOSE_DISCONNECT
                break;
            case CLOSE_INVENTORY:
                //TODO process actions for CLOSE_INVENTORY
                break;
            default:
                throw new IllegalStateException(menuCloseEventType + " is an unexpected MenuCloseEventType");
        }
    }

    public boolean isOpen(Player player) {
        return ElementalMenus.getInstance().menuManager.menusCurrentlyOpen.getOrDefault(player.getUniqueId(), "impossible.menu.name ;)").equals(name);
    }

    private void loadIcons() {
        /*
        Filler icon.
        Note: filler icon does not initially have any slots, its slots are populated in Menu#setIcons.
         */
        fillerIcon = new Icon(new HashSet<>(), getItemStackOfIcon("filler-icon", "filler-icon"), getActionMapOfIcon("filler-icon", "filler-icon"));

        for (String iconName : (Set<String>) Utils.getDefaultIfNull(config.getConfig().getConfigurationSection("icons").getKeys(false), new ArrayList<>())) {
            final String configPath = "icons." + iconName + ".";

            /*
            Slots
             */
            //TODO Convert to new system 'positions'.
            HashSet<Integer> slots = new HashSet<>();
            int slot = Math.max(
                    Utils.bound(0, // min slot limit
                            config.getConfig().getInt(configPath + "position.x", 0), 8) // x value
                            + Utils.bound(0, 9 * config.getConfig().getInt(configPath + "position.y", 0), 3), // y value
                    inventory.getSize() - 1); // max slot limit

            icons.add(new Icon(slots, getItemStackOfIcon(iconName, configPath), getActionMapOfIcon(iconName, configPath)));
        }
    }

    private void setIcons() {
        for (int i = 0; i < inventory.getSize(); i++) {
            final Icon icon = getIconAtSlot(i);

            inventory.setItem(i, icon.getItemStack());

            // Populate slots of filler icon.
            if (icon == fillerIcon) fillerIcon.getSlots().add(i);
        }
    }

    public Icon getIconAtSlot(int slot) {
        for (Icon icon : icons) {
            for (int definedSlot : icon.getSlots()) {
                if (slot == definedSlot) return icon;
            }
        }

        return fillerIcon;
    }

    private ItemStack getItemStackOfIcon(String iconName, String configPath) {
        /*
            Material
             */
        Material material = Material.AIR;
        if (config.getConfig().contains(configPath + "material")) {
            try {
                material = Material.valueOf(config.getConfig().getString(configPath + "material"));
            } catch (IllegalArgumentException e) {
                Utils.LOGGER.error("Icon '&b" + iconName + "&7' in menu '&b" + name + "&7' has an invalid material name specified! Using '&bAIR&7' until you fix it.");
            }
        } else {
            Utils.LOGGER.error("Material for an item is not defined for icon &b'" + iconName + "'&7 in menu '&b" + name + "&7'! Using '&bAIR&7' until you fix it.");
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

            /*
            Amount
             */
        itemStack.setAmount(config.getConfig().getInt(configPath + "amount", 1));

            /*
            Enchantments
             */
        if (config.getConfig().contains(configPath + "enchantments")) {
            boolean allowUnsafe = config.getConfig().getBoolean(configPath + "enchantments.allow-unsafe", false);
            Map<Enchantment, Integer> enchantmentMap = new HashMap<>();

            //TODO Get and apply enchantments to item

            if (!enchantmentMap.isEmpty()) {
                if (allowUnsafe) {
                    itemStack.addUnsafeEnchantments(enchantmentMap);
                } else {
                    itemStack.addEnchantments(enchantmentMap);
                }
            }
        }

        if (itemMeta != null) {
                /*
                Display Name
                 */
            itemMeta.setDisplayName(config.getConfig().getString(configPath + "name", null));

                /*
                Lore
                 */
            itemMeta.setLore(config.getConfig().getStringList(configPath + "lore"));

                /*
                Remove Durability
                 */
            if (itemMeta instanceof Damageable) {
                ((Damageable) itemMeta).setDamage(
                        config.getConfig().getInt(configPath + "remove-durability", 0)
                );
            }

                /*
                Color
                 */
            if (itemMeta instanceof LeatherArmorMeta) {
                if (config.getConfig().contains(configPath + "color")) {
                    ((LeatherArmorMeta) itemMeta).setColor(Color.fromRGB(
                            Utils.bound(0, config.getConfig().getInt(configPath + "color.r", 0), 255),
                            Utils.bound(0, config.getConfig().getInt(configPath + "color.g", 0), 255),
                            Utils.bound(0, config.getConfig().getInt(configPath + "color.b", 0), 255)
                    ));
                }
            }

                /*
                Skull Owner
                 */
            if (itemMeta instanceof SkullMeta) {
                if (config.getConfig().contains(configPath + "skull-owner")) {
                    //noinspection deprecation, ConstantConditions
                    ((SkullMeta) itemMeta).setOwningPlayer(Bukkit.getOfflinePlayer(
                            config.getConfig().getString(configPath + "skull-owner", "lokka30")
                    ));
                }
            }

                /*
                Banner Patterns
                 */
            if (itemMeta instanceof BannerMeta) {
                List<Pattern> patterns = new ArrayList<>();

                for (String configuredPattern : config.getConfig().getStringList(configPath + "banner-patterns")) {
                    PatternType patternType;
                    DyeColor dyeColor;

                    String[] split = configuredPattern.split(": ");

                    if (split.length != 2) {
                        Utils.LOGGER.error("Invalid banner pattern '&b" + configuredPattern + "&7' for icon '&b" + iconName + "&7' in menu '&b" + name + "&7'!");
                        continue;
                    }

                    try {
                        patternType = PatternType.valueOf(split[0]);
                    } catch (IllegalArgumentException ex) {
                        Utils.LOGGER.error("Invalid banner pattern type '&b" + split[0] + "&7' for icon '&b" + iconName + "&7' in menu '&b" + name + "&7'!");
                        continue;
                    }

                    try {
                        dyeColor = DyeColor.valueOf(split[1]);
                    } catch (IllegalArgumentException ex) {
                        Utils.LOGGER.error("Invalid banner dye color '&b" + split[1] + "&7' for icon '&b" + iconName + "&7' in menu '&b" + name + "&7'!");
                        continue;
                    }

                    patterns.add(new Pattern(dyeColor, patternType));
                }

                ((BannerMeta) itemMeta).setPatterns(patterns);
            }
        }

        if (itemStack.hasItemMeta()) itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private HashMap<IconInteractionType, HashSet<Action>> getActionMapOfIcon(String iconName, String configPath) {
        HashMap<IconInteractionType, HashSet<Action>> actionMap = new HashMap<>();

        //TODO

        return actionMap;
    }

    public enum MenuCloseEventType {
        /**
         * When the player closes the
         * inventory by normal means,
         * or if a plugin or the server
         * force-closes the inventory.
         */
        CLOSE_INVENTORY,

        /**
         * When the user disconnects
         * whilst the menu is open.
         * Limited actions are applicable
         * to players concerning this
         * menu event.
         */
        CLOSE_DISCONNECT
    }
}
