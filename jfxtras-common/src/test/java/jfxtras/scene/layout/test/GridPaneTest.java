/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.scene.layout.test;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.junit.Test;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import jfxtras.scene.layout.GridPane;
import jfxtras.test.AssertNode;
import jfxtras.test.JFXtrasGuiTest;

/**
 * Created by Tom Eugelink on 26-12-13.
 */
public class GridPaneTest extends JFXtrasGuiTest {

	@Override
	public void start(Stage stage) throws Exception {
		javafx.scene.text.Font.loadFont(this.getClass().getResource("/" + "Roboto-Medium.ttf").toExternalForm(), 12);

		this.stage = stage;
		Scene scene = new Scene(getRootNode());
		stage.setScene(scene);
		stage.show();
	}
	protected Stage stage;
	/**
	 * 
	 */
	public Parent getRootNode()
	{
		stage.setWidth(800.0);
		stage.setHeight(600.0);

		gridPane = new GridPane()
			.withHGap(5)
			.withVGap(5)
			.withPadding(new Insets(10, 10, 10, 10))
			.withGridLinesVisible(true);
	
	    gridPane.add(newLabel("SingleCell"), new GridPane.C().col(1).row(0));
		gridPane.add(newLabel("RIGHT"), new GridPane.C().col(2).row(0).halignment(HPos.RIGHT));
	
		gridPane.add(newLabel("Span2Row\nSpan2Row\nSpan2Row"), new GridPane.C().col(0).row(0).colSpan(1).rowSpan(2));
	
	    gridPane.add(newLabel("Span2Columns Span2Columns"), new GridPane.C().col(1).row(1).colSpan(2).rowSpan(1));
	
		gridPane.add(newLabel("Single"), new GridPane.C().col(0).row(2));
		gridPane.add(newLabel("Span2Col2RowCenter\nSpan2Col2RowCenter\nSpan2Col2RowCenter\nSpan2Col2RowCenter\nSpan2Col2RowCenter"), new GridPane.C().col(1).row(2).colSpan(2).rowSpan(2).halignment(HPos.CENTER));
	
		gridPane.add(newLabel("BOTTOM"), new GridPane.C().col(0).row(3).valignment(VPos.BOTTOM));
	
	    gridPane.add(newLabel("TOP"), new GridPane.C().col(3).row(3).valignment(VPos.TOP));

		return gridPane;
	}
	private GridPane gridPane = null;

	/**
	 * 
	 */
	@Test
	public void checkPositions()
	{
		AssertNode.generateSource("gridPane", gridPane.getChildren(), null, false, AssertNode.A.XYWH, AssertNode.A.CLASS);
		new AssertNode(gridPane.getChildren().get(0)).assertXYWH(0.0, 0.0, 270.5, 151.5, 0.01).assertClass(javafx.scene.Group.class);
		new AssertNode(gridPane.getChildren().get(1)).assertXYWH(74.0, 10.0, 54.0, 17.0, 0.01).assertClass(javafx.scene.control.Label.class);
		new AssertNode(gridPane.getChildren().get(2)).assertXYWH(207.0, 10.0, 35.0, 17.0, 0.01).assertClass(javafx.scene.control.Label.class);
		new AssertNode(gridPane.getChildren().get(3)).assertXYWH(10.0, 10.0, 59.0, 51.0, 0.01).assertClass(javafx.scene.control.Label.class);
		new AssertNode(gridPane.getChildren().get(4)).assertXYWH(74.0, 38.0, 168.0, 17.0, 0.01).assertClass(javafx.scene.control.Label.class);
		new AssertNode(gridPane.getChildren().get(5)).assertXYWH(10.0, 66.0, 34.0, 17.0, 0.01).assertClass(javafx.scene.control.Label.class);
		new AssertNode(gridPane.getChildren().get(6)).assertXYWH(99.0, 66.0, 119.0, 85.0, 0.01).assertClass(javafx.scene.control.Label.class);
		new AssertNode(gridPane.getChildren().get(7)).assertXYWH(10.0, 134.0, 49.0, 17.0, 0.01).assertClass(javafx.scene.control.Label.class);
		new AssertNode(gridPane.getChildren().get(8)).assertXYWH(247.0, 88.0, 23.0, 17.0, 0.01).assertClass(javafx.scene.control.Label.class);
	}

	private Label newLabel(String s) {
		Label label = new Label(s);
		label.setFont(new Font("Roboto Medium", 12));
		return label;
	}
}
