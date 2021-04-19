/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.scene.control.test;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import jfxtras.scene.control.AccordionPane;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

/**
 * 
 */
// TODO: reusableNodes and Refs
public class AccordionTest extends JFXtrasGuiTest {

	/**
	 * 
	 */
	public Parent getRootNode() {
		accordionPane = new AccordionPane();
		accordionPane.setPrefSize(800.0, 600.0);
		return accordionPane;
	}
	private AccordionPane accordionPane = null;

	/**
	 * 
	 */
	@Test
	public void empty() {
		// GIVEN a harmonica with no contents
		// THEN the skin should have no nodes
		Pane lHarmonicaContents = (Pane)accordionPane.getChildrenUnmodifiable().get(0);
		Assert.assertEquals(0, lHarmonicaContents.getChildren().size());
		Assert.assertNull(accordionPane.getVisibleTab());
	}

	/**
	 * 
	 */
	@Test
	public void one() {
		// GIVEN a harmonica with one tab
		TestUtil.runThenWaitForPaintPulse( () -> {
			accordionPane.addTab("test1", new Label("test 1"));
		});
		Assert.assertEquals(1, accordionPane.tabs().size());
		
		// THEN the first tab is visible per default
		Assert.assertEquals(accordionPane.tabs().get(0), accordionPane.getVisibleTab());
		
		// THEN the skin should have two controls
		assertVisible("#TabButton-1");
		assertVisible("#TabPane-1");
	}

	/**
	 * 
	 */
	@Test
	public void two() {
		// GIVEN a harmonica with two tabs
		TestUtil.runThenWaitForPaintPulse( () -> {
			accordionPane.addTab("test1", new Label("test 1"));
			accordionPane.addTab("test2", new Label("test 2"));
		});
		Assert.assertEquals(2, accordionPane.tabs().size());
		
		// THEN the first tab is visible per default
		Assert.assertEquals(accordionPane.tabs().get(0), accordionPane.getVisibleTab());
		
		// THEN the first tabpane is visible, and the other is not
		assertVisible("#TabButton-1");
		assertVisible("#TabPane-1");
		assertVisible("#TabButton-2");
		assertNotVisible("#TabPane-2");
	}

	/**
	 * 
	 */
	@Test
	public void three() {
		// GIVEN a harmonica with three tabs
		TestUtil.runThenWaitForPaintPulse( () -> {
			accordionPane.addTab("test1", new Label("test 1"));
			accordionPane.addTab("test2", new Label("test 2"));
			accordionPane.addTab("test3", new Label("test 3"));
		});
		Assert.assertEquals(3, accordionPane.tabs().size());
		
		// THEN the first tab is visible per default
		Assert.assertEquals(accordionPane.tabs().get(0), accordionPane.getVisibleTab());
		
		// THEN the first tabpane is visible, and the others are not
		assertVisible("#TabButton-1");
		assertVisible("#TabPane-1");
		assertVisible("#TabButton-2");
		assertNotVisible("#TabPane-2");
		assertVisible("#TabButton-3");
		assertNotVisible("#TabPane-3");
	}

	/**
	 * 
	 */
	@Test
	public void adding() {
		// GIVEN a harmonica with no contents
		Assert.assertNull(accordionPane.getVisibleTab());

		// WHEN adding one tab
		TestUtil.runThenWaitForPaintPulse( () -> {
			accordionPane.addTab("test1", new Label("test 1"));
		});
		Assert.assertEquals(1, accordionPane.tabs().size());
		
		// THEN the first tab is visible per default
		Assert.assertEquals(accordionPane.tabs().get(0), accordionPane.getVisibleTab());
		
		// THEN the skin should have two controls
		assertVisible("#TabButton-1");
		assertVisible("#TabPane-1");
		
		// WHEN adding a second one
		TestUtil.runThenWaitForPaintPulse( () -> {
			accordionPane.addTab("test2", new Label("test 2"));
		});
		Assert.assertEquals(2, accordionPane.tabs().size());
		
		// THEN the first tab is visible per default
		Assert.assertEquals(accordionPane.tabs().get(0), accordionPane.getVisibleTab());
		
		// THEN the first tabpane is visible, and the other is not
		assertVisible("#TabButton-1");
		assertVisible("#TabPane-1");
		assertVisible("#TabButton-2");
		assertNotVisible("#TabPane-2");
	}

	/**
	 * 
	 */
	@Test
	public void changeVisibleTabProperty() {
		// GIVEN a harmonica with three tabs
		TestUtil.runThenWaitForPaintPulse( () -> {
			accordionPane.addTab("test1", new Label("test 1"));
			accordionPane.addTab("test2", new Label("test 2"));
			accordionPane.addTab("test3", new Label("test 3"));
		});
		
		// WHEN the second tab is made visible
		TestUtil.runThenWaitForPaintPulse( () -> {
			accordionPane.setVisibleTab(accordionPane.tabs().get(1));
		});
		
		// THEN the second tabpane is visible, and the others are not
		assertNotVisible("#TabPane-1");
		assertVisible("#TabPane-2");
		assertNotVisible("#TabPane-3");
		
		// WHEN the third tab is made visible
		TestUtil.runThenWaitForPaintPulse( () -> {
			accordionPane.setVisibleTab(accordionPane.tabs().get(2));
		});
		
		// THEN the third tabpane is visible, and the others are not
		assertNotVisible("#TabPane-1");
		assertNotVisible("#TabPane-2");
		assertVisible("#TabPane-3");
	}

	/**
	 * 
	 */
	@Test
	public void removing() {
		// GIVEN a harmonica with three tabs
		TestUtil.runThenWaitForPaintPulse( () -> {
			accordionPane.addTab("test1", new Label("test 1"));
			accordionPane.addTab("test2", new Label("test 2"));
			accordionPane.addTab("test3", new Label("test 3"));
		});
		
		// WHEN the first (visible) tab is removed
		TestUtil.runThenWaitForPaintPulse( () -> {
			accordionPane.tabs().remove(accordionPane.tabs().get(0));
		});
		
		// THEN the now first tab is visible
		Assert.assertEquals(accordionPane.tabs().get(0), accordionPane.getVisibleTab());
		assertVisible("#TabPane-2");
		assertNotVisible("#TabPane-3");
		
		// WHEN the other tabs are removed
		TestUtil.runThenWaitForPaintPulse( () -> {
			accordionPane.tabs().clear();
		});
		
		// THEN no tab is visible
		Assert.assertNull(accordionPane.getVisibleTab());
		Pane lHarmonicaContents = (Pane)accordionPane.getChildrenUnmodifiable().get(0);
		Assert.assertEquals(0, lHarmonicaContents.getChildren().size());
	}


	/**
	 * 
	 */
	@Test
	public void click() {
		// GIVEN a harmonica with three tabs
		TestUtil.runThenWaitForPaintPulse( () -> {
			accordionPane.addTab("test1", new Label("test 1"));
			accordionPane.addTab("test2", new Label("test 2"));
			accordionPane.addTab("test3", new Label("test 3"));
		});
		
		// WHEN on the second header is clicked
		click("#TabButton-2");
		
		// THEN the second tab should be visible
		Assert.assertEquals(accordionPane.tabs().get(1), accordionPane.getVisibleTab());
		assertNotVisible("#TabPane-1");
		assertVisible("#TabPane-2");
		assertNotVisible("#TabPane-3");
		
		// WHEN on the third header is clicked
		click("#TabButton-3");
		
		// THEN the third tab should be visible
		Assert.assertEquals(accordionPane.tabs().get(2), accordionPane.getVisibleTab());
		assertNotVisible("#TabPane-1");
		assertNotVisible("#TabPane-2");
		assertVisible("#TabPane-3");
	}
	
	// ==========================================================================================================================================================================================================================================
	// SUPPORT
	

	private void assertVisible(String string) {
		Assert.assertTrue(find(string).isVisible());
	}
}
