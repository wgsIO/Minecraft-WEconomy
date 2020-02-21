package dev.walk.economy;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import dev.walk.economy.Events.MagnataChangeEvent;
import dev.walk.economy.Manager.*;
import dev.walk.economy.Util.ActionBar;
import dev.walk.economy.Util.EconomyUtils;
import dev.walk.economy.Util.MultiValue;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Iterator;

class Listeners implements Listener {

    EconomyUtils utils = new EconomyUtils();

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Account account = new AccountManager(e.getPlayer().getUniqueId()).getInstance();
        account.createAccount();
        account.setAccountIsPlayer();
        Magnata magnata = Storage.Magnata;
        if (!magnata.hasMagnata()) {
            magnata.setMagnata(account);
        }
        Storage.Log.addLog((CommandSender) e.getPlayer(), "&e<player> entrou no servidor, tem " + utils.formatMoney(account.getMoney()) + " coins em sua conta");
    }

    @EventHandler
    private void changeMagnata(MagnataChangeEvent e) {
        Iterator<? extends Player> players = Economy.getEconomy().getServer().getOnlinePlayers().iterator();
        MultiValue<Player, OfflinePlayer> magnata = e.getNewMagnata().getPlayer();
        String magnataPlayer = (magnata.getOne() != null ? magnata.getOne().getName() : magnata.getTwo().getName());
        String message = Storage.config.getMessageChangedRichest().replace("<jogador>", magnataPlayer).replace("<player>", magnataPlayer).replace("<jugador>", magnataPlayer);
        while (players.hasNext()) {
            Player player = players.next();
            if (player.getUniqueId().equals(e.getNewMagnata().getUUID())) {
                ActionBar.sendActionbar(player, Storage.config.getMessageYourIsRichest());
            } else {
                ActionBar.sendActionbar(player, message);
            }
        }
        new EconomyUtils().println("&e" + message);
    }

}
