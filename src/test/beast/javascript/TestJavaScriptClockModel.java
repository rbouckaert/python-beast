package test.beast.javascript;

import org.junit.Test;

import beast.javascript.JavaScriptClockModel;
import junit.framework.TestCase;

public class TestJavaScriptClockModel extends TestCase {
	
	@Test
	public void testJavaScriptClockModel() {
		JavaScriptClockModel m = new JavaScriptClockModel();
		m.initByName("value", "1");
	}

}
