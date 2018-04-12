package tdt4140.gr1809.app.ui.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import tdt4140.gr1809.app.client.DataClient;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.util.StreamUtils;
import tdt4140.gr1809.app.ui.FxAppController;
import tdt4140.gr1809.app.ui.graph.DataGraph;
import tdt4140.gr1809.app.ui.graph.HeartRateGraph;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HeartRateViewController implements Initializable {
	@FXML Label timePeriodLabel;

	//@FXML
	//private LineChart<Double, Double> lineGraph;

	@FXML
	private Button clearButton;

	@FXML
	private AnchorPane graphAnchorPane;

	private HeartRateGraph heartRateGraph;
	private DataGraph dataGraph;
	
	private FxAppController fxAppController;


	@Override
	public void initialize(final URL url, final ResourceBundle rb) {
		//heartRateGraph = new HeartRateGraph(lineGraph, 10);
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
		dataGraph.addDataPoints(dataPoints);
		graphAnchorPane.getChildren().add(dataGraph.getGraph());
	}


	// Methods just to illustrate that we can plot different time intervals.
	@FXML
	private void plotHeartRateLastHour(final ActionEvent event) {
		plotHeartRate(1);
		timePeriodLabel.setText("Last Hour");
	}

	@FXML
	private void plotHeartRateLast24Hours(final ActionEvent event) {
		plotHeartRate(24);
		timePeriodLabel.setText("24 Hours");}

	@FXML
	private void plotHeartRateLastWeek(final ActionEvent event) {
		plotHeartRate(24*7);
		timePeriodLabel.setText("Last Week");}

	@FXML
	private void plotHeartRateLastMonth(final ActionEvent event) {
		plotHeartRate(31);
		timePeriodLabel.setText("Last Month");
	}

	@FXML
	private void plotHeartRateLastYear(final ActionEvent event) {
		plotHeartRate(24*365);
		timePeriodLabel.setText("Last Year");
	}

	private void plotHeartRate(int timePeriod) {
		//clear the graph
		heartRateGraph.clear();
		//create a dataclient to get the datapoints
		DataClient dataClient = new DataClient();
		List<DataPoint> points = dataClient.getDataPointsForUserId(fxAppController.user.getId());
		
		//if there are no datapoints create 1h of fake data
		/*
		if(points.size() == 0) {
			for(int i = 0; i < 60; i++) {
				int value = (int)((Math.sin(360*(i*3/60))*50.0) + 100);
				DataPoint newPoint = DataPoint.builder()
						.userId(fxAppController.user.getId())
						.time(LocalDateTime.now().minusMinutes(i))
						.value(value)
						.dataType(DataPoint.DataType.HEART_RATE)
						.build();
				dataClient.createDataPoint(newPoint);
			}
		}*/

		//plot the dataPoints
		heartRateGraph.plotHeartRateLine(points, timePeriod);
	}

	@FXML
	private void handleClearButtonAction(final ActionEvent event) {
		clear();
	}

	private void clear() {
		//if (lineGraph.isVisible()) {
		//	heartRateGraph.clear();
		//}


	}
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;

		//just to test that the user selection works:
		System.out.println("User Selected: " + fxAppController.user.getId() );
	}
}
