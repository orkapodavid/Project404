package testing.comp3111;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataTableException;
import core.comp3111.DataType;

/**
 * DataTable test using JUnit. Achieves 100% test coverage in the DataTable class.
 * 
 * @author kwaleung
 *
 */
class DataTableTest {
	
	/**
	 * Test case for default constructor of the DataTable class.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableConstructor() throws DataTableException {
		DataTable testDataTable = new DataTable();
		
		assert(testDataTable.getNumRow() == 0);
	}
	
	/**
	 * Test case for the addCol method in the DataTable class.
	 * This test case covers the case where DataColumns are inserted into a DataTable successfully.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableAddCol() throws DataTableException {
		DataTable testDataTable = new DataTable();
		DataColumn testDataColumn1 = new DataColumn();
		DataColumn testDataColumn2 = new DataColumn();
		String colName1 = "Test", colName2 = "Test2";
		Object[] testCol1 = {"a", "b", "c"};
		Object[] testCol2 = {"a", "b", "d"};
		testDataColumn1.set(DataType.TYPE_STRING, testCol1);
		testDataColumn2.set(DataType.TYPE_STRING, testCol2);
		testDataTable.addCol(colName1, testDataColumn1);
		testDataTable.addCol(colName2, testDataColumn2);
		
		assert(testDataTable.getNumCol() == 2);
	}
	
	/**
	 * Test case for the addCol method in the DataTable class.
	 * This test case covers the case where a DataColumn is inserted into a DataTable unsuccessfully due to having an identical name with an existing DataColumn in the DataTable.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableAddColSameNameException() throws DataTableException {
		DataTable testDataTable = new DataTable();
		DataColumn testDataColumn = new DataColumn();
		String colName = "Test";
		
		testDataTable.addCol(colName, testDataColumn);
		assertThrows(DataTableException.class, () -> testDataTable.addCol(colName, testDataColumn));
	}
	
	/**
	 * Test case for the addCol method in the DataTable class.
	 * This test case covers the case where a DataColumn is inserted into a DataTable unsuccessfully due to having a different row count when compared to existing DataColumns in the DataTable.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableAddColDiffRowCountException() throws DataTableException {
		DataTable testDataTable = new DataTable();
		DataColumn testDataColumn1 = new DataColumn();
		DataColumn testDataColumn2 = new DataColumn();
		String colName1 = "Test", colName2 = "Test2";
		Object[] testCol1 = {"a", "b", "c"};
		Object[] testCol2 = {"a", "b", "c", "d"};
		testDataColumn1.set(DataType.TYPE_STRING, testCol1);
		testDataColumn2.set(DataType.TYPE_STRING, testCol2);
		
		testDataTable.addCol(colName1, testDataColumn1);
		assertThrows(DataTableException.class, () -> testDataTable.addCol(colName2, testDataColumn2));
	}
	
	/**
	 * Test case for the removeCol method in the DataTable class.
	 * This test case covers the case where a DataColumn is successfully removed from a DataTable.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableRemoveCol() throws DataTableException {
		DataTable testDataTable = new DataTable();
		DataColumn testDataColumn1 = new DataColumn();
		String colName1 = "Test";
		Object[] testCol1 = {"a", "b", "c"};
		testDataColumn1.set(DataType.TYPE_STRING, testCol1);
		testDataTable.addCol(colName1, testDataColumn1);
		testDataTable.removeCol(colName1);
		
		assert(testDataTable.getNumRow() == 0);
	}
	
	/**
	 * Test case for the removeCol method in the DataTable class.
	 * This test case covers the case where a DataColumn is unsuccessfully removed from a DataTable as it does not exists in the DataTable.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableRemoveColNotExistException() throws DataTableException {
		DataTable testDataTable = new DataTable();
		String colName1 = "Test";
		
		assertThrows(DataTableException.class, () -> testDataTable.removeCol(colName1));
	}
	
	/**
	 * Test case for the getCol method in the DataTable class.
	 * This test case covers the case where a DataColumn is successfully retrieved from a DataTable.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableGetCol() throws DataTableException {
		DataTable testDataTable = new DataTable();
		DataColumn testDataColumn1 = new DataColumn();
		String colName1 = "Test";
		Object[] testCol1 = {"a", "b", "c"};
		testDataColumn1.set(DataType.TYPE_STRING, testCol1);
		testDataTable.addCol(colName1, testDataColumn1);
		
		assert(testDataTable.getCol(colName1).getData().equals(testCol1));
	}
	
	/**
	 * Test case for the getCol method in the DataTable class.
	 * This test case covers the case where a DataColumn is not retrieved from a DataTable as it does not exist.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableGetColNotFound() throws DataTableException {
		DataTable testDataTable = new DataTable();
		DataColumn testDataColumn1 = new DataColumn();
		String colName1 = "Test", colName2 = "Test2";
		Object[] testCol1 = {"a", "b", "c"};
		testDataColumn1.set(DataType.TYPE_STRING, testCol1);
		testDataTable.addCol(colName1, testDataColumn1);
		
		assert(testDataTable.getCol(colName2) == null);
	}
	
	/**
	 * Test case for the getAllNumColName method in the DataTable class.
	 * This test case covers the case where the names of all numeric columns in a DataTable is retrieved.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableGetAllNumColName() throws DataTableException {
		DataTable testDataTable = new DataTable();
		DataColumn testDataColumn1 = new DataColumn();
		DataColumn testDataColumn2 = new DataColumn();
		String colName1 = "Test", colName2 = "Test2";
		Object[] testCol1 = {"1", "2", "3"};
		Object[] testCol2 = {"a", "b", "d"};
		testDataColumn1.set(DataType.TYPE_NUMBER, testCol1);
		testDataColumn2.set(DataType.TYPE_STRING, testCol2);
		testDataTable.addCol(colName1, testDataColumn1);
		testDataTable.addCol(colName2, testDataColumn2);
		
		assert(testDataTable.getAllNumColName().length == 1);
	}
	
	/**
	 * Test case for the getAllNumColName method in the DataTable class.
	 * This test case covers the case where no names of any numeric columns in a DataTable is retrieved due to an empty DataTable.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableGetAllNumColNameEmptyTable() {
		DataTable testDataTable = new DataTable();
		
		assert(testDataTable.getAllNumColName() == null);
	}
	
	/**
	 * Test case for the getAllTextColName method in the DataTable class.
	 * This test case covers the case where the names of all string columns in a DataTable is retrieved.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableGetAllTextColName() throws DataTableException {
		DataTable testDataTable = new DataTable();
		DataColumn testDataColumn1 = new DataColumn();
		DataColumn testDataColumn2 = new DataColumn();
		String colName1 = "Test", colName2 = "Test2";
		Object[] testCol1 = {"1", "2", "3"};
		Object[] testCol2 = {"a", "b", "d"};
		testDataColumn1.set(DataType.TYPE_NUMBER, testCol1);
		testDataColumn2.set(DataType.TYPE_STRING, testCol2);
		testDataTable.addCol(colName1, testDataColumn1);
		testDataTable.addCol(colName2, testDataColumn2);
		
		assert(testDataTable.getAllTextColName().length == 1);
	}
	
	/**
	 * Test case for the getAllTextColName method in the DataTable class.
	 * This test case covers the case where no names of any string columns in a DataTable is retrieved due to an empty DataTable.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableGetAllTextColNameEmptyTable() {
		DataTable testDataTable = new DataTable();
		
		assert(testDataTable.getAllTextColName() == null);
	}
	
	/**
	 * Test case for the getNumOfNumCol method in the DataTable class.
	 * This test case covers the case where the number of numeric columns in a DataTable is retrieved.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableGetNumOfNumCol() throws DataTableException {
		DataTable testDataTable = new DataTable();
		DataColumn testDataColumn1 = new DataColumn();
		DataColumn testDataColumn2 = new DataColumn();
		String colName1 = "Test", colName2 = "Test2";
		Object[] testCol1 = {"1", "2", "3"};
		Object[] testCol2 = {"a", "b", "d"};
		testDataColumn1.set(DataType.TYPE_NUMBER, testCol1);
		testDataColumn2.set(DataType.TYPE_STRING, testCol2);
		testDataTable.addCol(colName1, testDataColumn1);
		testDataTable.addCol(colName2, testDataColumn2);
		
		assert(testDataTable.getNumOfNumCol() == 1);
	}
	
	/**
	 * Test case for the getNumOfNumCol method in the DataTable class.
	 * This test case covers the case where the number of numeric columns in an empty DataTable is retrieved.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableGetNumOfNumColEmptyTable() {
		DataTable testDataTable = new DataTable();
		
		assert(testDataTable.getNumOfNumCol() == 0);
	}
	
	/**
	 * Test case for the getNumOfTextCol method in the DataTable class.
	 * This test case covers the case where the number of string columns in a DataTable is retrieved.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableGetNumOfTextCol() throws DataTableException {
		DataTable testDataTable = new DataTable();
		DataColumn testDataColumn1 = new DataColumn();
		DataColumn testDataColumn2 = new DataColumn();
		String colName1 = "Test", colName2 = "Test2";
		Object[] testCol1 = {"1", "2", "3"};
		Object[] testCol2 = {"a", "b", "d"};
		testDataColumn1.set(DataType.TYPE_NUMBER, testCol1);
		testDataColumn2.set(DataType.TYPE_STRING, testCol2);
		testDataTable.addCol(colName1, testDataColumn1);
		testDataTable.addCol(colName2, testDataColumn2);
		
		assert(testDataTable.getNumOfTextCol() == 1);
	}
	
	/**
	 * Test case for the getNumOfTextCol method in the DataTable class.
	 * This test case covers the case where the number of string columns in an empty DataTable is retrieved.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableGetNumOfTextColEmptyTable() {
		DataTable testDataTable = new DataTable();
		
		assert(testDataTable.getNumOfTextCol() == 0);
	}
	
	/**
	 * Test case for the getAllColName method in the DataTable class.
	 * This test case covers the case where the names of all columns in a DataTable is retrieved.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testdataTableGetAllColName() throws DataTableException {
		DataTable testDataTable = new DataTable();
		DataColumn testDataColumn1 = new DataColumn();
		DataColumn testDataColumn2 = new DataColumn();
		String colName1 = "Test", colName2 = "Test2";
		Object[] testCol1 = {"1", "2", "3"};
		Object[] testCol2 = {"a", "b", "d"};
		testDataColumn1.set(DataType.TYPE_NUMBER, testCol1);
		testDataColumn2.set(DataType.TYPE_STRING, testCol2);
		testDataTable.addCol(colName1, testDataColumn1);
		testDataTable.addCol(colName2, testDataColumn2);
		
		assert(testDataTable.getAllColName().length == 2);
	}
	
	/**
	 * Test case for the getAllColName method in the DataTable class.
	 * This test case covers the case where no names of any columns in a DataTable is retrieved due to an empty DataTable.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testDataTableGetAllColNameEmptyTable() {
		DataTable testDataTable = new DataTable();
		
		assert(testDataTable.getAllColName() == null);
	}
	
	/**
	 * Test case for the print method in the DataTable class.
	 * This test case simply prints out a DataTable.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testPrint() throws DataTableException {
		DataTable testDataTable = new DataTable();
		DataColumn testDataColumn1 = new DataColumn();
		DataColumn testDataColumn2 = new DataColumn();
		String colName1 = "Test", colName2 = "Test2";
		Object[] testCol1 = {"1", "2", "3"};
		Object[] testCol2 = {"a", "b", "d"};
		testDataColumn1.set(DataType.TYPE_NUMBER, testCol1);
		testDataColumn2.set(DataType.TYPE_STRING, testCol2);
		testDataTable.addCol(colName1, testDataColumn1);
		testDataTable.addCol(colName2, testDataColumn2);
		
		testDataTable.print();
		
		assert(testDataTable.getNumCol() == 2);
	}
}
