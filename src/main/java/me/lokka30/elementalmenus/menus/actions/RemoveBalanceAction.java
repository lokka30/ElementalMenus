package me.lokka30.elementalmenus.menus.actions;

import org.bukkit.entity.Player;

public class RemoveBalanceAction implements Action {

    double balanceToRemove;

    public RemoveBalanceAction(double balanceToRemove) {
        this.balanceToRemove = balanceToRemove;
    }

    @Override
    public void act(Player player) {
        //TODO
    }
}