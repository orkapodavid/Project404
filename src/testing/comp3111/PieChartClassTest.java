package testing.comp3111;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;

import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataTableException;
import core.comp3111.DataType;
import core.comp3111.PieChartClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart.Data;

/**
 * PieChartClass test cases written by JUnit. It achieves 100% test
 * coverage on the PieChartClass class
 * 
 * @author OR Ka Po, kpor
 *
 */
public class PieChartClassTest {
	
	/**
	 * Test case for default constructor in the PieChartClass class.
	 * 
	 * @author OR Ka Po, kpor
	 */
	@Test
	void testDefaultConstructer() {
		PieChartClass test = new PieChartClass();
		assert(test.getNumColName().equals(""));
		assert(test.getTextColName().equals(""));
		assert(test.getTitle().equals(""));
		assert(test.getObserList() == null);
	}
	
	/**
	 * Test case for constructor in the PieChartClass class.
	 * 
	 * @author OR Ka Po, kpor
	 * @throws DataTableException if operation on DataTable is invalid
	 */
	@Test
	void testConstructer() throws DataTableException {
		DataTable currentDataTable = new DataTable(); 
		Object[] objArr = {"ab"};
		Object[] objArr2 = {1};
		DataColumn newCol = new DataColumn(DataType.TYPE_STRING, objArr);
		DataColumn newCol2 = new DataColumn(DataType.TYPE_NUMBER, objArr2);
		currentDataTable.addCol("objArr", newCol);
		currentDataTable.addCol("objArr2", newCol2);
		PieChartClass test = new PieChartClass(currentDataTable, "objArr2","objArr", "Test" );
		assert(test.getNumColName().equals("objArr2"));
		assert(test.getTextColName().equals("objArr"));
		assert(test.getTitle().equals("Pie Chart of "+"Test"));
		for( PieChart.Data data: test.getObserList()) {
			assert(data.getName().equals("ab"));
			assert(data.getPieValue()==((Number)1).doubleValue());
		}
	}
	
	/**
	 * Test case for the method setList in the PieChartClass class.
	 * 
	 * @author OR Ka Po, kpor
	 */
	@Test 
	void testSetList() {
		PieChartClass test = new PieChartClass();
		ObservableList<PieChart.Data> pieChartDataList = FXCollections.observableArrayList();
		pieChartDataList.add(new PieChart.Data((String)"ab", ((Number) 1).doubleValue()));
		test.setList(pieChartDataList);
		for( PieChart.Data data: test.getObserList()) {
			assert(data.getName().equals("ab"));
			assert(data.getPieValue()==((Number)1).doubleValue());
		}
	}
	
	/**
	 * Test case for the method setTitle in the PieChartClass class.
	 * 
	 * @author OR Ka Po, kpor
	 */
	@Test
	void testSetTitle() {
		PieChartClass test = new PieChartClass();
		test.setTitle("Pie Chart of Test");
		assert(test.getTitle().equals("Pie Chart of Test"));
	}
	
	/**
	 * Test case for the method setAxisName in the PieChartClass class.
	 * 
	 * @author OR Ka Po, kpor
	 */
	@Test
	void testSetAxisName() {
		PieChartClass test = new PieChartClass();
		test.setAxisName("num", "text");
		assert(test.getNumColName().equals("num"));
		assert(test.getTextColName().equals("text"));
	}
	
	/**
	 * Test case for the methods readExternal and writeExternal in the PieChartClass class.
	 * 
	 * @author OR Ka Po, kpor
	 * @throws IOException Includes any I/O exceptions that may occur
	 * @throws ClassNotFoundException If the class for an object being restored cannot be found
	 * @throws DataTableException if operation on DataTable is invalid
	 */
	@Test
	void testReadWriteExternal() throws IOException, ClassNotFoundException, DataTableException{
		// Initialize a LineChartClass object for testing
		DataTable currentDataTable = new DataTable(); 
		Object[] objArr = {"ab"};
		Object[] objArr2 = {1};
		DataColumn newCol = new DataColumn(DataType.TYPE_STRING, objArr);
		DataColumn newCol2 = new DataColumn(DataType.TYPE_NUMBER, objArr2);
		currentDataTable.addCol("objArr", newCol);
		currentDataTable.addCol("objArr2", newCol2);
		PieChartClass test = new PieChartClass(currentDataTable, "objArr2","objArr", "Test" );
		// Write the LineChartClass object into file 
		String testFilePath = new String(System.getProperty("user.dir")+"/comp3111LineChartClaseTest.txt");
		System.out.println("Testing txt file: filePath = " + testFilePath);
		File testFile = new File(testFilePath);
		FileOutputStream outStream = new FileOutputStream(testFilePath);
		ObjectOutputStream objOStream =  new ObjectOutputStream(outStream);
		test.writeExternal(objOStream);
		objOStream.close();
		outStream.close();
		
		// Initialize a empty LineChartClass object for testing
		PieChartClass testRead = new PieChartClass();
		// Read the same file for testing
		FileInputStream inStream = new FileInputStream(testFilePath);
		ObjectInputStream objiStream =  new ObjectInputStream(inStream);
		testRead.readExternal(objiStream);
		
		assert(test.getNumColName().equals("objArr2"));
		assert(test.getTextColName().equals("objArr"));
		assert(test.getTitle().equals("Pie Chart of "+"Test"));
		for( PieChart.Data data: test.getObserList()) {
			assert(data.getName().equals("ab"));
			assert(data.getPieValue()==((Number)1).doubleValue());
		}
		
		inStream.close();
		objiStream.close();
		// Delete the file	
		Files.deleteIfExists(testFile.toPath());
	}
}
