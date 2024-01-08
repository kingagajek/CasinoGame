package com.gajek.casinogame.Controllers;

import com.gajek.casinogame.Strategy.PayoutStrategy;
import com.gajek.casinogame.Strategy.SlotMachine;
import com.gajek.casinogame.Strategy.StandardPayoutStrategy;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class SlotsGameController {
    @FXML
    private Button returnToMenuButton;
    @FXML
    private TextField betAmount;
    @FXML
    private ImageView reel1View;
    @FXML
    private ImageView reel2View;
    @FXML
    private ImageView reel3View;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label payoutLabel;
    @FXML
    private Label messageLabel;
    private PayoutStrategy payoutStrategy = new StandardPayoutStrategy();
    private int bet;
    private SlotMachine slotMachine;

    @FXML
    private void returnToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gajek/casinogame/casinoDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) returnToMenuButton.getScene().getWindow();
            Scene scene = new Scene(root, 800, 800);
            stage.setScene(scene);
            stage.show();

            MainMenuController controller = loader.getController();
            if (controller instanceof IStageAwareController) {
                ((IStageAwareController) controller).setMainStage(stage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
    }

    private void clearMessage() {
        messageLabel.setText("");
    }

    private void updateBalanceDisplay() {
        balanceLabel.setText("Balance: " + slotMachine.getBalance());
    }

    private void updatePayoutDisplay(int payout) {
        payoutLabel.setText("Payout: " + payout);
    }

    private void updateReelViews(List<Image> spinResults) {
        reel1View.setImage(spinResults.get(0));
        reel2View.setImage(spinResults.get(1));
        reel3View.setImage(spinResults.get(2));
    }

    @FXML
    private void betMax() {
        int maxBet = slotMachine.calculateMaxBet();
        betAmount.setText(String.valueOf(maxBet));
    }

    @FXML
    private void spinSlots() {
        try {
            int betValue = Integer.parseInt(betAmount.getText());
            if (slotMachine.validateAndSetBet(betValue)) {
                bet = betValue;
                int payout = slotMachine.spinAndCalculatePayout(bet);
                updateReelViews(slotMachine.getLastSpinResults());
                updateBalanceDisplay();
                updatePayoutDisplay(payout);
                clearMessage();
            } else {
                showMessage("Incorrect bet amount or insufficient funds.");
            }
        } catch (NumberFormatException e) {
            showMessage("Incorrect bet amount.");
        }
    }

    @FXML
    public void initialize() {
        List<Map<Image, Integer>> symbolsForReels = new ArrayList<>();

        Map<Image, Integer> weightedSymbols = new HashMap<>();
        weightedSymbols.put(new Image(getClass().getResource("/images/seven.png").toExternalForm()), 1); // Bardzo rzadkie
        weightedSymbols.put(new Image(getClass().getResource("/images/cherry.png").toExternalForm()), 10); // Bardzo częste
        weightedSymbols.put(new Image(getClass().getResource("/images/lemon.png").toExternalForm()), 8); // Częste
        weightedSymbols.put(new Image(getClass().getResource("/images/bar.png").toExternalForm()), 2); // Rzadkie
        weightedSymbols.put(new Image(getClass().getResource("/images/bell.png").toExternalForm()), 5); // Średnio częste
        weightedSymbols.put(new Image(getClass().getResource("/images/star.png").toExternalForm()), 3); // Dość rzadkie
        weightedSymbols.put(new Image(getClass().getResource("/images/watermelon.png").toExternalForm()), 7); // Częste

        symbolsForReels.add(weightedSymbols); // Bęben 1
        symbolsForReels.add(weightedSymbols); // Bęben 2
        symbolsForReels.add(weightedSymbols);

        slotMachine = new SlotMachine(symbolsForReels, payoutStrategy);
        updateBalanceDisplay();
    }
}
