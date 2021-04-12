package me.lokka30.elementalmenus.menus.actions;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundAction implements Action {

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
