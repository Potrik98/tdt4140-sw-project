package tdt4140.gr1809.app.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class FxAppController implements Initializable {
	@FXML private TextField testTextField;
	@FXML private Button graphViewButton;
	@FXML private Button heartRateViewButton;




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
	
	@FXML
	private void initialLoginRequest() {
		testTextField.setText(testTextField.getText() + "hei");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Kode som starter n�r man �pner applikasjonen
		
	}
}
