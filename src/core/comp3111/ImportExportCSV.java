package core.comp3111;

import java.io.File;
import java.io.IOException;

import core.comp3111.*;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import org.apache.commons.csv.*;

public class ImportExportCSV {

	private FileChooser ImportChooser;
	private FileChooser ExportChooser;
	
	public ImportExportCSV() {
		ImportChooser = new FileChooser();
		ExportChooser = new FileChooser();
		
		ImportChooser.setTitle("Import CSV");
		ExtensionFilter CSVfilter = new ExtensionFilter("CSV Files", "*.csv");
		ImportChooser.getExtensionFilters().add(CSVfilter);
		ImportChooser.setSelectedExtensionFilter(CSVfilter);
		
		ExportChooser.setTitle("Export CSV");
		ExportChooser.getExtensionFilters().add(CSVfilter);
		ExportChooser.setSelectedExtensionFilter(CSVfilter);
	}
	
	public void importCSV() {
		System.out.println("importCSV: method start");
		File selectedFile = ImportChooser.showOpenDialog(null);
		
		if (selectedFile != null) {
			String filePath = selectedFile.getAbsolutePath();
			System.out.println("importCSV: Selecteded file from: " + filePath);
			/*TODO: Insert CSV parsing method here 
			 * 1. Create new DataTable
			 * 2. Create new DataColumns according to number of records
			 * 3. Insert DataColumns into DataTable
			*/
			
			
		}
		else {
			System.out.println("importCSV: No file selected");
		}
	}
	
	public void exportCSV() {
		System.out.println("exportCSV: method start");
		File selectedFile = ExportChooser.showSaveDialog(null);
		
		if (selectedFile != null) {
			String filePath = selectedFile.getAbsolutePath();
			System.out.println("exportCSV: Save file to: " + filePath);
			//TODO: Insert CSV writing method here
			
		}
		else {
			System.out.println("exportCSV: No file saved");
		}
	}
	
}
