package dev.walk.economy.Manager;

import dev.walk.economy.Economy;

import java.util.List;

public class ConfigPlugin {

    private boolean moneyExact = true;

    private String NotVault = "&4Nao foi possivel estabelecer conexao com o VAULT. -> &8(Plugins que usa VAULT nao ira funcionar)"
    , NotPlaceHolder = "&4Nao foi possivel estabelecer conexao com o PlaceHolder (Nao conseguira usar as placeholder)."
    , NotLegendChat = "&4Nao foi possivel estabelecer conexao com o LegendChat (Use um chat próprio ou um com placeholder)."
    , YourIsRichest = "&eVocê tornou-se o(a) mais rico(a) do servidor"
    , ChangedRichest = "&e<jogador> tornou-se o(a) mais rico(a) do servidor"
    , noSendToYour = "&cVocê não pode enviar dinheiro para sí mesmo."

    , howToUseViewCommand = "&cUso: &7/dinheiro ver <jogador>"
    , howToUseSendCommand = "&cUso: &7/dinheiro enviar <jogador> <quantidade>"
    , howToUseAddCommand = "&cUso: &7/dinheiro adicionar <jogador> <quantidade>"
    , howToUseRemoveCommand = "&cUso: &7/dinheiro remover <jogador> <quantidade>"
    , howToUseDefineCommand = "&cUso: &7/dinheiro definir <jogador> <quantidade>"
    , howToUseResetCommand = "&cUso: &7/dinheiro resetar <jogador>"

    , permissionsAtView = "economy.viewmoney"
    , permissionsAtSend = "economy.sendmoney"
    , permissionsAtViewMoneyTop = "economy.viewmoneytop"
    , permissionsAtAdd = "economy.addmoney"
    , permissionsAtRemove = "economy.removemoney"
    , permissionsAtDefine = "economy.definemoney"
    , permissionsAtReset = "economy.resetmoney"
    , permissionsAtNpc = "economy.setnpc"

    , NoPermissionToUseCommand = "&cVocê não tem permissão para usar o comando <comando>, consulte um staff."
    , amountInvalid = "&cQuantidade inválida."
    , playerNotExists = "&cEste jogador não existe."

    , myMoney = "&eSeu dinheiro: &b<dinheiro>"
    , moneyAt = "&eDinheiro de &b<jogador>&e: &b<dinheiro>"
    , definedMoneyAt = "&eVocê definiu &b<dinheiro> &ena conta de &b<jogador>&e."
    , playerMaxMoney = "&cEste jogador já antigiu o máximo de dinheiro."
    , moneyAddedAt = "&eVocê adicionou &b<dinheiro>&e na conta de &e<jogador>&e."
    , moneyRemovedAt = "&eVocê removeu &b<dinheiro>&e da conta de &b<jogador>&e."
    , noMoney = "&cEste jogador não tem dinheiro em sua conta."
    , moneyResetedAt = "&eVocê resetou a conta de &b<jogador>&e."
    , noReasonToResetAt = "&cNão há porquê porque resetar esta conta, ele não tem dinheiro."
    , yourNoHaveMoney = "&cVocê não tem dinheiro suficiente para enviar para outro jogador."
    , yourSendAt = "&eVocê enviou &b<dinheiro>&e para a conta de &b<jogador>&e."
    , yourReceived = "&b<jogador> &eenviou &b<dinheiro> &epara sua conta."

    , moneyTop10 = "       &a&lTOP 10 MAIS RICOS DO SERVIDOR"
    , notHavePlayerInTop10 = "&cNão há nenhum jogador no &f'TOP 10 MAIS RICOS DO SERVIDOR'&c."
    , typeMoneyTop10 = "<rank>° <magnata>&f<jogador> &7- &f<dinheiro>"
    , magnataTag = "&2[Magnata]"

    , helpTitle = "             &eComandos disponíveis"
    , helpView = "&edinheiro ver &b- &epara ver money de um jogador."
    , helpSend = "&edinheiro enviar &b- &epara enviar dinheiro para um jogador."
    , helpTop10 = "&edinheiro ricos &b- &epara ver os 10 jogadores mais ricos."
    , helpDefine = "&edinheiro definir &b- &epara definir money de um jogador."
    , helpAdd = "&edinheiro adicionar &b- &epara dar money para um jogador."
    , helpRemove = "&edinheiro remover &b- &epara remover money de um jogador."
    , helpReset = "&edinheiro resetar &b- &epara resetar os money de um jogador."
    , helpHelp= "&edinheiro ajuda &b- &epara ver os comandos."
    , helpLegend = "&8( '&a*&8'&a = Com permissao &7: &8'&c*&8'&c = Sem permissao )"

    , yourMoneydefined = "&eSeu dinheiro foi definido para &b<dinheiro> &epor &b<jogador>&e."
    , yourMoneyadded = "&eFoi adicionado &b<dinheiro> &eem sua conta por &b<jogador>&e."
    , yourMoneyremoved = "&eFoi removido &b<dinheiro> &ede sua conta por &b<jogador>&e."
    , yourMoneyreseted = "&eSeu dinheiro foi resetado por &b<jogador>&e.";

    private List<String> commands

    , sub_view
    , sub_send
    , sub_help
    , sub_top
    , sub_add
    , sub_remove
    , sub_define
    , sub_reset
    , sub_npc;

    private Long MaxMoney = 999999999999999999L;
    private Integer MoneyTopSize = 10;

    //TODO:MYSQL CONFIG
    private boolean isMysql = false;
    private String Host = "localhost";
    private int Port = 3306;
    private String Database = "example";
    private String User = "root";
    private String Password = "";


    private ConfigManagerFile conf = new ConfigManagerFile(Economy.getEconomy(), "configuration.yml");

    public void save(){

        boolean exists = conf.exists();
        conf.prepare();
        if (!exists) {
            conf.save("template");
        }

    }

    public void loadConfigs() {

        try {

            save();

            //TODO:MYSQL
            isMysql = conf.getConfig().getBoolean("Options.MySql.Enabled");
            if (isMysql) {
                Host = conf.getConfig().getString("Options.MySql.Host");
                Port = conf.getConfig().getInt("Options.MySql.Port");
                Database = conf.getConfig().getString("Options.MySql.Database");
                User = conf.getConfig().getString("Options.MySql.User");
                Password = conf.getConfig().getString("Options.MySql.Password");
            }
            //TODO:MYSQL

            moneyExact = conf.getConfig().getBoolean("Options.MoneyExactMode");

            NotVault = conf.getConfig().getString("Message.VaultNotFound");

            YourIsRichest = conf.getConfig().getString("Message.YouBecameRichest");
            ChangedRichest = conf.getConfig().getString("Message.PlayerBecameRichest");
            noSendToYour = conf.getConfig().getString("Message.NoSendToYou");

            howToUseViewCommand = conf.getConfig().getString("Message.HowUseCommandView");
            howToUseSendCommand = conf.getConfig().getString("Message.HowUseCommandSend");
            howToUseDefineCommand = conf.getConfig().getString("Message.HowUseCommandDefine");
            howToUseAddCommand = conf.getConfig().getString("Message.HowUseCommandAdd");
            howToUseRemoveCommand = conf.getConfig().getString("Message.HowUseCommandRemove");
            howToUseResetCommand = conf.getConfig().getString("Message.HowUseCommandReset");

            NoPermissionToUseCommand = conf.getConfig().getString("Message.NoPermission");
            amountInvalid = conf.getConfig().getString("Message.AmountInvalid");
            playerNotExists = conf.getConfig().getString("Message.PlayerNotExists");

            playerMaxMoney = conf.getConfig().getString("Message.PlayerHasMaxMoney");
            noMoney = conf.getConfig().getString("Message.PlayerNoHasMoney");
            moneyAt = conf.getConfig().getString("Message.MoneyFrom");
            definedMoneyAt = conf.getConfig().getString("Message.MoneyDefinedFrom");
            moneyAddedAt = conf.getConfig().getString("Message.MoneyAddedFrom");
            moneyRemovedAt = conf.getConfig().getString("Message.MoneyRemovedFrom");
            moneyResetedAt = conf.getConfig().getString("Message.MoneyResetFrom");
            yourSendAt = conf.getConfig().getString("Message.MoneySentFrom");
            noReasonToResetAt = conf.getConfig().getString("Message.NotReasonToResetPlayer");
            yourNoHaveMoney = conf.getConfig().getString("Message.InsuficientMoney");
            moneyTop10 = conf.getConfig().getString("Message.MoneyTopTitle");
            notHavePlayerInTop10 = conf.getConfig().getString("Message.NotFoundPlayerInTheMoneyTop");
            typeMoneyTop10 = conf.getConfig().getString("Message.RowMoneyTopStyle");

            helpTitle = conf.getConfig().getString("Message.HelpTitle");
            helpView = conf.getConfig().getString("Message.HelpViewCommand");
            helpSend = conf.getConfig().getString("Message.HelpSendCommand");
            helpTop10 = conf.getConfig().getString("Message.HelpTopsCommand");
            helpDefine = conf.getConfig().getString("Message.HelpDefineCommand");
            helpAdd = conf.getConfig().getString("Message.HelpAddCommand");
            helpRemove = conf.getConfig().getString("Message.HelpRemoveCommand");
            helpReset = conf.getConfig().getString("Message.HelpResetCommand");
            helpHelp = conf.getConfig().getString("Message.HelpHelpingCommand");
            helpLegend = conf.getConfig().getString("Message.HelpLegend");

            myMoney = conf.getConfig().getString("Message.YourMoney");
            yourReceived = conf.getConfig().getString("Message.YouReceiveSent");
            yourMoneydefined = conf.getConfig().getString("Message.YouReceiveSet");
            yourMoneyadded = conf.getConfig().getString("Message.YouReceiveAdd");
            yourMoneyremoved = conf.getConfig().getString("Message.YouReceiveRemove");
            yourMoneyreseted = conf.getConfig().getString("Message.YouMoneyReseted");

            magnataTag = conf.getConfig().getString("Options.MagnataTag");
            MaxMoney = conf.getConfig().getLong("Options.MaxMoney");
            MoneyTopSize = conf.getConfig().getInt("Options.MoneyTopSize");
            MaxMoney = (MaxMoney == 0 ? 1 : MaxMoney);
            MoneyTopSize = (MoneyTopSize == 0 ? 1 : MoneyTopSize);

            permissionsAtView = conf.getConfig().getString("Options.Permissions.ToUseViewCommand");
            permissionsAtSend = conf.getConfig().getString("Options.Permissions.ToUseSendCommand");
            permissionsAtViewMoneyTop = conf.getConfig().getString("Options.Permissions.ToUseTopsCommand");
            permissionsAtDefine = conf.getConfig().getString("Options.Permissions.ToUseDefineCommand");
            permissionsAtAdd = conf.getConfig().getString("Options.Permissions.ToUseAddCommand");
            permissionsAtRemove = conf.getConfig().getString("Options.Permissions.ToUseRemoveCommand");
            permissionsAtReset = conf.getConfig().getString("Options.Permissions.ToUseResetCommand");
            permissionsAtNpc = conf.getConfig().getString("Options.Permissions.ToUseSetNpcCommand");

            commands = conf.getConfig().getStringList("Options.Commands.Aliases");
            sub_view = conf.getConfig().getStringList("Options.Commands.SubCommands.View");
            sub_send = conf.getConfig().getStringList("Options.Commands.SubCommands.Send");
            sub_help = conf.getConfig().getStringList("Options.Commands.SubCommands.Help");
            sub_top = conf.getConfig().getStringList("Options.Commands.SubCommands.Tops");
            sub_define = conf.getConfig().getStringList("Options.Commands.SubCommands.Define");
            sub_add = conf.getConfig().getStringList("Options.Commands.SubCommands.Add");
            sub_remove = conf.getConfig().getStringList("Options.Commands.SubCommands.Remove");
            sub_reset = conf.getConfig().getStringList("Options.Commands.SubCommands.Reset");
            sub_npc = conf.getConfig().getStringList("Options.Commands.SubCommands.SetNpc");

            CommandManager.registerCommand(commands);

        } catch (Exception e) { conf.getFile().delete(); loadConfigs();}

    }

    //Getters

    public String getMessageNotVault(){ return NotVault; }
    public String getMessageNotPlaceHolder(){ return NotPlaceHolder; }
    public String getMessageNotLegendChat(){ return NotLegendChat; }
    public String getMessageYourIsRichest(){ return YourIsRichest; }
    public String getMessageChangedRichest(){ return ChangedRichest; }
    public String getMessageNoSendToYour(){ return noSendToYour; }

    public String getNoPermissionToUseCommand(String command){ return NoPermissionToUseCommand.replace("<comando>", command).replace("<command>", command); }
    public String getAmountInvalidMessage() { return amountInvalid; }
    public String getPlayerNotExistsMessage(){ return playerNotExists; }
    public String getPlayerMaxMoneyMessage(){ return playerMaxMoney; }
    public String getPlayerNoHasMoney(){ return noMoney; }
    public String getNoReasonToResetMoneyMessage(){ return noReasonToResetAt; }
    public String getYourNoHaveMoneyMessage(){ return yourNoHaveMoney; }

    public String getMessageHowToUseViewCommand(){ return howToUseViewCommand; }
    public String getMessageHowToUseSendCommand(){ return howToUseSendCommand; }
    public String getMessageHowToAddCommand(){ return howToUseAddCommand; }
    public String getMessageHowToUseRemoveCommand(){ return howToUseRemoveCommand; }
    public String getMessageHowToUseDefineCommand(){ return howToUseDefineCommand; }
    public String getMessageHowToUseResetCommand(){ return howToUseResetCommand; }

    public String getPermissionAtViewCommand() { return permissionsAtView; }
    public String getPermissionAtSendCommand() { return permissionsAtSend; }
    public String getPermissionAtViewMoneyTopCommand() { return permissionsAtViewMoneyTop; }
    public String getPermissionAtAddCommand() { return permissionsAtAdd; }
    public String getPermissionAtRemoveCommand() { return permissionsAtRemove; }
    public String getPermissionAtDefineCommand() { return permissionsAtDefine; }
    public String getPermissionAtResetCommand() { return permissionsAtReset; }
    public String getPermissionAtSetNpcCommand() { return permissionsAtNpc; }

    public String getMyMoneyMessage(String value) { return myMoney.replace("<quantidade>", value).replace("<money>", value).replace("<dinheiro>", value).replace("<dinero>", value); }
    public String getMoneyAtMessage(String name, String value) { return moneyAt.replace("<quantidade>", value).replace("<money>", value).replace("<dinheiro>", value).replace("<dinero>", value).replace("<player>", name).replace("<jogador>", name).replace("<jugador>", name); }
    public String getDefinedMoneyAtMessage(String name, String value) { return definedMoneyAt.replace("<quantidade>", value).replace("<money>", value).replace("<dinheiro>", value).replace("<dinero>", value).replace("<player>", name).replace("<jogador>", name).replace("<jugador>", name); }
    public String getAddedMoneyAtMessage(String name, String value) { return moneyAddedAt.replace("<quantidade>", value).replace("<money>", value).replace("<dinheiro>", value).replace("<dinero>", value).replace("<player>", name).replace("<jogador>", name).replace("<jugador>", name); }
    public String getRemovedMoneyAtMessage(String name, String value) { return moneyRemovedAt.replace("<quantidade>", value).replace("<money>", value).replace("<dinheiro>", value).replace("<dinero>", value).replace("<player>", name).replace("<jogador>", name).replace("<jugador>", name); }
    public String getResetedMoneyAtMessage(String name) { return moneyResetedAt.replace("<player>", name).replace("<jogador>", name).replace("<jugador>", name); }
    public String getYourSendMoneyAtMessage(String name, String value) { return yourSendAt.replace("<quantidade>", value).replace("<money>", value).replace("<dinheiro>", value).replace("<dinero>", value).replace("<player>", name).replace("<jogador>", name).replace("<jugador>", name); }
    public String getYourReceivedMoneyMessage(String name, String value) { return yourReceived.replace("<quantidade>", value).replace("<money>", value).replace("<dinheiro>", value).replace("<dinero>", value).replace("<player>", name).replace("<jogador>", name).replace("<jugador>", name); }

    public String getMoneyTop10Message(){ return moneyTop10; }
    public String getNotHavePlayerInTop10Message(){ return notHavePlayerInTop10; }
    public String getTypeMoneyTop10(String name, String value) { return typeMoneyTop10.replace("<quantidade>", value).replace("<money>", value).replace("<dinheiro>", value).replace("<dinero>", value).replace("<player>", name).replace("<jogador>", name).replace("<jugador>", name); }

    public String getMagnataTag(){ return magnataTag; }

    public String getHelpTitle(){ return helpTitle; }
    public String getHelpView(){ return helpView; }
    public String getHelpSend(){ return helpSend; }
    public String getHelpTop10(){ return helpTop10; }
    public String getHelpDefine(){ return helpDefine; }
    public String getHelpAdd(){ return helpAdd; }
    public String getHelpRemove(){ return helpRemove; }
    public String getHelpReset(){ return helpReset; }
    public String getHelpHelp(){ return helpHelp; }
    public String getHelpLegend(){ return helpLegend; }

    public boolean hasSubView(String subCommand){ return sub_view.contains(subCommand); }
    public boolean hasSubSend(String subCommand){ return sub_send.contains(subCommand); }
    public boolean hasSubHelp(String subCommand){ return sub_help.contains(subCommand); }
    public boolean hasSubTop(String subCommand){ return sub_top.contains(subCommand); }
    public boolean hasSubAdd(String subCommand){ return sub_add.contains(subCommand); }
    public boolean hasSubRemove(String subCommand){ return sub_remove.contains(subCommand); }
    public boolean hasSubDefine(String subCommand){ return sub_define.contains(subCommand); }
    public boolean hasSubReset(String subCommand){ return sub_reset.contains(subCommand); }
    public boolean hasSubNpc(String subCommand){ return sub_npc.contains(subCommand); }

    public String getDefinedInYourAccountMessage(String name, String value) { return yourMoneydefined.replace("<quantidade>", value).replace("<money>", value).replace("<dinheiro>", value).replace("<dinero>", value).replace("<player>", name).replace("<jogador>", name).replace("<jugador>", name); }
    public String getAddedInYourAccountMessage(String name, String value) { return yourMoneyadded.replace("<quantidade>", value).replace("<money>", value).replace("<dinheiro>", value).replace("<dinero>", value).replace("<player>", name).replace("<jogador>", name).replace("<jugador>", name); }
    public String getRemovedInYourAccountMessage(String name, String value) { return yourMoneyremoved.replace("<quantidade>", value).replace("<money>", value).replace("<dinheiro>", value).replace("<dinero>", value).replace("<player>", name).replace("<jogador>", name).replace("<jugador>", name); }
    public String getResetedMoneyYourMessage(String name) { return yourMoneyreseted.replace("<player>", name).replace("<jogador>", name).replace("<jugador>", name); }

    public Long getMaxMoney(){ return MaxMoney; }
    public Integer getSizeMoneyTop() { return MoneyTopSize; }

    public boolean isMysql(){ return isMysql; }
    public String getHostMysql(){ return Host; }
    public Integer getPortMysql(){ return Port; }
    public String getDatabaseMysql(){ return Database; }
    public String getUserMysql(){ return User; }
    public String getPasswordMysql(){ return Password; }

    public boolean isMoneyExact() { return moneyExact; }

}
