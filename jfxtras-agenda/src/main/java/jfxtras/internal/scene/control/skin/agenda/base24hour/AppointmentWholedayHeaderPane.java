package jfxtras.internal.scene.control.skin.agenda.base24hour;

import javafx.scene.text.Text;
import jfxtras.scene.control.agenda.Agenda;

/**
 * Responsible for rendering a single whole day appointment on a day header.
 * 
 */
class AppointmentWholedayHeaderPane extends AppointmentAbstractPane
{
	/**
	 * 
	 * @param calendar
	 * @param appointment
	 */
	public AppointmentWholedayHeaderPane(Agenda.Appointment appointment, LayoutHelp layoutHelp)
	{
		super(appointment, layoutHelp);
		
		// add the duration as text
		getChildren().add(createSummaryText());
		
		// add the menu header
		getChildren().add(appointmentMenu);
	}

	private Text createSummaryText() {
		summaryText = new Text(appointment.getSummary());
		summaryText.getStyleClass().add("AppointmentLabel");
		summaryText.setX( layoutHelp.paddingProperty.get() );
		summaryText.setY(summaryText.prefHeight(0));
		layoutHelp.clip(summaryText, widthProperty().subtract( layoutHelp.paddingProperty.get() ), heightProperty());
		return summaryText;
	}
	private Text summaryText = null;
}