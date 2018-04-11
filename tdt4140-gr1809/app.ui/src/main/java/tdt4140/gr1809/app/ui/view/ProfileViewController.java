package tdt4140.gr1809.app.ui.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
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
	@FXML TextField maxPulse;
	@FXML CheckBox aggregateCheckbox;
	
	private FxAppController fxAppController;

	//TODO: validate user input

	@FXML
	public void deleteUser() throws IOException {
		final UserClient client = new UserClient();
		client.deleteUser(fxAppController.user.getId());
		fxAppController.goToLoginView(null);
	}
	
	@FXML
	public void updateUser() throws IOException {
	}
	
	@FXML
	public void exportData() throws IOException {
	}
	
	@FXML
	private void changeAggregateParticipation() {
		System.out.println(aggregateCheckbox.isSelected());
		UserClient userClient = new UserClient();
		User user = User.from(fxAppController.user)
				.participatingInAggregatedStatistics(aggregateCheckbox.isSelected())
				.build();
		userClient.updateUser(user);
		fxAppController.user = user;
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
            aggregateCheckbox.setSelected(user.isParticipatingInAggregatedStatistics());
		}else {
			nameLabel.setText(sp.getFirstName() + " " + sp.getLastName());
			genderLabel.setText(sp.getGender());
			birthdateLabel.setText(sp.getBirthDate().toLocalDate().toString());
		}
	}

}
