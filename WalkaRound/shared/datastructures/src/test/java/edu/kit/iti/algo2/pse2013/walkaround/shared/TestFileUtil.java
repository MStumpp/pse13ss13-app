package edu.kit.iti.algo2.pse2013.walkaround.shared;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestFileUtil {

	@Test
	public void test() {
		String s = FileUtil.getFile(null).getAbsolutePath();
		assertTrue(s.contains(System.getProperty("user.home")));
		assertTrue(s.contains("Dropbox"));
		assertTrue(s.contains("null"));
		s = FileUtil.getFile("abc").getAbsolutePath();
		assertTrue(s.contains(System.getProperty("user.home")));
		assertTrue(s.contains("Dropbox"));
		assertTrue(s.contains("abc"));
	}

}
