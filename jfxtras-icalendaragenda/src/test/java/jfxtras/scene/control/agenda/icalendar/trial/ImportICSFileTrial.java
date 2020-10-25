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
package jfxtras.scene.control.agenda.icalendar.trial;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.properties.calendar.Method.MethodType;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.layout.HBox;

/**
 * Demo that imports an ics file
 * 
 * @author David Bal
 *
 */
public class ImportICSFileTrial extends Application
{        
    public static void main(String[] args) {
        launch(args);       
    }

    @Override
    public void start(Stage primaryStage) {
        // setup VCalendar - holds all calendaring information (i.e events)
        VCalendar mainVCalendar = new VCalendar()
                .withProductIdentifier(ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER)
                .withVersion(); // uses default VERSION 2.0

        // setup controls
        BorderPane root = new BorderPane();
        ICalendarAgenda agenda = new ICalendarAgenda(mainVCalendar); // Agenda - displays the VCalendar information
        root.setCenter(agenda);
        
        // Buttons
        Button increaseWeek = new Button(">");
        Button decreaseWeek = new Button("<");
        HBox weekButtonHBox = new HBox(decreaseWeek, increaseWeek);
        Button importButton = new Button("Import an ics file");
        Button exportButton = new Button("Export an ics file");
        HBox buttonHBox = new HBox(weekButtonHBox, importButton, exportButton);
        buttonHBox.setSpacing(10);
        root.setTop(buttonHBox);
        
        // weekly increase/decrease event handlers
        increaseWeek.setOnAction(e ->
        {
            LocalDateTime newDisplayedLocalDateTime = agenda.getDisplayedLocalDateTime().plus(Period.ofWeeks(1));
            agenda.setDisplayedLocalDateTime(newDisplayedLocalDateTime);
        });        
        decreaseWeek.setOnAction(e ->
        {
            LocalDateTime newDisplayedLocalDateTime = agenda.getDisplayedLocalDateTime().minus(Period.ofWeeks(1));
            agenda.setDisplayedLocalDateTime(newDisplayedLocalDateTime);
        });
        
        // import ics event handler
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("ics files", "*.ics"));
        importButton.setOnAction(e ->
        {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null && file.toString().lastIndexOf("ics") > 0)
            {
                try
                {
                    // process iTIP and log exceptions
                    final List<String> log = new ArrayList<>();
                    VCalendar iTIPMessage = VCalendar.parseICalendarFile(file.toPath());
                    Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> log.add(exception.getMessage()));
                    List<String> messageLog = mainVCalendar.processITIPMessage(iTIPMessage);
                    log.addAll(messageLog);
//                    log.forEach(System.out::println);
                } catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            } else
            {
                throw new IllegalArgumentException("Invalid file:" + file + ". Select a valid ics file.");
            }
        });
        
        // export ics event handler
        exportButton.setOnAction(e ->
        {
            VCalendar publishMessage = new VCalendar()
                    .withMethod(MethodType.PUBLISH);
            mainVCalendar.copyChildrenInto(publishMessage);
            File file = fileChooser.showSaveDialog(primaryStage);
            BufferedWriter writer = null;
            try
            {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(publishMessage.toString());
                writer.close();
            } catch ( IOException e2)
            {
                e2.printStackTrace();
            }
        });
        
        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ICalendar Agenda Simple Demo");
        primaryStage.show();
    }
}