package com.gajek.casinogame.Controllers;

import com.gajek.casinogame.IGameFactory;
import com.gajek.casinogame.RouletteGameFactory;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    private Stage mainStage;
    @FXML
    private void playRoulette() {
        IGameFactory factory = new RouletteGameFactory();
        Scene rouletteScene = factory.createGameScene();
        if (rouletteScene != null && mainStage != null) {
            mainStage.setScene(rouletteScene);
            mainStage.show();
        }
    }


    @FXML
    private void playBlackjack() {
        // TODO: Implement the action when the Play Blackjack button is clicked
        System.out.println("Play Blackjack button clicked");
    }

    @FXML
    private void playSlots() {
        // TODO: Implement the action when the Play Slots button is clicked
        System.out.println("Play Slots button clicked");
    }
}
