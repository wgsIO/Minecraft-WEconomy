package dev.walk.economy.Util;

import dev.walk.economy.Economy;
import dev.walk.economy.Manager.Account;
import dev.walk.economy.Manager.AccountManager;
import dev.walk.economy.Manager.Storage;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaceholderExpansionHook extends PlaceholderExpansion {

    EconomyUtils utils = new EconomyUtils();

    @Override
    public String getIdentifier() {
        return "weconomy";
    }

    @Override
    public String getPlugin() {
        return Economy.getEconomy().getName();
    }

    @Override
    public String getAuthor() {
        return "WalkDev";
    }

    @Override
    public String getVersion() {
        return "1.2A";
    }

    @Override
    public String onPlaceholderRequest(Player player, String holder) {
        try {
            if (holder.equalsIgnoreCase("money")) {
                Account account = new AccountManager(player).getInstance();
                return utils.formatMoney(account.getMoney());
            } else if (holder.equalsIgnoreCase("moneyrankall")) {
                if (Storage.Accounts.size() > 1) {
                    int rank = Storage.Magnata.getRanks().get(player.getUniqueId());
                    return (rank >= 1 ? rank + "" : "");
                } else { return 1+""; }
            } else if (holder.equalsIgnoreCase("moneyrank")) {
                if (Storage.Accounts.size() > 1) {
                    int rank = Storage.Magnata.getRanks(1, Storage.config.getSizeMoneyTop()).get(player.getUniqueId());
                    return (rank >= 1 ? rank + "" : "");
                } else { return 1+""; }
            } else if (holder.equalsIgnoreCase("magnatatag")) {
                Account account = new AccountManager(player).getInstance();
                return (account.isMagnata() ? Storage.Magnata.getMagnataTag() : "");
            } else if (holder.equalsIgnoreCase("ismagnata")) {
                Account account = new AccountManager(player).getInstance();
                return (account.isMagnata() ? "sim" : "nao");
            }
        } catch (Exception e) {
            Storage.Log.addLog((CommandSender) player, "Ocorreu erro ao pegar placeholder: "+holder);
        }
        return null;
    }
}
