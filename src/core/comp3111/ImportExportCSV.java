package core.comp3111;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import core.comp3111.*;
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

	private FileChooser ImportChooser;
	private FileChooser ExportChooser;
	private Alert noFileChosen, emptyTable, tableAdded, wrongFileType, noFileSaved;
	
	private static final String replaceWithZeros = "Replace with zeros";
	private static final String replaceWithMean = "Replace with column mean";
	private static final String replaceWithMedian = "Replace with column median";
	
	public ImportExportCSV() {
		ImportChooser = new FileChooser();
		ExportChooser = new FileChooser();
		noFileChosen = new Alert(AlertType.ERROR);
		emptyTable = new Alert(AlertType.ERROR);
		tableAdded = new Alert(AlertType.INFORMATION);
		wrongFileType = new Alert(AlertType.ERROR);
		noFileSaved = new Alert(AlertType.ERROR);
		
		ImportChooser.setTitle("Import CSV");
		ExtensionFilter CSVfilter = new ExtensionFilter("CSV Files", "*.csv");
		ImportChooser.getExtensionFilters().add(CSVfilter);
		ImportChooser.setSelectedExtensionFilter(CSVfilter);
		
		ExportChooser.setTitle("Export CSV");
		ExportChooser.getExtensionFilters().add(CSVfilter);
		ExportChooser.setSelectedExtensionFilter(CSVfilter);
		
		noFileChosen.setTitle("Error");
		noFileChosen.setHeaderText("No file selected");
		noFileChosen.setContentText("Operation cancelled");
		
		emptyTable.setTitle("Error");
		emptyTable.setHeaderText("Selected CSV File is empty");
		emptyTable.setContentText("Operation cancelled");
		
		tableAdded.setTitle("Success");
		tableAdded.setHeaderText("File Imported");
		tableAdded.setContentText("CSV file successfully imported into data sets");
		
		wrongFileType.setTitle("ERROR");
		wrongFileType.setHeaderText("Invalid file type");
		wrongFileType.setContentText("Operation cancelled");
		
		noFileSaved.setTitle("ERROR");
		noFileSaved.setHeaderText("File not saved");
		noFileSaved.setContentText("Operation cancelled");
	}
	
	/**
	 * This functions reads a CSV file of the user's choice and
	 * creates a DataTable according to the content provided by the CSV
	 * 
	 * @param datasets
	 * 			-current collection of data tables
	 * @throws Exception
	 *          -Throws exception if FileReader cannot read file or CSVParser cannot parse the selected CSV file
	 *          
	 * @throws IOException
	 * 			-Throws IOException if FileReader or CSVParser cannot be closed properly
	 *
	 */
	public boolean importCSV(Map<String, DataTable>  datasets, int DataSetCount) {
		System.out.println("importCSV: method start");

		File selectedFile = ImportChooser.showOpenDialog(null);
		String name = "DataSet" + DataSetCount;
		
		if (selectedFile == null) {
			System.out.println("importCSV: No file selected");
			noFileChosen.showAndWait();
		} else {
			String filePath = selectedFile.getAbsolutePath();
			System.out.println("importCSV: Selecteded file from: " + filePath);

			FileReader fileReader = null;
			CSVParser csvParser = null;
			
			try {
				DataTable importedTable = new DataTable();
				fileReader = new FileReader(filePath);
				csvParser = new CSVParser(fileReader, CSVFormat.EXCEL);
				List<CSVRecord> csvContent = csvParser.getRecords();
				
				int rowCount = csvContent.size();
				int colCount = 0;
				if(rowCount != 0)
					 colCount = csvContent.get(0).size(); // No. of Columns
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
				
				for (int i=0; i<colCount; i++) {
					Object[] currColElements = newDataColumnElements.get(i);
					String currColTypeName = checkTypeName(currColElements);
					boolean isMissing = checkMissingData(currColElements);
					if (isMissing) {
						currColElements = replaceEmptyElements(currColElements, currColTypeName, newDataColumnNames.get(i));
					}
					DataColumn newCol = new DataColumn(currColTypeName, currColElements);
					importedTable.addCol(newDataColumnNames.get(i), newCol);
				}
				
				if (importedTable.getNumCol() == 0) { //empty table
					System.out.println("importCSV: Empty table not added");
					emptyTable.showAndWait();
				} else {
					datasets.put(name,importedTable);
					System.out.println("importCSV: Imported table added");
					tableAdded.showAndWait();
					return true;
				}
				
			} catch (Exception e) {
				System.out.println("importCSV: Error in CSVFileReader");
				e.printStackTrace();
			} finally {
				try {
					fileReader.close();
					csvParser.close();
				} catch (IOException e) {
					System.out.println("importCSV: Error while closing fileReader/csvFileParser.");
				}
			}
			
		}
		return false;
	}
	
	/**
	 * This functions writes a data table chosen by the user to a CSV format file.
	 * 
	 * @param datasets
	 * 			-current collection of data tables
	 */
	public void exportCSV(Map<String, DataTable>  datasets) {
		System.out.println("exportCSV: method start");
		//TODO: Let user choose with data table to export
		
		File selectedFile = ExportChooser.showSaveDialog(null);
		
		if (selectedFile != null) {
			String filePath = selectedFile.getAbsolutePath();
			System.out.println("exportCSV: Save file to: " + filePath);
			
			FileWriter fileWriter = null;
			CSVPrinter csvFilePrinter = null;
			
			try {
				fileWriter = new FileWriter(filePath);
				csvFilePrinter = new CSVPrinter(fileWriter, CSVFormat.EXCEL);
				//TODO: Get specific table from data sets
				datasets.get("DataSet0"); //hardcode testing
				
				
				
			} catch (Exception e) {
				
			}
			
		}
		else {
			System.out.println("exportCSV: No file saved");
			noFileSaved.showAndWait();
		}
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
	private String checkTypeName(Object[] currCol) {
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
	private boolean checkMissingData(Object[] currCol) {
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
	private Object[] replaceEmptyElements(Object[] currCol, String currColDataType, String currColName) {
		Alert replacedAlert = new Alert(AlertType.INFORMATION);
		replacedAlert.setTitle("Information");
		replacedAlert.setHeaderText("Replaced missing data");
		
		if(currColDataType.equals(DataType.TYPE_NUMBER)) {
			Number mean = calculateMean(currCol);
			Number median = calculateMedian(currCol);
			
			Collection<String> replaceOptions = new ArrayList<String>();
			replaceOptions.add(replaceWithZeros);
			replaceOptions.add(replaceWithMean);
			replaceOptions.add(replaceWithMedian);
			ChoiceDialog<String> chooseReplaceOption = new ChoiceDialog<String>(replaceWithZeros, replaceOptions);
			chooseReplaceOption.setTitle("Replace Option");
			chooseReplaceOption.setHeaderText("Please choose");
			chooseReplaceOption.setContentText("Replace missing data in column: " + currColName);
			chooseReplaceOption.getDialogPane().lookupButton(ButtonType.CANCEL).setDisable(true);
			Optional<String> returnedReplaceOption = chooseReplaceOption.showAndWait();
			String selectedReplaceOption = null;
			if (returnedReplaceOption.isPresent()) {
				selectedReplaceOption = returnedReplaceOption.get();
			} else {
				selectedReplaceOption = replaceWithZeros;
			}
			for (int i=0; i<currCol.length; i++) {
				if (currCol[i].equals("")) {
					switch (selectedReplaceOption) {
					case replaceWithZeros: currCol[i] = 0;
					break;
					case replaceWithMean: currCol[i] = mean;
					break;
					case replaceWithMedian: currCol[i] = median;
					break;
					default: currCol[i] = 0;
					break;
					}
				}
			}
			switch (selectedReplaceOption) {
			case replaceWithZeros: replacedAlert.setContentText("Replaced missing data with zeros in column: " + currColName);
			break;
			case replaceWithMean: replacedAlert.setContentText("Replaced missing data with mean value in column: " + currColName);
			break;
			case replaceWithMedian: replacedAlert.setContentText("Replaced missing data with median value in column: " + currColName);
			break;
			default: replacedAlert.setContentText("Replaced missing data with zeros in column: " + currColName);
			break;
			}
			replacedAlert.showAndWait();
			return currCol;
		}
		replacedAlert.setContentText("Replaced missing data with empty string in column: " + currColName);
		replacedAlert.showAndWait();
		return currCol;
	}
	
	private Number calculateMean(Object[] currCol) {
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
	
	private Number calculateMedian(Object[] currCol) {
		double median = 0;
	    int middle = currCol.length/2;
	    if (currCol.length%2 == 1) {
	    	median = (double)(((Long)currCol[middle]).doubleValue());
	    } else {
	        median = ((double)(((Long)currCol[middle-1]).doubleValue()) + (double)(((Long)currCol[middle]).doubleValue()) / 2.0);
	    }
		
		System.out.println("Median: " + median);
		return median;
	}
}
