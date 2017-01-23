/**
 * CalendarPickerFXMLTest.java
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
import java.util.Locale;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import jfxtras.fxml.JFXtrasBuilderFactory;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

import org.junit.Assert;
import org.junit.Test;


/**
 * We use a single FXML to minimize the number of files.
 * But in order to prevent that the window becomes too width or height, tabs are used to separate the test cases
 * 
 */
public class CalendarPickerFXMLTest extends JFXtrasGuiTest {

	/**
	 * 
	 */
	@Override
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
	public void defaultPicker()
	{
		// show the correct tab
		clickOn("#defaultTab");
		
		// get the node
		CalendarPicker lCalendarPicker = (CalendarPicker)find("#defaultPicker");
		
		// default value is null
		Assert.assertNull(lCalendarPicker.getCalendar());

		// click the first of the displayed month		
		clickOn("#defaultPicker #" + new SimpleDateFormat("yyyy-MM-01").format(lCalendarPicker.getDisplayedCalendar().getTime()));
		
		// default value is not null
		Assert.assertNotNull(lCalendarPicker.getCalendar());
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
		CalendarPicker lCalendarPicker = (CalendarPicker)find("#atributesAreSet2Picker");
		
		// set properties
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(lCalendarPicker.getDisplayedCalendar()));
		Assert.assertEquals("de", lCalendarPicker.getLocale().toString());
		Assert.assertEquals("MULTIPLE", lCalendarPicker.getMode().toString());
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
		CalendarPicker lCalendarPicker = (CalendarPicker)find("#atributesAreSet3Picker");
		
		// set properties
		Assert.assertEquals(true, lCalendarPicker.getShowTime());
		Assert.assertEquals("2013-01-10T22:33:44.000", TestUtil.quickFormatCalendarAsDateTime(lCalendarPicker.getCalendar()));
		
	}

	/**
	 * 
	 */
	@Test
	public void attributesAreSet4()
	{
		// show the correct tab
		clickOn("#atributesAreSet4Tab");
		
		// get the node
		CalendarPicker lCalendarPicker = (CalendarPicker)find("#atributesAreSet4Picker");
		
		// set properties
		Assert.assertEquals(false, lCalendarPicker.getAllowNull());
		Assert.assertNotNull(lCalendarPicker.getCalendar());
	}
}
