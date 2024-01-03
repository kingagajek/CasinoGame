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
    public void update(RouletteGame game) { // Aktualizacja parametru wejściowego do RouletteGame
        Platform.runLater(() -> {
            String betsText = game.getCurrentBetsText();  // Metoda w modelu zwraca tekst zakładów
            betsLabel.setText(betsText);  // Aktualizacja UI z nowym tekstem zakładów
        });
    }

}