package dev.walk.economy;

import dev.walk.economy.Manager.*;
import dev.walk.economy.Util.EconomyUtils;
import dev.walk.economy.Util.MultiValue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.Arrays;
import java.util.Iterator;

@SuppressWarnings("deprecation")
public class MoneyCommand {

    private EconomyUtils util = new EconomyUtils();

    private CommandSender sender;
    private Player player;
    private String[] args;
    private ConfigPlugin conf = Storage.config;
    private String command;

    public MoneyCommand(String command,CommandSender sender, String[] args){
        this.sender = sender;
        this.args = args;
        this.command = command;
        player = Bukkit.getPlayer(sender.getName());
        perform();
    }

    public String argAt(int index){return args[index]; }
    public boolean isArgAtIgnoreCase(int index, String argument){ return args[index].equalsIgnoreCase(argument); }
    public Player getPlayerArgumentAt(int index){ return Bukkit.getPlayer(args[index]); }
    public OfflinePlayer getOfflinePlayerArgumentAt(int index){ return Bukkit.getOfflinePlayer(args[index]); }
    public boolean isPlayer(){ return player != null; }
    public void sendMessage(String... message){ sendMessage(sender, message); }
    public void sendMessage(CommandSender sender, String... message){
        Iterator<String> i = Arrays.asList(message).iterator();
        while(i.hasNext()){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', i.next()));
        }
    }

    public void perform() {
        if (args.length > 0) {
            if (conf.hasSubView(argAt(0))) {
                if (sender.hasPermission(conf.getPermissionAtViewCommand())) {
                    if (args.length >= 2) {
                        viewMoney(getOfflinePlayerArgumentAt(1), true);
                    } else {
                        sendMessage(conf.getMessageHowToUseViewCommand());
                    }
                }else{
                    sendMessage(conf.getNoPermissionToUseCommand("'" + command + " " + argAt(0)+"'"));
                }
            } else if (isPlayer() && conf.hasSubSend(argAt(0))) {
                if (sender.hasPermission(conf.getPermissionAtSendCommand())) {
                    if (args.length >= 3) {
                        MultiValue<Long, Double> value = util.parse(argAt(2));
                        double money = (Storage.config.isMoneyExact() ? value.getTwo() : value.getOne());
                        sendMoney(getOfflinePlayerArgumentAt(1), money);
                    } else {
                        sendMessage(conf.getMessageHowToUseSendCommand());
                    }
                }else{
                    sendMessage(conf.getNoPermissionToUseCommand("'" + command + " " + argAt(0)+"'"));
                }
            } else if (isPlayer() && conf.hasSubNpc(argAt(0))) {
                if (sender.hasPermission(conf.getPermissionAtSetNpcCommand())){
                    if (args.length >= 2) {
                        int i = util.parseInt(argAt(1));
                        setNpc(i);
                    }
                }else{
                    sendMessage(conf.getNoPermissionToUseCommand("'" + command + " " + argAt(0)+"'"));
                }
            } else if (conf.hasSubHelp(argAt(0))) {
                help();
            } else if (conf.hasSubTop(argAt(0))) {
                if (sender.hasPermission(conf.getPermissionAtViewMoneyTopCommand())) {
                    moneyTop();
                }else{
                    sendMessage(conf.getNoPermissionToUseCommand("'" + command + " " + argAt(0)+"'"));
                }
            } else if (conf.hasSubAdd(argAt(0))) {
                if (sender.hasPermission(conf.getPermissionAtAddCommand())) {
                    if (args.length >= 3) {
                        MultiValue<Long, Double> value = util.parse(argAt(2));
                        double money = (Storage.config.isMoneyExact() ? value.getTwo() : value.getOne());
                        addMoney(getOfflinePlayerArgumentAt(1), money);
                    } else {
                        sendMessage(conf.getMessageHowToAddCommand());
                    }
                }else{
                    sendMessage(conf.getNoPermissionToUseCommand("'" + command + " " + argAt(0)+"'"));
                }
            } else if (conf.hasSubRemove(argAt(0))) {
                if (sender.hasPermission(conf.getPermissionAtRemoveCommand())) {
                    if (args.length >= 3) {
                        MultiValue<Long, Double> value = util.parse(argAt(2));
                        double money = (Storage.config.isMoneyExact() ? value.getTwo() : value.getOne());
                        removeMoney(getOfflinePlayerArgumentAt(1), money);
                    } else {
                        sendMessage(conf.getMessageHowToUseRemoveCommand());
                    }
                }else{
                    sendMessage(conf.getNoPermissionToUseCommand("'" + command + " " + argAt(0) + "'"));
                }
            } else if (conf.hasSubDefine(argAt(0))) {
                if (sender.hasPermission(conf.getPermissionAtDefineCommand())) {
                    if (args.length >= 3) {
                        MultiValue<Long, Double> value = util.parse(argAt(2));
                        double money = (Storage.config.isMoneyExact() ? value.getTwo() : value.getOne());
                        setMoney(getOfflinePlayerArgumentAt(1), money);
                    } else {
                        sendMessage(conf.getMessageHowToUseDefineCommand());
                    }
                }else{
                    sendMessage(conf.getNoPermissionToUseCommand("'" + command + " " + argAt(0) + "'"));
                }
            } else if (conf.hasSubReset(argAt(0))) {
                if (sender.hasPermission(conf.getPermissionAtResetCommand())) {
                    if (args.length >= 2) {
                        resetMoney(getOfflinePlayerArgumentAt(1));
                    } else {
                        sendMessage(conf.getMessageHowToUseResetCommand());
                    }
                }else{
                    sendMessage(conf.getNoPermissionToUseCommand("'" + command + " " + argAt(0) + "'"));
                }
            } else {
                help();
            }
        } else {
            if (isPlayer()) {
                viewMoney(null, false);
            } else {
                help();
            }
        }
    }

    private void viewMoney(OfflinePlayer target, boolean viewPlayer) {
        if (viewPlayer) {
            if (target.hasPlayedBefore() || new AccountManager(target.getUniqueId()).getInstance().isOwnedByPlayer()) {
                Account account = new AccountManager(target.getUniqueId()).getInstance();
                double money = account.getMoney();
                sendMessage(conf.getMoneyAtMessage(target.getName(), util.formatMoney(money)));
            } else {
                sendMessage(conf.getPlayerNotExistsMessage());
            }
        } else {
            Account account = new AccountManager(player.getUniqueId()).getInstance();
            double money = 0;
            try{ money = account.getMoney(); } catch (Exception e) { account.setMoney(0, true); }
            sendMessage(conf.getMyMoneyMessage(util.formatMoney(money)));
        }
    }

    private void setMoney(OfflinePlayer target, double money) {
        if (target.hasPlayedBefore() || new AccountManager(target.getUniqueId()).getInstance().isOwnedByPlayer()) {
            if (money <= -1) {
                sendMessage(conf.getAmountInvalidMessage());
            } else {
                Account account = new AccountManager(target.getUniqueId()).getInstance();
                account.setAccountIsPlayer();
                if (money > util.getMaxCoins()) money = util.getMaxCoins();
                account.setMoney(money, false);
                sendMessage(conf.getDefinedMoneyAtMessage(target.getName(), util.formatMoney(money)));
                if (target.isOnline()) {
                    sendMessage(Bukkit.getPlayer(target.getUniqueId()), conf.getDefinedInYourAccountMessage(player.getName(), util.formatMoney(money)));
                }
                Storage.Log.addLog(sender, "<player> definiu " + util.formatMoney(money) + " na conta de " + target.getName());
            }
        } else {
            sendMessage(conf.getPlayerNotExistsMessage());
        }
    }

    private void addMoney(OfflinePlayer target, double money) {
        if (target.hasPlayedBefore() || new AccountManager(target.getUniqueId()).getInstance().isOwnedByPlayer()) {
            if (money <= 0) {
                sendMessage(conf.getAmountInvalidMessage());
            } else {
                Account account = new AccountManager(target.getUniqueId()).getInstance();
                account.setAccountIsPlayer();
                if (account.getMoney() >= util.getMaxCoins()) {
                    sendMessage(conf.getPlayerMaxMoneyMessage());
                } else {
                    if (money > util.getMaxCoins()) money = util.getMaxCoins();
                    account.addMoney(money);
                    sendMessage(conf.getAddedMoneyAtMessage(target.getName(), util.formatMoney(money)));
                    if (target.isOnline()) {
                        sendMessage(Bukkit.getPlayer(target.getUniqueId()), conf.getAddedInYourAccountMessage(player.getName(), util.formatMoney(money)));
                    }
                    Storage.Log.addLog(sender, "<player> adicionou " + util.formatMoney(money) + " na conta de " + target.getName());
                }
            }
        } else {
            sendMessage(conf.getPlayerNotExistsMessage());
        }
    }

    private void removeMoney(OfflinePlayer target, double money) {
        if (target.hasPlayedBefore() || new AccountManager(target.getUniqueId()).getInstance().isOwnedByPlayer()) {
            if (money <= 0) {
                sendMessage(conf.getAmountInvalidMessage());
            } else {
                Account account = new AccountManager(target.getUniqueId()).getInstance();
                account.setAccountIsPlayer();
                if (money > account.getMoney()) {
                    money = account.getMoney();
                }
                if (account.getMoney() > 0) {
                    account.removeMoney(money);
                    sendMessage(conf.getRemovedMoneyAtMessage(target.getName(), util.formatMoney(money)));
                    if (target.isOnline()) {
                        sendMessage(Bukkit.getPlayer(target.getUniqueId()), conf.getRemovedInYourAccountMessage(player.getName(), util.formatMoney(money)));
                    }
                    Storage.Log.addLog(sender, "<player> removeu " + util.formatMoney(money) + " na conta de " + target.getName());
                } else {
                    sendMessage(conf.getPlayerNoHasMoney());
                }
            }
        } else {
            sendMessage(conf.getPlayerNotExistsMessage());
        }
    }

    private void resetMoney(OfflinePlayer target) {
        if (target.hasPlayedBefore() || new AccountManager(target.getUniqueId()).getInstance().isOwnedByPlayer()) {
            Account account = new AccountManager(target.getUniqueId()).getInstance();
            account.setAccountIsPlayer();
            if (account.getMoney() >= 1) {
                account.resetAccount();
                sendMessage(conf.getResetedMoneyAtMessage(target.getName()));
                if (target.isOnline()) {
                    sendMessage(Bukkit.getPlayer(target.getUniqueId()), conf.getResetedMoneyYourMessage(player.getName()));
                }
                Storage.Log.addLog(sender, "<player> resetou a conta de " + target.getName());
            } else {
                sendMessage(conf.getNoReasonToResetMoneyMessage());
            }
        } else {
            sendMessage(conf.getPlayerNotExistsMessage());
        }
    }

    private void sendMoney(OfflinePlayer target, double money) {
        if (target.hasPlayedBefore() || new AccountManager(target.getUniqueId()).getInstance().isOwnedByPlayer()) {
            if (target.getName().toLowerCase().equals(sender.getName().toLowerCase())){
                sendMessage(conf.getMessageNoSendToYour());
            }else if (money <= 0) {
                sendMessage(conf.getAmountInvalidMessage());
            } else {
                Account your = new AccountManager(player.getUniqueId()).getInstance();
                Account account = new AccountManager(target.getUniqueId()).getInstance();
                account.setAccountIsPlayer();
                if (money > util.getMaxCoins()) money = util.getMaxCoins();
                if (account.getMoney() >= util.getMaxCoins()) {
                    sendMessage(conf.getPlayerMaxMoneyMessage());
                } else {
                    if (your.getMoney() >= money) {
                        your.removeMoney(money);
                        account.addMoney(money);
                        sendMessage(conf.getYourSendMoneyAtMessage(target.getName(), util.formatMoney(money)));
                        Storage.Log.addLog(sender, "<player> enviou " + util.formatMoney(money) + " para " + target.getName());
                        if (target.isOnline()) {
                            sendMessage(Bukkit.getPlayer(target.getUniqueId()), conf.getYourReceivedMoneyMessage(player.getName(), util.formatMoney(money)));
                        }
                    } else {
                        sendMessage(conf.getYourNoHaveMoneyMessage());
                    }
                }
            }
        } else {
            sendMessage(conf.getPlayerNotExistsMessage());
        }
    }

    private void moneyTop() {
        Iterator<Account> accounts = Storage.Magnata.getTop(0, Storage.config.getSizeMoneyTop()).iterator();
        int rank = 1;
        if (accounts.hasNext()) {
            sendMessage(conf.getMoneyTop10Message(), "");
            while (accounts.hasNext()) {
                Account account = accounts.next();
                double money = account.getMoney();
                MultiValue<Player, OfflinePlayer> player = account.getPlayer();
                OfflinePlayer offline = (player.getOne() != null ? player.getOne() : player.getTwo());
                String prefix = ""; String suffix = "";
                if (Economy.pex){
                    prefix = PermissionsEx.getUser(offline.getName()).getPrefix();
                    suffix = PermissionsEx.getUser(offline.getName()).getPrefix();
                }
                sendMessage(conf.getTypeMoneyTop10((player.getOne() != null ? player.getOne().getName() : player.getTwo().getName()), util.formatMoney(money))
                        .replace("<prefix>", prefix)
                        .replace("<suffix>", suffix)
                        .replace("<magnata>", (rank == 1 ? Storage.Magnata.getMagnataTag() + " " : ""))
                        .replace("<rank>", (rank == 1 ? "&b" + rank : (rank == 2 ? "&e" + rank : (rank == 3 ? "&f" + rank : "&7" + rank))))
                );
                rank++;
            }
        } else {
            sendMessage(conf.getNotHavePlayerInTop10Message());
        }
    }

    private void setNpc(int rank){
        if (rank >= 1 && rank <= Storage.config.getSizeMoneyTop()){
            if (Npc.exists(rank)){ Npc.destroyNpc(rank); }
            Npc.setNpc(rank, player.getLocation());
            sendMessage("&aNpc adicionado com sucesso.");
        }else{
            sendMessage("&cO rank digitado é inválido.");
        }
    }

    private void removeNpc(int rank){
        if (rank >= 1 && rank <= Storage.config.getSizeMoneyTop()){
            Npc.destroyNpc(rank);
            sendMessage("&aNpc removido com sucesso.");
        }else{
            sendMessage("&cO rank digitado é inválido.");
        }
    }

    private void help() {
        sendMessage(
                conf.getHelpTitle()
                , ""
                , (sender.hasPermission(conf.getPermissionAtViewCommand()) ? "&a* " : "&c* ") +
                        conf.getHelpView()
                , (sender.hasPermission(conf.getPermissionAtSendCommand()) ? "&a* " : "&c* ") +
                        conf.getHelpSend()
                , (sender.hasPermission(conf.getPermissionAtViewMoneyTopCommand()) ? "&a* " : "&c* ") +
                        conf.getHelpTop10()
                , (sender.hasPermission(conf.getPermissionAtDefineCommand()) ? "&a* " : "&c* ") +
                        conf.getHelpDefine()
                , (sender.hasPermission(conf.getPermissionAtAddCommand()) ? "&a* " : "&c* ") +
                        conf.getHelpAdd()
                , (sender.hasPermission(conf.getPermissionAtRemoveCommand()) ? "&a* " : "&c* ") +
                        conf.getHelpRemove()
                , (sender.hasPermission(conf.getPermissionAtResetCommand()) ? "&a* " : "&c* ") +
                        conf.getHelpReset()
                , "&a* " +
                        conf.getHelpHelp()
                , conf.getHelpLegend(),""
        );
    }

}
