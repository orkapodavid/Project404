package core.comp3111;


import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

/**
 * This class used for containing parameters of a Line Chart for clearer structure.
 * 
 * @author kpor
 * @see DataTable
 * @see DataColumn
 */
public class LineChartClass implements Chart, Externalizable {

	private XYChart.Series<Number, Number> series;
	private String xAxisName;
	private String yAxisName;
	private String title;
	private boolean animation;

	/**
	 * Construct an empty line chart with no animation.
	 */
	public LineChartClass() {
		series = null;
		xAxisName = "";
		yAxisName = "";
		title = "";
		animation = false;
	}
	
	/**
	 * Construct a line chart with selected X-axis and Y-axis of a selected DataTable.
	 * <p>Set the parameter, animated, to create an animated line chart.
	 * <p>The title of the line chart will be "Line Chart of " plus the name of selected DataTable.
	 * @param currentDataTable the selected DataTable
	 * @param chartXaxisName name of the selected DataColumn for X-axis
	 * @param chartYaxisName name of the selected DataColumn for Y-axis
	 * @param currentDatasetName name of the selected DataTable
	 * @param animated set true to create an animated line chart
	 */
	public LineChartClass(DataTable currentDataTable, String chartXaxisName,String chartYaxisName, String currentDatasetName, boolean animated) {
		
		// Get 2 columns
		DataColumn xCol = currentDataTable.getCol(chartXaxisName);
		DataColumn yCol = currentDataTable.getCol(chartYaxisName);
		title = "Line Chart of " + currentDatasetName;
		xAxisName = chartXaxisName;
		yAxisName = chartYaxisName;
		
		// defining a series
		series = new XYChart.Series<Number, Number>();
		series.setName(currentDatasetName);
		// populating the series with data
		// In DataTable structure, both length must be the same
		for (int i = 0; i < xCol.getSize(); i++) {
			series.getData()
					.add(new XYChart.Data<Number, Number>((Number) xCol.getData()[i], (Number) yCol.getData()[i]));
		}
		if (animated)
			animation = true;
		else
			animation = false;
	}

	/**
	 * The LineChartClass implements the writeExternal method to save its contents by 
	 * calling the writeObject method of ObjectOutput for strings and numbers.
	 * 
	 * @param out the stream to write the object to
	 * @throws IOException Includes any I/O exceptions that may occur
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(series.getData().size());
		out.writeObject(series.getName());
		out.writeObject(xAxisName);
		out.writeObject(yAxisName);
		out.writeObject(title);
		out.writeBoolean(animation);
		for (Data<Number, Number> data : series.getData()) {
			out.writeObject(data.getXValue());
			out.writeObject(data.getYValue());
		}
	}
	
	/**
	 * The LineChartClass implements the readExternal method to restore its contents by 
	 * calling the readObject method of ObjectOutput for strings and numbers. 
	 * <p>The readExternal method must read the values in the same sequence and with the same types as were written by writeExternal.
	 * 
	 * @param in the stream to read data from in order to restore the object
	 * @throws IOException Includes any I/O exceptions that may occur
	 * @throws ClassNotFoundException If the class for an object being restored cannot be found
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int size = in.readInt();
		series = new XYChart.Series<Number, Number>();
		series.setName((String)in.readObject());
		xAxisName = (String)in.readObject();
		yAxisName = (String)in.readObject();
		title = (String)in.readObject();
		animation = in.readBoolean();
		for (int i = 0; i < size; i++) {
			series.getData().add(new Data<Number, Number>((Number) in.readObject(), (Number) in.readObject()));
		}
	}

	/**
	 * Associate a XYChart.Series to the line chart.
	 * 
	 * @param m_series a named series of data items of the line chart
	 */
	public void setSeries(XYChart.Series<Number, Number> m_series) {
		series = m_series;
	}

	/**
	 * Return the XYChart.Series of the line chart.
	 * 
	 * @return XYChart.Series reference or null.
	 */
	public XYChart.Series<Number, Number> getSeries() {
		return series;
	}

	/**
	 * Set option of animation of the line chart.
	 * 
	 * @param state true if an animated line chart is desired, false otherwise
	 */
	public void animate(boolean state) {
		animation = state;
	}

	/**
	 * Return the option of animation of the line chart.
	 * 
	 * @return boolean true if the line chart is animated, false otherwise
	 */
	public boolean get_animate() {
		return animation;
	}

	/**
	 * Return the line chart's X-axis name.
	 * 
	 * @return String or Empty String
	 */
	public String getXAxisName() {
		return xAxisName;
	}

	/**
	 * Return the line chart's Y-axis name.
	 * 
	 * @return String or Empty String
	 */
	public String getYAxisName() {
		return yAxisName;
	}

	/**
	 * Return the line chart's title.
	 * 
	 * @return String or Empty String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Return the line chart's Axis name.
	 * 
	 * @param x the name of X axis of the line chart.
	 * @param y the name of Y axis of the line chart.
	 */
	public void setAxisName(String x, String y) {
		xAxisName = x;
		yAxisName = y;
	}

	/**
	 * Return the line chart's title.
	 * 
	 * @param m_title the title of the line chart.
	 */
	public void setTitle(String m_title) {
		title = m_title;
	}

}
