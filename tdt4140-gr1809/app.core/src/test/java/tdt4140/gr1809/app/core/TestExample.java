package tdt4140.gr1809.app.core;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestExample extends TestCase{
	
	public TestExample(String testname) {
		super(testname);
	}
	public static Test suite() {
		return new TestSuite(TestExample.class);
	}
	public void testApp() {
		assertTrue(true);
	}
}
