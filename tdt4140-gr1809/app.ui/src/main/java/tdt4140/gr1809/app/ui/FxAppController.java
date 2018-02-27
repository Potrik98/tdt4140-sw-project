package tdt4140.gr1809.app.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;


public class FxAppController implements Initializable {
	@FXML private TextField UsernameTextfield;
	@FXML private TextField PasswordTextfield;
	@FXML private Label LoginStatus;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// If you want something to run when the application starts, put it here
		
	}
	
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
		
		//TODO kode for å åpne vinduet som gir info til brukeren
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
