/**
 * GridPaneTrial1.java
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

package jfxtras.scene.layout.trial;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import jfxtras.scene.layout.GridPane;

public class GridPaneTrial1 extends Application 
{
	
    public static void main(String[] args) 
    {
        launch(args);       
    }

	@Override
	public void start(Stage stage) 
	{
		GridPane gridPane = new GridPane()
			.withHGap(5)
			.withVGap(5)
			.withPadding(new Insets(10, 10, 10, 10))
			.withGridLinesVisible(true);

	    gridPane.add(new Label("SingleCell"), new GridPane.C().col(1).row(0));
		gridPane.add(new Label("RIGHT"), new GridPane.C().col(2).row(0).halignment(HPos.RIGHT));

		gridPane.add(new Label("Span2Row\nSpan2Row\nSpan2Row"), new GridPane.C().col(0).row(0).colSpan(1).rowSpan(2));

	    gridPane.add(new Label("Span2Columns Span2Columns"), new GridPane.C().col(1).row(1).colSpan(2).rowSpan(1));

		gridPane.add(new Label("Single"), new GridPane.C().col(0).row(2));
		gridPane.add(new Label("Span2Col2RowCenter\nSpan2Col2RowCenter\nSpan2Col2RowCenter\nSpan2Col2RowCenter\nSpan2Col2RowCenter"), new GridPane.C().col(1).row(2).colSpan(2).rowSpan(2).halignment(HPos.CENTER));

		gridPane.add(new Label("BOTTOM"), new GridPane.C().col(0).row(3).valignment(VPos.BOTTOM));

	    gridPane.add(new Label("TOP"), new GridPane.C().col(3).row(3).valignment(VPos.TOP));
	    
        // setup scene
		Scene scene = new Scene(gridPane, 800, 200);
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
	}

}
