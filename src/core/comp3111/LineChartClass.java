package core.comp3111;

import javafx.scene.chart.XYChart;

public class LineChartClass implements Chart {
	
	public LineChartClass() {
		series = null;
		xAxisName = "";
		yAxisName = "";
		title = "";
	}

	public void setSeries(XYChart.Series<Number, Number> m_series) {
		series = m_series;
	}

	public XYChart.Series<Number, Number> getSeries() {
		return series;
	}
	
	public String getXAxisName() {
		return xAxisName;
	}
	
	public String getYAxisName() {
		return yAxisName;
	}
	
	public String getTitle() {
		return title;
	}

	public void setAxisName(String x, String y) {
		xAxisName = x;
		yAxisName = y;
	}
	public void setTitle(String m_title) {
		title = m_title;
	}
	
	private XYChart.Series<Number, Number> series;
	private String xAxisName;
	private String yAxisName;
	private String title;
}
