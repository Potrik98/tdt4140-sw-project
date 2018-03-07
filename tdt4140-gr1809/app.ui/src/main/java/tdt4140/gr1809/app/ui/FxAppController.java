package tdt4140.gr1809.app.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.Pane;


public class FxAppController implements Initializable{

	
	@FXML private Pane rightPane;

	public void goToRegisterView(ActionEvent event) throws IOException{
		rightPane.getChildren().clear();
		rightPane.getChildren().add(FXMLLoader.load(getClass().getResource("RegisterView.fxml")));
	}

	public void goToProfileView(ActionEvent event) throws IOException{
		rightPane.getChildren().clear();
		rightPane.getChildren().add(FXMLLoader.load(getClass().getResource("ProfileView.fxml")));
	}

	public void goToLoginView(ActionEvent event) throws IOException{
		rightPane.getChildren().clear();
		rightPane.getChildren().add(FXMLLoader.load(getClass().getResource("Login.fxml")));
	}

	public void goToGraphView(ActionEvent event) throws IOException {
		rightPane.getChildren().clear();
		rightPane.getChildren().add(FXMLLoader.load(getClass().getResource("GraphView.fxml")));
	}

	public void goToHeartRateView(ActionEvent event) throws IOException {
		rightPane.getChildren().clear();
		rightPane.getChildren().add(FXMLLoader.load(getClass().getResource("HeartRateView.fxml")));
	}



	public void initialize(URL arg0, ResourceBundle arg1){
		// TODO Auto-generated method stub
		rightPane.getChildren().clear();
		try {
			rightPane.getChildren().add(FXMLLoader.load(getClass().getResource("Login.fxml")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
