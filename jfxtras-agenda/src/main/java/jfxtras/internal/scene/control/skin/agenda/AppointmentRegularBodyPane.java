package jfxtras.internal.scene.control.skin.agenda;

import java.time.LocalDate;

import javafx.scene.text.Text;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class AppointmentRegularBodyPane extends AppointmentAbstractTrackedPane {

	public AppointmentRegularBodyPane(LocalDate localDate, Appointment appointment, LayoutHelp layoutHelp) {
		super(localDate, appointment, layoutHelp, Draggable.YES);
		
		// strings
		this.startAsString = layoutHelp.timeDateTimeFormatter.format(this.startDateTime);
		this.endAsString = layoutHelp.timeDateTimeFormatter.format(this.endDateTime);

		// add the duration as text
		Text lTimeText = new Text((firstPaneOfAppointment ? startAsString : "") + "-" + (lastPaneOfAppointment ? endAsString : ""));
		{
			lTimeText.getStyleClass().add("AppointmentTimeLabel");
			lTimeText.setX( layoutHelp.paddingProperty.get() );
			lTimeText.setY(lTimeText.prefHeight(0));
			layoutHelp.clip(lTimeText, widthProperty().subtract( layoutHelp.paddingProperty.get() ), heightProperty());
			getChildren().add(lTimeText);
		}
		
		// add summary
		Text lSummaryText = new Text(appointment.getSummary());
		{
			lSummaryText.getStyleClass().add("AppointmentLabel");
			lSummaryText.setX( layoutHelp.paddingProperty.get() );
			lSummaryText.setY( lTimeText.getY() + layoutHelp.textHeightProperty.get());
			lSummaryText.wrappingWidthProperty().bind(widthProperty().subtract( layoutHelp.paddingProperty.get() ));
			layoutHelp.clip(lSummaryText, widthProperty(), heightProperty().subtract( layoutHelp.paddingProperty.get() ));
			getChildren().add(lSummaryText);			
		}
		
		// add the menu header
		getChildren().add(new AppointmentMenu(this, appointment, layoutHelp));
		
		// add the duration dragger
		if (lastPaneOfAppointment) {
			getChildren().add(new DurationDragger(this, appointment, layoutHelp));
		}
	}
	private String startAsString;
	private String endAsString;
}
