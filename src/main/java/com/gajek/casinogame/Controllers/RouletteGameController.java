package com.gajek.casinogame.Controllers;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RouletteGameController {

    @FXML
    private Button spinButton;
    @FXML
    private Button betRedButton;
    @FXML
    private Button betBlackButton;
    @FXML
    private Button betGreenButton;
    @FXML
    private Label resultLabel;

    // Atrybuty do przechowywania informacji o zakładach
    private boolean betOnRed = false;
    private boolean betOnBlack = false;
    private boolean betOnGreen = false;

    @FXML
    private Circle ball;
    @FXML
    private ImageView rouletteWheelImage;
    @FXML
    private ListView<Integer> numberSelection;
    @FXML
    private TextField betAmount;
    private final Map<Integer, Double> numberBets = new HashMap<>();
    private double totalBetAmount = 0;
    private double colorBetAmount = 0;
    // Add a balance property
    private double balance = 1000; // Starting balance

    @FXML
    private Label balanceLabel; // Label to display the balance

    @FXML
    private Label currentBetsLabel; // Label to display current bets

    // Metoda do aktualizowania wyświetlania aktualnych zakładów
    private void updateCurrentBetsDisplay() {
        System.out.println("Aktualizacja wyświetlania zakładów"); // Debug
        StringBuilder betsDisplay = new StringBuilder("Aktualne zakłady: ");

        // Debug: Sprawdź, czy etykieta została zainicjalizowana
        if (currentBetsLabel == null) {
            System.out.println("currentBetsLabel jest null!");
            return;
        }

        if (betOnRed) betsDisplay.append("Czerwony | ");
        if (betOnBlack) betsDisplay.append("Czarny | ");
        if (betOnGreen) betsDisplay.append("Zielony | ");
        numberBets.forEach((number, amount) -> betsDisplay.append("Nr ").append(number).append(": ").append(amount).append(" | "));

        // Debug: Wyświetl zawartość betsDisplay przed ustawieniem tekstu
        System.out.println(betsDisplay.toString());

        currentBetsLabel.setText(betsDisplay.toString());
    }

    private void updateBalanceDisplay() {
        balanceLabel.setText(String.format("Balance: %.2f", balance));
    }
    public void initialize() {
        // Dodaj numery do ListView
        for (int i = 0; i <= 36; i++) {
            numberSelection.getItems().add(i);
        }

        // Ustaw tryb wielokrotnego wyboru
        numberSelection.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        updateBalanceDisplay();
    }


    @FXML
    private void confirmBets() {
        // Wyczyść poprzednie zakłady
        numberBets.clear();
        totalBetAmount = 0;
        colorBetAmount = 0;
        double betValue;

        if (betAmount.getText().isEmpty() && !betOnRed && !betOnBlack && !betOnGreen) {
            resultLabel.setText("Proszę wpisać kwotę zakładu lub wybrać kolor.");
            return;
        }
        // Pobierz wybrane numery i postawione kwoty
        List<Integer> selectedNumbers = numberSelection.getSelectionModel().getSelectedItems();
        try {
            betValue = Double.parseDouble(betAmount.getText());
            for (int number : selectedNumbers) {
                numberBets.put(number, betValue);
                totalBetAmount += betValue;
            }
            if (betOnRed || betOnBlack || betOnGreen) {
                totalBetAmount += betValue;
                colorBetAmount = betValue;
            }
            resultLabel.setText("Zakłady potwierdzone. Łączna stawka: " + totalBetAmount);
        } catch (NumberFormatException e) {
            resultLabel.setText("Proszę wpisać poprawną kwotę zakładu.");
        }
        // Display current bets
        updateCurrentBetsDisplay();

        // Check if balance has run out
        if (balance <= 0) {
            resultLabel.setText("Game Over. You've run out of funds!");
            // Disable betting buttons or take other appropriate actions
        }

        Platform.runLater(() -> {
            updateCurrentBetsDisplay();
        });

    }

    @FXML
    private void spinRoulette() {
        // Tworzymy ścieżkę animacji wokół koła ruletki
        if (betAmount.getText().isEmpty() && !betOnRed && !betOnBlack && !betOnGreen) {
            resultLabel.setText("Proszę najpierw postawić zakład.");
            return;
        }
        Path path = new Path();
        MoveTo moveTo = new MoveTo();
        moveTo.setX(ball.getCenterX());
        moveTo.setY(ball.getCenterY());

        // Tworzymy łuk, który porusza się wokół koła ruletki
        ArcTo arcTo = new ArcTo();
        arcTo.setX(ball.getCenterX() - 0.1); // Niewielka zmiana, aby animacja była płynna
        arcTo.setY(ball.getCenterY());
        arcTo.setRadiusX(rouletteWheelImage.getFitWidth() / 2);
        arcTo.setRadiusY(rouletteWheelImage.getFitHeight() / 2);
        arcTo.setSweepFlag(true);
        arcTo.setLargeArcFlag(true); // Powinno być ustawione na false, jeśli tworzymy łuk mniejszy niż 180 stopni

// Dodajemy moveTo i arcTo do ścieżki
        path.getElements().add(moveTo);
        path.getElements().add(arcTo);

        // Stworzenie animacji ścieżki dla kulki
        PathTransition transition = new PathTransition();
        transition.setNode(ball);
        transition.setDuration(Duration.seconds(5));
        transition.setPath(path);
        transition.setCycleCount(1); // Ustawiamy, aby animacja wykonała się tylko raz

        // Ustawiamy akcję, która zostanie wykonana po zakończeniu animacji
        transition.setOnFinished(event -> {
            System.out.println("Animacja została zakończona");
            // Tutaj możesz umieścić dodatkową logikę, na przykład wyświetlić wynik gry
            Platform.runLater(() -> {
                displayResult(spinWheel());
                updateBalanceDisplay();
            });
        });

        transition.play();
    }


    // Metody wywoływane po naciśnięciu przycisków zakładów
    public void betRed(ActionEvent actionEvent) {
        resultLabel.setText("Bet placed on Red");
        if (betOnRed)
        {
            betOnRed = false;
            betRedButton.getStyleClass().remove("active");
        }
        else
        {
            betOnRed = true;
            betRedButton.getStyleClass().add("active");
        }
    }

    public void betBlack(ActionEvent actionEvent) {
        resultLabel.setText("Bet placed on Black");
        if (betOnBlack)
        {
            betOnBlack = false;
            betBlackButton.getStyleClass().remove("active");
        }
        else
        {
            betOnBlack = true;
            betBlackButton.getStyleClass().add("active");
        }
    }

    public void betGreen(ActionEvent actionEvent) {
        resultLabel.setText("Bet placed on Green");
        if (betOnGreen)
        {
            betOnGreen = false;
            betGreenButton.getStyleClass().remove("active");
        }
        else
        {
            betOnGreen = true;
            betGreenButton.getStyleClass().add("active");
        }
    }

    // Pomocnicza metoda do symulacji obrotu koła ruletki
    private int spinWheel() {
        Random random = new Random();
        return random.nextInt(37); // Ruletka europejska ma liczby od 0 do 36
    }

    // Pomocnicza metoda do wyświetlania wyniku
    private void displayResult(int number) {
        String color;
        if (number == 0) {
            color = "Green";
        } else if (number % 2 == 0) {
            color = "Black";
        } else {
            color = "Red";
        }

        boolean win = false;
        double payout = 0;
        String betDetails = "";
        balance -= totalBetAmount; // Odejmij stawkę z balansu

        // Sprawdź zakłady na liczby
        if (numberBets.containsKey(number)) {
            win = true;
            payout += numberBets.get(number) * 35; // Wypłata 35 do 1 za trafienie numeru
            betDetails += "Number " + number + " hit! Bet payout: " + (numberBets.get(number) * 35) + ". ";
        }

        // Sprawdź zakłady na kolory
        if (betOnRed && "Red".equals(color)) {
            win = true;
            payout += colorBetAmount * 2; // Wypłata 1 do 1 za trafienie koloru
            betDetails += "Red color hit! ";
        } else if (betOnBlack && "Black".equals(color)) {
            win = true;
            payout += colorBetAmount * 2; // Wypłata 1 do 1 za trafienie koloru
            betDetails += "Black color hit! ";
        } else if (betOnGreen && "Green".equals(color)) {
            win = true;
            payout += colorBetAmount * 35; // Wypłata 35 do 1 za trafienie zielonego (zerowego)
            betDetails += "Green color hit! ";
        }

        // Wyświetl wynik
        if (win) {
            balance += payout;
            resultLabel.setText("Win! Ball landed on " + number + " - " + color + ". " + betDetails + "Total payout: " + payout + ".");
        } else {
            resultLabel.setText("Lose. Ball landed on " + number + " - " + color + ".");
        }

        // Resetowanie zakładów i aktualizacja wyświetlania balansu
        resetBets();
        updateBalanceDisplay();
    }


    // Pomocnicza metoda do resetowania zakładów
    @FXML
    private void resetBets() {
        betOnRed = false;
        betOnBlack = false;
        betOnGreen = false;
        totalBetAmount = 0;
        numberBets.clear();
        betAmount.setText("");
        numberSelection.getSelectionModel().clearSelection();
        currentBetsLabel.setText("No bets placed.");
        updateCurrentBetsDisplay();
        currentBetsLabel.setText("");
    }
}
