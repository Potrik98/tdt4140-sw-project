package tdt4140.gr1809.app.ui.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import tdt4140.gr1809.app.client.DataClient;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.ui.FxAppController;
import tdt4140.gr1809.app.ui.graph.DataGraph;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ResourceBundle;

public class DataViewController implements Initializable {
	@FXML Label timePeriodLabel;

	@FXML
	private Button clearButton;

	@FXML
	private AnchorPane graphAnchorPane;
	
	//@FXML
	//private CheckBox aggregateCheckbox;

	private DataGraph dataGraph;
	
	private FxAppController fxAppController;

	private DataClient dataClient;

	@FXML
	private ComboBox<DataPoint.DataType> dataDropdown;

	@Override
	public void initialize(final URL url, final ResourceBundle rb) {
		dataClient = new DataClient();
		dataGraph = new DataGraph();
		graphAnchorPane.getChildren().add(dataGraph.getGraph());
		dataGraph.clear();
		dataDropdown.setItems(FXCollections.observableList(Arrays.asList(DataPoint.DataType.values())));
		dataDropdown.setValue(DataPoint.DataType.HEART_RATE);
	}
	
	
	//@FXML
	//private void showOrHideAggregate() {
		//dataGraph.setShowAggregate(false);
	//}

	// Methods just to illustrate that we can plot different time intervals.
	@FXML
	private void plotDataLastHour(final ActionEvent event) {
		timePeriodLabel.setText("Last Hour");
		final LocalDateTime now = LocalDateTime.now();
		dataGraph.setRange(now.minusHours(1), now);
	}

	@FXML
	private void plotDataLast24Hours(final ActionEvent event) {
		timePeriodLabel.setText("24 Hours");
		final LocalDateTime now = LocalDateTime.now();
		dataGraph.setRange(now.minusHours(24), now);
	}

	@FXML
	private void plotDataLastWeek(final ActionEvent event) {
		timePeriodLabel.setText("Last Week");
		final LocalDateTime now = LocalDateTime.now();
		dataGraph.setRange(now.minusWeeks(1), now);
	}

	@FXML
	private void plotDataLastMonth(final ActionEvent event) {
		timePeriodLabel.setText("Last Month");
		final LocalDateTime now = LocalDateTime.now();
		dataGraph.setRange(now.minusMonths(1), now);
	}

	@FXML
	private void plotDataLastYear(final ActionEvent event) {
		timePeriodLabel.setText("Last Year");
		final LocalDateTime now = LocalDateTime.now();
		dataGraph.setRange(now.minusYears(1), now);
	}

	@FXML
	public void plotDataLastQuarter(final ActionEvent actionEvent) {
		timePeriodLabel.setText("Last 15 Minutes");
		final LocalDateTime now = LocalDateTime.now();
		dataGraph.setRange(now.minusMinutes(15), now);
	}

	@FXML
	public void selectDataType(final ActionEvent actionEvent) {
		dataGraph.plotDataType(dataDropdown.getValue());
	}

	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		dataGraph.setData(dataClient.getDataPointsForUserId(fxAppController.user.getId()));
		dataGraph.plotDataType(DataPoint.DataType.TEMPERATURE);
		//just to test that the user selection works:
		System.out.println("User Selected: " + fxAppController.user.getId() );
	}
}
