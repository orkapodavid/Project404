package core.comp3111;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	/**
	 * This function reads an operand in String and translate it into real comparison operation
	 * and return the result in boolean.
	 * @param value
	 * 			-value (Type: double) to be compared with the threshold
	 * @param operand
	 * 			-operand (Type: String) to be used for comparison
	 * 			-operands ">","<", ">=","<=", "==", "!="
	 * @param threshold
	 * 			-threshold (Type: double) to be use for compared
	 * @return boolean
	 * 			-true if operand is supported and value operand threshold
	 * 			-false otherwise 
	 * @author kpor
	 */
	private boolean operandsCompare(double value, String operand, double threshold) {
		switch(operand) {
			case ">":
				if(value > threshold) {
					return true;
				}else {
					return false;
				}
			case "<":
				if(value < threshold) {
					return true;
				}else {
					return false;
				}
			case ">=":
				if(value >= threshold) {
					return true;
				}else {
					return false;
				}
			case "<=":
				if(value <= threshold) {
					return true;
				}else {
					return false;
				}
			case "==":
				if(value == threshold) {
					return true;
				}else {
					return false;
				}
			case "!=":
				if(value != threshold) {
					return true;
				}else {
					return false;
				}
			default:
				return false;
		}
		
	}
	/**
	 * Replace/Create a data set by filtering numeric data of a column
	 * Filter data using various comparison operators (">","<", ">=","<=", "==", "!=")
	 * @return String or null
	 * 			- name of a new DataTable if a new DataTable is put on the HashMap "DataTables"
	 * 			- "Empty DataTable" if all rows in the selected DataTable are filtered out
	 * 			- null if no new DataTable is put on the HashMap "DataTables"
	 * @author kpor
	 */
	public String filterDataset(String datasetName, String colName, String operand, double threshold, boolean isReplaced) {
		// Create a new DataTable
		DataTable filterDataTable  = new DataTable();
		String newDataTableName = null;
		DataTable selectedDataTable = DataTables.get(datasetName);
		Object[] selectedCol = (selectedDataTable.getCol(colName)).getData();
		int originalDataColNum = selectedDataTable.getNumCol();
		int origninalDataRowNum = selectedDataTable.getNumRow();
		String[] newDataColNames = selectedDataTable.getAllColName();
		String[] newDataColTypeNames = new String[originalDataColNum];
		
		// Initialize newDataColsContainer and newDataColTypeNames
		ArrayList<ArrayList<Object>> newDataColsContainer = new ArrayList<ArrayList<Object>>(originalDataColNum);
		DataColumn currentCol;
		for(int i = 0 ;i <originalDataColNum; ++i) {
			ArrayList<Object> temp = new ArrayList<Object>(); 
			newDataColsContainer.add(temp);
			// set all type names
			currentCol = selectedDataTable.getCol(newDataColNames[i]);
			newDataColTypeNames[i] = new String(currentCol.getTypeName());
		}
		
		/* 
		 * loop through all rows in selectedCol and do the comparison for filter
		*/ 
		for(int rowIndex = 0; rowIndex <origninalDataRowNum; ++rowIndex) {
			if(operandsCompare(((Number)selectedCol[rowIndex]).doubleValue(), operand, threshold)) {
				// loop all columns to keep this row of data 
				for(int colIndex = 0; colIndex < originalDataColNum ; ++colIndex) {
					// get a column from the selected DataTable
					currentCol = selectedDataTable.getCol(newDataColNames[colIndex]);
					// add the data in the specific row of the currentCol onto newDataColsContainer[colIndex]
					newDataColsContainer.get(colIndex).add((currentCol.getData())[rowIndex]);
				}
			}
			// Otherwise, ignore this row of data
		}
		
		// handle the exception of all rows are filtered out and filterDataTable has no rows
		if(newDataColsContainer.get(0).size() == 0) {
			return "Empty DataTable";
		}
		
		/*
		 * Initialize filterDataTable with the filtered data columns 
		 */
		for(int colIndex = 0 ;colIndex <originalDataColNum; ++colIndex) {
			currentCol = new DataColumn();
			currentCol.set(newDataColNames[colIndex], newDataColsContainer.get(colIndex).toArray());
			try {
				filterDataTable.addCol(newDataColNames[colIndex], currentCol);
			} catch (DataTableException e) {
				e.printStackTrace();
			}
		}
		
		if(isReplaced) {
			DataTables.replace(datasetName,filterDataTable);
		}else {
			newDataTableName = "DataSet" + (DataTables.size() + 1);
			DataTables.put(newDataTableName, filterDataTable);
		}
		
		return newDataTableName;

	}
	
	/**
	 * Randomly split a data set into 2 data sets
	 * Replacing the current data set OR Creating a new data set(s)
	 * After the split, both data sets should keep the same column names
	 * @author kpor
	 */
	public void randSplitDataset(String datasetName, int splitRatio, boolean isReplaced) {
		
		
	}
}
