package me.lokka30.elementalmenus.menus.actions;

import org.bukkit.entity.Player;

public class SetBalanceAction implements Action {

    double balance;

    public SetBalanceAction(double balance) {
        this.balance = balance;
    }

    @Override
    public void act(Player player) {
        //TODO
    }
}