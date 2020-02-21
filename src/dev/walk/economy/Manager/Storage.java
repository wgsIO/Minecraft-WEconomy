package dev.walk.economy.Manager;

import dev.walk.economy.Economy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Storage {

    public static Map<UUID, Account> Accounts = new HashMap<>();
    static MagnataManager MagnataManager = new MagnataManager();
    public static Magnata Magnata = new Magnata();
    public static Economy.Log Log;
    public static ConfigPlugin config;
    public static boolean npcHD = false;

}
