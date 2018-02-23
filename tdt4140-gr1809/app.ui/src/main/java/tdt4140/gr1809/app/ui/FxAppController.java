package tdt4140.gr1809.app.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;


public class FxAppController implements Initializable {
	@FXML private TextField UsernameTextfield;
	@FXML private TextField PasswordTextfield;
	
	
	@FXML
	private void initialLoginRequest() {
		String username = UsernameTextfield.getText();
		String password = PasswordTextfield.getText();
		
		//Login request to controller
	}
	
	@FXML
	private void initialCreateuserRequest() {
		String username = UsernameTextfield.getText();
		String password = PasswordTextfield.getText();
		
		//Create User request to controller in main java code
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Kode som starter når man åpner applikasjonen
		
	}
}
