package com.gajek.casinogame.Controllers;

import com.gajek.casinogame.Strategy.SlotMachine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SlotsGameController {

    @FXML
    private Button returnToMenuButton;
    @FXML
    private TextField betAmount;

    private int balance; // saldo użytkownika
    private int bet; // aktualny zakład użytkownika
    private SlotMachine slotMachine;

    @FXML
    private ImageView reel1View;
    @FXML
    private ImageView reel2View;
    @FXML
    private ImageView reel3View;

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
    public void spin() {
        // Wywołanie metody spinReels() na obiekcie slotMachine
        List<Image> spinResults = slotMachine.spinReels();

        // Aktualizacja ImageView w interfejsie użytkownika za pomocą wyników spinu
        reel1View.setImage(spinResults.get(0));
        reel2View.setImage(spinResults.get(1));
        reel3View.setImage(spinResults.get(2));
    }

    @FXML
    private void betMax() {
        // Ustaw maksymalny możliwy zakład
        // To może być np. maksymalna wartość, jaką użytkownik może postawić w zależności od salda
        int maxBet = calculateMaxBet();
        betAmount.setText(String.valueOf(maxBet));
        bet = maxBet;
        // Możesz również automatycznie uruchomić spin po ustawieniu maksymalnego zakładu
        // spinSlots();
    }

    // Metoda do obliczania maksymalnego zakładu
    private int calculateMaxBet() {
        // Tu zdefiniuj logikę obliczania maksymalnego zakładu
        return Math.min(balance, 100);
    }

    @FXML
    public void initialize() {
        // Inicjalizacja stanu gry, ustaw saldo początkowe, itp.
        balance = 1000;
        List<Image> reel1Symbols = Arrays.asList(
                new Image(getClass().getResource("/images/seven-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/bar-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/bell-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/cherries-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/lemon-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/watermelon-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/star-slot-game.png").toExternalForm())
        );

        List<Image> reel2Symbols = Arrays.asList(
                new Image(getClass().getResource("/images/seven-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/bar-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/bell-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/cherries-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/lemon-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/watermelon-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/star-slot-game.png").toExternalForm())
        );

        List<Image> reel3Symbols = Arrays.asList(
                new Image(getClass().getResource("/images/seven-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/bar-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/bell-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/cherries-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/lemon-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/watermelon-slot-game.png").toExternalForm()),
                new Image(getClass().getResource("/images/star-slot-game.png").toExternalForm())
        );

        // Tutaj tworzymy listę list obrazów, gdzie każda lista reprezentuje bęben
        List<List<Image>> symbolsForReels = Arrays.asList(reel1Symbols, reel2Symbols, reel3Symbols);

        // Utworzenie maszyny slotowej z załadowanymi symbolami
        slotMachine = new SlotMachine(symbolsForReels);
    }
}
