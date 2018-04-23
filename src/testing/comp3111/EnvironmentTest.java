package testing.comp3111;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
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

class EnvironmentTest {

	@Test
	void testEnvironmentConstructor() {
		Environment testEnv = new Environment();
		assert (testEnv.getEnvironmentDataTables().size() == 0);
		assert (testEnv.getEnviornmentLineCharts().size() == 0);
		assert (testEnv.getEnviornmentPieCharts().size() == 0);
	}
	
	@Test
	void testEnviornmentgetsetDataTables() throws DataTableException {
		Environment testEnv = new Environment();
		HashMap<String, DataTable> testTables = new HashMap<String, DataTable>();
		
		testEnv.setEnvironmentDataTables(testTables);
		assert (testEnv.getEnvironmentDataTables().size() == 0);
	}
	
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
	
	@Test
	void testSave() throws Exception {
		Environment testEnv = new Environment();
		String testFilePath = System.getProperty("user.dir") + "testenvr.3111";
		File testFile = new File(testFilePath);
		
		String returnTestPath = testEnv.saveEnvironment(testFile);
		assert(returnTestPath.equals(testFilePath));
	}
	
	@Test
	void testLoad() throws Exception {
		Environment testEnv = new Environment();
		
		String testFilePath = System.getProperty("user.dir") + "testenvr.3111";
		File testFile = new File(testFilePath);
		
		String returnTestPath = testEnv.loadEnvironment(testFile);
		assert(returnTestPath.equals(testFilePath));
	}
}
