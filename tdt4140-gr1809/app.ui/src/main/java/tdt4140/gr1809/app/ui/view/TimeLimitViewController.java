package tdt4140.gr1809.app.ui.view;

import javafx.fxml.Initializable;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.TimeFilter;
import tdt4140.gr1809.app.client.TimeFilterClient;
import tdt4140.gr1809.app.ui.FxAppController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javafx.fxml.FXML;

import javax.swing.ComboBoxEditor;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;




public class TimeLimitViewController implements Initializable {
	private FxAppController fxAppController;	
	@FXML private DatePicker fromdate;
	@FXML private TextField fromtime;
	@FXML private DatePicker todate;
	@FXML private TextField totime;
	@FXML private CheckBox datatypetemperature;
	@FXML private CheckBox datatypesteps;
	@FXML private CheckBox datatypeheart;
	@FXML private Label errorLabel;
	@FXML private Label heartRateDisabled;
	@FXML private Label temperatureDisabled;
	@FXML private Label stepsDisabled;

	@FXML
	private void setTimeLimit() {
		try {
		LocalDateTime fromdatetime;
		LocalDate fromlocaldate = fromdate.getValue();
		fromdatetime = fromlocaldate.atTime(Integer.parseInt(fromtime.getText().split(":")[0]),Integer.parseInt(fromtime.getText().split(":")[1]));
		
		LocalDateTime todatetime;
		LocalDate tolocaldate = todate.getValue();
		todatetime = tolocaldate.atTime(Integer.parseInt(totime.getText().split(":")[0]),Integer.parseInt(totime.getText().split(":")[1]));
		
		if(datatypetemperature.isSelected()) {
			TimeFilter timefilter1 = TimeFilter.builder().startTime(fromdatetime).endTime(todatetime).userId(fxAppController.user.getId()).dataType(DataPoint.DataType.TEMPERATURE).build();
			TimeFilterClient client = new TimeFilterClient();
			client.createTimeFilter(timefilter1);
			errorLabel.setVisible(false);
			datatypetemperature.setSelected(false);
			temperatureDisabled.setVisible(true);
			temperatureDisabled.setText("Disabled from: " + fromlocaldate.toString()+ " "+ fromtime.getText() + " to: " + tolocaldate.toString() +" " + totime.getText() );

		}
		if(datatypesteps.isSelected()) {
			TimeFilter timefilter2 = TimeFilter.builder().startTime(fromdatetime).endTime(todatetime).userId(fxAppController.user.getId()).dataType(DataPoint.DataType.STEPS).build();
			TimeFilterClient client = new TimeFilterClient();
			client.createTimeFilter(timefilter2);
			errorLabel.setVisible(false);
			datatypesteps.setSelected(false);
			stepsDisabled.setVisible(true);
			stepsDisabled.setText("Disabled from: " + fromlocaldate.toString()+ " "+ fromtime.getText() + " to: " + tolocaldate.toString() +" " + totime.getText() );


		}
		if(datatypeheart.isSelected()) {
			TimeFilter timefilter3 = TimeFilter.builder().startTime(fromdatetime).endTime(todatetime).userId(fxAppController.user.getId()).dataType(DataPoint.DataType.HEART_RATE).build();
			TimeFilterClient client = new TimeFilterClient();
			client.createTimeFilter(timefilter3);
			errorLabel.setVisible(false);
			datatypeheart.setSelected(false);
			heartRateDisabled.setVisible(true);
			heartRateDisabled.setText("Disabled from: " + fromlocaldate.toString()+ " "+ fromtime.getText() + " to: " + tolocaldate.toString() +" " + totime.getText() );

		}
		totime.setText("");
		fromtime.setText("");

		}catch(Exception e) {
			System.out.println(e);
			errorLabel.setVisible(true);
		}
	}

	public void cancelButtonPressed(){
		fromdate.setValue(LocalDate.now());
		fromtime.setText("");
		todate.setValue(LocalDate.now().plusDays(7));
		totime.setText("");
		errorLabel.setVisible(false);
		datatypeheart.setSelected(false);
		datatypesteps.setSelected(false);
		datatypetemperature.setSelected(false);
	}

	@Override
	public void initialize(final URL url, final ResourceBundle rb) {
		fromdate.setValue(LocalDate.now());
		todate.setValue(LocalDate.now().plusDays(7));

	}
	
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		datatypeheart.setSelected(true);
	}
}
