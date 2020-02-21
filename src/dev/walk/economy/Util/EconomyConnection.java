package dev.walk.economy.Util;

import dev.walk.economy.Manager.Account;
import dev.walk.economy.Manager.AccountManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("deprecation")
public class EconomyConnection implements Economy {

    EconomyUtils utils = new EconomyUtils();

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "WEconomy";
    }

    @Override
    public boolean hasBankSupport() { return false; }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double value) {
        return utils.formatMoney(value);
    }

    @Override
    public String currencyNamePlural() {
        return "dinheiros";
    }

    @Override
    public String currencyNameSingular() {
        return "dinheiro";
    }

    @Override
    public boolean hasAccount(String player) {
        return new AccountManager(player).getInstance().exists();
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return new AccountManager(offlinePlayer).getInstance().exists();
    }

    @Override
    public boolean hasAccount(String player, String world) {
        return new AccountManager(player).getInstance().exists();
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String world) {
        return new AccountManager(offlinePlayer).getInstance().exists();
    }

    @Override
    public double getBalance(String player) {
        return new AccountManager(player).getInstance().getMoney();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return new AccountManager(offlinePlayer).getInstance().getMoney();
    }

    @Override
    public double getBalance(String player, String world) {
        return new AccountManager(player).getInstance().getMoney();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String world) {
        return new AccountManager(offlinePlayer).getInstance().getMoney();
    }

    @Override
    public boolean has(String player, double money) {
        return new AccountManager(player).getInstance().hasMoney(money);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double money) {
        return new AccountManager(offlinePlayer).getInstance().hasMoney(money);
    }

    @Override
    public boolean has(String player, String world, double money) {
        return new AccountManager(player).getInstance().hasMoney(money);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String world, double money) {
        return new AccountManager(offlinePlayer).getInstance().hasMoney(money);
    }

    @Override
    public EconomyResponse withdrawPlayer(String player, double money) {
        Account account = new AccountManager(player).getInstance();
        Boolean sucess = account.removeMoney(money).getOne();
        return new EconomyResponse(money, account.getMoney(), sucess ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, sucess ? "Operação executada com exito." : "Falhou a operação.");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double money) {
        Account account = new AccountManager(offlinePlayer).getInstance();
        Boolean sucess = account.removeMoney(money).getOne();
        return new EconomyResponse(money, account.getMoney(), sucess ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, sucess ? "Operação executada com exito." : "Falhou a operação.");
    }

    @Override
    public EconomyResponse withdrawPlayer(String player, String world, double money) {
        Account account = new AccountManager(player).getInstance();
        Boolean sucess = account.removeMoney(money).getOne();
        return new EconomyResponse(money, account.getMoney(), sucess ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, sucess ? "Operação executada com exito." : "Falhou a operação.");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String world, double money) {
        Account account = new AccountManager(offlinePlayer).getInstance();
        Boolean sucess = account.removeMoney(money).getOne();
        return new EconomyResponse(money, account.getMoney(), sucess ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, sucess ? "Operação executada com exito." : "Falhou a operação.");
    }

    @Override
    public EconomyResponse depositPlayer(String player, double money) {
        Account account = new AccountManager(player).getInstance();
        Boolean sucess = account.addMoney(money).getOne();
        return new EconomyResponse(money, account.getMoney(), sucess ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, sucess ? "Operação executada com exito." : "Falhou a operação.");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double money) {
        Account account = new AccountManager(offlinePlayer).getInstance();
        Boolean sucess = account.addMoney(money).getOne();
        return new EconomyResponse(money, account.getMoney(), sucess ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, sucess ? "Operação executada com exito." : "Falhou a operação.");
    }

    @Override
    public EconomyResponse depositPlayer(String player, String world, double money) {
        Account account = new AccountManager(player).getInstance();
        Boolean sucess = account.addMoney(money).getOne();
        return new EconomyResponse(money, account.getMoney(), sucess ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, sucess ? "Operação executada com exito." : "Falhou a operação.");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String world, double money) {
        Account account = new AccountManager(offlinePlayer).getInstance();
        Boolean sucess = account.addMoney(money).getOne();
        return new EconomyResponse(money, account.getMoney(), sucess ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, sucess ? "Operação executada com exito." : "Falhou a operação.");
    }

    @Override
    public boolean createPlayerAccount(String player) {
        return new AccountManager(player).getInstance().createAccount();
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return new AccountManager(offlinePlayer).getInstance().createAccount();
    }

    @Override
    public boolean createPlayerAccount(String player, String world) {
        return new AccountManager(player).getInstance().createAccount();
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String world) {
        return new AccountManager(offlinePlayer).getInstance().createAccount();
    }

    @Override
    public EconomyResponse createBank(String bank, String world) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String bank, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String bank) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String bank) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String bank, double coins) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String bank, double coins) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String bank, double coins) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String bank, String world) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String bank, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String bank, String world) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String bank, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

}
