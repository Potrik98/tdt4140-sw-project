package tdt4140.gr1809.app.ui.graph;

import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import tdt4140.gr1809.app.client.StatisticsClient;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Statistic;
import tdt4140.gr1809.app.core.util.TimeUtils;
import tdt4140.gr1809.app.core.value.LocalDateTimeNumberConverter;
import tdt4140.gr1809.app.core.value.Numerable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataGraph {
    private static final int MAX_DATA_POINTS = 30;

    private static final Numerable.NumerableBuilder<LocalDateTime> numerableBuilder =
            new Numerable.NumerableBuilder<>(new LocalDateTimeNumberConverter());

    private final LineChart<Numerable<LocalDateTime>, Number> graph;

    private Map<DataPoint.DataType, List<DataPoint>> dataPointsByDataType;

    private DataPoint.DataType currentDataType;
    private LocalDateTime lowerBound;
    private LocalDateTime upperBound;
    private Boolean showAggregate;
    
    public float max;
    public float min;
    public float avg;

    public DataGraph() {
        final NumberAxis numberAxis = new NumberAxis();
        numberAxis.setForceZeroInRange(false);
        this.graph = new LineChart<>(new LocalDateTimeAxis(), numberAxis);
        graph.setVerticalGridLinesVisible(true);
        graph.setHorizontalGridLinesVisible(true);
        graph.setLegendVisible(false);
        graph.setCreateSymbols(false);
        graph.setPrefWidth(880.0);

        upperBound = LocalDateTime.now();
        lowerBound = upperBound.minusMonths(1);
        showAggregate = true;
    }

    public void setData(final List<DataPoint> dataPoints) {
        dataPointsByDataType = dataPoints.stream()
                .collect(Collectors.groupingBy(DataPoint::getDataType));
    }
    
    public void calculateStats(DataPoint.DataType dataType, final List<DataPoint> dataPoints) {
    	currentDataType = dataType;
    	
    	min = 999999;
        max = -999999;
        avg = 0;
        int numPoints = 0;
        float totalVal = 0;
        System.out.println(currentDataType);
        System.out.println(currentDataType + " dffdfd");
        for(int i = 0; i < dataPoints.size(); i++) {
        	if(dataPoints.get(i).getDataType() == currentDataType
        			&& dataPoints.get(i).getTime().isAfter(lowerBound)
        			&& dataPoints.get(i).getTime().isBefore(upperBound)) {
        		numPoints++;
        		totalVal += dataPoints.get(i).getValue();
        		if(dataPoints.get(i).getValue() < min) {
        			min = dataPoints.get(i).getValue();
        		} else if(dataPoints.get(i).getValue() > max) {
        			max = dataPoints.get(i).getValue();
        		}
        	}
        }
        avg = totalVal/numPoints;
    }

    public void setRange(final LocalDateTime lowerBound, final LocalDateTime upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        plotDataType(currentDataType);
    }
    
    public void setShowAggregate(Boolean show) {
    	this.showAggregate = show;
    	plotDataType(currentDataType);
    }

    public LineChart<Numerable<LocalDateTime>, Number> getGraph() {
        return graph;
    }

    /**
     * Plots data points of the data type within the graphs range.
     * Will aggregate some data points if there are too many
     * @param dataType Data type to plot
     */
    public void plotDataType(final DataPoint.DataType dataType) {
        currentDataType = dataType;
        final List<DataPoint> dataPointsInRange = dataPointsByDataType.get(dataType).stream()
                .filter(dataPoint -> dataPoint.getTime().isAfter(lowerBound)
                        && dataPoint.getTime().isBefore(upperBound))
                .collect(Collectors.toList());
        final int aggregation = (int) Math.ceil((double) dataPointsInRange.size() / MAX_DATA_POINTS);
        final List<DataPoint> aggregatedDataPoints =
                Streams.stream(Iterables.partition(dataPointsInRange, aggregation))
                        .map(dataPoints -> DataPoint.builder()
                                .value(dataPoints.stream().mapToInt(DataPoint::getValue).sum()
                                        / dataPoints.size())
                                .time(TimeUtils.averageLocalDateTime(
                                        dataPoints.stream().map(DataPoint::getTime).collect(Collectors.toList())))
                                .build())
                        .collect(Collectors.toList());
        final ObservableList<XYChart.Data<Numerable<LocalDateTime>, Number>> chartData =
                aggregatedDataPoints.stream()
                        .map(dataPoint -> new XYChart.Data<Numerable<LocalDateTime>, Number>(
                                numerableBuilder.numerableOfValue(dataPoint.getTime()),
                                dataType == DataPoint.DataType.TEMPERATURE
                                        ? dataPoint.getValue() / 100.0 : dataPoint.getValue()
                        ))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
        graph.setData(FXCollections.observableArrayList(new XYChart.Series<>(chartData)));
        
        if(showAggregate) {
        	plotAggregateData(dataType);
        }
    }
    
    private void plotAggregateData(final DataPoint.DataType dataType) {
    	StatisticsClient statisticsClient = new StatisticsClient();
    	Statistic stat = statisticsClient.getStatisticsForDataType(dataType);
    	Series<Numerable<LocalDateTime>, Number> series = new Series<Numerable<LocalDateTime>, Number>(); 
    	series.getData().add(new XYChart.Data<Numerable<LocalDateTime>, Number>(numerableBuilder.numerableOfValue(lowerBound), stat.getValue()));
    	series.getData().add(new XYChart.Data<Numerable<LocalDateTime>, Number>(numerableBuilder.numerableOfValue(upperBound), stat.getValue()));
    	graph.getData().add(series);
    }

    public void clear() {
        graph.getData().clear();
    }
}
