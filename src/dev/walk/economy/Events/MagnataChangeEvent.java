package dev.walk.economy.Events;

import dev.walk.economy.Economy;
import dev.walk.economy.Manager.Account;
import dev.walk.economy.Manager.Magnata;
import dev.walk.economy.Manager.Storage;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MagnataChangeEvent extends Event {

    Account magnata;
    Account beforeMagnata;
    boolean cancelled = false;

    public MagnataChangeEvent(Account beforeMagnata, Account magnata) {
        this.magnata = magnata;
        this.beforeMagnata = beforeMagnata;
        Bukkit.getPluginManager().callEvent(this);
    }

    public Account getNewMagnata() {
        return magnata;
    }

    public Account getBeforeMagnata() {
        return beforeMagnata;
    }

    public Magnata getMagnataClass() {
        return Storage.Magnata;
    }

    public Account getCurrentMagnata() {
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
