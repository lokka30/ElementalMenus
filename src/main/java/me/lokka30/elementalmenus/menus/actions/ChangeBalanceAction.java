package me.lokka30.elementalmenus.menus.actions;

import me.lokka30.elementalmenus.ElementalMenus;
import me.lokka30.elementalmenus.misc.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

/**
 * TODO Describe...
 *
 * @author lokka30
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
        Economy economy = ElementalMenus.getInstance().economy;

        if (amount <= 0) {
            Utils.LOGGER.error("Invalid amount specified for a balance change action with amount '&b" + amount + "&7' and transaction type '&b" + transaction + "&7'. Amount must be greater than 0.");
            return;
        }

        switch (transaction) {
            case DEPOSIT_BALANCE:
                economy.depositPlayer(player, amount);
                break;
            case WITHDRAW_BALANCE:
                if (economy.has(player, amount)) {
                    economy.withdrawPlayer(player, amount);
                }
                break;
            case SET_BALANCE:
                economy.withdrawPlayer(player, economy.getBalance(player));
                economy.depositPlayer(player, amount);
                break;
            default:
                throw new IllegalStateException("Unexpected Transaction " + transaction);
        }
    }

    /**
     * Type of economy transaction to take place
     *
     * @author lokka30
     * @since v0.0.0
     */
    public enum Transaction {
        DEPOSIT_BALANCE,
        WITHDRAW_BALANCE,
        SET_BALANCE
    }
}
