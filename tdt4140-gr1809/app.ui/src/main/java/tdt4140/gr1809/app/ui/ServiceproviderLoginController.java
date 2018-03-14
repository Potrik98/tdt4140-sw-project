package tdt4140.gr1809.app.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ServiceproviderLoginController {
	@FXML private TextField UsernameTextfield;
	@FXML private TextField PasswordTextfield;
	@FXML private Label LoginStatus;
	@FXML private Button graphViewButton;
	@FXML private Button heartRateViewButton;
	@FXML private AnchorPane NavBar;
	
	private FxAppController fxAppController;
	
	@FXML
	private void initialLoginRequest() {
		
	}
	
	
	
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		if(fxAppController.user != null) {
		UsernameTextfield.setText(fxAppController.user.getId().toString());
		}
	}
}
