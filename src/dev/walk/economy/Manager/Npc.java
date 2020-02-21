package dev.walk.economy.Manager;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import dev.walk.economy.Economy;
import dev.walk.economy.Util.EconomyUtils;
import dev.walk.economy.Util.MultiValue;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import net.citizensnpcs.util.NMS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Npc {

    private static ConfigManagerFile conf;
    private static NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();
    public static HashMap<UUID, Hologram> holograms = new HashMap<>();
    private static EconomyUtils utils = new EconomyUtils();

    static{
        conf = new ConfigManagerFile(Economy.getEconomy(), "npcs.dat");
        conf.prepare();
    }

    public static void setNpc(int rank, Location location){
        if (conf.getConfig().getString("npcs."+rank+".id") != null){
            NPC npc = npcRegistry.getByUniqueId(UUID.fromString(conf.getConfig().getString("npcs."+rank+".id")));
            List<Account> a = Storage.Magnata.getTop(0, Storage.config.getSizeMoneyTop());
            String name = null; Account account = (a.size() >= rank ? a.get(rank-1) : null);
            if (account != null) {
                MultiValue<Player, OfflinePlayer> mv = account.getPlayer();
                name = (mv.getOne() != null ? mv.getOne().getName() : (mv.getTwo() != null ? mv.getTwo().getName() : null));
            }
            if (npc != null) {
                npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, name);
                npc.data().set(NPC.PLAYER_SKIN_USE_LATEST, false);
            }
            if (npc.isSpawned()){
                npc.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
            } else {
                npc.spawn(location);
            }
            if (npc != null) {
                if (name != null) {
                    if (Storage.npcHD) {
                        if (!holograms.containsKey(npc.getUniqueId())){
                            Hologram hd = HologramsAPI.createHologram(Economy.getEconomy(), npc.getEntity().getLocation().add(0, 3.1, 0));
                            hd.insertTextLine(0, (rank == 1 ? "§b" : (rank == 2 ? "§e" : (rank == 3 ? "§f" : "§7")))+rank+"º Lugar");
                            hd.insertTextLine(1, "§e"+name);
                            hd.insertTextLine(2, "§2$ §7"+utils.formatMoney(account.getMoney())+" coins");
                            holograms.put(npc.getUniqueId(), hd);
                        } else {
                            Hologram hd = holograms.get(npc.getUniqueId());
                            hd.clearLines();
                            hd.insertTextLine(0, (rank == 1 ? "§b" : (rank == 2 ? "§e" : (rank == 3 ? "§f" : "§7")))+rank+"º Lugar");
                            hd.insertTextLine(1, "§e"+name);
                            hd.insertTextLine(2, "§2$ §7"+utils.formatMoney(account.getMoney())+" coins");
                            holograms.replace(npc.getUniqueId(), hd);
                        }

                        npc.setName("");
                    } else {
                        npc.setName((rank == 1 ? "&b" : (rank == 2 ? "&e" : (rank == 3 ? "&f" : "&7"))) + name + ": " + rank);
                    }
                } else {
                    if (Storage.npcHD) {
                        if (!holograms.containsKey(npc.getUniqueId())){
                            Hologram hd = HologramsAPI.createHologram(Economy.getEconomy(), npc.getEntity().getLocation().add(0, 3.1, 0));
                            hd.insertTextLine(0, (rank == 1 ? "§b" : (rank == 2 ? "§e" : (rank == 3 ? "§f" : "§7")))+rank+"º Lugar");
                            hd.insertTextLine(1, "§7Ninguém");
                            hd.insertTextLine(2, "§2$ §7Indefinido");
                            holograms.put(npc.getUniqueId(), hd);
                        } else {
                            Hologram hd = holograms.get(npc.getUniqueId());
                            hd.clearLines();
                            hd.insertTextLine(0, (rank == 1 ? "§b" : (rank == 2 ? "§e" : (rank == 3 ? "§f" : "§7")))+rank+"º Lugar");
                            hd.insertTextLine(1, "§7Ninguém");
                            hd.insertTextLine(2, "§2$ §7Indefinido");
                            holograms.replace(npc.getUniqueId(), hd);
                        }
                        npc.setName("");
                    } else {
                        npc.setName("&8[ Vazio -> "+rank+" ]");
                    }
                }
            }
            npc.setFlyable(true);
            npc.getDefaultGoalController().setPaused(true);
            setSkin(npc, name);
            conf.getConfig().set("npcs."+rank+".id", npc.getUniqueId().toString());
            conf.getConfig().set("npcs."+rank+".location", serializeLocation(location));
            conf.save();
        } else {
            NPC npc = npcRegistry.createNPC(EntityType.PLAYER, "&8[ Vazio -> "+rank+" ]");
            conf.getConfig().set("npcs."+rank+".id", npc.getUniqueId().toString());
            conf.getConfig().set("npcs."+rank+".location", serializeLocation(location));
            conf.save();
            setNpc(rank, location);
        }
    }

    public static void destroyNpc(int rank){
        NPC npc = npcRegistry.getByUniqueId(UUID.fromString(conf.getConfig().getString("npcs."+rank+".id")));
        if (npc != null ){
            npc.destroy();
            holograms.remove(npc.getUniqueId());
            conf.getConfig().set("npcs."+rank+".id", null);
            conf.getConfig().set("npcs."+rank+".location", null);
            conf.save();
        }
    }

    public static boolean exists(int rank){
        return (conf.getConfig().getString("npcs."+rank+".id") != null);
    }

    public static void updateNpcs(){
        if (conf.getConfig().contains("npcs")){
            int i = 1;
            while(i <= Storage.config.getSizeMoneyTop()){
                String location = conf.getConfig().getString("npcs."+i+".location");
                if (location != null) {
                    setNpc(i, deserialiseLocation(location));
                }
                i++;
            }
        }
    }

    public static void setSkin(NPC npc, String skinName){
        if (npc.isSpawned() && skinName != null) {
            SkinnableEntity skinnable = NMS.getSkinnable(npc.getEntity());
            if (skinnable != null) {
                skinnable.setSkinName(skinName);
            }
        }
    }

    private static String serializeLocation(Location l){
        return l.getWorld().getName()+","+l.getX()+","+l.getY()+","+l.getZ()+","+l.getYaw()+","+l.getPitch();
    }
    private static Location deserialiseLocation(String l){
        try{
            String[] L = l.split(",");
            return new Location(Bukkit.getWorld(L[0]), Double.parseDouble(L[1]), Double.parseDouble(L[2]), Double.parseDouble(L[3]), Float.parseFloat(L[4]), Float.parseFloat(L[5]));
        }catch (Exception e) {
            return null;
        }
    }

}
