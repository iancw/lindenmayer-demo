package edu.gwu.graphics2.lindenmayer;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class LindenmayerTest {

	@Test
	public void test()
	{
		String repl = "F[-F]F[+F][F]";
		Map<Character, String> map = new HashMap<Character, String>();
		map.put('F', repl);
		Lindenmayer lind = new Lindenmayer(map);
		String result = lind.iterate("F");
		assertEquals(repl, result);
		String i2 = lind.iterate(result);
		assertEquals(repl, i2);
	}
}
