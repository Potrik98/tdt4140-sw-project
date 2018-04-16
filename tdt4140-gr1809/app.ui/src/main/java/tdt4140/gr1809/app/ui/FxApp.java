package tdt4140.gr1809.app.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class FxApp extends Application {

	
	public FxAppController controller;
    @Override
    public void start(Stage stage) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("FxApp.fxml"));
    	
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        controller = loader.<FxAppController>getController();
        controller.setController(controller);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/icon.png")));
        stage.setScene(scene);
        stage.setTitle("Health Monitor 9000");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
