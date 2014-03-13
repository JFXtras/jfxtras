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
import jfxtras.scene.layout.VBox;

import org.junit.Assert;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

/**
 * Created by Tom Eugelink on 26-12-13.
 */
public class VBoxTest extends GuiTest {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		vbox = new VBox(5.0);
		vbox.add(new Button("short"), new VBox.C().vgrow(Priority.ALWAYS));
		vbox.add(new Button("medium length"), new VBox.C().vgrow(Priority.ALWAYS));
		vbox.add(new Button("a longer description in order to test things"), new VBox.C().vgrow(Priority.ALWAYS));
		vbox.add(new Button("margin 5 grow"), new VBox.C().margin(new Insets(5.0)).vgrow(Priority.ALWAYS));
		vbox.getChildren().add(new Button("old style"));
		vbox.add(new Button("margin 20 nogrow"), new VBox.C().margin(new Insets(20.0)));
		vbox.add(new Button("grow maxwidth 150"), new VBox.C().vgrow(Priority.ALWAYS).maxWidth(150.0));

		return vbox;
	}
	private VBox vbox = null;

	/**
	 * 
	 */
	@Test
	public void checkPositions()
	{
		int lIdx = 0;
		assertButton((Button)vbox.getChildren().get(lIdx++), 0, 0, 240, 25);
		assertButton((Button)vbox.getChildren().get(lIdx++), 0, 30, 240, 25);
		assertButton((Button)vbox.getChildren().get(lIdx++), 0, 60, 240, 25);
		assertButton((Button)vbox.getChildren().get(lIdx++), 5, 95, 230, 25);
		assertButton((Button)vbox.getChildren().get(lIdx++), 0, 130, 61, 25);
		assertButton((Button)vbox.getChildren().get(lIdx++), 20, 180, 114, 25);
		assertButton((Button)vbox.getChildren().get(lIdx++), 0, 230, 150, 25);
	}
	
	private void assertButton(Button lButton, double x, double y, double w, double h) {
		System.out.println(lButton.getText() + ": X=" + lButton.getLayoutX() + ", Y=" + lButton.getLayoutY() + ", W=" + lButton.getWidth() + ", H=" + lButton.getHeight());
		Assert.assertEquals(x, lButton.getLayoutX(), 0.001);
		Assert.assertEquals(y, lButton.getLayoutY(), 0.001);
		Assert.assertEquals(w, lButton.getWidth(), 0.001);
		Assert.assertEquals(h, lButton.getHeight(), 0.001);
	}
	
}
