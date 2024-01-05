package com.gajek.casinogame.Strategy;

import javafx.scene.image.Image;

import java.util.List;

public class Reel {
    private ReelStrategy strategy;

    public Reel(ReelStrategy strategy) {
        this.strategy = strategy;
    }

    public Image spin() {
        return strategy.spin();
    }
}