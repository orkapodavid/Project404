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
	 */
	public EnvironmentParams() {
		DataTables = new HashMap<String, DataTable>();
		lineChartsMap = new HashMap<String, LineChartClass>();
		pieChartsMap = new HashMap<String, PieChartClass>();
	}
	
	public void setEnvironmentDataTables(HashMap<String, DataTable> newDataTables) {
		DataTables = newDataTables;
	}
	
	public void setEnviornmentLineCharts(HashMap<String, LineChartClass> newLineCharts) {
		lineChartsMap = newLineCharts;
	}
	
	public void setEnviornmentPieCharts(HashMap<String, PieChartClass> newPieCharts) {
		pieChartsMap = newPieCharts;
	}
	
	public HashMap<String, DataTable> getEnvironmentDataTables() {
		return DataTables;
	}
	
	public Map<String, LineChartClass> getEnviornmentLineCharts() {
		return lineChartsMap;
	}
	
	public Map<String, PieChartClass> getEnviornmentPieCharts() {
		return pieChartsMap;
	}
}
