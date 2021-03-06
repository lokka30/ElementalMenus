package me.lokka30.elementalmenus.menus.actions;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.misc.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @since v0.0.0
 */
public class SendMessageAction implements Action {

    MessageType type;
    List<String> messages;
    boolean broadcast;
    ExtraInfo extraInfo;

    public SendMessageAction(@NotNull MessageType type, @Nullable List<String> messages, boolean broadcast, ExtraInfo extraInfo) {
        this.type = type;
        this.messages = messages;
        this.broadcast = broadcast;
        this.extraInfo = extraInfo;
    }

    @Override
    public void parse(Player player) {
        switch (type) {
            case STANDARD:
                if (broadcast) {
                    Bukkit.getOnlinePlayers().forEach(onlinePlayer -> messages.forEach(onlinePlayer::sendMessage));
                } else {
                    messages.forEach(player::sendMessage);
                }
                break;
            case JSON:
                if (broadcast) {
                    messages.forEach(message -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw @a " + message));
                } else {
                    messages.forEach(message -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + message));
                }
                break;
            case ACTION_BAR:
                try {
                    Class.forName("org.bukkit.entity.Player.Spigot");
                } catch (ClassNotFoundException e) {
                    Utils.LOGGER.error("You must be using &bSpigotMC&7 (or a derivative such as &bPaperMC&7) to utilise the &bAction Bar&7 messaging type, since it relies on Spigot code to function.");
                    break;
                }

                ActionBarExtraInfo actionBarExtraInfo = (ActionBarExtraInfo) extraInfo;

                HashSet<Player> actionBarRecipients = new HashSet<>();
                if (broadcast) {
                    actionBarRecipients.addAll(Bukkit.getOnlinePlayers());
                } else {
                    actionBarRecipients.add(player);
                }

                for (int i = 0; i < messages.size(); i++) {
                    int tempI = i;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            actionBarRecipients.forEach(recipient -> {
                                if (recipient != null) {
                                    recipient.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messages.get(tempI)));
                                }
                            });
                        }
                    }.runTaskLater(ElementalMenus.getInstance(), (long) i * actionBarExtraInfo.getDuration());
                }
                break;
            case BOSS_BAR:
                BossBarExtraInfo bossBarExtraInfo = (BossBarExtraInfo) extraInfo;

                HashSet<Player> bossBarRecipients = new HashSet<>();
                if (broadcast) {
                    bossBarRecipients.addAll(Bukkit.getOnlinePlayers());
                } else {
                    bossBarRecipients.add(player);
                }

                BossBar bossBar = Bukkit.createBossBar("", bossBarExtraInfo.getBarColor(), bossBarExtraInfo.getBarStyle(), bossBarExtraInfo.getBarFlags());
                bossBarRecipients.forEach(bossBar::addPlayer);
                bossBarRecipients.forEach(recipient -> ElementalMenus.getInstance().menuManager.bossBarsToRemoveOnQuit.put(player.getUniqueId(), bossBar));

                for (int i = 0; i < messages.size(); i++) {
                    boolean isLastMessage = (i == messages.size() - 1);

                    int tempI = i;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (isLastMessage) {
                                bossBarRecipients.forEach(recipient -> {
                                    if (recipient != null) {
                                        bossBar.removePlayer(recipient);
                                    }
                                });
                            } else {
                                bossBar.setTitle(messages.get(tempI));
                            }
                        }
                    }.runTaskLater(ElementalMenus.getInstance(), (long) i * bossBarExtraInfo.getDuration());
                }
                break;
            case TITLE:
                TitleExtraInfo titleExtraInfo = (TitleExtraInfo) extraInfo;

                HashSet<Player> titleRecipients = new HashSet<>();
                if (broadcast) {
                    titleRecipients.addAll(Bukkit.getOnlinePlayers());
                } else {
                    titleRecipients.add(player);
                }

                titleRecipients.forEach(recipient -> {
                    if (recipient != null) { //TODO ensure messages for title contains non-null subtitle, can be empty string.
                        player.sendTitle(messages.get(0), messages.get(1), titleExtraInfo.getFade(), titleExtraInfo.getDuration(), titleExtraInfo.getFade());
                    }
                });

                if (messages.size() > 2)
                    Utils.LOGGER.warning("One of your menus is sending a Title message with more than 2 lines specified. Only a 'ti" +
                            "tle' should be specified, and a 'subtitle' is optional - therefore, only 1 or 2 lines should be set for Title messages.");
                break;
            default:
                throw new IllegalStateException("Unexpected MessageType " + type);
        }
    }

    /**
     * TODO Describe...
     *
     * @author lokka30
     * @since v0.0.0
     */
    public enum MessageType {
        STANDARD,
        JSON,
        ACTION_BAR,
        BOSS_BAR,
        TITLE
    }

    /**
     * TODO Describe...
     *
     * @author lokka30
     * @since v0.0.0
     */
    interface ExtraInfo {
    }

    /**
     * TODO Describe...
     *
     * @author lokka30
     * @since v0.0.0
     */
    static class ActionBarExtraInfo implements ExtraInfo {
        int duration;

        public ActionBarExtraInfo(int duration) {
            this.duration = duration;
        }

        /**
         * TODO Describe...
         *
         * @author lokka30
         * @since v0.0.0
         */
        public int getDuration() {
            return duration;
        }
    }

    /**
     * TODO Describe...
     *
     * @author lokka30
     * @since v0.0.0
     */
    static class BossBarExtraInfo implements ExtraInfo {
        int duration;
        BarColor barColor;
        BarStyle barStyle;
        BarFlag[] barFlags;

        public BossBarExtraInfo(int duration, BarColor barColor, BarStyle barStyle, BarFlag[] barFlags) {
            this.duration = duration;
            this.barColor = barColor;
            this.barStyle = barStyle;
            this.barFlags = barFlags;
        }

        /**
         * TODO Describe...
         *
         * @author lokka30
         * @since v0.0.0
         */
        public int getDuration() {
            return duration;
        }

        /**
         * TODO Describe...
         *
         * @author lokka30
         * @since v0.0.0
         */
        public BarColor getBarColor() {
            return barColor;
        }

        /**
         * TODO Describe...
         *
         * @author lokka30
         * @since v0.0.0
         */
        public BarStyle getBarStyle() {
            return barStyle;
        }

        /**
         * TODO Describe...
         *
         * @author lokka30
         * @since v0.0.0
         */
        public BarFlag[] getBarFlags() {
            return barFlags;
        }
    }

    /**
     * TODO Describe...
     *
     * @author lokka30
     * @since v0.0.0
     */
    static class TitleExtraInfo implements ExtraInfo {
        int duration;
        int fade;

        public TitleExtraInfo(int duration, int fade) {
            this.duration = duration;
            this.fade = fade;
        }

        /**
         * TODO Describe...
         *
         * @author lokka30
         * @since v0.0.0
         */
        public int getDuration() {
            return duration;
        }

        /**
         * TODO Describe...
         *
         * @author lokka30
         * @since v0.0.0
         */
        public int getFade() {
            return fade;
        }
    }
}
