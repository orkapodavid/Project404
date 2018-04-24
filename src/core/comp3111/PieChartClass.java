package core.comp3111;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

/**
 * This class used for containing parameters of a Pie Chart for clearer structure.
 * 
 * @author kpor
 */
public class PieChartClass implements Chart, Externalizable{
	
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
	
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(pieChartDataList.size());
		out.writeChars(textColName);
		out.writeChar('\n');
		out.writeChars(numColName);
		out.writeChar('\n');
		out.writeChars(title);
		out.writeChar('\n');
		for( PieChart.Data data: pieChartDataList) {
			out.writeObject(data.getName());
			out.writeObject(data.getPieValue());
			System.out.println("write Object: "+ data.getName()+" &"+data.getPieValue());
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int size = in.readInt();
		textColName = in.readLine();
		numColName = in.readLine();
		title = in.readLine();
		pieChartDataList = FXCollections.observableArrayList();
		for(int i = 0 ; i < size; i++) {
			pieChartDataList.add(new PieChart.Data((String) in.readObject(),((Number) in.readObject()).doubleValue()));
		}
	}
}
