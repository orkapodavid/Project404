package core.comp3111;

import ui.comp3111.*;

import java.util.HashMap;
import java.util.Map;
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
 *
 */
public class Environment{

	private EnvironmentParams envParams;
	
	private FileChooser SaveChooser;
	private FileChooser LoadChooser;
	private Alert saved, loaded, noFileSelected; 
	
	/**
	 * Default constructor of Environment class.
	 */
	public Environment() {		
		envParams = new EnvironmentParams();
		
		SaveChooser = new FileChooser();
		LoadChooser = new FileChooser();
		saved = new Alert(AlertType.INFORMATION);
		loaded = new Alert(AlertType.INFORMATION);
		noFileSelected = new Alert(AlertType.ERROR);
		
		LoadChooser.setTitle("Load Environment");
		ExtensionFilter SaveLoadFilter = new ExtensionFilter("comp3111 files", "*.comp3111");
		LoadChooser.getExtensionFilters().add(SaveLoadFilter);
		LoadChooser.setSelectedExtensionFilter(SaveLoadFilter);
		
		SaveChooser.setTitle("Save Environment");
		SaveChooser.getExtensionFilters().add(SaveLoadFilter);
		SaveChooser.setSelectedExtensionFilter(SaveLoadFilter);
		
		saved.setTitle("Saved");
		saved.setHeaderText("Success");
		
		loaded.setTitle("Loaded");
		loaded.setHeaderText("Success");
		
		noFileSelected.setTitle("ERROR");
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
	 * Returns the HashMap of data tables in the environment
	 * @return HashMap<String, DataTable>
	 */
	public HashMap<String, DataTable> getEnvironmentDataTables() {
		return envParams.getEnvironmentDataTables();
	}
	
	/**
	 * Returns the HashMap of line charts in the environment
	 * @return Map<String, LineChartClass>
	 */
	public Map<String, LineChartClass> getEnviornmentLineCharts() {
		return envParams.getEnviornmentLineCharts();
	}
	
	/**
	 * Returns the HashMap of pie charts in the environment
	 * @return Map<String, PieChartClass>
	 */
	public Map<String, PieChartClass> getEnviornmentPieCharts() {
		return envParams.getEnviornmentPieCharts();
	}
	
	/**
	 * This function saves the Environment Parameters to as a .comp3111 file in a user designated directory
	 *  by serializing an EnvironmentParams object.
	 */
	public void saveEnvironment() {
		SaveChooser.setInitialFileName("envparams.comp3111");
		File selectedFile = SaveChooser.showSaveDialog(null);
		 
		FileOutputStream oStream = null;
		ObjectOutputStream objOStream = null;
		
		if (selectedFile != null) {
			String filePath = selectedFile.getAbsolutePath();
			try {
				oStream = new FileOutputStream(filePath);
				objOStream = new ObjectOutputStream(oStream);
				objOStream.writeObject(envParams);
				
			} catch (FileNotFoundException e) {
				System.out.println("saveEnv: FileNotFoundException");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("saveEnv: IOexception");
				e.printStackTrace();
			} finally {
				try {
					objOStream.close();
					oStream.close();
				} catch (IOException e) {
					System.out.println("saveEnv: Error when closing ostream/objostream");
				}
			}
			saved.setContentText("Environment has been saved to: " + filePath);
			saved.showAndWait();
		} else {
			System.out.println("saveEnv: No file saved.");
			noFileSelected.setHeaderText("File not saved");
			noFileSelected.setContentText("Operation cancelled as no directory has been specified.");
			noFileSelected.showAndWait();
		}
	}
	
	/**
	 * This function loads a .comp3111 file selected by the user and de-serializes the file into an EnvironmentParams object
	 */
	public void loadEnvironment(ListView<String> dataList, ListView<String> chartList) {
		File selectedFile = LoadChooser.showOpenDialog(null);
		
		FileInputStream iStream = null;
		ObjectInputStream objIStream = null;
		
		if (selectedFile != null) {
			String filePath = selectedFile.getAbsolutePath();
			try {
				iStream = new FileInputStream(filePath);
				objIStream = new ObjectInputStream(iStream);
				envParams = (EnvironmentParams)objIStream.readObject();
				
			} catch (FileNotFoundException e) {
				System.out.println("loadEnv: FileNotFoundException");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("loadEnv: IOexception");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("loadEnv: ClassNotFoundException");
				e.printStackTrace();
			} finally {
				try {
					objIStream.close();
					iStream.close();
				} catch (IOException e) {
					System.out.println("loadEnv: Error when closing istream/objistream");
				}
			}
			dataList.getItems().removeAll();
			chartList.getItems().removeAll();
			for (String datakey:this.getEnvironmentDataTables().keySet()) {
				dataList.getItems().add(datakey);
			}
			for (String chartkey:this.getEnviornmentLineCharts().keySet()) {
				chartList.getItems().add(chartkey);
			}
			for (String chartkey:this.getEnviornmentPieCharts().keySet()) {
				chartList.getItems().add(chartkey);
			}

			loaded.setContentText("Environment has been loaded from: " + filePath);
			loaded.showAndWait();
		} else {
			System.out.println("loadEnv: No file loaded.");
			noFileSelected.setHeaderText("File not selected");
			noFileSelected.setContentText("Operation cancelled as no file selected.");
			noFileSelected.showAndWait();
		}
	}
}
