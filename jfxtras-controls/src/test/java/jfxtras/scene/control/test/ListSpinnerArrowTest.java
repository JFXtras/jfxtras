/**
 * ListSpinnerArrowTest.java
 *
 * Copyright (c) 2011-2015, JFXtras
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

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import jfxtras.internal.scene.control.skin.ListSpinnerSkin;
import jfxtras.scene.control.ListSpinner;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Created by Tom Eugelink on 26-12-13.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ListSpinnerArrowTest extends JFXtrasGuiTest {

	public Parent getRootNode()
	{
		Locale.setDefault(Locale.ENGLISH);
		
		VBox box = new VBox();

		spinner = new ListSpinner<String>("a", "b", "c");
		box.getChildren().add(spinner);
		box.getChildren().add(new Button("focus helper"));
		

		// make sure that the listspinner fits with arrows in both directions 
		box.setPrefSize(100, 100);
		
		return box;
	}
	private ListSpinner<String> spinner = null;

	@Test
	public void _forSomeReasonTheFirstTestOftenFails()
	{		
		// non cyclic is the default
		spinner.cyclicProperty().set(true);
		click(".button"); // we need to move the focus first, otherwise the test will fail at random
		click(".right-arrow");
		click(".left-arrow");
	}


	@Test
	public void navigateUpDownThroughTheValuesCyclic()
	{
		// horizontal is the default
		TestUtil.runThenWaitForPaintPulse( () -> {
			// the CSS change is not processed correctly?
			//spinner.setStyle("-fxx-arrow-direction: " + ListSpinnerSkin.ArrowDirection.VERTICAL);
			((ListSpinnerSkin)spinner.getSkin()).arrowDirectionProperty().set(ListSpinnerSkin.ArrowDirection.VERTICAL);
		});

		// non cyclic is the default
		spinner.cyclicProperty().set(true);

		// check to see what the current value is
		Assert.assertEquals("a", spinner.getValue());

		// ----
		// move to next until we cycle over
		click(".button"); // we need to move the focus first, otherwise the test will fail at random
		
		// select next
		click(".up-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select next
		click(".up-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select next (cyclic)
		click(".up-arrow");
		Assert.assertEquals("a", spinner.getValue());

		// ----
		// now move backwards, cycling back

		// select prev (cyclic)
		click(".down-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select prev
		click(".down-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select prev
		click(".down-arrow");
		Assert.assertEquals("a", spinner.getValue());

		// select prev (cyclic)
		click(".down-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select prev
		click(".down-arrow");
		Assert.assertEquals("b", spinner.getValue());
	}

	@Test
	public void navigateUpDownThroughTheValuesNonCyclic()
	{
		// horizontal is the default
		TestUtil.runThenWaitForPaintPulse( () -> {
			// the CSS change is not processed correctly?
			//spinner.setStyle("-fxx-arrow-direction: " + ListSpinnerSkin.ArrowDirection.VERTICAL);
			((ListSpinnerSkin)spinner.getSkin()).arrowDirectionProperty().set(ListSpinnerSkin.ArrowDirection.VERTICAL);
		});

		// check to see what the current value is
		Assert.assertEquals("a", spinner.getValue());

		// ----
		// move forward, non cyclic
		click(".button"); // we need to move the focus first, otherwise the test will fail at random
		
		// select next
		click(".up-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select next
		click(".up-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select next (stick)
		click(".up-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// ----
		// move back, non cyclic

		// select prev
		click(".down-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select prev
		click(".down-arrow");
		Assert.assertEquals("a", spinner.getValue());

		// select prev (stick)
		click(".down-arrow");
		Assert.assertEquals("a", spinner.getValue());
	}

	@Test
	public void navigateLeftRightThroughTheValuesCyclic()
	{		
		// non cyclic is the default
		spinner.cyclicProperty().set(true);

		// check to see what the current value is
		Assert.assertEquals("a", spinner.getValue());

		// ----
		// move to next until we cycle over
		click(".button"); // we need to move the focus first, otherwise the test will fail at random

		// select next
		click(".right-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select next
		click(".right-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select next (cyclic)
		click(".right-arrow");
		Assert.assertEquals("a", spinner.getValue());

		// ----
		// now move backwards, cycling back

		// select prev (cyclic)
		click(".left-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select prev
		click(".left-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select prev
		click(".left-arrow");
		Assert.assertEquals("a", spinner.getValue());

		// select prev (cyclic)
		click(".left-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select prev
		click(".left-arrow");
		Assert.assertEquals("b", spinner.getValue());
	}

	@Test
	public void navigateLeftRightThroughTheValuesNonCyclic()
	{
		// check to see what the current value is
		Assert.assertEquals("a", spinner.getValue());

		click(".button"); // we need to move the focus first, otherwise the test will fail at random
		
		// ----
		// move forward, non cyclic

		// select next
		click(".right-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select next
		click(".right-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select next (stick)
		click(".right-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// ----
		// move back, non cyclic

		// select prev
		click(".left-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select prev
		click(".left-arrow");
		Assert.assertEquals("a", spinner.getValue());

		// select prev (stick)
		click(".left-arrow");
		Assert.assertEquals("a", spinner.getValue());
	}

	@Test
	public void navigateLeftAndHold()
	{
		// check to see what the current value is
		Assert.assertEquals("a", spinner.getValue());

		// we need to move the focus first, otherwise the test will fail at random
		click(".button"); 

		// ----
		
		// press and hold
		move(".right-arrow");
		press(MouseButton.PRIMARY);
		TestUtil.sleep(500);
		release(MouseButton.PRIMARY);
		
		// the value repeatedly changed and stopped at the end 
		Assert.assertEquals("c", spinner.getValue());

		// ----
		
		// press and hold
		move(".left-arrow");
		press(MouseButton.PRIMARY);
		TestUtil.sleep(500);
		release(MouseButton.PRIMARY);
		
		// the value repeatedly changed and stopped at the beginning 
		Assert.assertEquals("a", spinner.getValue());
	}
}
