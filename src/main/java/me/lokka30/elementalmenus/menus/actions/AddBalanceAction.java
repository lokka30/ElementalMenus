package me.lokka30.elementalmenus.menus.actions;

import org.bukkit.entity.Player;

public class AddBalanceAction implements Action {

    double balanceToAdd;

    public AddBalanceAction(double balanceToAdd) {
        this.balanceToAdd = balanceToAdd;
    }

    @Override
    public void act(Player player) {
        //TODO
    }
}