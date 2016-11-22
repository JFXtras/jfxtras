/**
 * CalendarPickerTrial.java
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

package jfxtras.scene.control.trial;

import java.util.GregorianCalendar;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import jfxtras.scene.control.CalendarTimePicker;

/**
 * 
 * @author Tom Eugelink
 *
 */
public class CalendarTimePickerTrial extends Application {
	
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {
		
		FlowPane lFlowPane = new FlowPane();
		
		{
			CalendarTimePicker lCalendarTimePicker = new CalendarTimePicker();
			lCalendarTimePicker.setCalendar(new GregorianCalendar(2013, 0, 1, 12, 30, 00));
//			lCalendarTimePicker.setHourStep(1);
			lCalendarTimePicker.setMinuteStep(15);
//			lCalendarTimePicker.setSecondStep(15);
			lCalendarTimePicker.setStyle("-fxx-label-dateformat:\"HH:mm:ss\";");
			lFlowPane.getChildren().add(lCalendarTimePicker);
		}
		
		Button lButton = new Button("dummy");
		lFlowPane.getChildren().add(lButton);
		
		// create scene
        Scene scene = new Scene(lFlowPane, 800, 800);
        
		// load custom CSS
        //scene.getStylesheets().addAll(this.getClass().getResource(this.getClass().getSimpleName() + ".css").toExternalForm());
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();
    }
}
