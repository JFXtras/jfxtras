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
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.scene.control.agenda.icalendar.trial;

import java.time.LocalDateTime;
import java.time.Period;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.layout.HBox;

/**
 * Simple Demo of {@link ICalendarAgenda} with weekly increase/decrease buttons
 * 
 * @author David Bal
 *
 */
public class ICalendarAgendaSimpleTrial2 extends Application
{
    public static void main(String[] args) {
        launch(args);       
    }

    @Override
    public void start(Stage primaryStage) {

        // setup control
        BorderPane root = new BorderPane();
        VCalendar vCalendar = new VCalendar(); // iCalendarFx model - contains calendaring information
        ICalendarAgenda agenda = new ICalendarAgenda(vCalendar); // Agenda - displays and edits the VCalendar information
        agenda.setOrganizer("ORGANIZER;CN=David Bal:mailto:david.bal37@gmail.com");
        root.setCenter(agenda);
        
        // weekly increase/decrease buttons
        Button increaseWeek = new Button(">");
        Button decreaseWeek = new Button("<");
        HBox buttonHBox = new HBox(decreaseWeek, increaseWeek);
        root.setTop(buttonHBox);
        
        // weekly increase/decrease event handlers
        increaseWeek.setOnAction((e) ->
        {
            LocalDateTime newDisplayedLocalDateTime = agenda.getDisplayedLocalDateTime().plus(Period.ofWeeks(1));
            agenda.setDisplayedLocalDateTime(newDisplayedLocalDateTime);
        });        
        decreaseWeek.setOnAction((e) ->
        {
            LocalDateTime newDisplayedLocalDateTime = agenda.getDisplayedLocalDateTime().minus(Period.ofWeeks(1));
            agenda.setDisplayedLocalDateTime(newDisplayedLocalDateTime);
        });
        
        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ICalendar Agenda Simple Demo");
        primaryStage.setOnCloseRequest(e -> System.out.println(vCalendar.toString())); // prints resulting VCALENDAR on close
        primaryStage.show();
    }
}
