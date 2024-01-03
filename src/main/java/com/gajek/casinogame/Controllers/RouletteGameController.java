package com.gajek.casinogame.Controllers;

import com.gajek.casinogame.Observer.BalanceObserver;
import com.gajek.casinogame.Observer.BetsObserver;
import com.gajek.casinogame.Observer.RouletteGame;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class RouletteGameController {

    @FXML
    private Button betRedButton;
    @FXML
    private Button betBlackButton;
    @FXML
    private Button betGreenButton;
    @FXML
    private Button returnToMenuButton;
    @FXML
    private Circle ball;
    @FXML
    private ImageView rouletteWheelImage;
    @FXML
    private ListView<Integer> numberSelection;
    @FXML
    private TextField betAmount;
    @FXML
    private Label balanceLabel; // Label to display the balance
    @FXML
    private Label currentBetsLabel; // Label to display current bets
    @FXML
    private Label resultLabel; // Label to display current bets

    private RouletteGame gameModel = new RouletteGame();
    private BalanceObserver balanceObserver;
    private BetsObserver betsObserver;

    public void initialize() {
        for (int i = 0; i <= 36; i++) {
            numberSelection.getItems().add(i);
        }

        // Ustaw tryb wielokrotnego wyboru
        numberSelection.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        gameModel.setBalance(1000);
        balanceObserver = new BalanceObserver(balanceLabel);
        gameModel.attach(balanceObserver);

        // Utworzenie obserwatora zakładów i jego dołączenie
        betsObserver = new BetsObserver(currentBetsLabel);
        gameModel.attach(betsObserver);
    }

    @FXML
    private void returnToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gajek/casinogame/casinoDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) returnToMenuButton.getScene().getWindow();
            Scene scene = new Scene(root, 800, 800);
            stage.setScene(scene);
            stage.show();

            // Ponowne ustawienie mainStage w kontrolerze menu głównego
            MainMenuController controller = loader.getController();
            if (controller instanceof IStageAwareController) {
                ((IStageAwareController) controller).setMainStage(stage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void confirmBets() {
        List<Integer> selectedNumbers = numberSelection.getSelectionModel().getSelectedItems();
        double betValue = 0;

        if (betAmount.getText().isEmpty() && !gameModel.getBetOnRed() && !gameModel.getBetOnBlack() && !gameModel.getBetOnGreen()) {
            resultLabel.setText("Proszę wpisać kwotę zakładu lub wybrać kolor.");
            return;
        }

        try {
            betValue = Double.parseDouble(betAmount.getText());
        } catch (NumberFormatException e) {
            resultLabel.setText("Proszę wpisać poprawną kwotę zakładu.");
            return;
        }

        gameModel.updateBets(selectedNumbers, betValue, gameModel.getBetOnRed(), gameModel.getBetOnBlack(), gameModel.getBetOnGreen());


        if (gameModel.getBalance() <= 0) {
            resultLabel.setText("Game Over. You've run out of funds!");
        }

    }

    @FXML
    private void spinRoulette() {
        // Tworzymy ścieżkę animacji wokół koła ruletki
        if (betAmount.getText().isEmpty() && !gameModel.getBetOnRed() && !gameModel.getBetOnBlack() && !gameModel.getBetOnGreen()) {
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
            int result = spinWheel();
            displayResult(result);
        });

        transition.play();
    }


    // Metody wywoływane po naciśnięciu przycisków zakładów
    public void betRed(ActionEvent actionEvent) {
        resultLabel.setText("Bet placed on Red");
        if (gameModel.getBetOnRed()) {
            gameModel.setBetOnColor("Red", false);
            betRedButton.getStyleClass().remove("active");
        } else {
            gameModel.setBetOnColor("Red", true);
            betRedButton.getStyleClass().add("active");
        }
    }

    public void betBlack(ActionEvent actionEvent) {
        resultLabel.setText("Bet placed on Black");
        if (gameModel.getBetOnBlack()) {
            gameModel.setBetOnColor("Black", false);
            betBlackButton.getStyleClass().remove("active");
        } else {
            gameModel.setBetOnColor("Black", true);
            betBlackButton.getStyleClass().add("active");
        }
    }

    public void betGreen(ActionEvent actionEvent) {
        resultLabel.setText("Bet placed on Green");
        if (gameModel.getBetOnGreen()) {
            gameModel.setBetOnColor("Green", false);
            betGreenButton.getStyleClass().remove("active");
        } else {
            gameModel.setBetOnColor("Green", true);
            betGreenButton.getStyleClass().add("active");
        }
    }

    // Pomocnicza metoda do symulacji obrotu koła ruletki
    private int spinWheel() {
        Random random = new Random();
        return random.nextInt(37);
    }

    private void displayResult(int number) {
        // Pobierz kolor dla wylosowanego numeru
        StringBuilder details = new StringBuilder();
        String color = gameModel.getColorForNumber(number);
        boolean win = gameModel.checkWin(number, color);

        // Przetwórz wynik w modelu gry, który zaktualizuje stan i powiadomi obserwatorów
        gameModel.processResult(number, color);
        if (win) {
            // Append details of winning bets
            details.append(String.format("Number %d - %s hit! Payout: %.2f", number, color, gameModel.calculatePayout(number, color)));
        } else {
            // Append message for no wins
            details.append(String.format("Number %d - %s. No win this time.", number, color));
        }
        resultLabel.setText(details.toString());
        gameModel.resetBets();
        betRedButton.getStyleClass().remove("active");
        betBlackButton.getStyleClass().remove("active");
        betGreenButton.getStyleClass().remove("active");
    }


    // Pomocnicza metoda do resetowania zakładów
    @FXML
    private void resetBets() {
        gameModel.setBetOnColor("Red", false);
        gameModel.setBetOnColor("Black", false);
        gameModel.setBetOnColor("Green", false);
        betAmount.setText("");
        currentBetsLabel.setText("No bets placed.");
        numberSelection.getSelectionModel().clearSelection();
        gameModel.resetBets();
    }
}

