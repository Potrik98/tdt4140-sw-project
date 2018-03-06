package tdt4140.gr1809.app.ui;

import java.time.LocalDateTime;
import java.util.UUID;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.core.model.User;

public class LoginController {
	@FXML private TextField UsernameTextfield;
	@FXML private TextField PasswordTextfield;
	@FXML private Label LoginStatus;
	@FXML private Button graphViewButton;
	@FXML private Button heartRateViewButton;
	
	@FXML
	private void initialLoginRequest() {
		LoginStatus.setText("");
		String username = getLoginUsername();
		String password = getLoginPassword();
		if(username != null && password != null) {
			//TODO Login request to controller
		}
	}
	
	@FXML
	private void initialCreateuserRequest() {
		LoginStatus.setText("");
		String username = getLoginUsername();
		String password = getLoginPassword();
		if(username != null && password != null) {
			
			//TODO Create User request to controller in main java code
			
			UserClient userClient = new UserClient();
			final User user = new User(null, password, password, password, null);
			userClient.createUser(user);
			UUID id = user.getId();
		}
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
	
	private String getLoginUsername() {
		String username = UsernameTextfield.getText();
		if(!username.matches("[A-Za-z0-9]+")) {
			LoginStatus.setText("Username contains invalid characters");
			return null;
		}
		else if(username.length() < 15 && username.length() < 1) {
			LoginStatus.setText("Username is of incorrect length");
			return null;
		}
		else {
			return username;
		}
	}
	
	private String getLoginPassword() {
		String password = PasswordTextfield.getText();
		if(!password.matches("[A-Za-z0-9]+")) {
			LoginStatus.setText("Password contains invalid characters");
			return null;
		}
		else if(password.length() < 15 && password.length() < 1) {
			LoginStatus.setText("Password is of incorrect length");
			return null;
		}
		else {
			return password;
		}
	}
}
