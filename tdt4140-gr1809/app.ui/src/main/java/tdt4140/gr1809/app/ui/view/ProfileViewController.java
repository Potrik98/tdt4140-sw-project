package tdt4140.gr1809.app.ui.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.ui.FxAppController;
import tdt4140.gr1809.app.ui.io.FileUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileViewController implements Initializable {

	@FXML Label nameLabel;
	@FXML Label birthdateLabel;
	@FXML Label genderLabel;
	@FXML TextField maxPulse;
	@FXML CheckBox aggregateCheckbox;

	private FxAppController fxAppController;

	private UserClient userClient = new UserClient();

	@FXML
	public void deleteUser() throws IOException {
		final UserClient client = new UserClient();
		client.deleteUser(fxAppController.user.getId());
		fxAppController.logout();
	}
	
	@FXML
	public void updateUser() {
		try {
			final Integer maxPulse = Integer.valueOf(this.maxPulse.getText());
			fxAppController.user = User.from(fxAppController.user)
					.maxPulse(maxPulse)
					.build();
			userClient.updateUser(fxAppController.user);
		} catch (final NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Update failed");
			alert.setHeaderText("Failed to update user");
			alert.setContentText("Please input a valid value for max pulse");
			alert.showAndWait();
		} catch (final Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Update failed");
			alert.setHeaderText("Failed to update user");
			alert.setContentText("Something went wrong: " + e.getMessage());
			alert.showAndWait();
		}



	}
	
	@FXML
	public void exportData() {
		final Optional<User> user = userClient.getAllUserDataById(fxAppController.user.getId());
		if (user.isPresent()) {
			final User userWithData = user.get();
			final String fileName = userWithData.getId().toString().concat(".json");
			FileUtils.writeObjectToFile(userWithData, fileName);
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Export failed");
			alert.setHeaderText("Failed to export user");
			alert.setContentText("Something went wrong");
			alert.showAndWait();
		}
	}
	
	@FXML
	private void changeAggregateParticipation() {
		System.out.println(aggregateCheckbox.isSelected());
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
		

		if (user != null) {
			nameLabel.setText(user.getFirstName() + " " + user.getLastName());
			genderLabel.setText(user.getGender());
			birthdateLabel.setText(fxAppController.user.getBirthDate().toLocalDate().toString());
            aggregateCheckbox.setSelected(user.isParticipatingInAggregatedStatistics());
            if (user.getMaxPulse().isPresent()) maxPulse.setText(user.getMaxPulse().get().toString());
		} else {
			nameLabel.setText(sp.getFirstName() + " " + sp.getLastName());
			genderLabel.setText(sp.getGender());
			birthdateLabel.setText(sp.getBirthDate().toLocalDate().toString());
		}
	}

}
