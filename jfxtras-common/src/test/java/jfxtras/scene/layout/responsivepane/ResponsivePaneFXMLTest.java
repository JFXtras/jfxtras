/**
 * VBoxTest.java
 *
 * Copyright (c) 2011-2016, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.scene.layout.responsivepane;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

/**
 * 
 */
public class ResponsivePaneFXMLTest extends JFXtrasGuiTest {

	@Rule public TestName testName = new TestName();
	
	/**
	 * 
	 */
	public Parent getRootNode() {
		try {
			Locale.setDefault(Locale.ENGLISH);
			responsivePane = null;
			
	    	// load FXML
			String lName = this.getClass().getSimpleName() + "_" + testName.getMethodName() + ".fxml";
			URL lURL = this.getClass().getResource(lName);
			System.out.println("loading FXML " + lName + " -> " + lURL);
			if (lURL == null) throw new IllegalStateException("FXML file not found: " + lName);
			Parent lRoot = (Parent)FXMLLoader.load(lURL, null);
			return lRoot;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	private ResponsivePane responsivePane = null;
	
	/**
	 * 
	 */
	@Test
	public void empty() {
		// GIVEN a pane without any configuration
		responsivePane = (ResponsivePane)find("#responsivePane");
		// THEN the default (size 0.0) settings should be active
		Assert.assertNotNull(responsivePane);
		Assert.assertEquals("0.0in", responsivePane.getActiveLayout().describeSizeConstraints());
		Assert.assertEquals("0.0in", responsivePane.getActiveSceneStylesheet().describeSizeConstraints());
		Assert.assertEquals("0.0in", responsivePane.getActiveMyStylesheet().describeSizeConstraints());
	}

	/**
	 * 
	 */
	@Test
	public void basic() {

		// GIVEN a pane with a compound configuration
		responsivePane = (ResponsivePane)find("#responsivePane");
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(10.0);
		});
		
		// THEN all the layouts haven been assigned in the controller
		Assert.assertNotNull(ResponsivePaneFXMLTestBasicController.responsivePaneFXMLTestBasicController.layout3_0in);
		Assert.assertNotNull(ResponsivePaneFXMLTestBasicController.responsivePaneFXMLTestBasicController.size100_0cm);
		Assert.assertNotNull(ResponsivePaneFXMLTestBasicController.responsivePaneFXMLTestBasicController.size100_0cmL);
		Assert.assertNotNull(ResponsivePaneFXMLTestBasicController.responsivePaneFXMLTestBasicController.tablet);
		Assert.assertNotNull(ResponsivePaneFXMLTestBasicController.responsivePaneFXMLTestBasicController.width_3_0in);

		// THEN the reusable nodes all have been loaded
		Assert.assertEquals(2, responsivePane.getReusableNodes().size());
		Assert.assertTrue(responsivePane.getReusableNodes().get(0) instanceof Label);
		Assert.assertTrue(responsivePane.getReusableNodes().get(1) instanceof Button);
		
		// THEN the layouts should all have been loaded
		Assert.assertEquals(5, responsivePane.getLayouts().size());
		Assert.assertEquals("3.0in", responsivePane.getLayouts().get(0).describeSizeConstraints());
		Assert.assertEquals("width=3.0in", responsivePane.getLayouts().get(1).describeSizeConstraints());
		Assert.assertEquals(responsivePane.getDeviceSizes().get("TABLET").toString(), responsivePane.getLayouts().get(2).describeSizeConstraints());
		Assert.assertEquals("100.0cm", responsivePane.getLayouts().get(3).describeSizeConstraints());
		Assert.assertEquals("100.0cm-LANDSCAPE", responsivePane.getLayouts().get(4).describeSizeConstraints());
		
		// THEN the scene stylesheets should all have been loaded
		Assert.assertEquals(3, responsivePane.getSceneStylesheets().size());
		Assert.assertEquals("3.0in", responsivePane.getSceneStylesheets().get(0).describeSizeConstraints());
		Assert.assertEquals("width=3.0in", responsivePane.getSceneStylesheets().get(1).describeSizeConstraints());
		Assert.assertEquals(responsivePane.getDeviceSizes().get("TABLET").toString(), responsivePane.getSceneStylesheets().get(2).describeSizeConstraints());
		
		// THEN the my stylesheets should all have been loaded
		Assert.assertEquals(3, responsivePane.getMyStylesheets().size());
		Assert.assertEquals("3.0in", responsivePane.getMyStylesheets().get(0).describeSizeConstraints());
		Assert.assertEquals("width=3.0in", responsivePane.getMyStylesheets().get(1).describeSizeConstraints());
		Assert.assertEquals(responsivePane.getDeviceSizes().get("TABLET").toString(), responsivePane.getMyStylesheets().get(2).describeSizeConstraints());
		
		// THEN the custom device should have been added
		Assert.assertEquals(4, responsivePane.getDeviceSizes().size());
		// AND the desktop size should have been changed
		Assert.assertEquals("w:12in", "" + responsivePane.getDeviceSizes().get(Device.DESKTOP.toString()));
	}

	// ==========================================================================================================================================================================================================================================
	// SUPPORT
	
	private void setStageSizeInInch(double width, double height) {
		double lPPI = responsivePane.determinePPI();
		stage.setWidth(width * lPPI);
		stage.setHeight(height * lPPI);
	}
	
	private void setStageDiagonalSizeInInch(double diagonal) {
		double lSideInInch = Math.sqrt((diagonal * diagonal) / 2);
		setStageSizeInInch(lSideInInch, lSideInInch);
	}
}
