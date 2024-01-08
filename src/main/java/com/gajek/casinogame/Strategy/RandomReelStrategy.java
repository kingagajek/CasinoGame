package com.gajek.casinogame.Strategy;

import javafx.scene.image.Image;

import java.util.*;

public class RandomReelStrategy implements IReelStrategy {
    private final List<Image> weightedSymbolPool;
    private final Random randomGenerator = new Random();
    private Set<Image> alreadySpunSymbols = new HashSet<>();

    public RandomReelStrategy(Map<Image, Integer> symbolWeights) {
        this.weightedSymbolPool = createWeightedSymbolPool(symbolWeights);
    }

    private List<Image> createWeightedSymbolPool(Map<Image, Integer> symbolWeights) {
        List<Image> symbolPool = new ArrayList<>();
        for (Map.Entry<Image, Integer> entry : symbolWeights.entrySet()) {
            Image symbol = entry.getKey();
            int weight = entry.getValue();
            for (int i = 0; i < weight; i++) {
                symbolPool.add(symbol);
            }
        }
        return symbolPool;
    }

    public Image spin() {
        int randomIndex = randomGenerator.nextInt(weightedSymbolPool.size());
        Image chosenSymbol = weightedSymbolPool.get(randomIndex);

        alreadySpunSymbols.clear();
        if (alreadySpunSymbols.contains(chosenSymbol)) {

            randomIndex = adjustRandomIndexBasedOnPreviousSpins(randomIndex, chosenSymbol, weightedSymbolPool, alreadySpunSymbols);
            chosenSymbol = weightedSymbolPool.get(randomIndex);
        }

        alreadySpunSymbols.add(chosenSymbol);
        return chosenSymbol;
    }

    private int adjustRandomIndexBasedOnPreviousSpins(int currentIndex, Image chosenSymbol, List<Image> weightedSymbolPool, Set<Image> alreadySpunSymbols) {
        if (alreadySpunSymbols.contains(chosenSymbol)) {
            List<Image> availableSymbols = new ArrayList<>(weightedSymbolPool);
            availableSymbols.removeAll(alreadySpunSymbols);
            if (!availableSymbols.isEmpty()) {
                int newRandomIndex = randomGenerator.nextInt(availableSymbols.size());
                return weightedSymbolPool.indexOf(availableSymbols.get(newRandomIndex));
            }
        }
        return currentIndex;
    }
}
