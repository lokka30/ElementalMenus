package me.lokka30.elementalmenus.objects;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.utils.Utils;
import me.lokka30.microlib.MessageUtils;
import me.lokka30.microlib.YamlConfigFile;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Menu {
    public final String name;
    public final YamlConfigFile config;

    public Menu(final String name, final YamlConfigFile config) {
        this.name = name;
        this.config = config;
    }

    public Icon fillerIcon;
    public HashSet<Icon> icons = new HashSet<>();

    private Inventory inventory;

    protected void load() {
        inventory = Bukkit.createInventory(null,
                Utils.bound(1, config.getConfig().getInt("menu.rows", 3), 6) * 9,
                MessageUtils.colorizeAll(config.getConfig().getString("menu.title", "Menu")));

        loadIcons();
    }

    public void open(Player player) {
        ElementalMenus.getInstance().menuManager.menusCurrentlyOpen.put(player.getUniqueId(), name);

        player.openInventory(inventory);

        //TODO open actions
    }

    public void registerClick(Player player, int slot) {
        //TODO
    }

    public boolean isOpen(Player player) {
        return ElementalMenus.getInstance().menuManager.menusCurrentlyOpen.getOrDefault(player.getUniqueId(), "impossible.menu.name ;)").equals(name);
    }

    private void loadIcons() {
        /*
        Load filler icon
         */
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, fillerIcon.getItemStack());
        }

        /*
        Load other icons
         */
        //TODO
    }

    public static class Icon {

        final int[] slots;
        final String displayName;
        final Material material;
        int amount;
        List<String> lore;
        final HashMap<Interaction, Action> actionMap;

        public Icon(int[] slots, String displayName, Material material, int amount, List<String> lore, HashMap<Interaction, Action> actionMap) {
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
                if (!displayName.isEmpty()) itemStack.getItemMeta().setDisplayName(displayName);
                if (!lore.isEmpty()) itemStack.getItemMeta().setLore(lore);
            }

            return itemStack;
        }

        enum Interaction {
            LEFT_CLICK,
            MIDDLE_CLICK,
            RIGHT_CLICK,
            DROP,
            ALL
        }

        interface Action {
            void act(Player player);
        }

        class SoundAction implements Action {
            Sound sound;
            float pitch, volume;
            boolean onlyForPlayer;

            public SoundAction(Sound sound, float pitch, float volume, boolean onlyForPlayer) {
                this.sound = sound;
                this.pitch = pitch;
                this.volume = volume;
                this.onlyForPlayer = onlyForPlayer;
            }

            @Override
            public void act(Player player) {
                if (onlyForPlayer) {
                    player.playSound(player.getLocation(), sound, pitch, volume);
                } else {
                    player.getWorld().playSound(player.getLocation(), sound, pitch, volume);
                }
            }
        }

        static class PlayerRunCommandsAction implements Action {
            List<String> commands;

            public PlayerRunCommandsAction(List<String> commands) {
                this.commands = commands;
            }

            @Override
            public void act(Player player) {
                final boolean prefixedWithSlash = ElementalMenus.getInstance().advancedSettingsCfg.getConfig().getBoolean("menus.commands-prefixed-with-slash", true);

                commands.forEach(command -> {
                    if (prefixedWithSlash) command = command.replaceFirst("/", "");

                    command = command
                            .replace("%username%", player.getName())
                            .replace("%displayname%", player.getDisplayName());

                    Bukkit.dispatchCommand(player, command);
                });
            }
        }

        static class ConsoleRunCommandsAction implements Action {
            List<String> commands;

            public ConsoleRunCommandsAction(List<String> commands) {
                this.commands = commands;
            }

            @Override
            public void act(Player player) {
                final boolean prefixedWithSlash = ElementalMenus.getInstance().advancedSettingsCfg.getConfig().getBoolean("menus.commands-prefixed-with-slash", true);

                commands.forEach(command -> {
                    if (prefixedWithSlash) command = command.replaceFirst("/", "");

                    command = command
                            .replace("%username%", player.getName())
                            .replace("%displayname%", player.getDisplayName());

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                });
            }
        }

        static class CloseMenuAction implements Action {

            boolean closeMenu;

            public CloseMenuAction(boolean closeMenu) {
                this.closeMenu = closeMenu;
            }

            @Override
            public void act(Player player) {
                if (closeMenu) player.closeInventory();
            }
        }

        static class RemoveItemsAction implements Action {

            HashSet<DetectableItem> itemsToRemove;

            public RemoveItemsAction(HashSet<DetectableItem> itemsToRemove) {
                this.itemsToRemove = itemsToRemove;
            }

            @Override
            public void act(Player player) {
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
    }

    static class AddBalanceAction implements Icon.Action {

        double balanceToAdd;

        public AddBalanceAction(double balanceToAdd) {
            this.balanceToAdd = balanceToAdd;
        }

        @Override
        public void act(Player player) {
            //TODO
        }
    }

    static class RemoveBalanceAction implements Icon.Action {

        double balanceToRemove;

        public RemoveBalanceAction(double balanceToRemove) {
            this.balanceToRemove = balanceToRemove;
        }

        @Override
        public void act(Player player) {
            //TODO
        }
    }

    static class SetBalanceAction implements Icon.Action {

        double balance;

        public SetBalanceAction(double balance) {
            this.balance = balance;
        }

        @Override
        public void act(Player player) {
            //TODO
        }
    }

    static class StandardMessageAction implements Icon.Action {

        private final List<String> messages;
        private final boolean broadcast;

        public StandardMessageAction(List<String> messages, boolean broadcast) {
            this.messages = messages;
            this.broadcast = broadcast;
        }

        @Override
        public void act(Player player) {
            if (broadcast) {
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> messages.forEach(onlinePlayer::sendMessage));
            } else {
                messages.forEach(player::sendMessage);
            }
        }
    }

    static class JsonMessageAction implements Icon.Action {

        private final List<String> messages;
        private final boolean broadcast;

        public JsonMessageAction(List<String> messages, boolean broadcast) {
            this.messages = messages;
            this.broadcast = broadcast;
        }

        @Override
        public void act(Player player) {
            if (broadcast) {
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> messages.forEach(message -> sendJsonMessage(onlinePlayer, message)));
            } else {
                messages.forEach(message -> sendJsonMessage(player, message));
            }
        }

        private void sendJsonMessage(Player player, String message) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + message);
        }
    }

    static class ActionBarMessageAction implements Icon.Action {
        private final String message;
        private final boolean broadcast;

        public ActionBarMessageAction(String message, boolean broadcast) {
            this.message = message;
            this.broadcast = broadcast;
        }

        @Override
        public void act(Player player) {
            if (broadcast) {
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> sendActionBarMessage(onlinePlayer));
            } else {
                sendActionBarMessage(player);
            }
        }

        private void sendActionBarMessage(Player player) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        }
    }

    static class TitleMessageAction implements Icon.Action {
        private final String title, subtitle;
        private final boolean broadcast;
        int fadeIn, stay, fadeOut;

        public TitleMessageAction(String title, String subtitle, int fadeIn, int stay, int fadeOut, boolean broadcast) {
            this.title = title;
            this.subtitle = subtitle;
            this.fadeIn = fadeIn;
            this.stay = stay;
            this.fadeOut = fadeOut;
            this.broadcast = broadcast;
        }

        @Override
        public void act(Player player) {
            if (broadcast) {
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> sendTitleMessage(onlinePlayer));
            } else {
                sendTitleMessage(player);
            }
        }

        private void sendTitleMessage(Player player) {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    static class BossBarMessageAction implements Icon.Action {
        private final String title;
        private final BarColor color;
        private final BarStyle style;
        private final BarFlag[] flags;
        private final int duration;
        private final boolean broadcast;

        public BossBarMessageAction(String title, BarColor color, BarStyle style, BarFlag[] flags, int duration, boolean broadcast) {
            this.title = title;
            this.color = color;
            this.style = style;
            this.flags = flags;
            this.duration = duration * 20;
            this.broadcast = broadcast;
        }

        @Override
        public void act(Player player) {
            if (broadcast) {
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> sendBossBarMessage(onlinePlayer));
            } else {
                sendBossBarMessage(player);
            }
        }

        private void sendBossBarMessage(Player player) {
            BossBar bossBar = Bukkit.createBossBar(title, color, style, flags);
            bossBar.addPlayer(player);
            ElementalMenus.getInstance().menuManager.bossBarsToRemoveOnQuit.put(player.getUniqueId(), bossBar);

            new BukkitRunnable() {
                @Override
                public void run() {
                    //noinspection ConstantConditions
                    if (player != null && bossBar.getPlayers().contains(player)) {
                        bossBar.removePlayer(player);
                    }
                }
            }.runTaskLater(ElementalMenus.getInstance(), duration);
        }
    }

    static class OpenOtherMenuAction implements Icon.Action {

        Menu otherMenu;

        public OpenOtherMenuAction(Menu otherMenu) {
            this.otherMenu = otherMenu;
        }

        @Override
        public void act(Player player) {
            player.closeInventory();

            // 1 tick delay to avoid issues
            new BukkitRunnable() {
                @Override
                public void run() {
                    otherMenu.open(player);
                }
            }.runTaskLater(main, 1);
        }
    }

    static class ChangeServerAction implements Icon.Action {

        String server;

        public ChangeServerAction(String server) {
            this.server = server;
        }

        @Override
        public void act(Player player) {
            //TODO
        }
    }

    static class DetectableItem {
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
