package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.trial;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.stage.Stage;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.Settings;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.EditDisplayableScene;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.SimpleEditSceneFactory;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.control.agenda.icalendar.factories.DefaultRecurrenceFactory;
import jfxtras.scene.control.agenda.icalendar.factories.RecurrenceFactory;
import jfxtras.scene.control.agenda.icalendar.test.ICalendarStaticComponents;
import jfxtras.scene.control.agenda.icalendar.test.agenda.AgendaTestAbstract;

/**
 * Demo of edit VEvent popup
 * 
 * @author David Bal
 *
 */
public class EditComponentPopupTrial extends Application
{
    
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage primaryStage)
	{
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.ICalendarAgenda", Locale.getDefault());
        Settings.setup(resources);

        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        myCalendar.addVComponent(vevent);
        
        RecurrenceFactory<Appointment> recurrenceFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS); // default VComponent store - for Appointments, if other implementation used make new store
        recurrenceFactory.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceFactory.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));
        List<Appointment> newAppointments = recurrenceFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(2);
        
        List<String> categories = IntStream.range(0, 24)
              .mapToObj(i -> new String("group" + (i < 10 ? "0" : "") + i))
              .collect(Collectors.toList());
        Object[] params = new Object[] {
                myCalendar,
                appointment.getStartTemporal(),
                appointment.getEndTemporal(),
                categories
                };
        EditDisplayableScene scene = SimpleEditSceneFactory.newScene(vevent, params);

        String agendaSheet = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
        scene.getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET, agendaSheet);

        primaryStage.setScene(scene);
        primaryStage.setTitle("ICalendar Edit Popup Demo");
        primaryStage.show();
        
        scene.getEditDisplayableTabPane().iTIPMessagesProperty().addListener((obs, oldValue, newValue) ->
        {
            newValue.forEach(message -> myCalendar.processITIPMessage(message));
            myCalendar.getVEvents().stream().forEach(System.out::println);
            primaryStage.hide();
        });
    }
}
