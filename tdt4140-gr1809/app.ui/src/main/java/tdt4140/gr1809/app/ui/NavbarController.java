package tdt4140.gr1809.app.ui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NavbarController {

	public void goToLoginView(ActionEvent event) throws IOException{
		//first get graphView
				Parent graphView = FXMLLoader.load(getClass().getResource("FxApp.fxml"));
				Scene graphViewScene = new Scene(graphView);

				Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
				window.setScene(graphViewScene);
				window.show();
	}

	public void goToGraphView(ActionEvent event) throws IOException {

		//first get graphView
		Parent graphView = FXMLLoader.load(getClass().getResource("GraphView.fxml"));
		Scene graphViewScene = new Scene(graphView);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(graphViewScene);
		window.show();
	}

	public void goToHeartRateView(ActionEvent event) throws IOException {

		//first get graphView
		Parent graphView = FXMLLoader.load(getClass().getResource("HeartRateView.fxml"));
		Scene graphViewScene = new Scene(graphView);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(graphViewScene);
		window.show();
	}
}
