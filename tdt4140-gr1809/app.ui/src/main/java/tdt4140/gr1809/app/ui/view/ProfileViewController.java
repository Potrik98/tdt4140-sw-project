package tdt4140.gr1809.app.ui.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.ui.FxAppController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileViewController implements Initializable {

	@FXML Label nameLabel;
	@FXML Label birthdateLabel;
	@FXML Label genderLabel;
	
	private FxAppController fxAppController;

	//TODO: validate user input

	@FXML
	public void deleteUser() throws IOException {
		final UserClient client = new UserClient();
		client.deleteUser(fxAppController.user.getId());
		fxAppController.goToLoginView(null);
		fxAppController.changeNavbarVisibility(false);
	}

	@Override
	public void initialize(final URL url, final ResourceBundle rb) {

	}
	
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		System.out.println(fxAppController);
		User user = fxAppController.user;
		ServiceProvider sp = fxAppController.serviceProvider;
		
		if(user != null) {
			nameLabel.setText(user.getFirstName() + " " + user.getLastName());
			genderLabel.setText(user.getGender());
			birthdateLabel.setText(fxAppController.user.getBirthDate().toLocalDate().toString());
		}else {
			nameLabel.setText(sp.getFirstName() + " " + sp.getLastName());
			genderLabel.setText(sp.getGender());
			birthdateLabel.setText(sp.getBirthDate().toLocalDate().toString());
		}
	}

}
