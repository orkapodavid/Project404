package core.comp3111;

import java.io.Serializable;

import javafx.scene.chart.XYChart;

public class LineChartClass implements Chart, Serializable {
	
	/**
	 * Construct - Create an empty copy of all the essential data in a line chart.
	 */
	public LineChartClass() {
		series = null;
		xAxisName = "";
		yAxisName = "";
		title = "";
		animation = false;
	}
	
	/**
	 * Associate a XYChart.Series to the line chart.
	 * 
	 * @param m_series
	 *            - a XYChart.Series containing all the content of a line chart.
	 */
	public void setSeries(XYChart.Series<Number, Number> m_series) {
		series = m_series;
	}
	
	/**
	 * Retrieve the XYChart.Series of the line chart.
	 * 
	 * @return XYChart.Series reference or null.
	 */
	public XYChart.Series<Number, Number> getSeries() {
		return series;
	}
	
	/**
	 * Record the line chart's state.
	 * 
	 * @param state
	 *            - either animated line chart or static line chart.
	 */
	public void animate(boolean state) {
		animation = state;
	}
	
	/**
	 * Get the line chart's state.
	 * 
	 * @return boolean
	 */
	public boolean get_animate() {
		return animation;
	}

	/**
	 * Get the line chart's XAxis name.
	 * 
	 * @return String
	 */
	public String getXAxisName() {
		return xAxisName;
	}
	
	/**
	 * Get the line chart's YAxis name.
	 * 
	 * @return String
	 */
	public String getYAxisName() {
		return yAxisName;
	}
	
	/**
	 * Get the line chart's title.
	 * 
	 * @return String
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Record the line chart's Axis name.
	 * 
	 * @param x
	 * 			-the X axis name of the line chart.
	 * @param y
	 * 			-the Y axis name of the line chart.
	 */
	public void setAxisName(String x, String y) {
		xAxisName = x;
		yAxisName = y;
	}
	

	/**
	 * Record the line chart's title.
	 * 
	 * @param m_title
	 * 			-the title of the line chart.
	 */
	public void setTitle(String m_title) {
		title = m_title;
	}
	
	private XYChart.Series<Number, Number> series;
	private String xAxisName;
	private String yAxisName;
	private String title;
	private boolean animation;
}
