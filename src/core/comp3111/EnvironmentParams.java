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
			currentCol.set(newDataColTypeNames[colIndex], newDataColsContainer.get(colIndex).toArray());
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
	 * @return String[] or null
	 * 			- first String in the array will be name of first new dataset
	 * 			- second String in the array will be name of second new dataset
	 * 			- "" String in the first/second index of array if first/second new dataset is empty
	 * @author kpor
	 */
	public String[] randSplitDataset(String datasetName, int splitRatio, boolean isReplaced) {
		int splittedNum;
		DataTable splitDataTableA  = new DataTable();
		DataTable splitDataTableB  = new DataTable();
		DataTable selectedDataTable = DataTables.get(datasetName);
		int originalDataColNum = selectedDataTable.getNumCol();
		int origninalDataRowNum = selectedDataTable.getNumRow();
		String[] newDataColNames = selectedDataTable.getAllColName();
		String[] newDataColTypeNames = new String[originalDataColNum];
		
		// Calculate how many row(s) will be kept in a dataset
		splittedNum = (int)Math.round(((double)splitRatio/100.0)*origninalDataRowNum);
		System.out.println("splittedNum =  " + splittedNum);
		
		// Initialize rowIndexMap to randomly generate indexes of rows to be put on a dataset
		HashMap<Integer, Boolean> rowIndexMap = new HashMap<Integer, Boolean>(origninalDataRowNum);
		int randIndex;
		for(int num = 0; num < splittedNum; num++) {
			// Randomly generate an index number from 0 to (no. of rows - 1) 
			randIndex = (int)Math.round(Math.random()*(origninalDataRowNum-1));
			// set true for new random index
			if(!rowIndexMap.containsKey(randIndex)) {
				rowIndexMap.put(randIndex, true);
				System.out.println("Seting " + randIndex + " to be true");
			}else {
			// Loop one more time if a random index has been already generated
				num -= 1;
			}
		}
		
		for(int num = 0; num < origninalDataRowNum; num++) {
			// set false for all numbers within a range of 0 to no. of rows has not been randomly generated
			if(!rowIndexMap.containsKey(num)) {
				rowIndexMap.put(num, false);
			}
		}
		
		// Initialize newDataColsContainerA, newDataColsContainerB and newDataColTypeNames
		ArrayList<ArrayList<Object>> newDataColsContainerA = new ArrayList<ArrayList<Object>>(originalDataColNum);
		ArrayList<ArrayList<Object>> newDataColsContainerB = new ArrayList<ArrayList<Object>>(originalDataColNum);
		DataColumn currentCol;
		for(int i = 0 ;i <originalDataColNum; ++i) {
			ArrayList<Object> tempA = new ArrayList<Object>(); 
			ArrayList<Object> tempB = new ArrayList<Object>(); 
			newDataColsContainerA.add(tempA);
			newDataColsContainerB.add(tempB);
			// set all type names
			currentCol = selectedDataTable.getCol(newDataColNames[i]);
			newDataColTypeNames[i] = new String(currentCol.getTypeName());
		}
		
		System.out.println("rowIndexMap: " + rowIndexMap);
		
		// Loop rowIndexMap to split the rows onto newDataColsContainerA and newDataColsContainerB
		Set<Integer> rowsSet =  rowIndexMap.keySet();
		for(Integer rowIndex:rowsSet) {
			if(rowIndexMap.get(rowIndex) == true) {		
				// true ---> newDataColsContainerA
				// loop all columns to keep this row of data 
				for(int colIndex = 0; colIndex < originalDataColNum ; ++colIndex) {
					// get a column from the selected DataTable
					currentCol = selectedDataTable.getCol(newDataColNames[colIndex]);
					// add the data in the specific row of the currentCol onto newDataColsContainer[colIndex]
					newDataColsContainerA.get(colIndex).add((currentCol.getData())[rowIndex]);
				}		
			}else {
				// false ---> newDataColsContainerB
				// loop all columns to keep this row of data 
				for(int colIndex = 0; colIndex < originalDataColNum ; ++colIndex) {
					// get a column from the selected DataTable
					currentCol = selectedDataTable.getCol(newDataColNames[colIndex]);
					// add the data in the specific row of the currentCol onto newDataColsContainer[colIndex]
					newDataColsContainerB.get(colIndex).add((currentCol.getData())[rowIndex]);
				}			
			}
		}
		
		String[] resultArr = new String[2];
		String newDataTableNameA = new String("");
		String newDataTableNameB = new String("");
		// handle the exception of one of the new dataTable has no rows
		if(newDataColsContainerA.get(0).size() == 0) {
			// Initialize splitDataTableB with newDataColsContainerB
			for(int colIndex = 0 ;colIndex <originalDataColNum; ++colIndex) {
				currentCol = new DataColumn();
				currentCol.set(newDataColTypeNames[colIndex], newDataColsContainerB.get(colIndex).toArray());
				try {
					splitDataTableB.addCol(newDataColNames[colIndex], currentCol);
				} catch (DataTableException e) {
					e.printStackTrace();
				}
			}
			if(!isReplaced) {
				newDataTableNameB = "DataSet" + (DataTables.size() + 1);
				DataTables.put(newDataTableNameB, splitDataTableB);
			}
		}else if(newDataColsContainerB.get(0).size() == 0) {
			// Initialize splitDataTableA with newDataColsContainerA
			for(int colIndex = 0 ;colIndex <originalDataColNum; ++colIndex) {
				currentCol = new DataColumn();
				currentCol.set(newDataColTypeNames[colIndex], newDataColsContainerA.get(colIndex).toArray());
				try {
					splitDataTableA.addCol(newDataColNames[colIndex], currentCol);
				} catch (DataTableException e) {
					e.printStackTrace();
				}
			}
			if(!isReplaced) {
				newDataTableNameA = "DataSet" + (DataTables.size() + 1);
				DataTables.put(newDataTableNameA, splitDataTableA);
			}
		}else {
			// both newDataColsContainerA and newDataColsContainerB have at least one row
			
			// Initialize splitDataTableA with newDataColsContainerA
			for(int colIndex = 0 ;colIndex <originalDataColNum; ++colIndex) {
				currentCol = new DataColumn();
				currentCol.set(newDataColTypeNames[colIndex], newDataColsContainerA.get(colIndex).toArray());
				try {
					splitDataTableA.addCol(newDataColNames[colIndex], currentCol);
				} catch (DataTableException e) {
					e.printStackTrace();
				}
			}
			// Initialize splitDataTableB with newDataColsContainerB
			for(int colIndex = 0 ;colIndex <originalDataColNum; ++colIndex) {
				currentCol = new DataColumn();
				currentCol.set(newDataColTypeNames[colIndex], newDataColsContainerB.get(colIndex).toArray());
				try {
					splitDataTableB.addCol(newDataColNames[colIndex], currentCol);
				} catch (DataTableException e) {
					e.printStackTrace();
				}
			}
			
			/*
			System.out.println("---------A---------");
			splitDataTableA.print();
			System.out.println("---------B---------");
			splitDataTableB.print();
			*/
			
			if(isReplaced) {
				// replace the original dataset with splitDataTableA 
				DataTables.replace(datasetName,splitDataTableA);
				newDataTableNameA = new String(datasetName);
				newDataTableNameB = "DataSet" + (DataTables.size() + 1);
				// put splitDataTableB into DataTables
				DataTables.put(newDataTableNameB, splitDataTableB);
			}else {
				newDataTableNameA = "DataSet" + (DataTables.size() + 1);
				newDataTableNameB = "DataSet" + (DataTables.size() + 2);
				DataTables.put(newDataTableNameA, splitDataTableA);
				DataTables.put(newDataTableNameB, splitDataTableB);
				
			}
		}
		
		resultArr[0] = newDataTableNameA;
		resultArr[1] = newDataTableNameB;
		return resultArr;
		
	}
}
