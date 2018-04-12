package tdt4140.gr1809.app.ui.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import tdt4140.gr1809.app.client.CustomNotificationThresholdClient;
import tdt4140.gr1809.app.core.model.CustomNotificationThreshold;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.ui.FxAppController;

import java.net.URL;
import java.util.*;

public class ThresholdsViewController implements Initializable {

	private FxAppController fxAppController;

	private ServiceProvider serviceProvider;
	private User user;

    @FXML Label usernameLabel;
    @FXML TableView thresholdsTable;
    @FXML TableColumn<CustomNotificationThreshold, DataPoint.DataType> col_dataType;
    @FXML TableColumn<CustomNotificationThreshold, CustomNotificationThreshold.ThresholdType> col_threshold;
    @FXML TableColumn<CustomNotificationThreshold, Integer> col_value;
    @FXML TableColumn<CustomNotificationThreshold, String> col_message;


	@FXML TextArea messageInput;
	@FXML TextField valueInput;

	@FXML ComboBox<DataPoint.DataType> dataTypeComboBox;
	@FXML ComboBox<CustomNotificationThreshold.ThresholdType> thresholdTypeComboBox;


    @FXML VBox selectedThresholdBox;
    @FXML Label dataTypeLabel;
    @FXML Label thresholdTypeLabel;
    @FXML Label valueLabel;
    @FXML Label messageLabel;

    @FXML Label valueErrorLabel;

    private CustomNotificationThreshold selectedThreshold;





    CustomNotificationThresholdClient thresholdClient = new CustomNotificationThresholdClient();


	ObservableList<DataPoint.DataType> dataTypeObservableList = FXCollections.observableArrayList();
	ObservableList<CustomNotificationThreshold.ThresholdType> thresholdTypeObservableList = FXCollections.observableArrayList();

    ObservableList<CustomNotificationThreshold> customThresholdsObservableList = FXCollections.observableArrayList();

    public void loadCustomThresholds(){
        customThresholdsObservableList.clear();
        col_dataType.setCellValueFactory(new PropertyValueFactory<>("dataType"));
        col_threshold.setCellValueFactory(new PropertyValueFactory<>("thresholdType"));
        col_value.setCellValueFactory(new PropertyValueFactory<>("value"));
        col_message.setCellValueFactory(new PropertyValueFactory<>("message"));

        col_dataType.setOnEditStart(event -> {
            CustomNotificationThreshold threshold = event.getRowValue();
            displaySelectedThreshold(threshold);});
        col_threshold.setOnEditStart(event -> {
            CustomNotificationThreshold threshold = event.getRowValue();
            displaySelectedThreshold(threshold);});
        col_value.setOnEditStart(event -> {
            CustomNotificationThreshold threshold = event.getRowValue();
            displaySelectedThreshold(threshold);});
        col_message.setOnEditStart(event -> {
            CustomNotificationThreshold threshold = event.getRowValue();
            displaySelectedThreshold(threshold);});
        try{
            customThresholdsObservableList.addAll(thresholdClient.getCustomNotificationThresholdsForUserId(UUID.fromString("4e9ce214-fbf6-4eb4-bdac-be2e9a0609ec")));
            thresholdsTable.setItems(customThresholdsObservableList);
            usernameLabel.setText("Anderson, John"); //set it to user.getName()
        }
        catch (Exception e){
            System.err.println(e);
        }

    }
    public void displaySelectedThreshold(CustomNotificationThreshold customNotificationThreshold){
        dataTypeLabel.setText(customNotificationThreshold.getDataType().toString());
        thresholdTypeLabel.setText(customNotificationThreshold.getThresholdType().toString());
        valueLabel.setText(customNotificationThreshold.getValue().toString());
        messageLabel.setText(customNotificationThreshold.getMessage());
        selectedThresholdBox.setVisible(true);
        selectedThreshold = customNotificationThreshold;
    }

    public void deleteSelectedThreshold(){
        thresholdClient.deleteCustomNotificationThreshold(selectedThreshold.getId());
        selectedThresholdBox.setVisible(false);
        loadCustomThresholds();
    }

	public void createCustomNotificationThreshold(){
        if (validateValueInput()){
            final CustomNotificationThreshold customNotificationThreshold = CustomNotificationThreshold.builder()
                    .userId(UUID.fromString("4e9ce214-fbf6-4eb4-bdac-be2e9a0609ec")) //replace with user.getId()
                    .dataType(dataTypeComboBox.getValue())
                    .value(Integer.parseInt(valueInput.getText()))
                    .message(messageInput.getText())
                    .thresholdType(thresholdTypeComboBox.getValue())
                    .build();

            thresholdClient.createCustomNotificationThreshold(customNotificationThreshold);
            System.out.println("Created Custom Notification for: " + dataTypeComboBox.getValue().toString() + " " + thresholdTypeComboBox.getValue().toString() +" " + customNotificationThreshold.getValue().toString());
            displaySelectedThreshold(customNotificationThreshold);
            loadCustomThresholds();
            clearInput();
        }


	}

	public void clearInput(){
        messageInput.clear();
        valueInput.clear();
        valueErrorLabel.setVisible(false);
    }

    public boolean validateValueInput(){
        try{
            Integer.parseInt(valueInput.getText());
            return true;
        }
        catch (Exception e){
            valueErrorLabel.setVisible(true);
            return false;
        }
    }


	public void initialize(final URL url, final ResourceBundle rb){

		List<DataPoint.DataType> datatypeDropdownItems = Arrays.asList(DataPoint.DataType.values());
		dataTypeObservableList.addAll(datatypeDropdownItems);

		List<CustomNotificationThreshold.ThresholdType> thresholdDropdownItems = Arrays.asList(CustomNotificationThreshold.ThresholdType.values());
		thresholdTypeObservableList.addAll(thresholdDropdownItems);

		dataTypeComboBox.setItems(dataTypeObservableList);
		thresholdTypeComboBox.setItems(thresholdTypeObservableList);

		//default set value to the first in list, thus ensuring that something is always is selected
		dataTypeComboBox.setValue(dataTypeObservableList.get(0));
		thresholdTypeComboBox.setValue(thresholdTypeObservableList.get(0));

        selectedThresholdBox.setVisible(false);

        loadCustomThresholds();


	}
	
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		System.out.println(fxAppController);
		serviceProvider = controller.serviceProvider;
		user = controller.user;
	}

}
