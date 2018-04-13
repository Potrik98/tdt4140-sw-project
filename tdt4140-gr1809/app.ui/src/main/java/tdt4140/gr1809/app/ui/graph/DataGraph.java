package tdt4140.gr1809.app.ui.graph;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.value.LocalDateTimeNumberConverter;
import tdt4140.gr1809.app.core.value.Numerable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataGraph {
    private static final Numerable.NumerableBuilder<LocalDateTime> numerableBuilder =
            new Numerable.NumerableBuilder<>(new LocalDateTimeNumberConverter());

    private final LineChart<Numerable<LocalDateTime>, Number> graph;
    private final LocalDateTimeAxis localDateTimeAxis;

    private Map<DataPoint.DataType, List<DataPoint>> dataPointsByDataType;

    private DataPoint.DataType currentDataType;
    private LocalDateTime lowerBound;
    private LocalDateTime upperBound;

    public DataGraph() {
        localDateTimeAxis = new LocalDateTimeAxis();
        this.graph = new LineChart<>(localDateTimeAxis, new NumberAxis());
        graph.setVerticalGridLinesVisible(true);
        graph.setHorizontalGridLinesVisible(true);
        graph.setLegendVisible(false);
        graph.setCreateSymbols(false);

        upperBound = LocalDateTime.now();
        lowerBound = upperBound.minusMonths(1);
    }

    public void setData(final List<DataPoint> dataPoints) {
        dataPointsByDataType = dataPoints.stream()
                .collect(Collectors.groupingBy(DataPoint::getDataType));
    }

    public void setRange(final LocalDateTime lowerBound, final LocalDateTime upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        plotDataType(currentDataType);
    }

    public LineChart<Numerable<LocalDateTime>, Number> getGraph() {
        return graph;
    }

    public void plotDataType(final DataPoint.DataType dataType) {
        currentDataType = dataType;
        final ObservableList<XYChart.Data<Numerable<LocalDateTime>, Number>> chartData =
                dataPointsByDataType.get(dataType).stream()
                        .filter(dataPoint -> dataPoint.getTime().isAfter(lowerBound)
                                && dataPoint.getTime().isBefore(upperBound))
                        .map(dataPoint -> new XYChart.Data<Numerable<LocalDateTime>, Number>(
                                numerableBuilder.numerableOfValue(dataPoint.getTime()), dataPoint.getValue()
                        ))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
        graph.setData(FXCollections.observableArrayList(new XYChart.Series<>(chartData)));
    }

    public void clear() {
        graph.getData().clear();
        localDateTimeAxis.setAutoRanging(true);
    }
}
