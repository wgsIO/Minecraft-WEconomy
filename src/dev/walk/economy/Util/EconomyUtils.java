package dev.walk.economy.Util;

import dev.walk.economy.Economy;
import dev.walk.economy.Manager.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

public class EconomyUtils {

    public enum TypeRedond{ CEIL, FLOOR }

    public String formatMoney(double money){
        DecimalFormat f; String sm;
        if (!Storage.config.isMoneyExact()) {
            if (money < 1000.0) { return ((int) money) + ",00"; }
            f = new DecimalFormat("#,###.00");
            sm = f.format(money);
        }else{
            sm = String.format("%1$,.2f", doubleRedond(money, 2, TypeRedond.CEIL));
        }
        return sm;
    }

    public void println(String... message){
        Iterator<String> i = Arrays.asList(message).iterator();
        while(i.hasNext()){
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', i.next()));
        }
    }

    public int parseInt(String number) { try{ int value = Integer.parseInt(number);return value; } catch ( Exception e ) {}return -1; }
    public MultiValue<Long, Double> parse(String number) {
        if (!Storage.config.isMoneyExact()) { try { Long value = Long.parseLong(number); return new MultiValue<>(value, value.doubleValue()); } catch (Exception e) { }
        } else { try { Double value = Double.parseDouble(number);return new MultiValue<>(value.longValue(), value); } catch (Exception e1) {} }
        return new MultiValue<>(-1L, -1D);
    }
    public Double doubleRedond(Double value, int slots, TypeRedond type){
        double arredond = value;
        arredond *= (Math.pow(10, slots));
        switch (type) {
            case CEIL:
                arredond = Math.ceil(arredond);
            case FLOOR:
                arredond = Math.floor(arredond);
        }
        arredond /= (Math.pow(10, slots));
        return arredond;
    }



    public long getMaxCoins() {
        return Storage.config.getMaxMoney();
    }

    public void loadAccounts() {
        if (!Storage.config.isMysql()) {
            try {
                GsonManager gson = new GsonManager(Economy.getEconomy().getDataFolder() + "/Accounts", "");
                File[] files = gson.getFiles();
                if (files != null) {
                    Arrays.asList(files).forEach(account -> {
                        UUID uuid = UUID.fromString(account.getName().replace(".json", ""));
                        new AccountManager(uuid).getInstance();
                    });
                    Storage.Magnata.setMagnata(Storage.Magnata.getTop());
                }
            } catch (Exception e) {
                Iterator<Account> accounts = Storage.Magnata.getTop(0, Storage.config.getSizeMoneyTop()).iterator();
                if (accounts.hasNext()) { Storage.Magnata.setMagnata(accounts.next()); }
            }
        }else{
            Bukkit.getScheduler().runTaskAsynchronously(Economy.getEconomy(), () -> {
                try {
                    PreparedStatement ps = Economy.mysql.newPreparedStatement("select uuid from WEconomy");
                    ResultSet rs = Economy.mysql.query(ps);
                    while (rs.next()) {
                        UUID uuid = UUID.fromString(rs.getString("uuid"));
                        new AccountManager(uuid).getInstance();
                    }
                    Storage.Magnata.setMagnata(Storage.Magnata.getTop());
                } catch (Exception e) {}
            });
        }
    }


}
