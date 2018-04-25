package testing.comp3111;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataType;
import core.comp3111.ImportExportCSV;


class ImportExportTest {

	@Test
	void testImport() throws Exception {
		ImportExportCSV testImportExport = new ImportExportCSV();
		String fileDir = System.getProperty("user.dir") + "/TestingCSV/Test.csv";		
		File testImportFile = new File(fileDir);
		
		DataTable testImportTable = testImportExport.importCSV(testImportFile);
		assert(testImportTable.getNumCol() == 7);
	}
	
	@Test
	void testImportEmpty() throws Exception {
		ImportExportCSV testImportExport = new ImportExportCSV();
		String fileDir = System.getProperty("user.dir") + "/TestingCSV/TestEmpty.csv";		
		File testImportFile = new File(fileDir);
		
		DataTable testImportTable = testImportExport.importCSV(testImportFile);
		assert(testImportTable == null);
	}
	
	@Test
	void testExport() throws Exception {
		ImportExportCSV testImportExport = new ImportExportCSV();
		String fileDir = System.getProperty("user.dir") + "/TestingCSV/testExport.csv";
		File testExportFile = new File(fileDir);
		
		DataTable testTable = new DataTable();
		String testTableName = "TestTable";
		DataColumn testCol = new DataColumn();
		String colName = "TestCol";
		Object[] testArr = {1, 2, 3};
		testCol.set(DataType.TYPE_NUMBER, testArr);
		testTable.addCol(colName, testCol);
		HashMap<String, DataTable> testTables = new HashMap<String, DataTable>();
		testTables.put(testTableName, testTable);
		testImportExport.exportCSV(testExportFile, testTableName, testTables);
		
		assert(testExportFile.exists() == true);
		Files.deleteIfExists(testExportFile.toPath());
	}
	
	@Test
	void testTypeCheckNumber() {
		
	}
	
	@Test
	void testTypeCheckString() {
		
	}
	
	@Test
	void testTypeCheckObject() {
		
	}
	
	@Test
	void testMean() {
		
	}
	
	@Test
	void testmedian() {
		
	}
}
