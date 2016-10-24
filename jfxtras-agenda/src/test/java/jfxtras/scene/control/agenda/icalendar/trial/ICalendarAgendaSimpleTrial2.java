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
        ICalendarAgenda agenda = new ICalendarAgenda(vCalendar); // Agenda - displays the VCalendar information
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
        primaryStage.setOnCloseRequest(e -> System.out.println(vCalendar.toContent())); // prints resulting VCALENDAR on close
        primaryStage.show();
    }
}
