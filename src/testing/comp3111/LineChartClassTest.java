package testing.comp3111;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import core.comp3111.Environment;
import core.comp3111.EnvironmentParams;
import core.comp3111.LineChartClass;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

public class LineChartClassTest {
	@Test
	void testLineChartClassConstructor() {
		LineChartClass test = new LineChartClass();
		assert (test.getSeries() == null);
		assert (test.getXAxisName().equals(""));
		assert (test.getYAxisName().equals(""));
		assert (test.getTitle().equals(""));
		assert (test.get_animate() == false);
	}
	
	@Test
	void testSetSeries() {
		LineChartClass test = new LineChartClass();
		XYChart.Series<Number, Number> testSeries = new XYChart.Series<Number, Number>();
		test.setSeries(testSeries);
		assert (test.getSeries() == testSeries);
	}
	
	@Test
	void testSetAnimate() {
		LineChartClass test = new LineChartClass();
		test.animate(true);
		assert(test.get_animate() == true);
	}
	
	@Test 
	void testSetAxisName() {
		LineChartClass test = new LineChartClass();
		test.setAxisName("x", "y");
		assert(test.getXAxisName().equals("x"));
		assert(test.getYAxisName().equals("y"));
	}
	
	@Test
	void testSetTitle() {
		LineChartClass test = new LineChartClass();
		test.setTitle("title");
		assert(test.getTitle().equals("title"));
	}
	
	@Test
	void testReadWriteExternal() throws Exception {
		// Initialize a LineChartClass object for testing
		LineChartClass test = new LineChartClass();
		test.setAxisName("x", "y");
		test.animate(true);
		test.setTitle(new String("title"));
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName("series");
		series.getData().add(new XYChart.Data<Number, Number>((Number) 1, (Number) 2));
		test.setSeries(series);
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
		LineChartClass testRead = new LineChartClass();
		
		// Read the same file for testing
		FileInputStream inStream = new FileInputStream(testFilePath);
		ObjectInputStream objiStream =  new ObjectInputStream(inStream);
		testRead.readExternal(objiStream);
		assert(testRead.get_animate()== true);
		for( Data<Number, Number> data: testRead.getSeries().getData()) {
			assert(data.getXValue().equals((Number)1));
			assert(data.getYValue().equals((Number)2));
		}
		assert(testRead.getYAxisName().equals("y"));
		assert(testRead.getTitle().equals("title"));
		assert(testRead.getXAxisName().equals("x"));
		assert(testRead.getYAxisName().equals("y"));
		
		inStream.close();
		objiStream.close();
		// Delete the file	
		Files.deleteIfExists(testFile.toPath());
		
		
	}
	
}
