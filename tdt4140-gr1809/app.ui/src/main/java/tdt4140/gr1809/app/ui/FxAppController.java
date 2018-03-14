package tdt4140.gr1809.app.ui;

import java.io.IOException;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tdt4140.gr1809.app.core.model.User;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;


public class FxAppController implements Initializable{

	
	@FXML private Pane rightPane;
	@FXML private AnchorPane NavBar;

	private RegisterViewController registerViewController;
	private ProfileViewController profileviewController;
	private LoginController loginController;
	private GraphViewController graphViewController;
	public HeartRateViewController heartRateViewController;
	public FxAppController Appcontroller;
	public ServiceproviderLoginController serviceProviderLoginViewController;
	public User user;


	public void goToRegisterView(ActionEvent event) throws IOException{
		rightPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("RegisterView.fxml"));
		loader.load();
		registerViewController = loader.getController();
		registerViewController.setfxAppController(this);
		rightPane.getChildren().add(loader.getRoot());
	}

	public void goToProfileView(ActionEvent event) throws IOException{
		rightPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProfileView.fxml"));
        loader.load();
        profileviewController = loader.getController();
		profileviewController.setfxAppController(this);
        rightPane.getChildren().add(loader.getRoot());
	}

	public void goToLoginView(ActionEvent event) throws IOException{
		rightPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Login.fxml"));
        loader.load();
        loginController = loader.getController();
        loginController.setfxAppController(this);
        rightPane.getChildren().add(loader.getRoot());
	}
	
	@FXML
	public void logout() throws IOException {
		changeNavbarVisibility(false);
		user = null;
		goToLoginView(null);
	}

	public void goToGraphView(ActionEvent event) throws IOException {
		rightPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GraphView.fxml"));
        loader.load();
        graphViewController = loader.getController();
        graphViewController.setfxAppController(this);
        rightPane.getChildren().add(loader.getRoot());
	}

	public void goToHeartRateView(ActionEvent event) throws IOException {
		rightPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("HeartRateView.fxml"));
        loader.load();
        heartRateViewController = loader.getController();
        heartRateViewController.setfxAppController(this);        
        rightPane.getChildren().add(loader.getRoot());
        }
	
	public void goToServiceproviderLoginView(ActionEvent event) throws IOException {
		rightPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ServiceproviderLogin.fxml"));
        loader.load();
        serviceProviderLoginViewController = loader.getController();
        serviceProviderLoginViewController.setfxAppController(this);        
        rightPane.getChildren().add(loader.getRoot());
        }



	public void initialize(URL arg0, ResourceBundle arg1){
		changeNavbarVisibility(false);
		rightPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Login.fxml"));

		try {
	        loader.load();
	        loginController = loader.getController();
	        loginController.setfxAppController(this);
	        rightPane.getChildren().add(loader.getRoot());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setController(FxAppController controller) {
		Appcontroller = controller;
	}
	
	public void changeNavbarVisibility(boolean change) {
			NavBar.setVisible(change);
			NavBar.setDisable(!change);

	}
}
