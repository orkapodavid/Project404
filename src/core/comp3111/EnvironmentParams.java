package core.comp3111;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class used for containing parameters of the environment for clearer structure.
 * 
 * @author kwaleung
 */
public class EnvironmentParams implements Serializable {

	private HashMap<String, DataTable> DataTables;
	private Map<String, LineChartClass> lineChartsMap;
	private Map<String, PieChartClass> pieChartsMap;
	
	/**
	 * Default constructor for EnvironmentParams class.
	 * 
	 * @author kwaleung
	 */
	public EnvironmentParams() {
		DataTables = new HashMap<String, DataTable>();
		lineChartsMap = new HashMap<String, LineChartClass>();
		pieChartsMap = new HashMap<String, PieChartClass>();
	}
	
	/**
	 * Set the HashMap of data tables in the environment
	 * 
	 * @param newDataTables - data tables to be put into environment
	 * 
	 * @author kwaleung
	 */
	public void setEnvironmentDataTables(HashMap<String, DataTable> newDataTables) {
		DataTables = newDataTables;
	}
	
	/**
	 * Set the HashMap of line charts in the environment
	 * 
	 * @param newLineCharts - line charts to be put into environment
	 * 
	 * @author kwaleung
	 */
	public void setEnviornmentLineCharts(HashMap<String, LineChartClass> newLineCharts) {
		lineChartsMap = newLineCharts;
	}
	
	/**
	 * Set the HashMap of pie charts in the environment
	 * 
	 * @param newPieCharts - pie charts to be put into environment
	 * 
	 * @author kwaleung
	 */
	public void setEnviornmentPieCharts(HashMap<String, PieChartClass> newPieCharts) {
		pieChartsMap = newPieCharts;
	}
	
	/**
	 * Returns the HashMap of data tables in the environment
	 * @return HashMap of data tables
	 * 
	 * @author kwaleung
	 */
	public HashMap<String, DataTable> getEnvironmentDataTables() {
		return DataTables;
	}
	
	/**
	 * Returns the HashMap of line charts in the environment
	 * @return Map of line charts
	 * 
	 * @author kwaleung
	 */
	public Map<String, LineChartClass> getEnviornmentLineCharts() {
		return lineChartsMap;
	}
	
	/**
	 * Returns the HashMap of pie charts in the environment
	 * @return Map of pie charts
	 * 
	 * @author kwaleung
	 */
	public Map<String, PieChartClass> getEnviornmentPieCharts() {
		return pieChartsMap;
	}	
	
}
