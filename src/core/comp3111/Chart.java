package core.comp3111;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public interface Chart {
	public XYChart.Series<Number, Number> getSeries();
}
