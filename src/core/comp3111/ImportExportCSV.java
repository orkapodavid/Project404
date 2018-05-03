package core.comp3111;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	 * 
	 * @author kwaleung
	 */
	public ImportExportCSV() {

	}
	
	/**
	 * This functions reads a CSV file of the user's choice and
	 * creates a DataTable according to the content provided by the CSV
	 * 
	 * @param selectedFile
	 * 			 selected file object to be imported from
	 *
	 * @return 
	 * 			 true if CSV imported, otherwise false
	 * 
	 * @throws Exception
	 * 			 throws exception when there is error while importing CSV
	 * 
	 * @author kwaleung
	 */
	public DataTable importCSV(File selectedFile) throws Exception {
		
		String filePath = selectedFile.getAbsolutePath();
		//System.out.println("importCSV: Selected file from: " + filePath);

		FileReader fileReader = null;
		CSVParser csvParser = null;
		
		DataTable importedTable = new DataTable();
		fileReader = new FileReader(filePath);
		csvParser = new CSVParser(fileReader, CSVFormat.EXCEL);
		List<CSVRecord> csvContent = csvParser.getRecords();
		
		int rowCount = csvContent.size();
		if(rowCount != 0) {
			int colCount = csvContent.get(0).size();
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
<<<<<<< HEAD
=======
					}
				}
			}
			fileReader.close();
			csvParser.close();
			for (int i=0; i<colCount; i++) {
				Object[] currColElements = newDataColumnElements.get(i);
				String currColTypeName = checkTypeName(currColElements);
				if (currColTypeName.equals(DataType.TYPE_NUMBER)) {
					for (int j=0; j<currColElements.length; j++) {
						if (!currColElements[j].equals(""))
							currColElements[j] = NumberFormat.getInstance().parse(currColElements[j].toString());
					}
				} else if (currColTypeName.equals(DataType.TYPE_STRING)) {
					for (int j=0; j<currColElements.length; j++) {
						currColElements[j] = currColElements[j].toString();
>>>>>>> origin/master
					}
				}
				DataColumn newCol = new DataColumn(currColTypeName, currColElements);
				importedTable.addCol(newDataColumnNames.get(i), newCol);
			}
<<<<<<< HEAD
			fileReader.close();
			csvParser.close();
			for (int i=0; i<colCount; i++) {
				Object[] currColElements = newDataColumnElements.get(i);
				String currColTypeName = checkTypeName(currColElements);
				DataColumn newCol = new DataColumn(currColTypeName, currColElements);
				importedTable.addCol(newDataColumnNames.get(i), newCol);
			}
=======
>>>>>>> origin/master
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
	 * @param selectedFile
	 * 			 the selected file object for exporting to
	 * 
	 * @param selectedDataSetName
	 * 			 Name of data table to be exported
	 * 
	 * @param datasets
	 * 			 current collection of data tables
	 * 
	 * @throws Exception
	 * 			 throws exception when there is error while exporting CSV
	 * 
	 * @author kwaleung
	 */
	public void exportCSV(File selectedFile, String selectedDataSetName, HashMap<String, DataTable> datasets) throws Exception {
		DataTable selectedDataTable = null;
		String filePath = selectedFile.getAbsolutePath();
		//System.out.println("exportCSV: Save file to: " + filePath);
		
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
		
		csvFilePrinter.printRecord((Object[])allColNames); //Create Header
		
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
	 * 			 Column to be checked
	 * 
	 * @return String
	 * 			 Datatype of column
	 * 
	 * @author kwaleung
	 */
	public String checkTypeName(Object[] currCol) {
		int colSize = currCol.length;
		Boolean isNumber = false, isString = false;
		for (int i=0; i<colSize; i++) {
			//System.out.println("checktype: " + currCol[i] + " getClass: " + currCol[i].getClass().toString());
			if (currCol[i].equals("")) {
				//Do nothing if empty record
			} else {
				try {
					Number number = NumberFormat.getInstance().parse((String) currCol[i]);
					currCol[i] = number;
				} catch (Exception e) {
					//System.out.println("importCSV: Cannot parse to Number");
					//e.printStackTrace();
				}
				if (currCol[i] instanceof Number) {
					isNumber = true;
				} else if (currCol[i] instanceof String) {
					isString = true;
				}
			}
		}
		
		if (isNumber && !isString) {
			return DataType.TYPE_NUMBER;
		} else {
			return DataType.TYPE_STRING;
		}
	}
	
	/**
	 * This functions checks whether a column has missing data to be handled.
	 * 
	 * @param currCol
	 * 			 Column to be checked
	 * 
	 * @return 
	 * 			 true if there is missing data, otherwise false
	 * 
	 * @author kwaleung
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
	 * @param selectedReplaceOption
	 * 			 Column to be checked
	 * 
	 * @param mean
	 * 			 calculated mean value of column
	 * 
	 * @param median
	 * 			 calculated median value of column
	 * 
	 * @return boolean
	 * 			 true if there is missing data, otherwise false
	 * 
	 * @author kwaleung
	 */
	public Object replaceEmptyElement(String selectedReplaceOption, Number mean, Number median) {
		if (selectedReplaceOption != null) {
			if (selectedReplaceOption.equals(replaceWithZeros))
				return 0;
			else if (selectedReplaceOption.equals(replaceWithMean))
				return mean;
			else if (selectedReplaceOption.equals(replaceWithMedian))
				return median;
			else
			    return 0;
		}
		return 0;
	}
	
	/**
	 * This function returns the mean of a number column.
	 * 
	 * @param currCol
	 * 			 Column data
	 * @return Number
	 * 			 mean of current column
	 * 
	 * @author kwaleung
	 */
	public Double calculateMean(Object[] currCol) {
		Double sum = (double) 0, mean = (double) 0;
		Double objCount = (double) 0;
		for (int i=0; i<currCol.length; i++) {
			if (!currCol[i].equals("")) {
				objCount++;
				sum += (Double)(((Number) currCol[i]).doubleValue());
			}
		}
		mean = sum/objCount;
		//System.out.println("Mean: " + mean);
		return mean;
	}
	
	/**
	 * This function returns the median of a number column.
	 * 
	 * @param currCol
	 * 			 Column data
	 * @return Number
	 * 			 median of current column
	 * 
	 * @author kwaleung
	 */
	public Double calculateMedian(Object[] currCol) {
		Double median = (double) 0;
		List<Number> nonMissing = new ArrayList<Number>();
		
		for (int i=0; i<currCol.length; i++) {
			if (!currCol[i].equals("")) {
				nonMissing.add((Number)currCol[i]);
			}
		}
		nonMissing.sort(null);
	    int middle = nonMissing.size()/2;
	    if (nonMissing.size()%2 == 1) {
	    	median = (Double)(((Number)nonMissing.get(middle)).doubleValue());
	    	//System.out.println("Size of records: " + nonMissing.size());
	    } else {
	        median = ((Double)(((((Number)nonMissing.get(middle-1)).doubleValue()) + (((Number)nonMissing.get(middle)).doubleValue())) / 2.0));
	    }
		
		//System.out.println("Median: " + median);
		return median;
	}
}
