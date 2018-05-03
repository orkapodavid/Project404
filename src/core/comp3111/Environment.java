package core.comp3111;

import ui.comp3111.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.*;

/**
 * This class provides a centralized interface for accessing data tables and charts.
 * It also provides save and load functions which serializes the EnvironmentParams class using a custom file extension ".comp3111"
 * 
 * @author kwaleung
 * @author OR Ka Po, kpor
 *
 */
public class Environment{

	private EnvironmentParams envParams;
	
	/**
	 * Default constructor of Environment class.
	 */
	public Environment() {		
		envParams = new EnvironmentParams();
	}
	
	public void setEnvironmentDataTables(HashMap<String, DataTable> newDataTables) {
		envParams.setEnvironmentDataTables(newDataTables);
	}
	
	public void setEnviornmentLineCharts(HashMap<String, LineChartClass> newLineCharts) {
		envParams.setEnviornmentLineCharts(newLineCharts);
	}
	
	public void setEnviornmentPieCharts(HashMap<String, PieChartClass> newPieCharts) {
		envParams.setEnviornmentPieCharts(newPieCharts);
	}
	
	/**
	 * This function reads an operator in String and translate it into real comparison operation
	 * and return the result in boolean.
	 * @param value
	 * 			value (Type: double) to be compared with the threshold
	 * @param operator
	 * 			operand ("&#62;","&#60;", "&#62;=","&#60;=", "==", "!=") to be used for comparison
	 * @param threshold
	 * 			threshold (Type: double) to be use for compared
	 * @return boolean
	 * 			true if the operation of "value operator threshold" returns true, false otherwise 
	 * @author OR Ka Po， kpor
	 */
	public boolean operandsCompare(double value, String operator, double threshold) {
		switch(operator) {
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
	 * Return the name of a new DataTable.
	 * <p>Replace/Create a data set by filtering numeric data of a column.
	 * <p>Filter data using various comparison operators ("&#62;","&#60;", "&#62;=","&#60;=", "==", "!=").
	 * @param datasetName 
	 * 			name of the selected DataTable
	 * @param colName
	 * 			name of the column being filtered
	 * @param operator
	 * 			operator to be used for comparison
	 * @param threshold
	 * 			value to be used for comparison
	 * @param isReplaced
	 * 			set true to replace the selected DataTable with the filtered DataTable
	 * 			<p>set false to create a new DataTable with the filtered rows
	 * @return String or null name of a new DataTable, "Empty DataTable" or null
	 * @author OR Ka Po， kpor
	 * @throws DataTableException if operation on DataTable is invalid
	 */
	public String filterDatasetByNum(String datasetName, String colName, String operator,  double threshold, boolean isReplaced) throws DataTableException {
		// Create a new DataTable
		DataTable filterDataTable  = new DataTable();
		String newDataTableName = null;
		DataTable selectedDataTable = envParams.getEnvironmentDataTables().get(datasetName);
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
			if(operandsCompare(((Number)selectedCol[rowIndex]).doubleValue(), operator, threshold)) {
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
			filterDataTable.addCol(newDataColNames[colIndex], currentCol);
		}
		
		if(isReplaced) {
			envParams.getEnvironmentDataTables().replace(datasetName,filterDataTable);
		}else {
			newDataTableName = "DataSet" + (envParams.getEnvironmentDataTables().size() + 1);
			envParams.getEnvironmentDataTables().put(newDataTableName, filterDataTable);
		}
		
		return newDataTableName;
	}
	
	/**
	 * Return an array of String of two split DataTable
	 * <p>Randomly split a data set into 2 data sets by rows.
	 * <p>Set the parameters, isReplaced, to replace the current data set OR to create new DataTable(s).
	 * <p>After the split, both data sets should keep the same column names.
	 * @param datasetName name of the selected DataTable
	 * @param splitRatio the percentage of split (0% to 100%)
	 * @param isReplaced
	 * 			set true to replace the selected DataTable with one of the new DataTable
	 * 			<p>set false to create a new DataTable with the filtered rows
	 * @return String[] or null
	 * 			First String in the array will be name of first new DataTable
	 * 			<p>Second String in the array will be name of second new DataTable
	 * 			<p>"" (Empty String) in the first/second index of array if the first/second new DataTable is empty
	 * @author OR Ka Po， kpor
	 * @throws DataTableException if operation on DataTable is invalid
	 */
	public String[] randSplitDatasetByNum(String datasetName, int splitRatio, boolean isReplaced) throws DataTableException {
		int splittedNum;
		DataTable splitDataTableA  = new DataTable();
		DataTable splitDataTableB  = new DataTable();
		DataTable selectedDataTable = envParams.getEnvironmentDataTables().get(datasetName);
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
				//System.out.println("Seting " + randIndex + " to be true");
			}else {
			// Loop one more time if a random index has been already generated
				//System.out.println("Repeated Index = " + randIndex);
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
				splitDataTableB.addCol(newDataColNames[colIndex], currentCol);
			}
			if(!isReplaced) {
				newDataTableNameB = "DataSet" + (envParams.getEnvironmentDataTables().size() + 1);
				envParams.getEnvironmentDataTables().put(newDataTableNameB, splitDataTableB);
			}
		}else if(newDataColsContainerB.get(0).size() == 0) {
			// Initialize splitDataTableA with newDataColsContainerA
			for(int colIndex = 0 ;colIndex <originalDataColNum; ++colIndex) {
				currentCol = new DataColumn();
				currentCol.set(newDataColTypeNames[colIndex], newDataColsContainerA.get(colIndex).toArray());
				splitDataTableA.addCol(newDataColNames[colIndex], currentCol);
			}
			if(!isReplaced) {
				newDataTableNameA = "DataSet" + (envParams.getEnvironmentDataTables().size() + 1);
				envParams.getEnvironmentDataTables().put(newDataTableNameA, splitDataTableA);
			}
		}else {
			// both newDataColsContainerA and newDataColsContainerB have at least one row
			
			// Initialize splitDataTableA with newDataColsContainerA
			for(int colIndex = 0 ;colIndex <originalDataColNum; ++colIndex) {
				currentCol = new DataColumn();
				currentCol.set(newDataColTypeNames[colIndex], newDataColsContainerA.get(colIndex).toArray());
				splitDataTableA.addCol(newDataColNames[colIndex], currentCol);
			}
			// Initialize splitDataTableB with newDataColsContainerB
			for(int colIndex = 0 ;colIndex <originalDataColNum; ++colIndex) {
				currentCol = new DataColumn();
				currentCol.set(newDataColTypeNames[colIndex], newDataColsContainerB.get(colIndex).toArray());
				splitDataTableB.addCol(newDataColNames[colIndex], currentCol);
			}
			
			/*
			System.out.println("---------A---------");
			splitDataTableA.print();
			System.out.println("---------B---------");
			splitDataTableB.print();
			*/
			
			if(isReplaced) {
				// replace the original dataset with splitDataTableA 
				envParams.getEnvironmentDataTables().replace(datasetName,splitDataTableA);
				newDataTableNameA = new String(datasetName);
				newDataTableNameB = "DataSet" + (envParams.getEnvironmentDataTables().size() + 1);
				// put splitDataTableB into DataTables
				envParams.getEnvironmentDataTables().put(newDataTableNameB, splitDataTableB);
			}else {
				newDataTableNameA = "DataSet" + (envParams.getEnvironmentDataTables().size() + 1);
				newDataTableNameB = "DataSet" + (envParams.getEnvironmentDataTables().size() + 2);
				envParams.getEnvironmentDataTables().put(newDataTableNameA, splitDataTableA);
				envParams.getEnvironmentDataTables().put(newDataTableNameB, splitDataTableB);
				
			}
		}
		
		resultArr[0] = newDataTableNameA;
		resultArr[1] = newDataTableNameB;
		return resultArr;
		
	}
	
	/**
	 * Returns the HashMap of data tables in the environment
	 * @return HashMap of data tables
	 * 
	 * @author kwaleung
	 */
	public HashMap<String, DataTable> getEnvironmentDataTables() {
		return envParams.getEnvironmentDataTables();
	}
	
	/**
	 * Returns the HashMap of line charts in the environment
	 * @return Map of line charts
	 * 
	 * @author kwaleung
	 */
	public Map<String, LineChartClass> getEnviornmentLineCharts() {
		return envParams.getEnviornmentLineCharts();
	}
	
	/**
	 * Returns the HashMap of pie charts in the environment
	 * @return Map of pie charts
	 * 
	 * @author kwaleung
	 */
	public Map<String, PieChartClass> getEnviornmentPieCharts() {
		return envParams.getEnviornmentPieCharts();
	}
	
	/**
	 * This function saves the Environment Parameters to as a .comp3111 file in a user designated directory
	 *  by serializing an EnvironmentParams object.
	 *  
	 *  @param selectedFile - selected file object to be saved to
	 *  
	 *  @return file path where the environment is saved to
	 *  
	 *  @throws Exception - throws exceptions when there is error while saving
	 *  
	 *  @author kwaleung
	 */
	public String saveEnvironment(File selectedFile) throws Exception {
		 
		FileOutputStream oStream = null;
		ObjectOutputStream objOStream = null;
		
		String filePath = selectedFile.getAbsolutePath();
		System.out.println("saveEnv: filePath = " + filePath);
		oStream = new FileOutputStream(filePath);
		objOStream = new ObjectOutputStream(oStream);
		objOStream.writeObject(envParams);
		objOStream.close();
		oStream.close();
		return filePath;
	}
	
	/**
	 * This function loads a .comp3111 file selected by the user and de-serializes the file into an EnvironmentParams object
	 * 
	 * @param selectedFile - selected file object to be loaded from
	 *  
	 * @return file path where the environment is loaded from
	 *  
	 * @throws Exception - throws exceptions when there is error while loading
	 * 
	 * @author kwaleung
	 */
	public String loadEnvironment(File selectedFile) throws Exception {
		
		FileInputStream iStream = null;
		ObjectInputStream objIStream = null;
		
		String filePath = selectedFile.getAbsolutePath();
		System.out.println("loadEnv: filePath = " + filePath);
		iStream = new FileInputStream(filePath);
		objIStream = new ObjectInputStream(iStream);
		envParams = (EnvironmentParams)objIStream.readObject();
		objIStream.close();
		iStream.close();
		return filePath;
	}
}
