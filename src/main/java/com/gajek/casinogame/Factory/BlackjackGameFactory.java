package com.gajek.casinogame.Factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class BlackjackGameFactory implements IGameFactory {
    @Override
    public Scene createGameScene() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gajek/casinogame/BlackjackGame.fxml"));
            return new Scene(root, 800, 800);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}