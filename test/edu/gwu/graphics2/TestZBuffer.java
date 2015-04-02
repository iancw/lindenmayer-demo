package edu.gwu.graphics2;

import java.awt.Dimension;

import junit.framework.Assert;

import org.junit.Test;

public class TestZBuffer {

	@Test
	public void testScale()
	{
		ZBuffer buf = new ZBuffer(new Dimension(500,500), -7.1860557f, 1.4e-45f);
		Assert.assertEquals(Integer.MAX_VALUE, buf.scale(1.4e-45f));
		Assert.assertEquals(0, buf.scale(-7.1860557f));
	}
}
