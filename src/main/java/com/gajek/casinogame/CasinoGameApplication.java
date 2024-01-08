package com.gajek.casinogame;

import com.gajek.casinogame.Controllers.IStageAwareController;
import com.gajek.casinogame.Controllers.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CasinoGameApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CasinoGameApplication.class.getResource("/com/gajek/casinogame/casinoDashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);

        MainMenuController mainMenuController = fxmlLoader.getController();

        if (mainMenuController instanceof IStageAwareController) {
            ((IStageAwareController) mainMenuController).setMainStage(stage);
        }

        stage.setTitle("Welcome to the Casino!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
