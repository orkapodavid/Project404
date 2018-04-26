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

	private static final String replaceWithZeros = "Replace with zeros";
	private static final String replaceWithMean = "Replace with column mean";
	private static final String replaceWithMedian = "Replace with column median";
	
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
		ImportExportCSV testImportExport = new ImportExportCSV();
		Object[] currCol = {"1", "2", "3"};
		assert(testImportExport.checkTypeName(currCol).equals(DataType.TYPE_NUMBER));
	}
	
	@Test
	void testTypeCheckString() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Object[] currCol = {"a", "b", "c"};
		assert(testImportExport.checkTypeName(currCol).equals(DataType.TYPE_STRING));
	}
	
	@Test
	void testTypeCheckObject() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Object a = new Object();
		Object b = new Object();
		Object c = new Object();
		Object[] currCol = {a, b, c};
		assert(testImportExport.checkTypeName(currCol).equals(DataType.TYPE_OBJECT));
	}
	
	@Test
	void testMean() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Number x = 19;
		Number y = 34;
		Double ans = 26.5;
		Object[] currCol = {x, y};
		
		assert(testImportExport.calculateMean(currCol).equals(ans));
	}
	
	@Test
	void testMeanHaveEmpty() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Number x = 19;
		Number y = 34;
		Object z = "";
		Double ans = 26.5;
		Object[] currCol = {x, z, y};
		
		assert(testImportExport.calculateMean(currCol).equals(ans));
	}
	
	@Test
	void testmedianOddElements() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Number x = 19;
		Number y = 34;
		Number z = 0;
		Double ans = (double) 19;
		Object[] currCol = {x, y, z};
		
		assert(testImportExport.calculateMedian(currCol).equals(ans));
	}
	
	@Test
	void testmedianEvenElements() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Number x = 19;
		Number y = 34;
		Double ans = 26.5;
		Object[] currCol = {x, y};
		
		assert(testImportExport.calculateMedian(currCol).equals(ans));
	}
	
	@Test
	void testmedianHaveEmpty() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Number x = 19;
		Number y = 34;
		Object z = "";
		Double ans = 26.5;
		Object[] currCol = {x, z, y};
		
		assert(testImportExport.calculateMedian(currCol).equals(ans));
	}
	
	@Test
	void testReplace() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Number mean = 1, median = 2;
		assert(testImportExport.replaceEmptyElement(replaceWithZeros, mean, median) == (Object)0);
		assert(testImportExport.replaceEmptyElement(replaceWithMean, mean, median) == (Object)mean);
		assert(testImportExport.replaceEmptyElement(replaceWithMedian, mean, median) == (Object)median);
		assert(testImportExport.replaceEmptyElement("", mean, median) == (Object)0);
		assert(testImportExport.replaceEmptyElement(null, mean, median) == (Object)0);
	}
	
	@Test
	void testEmptyTrue() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Object[] currCol = {"", "0", "abc"};
		assert(testImportExport.checkMissingData(currCol));
	}
	
	@Test
	void testEmptyFalse() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Object[] currCol = {"abc", "0", "abc"};
		assert(!testImportExport.checkMissingData(currCol));
	}
}
