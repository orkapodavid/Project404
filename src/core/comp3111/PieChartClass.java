package core.comp3111;

import java.io.Serializable;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

/**
 * This class used for containing parameters of a Pie Chart for clearer structure.
 * 
 * @author kpor
 */
public class PieChartClass implements Serializable{
	
	private ObservableList<PieChart.Data> pieChartDataList;
	private String numColName;
	private String textColName;
	private String title;
	
	public PieChartClass() {
		pieChartDataList = null;
		numColName = "";
		textColName = "";
		title = "";
	}
	
	public ObservableList<PieChart.Data> getObserList(){
		return pieChartDataList;
	}
	
	public void setList(ObservableList<PieChart.Data> newList) {
		pieChartDataList = newList;
	}
	
	public void setTitle(String m_title) {
		title = m_title;
	}
	
	/**
	 * Record the pie chart's Axis name.
	 * 
	 * @param num
	 *            -the X axis name of the pie chart.
	 * @param text
	 *            -the Y axis name of the pie chart.
	 */
	public void setAxisName(String num, String text) {
		numColName = num;
		textColName = text;
	}

	
	public String getTitle() {
		return title;
	}
	
	/**
	 * Get the pie chart's numerical column name.
	 * 
	 * @return String
	 */
	public String getNumColName() {
		return numColName;
	}

	/**
	 * Get the pie chart's text column name.
	 * 
	 * @return String
	 */
	public String getTextColName() {
		return textColName;
	}

}
