package dev.walk.economy.Manager;

import dev.walk.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class AccountManager {

    private UUID uuid;

    public AccountManager(UUID uuid) {
        this.uuid = uuid;
    }
    public AccountManager(Player player) { uuid = player.getUniqueId(); }
    public AccountManager(OfflinePlayer offlinePlayer) {
        uuid = offlinePlayer.getUniqueId();
    }
    @SuppressWarnings("deprecation") public AccountManager(String player) { uuid = Bukkit.getOfflinePlayer(player).getUniqueId(); }

    public Account getInstance() {
        MySql mysql = Economy.mysql;
        if (!Storage.Accounts.containsKey(uuid)) {
            Account account;
            boolean exists = false;
            GsonManager gson = null;
            PreparedStatement ps = null; ResultSet rs = null;
            if (!Storage.config.isMysql()){
                gson = new GsonManager(Economy.getEconomy().getDataFolder() + "/Accounts", uuid.toString());
                exists = gson.exists();
            }else{
                try{
                    ps = mysql.newPreparedStatement("select money, created, isplayer from WEconomy where uuid='" + uuid + "'");
                    rs = mysql.query(ps);
                    if (rs.next()){
                        exists = true;
                    }
                } catch (Exception e) {}
            }
            if (exists) {
                Double money = 0D;
                boolean isPlayer = false, created = false;
                if (!Storage.config.isMysql()) {
                    gson.prepareGson();
                    money = gson.get("money").asDouble();
                    isPlayer = gson.get("isplayer").asBoolean();
                    created = gson.get("created").asBoolean();
                }else{
                    try{
                        money = rs.getDouble("money");
                        isPlayer = rs.getBoolean("isplayer");
                        created = rs.getBoolean("created");
                        ps.close();
                    } catch (Exception e) {}
                }
                account = new Account(uuid, (Storage.config.isMoneyExact() ? money : money.longValue()), isPlayer, created);
            } else {
                account = new Account(uuid);
                account.createAccount();
            }
            Storage.Accounts.put(uuid, account);
        }
        return Storage.Accounts.get(uuid);
    }

}
