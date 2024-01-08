package com.gajek.casinogame.Strategy;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SlotMachine {
    private final PayoutStrategy payoutStrategy;
    private List<Reel> reels;
    private int balance;
    private int lastPayout;
    private List<Image> lastSpinResults;


    public SlotMachine(List<Map<Image, Integer>> weightedSymbolsForReels, PayoutStrategy payoutStrategy) {
        this.payoutStrategy = payoutStrategy;
        this.reels = new ArrayList<>();
        for (Map<Image, Integer> weightedSymbols : weightedSymbolsForReels) {
            reels.add(new Reel(new RandomReelStrategy(weightedSymbols)));
        }
        this.balance = 1000;
    }

    public List<Image> getLastSpinResults() {
        return lastSpinResults;
    }


    public int spinAndCalculatePayout(int betAmount) {
        lastSpinResults = spinReels();
        lastPayout = payoutStrategy.calculatePayout(lastSpinResults, betAmount);
        balance -= betAmount;
        balance += lastPayout;
        return lastPayout;
    }

    public List<Image> spinReels() {
        return reels.stream().map(Reel::spin).collect(Collectors.toList());
    }

    public void setBalance(int newBalance) {
        balance = newBalance;
    }

    public int getBalance() {
        return balance;
    }

    public int calculateMaxBet() {
        return Math.min(balance, 100);
    }


    public boolean validateAndSetBet(int betAmount) {
        if (betAmount > 0 && betAmount <= balance) {
            return true;
        }
        return false;
    }
}
