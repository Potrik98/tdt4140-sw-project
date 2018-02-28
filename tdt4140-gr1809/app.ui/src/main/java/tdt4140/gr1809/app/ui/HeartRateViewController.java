package tdt4140.gr1809.app.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

public class HeartRateViewController implements Initializable {
	@FXML Button backButton;

	@FXML
	private LineChart<Double, Double> lineGraph;

	@FXML
	private AreaChart<Double, Double> areaGraph;

	@FXML
	private Button lineGraphButton;

	@FXML
	private Button areaGraphButton;

	@FXML
	private Button xyButton;

	@FXML
	private Button xyButton2;

	@FXML
	private Button squaredButton;

	@FXML
	private Button squaredButton2;

	@FXML
	private Button cubedButton;

	@FXML
	private Button cubedButton2;

	@FXML
	private Button clearButton;

	private HeartRateGraph mathsGraph;
	private HeartRateGraph areaMathsGraph;

	@Override
	public void initialize(final URL url, final ResourceBundle rb) {
		mathsGraph = new HeartRateGraph(lineGraph, 10);
		areaMathsGraph = new HeartRateGraph(areaGraph, 10);
	}

	@FXML
	private void handleLineGraphButtonAction(final ActionEvent event) {
		lineGraph.setVisible(true);
		areaGraph.setVisible(false);
	}

	@FXML
	private void handleAreaGraphButtonAction(final ActionEvent event) {
		areaGraph.setVisible(true);
		lineGraph.setVisible(false);
	}

	@FXML
	private void handleXYButtonAction(final ActionEvent event) {
		plotHeartRate(150);
	}

	private void plotHeartRate(int avg) {
		if (lineGraph.isVisible()) {
			mathsGraph.plotHeartRateLine(avg);
		} else {
			areaMathsGraph.plotHeartRateLine(avg);
		}
	}

	private void plotLine(Function<Double, Double> function) {
		if (lineGraph.isVisible()) {
			mathsGraph.plotLine(function);
		} else {
			areaMathsGraph.plotLine(function);
		}
	}

	@FXML
	private void handleXYButton2Action(final ActionEvent event) {
		plotLine(x -> x - 3);
	}

	@FXML
	private void handleSquaredButtonAction(final ActionEvent event) {
		plotLine(x -> Math.pow(x, 2));
	}

	@FXML
	private void handleSquaredButton2Action(final ActionEvent event) {
		plotLine(x -> Math.pow(x, 2) + 2);
	}

	@FXML
	private void handleCubedButtonAction(final ActionEvent event) {
		plotLine(x -> Math.pow(x, 3));
	}

	@FXML
	private void handleCubedButton2Action(final ActionEvent event) {
		plotLine(x -> Math.pow(x - 3, 3) - 1);
	}

	@FXML
	private void handleClearButtonAction(final ActionEvent event) {
		clear();
	}

	private void clear() {
		if (lineGraph.isVisible()) {
			mathsGraph.clear();
		} else {
			areaMathsGraph.clear();
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


}
