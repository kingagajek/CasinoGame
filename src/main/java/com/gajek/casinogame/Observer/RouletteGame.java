package com.gajek.casinogame.Observer;

import com.gajek.casinogame.Controllers.RouletteGameController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouletteGame extends Subject {
    private List<Observer> observers = new ArrayList<>();
    private double balance;
    private String currentBetsText;
    private Map<Integer, Double> numberBets;

    private double totalBetAmount;
    private double colorBetAmount;
    private boolean betOnRed = false;
    private boolean betOnBlack = false;
    private boolean betOnGreen = false;


    public RouletteGame() {
        // inicjalizacja zmiennych...
        numberBets = new HashMap<>();
        totalBetAmount = 0;
    }

    public void setBetOnColor (String color, boolean isSet) {
        if ("Red".equals(color)) {
            betOnRed = isSet;
        }
        else if ("Black".equals(color)) {
            betOnBlack = isSet;
        }
        else if ("Green".equals(color)) {
            betOnGreen = isSet;
        }
    }
    public boolean getBetOnRed () {
        return betOnRed;
    }
    public boolean getBetOnBlack () {
        return betOnBlack;
    }
    public boolean getBetOnGreen () {
        return betOnGreen;
    }

    public void updateBets(List<Integer> selectedNumbers, double betValue, boolean betOnRed, boolean betOnBlack, boolean betOnGreen) {
        // Resetuj zakłady
        numberBets.clear();
        totalBetAmount = 0;
        colorBetAmount = 0;

        // Dodaj zakłady na numery
        for (Integer number : selectedNumbers) {
            numberBets.put(number, betValue);
            totalBetAmount += betValue;
        }

        // Dodaj zakłady na kolory
        if (betOnRed || betOnBlack || betOnGreen) {
            colorBetAmount = betValue;
            totalBetAmount += colorBetAmount;
        }

        // Powiadom obserwatorów o zmianie
        notifyObservers();
    }

    public void processResult(int number, String color) {
        boolean win = checkWin(number, color);
        double payout = calculatePayout(number, color);
        setBalance(getBalance() - totalBetAmount + payout); // Załóżmy, że zakłady zostały już odjęte od salda
        //setCurrentBetsText(getBetDetails(number, color, win, payout));
       // resetBets(); // Resetuj zakłady na następną grę
        notifyObservers(); // Powiadom obserwatorów o zmianach
    }


    public boolean checkWin(int number, String color) {
        // If a number bet was placed and it matches the winning number, it's a win
        if (numberBets.containsKey(number)) {
            return true;
        }
        // Check color bets
        if ((betOnRed && "Red".equals(color)) ||
                (betOnBlack && "Black".equals(color)) ||
                (betOnGreen && "Green".equals(color))) {
            return true;
        }
        // No bets matched the winning conditions
        return false;
    }

    // This method should calculate the payout based on whether the player won
    public double calculatePayout(int winningNumber, String winningColor) {
        double payout = 0;
        // If there was a win on a number, pay out for the number
        if (numberBets.containsKey(winningNumber)) {
            payout += numberBets.get(winningNumber) * 35; // Example payout for numbers
        }
        // Check color bets for payout
        if ((betOnRed && "Red".equals(winningColor)) ||
                (betOnBlack && "Black".equals(winningColor)) ||
                (betOnGreen && "Green".equals(winningColor))) {
            payout += colorBetAmount * 2; // Example payout for colors
        }
        return payout;
    }

    // This method should return the bet details based on the game's result
//    public String getBetDetails(int number, String color, boolean win, double payout) {
//        StringBuilder details = new StringBuilder();
//        if (win) {
//            // Append details of winning bets
//            details.append(String.format("Number %d - %s hit! Payout: %.2f", number, color, payout));
//        } else {
//            // Append message for no wins
//            details.append("No win this time.");
//        }
//        return details.toString();
//    }
    public void resetBets() {
        totalBetAmount = 0;
        numberBets.clear();
        betOnRed = false;
        betOnBlack = false;
        betOnGreen = false;
        colorBetAmount = 0;

        // Notify observers to update the UI
        notifyObservers();
    }
    private void betsChanged() {
        // Powiadamianie obserwatorów o zmianie zakładów
        for (Observer observer : observers) {
            observer.update(this); // Przekazujemy cały obiekt gry do obserwatora
        }
    }

    public double getTotalBetAmount() {
        return totalBetAmount;
    }

    public double getColorBetAmount() {
        return colorBetAmount;
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

    public Double getBetAmountForNumber(int number) {
        // Zakładamy, że `numberBets` to mapa przechowująca zakłady na liczby.
        return numberBets.get(number); // Zwróć kwotę zakładu dla danego numeru lub null, jeśli nie ma zakładu
    }


    public Map<Integer, Double> getNumberBets() {
        return new HashMap<>(numberBets);
    }
    // Metoda wywoływana przez obserwatorów, aby pobrać aktualny tekst zakładów
    public String getCurrentBetsText() {
        StringBuilder betsText = new StringBuilder();

        if (betOnRed) {
            betsText.append(String.format("Czerwony: %.2f| ", colorBetAmount));
        }
        if (betOnBlack) {
            betsText.append(String.format("Czarny: %.2f| ", colorBetAmount));
        }
        if (betOnGreen) {
            betsText.append(String.format("Zielony: %.2f| ", colorBetAmount));
        }

        for (Map.Entry<Integer, Double> entry : numberBets.entrySet()) {
            int number = entry.getKey();
            double amount = entry.getValue();
            betsText.append(String.format("Numer %d: %.2f| ", number, amount)); // Formatowanie tekstu zakładu
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

//    public void setCurrentBetsText(String currentBetsText) {
//        this.currentBetsText = currentBetsText;
//        notifyObservers();
//    }

    // Metoda wywoływana, aby powiadomić obserwatorów o zmianie zakładów


    @Override
    public void attach(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this); // przekazujemy 'this' czyli obiekt RouletteGame
        }
    }


    // Metody biznesowe, które zmieniają stan i wywołują notifyObservers...
}
