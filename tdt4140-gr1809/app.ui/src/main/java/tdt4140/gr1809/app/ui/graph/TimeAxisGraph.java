package tdt4140.gr1809.app.ui.graph;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import tdt4140.gr1809.app.core.model.DataPoint;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


// Just a reference implementation of the the Graph..
public class TimeAxisGraph {
    private XYChart<LocalDateTime, Number> graph;
    private Map<DataPoint.DataType, XYChart.Series<LocalDateTime, Number>> seriesMap;

    public TimeAxisGraph() {
        this.graph = new LineChart<>(new TimeAxis(), new NumberAxis());
        seriesMap = Arrays.stream(DataPoint.DataType.values())
                .collect(Collectors.toMap(
                        Function.identity(),
                        a -> new XYChart.Series<LocalDateTime, Number>()));
    }

    public XYChart<LocalDateTime, Number> getGraph() {
        return graph;
    }

    public void addDataPoints(List<DataPoint> dataPoints) {
        dataPoints.forEach(dataPoint -> seriesMap.get(dataPoint.getDataType()).getData()
                        .add(new XYChart.Data<>(dataPoint.getTime(), dataPoint.getValue())));
    }

    public void clear() {
        graph.getData().clear();
    }
}