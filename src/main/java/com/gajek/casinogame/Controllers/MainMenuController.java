package com.gajek.casinogame.Controllers;

import com.gajek.casinogame.Factory.BlackjackGameFactory;
import com.gajek.casinogame.Factory.IGameFactory;
import com.gajek.casinogame.Factory.RouletteGameFactory;
import com.gajek.casinogame.Factory.SlotsGameFactory;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController implements IStageAwareController{

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
        IGameFactory factory = new BlackjackGameFactory();
        Scene slotsScene = factory.createGameScene();
        if (slotsScene != null && mainStage != null) {
            mainStage.setScene(slotsScene);
            mainStage.show();
        }
    }

    @FXML
    private void playSlots() {
        IGameFactory factory = new SlotsGameFactory();
        Scene slotsScene = factory.createGameScene();
        if (slotsScene != null && mainStage != null) {
            mainStage.setScene(slotsScene);
            mainStage.show();
        }
    }

    @Override
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
