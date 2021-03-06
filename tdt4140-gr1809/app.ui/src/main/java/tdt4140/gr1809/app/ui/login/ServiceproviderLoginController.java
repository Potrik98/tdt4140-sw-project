package tdt4140.gr1809.app.ui.login;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import tdt4140.gr1809.app.client.ServiceProviderClient;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.ui.FxAppController;

public class ServiceproviderLoginController {
	@FXML private TextField UsernameTextfield;
	@FXML private TextField PasswordTextfield;
	@FXML private Label passwordError;

	
	private FxAppController fxAppController;
	
	@FXML
	private void initialLoginRequest() throws IOException {
		String username = "Invalid UUID";
		if(validatePassword()) {
			try {
				username = UsernameTextfield.getText();
				UUID uid = UUID.fromString(username);
				ServiceProviderClient serviceproviderclient = new ServiceProviderClient();
				Optional<ServiceProvider> serviceprovider = serviceproviderclient.getServiceProviderById(uid);
				fxAppController.serviceProvider = serviceprovider.get();
				fxAppController.lastLoggedInServiceProvider = serviceprovider.get();
				fxAppController.goToClientView(null);
				fxAppController.setUserNavbar();

				fxAppController.setServiceProviderNavbar();
				fxAppController.disableDataView(); //disable until a user is selected
			} catch (Exception e) {
				System.err.println(e);

				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Login failed");
				alert.setHeaderText("Login Failed");
				alert.setContentText("service provider\n" + username + "\ndoes not exist");
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
	private void goToLoginView() throws IOException {
		fxAppController.goToLoginView(null);
	}

	public void createUserButtonPressed() throws IOException {
		fxAppController.goToServiceProviderRegisterView(null);

	}
	
	
	
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		if(fxAppController.serviceProvider != null) {
		UsernameTextfield.setText(fxAppController.serviceProvider.getId().toString());
		}
		else if(fxAppController.lastLoggedInServiceProvider != null) {
			UsernameTextfield.setText(fxAppController.lastLoggedInServiceProvider.getId().toString());
		}
	}
}
