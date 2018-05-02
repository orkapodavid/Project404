package core.comp3111;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

/**
 * This class used for containing parameters of a Pie Chart for clearer
 * structure.
 * 
 * @author OR Ka Po, kpor
 * @see DataTable
 * @see DataColumn
 */
public class PieChartClass implements Chart, Externalizable {

	private ObservableList<PieChart.Data> pieChartDataList;
	private String numColName;
	private String textColName;
	private String title;

	/**
	 * Construct an empty pie chart.
	 */
	public PieChartClass() {
		pieChartDataList = null;
		numColName = "";
		textColName = "";
		title = "";
	}

	/**
	 * Construct a pie chart with selected numeric column and text column of a selected DataTable.
	 * <p>The title of the line chart will be "Pie Chart of " plus the name of selected DataTable.
	 * @param currentDataTable the selected DataTable
	 * @param chartNumColName name of the selected DataColumn for numeric column
	 * @param chartTextColName name of the selected DataColumn for text column
	 * @param currentDatasetName name of the selected DataTable
	 */
	public PieChartClass(DataTable currentDataTable, String chartNumColName, String chartTextColName,
			String currentDatasetName) {
		
		
		numColName = chartNumColName;
		textColName = chartTextColName;	
		// Get 2 columns
		DataColumn numCol = currentDataTable.getCol(chartNumColName);
		DataColumn textCol = currentDataTable.getCol(chartTextColName);
		
		pieChartDataList = FXCollections.observableArrayList();

		Object[] numColValues = numCol.getData();
		Object[] textColValues = textCol.getData();

		int len = numColValues.length;
		for (int i = 0; i < len; i++) {
			pieChartDataList
					.add(new PieChart.Data((String) textColValues[i], ((Number) numColValues[i]).doubleValue()));
		}
		title = "Pie Chart of " + currentDatasetName;
	}
	
	/**
	 * Return the ObservableList of the pie chart data.
	 * 
	 * @return ObservableList or null
	 */

	public ObservableList<PieChart.Data> getObserList() {
		return pieChartDataList;
	}

	/**
	 * Set the pie chart data with an ObservableList.
	 * 
	 * @param newList ObservableList containing all PieChart.Data of the pie chart
	 */
	public void setList(ObservableList<PieChart.Data> newList) {
		pieChartDataList = newList;
	}
	
	
	/**
	 * Set the title of the pie chart.
	 * 
	 * @param m_title title of the pie chart
	 */
	public void setTitle(String m_title) {
		title = m_title;
	}

	/**
	 * Set the names of the pie chart's axes.
	 * 
	 * @param num the numeric axis name of the pie chart.
	 * @param text the text axis name of the pie chart.
	 */
	public void setAxisName(String num, String text) {
		numColName = num;
		textColName = text;
	}
	
	/**
	 * Return the title of the pie chart.
	 * 
	 * @return String or Empty String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Return the pie chart's numeric column name.
	 * 
	 * @return String or Empty String
	 */
	public String getNumColName() {
		return numColName;
	}

	/**
	 * Return the pie chart's text column name.
	 * 
	 * @return String or Empty String
	 */
	public String getTextColName() {
		return textColName;
	}

	
	/**
	 * The PieChartClass implements the writeExternal method to save its contents by 
	 * calling the writeObject method of ObjectOutput for strings and numbers.
	 * 
	 * @param out the stream to write the object to
	 * @throws IOException Includes any I/O exceptions that may occur
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(pieChartDataList.size());
		out.writeObject(textColName);
		out.writeObject(numColName);
		out.writeObject(title);
		for (PieChart.Data data : pieChartDataList) {
			out.writeObject(data.getName());
			out.writeObject(data.getPieValue());
			System.out.println("write Object: " + data.getName() + " &" + data.getPieValue());
		}
	}

	/**
	 * The PieChartClass implements the readExternal method to restore its contents by 
	 * calling the readObject method of ObjectOutput for strings and numbers. 
	 * <p>The readExternal method must read the values in the same sequence and with the same types as were written by writeExternal.
	 * 
	 * @param in the stream to read data from in order to restore the object
	 * @throws IOException Includes any I/O exceptions that may occur
	 * @throws ClassNotFoundException If the class for an object being restored cannot be found.
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int size = in.readInt();
		textColName = (String)in.readObject();
		numColName = (String)in.readObject();
		title = (String)in.readObject();
		pieChartDataList = FXCollections.observableArrayList();
		for (int i = 0; i < size; i++) {
			pieChartDataList.add(new PieChart.Data((String) in.readObject(), ((Number) in.readObject()).doubleValue()));
		}
	}
}
