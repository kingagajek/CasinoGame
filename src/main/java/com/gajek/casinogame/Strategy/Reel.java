package com.gajek.casinogame.Strategy;

import javafx.scene.image.Image;

public class Reel {
    private IReelStrategy strategy;

    public Reel(IReelStrategy strategy) {
        this.strategy = strategy;
    }

    public Image spin() {
        return strategy.spin();
    }
}