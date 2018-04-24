package core.comp3111;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import org.apache.commons.csv.*;

/**
 * This class is responsible for handling the import 
 * and export of csv files. Only CSV files that follow the excel 
 * comma delimited format will be accepted.
 * 
 * @author kwaleung
 *
 */
public class ImportExportCSV {
	
	private static final String replaceWithZeros = "Replace with zeros";
	private static final String replaceWithMean = "Replace with column mean";
	private static final String replaceWithMedian = "Replace with column median";
	
	/**
	 * Default constructor of ImportExportCSV class.
	 */
	public ImportExportCSV() {

	}
	
	/**
	 * This functions reads a CSV file of the user's choice and
	 * creates a DataTable according to the content provided by the CSV
	 * 
	 * @param datasets
	 * 			-current collection of data tables
	 * @param name
	 * 			-name to be used by the imported table
	 *
	 * @return boolean
	 * 			-true if CSV imported
	 * 			-false otherwise
	 */
	public DataTable importCSV(Map<String, DataTable> datasets, File selectedFile) throws Exception {
		
		String filePath = selectedFile.getAbsolutePath();
		System.out.println("importCSV: Selected file from: " + filePath);

		FileReader fileReader = null;
		CSVParser csvParser = null;
		
		DataTable importedTable = new DataTable();
		fileReader = new FileReader(filePath);
		csvParser = new CSVParser(fileReader, CSVFormat.EXCEL);
		List<CSVRecord> csvContent = csvParser.getRecords();
		
		int rowCount = csvContent.size();
		int colCount = csvContent.get(0).size();
		if(rowCount != 0) {
			List<Object[]> newDataColumnElements = new ArrayList<Object[]>();
			List<String> newDataColumnNames = new ArrayList<String>();
			
			for (int i=0; i<colCount; i++) {
				Object[] newDataColumnElement = new Object[rowCount-1];
				newDataColumnElements.add(newDataColumnElement);
			}
			
			for (int i=0; i<rowCount; i++) {					
				
				for (int j=0; j<colCount; j++) {
					if (i==0) {
						//Set ColumnName
						newDataColumnNames.add(csvContent.get(i).get(j));
					} else {
						//Set ColumnValues
						Object[] currDataColumnElement = newDataColumnElements.get(j);
						currDataColumnElement[i-1] = csvContent.get(i).get(j);
					}
				}
			}
			fileReader.close();
			csvParser.close();
			for (int i=0; i<colCount; i++) {
				Object[] currColElements = newDataColumnElements.get(i);
				String currColTypeName = checkTypeName(currColElements);
				DataColumn newCol = new DataColumn(currColTypeName, currColElements);
				importedTable.addCol(newDataColumnNames.get(i), newCol);
			}
			return importedTable;
		} else {
			fileReader.close();
			csvParser.close();
			return null;
		}
	}
	
	/**
	 * This functions writes a data table chosen by the user to a CSV format file.
	 * 
	 * @param datasets
	 * 			-current collection of data tables
	 */
	public void exportCSV(File selectedFile, String selectedDataSetName, HashMap<String, DataTable> datasets) throws Exception {
		DataTable selectedDataTable = null;
		String filePath = selectedFile.getAbsolutePath();
		System.out.println("exportCSV: Save file to: " + filePath);
		
		FileWriter fileWriter = null;
		CSVPrinter csvFilePrinter = null;
		
		fileWriter = new FileWriter(filePath);
		csvFilePrinter = new CSVPrinter(fileWriter, CSVFormat.EXCEL);
		selectedDataTable = datasets.get(selectedDataSetName);
		String[] allColNames = selectedDataTable.getAllColName();
		Map<String, DataColumn> allCols = new HashMap<String, DataColumn>();
		
		for (int i=0; i<selectedDataTable.getNumCol(); i++) {
			allCols.put(allColNames[i], selectedDataTable.getCol(allColNames[i]));
		}
		
		csvFilePrinter.printRecord(allColNames); //Create Header
		
		for (int i=0; i<selectedDataTable.getNumRow(); i++) {
			List<Object> currRecord = new ArrayList<Object>();
			for (int j=0; j<selectedDataTable.getNumCol(); j++) {
				DataColumn currCol = allCols.get(allColNames[j]);
				Object currObj = currCol.getData()[i];
				currRecord.add(currObj);
			}
			csvFilePrinter.printRecord(currRecord);
		}
		fileWriter.flush();
		fileWriter.close();
		csvFilePrinter.close();
	}
	
	/**
	 * This functions checks the data type of a column
	 * 
	 * @param currCol
	 * 			-Column to be checked
	 * 
	 * @return String
	 * 			-Datatype of column
	 */
	public String checkTypeName(Object[] currCol) {
		int colSize = currCol.length;
		Boolean isNumber = false, isString = false;
		for (int i=0; i<colSize; i++) {
			System.out.println("checktype: " + currCol[i] + " getClass: " + currCol[i].getClass().toString());
			if (currCol[i].equals("")) {
				//Do nothing if empty record
			} else {
				try {
					Number number = NumberFormat.getInstance().parse((String) currCol[i]);
					currCol[i] = number;
				} catch (ParseException e) {
					System.out.println("importCSV: Cannot parse to Number");
					//e.printStackTrace();
				} finally {
					if (currCol[i] instanceof Number) {
						isNumber = true;
					} else if (currCol[i] instanceof String) {
						isString = true;
					}
				}
			}
		}
		
		if (isNumber && !isString) {
			return DataType.TYPE_NUMBER;
		} else if (!isNumber && isString) {
			return DataType.TYPE_STRING;
		} else {
			return DataType.TYPE_OBJECT;
		}
	}
	
	/**
	 * This functions checks whether a column has missing data to be handled.
	 * 
	 * @param currCol
	 * 			-Column to be checked
	 * 
	 * @return boolean
	 * 			-true if there is missing data, otherwise false
	 */
	public boolean checkMissingData(Object[] currCol) {
		for (int i=0; i<currCol.length; i++) {
			if (currCol[i].equals("")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This functions checks whether a column has missing data to be handled.
	 * 
	 * @param currCol
	 * 			-Column to be checked
	 * 
	 * @return boolean
	 * 			-true if there is missing data, otherwise false
	 */
	public Object replaceEmptyElement(Object currObj, String selectedReplaceOption, Number mean, Number median) {
		switch (selectedReplaceOption) {
		case replaceWithZeros: return 0;
		case replaceWithMean: return mean;
		case replaceWithMedian: return median;
		default: return 0;
		}
	}
	
	/**
	 * This function returns the mean of a number column.
	 * 
	 * @param currCol
	 * 			-Column data
	 * @return Number
	 * 			-mean of current column
	 */
	public Number calculateMean(Object[] currCol) {
		double sum = 0, mean = 0;
		double objCount = 0;
		for (int i=0; i<currCol.length; i++) {
			if (!currCol[i].equals("")) {
				objCount++;
				sum += (double)(((Long)currCol[i]).doubleValue());
			}
		}
		mean = sum/objCount;
		System.out.println("Mean: " + mean);
		return mean;
	}
	
	/**
	 * This function returns the median of a number column.
	 * 
	 * @param currCol
	 * 			-Column data
	 * @return Number
	 * 			-median of current column
	 */
	public Number calculateMedian(Object[] currCol) {
		double median = 0;
		List<Object> nonMissing = new ArrayList<Object>();
		
		for (int i=0; i<currCol.length; i++) {
			if (!currCol[i].equals("")) {
				nonMissing.add(currCol[i]);
			}
		}
		
	    int middle = nonMissing.size()/2;
	    if (nonMissing.size()%2 == 1) {
	    	median = (double)(((Long)nonMissing.get(middle)).doubleValue());
	    	System.out.println("Size of records: " + nonMissing.size());
	    } else {
	        median = ((double)(((((Long)nonMissing.get(middle-1)).doubleValue()) + (((Long)nonMissing.get(middle)).doubleValue())) / 2.0));
	    }
		
		System.out.println("Median: " + median);
		return median;
	}
}
