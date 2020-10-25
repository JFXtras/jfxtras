/**
 * Copyright (c) 2011-2020, JFXtras
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
 * DISCLAIMED. IN NO EVENT SHALL JFXRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.scene.control.trial;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import jfxtras.scene.control.AccordionPane;

public class AccordionDemo extends Application {

	public static void main(final String[] args) {
		Application.launch(AccordionDemo.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		AccordionPane lHarmonica = new AccordionPane();
		lHarmonica.addTab("test1", new Label("test 1"));
		lHarmonica.addTab("test2", createTree(5) );
		lHarmonica.addTab("test3", createTree(500));

		// show
		primaryStage.setScene(new Scene(lHarmonica, 1000, 500));
		primaryStage.sizeToScene();
		primaryStage.show();
	}
	
	private TreeView<String> createTree(int size) {
		TreeItem<String> rootItem = new TreeItem<String>("Tree " + size);
		rootItem.setExpanded(true);
		for (int i = 0; i < size; i++) {
			rootItem.getChildren().add(new TreeItem<String>("Item " + i));
		}
		TreeView<String> treeView = new TreeView<String>(rootItem);
		return treeView;
	}
}
