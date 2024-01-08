package com.gajek.casinogame.Observer;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.scene.control.Label;

public class BetsObserver implements Observer {
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