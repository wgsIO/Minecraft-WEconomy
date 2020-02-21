package dev.walk.economy;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import dev.walk.economy.Manager.*;
import dev.walk.economy.Util.Cooldown;
import dev.walk.economy.Util.EconomyConnection;
import dev.walk.economy.Util.EconomyUtils;
import dev.walk.economy.Util.PlaceholderExpansionHook;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class Economy extends JavaPlugin {

    private static Economy economy;
    public static boolean pex = false;
    public static Economy getEconomy() { return economy; }
    public static MySql mysql;

    private EconomyUtils utils = new EconomyUtils();

    @Override
    public void onEnable() {

        utils.println("", "&7--------------------- &b[ CloudEconomy ] &7---------------------",
               "&7Plugin ativado, caso de algum bug favor reportar... ",
               "&7envie arquivos da pasta: &eCloudEconomy/Logs/*",
                "&7--------------------- &b[ CloudEconomy ] &7---------------------", "");
        //utils.println("", "&7--------------------- &b[ WEconomy ] &a(BR) &7---------------------",
        //        "&7Plugin ativado, caso de algum bug favor reportar... ",
        //        "&7envie arquivos da pasta: &fWEconomy/Logs/*",
        //        "&7--------------------- &b[ WEconomy ] &a(BR) &7---------------------", "");
        //utils.println("", "&7--------------------- &b[ WEconomy ] &a(US) &7---------------------",
        //        "&7Plugin enabled, case of any bug please report... ",
        //        "&7send files from the folder: &fWEconomy/Logs/*",
        //        "&7--------------------- &b[ WEconomy ] &a(US) &7---------------------", "");
        economy = this;
        Storage.config = new ConfigPlugin();
        Storage.config.loadConfigs();
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        try {
            getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new EconomyConnection(), this, ServicePriority.Highest);
        } catch (NoClassDefFoundError e) {
            utils.println(Storage.config.getMessageNotVault());
        }
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderExpansionHook().register();
        } else {
            utils.println(Storage.config.getMessageNotPlaceHolder());
        }
        if (getServer().getPluginManager().isPluginEnabled("Legendchat")) {
            getServer().getPluginManager().registerEvents(new LegendChat(), this);
        }else{
            utils.println(Storage.config.getMessageNotLegendChat());
        }
        if (getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
           pex = true;
        }else{
            utils.println("&cPlugin de permissão não encontrado.");
        }
        Storage.Log = new Log();
        if (Storage.config.isMysql()){
            mysql = new MySql(Storage.config.getHostMysql(), Storage.config.getPortMysql(), Storage.config.getDatabaseMysql(),Storage.config.getUserMysql(), Storage.config.getPasswordMysql());
            if (!mysql.openConnection()){
                utils.println("&bWEconomy: &cFailed to connect MySql.");
            }else{
                try {
                    mysql.queryUpdate(mysql.newPreparedStatement("create table if not exists WEconomy(uuid varchar(255) primary key, money text, created text, isplayer text)"));
                    utils.println("&bWEconomy: &aMysql connected.");
                } catch (Exception e) { utils.println("&cCould not connect to table, starting in normal mode.");}
            }
        }
        utils.loadAccounts();
        if (getServer().getPluginManager().isPluginEnabled("HolographicDisplays")) {
            Storage.npcHD = true;
        }
        if (getServer().getPluginManager().isPluginEnabled("Citizens")) {
            try { Npc.updateNpcs(); } catch (Exception e) {}
            getServer().getScheduler().runTaskTimer(this, () -> {
                try{ Npc.updateNpcs(); } catch (Exception e) {}
            }, 20, 20*60*2);
        }
    }

    class LegendChat implements Listener{

        @EventHandler
        private void onChat(ChatMessageEvent e) {
            Account account = new AccountManager(e.getSender()).getInstance();
            e.setTagValue("WMagnataTag",(account.isMagnata() ? Storage.Magnata.getMagnataTag() : ""));
            e.setTagValue("WMoney",utils.formatMoney(account.getMoney()));
        }

    }


    public class Log extends Cooldown {

        GsonManager gson;
        int size = 0;

        public Log() {
            gson = new GsonManager(Economy.getEconomy().getDataFolder() + "/Logs", "log-" + getDateActual()).prepareGson();
            if (gson.contains("size")) {
                size = gson.get("size").asInt();
            }
        }

        public void addLog(OfflinePlayer player, String text) {
            size++;
            gson.put("size", size);
            gson.put(size + "", "[" + getDate() + "]: " + text.replace("<player>", player.getName()));
            gson.save();
        }

        public void addLog(CommandSender sender, String text) {
            size++;
            gson.put("size", size);
            gson.put(size + "", "[" + getDate() + "]: " + text.replace("<player>", sender.getName()));
            gson.save();
        }

        public void removeLog(int id) {
            gson.put(id + "", (Object) null);
            gson.save();
        }

    }

}
