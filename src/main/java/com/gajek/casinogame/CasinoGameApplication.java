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

        // Uzyskaj kontroler dla casinoDashboard.fxml
        MainMenuController mainMenuController = fxmlLoader.getController();

        // Jeżeli kontroler implementuje interfejs umożliwiający ustawienie stage,
        // to przekaż do niego aktualny stage.
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
