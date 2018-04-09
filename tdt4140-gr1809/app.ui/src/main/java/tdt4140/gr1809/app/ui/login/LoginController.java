package tdt4140.gr1809.app.ui.login;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.ui.FxAppController;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class LoginController {
	@FXML private TextField UsernameTextfield;
	@FXML private TextField PasswordTextfield;
	@FXML private Label LoginStatus;


	
	private FxAppController fxAppController;
	
	@FXML
	private void initialLoginRequest() throws IOException {
		String username = "Invalid username";
		
		try{
			LoginStatus.setText("");
			username = UsernameTextfield.getText();
			UUID uid = UUID.fromString(username);

			UserClient userclient = new UserClient();
			Optional<User> user = userclient.getUserById(uid);
			fxAppController.user = user.get();
			fxAppController.goToProfileView(null);
			fxAppController.setUserNavbar();
		}
		catch (Exception e){
			System.err.println(e);

			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Login failed");
			alert.setHeaderText("Login Failed");
			alert.setContentText("user\n" + username + "\ndoes not exist");
			alert.showAndWait();
		}
		

	}
	
	@FXML
	private void serviceproviderLoginRequest() throws IOException {
		fxAppController.goToServiceproviderLoginView(null);
	}
	
	@FXML
	private void initialCreateuserRequest() throws IOException {
			fxAppController.goToRegisterView(null);
	}

	private void WrongLoginCredentials() {
		//Clear login textboxes (use if username/password is wrong)
		UsernameTextfield.setText("");
		PasswordTextfield.setText("");
		LoginStatus.setText("Wrong login credentials");
	}
	
	private void CorrectLoginCredentials() {
		LoginStatus.setText("Welcome " + UsernameTextfield.getText());
		UsernameTextfield.setText("");
		PasswordTextfield.setText("");
	}
	
	private UUID getUUIDUsername() {
		try {
			UUID uid = UUID.fromString(UsernameTextfield.getText());
			return uid;
		} catch (Exception e) {
			UsernameTextfield.setText("");
			LoginStatus.setText("Invalid UUID as username");
			return null;
		}
	}
	
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		if(fxAppController.user != null) {
		UsernameTextfield.setText(fxAppController.user.getId().toString());
		}
	}
}
