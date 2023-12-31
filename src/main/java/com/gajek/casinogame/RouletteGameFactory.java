package com.gajek.casinogame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class RouletteGameFactory implements IGameFactory {
    @Override
    public Scene createGameScene() {
        try {
            // Upewnij się, że ścieżka jest poprawna
            Parent root = FXMLLoader.load(getClass().getResource("/com/gajek/casinogame/RouletteGame.fxml"));
            return new Scene(root, 800, 800);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // W prawdziwej aplikacji powinieneś tu obsłużyć wyjątek
        }
    }
}
