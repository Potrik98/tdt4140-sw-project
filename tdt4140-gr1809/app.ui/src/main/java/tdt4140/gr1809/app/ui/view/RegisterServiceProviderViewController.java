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
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RegisterServiceProviderViewController implements Initializable {

	@FXML Label firstNameLabel;
	@FXML Label lastNameLabel;
	@FXML Label birthdateLabel;
	@FXML Label genderLabel;

	@FXML Label firstNameError;
	@FXML Label lastNameError;

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

		//note that the  & operator is bitwise forcing the evaluation of both clauses!
		if(validateFirstNameInput() & validateLastNameInput()){

			final ServiceProviderClient client = new ServiceProviderClient();

			final ServiceProvider serviceProvider = ServiceProvider.builder()
					.firstName(firstNameInput.getText())
					.lastName(lastNameInput.getText())
					.gender(genderChoiceBox.getValue().toString())
					.birthDate(birthdateInput.getValue().atStartOfDay())
					.build();

			try{
				client.createServiceProvider(serviceProvider);
				System.out.println("Created Service Provider: " + serviceProvider.getId());
				fxAppController.serviceProvider = serviceProvider;
				fxAppController.goToServiceproviderLoginView(null);
			}
			catch (Exception e){
				System.err.println(e);
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Register Failed");
				alert.setHeaderText("Register Failed");
				alert.setContentText("failed to create Service Provider");
				alert.showAndWait();
			}

			clearInput();
		}
	}

	public boolean validateFirstNameInput(){
		if (firstNameInput.getText().isEmpty()){
			firstNameError.setVisible(true);
			return false;
		}
		firstNameError.setVisible(false);
		return true;

	}

	public boolean validateLastNameInput(){
		if (lastNameInput.getText().isEmpty()){
			lastNameError.setVisible(true);
			return false;
		}
		lastNameError.setVisible(false);
		return true;
	}

	public void cancelButtonClicked(ActionEvent event) throws IOException {
		fxAppController.goToServiceproviderLoginView(event);
	}





	@Override
	public void initialize(final URL url, final ResourceBundle rb) {
		genderChoiceBox.getItems().addAll("Male", "Female");
		genderChoiceBox.setValue("Male");
		birthdateInput.setValue(LocalDate.now().minusYears(22));

	}

	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
	}
}
