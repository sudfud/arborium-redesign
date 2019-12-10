package com.mygdx.arborium.item;

import java.util.function.Function;

public class Upgrade {
    private boolean enabled;

    public Upgrade(Tree tree, Function<Void, Void> effectFunction) {
        this.enabled = false;
    }
}
