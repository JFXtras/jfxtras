package jfxtras.scene.control.agenda.test;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.test.JFXtrasGuiTest;

public class AbstractAgendaTest extends JFXtrasGuiTest {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		Locale.setDefault(Locale.ENGLISH);
		
		vbox = new VBox();

		agenda = new Agenda();
		agenda.setDisplayedLocalDateTime(LocalDate.of(2014, 1, 1).atStartOfDay());
		agenda.setPrefSize(1000, 800);
		
        // setup appointment groups
        for (int i = 0; i < 24; i++) {
        	appointmentGroupMap.put("group" + (i < 10 ? "0" : "") + i, new Agenda.AppointmentGroupImpl().withStyleClass("group" + i));
        }
        for (String lId : appointmentGroupMap.keySet())
        {
            Agenda.AppointmentGroup lAppointmentGroup = appointmentGroupMap.get(lId);
            lAppointmentGroup.setDescription(lId);
            agenda.appointmentGroups().add(lAppointmentGroup);
        }

        // accept new appointments
        agenda.newAppointmentCallbackProperty().set(new Callback<Agenda.LocalDateTimeRange, Agenda.Appointment>()
        {
            @Override
            public Agenda.Appointment call(Agenda.LocalDateTimeRange calendarRange)
            {
                return new Agenda.AppointmentImplLocal()
                        .withStartLocalDateTime(calendarRange.getStartLocalDateTime())
                        .withEndLocalDateTime(calendarRange.getEndLocalDateTime())
                        .withSummary("new")
                        .withDescription("new")
                        .withAppointmentGroup(appointmentGroupMap.get("group01"));
            }
        });
        
		vbox.getChildren().add(agenda);
		return vbox;
	}
	protected VBox vbox = new VBox();
    final protected Map<String, Agenda.AppointmentGroup> appointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
    protected Agenda agenda = null;

}
