package jfxtras.internal.scene.control.skin.agenda.base24hour;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import jfxtras.internal.scene.control.skin.DateTimeToCalendarHelper;
import jfxtras.internal.scene.control.skin.agenda.AllAppointments;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.util.NodeUtil;

/**
 * Responsible for rendering the day header (whole day appointments).
 */
public class DayHeaderPane extends Pane {
	

	public DayHeaderPane(LocalDate localDate, AllAppointments allAppointments, LayoutHelp layoutHelp) {
		this.localDateObjectProperty.set(localDate);
		this.allAppointments = allAppointments;
		this.layoutHelp = layoutHelp;
		construct();
	}
	final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<LocalDate>(this, "localDate");
	final AllAppointments allAppointments;
	final LayoutHelp layoutHelp;
	
	private void construct() {
		
		// for debugging setStyle("-fx-border-color:PINK;-fx-border-width:4px;");
		getStyleClass().add("DayHeader");
		
		// set day label
		dayText = new Text("?");
		dayText.getStyleClass().add("DayLabel");
		dayText.setX( layoutHelp.paddingProperty.get() ); // align left
		dayText.setY( dayText.prefHeight(0) );
		getChildren().add(dayText);
		
		// for testing
		localDateObjectProperty.addListener( (observable) -> {
			setId("DayHeader" + localDateObjectProperty.get());
		});
		setId("DayHeader" + localDateObjectProperty.get());
		
		// clip the visible part
		Rectangle lClip = new Rectangle(0,0,0,0);
		lClip.widthProperty().bind(widthProperty().subtract(layoutHelp.paddingProperty.get()));
		lClip.heightProperty().bind(heightProperty());
		dayText.setClip(lClip);
		
		// react to changes in the calendar by updating the label
		localDateObjectProperty.addListener( (observable) -> {
			setLabel();
		});
		setLabel();
		
		// react to changes in the appointments
		allAppointments.addOnChangeListener( () -> {
			setupAppointments();
		});
		setupAppointments();
		
		// setup the create appointment
		setupMouse();
	}

	private void setLabel() {
		String lLabel = localDateObjectProperty.get().format(layoutHelp.dayOfWeekDateTimeFormatter)
				      + " " 
				      + localDateObjectProperty.get().format(layoutHelp.dateDateTimeFormatter)
				      ;
		dayText.setText(lLabel);
	}
	private Text dayText = new Text("?");
	
	/**
	 * 
	 */
	public void setupAppointments() {
		
		// remove all appointments
		getChildren().removeAll(appointmentHeaderPanes);
		appointmentHeaderPanes.clear();
		
		// for all wholeday appointments on this date, create a header appointment pane
		appointments.clear();
		appointments.addAll( allAppointments.collectWholedayFor(localDateObjectProperty.get()) );
		int lCnt = 0;
		for (Appointment lAppointment : appointments) {
			// create pane
			AppointmentWholedayHeaderPane lAppointmentHeaderPane = new AppointmentWholedayHeaderPane(lAppointment, layoutHelp);
			getChildren().add(lAppointmentHeaderPane);				
			appointmentHeaderPanes.add(lAppointmentHeaderPane);
			lAppointmentHeaderPane.setId(lAppointmentHeaderPane.getClass().getSimpleName() + localDateObjectProperty.get() + "/" + lCnt); // for testing
			
			// position by binding
			lAppointmentHeaderPane.layoutXProperty().bind(layoutHelp.wholedayAppointmentFlagpoleWidthProperty.multiply(lCnt)); // each pane is cascade offset to the right to allow connecting to the wholeday appointment on the day pane
			lAppointmentHeaderPane.layoutYProperty().bind(heightProperty().subtract(layoutHelp.appointmentHeaderPaneHeightProperty.multiply(appointments.size() - lCnt))); // each pane is cascaded offset down so the title label is visible 
			lAppointmentHeaderPane.prefWidthProperty().bind(widthProperty().subtract(layoutHelp.wholedayAppointmentFlagpoleWidthProperty.multiply(lCnt))); // make sure the size matches the cascading
			lAppointmentHeaderPane.prefHeightProperty().bind(heightProperty().subtract(lAppointmentHeaderPane.layoutYProperty())); // and the height reaches all the way to the bottom to connect to the flagpole
			
			lCnt++;
		}
	}
	final private List<Appointment> appointments = new ArrayList<>();
	final private List<AppointmentWholedayHeaderPane> appointmentHeaderPanes = new ArrayList<>();
	
	/**
	 * So the out view knows how much room (height) we need
	 * @return
	 */
	public int getNumberOfWholeDayAppointments() {
		return appointments.size();
	}
	
	/**
	 * 
	 */
	private void setupMouse() {
		
		// start new appointment
		setOnMousePressed((mouseEvent) -> {
			// only on primary
			if (mouseEvent.getButton().equals(MouseButton.PRIMARY) == false) {
				return;
			}
			// if there is no one to handle the result, don't even bother
			if (layoutHelp.skinnable.createAppointmentCallbackProperty().get() == null && layoutHelp.skinnable.newAppointmentCallbackProperty().get() == null) {
				return;
			}
			
			// no one else
			mouseEvent.consume();
			
			// calculate the starttime
			LocalDateTime lStartDateTime = localDateObjectProperty.get().atStartOfDay();
			LocalDateTime lEndDateTime = lStartDateTime.plusDays(1);
			
			// ask the control to create a new appointment (null may be returned)
			Agenda.Appointment lAppointment;
			if (layoutHelp.skinnable.newAppointmentCallbackProperty().get() != null) {
				lAppointment = layoutHelp.skinnable.newAppointmentCallbackProperty().get().call(new Agenda.LocalDateTimeRange(lStartDateTime, lEndDateTime));
			}
			else {
				lAppointment = layoutHelp.skinnable.createAppointmentCallbackProperty().get().call( new Agenda.CalendarRange(
					DateTimeToCalendarHelper.createCalendarFromLocalDateTime(lStartDateTime, TimeZone.getDefault(), Locale.getDefault()),
					DateTimeToCalendarHelper.createCalendarFromLocalDateTime(lEndDateTime, TimeZone.getDefault(), Locale.getDefault())
			    ));
			}
			if (lAppointment != null) {
				lAppointment.setWholeDay(true);
				layoutHelp.skinnable.appointments().add(lAppointment); // the appointments collection is listened to, so they will automatically be refreshed
			}
		});
	}


	
	/**
	 * 
	 * @param x scene coordinate
	 * @param y scene coordinate
	 * @return a localDateTime where nano seconds == 0
	 */
	LocalDateTime convertClickInSceneToDateTime(double x, double y) {
		Rectangle r = new Rectangle(NodeUtil.sceneX(this), NodeUtil.sceneY(this), this.getWidth(), this.getHeight());
		if (r.contains(x, y)) {
			LocalDate localDate = localDateObjectProperty.get();
			LocalDateTime localDateTime = localDate.atStartOfDay();
			localDateTime = localDateTime.withNano(AppointmentAbstractPane.DRAG_DAYHEADER); // we abuse the nano second to deviate body panes from header panes
			return localDateTime;
		}
		return null;
	}

}