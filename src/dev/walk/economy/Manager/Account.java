package dev.walk.economy.Manager;

import dev.walk.economy.Economy;
import dev.walk.economy.Events.ChangeType;
import dev.walk.economy.Events.EconomyAccountEvent;
import dev.walk.economy.Util.EconomyUtils;
import dev.walk.economy.Util.MultiValue;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class Account implements Comparable<Account>, Cloneable, Serializable {

    ConfigPlugin config;
    MySql mysql;

    private UUID uuid;
    private double money;
    private boolean isPlayer = false, created = false;

    private GsonManager gson;
    private EconomyUtils util = new EconomyUtils();

    public Account(UUID uuid) {
        this.uuid = uuid;
        mysql = Economy.getEconomy().mysql;
        config = Storage.config;
        if (!config.isMysql()) {
            gson = new GsonManager(Economy.getEconomy().getDataFolder() + "/Accounts", uuid.toString()).prepareGson();
        }
    }

    public Account(UUID uuid, double money, boolean isPlayer, boolean created) {
        this.uuid = uuid;
        this.money = money;
        this.isPlayer = isPlayer;
        this.created = created;
        mysql = Economy.getEconomy().mysql;
        config = Storage.config;
        if (!config.isMysql()) {
            gson = new GsonManager(Economy.getEconomy().getDataFolder() + "/Accounts", uuid.toString()).prepareGson();
        }
    }

    public double getMoney() {
        try{ return money; } catch ( Exception e ) { return 0; }
    }

    public MultiValue<Boolean, Double> setMoney(double money, boolean eventStarted) {
        if (exists()) {
            if (money > util.getMaxCoins()) money = util.getMaxCoins();
            if (money < 1) money = 0;
            money = util.doubleRedond(money, 2, EconomyUtils.TypeRedond.CEIL);
            EconomyAccountEvent e = null;
            if (!eventStarted) {
                e = new EconomyAccountEvent(ChangeType.SETTING, this, money);
            }
            if (e == null || !e.isCancelled()) {
                this.money = money;
                if (!config.isMysql()) {
                    gson.put("money", money);
                    gson.save();
                } else {
                    Bukkit.getScheduler().runTaskAsynchronously(Economy.getEconomy(), () -> mysql.queryUpdate(mysql.newPreparedStatement("update WEconomy set money='" + getMoney() + "' where uuid='"+uuid+"'")));
                }
                Storage.Magnata.setMagnata(Storage.Magnata.getTop());
                return new MultiValue<>(true, money);
            }
        }
        return new MultiValue<>(false, 0D);
    }

    public MultiValue<Boolean, Double> setMoney(BigDecimal money, boolean eventStarted) {
        return setMoney(money.doubleValue(), eventStarted);
    }

    public MultiValue<Boolean, Double> setMoney(double money) {
        return setMoney(money, false);
    }

    public MultiValue<Boolean, Double> setMoney(BigDecimal money) {
        return setMoney(money.doubleValue(), false);
    }

    public MultiValue<Boolean, Double> addMoney(double money) {
        EconomyAccountEvent e = new EconomyAccountEvent(ChangeType.ADDING, this, money);
        if (!e.isCancelled()) {
            return setMoney(Math.abs(getMoney() + money), true);
        }
        return new MultiValue<>(false, 0D);
    }

    public MultiValue<Boolean, Double> addMoney(BigDecimal money) {
        return addMoney(money.doubleValue());
    }

    public MultiValue<Boolean, Double> removeMoney(double money) {
        if (hasMoney(money)) {
            EconomyAccountEvent e = new EconomyAccountEvent(ChangeType.REMOVING, this, money);
            if (!e.isCancelled()) {
                return setMoney(Math.abs(getMoney() - money), true);
            }
        }
        return new MultiValue<>(false, 0D);
    }

    public MultiValue<Boolean, Double> removeMoney(BigDecimal money) {
        return removeMoney(money.doubleValue());
    }

    public boolean hasMoney(double money) {
        return getMoney() >= money;
    }

    public boolean hasMoney(BigDecimal money) {
        return hasMoney(money.doubleValue());
    }

    public boolean setAccountIsPlayer() {
        if (!isPlayer) {
            isPlayer = true;
            if (!config.isMysql()) {
                gson.put("isplayer", isPlayer);
                gson.save();
            }else{
                Bukkit.getScheduler().runTaskAsynchronously(Economy.getEconomy(), () -> mysql.queryUpdate(mysql.newPreparedStatement("update WEconomy set isplayer='" + true + "' where uuid='" + uuid + "'")));
            }
            return true;
        }
        return false;
    }

    public boolean createAccount() {
        if (!exists()) {
            EconomyAccountEvent e = new EconomyAccountEvent(ChangeType.CREATING, this, 0);
            if (!e.isCancelled()) {
                money = 0;
                created = true;
                if (!config.isMysql()) {
                    gson.put("money", money);
                    gson.put("created", created);
                    gson.save();
                }else{
                    Bukkit.getScheduler().runTaskAsynchronously(Economy.getEconomy(), () -> mysql.queryUpdate(mysql.newPreparedStatement("insert into WEconomy(uuid, money, created, isplayer) values('" + uuid + "', '" + money + "', '" + created + "' ,'" + false + "')")));
                }
                return true;
            }
        }
        return false;
    }

    public boolean deleteAccount() {
        if (exists()) {
            EconomyAccountEvent e = new EconomyAccountEvent(ChangeType.DELLETING, this, 0);
            if (!e.isCancelled()) {
                unLoad();
                if (!config.isMysql()) {
                    gson.delete();
                } else {
                    Bukkit.getScheduler().runTaskAsynchronously(Economy.getEconomy(), () -> mysql.queryUpdate(mysql.newPreparedStatement("delete from WEconomy where uuid='" + uuid + "'")));
                }
                return true;
            }
        }
        return false;
    }

    public boolean unLoad() {
        if (exists()) {
            EconomyAccountEvent e = new EconomyAccountEvent(ChangeType.UNLOADING, this, 0);
            if (!e.isCancelled()) {
                if (Storage.Accounts != null && !Storage.Accounts.isEmpty()) {
                    if (Storage.Accounts.containsKey(uuid)) {
                        Storage.Accounts.remove(uuid);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean load() {
        if (exists()) {
            EconomyAccountEvent e = new EconomyAccountEvent(ChangeType.LOADING, this, 0);
            if (!e.isCancelled()) {
                if (Storage.Accounts != null && !Storage.Accounts.containsKey(uuid)) {
                    Storage.Accounts.put(uuid, this);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean resetAccount() {
        EconomyAccountEvent e = new EconomyAccountEvent(ChangeType.RESETING, this, 0);
        if (!e.isCancelled()) {
            return setMoney(0, false).getOne();
        }
        return false;
    }

    public boolean exists() {
        return created;
    }

    public boolean isOwnedByPlayer() {
        return isPlayer;
    }

    public UUID getUUID() {
        return uuid;
    }

    public GsonManager getStorage() {
        return gson;
    }

    public MultiValue<Player, OfflinePlayer> getPlayer() {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            return new MultiValue<>(player, player);
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            if (offlinePlayer != null) {
                return new MultiValue<>(null, offlinePlayer);
            }
            return null;
        }
    }

    public boolean isMagnata() {
        return Storage.Magnata.isMagnata(this);
    }

    public int compareTo(Account comparable) {
        if (getMoney() < comparable.getMoney()) {
            return 1;
        }
        if (getMoney() > comparable.getMoney()) {
            return -1;
        }
        return 0;
    }

}
