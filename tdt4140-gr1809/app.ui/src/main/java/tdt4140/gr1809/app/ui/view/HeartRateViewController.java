package tdt4140.gr1809.app.ui.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import tdt4140.gr1809.app.core.model.*;
import tdt4140.gr1809.app.client.*;
import tdt4140.gr1809.app.ui.FxAppController;
import tdt4140.gr1809.app.ui.graph.HeartRateGraph;

public class HeartRateViewController implements Initializable {
	@FXML Label timePeriodLabel;

	@FXML
	private LineChart<Double, Double> lineGraph;


	@FXML
	private Button clearButton;

	private HeartRateGraph heartRateGraph;
	
	private FxAppController fxAppController;


	@Override
	public void initialize(final URL url, final ResourceBundle rb) {
		heartRateGraph = new HeartRateGraph(lineGraph, 10);




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
		if (lineGraph.isVisible()) {
			heartRateGraph.clear();
		}


	}
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;

		//just to test that the user selection works:
		System.out.println("User Selected: " + fxAppController.user.getId() );
	}
}
