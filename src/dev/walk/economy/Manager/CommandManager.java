package dev.walk.economy.Manager;

import dev.walk.economy.Economy;
import dev.walk.economy.MoneyCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommandManager {

    static List<String> commands = new ArrayList<>();

    public static void registerCommand(String... cmd){ registerCommand(Arrays.asList(cmd)); }
    public static void registerCommand(List<String> cmdL) {
        Iterator<String> cmds = cmdL.iterator();
        while (cmds.hasNext()){
            String cmdActual = cmds.next();
            if (!commands.contains(cmdActual)){
                commands.add(cmdActual);
                try {
                    Class<?> craftServer = getNMSClass("CraftServer");
                    Method getCommandMap = craftServer.getMethod("getCommandMap", new Class[0]);
                    SimpleCommandMap scm = (SimpleCommandMap)getCommandMap.invoke(Bukkit.getServer(), new Object[0]);
                    Command aliase = new Command(cmdActual.trim()){
                        String cmdU = cmdActual;
                        public boolean execute(CommandSender s, String l, String[] args) {
                            new MoneyCommand(cmdU, s, args);
                            return true;
                        }
                    };
                    scm.register(Economy.getEconomy().getName().toLowerCase(), aliase);
                }
                catch (Exception e) {}
            }
        }
    }

    private static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

}
