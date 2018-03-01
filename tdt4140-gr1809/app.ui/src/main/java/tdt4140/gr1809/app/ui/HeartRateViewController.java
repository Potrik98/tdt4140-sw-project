package tdt4140.gr1809.app.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HeartRateViewController implements Initializable {
	@FXML Button backButton;
	@FXML Label timePeriodLabel;

	@FXML
	private LineChart<Double, Double> lineGraph;


	@FXML
	private Button clearButton;

	private HeartRateGraph heartRateGraph;


	@Override
	public void initialize(final URL url, final ResourceBundle rb) {
		heartRateGraph = new HeartRateGraph(lineGraph, 10);

	}


	// Methods just to illustrate that we can plot different time intervals.
	// The range can possibly be the length of the input data array?
	@FXML
	private void plotHeartRateLastHour(final ActionEvent event) {
		plotHeartRate(150, 60);
		timePeriodLabel.setText("Last Hour");
	}

	@FXML
	private void plotHeartRateLast24Hours(final ActionEvent event) {
		plotHeartRate(150, 24);
		timePeriodLabel.setText("24 Hours");}

	@FXML
	private void plotHeartRateLastWeek(final ActionEvent event) {
		plotHeartRate(150, 24*7);
		timePeriodLabel.setText("Last Week");}

	@FXML
	private void plotHeartRateLastMonth(final ActionEvent event) {
		plotHeartRate(150, 31);
		timePeriodLabel.setText("Last Month");
	}

	@FXML
	private void plotHeartRateLastYear(final ActionEvent event) {
		plotHeartRate(150, 365);
		timePeriodLabel.setText("Last Year");
	}



	private void plotHeartRate(int avg, double range) {
		heartRateGraph.clear();
		heartRateGraph.plotHeartRateLine(avg, range);
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



	//method to go back to main view
	public void goBackToFxApp(ActionEvent event) throws IOException {

		//first get the parent view
		Parent parentView = FXMLLoader.load(getClass().getResource("FxApp.fxml"));
		Scene parentViewScene = new Scene(parentView);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(parentViewScene);
		window.show();

	}
	
	public void goToLoginView(ActionEvent event) throws IOException{
		//first get graphView
				Parent graphView = FXMLLoader.load(getClass().getResource("FxApp.fxml"));
				Scene graphViewScene = new Scene(graphView);

				Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
				window.setScene(graphViewScene);
				window.show();
	}

	public void goToGraphView(ActionEvent event) throws IOException {

		//first get graphView
		Parent graphView = FXMLLoader.load(getClass().getResource("GraphView.fxml"));
		Scene graphViewScene = new Scene(graphView);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(graphViewScene);
		window.show();
	}

	public void goToHeartRateView(ActionEvent event) throws IOException {

		//first get graphView
		Parent graphView = FXMLLoader.load(getClass().getResource("HeartRateView.fxml"));
		Scene graphViewScene = new Scene(graphView);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(graphViewScene);
		window.show();
	}


}
