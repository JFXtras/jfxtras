/**
 * CalendarTextFieldFXMLTest.java
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

package jfxtras.scene.control.test;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import jfxtras.fxml.JFXtrasBuilderFactory;
import jfxtras.scene.control.CalendarTextField;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;
import jfxtras.util.PlatformUtil;

import org.junit.Assert;
import org.junit.Test;


/**
 * We use a single FXML to minimize the number of files.
 * But in order to prevent that the window becomes too width or height, tabs are used to separate the test cases
 * 
 */
public class CalendarTextFieldFXMLTest extends JFXtrasGuiTest {

	/**
	 * 
	 */
	public Parent getRootNode() 
	{
		try {
			Locale.setDefault(Locale.ENGLISH);
			
	    	// load FXML
			String lName = this.getClass().getSimpleName() + ".fxml";
			URL lURL = this.getClass().getResource(lName);
			//System.out.println("loading FXML " + lName + " -> " + lURL);
			if (lURL == null) throw new IllegalStateException("FXML file not found: " + lName);
			Parent lRoot = (Parent)FXMLLoader.load(lURL, null, new JFXtrasBuilderFactory());
			return lRoot;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 */
	@Test
	public void defaultControl()
	{
		// show the correct tab
		clickOn("#defaultTab");
		
		// get the node
		CalendarTextField lCalendarTextField = (CalendarTextField)find("#defaultPicker");
		
		// default value is null
		Assert.assertNull(lCalendarTextField.getCalendar());

		// popup the picker
		clickOn(".icon");

		// click the last day in the first week (this button is always visible)
		clickOn("#" + new SimpleDateFormat("yyyy-MM-01").format(Calendar.getInstance().getTime()));

		// default value is not null
		Assert.assertNotNull(lCalendarTextField.getCalendar());
	}

	/**
	 * 
	 */
	@Test
	public void attributesAreSet2()
	{
		// show the correct tab
		clickOn("#atributesAreSet2Tab");
		
		// get the node
		CalendarTextField lCalendarTextField = (CalendarTextField)find("#atributesAreSet2Picker");
		TextField lTextField = (TextField)find("#atributesAreSet2Picker .text-field");
		
		// check properties
		Assert.assertEquals("2013-01-01 22:33:44", lCalendarTextField.getDateFormat().format(new GregorianCalendar(2013, 1-1, 1, 22, 33, 44).getTime()));
		
		// type value
		clickOn("#atributesAreSet2Picker .text-field");
		write("2010");
		clickOn(".button"); //just to move the focus
		Assert.assertEquals("2010-01-01T00:00:00.000", TestUtil.quickFormatCalendarAsDateTime(lCalendarTextField.getCalendar()) );
		TestUtil.runThenWaitForPaintPulse( () -> {
			lTextField.clear();
		});
		
		// type value
		clickOn("#atributesAreSet2Picker .text-field");
		write("2010-06");
		clickOn(".button"); //just to move the focus
		Assert.assertEquals("2010-06-01T00:00:00.000", TestUtil.quickFormatCalendarAsDateTime(lCalendarTextField.getCalendar()) );
		TestUtil.runThenWaitForPaintPulse( () -> {
			lTextField.clear();
		});
		
		// type value
		clickOn("#atributesAreSet2Picker .text-field");
		write("2010-06-12");
		clickOn(".button"); //just to move the focus
		Assert.assertEquals("2010-06-12T00:00:00.000", TestUtil.quickFormatCalendarAsDateTime(lCalendarTextField.getCalendar()) );
		TestUtil.runThenWaitForPaintPulse( () -> {
			lTextField.clear();
		});
	}

	/**
	 * 
	 */
	@Test
	public void attributesAreSet3()
	{
		// show the correct tab
		clickOn("#atributesAreSet3Tab");
		
		// get the node
		CalendarTextField lCalendarTextField = (CalendarTextField)find("#atributesAreSet3Picker");		
		TextField lTextField = (TextField)find("#atributesAreSet3Picker .text-field");
		
		// check properties
		Assert.assertEquals("de", lCalendarTextField.getLocale().toString());
		Assert.assertEquals("locale set", lTextField.getPromptText());
	}

}
