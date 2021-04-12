package me.lokka30.elementalmenus.menus.actions;

import me.lokka30.elementalmenus.ElementalMenus;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BossBarMessageAction implements Action {
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
            Bukkit.getOnlinePlayers().forEach(this::sendBossBarMessage);
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