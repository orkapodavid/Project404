package testing.comp3111;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.Environment;
import core.comp3111.LineChartClass;
import javafx.scene.chart.XYChart;

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
}
