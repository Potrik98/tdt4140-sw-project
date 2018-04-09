package tdt4140.gr1809.app.ui;

import java.io.IOException;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import tdt4140.gr1809.app.ui.login.LoginController;
import tdt4140.gr1809.app.ui.login.ServiceproviderLoginController;
import tdt4140.gr1809.app.ui.view.*;


public class FxAppController implements Initializable{

	
	@FXML private Pane rightPane;

	@FXML private VBox userNavbar;
	@FXML private VBox serviceProviderNavbar;

	@FXML private Button heartRateViewButton;
	@FXML private Button profileViewButton;

	private RegisterViewController registerViewController;
	private RegisterServiceProviderViewController registerServiceProviderViewController;
	private ProfileViewController profileviewController;
	private ClientsViewController clientsViewController;
	private ServiceProviderViewController serviceProviderViewController;
	private LoginController loginController;
	private GraphViewController graphViewController;
	public HeartRateViewController heartRateViewController;
	public FxAppController Appcontroller;
	public ServiceproviderLoginController serviceProviderLoginViewController;
	public TimeLimitViewController timelimitViewController;

	public User user;
	public ServiceProvider serviceProvider;
	public ServiceProvider lastLoggedInServiceProvider;
	public User lastLoggedInUser;


	public void goToRegisterView(ActionEvent event) throws IOException{
		rightPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("RegisterView.fxml"));
		loader.load();
		registerViewController = loader.getController();
		registerViewController.setfxAppController(this);
		rightPane.getChildren().add(loader.getRoot());
	}
	public void goToTimeLimitView(ActionEvent event) throws IOException {
		rightPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("TimeLimitView.fxml"));
        loader.load();
        timelimitViewController = loader.getController();
        timelimitViewController.setfxAppController(this);
        rightPane.getChildren().add(loader.getRoot());
	}

	public void goToServiceProviderRegisterView(ActionEvent event) throws IOException{
		rightPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("RegisterServiceProviderView.fxml"));
		loader.load();
		registerServiceProviderViewController = loader.getController();
		registerServiceProviderViewController.setfxAppController(this);
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

	public void goToClientView(ActionEvent event) throws IOException{
		rightPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("ClientsView.fxml"));
		loader.load();
		clientsViewController = loader.getController();
		clientsViewController.setfxAppController(this);
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
		lastLoggedInUser = user;
		lastLoggedInServiceProvider = serviceProvider;
		userNavbar.setVisible(false);
		serviceProviderNavbar.setVisible(false);
		user = null;
		serviceProvider = null;
		goToLoginView(null);
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

	public void goToServiceProviderView(ActionEvent event) throws IOException {
		rightPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("ServiceProviderView.fxml"));
		loader.load();
		serviceProviderViewController = loader.getController();
		serviceProviderViewController.setfxAppController(this);
		rightPane.getChildren().add(loader.getRoot());
	}



	public void initialize(URL arg0, ResourceBundle arg1){
		rightPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Login.fxml"));
		userNavbar.setVisible(false);
		serviceProviderNavbar.setVisible(false);

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


	public void setUserNavbar() {
		userNavbar.setVisible(true);
		serviceProviderNavbar.setVisible(false);
	}

	public void setServiceProviderNavbar() {
		userNavbar.setVisible(false);
		serviceProviderNavbar.setVisible(true);
	}

	public void disableDataView() {
		heartRateViewButton.setDisable(true);
		profileViewButton.setDisable(true);
	}

	public void enableDataView() {
		heartRateViewButton.setDisable(false);
		profileViewButton.setDisable(false);
		profileViewButton.setText(user.getFirstName() + " " + user.getLastName());
	}
}
