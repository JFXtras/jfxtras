package jfxtras.internal.scene.control.skin.agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import jfxtras.internal.scene.control.skin.DateTimeToCalendarHelper;
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
		
		// change the layout related to the size
		widthProperty().addListener( (observable) -> {
			relayout();
		});
		heightProperty().addListener( (observable) -> {
			relayout();
		});
		
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
	public void relayout() {
		// position headers:
		// - we know our own height
		// - we know the height each AppointmentHeaderPar is allowed to have
		// - so the start position is calculated from the bottom
		double lY = getHeight() - (appointmentHeaderPanes.size() * layoutHelp.appointmentHeaderPaneHeightProperty.get()); // to make sure the appointments are renders aligned bottom
		for (AppointmentWholedayHeaderPane lAppointmentHeaderPane : appointmentHeaderPanes) {
			int lIdx = appointmentHeaderPanes.indexOf(lAppointmentHeaderPane);
			lAppointmentHeaderPane.setLayoutX(lIdx * layoutHelp.wholedayAppointmentFlagpoleWidthProperty.get()); // each pane is cascade offset to the right to allow connecting to the wholeday appointment on the day pane 
			lAppointmentHeaderPane.setLayoutY(lY); // each pane is cascaded offset down so the title label is visible 
			lAppointmentHeaderPane.setPrefWidth( getWidth() - (lIdx * layoutHelp.wholedayAppointmentFlagpoleWidthProperty.get()) ); // make sure the size matches the cascading
			lAppointmentHeaderPane.setPrefHeight( getHeight() - lY ); // and the height reaches all the way to the bottom to connect to the flagpole 
			lY += layoutHelp.appointmentHeaderPaneHeightProperty.get();
		}
	}
	
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
			AppointmentWholedayHeaderPane lAppointmentHeaderPane = new AppointmentWholedayHeaderPane(lAppointment, layoutHelp);
			getChildren().add(lAppointmentHeaderPane);				
			appointmentHeaderPanes.add(lAppointmentHeaderPane);
			lAppointmentHeaderPane.setId(lAppointmentHeaderPane.getClass().getSimpleName() + (++lCnt)); // for testing
		}
		
		// and layout
		relayout();
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
				lAppointment = layoutHelp.skinnable.newAppointmentCallbackProperty().get().call(new Agenda.DateTimeRange(lStartDateTime, lEndDateTime));
			}
			else {
				lAppointment = layoutHelp.skinnable.createAppointmentCallbackProperty().get().call(new Agenda.CalendarRange(DateTimeToCalendarHelper.createCalendarFromLocalDateTime(lStartDateTime, Locale.getDefault()), DateTimeToCalendarHelper.createCalendarFromLocalDateTime(lEndDateTime, Locale.getDefault())));
			}
			if (lAppointment != null) {
				lAppointment.setWholeDay(true);
				layoutHelp.skinnable.appointments().add(lAppointment); // the appointments collection is listened to, so they will automatically be refreshed
			}
		});
	}


	
	/**
	 * 
	 * @param x screen coordinate
	 * @param y screen coordinate
	 * @return a localDateTime where nano seconds == 0
	 */
	LocalDateTime convertClickToDateTime(double x, double y) {
		Rectangle r = new Rectangle(NodeUtil.screenX(this), NodeUtil.screenY(this), this.getWidth(), this.getHeight());
		if (r.contains(x, y)) {
			LocalDate localDate = localDateObjectProperty.get();
			LocalDateTime localDateTime = localDate.atStartOfDay();
			localDateTime = localDateTime.withNano(AppointmentAbstractPane.DRAG_DAYHEADER); // we abuse the nano second to deviate body panes from header panes
			return localDateTime;
		}
		return null;
	}

}