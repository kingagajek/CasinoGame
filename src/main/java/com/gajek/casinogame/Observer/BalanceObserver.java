package com.gajek.casinogame.Observer;

import com.gajek.casinogame.Models.RouletteGame;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class BalanceObserver implements IObserver {
    private Label balanceLabel;

    public BalanceObserver(Label balanceLabel) {
        this.balanceLabel = balanceLabel;
    }

    @Override
    public void update(RouletteGame game) {
        Platform.runLater(() -> balanceLabel.setText(String.format("Balance: %.2f", game.getBalance())));
    }
}