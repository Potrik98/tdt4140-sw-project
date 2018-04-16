package tdt4140.gr1809.app.ui.graph;

import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
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
import java.util.DoubleSummaryStatistics;
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
    private LocalDateTime timeOfFirstDataPoint;
    private LocalDateTime timeOfLastDataPoint;
    private Boolean showAggregate;
    
    public double max;
    public double min;
    public double avg;

    public DataGraph() {
        final NumberAxis numberAxis = new NumberAxis();
        numberAxis.setForceZeroInRange(false);
        this.graph = new LineChart<>(new LocalDateTimeAxis(), numberAxis);
        graph.setVerticalGridLinesVisible(true);
        graph.setHorizontalGridLinesVisible(true);
        graph.setLegendSide(Side.RIGHT);
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
    
    private void calculateStats(final List<DataPoint> dataPoints) {
    	final DoubleSummaryStatistics statistics = dataPoints.stream()
                .collect(Collectors.summarizingDouble(dataPoint -> (
                        dataPoint.getDataType() == DataPoint.DataType.TEMPERATURE
                                ? dataPoint.getValue() / 100.0
                                : dataPoint.getValue())));
    	avg = statistics.getAverage();
    	min = statistics.getMin();
    	max = statistics.getMax();
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
        System.out.println("Plotting data type " + dataType);
        currentDataType = dataType;
        final List<DataPoint> dataPointsInRange = dataPointsByDataType.get(dataType).stream()
                .filter(dataPoint -> dataPoint.getTime().isAfter(lowerBound)
                        && dataPoint.getTime().isBefore(upperBound))
                .collect(Collectors.toList());

        /*
         * If there are too many data points, aggregate some of them
         */
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
        graph.setData(FXCollections.observableArrayList(new XYChart.Series<>(dataType.name(), chartData)));
        
        if(showAggregate) {
        	timeOfFirstDataPoint = dataPointsInRange.stream()
        		    .map(DataPoint::getTime)
        		    .min(LocalDateTime::compareTo)
        		    .orElse(LocalDateTime.now());
        	timeOfLastDataPoint = dataPointsInRange.stream()
        		    .map(DataPoint::getTime)
        		    .max(LocalDateTime::compareTo)
        		    .orElse(LocalDateTime.now());
        	calculateStats(dataPointsInRange);
        	plotAggregateData(dataType);
        }
    }
    
    private void plotAggregateData(final DataPoint.DataType dataType) {
        final StatisticsClient statisticsClient = new StatisticsClient();

    	final Statistic generalStatistic = statisticsClient.getStatisticsForDataType(dataType);
    	final double statisticValue = dataType == DataPoint.DataType.TEMPERATURE
                ? generalStatistic.getValue() / 100.0
                : generalStatistic.getValue();
    	final Series<Numerable<LocalDateTime>, Number> generalStatisticSeries = new Series<>();
    	generalStatisticSeries.getData().add(new XYChart.Data<>(
    	        numerableBuilder.numerableOfValue(timeOfFirstDataPoint), statisticValue));
    	generalStatisticSeries.getData().add(new XYChart.Data<>(
    	        numerableBuilder.numerableOfValue(timeOfLastDataPoint), statisticValue));
    	generalStatisticSeries.setName("General average");

        final Series<Numerable<LocalDateTime>, Number> averageSeries = new Series<>();
        averageSeries.getData().add(new XYChart.Data<>(
                numerableBuilder.numerableOfValue(timeOfFirstDataPoint), avg));
        averageSeries.getData().add(new XYChart.Data<>(
                numerableBuilder.numerableOfValue(timeOfLastDataPoint), avg));
        averageSeries.setName("Your average");

    	graph.getData().add(generalStatisticSeries);
    	graph.getData().add(averageSeries);
    }

    public void clear() {
        graph.getData().clear();
    }
}
