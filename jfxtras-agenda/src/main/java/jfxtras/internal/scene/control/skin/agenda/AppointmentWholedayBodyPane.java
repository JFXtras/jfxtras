package jfxtras.internal.scene.control.skin.agenda;

import java.time.LocalDate;

import jfxtras.scene.control.agenda.Agenda;

/**
 * Responsible for rendering a wholeday appointment on a single day.
 * 
 */
class AppointmentWholedayBodyPane extends AppointmentAbstractPane {
	
	/**
	 * 
	 * @param calendar
	 * @param appointment
	 */
	public AppointmentWholedayBodyPane(LocalDate localDate, Agenda.Appointment appointment, LayoutHelp layoutHelp) {
		super(appointment, layoutHelp, Draggable.YES);
	}
}

