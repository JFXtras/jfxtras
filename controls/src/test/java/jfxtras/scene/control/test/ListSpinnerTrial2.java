/**
 * ListSpinnerTrial2.java
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

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.scene.control.ListSpinner;
import jfxtras.scene.control.ListSpinnerIntegerList;
import jfxtras.util.StringConverterFactory;

public class ListSpinnerTrial2 extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage)
	{
		VBox lVBox = new VBox(20);
		
		final ListSpinnerIntegerList spinnerIntegerList = new ListSpinnerIntegerList(100, 1000, 100);
		final ListSpinner<Integer> segmentSpinner = new ListSpinner<Integer>(spinnerIntegerList);
		segmentSpinner.setValue(500);
		segmentSpinner.setEditable(true);
		segmentSpinner.setStringConverter(StringConverterFactory.forInteger());
		segmentSpinner.setStyle("-fxx-arrow-direction:VERTICAL;");
		segmentSpinner.setMaxWidth(60);
		segmentSpinner.addCallbackProperty().set(new Callback<Integer, Integer>()
		{
			@Override
			public Integer call(Integer integer)
			{
				if (integer < 100 || integer > 1000) return null;
				int l = integer; while (l > 100) l -= 100; l += 100;
				int u = integer; while (u < 1000) u += 100; u -= 100;
				ListSpinnerIntegerList spinnerIntegerList = new ListSpinnerIntegerList(l, u, 100);
				segmentSpinner.setItems(FXCollections.observableList(spinnerIntegerList));
				int i = spinnerIntegerList.indexOf(integer);
				return i;
			}
		});
		lVBox.getChildren().add(segmentSpinner);
		
		// just a focusable control
		lVBox.getChildren().add(new TextField());
		
		// create scene
        Scene scene = new Scene(lVBox, 800, 600);
        
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();
	}
}
