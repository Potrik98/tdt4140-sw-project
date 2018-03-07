package tdt4140.gr1809.app.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterViewController implements Initializable {

	@FXML Button editProfileButton;
	@FXML Button saveProfileButton;


	@FXML Label nameLabel;
	@FXML Label phoneLabel;
	@FXML Label genderLabel;

	@FXML TextField nameInput;
	@FXML TextField phoneInput;
	@FXML ChoiceBox genderChoiceBox;


	public void editProfileButtonClicked(ActionEvent event){
		genderChoiceBox.setVisible(true);
		nameInput.setVisible(true);
		phoneInput.setVisible(true);

		nameLabel.setVisible(false);
		phoneLabel.setVisible(false);
		genderLabel.setVisible(false);

		editProfileButton.setVisible(false);
		saveProfileButton.setVisible(true);
	}


	//TODO: validate user input

	public void saveProfileButtonClicked(ActionEvent event){

		//TODO: send updated user data to database


		genderChoiceBox.setVisible(false);
		nameInput.setVisible(false);
		phoneInput.setVisible(false);

		nameLabel.setVisible(true);
		phoneLabel.setVisible(true);
		genderLabel.setVisible(true);

		editProfileButton.setVisible(true);
		saveProfileButton.setVisible(false);
	}


	@Override
	public void initialize(final URL url, final ResourceBundle rb) {
		genderChoiceBox.getItems().addAll("Male", "Female");
		genderChoiceBox.setValue("Male");


		nameInput.setVisible(false);
		phoneInput.setVisible(false);
		genderChoiceBox.setVisible(false);
		saveProfileButton.setVisible(false);


	}


}
