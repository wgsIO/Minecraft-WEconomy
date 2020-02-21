package dev.walk.economy.Manager;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

class MagnataManager {

    private Account magnata;

    @Getter
    Account getMagnata() {
        return magnata;
    }

    @Setter
    void setMagnata(Account magnata) {
        this.magnata = magnata;
    }

}
