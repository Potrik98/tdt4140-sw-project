package tdt4140.gr1809.app.ui.login;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tdt4140.gr1809.app.client.DataClient;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.datagen.generator.Generator;
import tdt4140.gr1809.app.ui.FxAppController;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class LoginController {
	@FXML private TextField UsernameTextfield;
	@FXML private TextField PasswordTextfield;
	@FXML private Label passwordError;

	
	private FxAppController fxAppController;
	
	@FXML
	private void initialLoginRequest() throws IOException {
        String username = "Invalid username";
		if(validatePassword()){
			try {
				username = UsernameTextfield.getText();
				UUID uid = UUID.fromString(username);

				UserClient userclient = new UserClient();
				Optional<User> user = userclient.getUserById(uid);
				fxAppController.user = user.get();
				DataClient dataClient = new DataClient();
				Arrays.stream(DataPoint.DataType.values())
						.forEach(dataType -> dataClient.createDataPoints(
								Generator.getDataGenerator(user.get().getId(), dataType)
								.generateDataPoints(25)));
				fxAppController.goToProfileView(null);
				fxAppController.setUserNavbar();
				fxAppController.lastLoggedInUser = user.get();
			} catch (Exception e) {
				System.err.println(e);

				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Login failed");
				alert.setHeaderText("Login Failed");
				alert.setContentText("user\n" + username + "\ndoes not exist");
				alert.showAndWait();
			}
		}


    }

	public boolean validatePassword(){
		if (PasswordTextfield.getText().isEmpty()){
			passwordError.setVisible(true);
			return false;
		}
		passwordError.setVisible(false);
		return true;
	}
	
	@FXML
	private void serviceproviderLoginRequest() throws IOException {
		fxAppController.goToServiceproviderLoginView(null);
	}
	
	@FXML
	private void initialCreateuserRequest() throws IOException {
			fxAppController.goToRegisterView(null);
	}


	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		if(fxAppController.user != null) {
			UsernameTextfield.setText(fxAppController.user.getId().toString());
		}
		else if(fxAppController.lastLoggedInUser != null) {
			UsernameTextfield.setText(fxAppController.lastLoggedInUser.getId().toString());
		}
	}
}
