package jfxtras.internal.scene.control.skin.agenda.base24hour;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class AppointmentTaskBodyPane extends AppointmentAbstractTrackedPane {

	public AppointmentTaskBodyPane(Appointment appointment, LayoutHelp layoutHelp) {
		super(appointment.getStartLocalDateTime().toLocalDate(), appointment, layoutHelp);
		
		// strings
		this.startAsString = layoutHelp.timeDateTimeFormatter.format(this.startDateTime);

		// add the menu
		appointmentMenu.yProperty().bind(heightProperty().subtract(appointmentMenu.heightProperty()).divide(2.0)); // position is slightly different from the default
		getChildren().add(appointmentMenu);

		// add the start time as text
		getChildren().add(createTimeText());
		
		// add summary
		if (appointment.getSummary() != null) {
			getChildren().add(createSummaryText());
		}
	}
	private String startAsString;

	/**
	 * 
	 */
	private Text createTimeText() {
		timeText = new Text(startAsString);
		{
			timeText.getStyleClass().add("AppointmentTimeLabel");
			timeText.xProperty().bind( appointmentMenu.widthProperty().add(layoutHelp.paddingProperty).add(layoutHelp.paddingProperty) ); // directly next to the menu
			timeText.yProperty().bind( heightProperty().multiply(-0.5).add(timeText.prefHeight(0) / 2) );
		}
		return timeText;
	}
	private Text timeText = null;
	
	/**
	 * 
	 */
	private Text createSummaryText() {
		summaryText = new Text(appointment.getSummary().replace("\n", ""));
		summaryText.setWrappingWidth(0);
		summaryText.getStyleClass().add("AppointmentLabel");
		summaryText.xProperty().bind( timeText.xProperty().add(timeText.prefWidth(0.0)) ); // directly next to the timetext
		summaryText.yProperty().bind( timeText.yProperty() );
		summaryText.wrappingWidthProperty().bind(widthProperty().subtract( layoutHelp.paddingProperty.get() ));
		
		// place a clip over the text to only show a single line
		Rectangle lClip = new Rectangle(0,0,0,0);
		lClip.xProperty().bind(summaryText.xProperty());
		lClip.yProperty().bind(summaryText.yProperty().multiply(-1.0));
		lClip.widthProperty().bind(widthProperty().subtract(summaryText.yProperty()).subtract(40.0)); // no clue why I need to subtract an additional 40 pixels here
		lClip.heightProperty().set(timeText.prefHeight(0.0));
		summaryText.setClip(lClip);
		
		return summaryText;
	}
	private Text summaryText = null;
}
