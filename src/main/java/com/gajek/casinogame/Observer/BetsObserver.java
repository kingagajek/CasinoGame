package com.gajek.casinogame.Observer;

import com.gajek.casinogame.Models.RouletteGame;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class BetsObserver implements IObserver {
    private Label betsLabel;

    public BetsObserver(Label betsLabel) {
        this.betsLabel = betsLabel;
    }

    @Override
    public void update(RouletteGame game) {
        Platform.runLater(() -> {
            String betsText = game.getCurrentBetsText();
            betsLabel.setText(betsText);
        });
    }

}