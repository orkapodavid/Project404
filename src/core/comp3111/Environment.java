package core.comp3111;

import java.util.HashMap;

public class Environment {

	public Environment() {
		
	}
	
	public void setEnvironmentDataTables(HashMap<String, DataTable> newDataTables) {
		DataTables = newDataTables;
	}
	
	public void setEnviornmentCharts() {
		
	}
	
	public HashMap<String, DataTable> getEnvironmentDataTables() {
		return DataTables;
	}
	
	public void getEnviornmentCharts() {
		
	}
	
	public void saveEnvironment() {
		
	}
	
	public void loadEnvironment() {
		
	}
	
	private HashMap<String, DataTable> DataTables;
	//TODO: Add chart map
}
