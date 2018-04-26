package testing.comp3111;

import org.junit.jupiter.api.Test;

import core.comp3111.DataType;

/**
 * DataType test using JUnit. Achieves 100% test coverage in the DataType class.
 * 
 * @author kwaleung
 *
 */
class DataTypeTest {

	@Test
	void testDataType(){
		DataType testDataType = new DataType();
		assert(testDataType.TYPE_NUMBER.equals("java.lang.Number"));
		assert(testDataType.TYPE_STRING.equals("java.lang.String"));
		assert(testDataType.TYPE_OBJECT.equals("java.lang.Object"));
	}
	
}
