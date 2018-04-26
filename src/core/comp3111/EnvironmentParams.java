package core.comp3111;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	 * Set the HashMap of data tables in the environment>
	 * 
	 * @author kwaleung
	 */
	public void setEnvironmentDataTables(HashMap<String, DataTable> newDataTables) {
		DataTables = newDataTables;
	}
	
	/**
	 * Set the HashMap of line charts in the environment>
	 * 
	 * @author kwaleung
	 */
	public void setEnviornmentLineCharts(HashMap<String, LineChartClass> newLineCharts) {
		lineChartsMap = newLineCharts;
	}
	
	/**
	 * Set the HashMap of pie charts in the environment>
	 * 
	 * @author kwaleung
	 */
	public void setEnviornmentPieCharts(HashMap<String, PieChartClass> newPieCharts) {
		pieChartsMap = newPieCharts;
	}
	
	/**
	 * Returns the HashMap of data tables in the environment
	 * @return HashMap<String, DataTable>
	 * 
	 * @author kwaleung
	 */
	public HashMap<String, DataTable> getEnvironmentDataTables() {
		return DataTables;
	}
	
	/**
	 * Returns the HashMap of line charts in the environment
	 * @return Map<String, LineChartClass>
	 * 
	 * @author kwaleung
	 */
	public Map<String, LineChartClass> getEnviornmentLineCharts() {
		return lineChartsMap;
	}
	
	/**
	 * Returns the HashMap of pie charts in the environment
	 * @return Map<String, PieChartClass>
	 * 
	 * @author kwaleung
	 */
	public Map<String, PieChartClass> getEnviornmentPieCharts() {
		return pieChartsMap;
	}	
	
}
