package tdt4140.gr1809.app.ui;

import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.TimeFilter;
import tdt4140.gr1809.app.client.TimeFilterClient;
import tdt4140.gr1809.app.ui.FxAppController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.fxml.FXML;

import javax.swing.ComboBoxEditor;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;




public class TimeLimitViewController {
	private FxAppController fxAppController;	
	@FXML private DatePicker fromdate;
	@FXML private TextField fromtime;
	@FXML private DatePicker todate;
	@FXML private TextField totime;
	@FXML private CheckBox datatypetemperature;
	@FXML private CheckBox datatypesteps;
	@FXML private CheckBox datatypeheart;
	
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
		}
		if(datatypesteps.isSelected()) {
			TimeFilter timefilter2 = TimeFilter.builder().startTime(fromdatetime).endTime(todatetime).userId(fxAppController.user.getId()).dataType(DataPoint.DataType.STEPS).build();
			TimeFilterClient client = new TimeFilterClient();
			client.createTimeFilter(timefilter2);
		}
		if(datatypeheart.isSelected()) {
			TimeFilter timefilter3 = TimeFilter.builder().startTime(fromdatetime).endTime(todatetime).userId(fxAppController.user.getId()).dataType(DataPoint.DataType.HEART_RATE).build();
			TimeFilterClient client = new TimeFilterClient();
			client.createTimeFilter(timefilter3);
		}
		
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		datatypeheart.setSelected(true);
	}
}
