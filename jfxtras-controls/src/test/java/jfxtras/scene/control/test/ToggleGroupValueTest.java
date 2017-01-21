/**
 * ToggleGroupValueTest.java
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.ToggleGroupValue;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tom Eugelink on 26-12-13.
 */
public class ToggleGroupValueTest extends JFXtrasGuiTest {

	public Parent getRootNode()
	{
		Locale.setDefault(Locale.ENGLISH);
		
		VBox box = new VBox();

		toggleGroupValue = new ToggleGroupValue<>();
		int idx = 0;
		for (int i = 0; i < 3; i++) {
			RadioButton lButton = new RadioButton("" + idx);
			lButton.setId("button" + idx);
			toggleGroupValue.add(lButton, "value" + idx);
			toggles.add(lButton);
			box.getChildren().add(lButton);
			idx++;
		}
		for (int i = 0; i < 3; i++) {
			ToggleButton lButton = new ToggleButton("" + idx);
			lButton.setId("button" + idx);
			toggleGroupValue.add(lButton, "value" + idx);
			toggles.add(lButton);
			box.getChildren().add(lButton);
			idx++;
		}
		box.getChildren().add(new Button("focus helper"));
		

		// make sure that the listspinner fits with arrows in both directions 
		box.setPrefSize(100, 100);
		
		return box;
	}
	ToggleGroupValue<String> toggleGroupValue;
	List<ToggleButton> toggles = new ArrayList<>();
	
	@Test
	public void defaultIsNotSelected()
	{
		Assert.assertNull(toggleGroupValue.getValue());
		Assert.assertNull(toggleGroupValue.getSelectedToggle());
	}

	@Test
	public void selectByclickOn()
	{
		Assert.assertNull(toggleGroupValue.getValue());
		clickOn("#button0");
		Assert.assertEquals("value0", toggleGroupValue.getValue());
		clickOn("#button3");
		Assert.assertEquals("value3", toggleGroupValue.getValue());
		clickOn("#button3");
		Assert.assertNull(toggleGroupValue.getValue());
	}

	@Test
	public void selectByValue()
	{
		Assert.assertNull(toggleGroupValue.getValue());
		
		TestUtil.runThenWaitForPaintPulse( () -> {
			toggleGroupValue.setValue("value2");
		});
		Assert.assertEquals(toggles.get(2).getText(), ((ToggleButton)toggleGroupValue.getSelectedToggle()).getText());
		
		TestUtil.runThenWaitForPaintPulse( () -> {
			toggleGroupValue.setValue(null);
		});
		Assert.assertNull(toggleGroupValue.getSelectedToggle());
	}
}
