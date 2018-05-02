package testing.comp3111;


import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import core.comp3111.Environment;
import core.comp3111.EnvironmentParams;
import core.comp3111.LineChartClass;
import core.comp3111.PieChartClass;
import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataTableException;
import core.comp3111.DataType;

/**
 * Environment test using JUnit. Achieves 100% test coverage in the Environment and EnvironmentParams class.
 * 
 * @author kwaleung
 *
 */
class EnvironmentTest {

	/**
	 * Test case for constructor of Environment class
	 * 
	 * @author kwaleung
	 */
	@Test
	void testEnvironmentConstructor() {
		Environment testEnv = new Environment();
		assert (testEnv.getEnvironmentDataTables().size() == 0);
		assert (testEnv.getEnviornmentLineCharts().size() == 0);
		assert (testEnv.getEnviornmentPieCharts().size() == 0);
	}
	
	/**
	 * Test case for get and set methods for DataTable Map in the EnvironmentParams class
	 * 
	 * @author kwaleung
	 */
	@Test
	void testEnviornmentgetsetDataTables() throws DataTableException {
		Environment testEnv = new Environment();
		HashMap<String, DataTable> testTables = new HashMap<String, DataTable>();
		
		testEnv.setEnvironmentDataTables(testTables);
		assert (testEnv.getEnvironmentDataTables().size() == 0);
	}
	
	/**
	 * Test case for get and set methods for the LineChartClass and PieChartClass Maps in the EnvironmentParams class
	 * 
	 * @author kwaleung
	 */
	@Test
	void testEnviornmentgetsetLinePieCharts() {
		Environment testEnv = new Environment();
		HashMap<String, LineChartClass> testLineCharts = new HashMap<String, LineChartClass>();
		HashMap<String, PieChartClass> testPieCharts = new HashMap<String, PieChartClass>();

		testEnv.setEnviornmentLineCharts(testLineCharts);
		testEnv.setEnviornmentPieCharts(testPieCharts);
		assert (testEnv.getEnviornmentLineCharts().size() == 0);
		assert (testEnv.getEnviornmentPieCharts().size() == 0);
	}
	
	/**
	 * Test case for methods saveEnvironment and loadEnvironment in the Environment class
	 * 
	 * @author kwaleung
	 */
	@Test
	void testSaveLoad() throws Exception {
		Environment testEnv = new Environment();
		String testFilePath = System.getProperty("user.dir") + "/saveloadtest.3111";
		File testFile = new File(testFilePath);
		String returnTestPath = testEnv.saveEnvironment(testFile);
		assert(returnTestPath.equals(testFile.getAbsolutePath()));
		returnTestPath = testEnv.loadEnvironment(testFile);
		assert(returnTestPath.equals(testFile.getAbsolutePath()));
		Files.deleteIfExists(testFile.toPath());
	}
	
	@Test
	void testOperandsCompare() {
		Environment testEnv = new Environment();
		assert(testEnv.operandsCompare(3.0, ">", 2.0) == true);
		assert(testEnv.operandsCompare(3.0, ">", 4.0) == false);
		assert(testEnv.operandsCompare(1.0, "<", 2.0) == true);
		assert(testEnv.operandsCompare(3.0, "<", 2.0) == false);
		assert(testEnv.operandsCompare(2.0, ">=", 2.0) == true);
		assert(testEnv.operandsCompare(1.0, ">=", 2.0) == false);
		assert(testEnv.operandsCompare(1.0, "<=", 2.0) == true);
		assert(testEnv.operandsCompare(3.0, "<=", 2.0) == false);
		assert(testEnv.operandsCompare(2.0, "==", 2.0) == true);
		assert(testEnv.operandsCompare(1.0, "==", 2.0) == false);
		assert(testEnv.operandsCompare(3.0, "!=", 2.0) == true);
		assert(testEnv.operandsCompare(2.0, "!=", 2.0) == false);
		assert(testEnv.operandsCompare(2.0, "default", 2.0) == false);
		
		assert(testEnv.operandsCompare(2.0, "\0>", 2.0) == false);
		assert(testEnv.operandsCompare(2.0, "\0<", 2.0) == false);
		assert(testEnv.operandsCompare(2.0, "\0>=", 2.0) == false);
		assert(testEnv.operandsCompare(2.0, "\0<=", 2.0) == false);
		assert(testEnv.operandsCompare(2.0, "\0==", 2.0) == false);
		assert(testEnv.operandsCompare(2.0, "\0!=", 2.0) == false);
	}
	
	@Test 
	void testFilterDatasetByNum() throws DataTableException {
		// Setup:
		Environment testEnv = new Environment();
		DataTable testDataTable = new DataTable();
		Object[] objArr = {1,2,3,4};
		DataColumn newCol = new DataColumn(DataType.TYPE_NUMBER, objArr);
		testDataTable.addCol("objArr", newCol);
		testEnv.getEnvironmentDataTables().put("testDataTable", testDataTable);
		
		// Case: replaced the original DataTable with new filtered DataTable
		String resultNotReplacedName = testEnv.filterDatasetByNum("testDataTable", "objArr", ">", 2.0, true);
		// If replaced == true, return null 
		assert(resultNotReplacedName == null);
		// If replaced == true, DataTable remains unchanged
		DataTable resultNotReplacedTable = testEnv.getEnvironmentDataTables().get("testDataTable");
		assert(resultNotReplacedTable != null);
		DataColumn resultNotReplacedCol = resultNotReplacedTable.getCol("objArr");
		Object[] resultNotReplacedArr = resultNotReplacedCol.getData();
		assert(resultNotReplacedArr.length == 2);
		assert((int)resultNotReplacedArr[0] == 3);
		assert((int)resultNotReplacedArr[1] == 4);
		
		// Case: The filtered DataTable is empty--> environment remains unchanged
		String resultEmptyDataTable = testEnv.filterDatasetByNum("testDataTable", "objArr", ">", 4.0, true);
		assert(resultEmptyDataTable.equals("Empty DataTable"));
		assert(resultNotReplacedArr.length == 2);
		assert((int)resultNotReplacedArr[0] == 3);
		assert((int)resultNotReplacedArr[1] == 4);
		
		// Case: Create a new DataTable with new filtered DataTable
		String resultReplacedName = testEnv.filterDatasetByNum("testDataTable", "objArr", ">", 2.0, false);
		assert(resultReplacedName.equals("DataSet2"));
		DataTable resultReplacedTable = testEnv.getEnvironmentDataTables().get("DataSet2");
		DataColumn resultReplacedCol = resultReplacedTable.getCol("objArr");
		Object[] resultReplacedArr = resultReplacedCol.getData();
		assert(resultNotReplacedArr.length == 2);
		assert((int)resultReplacedArr[0] == 3);
		assert((int)resultReplacedArr[1] == 4);
	}
	
	@Test
	void testRandSplitDatasetByNum() throws DataTableException {
		// Setup:
		Environment testEnv = new Environment();
		DataTable testDataTable = new DataTable();
		Object[] objArr = {1,2,3,4};
		DataColumn newCol = new DataColumn(DataType.TYPE_NUMBER, objArr);
		testDataTable.addCol("objArr", newCol);
		testEnv.getEnvironmentDataTables().put("testDataTable", testDataTable);
		int splitRatio = 50;
		
		// Case:  replaced the original DataTable with one of the split DataTable
		String[] resultReplaced = testEnv.randSplitDatasetByNum("testDataTable", splitRatio, true);
		assert(resultReplaced[0].equals("testDataTable"));
		assert(resultReplaced[1].equals("DataSet2"));
		DataTable resultReplacedTable1 = testEnv.getEnvironmentDataTables().get("testDataTable");
		DataTable resultReplacedTable2 = testEnv.getEnvironmentDataTables().get("DataSet2");
		assert(resultReplacedTable1.getCol("objArr").getData().length == 2);
		assert(resultReplacedTable2.getCol("objArr").getData().length == 2);
		
		//  Case: Create two new DataTables with the split DataTables
		String[] resultNotReplaced = testEnv.randSplitDatasetByNum("testDataTable", splitRatio, false);
		assert(resultNotReplaced[0].equals("DataSet3"));
		assert(resultNotReplaced[1].equals("DataSet4"));
		DataTable resultNotReplacedTable1 = testEnv.getEnvironmentDataTables().get("DataSet3");
		DataTable resultNotReplacedTable2 = testEnv.getEnvironmentDataTables().get("DataSet4");
		assert(resultNotReplacedTable1.getCol("objArr").getData().length == 1);
		assert(resultNotReplacedTable2.getCol("objArr").getData().length == 1);
		
		/*Case:		Fail to create two new DataTables with the split DataTables
		 * 			Because one of the split DataTable is empty
		 * 			Thus, only one non-empty new DataTable is created
		 * */
		splitRatio = 0;
		String[] resultEmptyNotReplaced = testEnv.randSplitDatasetByNum("testDataTable", splitRatio, false);
		assert(resultEmptyNotReplaced[0].equals(""));
		assert(resultEmptyNotReplaced[1].equals("DataSet5"));
		DataTable resultEmptyNotReplacedTable = testEnv.getEnvironmentDataTables().get("DataSet5");
		assert(resultEmptyNotReplacedTable.getCol("objArr").getData().length == 2);
		
		splitRatio = 100;
		String[] resultEmptyNotReplaced2 = testEnv.randSplitDatasetByNum("testDataTable", splitRatio, false);
		assert(resultEmptyNotReplaced2[0].equals("DataSet6"));
		assert(resultEmptyNotReplaced2[1].equals(""));
		DataTable resultEmptyNotReplacedTable2 = testEnv.getEnvironmentDataTables().get("DataSet5");
		assert(resultEmptyNotReplacedTable2.getCol("objArr").getData().length == 2);
		
		/* Case:	Fail to replaced the original DataTable with the split DataTables
		 * 			Because one of the split DataTable is empty
		 * 			Thus, the environment remains unchanged
		 * */
		splitRatio = 0;
		String[] resultEmptyReplaced = testEnv.randSplitDatasetByNum("testDataTable", splitRatio, true);
		assert(resultEmptyReplaced[0].equals(""));
		assert(resultEmptyReplaced[1].equals(""));
		splitRatio = 100;
		String[] resultEmptyReplaced2 = testEnv.randSplitDatasetByNum("testDataTable", splitRatio, true);
		assert(resultEmptyReplaced2[0].equals(""));
		assert(resultEmptyReplaced2[1].equals(""));
		
		// Case: Make sure enough random indices have been created
		splitRatio = 100;
		DataTable testManyDataTable = new DataTable();
		Object[] manyObjArr = new Object[100];
		Arrays.fill(manyObjArr, (Object)1);
		DataColumn manyObjCol = new DataColumn(DataType.TYPE_NUMBER, manyObjArr);
		testManyDataTable.addCol("manyObjArr", manyObjCol);
		testEnv.getEnvironmentDataTables().put("testManyDataTable", testManyDataTable);
		testEnv.randSplitDatasetByNum("testManyDataTable", splitRatio, true);
	}
}
