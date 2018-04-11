package tdt4140.gr1809.app.ui.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import tdt4140.gr1809.app.client.CustomNotificationThresholdClient;
import tdt4140.gr1809.app.core.model.CustomNotificationThreshold;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.ui.FxAppController;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ThresholdsViewController implements Initializable {

	private FxAppController fxAppController;

	private ServiceProvider serviceProvider;
	private User user;

	@FXML TextArea messageInput;
	@FXML TextField valueInput;

	@FXML ComboBox<DataPoint.DataType> dataTypeComboBox;
	@FXML ComboBox<CustomNotificationThreshold.ThresholdType> thresholdTypeComboBox;


	CustomNotificationThresholdClient thresholdClient = new CustomNotificationThresholdClient();


	ObservableList<DataPoint.DataType> dataTypeObservableList = FXCollections.observableArrayList();
	ObservableList<CustomNotificationThreshold.ThresholdType> customNotificationThresholdObservableList = FXCollections.observableArrayList();


	public void createCustomNotificationThreshold(){
		final CustomNotificationThreshold customNotificationThreshold = CustomNotificationThreshold.builder()
				.userId(UUID.fromString("4e9ce214-fbf6-4eb4-bdac-be2e9a0609ec")) //replaces with user.getId()
				.dataType(dataTypeComboBox.getValue())
				.value(Integer.parseInt(valueInput.getText()))
				.message(messageInput.getText())
				.thresholdType(thresholdTypeComboBox.getValue())
				.build();

		thresholdClient.createCustomNotificationThreshold(customNotificationThreshold);
		System.out.println("Created Custom Notification for: " + dataTypeComboBox.getValue().toString() + " " + thresholdTypeComboBox.getValue().toString() +" " + customNotificationThreshold.getValue().toString());


	}

	public void getNotification(){
		List<CustomNotificationThreshold> hei = thresholdClient.getCustomNotificationThresholdsForUserId(UUID.fromString("4e9ce214-fbf6-4eb4-bdac-be2e9a0609ec"));
		System.out.println(hei.get(hei.size()-1).getValue());
	}


	public void initialize(final URL url, final ResourceBundle rb){

		List<DataPoint.DataType> datatypeDropdownItems = Arrays.asList(DataPoint.DataType.values());
		dataTypeObservableList.addAll(datatypeDropdownItems);

		List<CustomNotificationThreshold.ThresholdType> thresholdDropdownItems = Arrays.asList(CustomNotificationThreshold.ThresholdType.values());
		customNotificationThresholdObservableList.addAll(thresholdDropdownItems);

		dataTypeComboBox.setItems(dataTypeObservableList);
		thresholdTypeComboBox.setItems(customNotificationThresholdObservableList);

		//default set value to the first in list, thus ensuring that something is always is selected
		dataTypeComboBox.setValue(dataTypeObservableList.get(0));
		thresholdTypeComboBox.setValue(customNotificationThresholdObservableList.get(0));


	}
	
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		System.out.println(fxAppController);
		serviceProvider = controller.serviceProvider;
		user = controller.user;
	}

}
