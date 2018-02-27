package tdt4140.gr1809.app.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;


public class GraphViewController implements Initializable {
	@FXML private Button backButton;
	@FXML private LineChart heartRateChart;


	//method to go back to main view
	public void goBackToFxApp(ActionEvent event) throws IOException {

		//first get the parent view
		Parent parentView = FXMLLoader.load(getClass().getResource("FxApp.fxml"));
		Scene parentViewScene = new Scene(parentView);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(parentViewScene);
		window.show();

	}

	public class MyGraph {
		private XYChart<Double, Double> graph;
		private double range;
		public MyGraph(final XYChart<Double, Double> graph, final double range) {
			this.graph = graph;
			this.range = range;
		}
		public void plotLine(final Function<Double, Double> function) {
			final XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
			for (double x = -range; x <= range; x = x + 0.01) {
				plotPoint(x, function.apply(x), series);
			}
			graph.getData().add(series);
		}
		private void plotPoint(final double x, final double y,
							   final XYChart.Series<Double, Double> series) {
			series.getData().add(new XYChart.Data<Double, Double>(x, y));
		}
		public void clear() {
			graph.getData().clear();
		}
	}





	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
}
