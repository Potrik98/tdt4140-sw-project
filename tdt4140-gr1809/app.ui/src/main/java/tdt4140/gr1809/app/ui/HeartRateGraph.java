package tdt4140.gr1809.app.ui;

import javafx.scene.chart.XYChart;

import java.util.function.Function;

public class HeartRateGraph {

    private XYChart<Double, Double> graph;
    private double range;

    public HeartRateGraph(final XYChart<Double, Double> graph, final double range) {
        this.graph = graph;
        this.range = range;
    }

    public void plotHeartRateLine(int avg, double range) {
        final XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
        for (double x = 0; x <= range; x = x + 1) {
            //just som random numbers around an average heart rate
            double variation = (Math.random() *10) + avg - (Math.random()*12);
            plotPoint(x, variation, series);

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