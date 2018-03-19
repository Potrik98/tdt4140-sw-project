package tdt4140.gr1809.app.ui;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import tdt4140.gr1809.app.core.model.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class HeartRateGraph {

    private XYChart<Double, Double> graph;
    private double range;

    public HeartRateGraph(final LineChart<Double, Double> lineGraph, final double range) {
        this.graph = lineGraph;
        this.range = range;
    }
    
    public void plotHeartRateLine(List<DataPoint> dataPoints, int timePeriod) {
    	//sort the dataPoint by their time
    	Collections.sort(dataPoints, new Comparator<DataPoint>() {
            public int compare(DataPoint p1, DataPoint p2) {
                  return p1.getTime().compareTo(p2.getTime());
             }
    	});
    	//create a data series
    	final XYChart.Series<Double, Double> series = new XYChart.Series<>();
    	
    	//add the datapoints to the series
    	//the time right now minus the timePeriod is the range of dates to show
    	LocalDateTime range = LocalDateTime.now().minusHours(timePeriod);
    	for(int i = 0; i < dataPoints.size(); i++) {
    		//if the dataPoint is within the timePeriod we add it to the graph
    		if(dataPoints.get(i).getTime().isAfter(range)) {
    			//time the datapoint was recorded
    			LocalDateTime dataPointTime = dataPoints.get(i).getTime();
    			//calculate how many minutes ago it was recorded
    			int minutes = (int) dataPointTime.until(LocalDateTime.now(), ChronoUnit.MINUTES);
    			minutes += (int) dataPointTime.until(LocalDateTime.now(), ChronoUnit.HOURS)*60;
    			minutes += (int) dataPointTime.until(LocalDateTime.now(), ChronoUnit.DAYS)*24*60;
        		plotPoint(-minutes, dataPoints.get(i).getValue(), series);
    		}
    	}
    	//plot the data
    	graph.getData().add(series);
    }

    private void plotPoint(final double xPos, final double y,
                           final XYChart.Series<Double, Double> series) {
        series.getData().add(new XYChart.Data<Double, Double>(xPos, y));
    }

    public void clear() {
        graph.getData().clear();
    }
}