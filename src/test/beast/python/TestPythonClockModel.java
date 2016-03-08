package test.beast.python;

import org.junit.Test;

import beast.python.PythonClockModel;
import junit.framework.TestCase;

public class TestPythonClockModel extends TestCase {
	
	@Test
	public void testPythonClockModel() {
		PythonClockModel m = new PythonClockModel();
		m.initByName("value", "1");
	}

}
