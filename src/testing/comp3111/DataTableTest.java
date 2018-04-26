package testing.comp3111;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataTableException;

class DataTableTest {
	
	@Test
	void testDataTableConstructor() {
		DataTable testDataTable = new DataTable();
		assert(testDataTable.getNumRow() == 0);
	}
	
	@Test
	void testDataTableAddCol() throws DataTableException {
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
	
	
}
