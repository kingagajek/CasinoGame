package com.gajek.casinogame.Strategy;

import javafx.scene.image.Image;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomReelStrategy implements ReelStrategy {
    private final List<Image> symbols;
    private final Random randomGenerator = new Random();

    public RandomReelStrategy(List<Image> symbols) {
        this.symbols = symbols;
    }

    @Override
    public Image spin() {
        // Losowe wybranie indeksu z listy dostępnych symboli
        int randomIndex = randomGenerator.nextInt(symbols.size());
        // Zwrócenie wybranego symbolu
        return symbols.get(randomIndex);
    }
}
