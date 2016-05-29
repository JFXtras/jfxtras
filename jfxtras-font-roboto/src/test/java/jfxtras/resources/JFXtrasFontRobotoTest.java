/**
 * JFXtrasFontRobotoTest.java
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

package jfxtras.resources;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jfxtras.test.AssertNode;
import jfxtras.test.AssertNode.A;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

public class JFXtrasFontRobotoTest extends JFXtrasGuiTest {
	/**
	 * 
	 */
	public Parent getRootNode()
	{
		Locale.setDefault(Locale.ENGLISH);
		box = new VBox();
		box.setPrefSize(300, 300);
		return box;
	}
	private VBox box;

	/**
	 * 
	 */
	@Test
	public void loadAll()
	{
		// load the fonts and set one on the label
		TestUtil.runThenWaitForPaintPulse( () -> {
			JFXtrasFontRoboto.loadAll();
			
	        Label l = new Label("xxxxx");
	        l.setStyle("-fx-font-family: 'Roboto Italic'; -fx-font-size:32pt;");
			box.getChildren().clear();
	        box.getChildren().add(l);
		});
		
		// assert the size
		generateXYWH();
		new AssertNode(find(".label")).assertXYWH(0.0, 0.0, 102.0, 59.0, 0.01);
		
		// set another on the label
		TestUtil.runThenWaitForPaintPulse( () -> {
			
	        Label l = new Label("xxxxx");
	        l.setStyle("-fx-font-family: 'Roboto Medium'; -fx-font-size:32pt;");
			box.getChildren().clear();
	        box.getChildren().add(l);
		});
		
		// assert the size
		generateXYWH();
		new AssertNode(find(".label")).assertXYWH(0.0, 0.0, 109.0, 59.0, 0.01);
	}
	
	// =============================================================================================================================================================================================================================
	// SUPPORT

	List<String> EXCLUDED_CLASSES = java.util.Arrays.asList();
	
	private void generateXYWH() {
		Node node = find(".label");
		AssertNode.generateSource("find(\".label\")", node, EXCLUDED_CLASSES, false, A.XYWH);
	}
}
