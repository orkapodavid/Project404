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
	 */
	public String loadEnvironment(File selectedFile) throws Exception {
		
		FileInputStream iStream = null;
		ObjectInputStream objIStream = null;
		
		String filePath = selectedFile.getAbsolutePath();
		iStream = new FileInputStream(filePath);
		objIStream = new ObjectInputStream(iStream);
		envParams = (EnvironmentParams)objIStream.readObject();
		objIStream.close();
		iStream.close();
		return filePath;
	}
}
