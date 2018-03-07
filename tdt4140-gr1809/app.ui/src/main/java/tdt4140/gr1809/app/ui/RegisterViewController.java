package tdt4140.gr1809.app.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.core.model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterViewController implements Initializable {

	@FXML Button registerProfileButton;



	@FXML Label firstNameLabel;
	@FXML Label lastNameLabel;
	@FXML Label birthdateLabel;
	@FXML Label genderLabel;

	@FXML TextField firstNameInput;
	@FXML TextField lastNameInput;
	@FXML DatePicker birthdateInput;
	@FXML ChoiceBox genderChoiceBox;


	public void registerButtonClicked(ActionEvent event){
		//TODO: send data to database

		final UserClient client = new UserClient();

		final User user = User.builder()
				.firstName(firstNameInput.getText())
				.lastName(lastNameInput.getText())
				.gender(genderChoiceBox.getValue().toString())
				.birthDate(birthdateInput.getValue().atStartOfDay())
				.build();
		client.createUser(user);

	}




	@Override
	public void initialize(final URL url, final ResourceBundle rb) {
		genderChoiceBox.getItems().addAll("Male", "Female");
		genderChoiceBox.setValue("Male");


	}


}
