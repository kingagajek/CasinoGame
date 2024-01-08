package com.gajek.casinogame.Models;

import com.gajek.casinogame.Observer.IObserver;
import com.gajek.casinogame.Observer.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouletteGame extends Subject {
    private List<IObserver> IObservers = new ArrayList<>();
    private double balance;
    private Map<Integer, Double> numberBets;

    private double totalBetAmount;
    private double colorBetAmount;
    private boolean betOnRed = false;
    private boolean betOnBlack = false;
    private boolean betOnGreen = false;


    public RouletteGame() {
        numberBets = new HashMap<>();
        totalBetAmount = 0;
    }

    public void setBetOnColor(String color, boolean isSet) {
        if ("Red".equals(color)) {
            betOnRed = isSet;
        } else if ("Black".equals(color)) {
            betOnBlack = isSet;
        } else if ("Green".equals(color)) {
            betOnGreen = isSet;
        }
    }

    public boolean getBetOnRed() {
        return betOnRed;
    }

    public boolean getBetOnBlack() {
        return betOnBlack;
    }

    public boolean getBetOnGreen() {
        return betOnGreen;
    }

    public void updateBets(List<Integer> selectedNumbers, double betValue, boolean betOnRed, boolean betOnBlack, boolean betOnGreen) {
        numberBets.clear();
        totalBetAmount = 0;
        colorBetAmount = 0;

        for (Integer number : selectedNumbers) {
            numberBets.put(number, betValue);
            totalBetAmount += betValue;
        }

        if (betOnRed || betOnBlack || betOnGreen) {
            colorBetAmount = betValue;
            totalBetAmount += colorBetAmount;
        }

        notifyObservers();
    }

    public void processResult(int number, String color) {
        boolean win = checkWin(number, color);
        double payout = calculatePayout(number, color);
        setBalance(getBalance() - totalBetAmount + payout);
        notifyObservers();
    }


    public boolean checkWin(int number, String color) {
        if (numberBets.containsKey(number)) {
            return true;
        }
        if ((betOnRed && "Red".equals(color)) ||
                (betOnBlack && "Black".equals(color)) ||
                (betOnGreen && "Green".equals(color))) {
            return true;
        }
        return false;
    }

    public double calculatePayout(int winningNumber, String winningColor) {
        double payout = 0;
        if (numberBets.containsKey(winningNumber)) {
            payout += numberBets.get(winningNumber) * 35;
        }
        // Check color bets for payout
        if ((betOnRed && "Red".equals(winningColor)) ||
                (betOnBlack && "Black".equals(winningColor)) ||
                (betOnGreen && "Green".equals(winningColor))) {
            payout += colorBetAmount * 2;
        }
        return payout;
    }

    public void resetBets() {
        totalBetAmount = 0;
        numberBets.clear();
        betOnRed = false;
        betOnBlack = false;
        betOnGreen = false;
        colorBetAmount = 0;

        notifyObservers();
    }

    public String getColorForNumber(int number) {
        if (number == 0) {
            return "Green";
        } else if (number % 2 == 0) {
            return "Black";
        } else {
            return "Red";
        }
    }

    public String getCurrentBetsText() {
        StringBuilder betsText = new StringBuilder();

        if (betOnRed) {
            betsText.append(String.format("Red: %.2f| ", colorBetAmount));
        }
        if (betOnBlack) {
            betsText.append(String.format("Black: %.2f| ", colorBetAmount));
        }
        if (betOnGreen) {
            betsText.append(String.format("Green: %.2f| ", colorBetAmount));
        }

        for (Map.Entry<Integer, Double> entry : numberBets.entrySet()) {
            int number = entry.getKey();
            double amount = entry.getValue();
            betsText.append(String.format("Number %d: %.2f| ", number, amount));
        }

        return betsText.toString();
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
        notifyObservers();
    }


    @Override
    public void attach(IObserver o) {
        if (!IObservers.contains(o)) {
            IObservers.add(o);
        }
    }

    @Override
    public void detach(IObserver o) {
        IObservers.remove(o);
    }

    public void notifyObservers() {
        for (IObserver IObserver : IObservers) {
            IObserver.update(this);
        }
    }
}
