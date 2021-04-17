package me.lokka30.elementalmenus.menus.actions;

import org.bukkit.entity.Player;

/**
 * TODO Describe...
 *
 * @author lokka30
 * @contributors none
 * @since v0.0.0
 */
public class ChangeBalanceAction implements Action {

    double amount;
    Transaction transaction;

    public ChangeBalanceAction(double amount, Transaction transaction) {
        this.amount = amount;
        this.transaction = transaction;
    }

    @Override
    public void parse(Player player) {
        switch (transaction) {
            case DEPOSIT_BALANCE:
                //TODO
                break;
            case WITHDRAW_BALANCE:
                //TODO
                break;
            case SET_BALANCE:
                //TODO
                break;
            default:
                throw new IllegalStateException("Unexpected Transaction " + transaction);
        }
    }

    public enum Transaction {
        DEPOSIT_BALANCE,
        WITHDRAW_BALANCE,
        SET_BALANCE
    }
}
