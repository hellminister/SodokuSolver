package ca.dauqui.sodokusolver.gui;

import ca.dauqui.sodokusolver.localization.Localize;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainWindow extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Localize.setLanguage("francais");

        MainScene mainScene = new MainScene();

        stage.titleProperty().bind(LocalizedText.TITLE.localizedProperty());

        stage.setScene(mainScene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }
}