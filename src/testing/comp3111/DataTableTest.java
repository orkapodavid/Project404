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
	
	@Test
	void testDataTableConstructor() throws DataTableException {
		DataTable testDataTable = new DataTable();
		
		assert(testDataTable.getNumRow() == 0);
	}
	
	@Test
	void testDataTableAddColNormal() throws DataTableException {
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
	
	@Test
	void testDataTableAddColEmptyTable() throws DataTableException {
		DataTable testDataTable = new DataTable();
		DataColumn testDataColumn = new DataColumn();
		String colName = "Test";
		
		testDataTable.addCol(colName, testDataColumn);
		assert(testDataTable.getNumCol() == 1);
	}
	
	@Test
	void testDataTableAddColSameNameException() throws DataTableException {
		DataTable testDataTable = new DataTable();
		DataColumn testDataColumn = new DataColumn();
		String colName = "Test";
		
		testDataTable.addCol(colName, testDataColumn);
		assertThrows(DataTableException.class, () -> testDataTable.addCol(colName, testDataColumn));
	}
	
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
	
	@Test
	void testDataTableRemoveColNotExistException() throws DataTableException {
		DataTable testDataTable = new DataTable();
		String colName1 = "Test";
		
		assertThrows(DataTableException.class, () -> testDataTable.removeCol(colName1));
	}
	
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
	
	@Test
	void testDataTableGetAllNumColNameEmptyTable() {
		DataTable testDataTable = new DataTable();
		
		assert(testDataTable.getAllNumColName() == null);
	}
	
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
	
	@Test
	void testDataTableGetAllTextColNameEmptyTable() {
		DataTable testDataTable = new DataTable();
		
		assert(testDataTable.getAllTextColName() == null);
	}
	
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
	
	@Test
	void testDataTableGetNumOfNumColEmptyTable() {
		DataTable testDataTable = new DataTable();
		
		assert(testDataTable.getNumOfNumCol() == 0);
	}
	
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
	
	@Test
	void testDataTableGetNumOfTextColEmptyTable() {
		DataTable testDataTable = new DataTable();
		
		assert(testDataTable.getNumOfTextCol() == 0);
	}
	
	@Test
	void testDAtaTableGetAllColName() throws DataTableException {
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
	
	@Test
	void testDataTableGetAllColNameEmptyTable() {
		DataTable testDataTable = new DataTable();
		
		assert(testDataTable.getAllColName() == null);
	}
	
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
