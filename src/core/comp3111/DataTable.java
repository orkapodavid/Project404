package core.comp3111;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 2D array of data values with the following requirements: <p>(1) There are 0 to
 * many columns <p>(2) The number of row for each column is the same <p>(3) 2 columns
 * may have different type (e.g. String and Number). <p>(4) A column can be
 * uniquely identified by its column name <p>(5) add/remove a column is supported
 * <p>(6) Suitable exception handling is implemented
 * 
 * @author cspeter
 * @author kpor
 *
 */
public class DataTable implements Serializable {

	/**
	 * Construct  Create an empty DataTable
	 */
	public DataTable() {

		// In this application, we use HashMap data structure defined in
		// java.util.HashMap
		dc = new HashMap<String, DataColumn>();
	}

	/**
	 * Add a data column to the table.
	 * 
	 * @param colName
	 *            name of the column. It should be a unique identifier
	 * @param newCol
	 *            the data column
	 * @throws DataTableException
	 *             It throws DataTableException if a column is already exist, or
	 *             the row size does not match.
	 */
	public void addCol(String colName, DataColumn newCol) throws DataTableException {
		if (containsColumn(colName)) {
			throw new DataTableException("addCol: The column already exists");
		}

		int curNumCol = getNumCol();
		if (curNumCol == 0) {
			dc.put(colName, newCol); // add the column
			return; // exit the method
		}

		// If there is more than one column,
		// we need to ensure that all columns having the same size

		int curNumRow = getNumRow();
		if (newCol.getSize() != curNumRow) {
			throw new DataTableException(String.format(
					"addCol: The row size does not match: newCol(%d) and curNumRow(%d)", newCol.getSize(), curNumRow));
		}

		dc.put(colName, newCol); // add the mapping
	}

	/**
	 * Remove a column from the data table
	 * 
	 * @param colName
	 *            The column name. It should be a unique identifier
	 * @throws DataTableException
	 *            It throws DataTableException if the column does not exist
	 */
	public void removeCol(String colName) throws DataTableException {
		if (containsColumn(colName)) {
			dc.remove(colName);
			return;
		}
		throw new DataTableException("removeCol: The column does not exist");
	}

	/**
	 * Get the DataColumn object based on the give colName. 
	 * <p>Return null if the column does not exist
	 * 
	 * @param colName
	 *            The column name
	 * @return DataColumn reference or null
	 */
	public DataColumn getCol(String colName) {
		if (containsColumn(colName)) {
			return dc.get(colName);
		}
		return null;
	}
	/**
	 * Get a String array of the names of all numerical columns
	 * 
	 * @return String[] or null
	 * @author kpor
	 */
	public String[] getAllNumColName() {
		
		if(dc.size() == 0) {
			return null;
		}
		
		ArrayList<String> colsName = new ArrayList<String>(dc.size());
		Set<String> colsSet =  dc.keySet();
		for(String col:colsSet) {
			if(dc.get(col).getTypeName().equals(DataType.TYPE_NUMBER)) {
				colsName.add(col);
			}
		}
		
		return colsName.toArray(new String[0]);
	}
	
	/**
	 * Get a String array of the names of all text columns
	 * 
	 * @return String[] or null
	 * @author kpor
	 */
	public String[] getAllTextColName() {
		
		if(dc.size() == 0) {
			return null;
		}
		
		ArrayList<String> colsName = new ArrayList<String>(dc.size());
		Set<String> colsSet =  dc.keySet();
		for(String col:colsSet) {
			if(dc.get(col).getTypeName().equals(DataType.TYPE_STRING)) {
				colsName.add(col);
			}
		}
		
		return colsName.toArray(new String[0]);
	}
	
	/**
	 * Get a String array of the names of all columns
	 * 
	 * @return String[] or null
	 * @author kpor
	 */
	public String[] getAllColName() {
		
		if(dc.size() == 0) {
			return null;
		}
		
		ArrayList<String> colsName = new ArrayList<String>(dc.size());
		Set<String> colsSet =  dc.keySet();
		for(String col:colsSet) {
			colsName.add(col);
		}
		
		return colsName.toArray(new String[0]);
	}

	/**
	 * Check whether the column exists by the given column name
	 * 
	 * @param colName  selected column name
	 * @return true if the column exists, false otherwise
	 */
	public boolean containsColumn(String colName) {
		return dc.containsKey(colName);
	}
	
	/**
	 * Get the number of all numerical columns
	 * 
	 * @return int or 0
	 * @author kpor
	 */
	public int getNumOfNumCol() {
		
		if(dc.size() == 0) {
			return 0;
		}
		
		int num = 0;
		Set<String> colsSet =  dc.keySet();
		for(String col:colsSet) {
			if(dc.get(col).getTypeName().equals(DataType.TYPE_NUMBER)) {
				num += 1;
			}
		}
		
		return num;
	}
	
	/**
	 * Get the number of all text columns
	 * 
	 * @return int or 0
	 * @author kpor
	 */
	public int getNumOfTextCol() {
		
		if(dc.size() == 0) {
			return 0;
		}
		
		int num = 0;
		Set<String> colsSet =  dc.keySet();
		for(String col:colsSet) {
			if(dc.get(col).getTypeName().equals(DataType.TYPE_STRING)) {
				num += 1;
			}
		}
		
		return num;
	}

	/**
	 * Return the number of column in the data table
	 * 
	 * @return the number of column in the data table
	 */
	public int getNumCol() {
		return dc.size();
	}

	/**
	 * Return the number of row of the data table. This data structure ensures that
	 * all columns having the same number of row
	 * 
	 * @return the number of row of the data table
	 */
	public int getNumRow() {
		if (dc.size() <= 0)
			return dc.size();

		// Pick the first entry and get its size
		// assumption: For DataTable, all columns should have the same size
		Map.Entry<String, DataColumn> entry = dc.entrySet().iterator().next();
		return dc.get(entry.getKey()).getSize();
	}
	
	/**
	 * A function used to print the DataTable in Console for debugging.
	 */
	public void print() {
		int colcount = this.getNumCol();
		int rowcount = this.getNumRow();
		System.out.println("colcount: " + colcount + " rowcount: " + rowcount);
		
		List<DataColumn> Columns = new ArrayList<DataColumn>();
		for (String key: dc.keySet()) {
			System.out.print(key + "\t");
			Columns.add(dc.get(key));
		}
		System.out.println();
		for (int i=0; i<rowcount; i++) {
			for (int j=0; j<colcount; j++) {
				System.out.print(Columns.get(j).getData()[i] + "\t");
			}
			System.out.println();
		}
		for (int i=0; i<colcount; i++) {
			System.out.print(Columns.get(i).getTypeName() + "\t");
		}
		System.out.println();
	}

	// attribute: A java.util.Map interface
	// KeyType: String
	// ValueType: DataColumn
	private Map<String, DataColumn> dc;

}
