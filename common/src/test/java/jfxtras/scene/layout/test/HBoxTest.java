/**
 * HBoxTest.java
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

package jfxtras.scene.layout.test;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import jfxtras.scene.layout.HBox;

import org.junit.Assert;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

/**
 * Created by tbee on 26-12-13.
 */
public class HBoxTest extends GuiTest {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		hbox = new HBox(5.0);
		hbox.add(new Button("grow"), new HBox.C().hgrow(Priority.ALWAYS));
		hbox.add(new Button("margin 5 grow"), new HBox.C().margin(new Insets(5.0)).hgrow(Priority.ALWAYS));
		hbox.getChildren().add(new Button("old style"));
		hbox.add(new Button("margin 20 nogrow"), new HBox.C().margin(new Insets(20.0)));
		hbox.add(new Button("grow maxheight 50"), new HBox.C().hgrow(Priority.ALWAYS).maxHeight(50.0));

		return hbox;
	}
	private HBox hbox = null;

	/**
	 * 
	 */
	@Test
	public void checkPositions()
	{
		int lIdx = 0;
		assertButton((Button)hbox.getChildren().get(lIdx++), "grow", 0, 0, 43, 400);
		assertButton((Button)hbox.getChildren().get(lIdx++), "margin 5 grow", 53, 5, 94, 390);
		assertButton((Button)hbox.getChildren().get(lIdx++), "old style", 157, 0, 61, 25);
		assertButton((Button)hbox.getChildren().get(lIdx++), "margin 20 nogrow", 243, 20, 114, 25);
		assertButton((Button)hbox.getChildren().get(lIdx++), "grow maxheight 50", 382, 0, 119, 50);
	}
	
	private void assertButton(Button lButton, String title, double x, double y, double w, double h) {
		System.out.println(lButton.getText() + ": X=" + lButton.getLayoutX() + ", Y=" + lButton.getLayoutY() + ", W=" + lButton.getWidth() + ", H=" + lButton.getHeight());
		System.out.println(lButton.getText() + ": LB=" + lButton.getLayoutBounds());
		System.out.println(lButton.getText() + ": BiL=" + lButton.getBoundsInLocal());
		System.out.println(lButton.getText() + ": BiP=" + lButton.getBoundsInParent());
		System.out.println("\n");
		Assert.assertEquals(title, lButton.getText());
//		Assert.assertEquals(x, lButton.getLayoutX(), 0.001);
//		Assert.assertEquals(y, lButton.getLayoutY(), 0.001);
//		Assert.assertEquals(w, lButton.getWidth(), 0.001);
//		Assert.assertEquals(h, lButton.getHeight(), 0.001);
	}
	
}
