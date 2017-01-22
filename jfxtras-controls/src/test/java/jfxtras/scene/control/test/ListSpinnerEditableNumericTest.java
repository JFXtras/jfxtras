/**
 * ListSpinnerEditableNumericTest.java
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

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import jfxtras.scene.control.ListSpinner;
import jfxtras.scene.layout.VBox;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.util.StringConverterFactory;

/**
 * Created by user on 26-12-13.
 */
public class ListSpinnerEditableNumericTest extends JFXtrasGuiTest {
	public Parent getRootNode()
	{
		Locale.setDefault(Locale.ENGLISH);
		
		VBox box = new VBox();

		lSpinner = new ListSpinner<Integer>(1, 10)
				.withEditable(true)
				.withStringConverter(StringConverterFactory.forInteger())
				.withValue(5)
				;
		box.getChildren().add(lSpinner);
		box.getChildren().add(new Button("focus helper"));

		return box;
	}
	ListSpinner<Integer> lSpinner = null;

	@Test
	public void enterSelectValueByTyping()
	{
		// check to see what the current value is
		Assert.assertEquals(5, lSpinner.getValue().intValue());

		// enter the text (this still is limited to the list)
		clickOn(".value").eraseText(1).write("6");

		// move focus away
		clickOn(".button");

		// see if the typed text is the current value
		Assert.assertEquals(6, lSpinner.getValue().intValue());
	}

	@Test
	public void lowerEdge()
	{
		// check to see what the current value is
		Assert.assertEquals(5, lSpinner.getValue().intValue());

		// enter the text (this still is limited to the list)
		clickOn(".value").eraseText(1).write("0");

		// move focus away
		clickOn(".button");

		// see if the typed text is the current value
		Assert.assertEquals(5, lSpinner.getValue().intValue());

		// enter the text (this still is limited to the list)
		clickOn(".value").eraseText(1).write("1");

		// move focus away
		clickOn(".button");

		// see if the typed text is the current value
		Assert.assertEquals(1, lSpinner.getValue().intValue());
	}

	@Test
	public void upperEdge()
	{
		// check to see what the current value is
		Assert.assertEquals(5, lSpinner.getValue().intValue());

		// enter the text (this still is limited to the list)
		clickOn(".value").eraseText(1).write("11");

		// move focus away
		clickOn(".button");

		// see if the typed text is the current value
		Assert.assertEquals(5, lSpinner.getValue().intValue());

		// enter the text (this still is limited to the list)
		clickOn(".value").eraseText(1).write("10");

		// move focus away
		clickOn(".button");

		// see if the typed text is the current value
		Assert.assertEquals(10, lSpinner.getValue().intValue());
	}
}
