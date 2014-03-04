/**
 * CalendarPickerTest.java
 *
 * Copyright (c) 2011-2014, JFXtras
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
import java.util.Locale;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import jfxtras.fxml.JFXtrasBuilderFactory;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created by Tom Eugelink on 26-12-13.
 */
public class CalendarPickerFXMLTest extends JFXtrasGuiTest {

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
			Pane lRoot = (Pane)FXMLLoader.load(lURL, null, new JFXtrasBuilderFactory());
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
		// get the node
		CalendarPicker lCalendarPicker = (CalendarPicker)find("#id1");
		
		// default value is null
		Assert.assertNull(lCalendarPicker.getCalendar());

		// click the last day in the first week (this button is always visible)
		click("#id1 #day6");
		
		// default value is not null
		Assert.assertNotNull(lCalendarPicker.getCalendar());
	}

	/**
	 * 
	 */
	@Test
	public void atributesAreSet()
	{
		// get the node
		CalendarPicker lCalendarPicker = (CalendarPicker)find("#id2");
		
		// set properties
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(lCalendarPicker.getDisplayedCalendar()));
		Assert.assertEquals("de", lCalendarPicker.getLocale().toString());
		Assert.assertEquals("MULTIPLE", lCalendarPicker.getMode().toString());
	}

	/**
	 * 
	 */
	@Test
	public void atributesAreSet2()
	{
		// get the node
		CalendarPicker lCalendarPicker = (CalendarPicker)find("#id3");
		
		// set properties
		Assert.assertEquals(false, lCalendarPicker.getAllowNull());
		Assert.assertNotNull(lCalendarPicker.getCalendar());
		Assert.assertEquals(true, lCalendarPicker.getShowTime());
	}
}
