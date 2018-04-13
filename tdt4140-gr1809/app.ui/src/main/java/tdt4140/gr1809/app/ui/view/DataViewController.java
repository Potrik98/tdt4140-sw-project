package tdt4140.gr1809.app.ui.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.util.StreamUtils;
import tdt4140.gr1809.app.ui.FxAppController;
import tdt4140.gr1809.app.ui.graph.DataGraph;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataViewController implements Initializable {
	@FXML Label timePeriodLabel;

	@FXML
	private Button clearButton;

	@FXML
	private AnchorPane graphAnchorPane;

	private DataGraph dataGraph;
	
	private FxAppController fxAppController;

	@Override
	public void initialize(final URL url, final ResourceBundle rb) {
		dataGraph = new DataGraph();
		Random random = new Random();
		List<DataPoint> dataPoints = StreamUtils.takeWhile(
				Stream.iterate(LocalDateTime.now(), time -> time.minus(1, ChronoUnit.MINUTES)),
						time -> time.isAfter(LocalDateTime.now().minus(2, ChronoUnit.HOURS)))
				.map(time -> DataPoint.builder()
						.time(time)
						.dataType(DataPoint.DataType.HEART_RATE)
						.value(40 + random.nextInt(100))
						.build())
				.collect(Collectors.toList());
		graphAnchorPane.getChildren().add(dataGraph.getGraph());
		dataGraph.clear();
		dataGraph.addDataPoints(dataPoints);

		dataGraph.plotDataType(DataPoint.DataType.HEART_RATE);
	}

	// Methods just to illustrate that we can plot different time intervals.
	@FXML
	private void plotHeartRateLastHour(final ActionEvent event) {
		timePeriodLabel.setText("Last Hour");
	}

	@FXML
	private void plotHeartRateLast24Hours(final ActionEvent event) {
		timePeriodLabel.setText("24 Hours");}

	@FXML
	private void plotHeartRateLastWeek(final ActionEvent event) {
		timePeriodLabel.setText("Last Week");}

	@FXML
	private void plotHeartRateLastMonth(final ActionEvent event) {
		timePeriodLabel.setText("Last Month");
	}

	@FXML
	private void plotHeartRateLastYear(final ActionEvent event) {
		timePeriodLabel.setText("Last Year");
	}

	@FXML
	private void handleClearButtonAction(final ActionEvent event) {
		dataGraph.clear();
	}

	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;

		//just to test that the user selection works:
		System.out.println("User Selected: " + fxAppController.user.getId() );
	}
}
