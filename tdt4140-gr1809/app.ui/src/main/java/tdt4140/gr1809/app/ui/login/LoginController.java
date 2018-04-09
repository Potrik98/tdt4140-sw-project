package tdt4140.gr1809.app.ui.login;

import javafx.fxml.FXML;
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
	@FXML private Label ErrorLabel;

	
	private FxAppController fxAppController;
	
	@FXML
	private void initialLoginRequest() throws IOException {
		String username = UsernameTextfield.getText();
		UUID uid = getUUIDByUsername();
		if(uid == null) {
			wrongLoginCredentials();
		}
		
		UserClient userclient = new UserClient();
		Optional<User> user = userclient.getUserById(uid);
		
		if(user.isPresent()) {
			fxAppController.user = user.get();
			
			correctLoginCredentials();
			fxAppController.goToProfileView(null);
			fxAppController.setUserNavbar();
		} else {
			wrongLoginCredentials();
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

	private void wrongLoginCredentials() {
		//Clear login textboxes (use if username/password is wrong)
		UsernameTextfield.setText("");
		PasswordTextfield.setText("");
		ErrorLabel.setVisible(true);
	}
	
	private void correctLoginCredentials() {
		UsernameTextfield.setText("");
		PasswordTextfield.setText("");
		ErrorLabel.setVisible(false);
	}
	
	private UUID getUUIDByUsername() {
		try {
			UUID uid = UUID.fromString(UsernameTextfield.getText());
			return uid;
		} catch (Exception e) {
			UsernameTextfield.setText("");
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
