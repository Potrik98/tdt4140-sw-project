package tdt4140.gr1809.app.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tdt4140.gr1809.app.client.DataGeneratorHeartbeat;

public class FxApp extends Application {

	
	public FxAppController controller;
    @Override
    public void start(Stage stage) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("FxApp.fxml"));
    	
        Scene scene = new Scene(loader.load());
        controller = loader.<FxAppController>getController();
        controller.setController(controller);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
