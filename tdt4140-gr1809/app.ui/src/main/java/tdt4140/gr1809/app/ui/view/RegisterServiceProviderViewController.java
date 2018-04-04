package tdt4140.gr1809.app.ui.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import tdt4140.gr1809.app.client.ServiceProviderClient;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.ui.FxAppController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterServiceProviderViewController implements Initializable {

	@FXML Button registerProfileButton;
	@FXML Label firstNameLabel;
	@FXML Label lastNameLabel;
	@FXML Label birthdateLabel;
	@FXML Label genderLabel;

	@FXML TextField firstNameInput;
	@FXML TextField lastNameInput;
	@FXML DatePicker birthdateInput;
	@FXML ChoiceBox genderChoiceBox;

	private FxAppController fxAppController;

	public void clearInput(){
		firstNameInput.setText("");
		lastNameInput.setText("");
		birthdateInput.getEditor().clear();
		genderChoiceBox.setValue("Male");
	}

	public void registerButtonClicked(ActionEvent event) throws IOException{

		final ServiceProviderClient client = new ServiceProviderClient();

		final ServiceProvider serviceProvider = ServiceProvider.builder()
				.firstName(firstNameInput.getText())
				.lastName(lastNameInput.getText())
				.gender(genderChoiceBox.getValue().toString())
				.birthDate(birthdateInput.getValue().atStartOfDay())
				.build();
		client.createServiceProvider(serviceProvider);

		clearInput();
		System.out.println("Created serviceProvider: " + serviceProvider.getId());
		fxAppController.serviceProvider= serviceProvider;
		fxAppController.goToLoginView(null);
	}




	@Override
	public void initialize(final URL url, final ResourceBundle rb) {
		genderChoiceBox.getItems().addAll("Male", "Female");
		genderChoiceBox.setValue("Male");

	}

	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
	}
}
