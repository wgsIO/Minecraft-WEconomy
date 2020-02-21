package dev.walk.economy.Manager;

import com.google.common.collect.Collections2;
import dev.walk.economy.Events.MagnataChangeEvent;

import java.util.*;

public class Magnata {

    public List<Account> getTop(int start, int stop) {
        List<Account> tops = new ArrayList<>();
        List<Account> comparators = new ArrayList<>(Collections2.filter(Storage.Accounts.values(), Account::isOwnedByPlayer));
        Collections.sort(comparators);
        Iterator<Account> accounts = comparators.iterator();
        int i = start;
        while (i < stop && accounts.hasNext()) {
            Account account = accounts.next();
            tops.add(account);
            i++;
        }
        return tops;
    }

    public Account getTop() {
        List<Account> comparators = new ArrayList<>(Collections2.filter(Storage.Accounts.values(), Account::isOwnedByPlayer));
        Collections.sort(comparators);
        if (!comparators.isEmpty()) {
            return comparators.get(0);
        }
        return null;
    }
    public HashMap<UUID, Integer> getRanks() { return getRanks(1, Storage.Accounts.size()); }
    public HashMap<UUID, Integer> getRanks(int start, int stop) {
        HashMap<UUID, Integer> tops = new HashMap<>();
        List<Account> comparators = new ArrayList<>(Collections2.filter(Storage.Accounts.values(), Account::isOwnedByPlayer));
        Collections.sort(comparators);
        Iterator<Account> accounts = comparators.iterator();
        int i = start;
        while (i < stop && accounts.hasNext()) {
            Account account = accounts.next();
            tops.put(account.getUUID(), i);
            i++;
        }
        return tops;
    }

    public void setMagnata(Account account) {
        if (!isMagnata(account)) {
            if (hasMagnata()) {
                MagnataChangeEvent e = new MagnataChangeEvent(getMagnata(), account);
                if (!e.isCancelled()) {
                    Storage.MagnataManager.setMagnata(account);
                }
            } else {
                Storage.MagnataManager.setMagnata(account);
            }
        }
    }

    public String getMagnataTag() {
        return Storage.config.getMagnataTag();
    }

    public Account getMagnata() {
        return Storage.MagnataManager.getMagnata();
    }

    public boolean isMagnata(Account account) {
        return hasMagnata() && Storage.MagnataManager.getMagnata().getUUID().equals(account.getUUID());
    }

    public boolean hasMagnata() {
        return Storage.MagnataManager.getMagnata() != null;
    }

}
