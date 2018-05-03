package testing.comp3111;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataType;
import core.comp3111.ImportExportCSV;


/**
 * ImportExport test using JUnit. Achieves 100% test coverage in the ImportExportCSV class.
 * 
 * @author kwaleung
 *
 */
class ImportExportTest {

	private static final String replaceWithZeros = "Replace with zeros";
	private static final String replaceWithMean = "Replace with column mean";
	private static final String replaceWithMedian = "Replace with column median";
	
	/**
	 * Test case for importCSV function in the ImportExportCSV class.
	 * CSV file used for this test case can be found in TestingCSV under project directory.
	 * This test case covers the import case where a normal CSV file with content is chosen by the user to import.
	 * The CSV file will be imported nonrmally as a DataTable.
	 * 
	 * @throws Exception
	 * @author kwaleung
	 */
	@Test
	void testImport() throws Exception {
		ImportExportCSV testImportExport = new ImportExportCSV();
		String fileDir = System.getProperty("user.dir") + "/TestingCSV/Test.csv";		
		File testImportFile = new File(fileDir);
		
		DataTable testImportTable = testImportExport.importCSV(testImportFile);
		assert(testImportTable.getNumCol() == 8);
	}
	
	/**
	 * Test case for importCSV function in the ImportExportCSV class.
	 * CSV file used for this test case can be found in TesingCSV under project directory.
	 * This test case covers the import case where an empty CSV file without content is chosen by the user to import.
	 * The empty CSV file will not be imported.
	 * 
	 * @throws Exception
	 * @author kwaleung
	 */
	@Test
	void testImportEmpty() throws Exception {
		ImportExportCSV testImportExport = new ImportExportCSV();
		String fileDir = System.getProperty("user.dir") + "/TestingCSV/TestEmpty.csv";
		File testImportFile = new File(fileDir);
		
		DataTable testImportTable = testImportExport.importCSV(testImportFile);
		assert(testImportTable == null);
	}
	
	/**
	 * Test case for exportCSV function in the ImportExportCSV class.
	 * This test case covers the export case where a DataTable is exported as an excel UTF-8 format CSV file.
	 * The CSV file generated will be deleted automatically upon completion of the test case.
	 * 
	 * @throws Exception
	 * @author kwaleung
	 */
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
	
	/**
	 * Test case for method checkTypeName in the ImportExportCSV class.
	 * This test case covers the case where a column containing numbers is checked.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testTypeCheckNumber() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Object[] currCol = {"1", "2", "3"};
		assert(testImportExport.checkTypeName(currCol).equals(DataType.TYPE_NUMBER));
	}
	
	/**
	 * Test case for method checkTypeName in the ImportExportCSV class.
	 * This test case covers the case where a column containing strings is checked.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testTypeCheckString() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Object[] currCol = {"a", "b", "c"};
		assert(testImportExport.checkTypeName(currCol).equals(DataType.TYPE_STRING));
	}
	
	/**
	 * Test case for method checkTypeName in the ImportExportCSV class.
	 * This test case covers the case where a column containing Java Objects is checked.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testTypeCheckObject() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Object a = new Object();
		Object b = new Object();
		Object c = new Object();
		Object[] currCol = {a, b, c};
		assert(testImportExport.checkTypeName(currCol).equals(DataType.TYPE_OBJECT));
	}
	
	/**
	 * Test case for method calculateMean in the ImportExportCSV class.
	 * This test case covers the case where the mean of a complete column containing numbers is calculated.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testMean() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Number x = 19;
		Number y = 34;
		Double ans = 26.5;
		Object[] currCol = {x, y};
		
		assert(testImportExport.calculateMean(currCol).equals(ans));
	}
	
	/**
	 * Test case for method calculateMean in the ImportExportCSV class.
	 * This test case covers the case where the mean of a column containing numbers with missing data is calculated.
	 * 
	 * @author kwaleung
	 */
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
	
	/**
	 * Test case for method calculateMedian in the ImportExportCSV class.
	 * This test case covers the case where the median of a complete column with odd count elements containing numbers is calculated.
	 * 
	 * @author kwaleung
	 */
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
	
	/**
	 * Test case for method calculateMedian in the ImportExportCSV class.
	 * This test case covers the case where the median of a complete column with even count elements containing numbers is calculated.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testmedianEvenElements() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Number x = 19;
		Number y = 34;
		Double ans = 26.5;
		Object[] currCol = {x, y};
		
		assert(testImportExport.calculateMedian(currCol).equals(ans));
	}
	
	/**
	 * Test case for method calculateMedian in the ImportExportCSV class.
	 * This test case covers the case where the median of a column containing numbers with missing data is calculated.
	 * 
	 * @author kwaleung
	 */
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
	
	/**
	 * Test case for method replaceEmptyElement in the ImportExportCSV class.
	 * This test case covers all cases where the user:
	 * 1. Chooses any of the three replace options
	 * 2. Default case where an empty or null choice is selected 
	 * 
	 * @author kwaleung
	 */
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
	
	/**
	 * Test case for method checkMissingData in the ImportExportCSV class.
	 * This test case covers the case where the column being checked contains missing data.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testEmptyTrue() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Object[] currCol = {"", "0", "abc"};
		assert(testImportExport.checkMissingData(currCol));
	}
	
	/**
	 * Test case for method checkMissingData in the ImportExportCSV class.
	 * This test case covers the case where the column being checked does not have missing data.
	 * 
	 * @author kwaleung
	 */
	@Test
	void testEmptyFalse() {
		ImportExportCSV testImportExport = new ImportExportCSV();
		Object[] currCol = {"abc", "0", "abc"};
		assert(!testImportExport.checkMissingData(currCol));
	}
}
