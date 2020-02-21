package dev.walk.economy.Events;

import dev.walk.economy.Economy;
import dev.walk.economy.Manager.Account;
import dev.walk.economy.Manager.Magnata;
import dev.walk.economy.Manager.Storage;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EconomyAccountEvent extends Event {

    Account account;
    ChangeType type;
    double money;
    boolean cancelled = false;

    public EconomyAccountEvent(ChangeType type, Account account, double money) {
        this.type = type;
        this.account = account;
        this.money = money;
        Bukkit.getPluginManager().callEvent(this);
    }

    public Account getAccount() {
        return account;
    }

    public ChangeType getEventType() {
        return type;
    }

    public double getChangedMoney() {
        return money;
    }

    public Magnata getMagnataClass() { return Storage.Magnata; }

    public Account getMagnata() {
        return getMagnataClass().getMagnata();
    }

    public Account getAccountTop() {
        return getMagnataClass().getTop();
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    private static HandlerList hl = new HandlerList();

    public HandlerList getHandlers() {
        return hl;
    }

    public static HandlerList getHandlerList() {
        return hl;
    }

}
