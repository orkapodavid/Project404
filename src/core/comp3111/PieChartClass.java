package core.comp3111;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

public class PieChartClass {
	
	private ObservableList<PieChart.Data> pieChartDataList;
	private String title;
	
	public PieChartClass() {
		pieChartDataList = null;
		title = null;
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
	
	public String getTitle() {
		return title;
	}

}
