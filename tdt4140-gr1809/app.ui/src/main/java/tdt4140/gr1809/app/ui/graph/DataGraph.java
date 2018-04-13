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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataGraph {
    private static final Numerable.NumerableBuilder<LocalDateTime> numerableBuilder =
            new Numerable.NumerableBuilder<>(new LocalDateTimeNumberConverter());

    private final XYChart<Numerable<LocalDateTime>, Number> graph;
    private final Map<DataPoint.DataType, XYChart.Series<Numerable<LocalDateTime>, Number>> seriesMap;

    public DataGraph() {
        this.graph = new LineChart<>(new LocalDateTimeAxis(), new NumberAxis());
        seriesMap = Arrays.stream(DataPoint.DataType.values())
                .collect(Collectors.toMap(
                        Function.identity(),
                        a -> new XYChart.Series<Numerable<LocalDateTime>, Number>()));
    }

    public XYChart<Numerable<LocalDateTime>, Number> getGraph() {
        return graph;
    }

    public void plotDataType(final DataPoint.DataType dataType) {
        graph.setData(FXCollections.observableArrayList(seriesMap.get(dataType)));
    }

    public void addDataPoints(final List<DataPoint> dataPoints) {
        dataPoints.forEach(dataPoint -> seriesMap.get(dataPoint.getDataType()).getData()
                .add(new XYChart.Data<Numerable<LocalDateTime>, Number>(numerableBuilder.numerableOfValue(dataPoint.getTime()), dataPoint.getValue())));
    }

    public void clear() {
        graph.getData().clear();
    }
}
