package jfxtras.internal.scene.control.skin.agenda;

import java.time.LocalDateTime;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.util.Callback;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.util.NodeUtil;

public class AppointmentMenu extends Rectangle {

	/**
	 * 
	 * @param pane
	 * @param appointment
	 * @param layoutHelp
	 */
	public AppointmentMenu(Pane pane, Appointment appointment, LayoutHelp layoutHelp) {
		this.pane = pane;
		this.appointment = appointment;
		this.layoutHelp = layoutHelp;
		
		// layout
		setX(NodeUtil.snapXY(layoutHelp.paddingProperty.get()));
		setY(NodeUtil.snapXY(layoutHelp.paddingProperty.get()));
		setWidth(6);
		setHeight(3);
		
		// style
		getStyleClass().add("MenuIcon");
		
		// mouse
		layoutHelp.setupMouseOverAsBusy(this);
		setupMouseClick();
	}
	final Pane pane;
	final Appointment appointment;
	final LayoutHelp layoutHelp;

	/**
	 * 
	 */
	private void setupMouseClick() {
		setOnMousePressed((mouseEvent) -> {
			mouseEvent.consume();
		});
		setOnMouseReleased((mouseEvent) -> {
			mouseEvent.consume();
		});
		setOnMouseClicked( (mouseEvent) -> {
			mouseEvent.consume();
			showMenu(mouseEvent);
		});
	}
	
	/**
	 * 
	 * @param mouseEvent
	 */
	private void showMenu(MouseEvent mouseEvent) {
		
		// has the client done his own popup?
		Callback<Appointment, Void> lEditCallback = layoutHelp.skinnable.getEditAppointmentCallback();
		if (lEditCallback != null) {
			lEditCallback.call(appointment);
			return;
		}
		
		// create popup
		popup = new Popup();
		popup.setAutoFix(true);
		popup.setAutoHide(true);
		popup.setHideOnEscape(true);
		popup.setOnHidden( (windowEvent) -> {
			layoutHelp.skin.setupAppointments();
		});

		// popup contents
		BorderPane lBorderPane = new BorderPane();
		lBorderPane.getStyleClass().add(layoutHelp.skinnable.getClass().getSimpleName() + "Popup");
		popup.getContent().add(lBorderPane);

		// close icon
		lBorderPane.setRight(createCloseIcon());
		
		// initial layout
		VBox lVBox = new VBox(layoutHelp.paddingProperty.get());
		lBorderPane.setCenter(lVBox);

		// start and end
		lVBox.getChildren().add(new Text("Time:"));
		lVBox.getChildren().add(createStartTextField());
		lVBox.getChildren().add(createEndTextField());

		// wholeday
		if ((appointment.isWholeDay() != null && appointment.isWholeDay() == true) || appointment.getEndDateTime() != null) {
			lVBox.getChildren().add(createWholedayCheckbox());
		}
		
		// summary
		lVBox.getChildren().add(new Text("Summary:"));
		lVBox.getChildren().add(createSummaryTextField());

		// location
		lVBox.getChildren().add(new Text("Location:"));
		lVBox.getChildren().add(createLocationTextField());

		// actions
		lVBox.getChildren().add(new Text("Actions:"));  // TBEERNOT: internationalize
		lVBox.getChildren().add(createActions());

		// appointment groups
		lVBox.getChildren().add(new Text("Group:"));
		lVBox.getChildren().add(createAppointmentGroups());
		
		// show it just below the menu icon
		popup.show(pane, NodeUtil.screenX(pane), NodeUtil.screenY(pane));
	}
	private Popup popup;
	
	/**
	 * 
	 * @param popup
	 * @return
	 */
	private ImageViewButton createCloseIcon() {
		closeIconImageView = new ImageViewButton();
		closeIconImageView.getStyleClass().add("close-icon");
		closeIconImageView.setPickOnBounds(true);
		closeIconImageView.setOnMouseClicked( (mouseEvent2) -> {
			popup.hide();
		});
		return closeIconImageView;
	}
	private ImageViewButton closeIconImageView = null;

	/**
	 * 
	 * @return
	 */
	private LocalDateTimeTextField createStartTextField() {
		startTextField = new LocalDateTimeTextField();
		startTextField.setLocale(layoutHelp.skinnable.getLocale());
		startTextField.setLocalDateTime(appointment.getStartDateTime());
		
		// event handling
		startTextField.localDateTimeProperty().addListener( (observable, oldValue, newValue) ->  {
			
			// remember
			LocalDateTime lOldStart = appointment.getStartDateTime();

			// set
			appointment.setStartDateTime(newValue);

			// update end date
			if (appointment.getEndDateTime() != null) {
				
				// enddate = start + duration
				long lDurationInNano = appointment.getEndDateTime().getNano() - lOldStart.getNano();
				LocalDateTime lEndLocalDateTime = appointment.getStartDateTime().plusNanos(lDurationInNano);
				appointment.setEndDateTime(lEndLocalDateTime);

				// update field
				endTextField.setLocalDateTime(appointment.getEndDateTime());
			}

			// refresh is done upon popup close
		});

		return startTextField;
	}
	private LocalDateTimeTextField startTextField = null;

	/**
	 * 
	 * @return
	 */
	private LocalDateTimeTextField createEndTextField() {
		endTextField = new LocalDateTimeTextField();
		endTextField.setLocale(layoutHelp.skinnable.getLocale());
		endTextField.setLocalDateTime(appointment.getEndDateTime());
		endTextField.setVisible(appointment.getEndDateTime() != null);

		endTextField.localDateTimeProperty().addListener( (observable, oldValue, newValue) ->  {
			appointment.setEndDateTime(newValue);
			// refresh is done upon popup close
		});

		return endTextField;
	}
	private LocalDateTimeTextField endTextField = null;

	/**
	 * 
	 * @return
	 */
	private CheckBox createWholedayCheckbox() {
		wholedayCheckBox = new CheckBox("Wholeday");
		wholedayCheckBox.setId("wholeday-checkbox");
		wholedayCheckBox.selectedProperty().set(appointment.isWholeDay());

		wholedayCheckBox.selectedProperty().addListener( (observable, oldValue, newValue) ->  {
			appointment.setWholeDay(newValue);
			if (newValue == true) {
				appointment.setEndDateTime(null);
			}
			else {
				LocalDateTime lEndTime = appointment.getStartDateTime().plusMinutes(30);
				appointment.setEndDateTime(lEndTime);
				endTextField.setLocalDateTime(appointment.getEndDateTime());
			}
			endTextField.setVisible(appointment.getEndDateTime() != null);
			// refresh is done upon popup close
		});
		
		return wholedayCheckBox;
	}
	private CheckBox wholedayCheckBox = null;

	/**
	 * 
	 * @return
	 */
	private TextField createSummaryTextField() {
		summaryTextField = new TextField();
		summaryTextField.setText(appointment.getSummary());
		summaryTextField.textProperty().addListener( (observable, oldValue, newValue) ->  {
			appointment.setSummary(newValue);
			// refresh is done upon popup close
		});
		return summaryTextField;
	}
	private TextField summaryTextField = null;

	/**
	 * 
	 * @return
	 */
	private TextField createLocationTextField() {
		locationTextField = new TextField();
		locationTextField.setText( appointment.getLocation() == null ? "" : appointment.getLocation());
		locationTextField.textProperty().addListener( (observable, oldValue, newValue) ->  {
			appointment.setLocation(newValue);
			// refresh is done upon popup close
		});
		return locationTextField;
	}
	private TextField locationTextField = null;

	/**
	 * 
	 * @return
	 */
	private HBox createActions() {
		HBox lHBox = new HBox();
		
		// delete
		{
			deleteIconImageView = new ImageViewButton();
			deleteIconImageView.getStyleClass().add("delete-icon");
			deleteIconImageView.setPickOnBounds(true);
			deleteIconImageView.setOnMouseClicked( (mouseEvent) -> {
				popup.hide();
				layoutHelp.skinnable.appointments().remove(appointment);
				// refresh is done via the collection events
			});
			Tooltip.install(deleteIconImageView, new Tooltip("Delete")); // TBEERNOT: internationalize
			lHBox.getChildren().add(deleteIconImageView);
		}
		return lHBox;
	}
	private ImageViewButton deleteIconImageView = null;

	/**
	 * 
	 * @return
	 */
	private GridPane createAppointmentGroups() {
		
		// construct a area of appointment groups
		GridPane lAppointmentGroupGridPane = new GridPane();
		lAppointmentGroupGridPane.getStyleClass().add("AppointmentGroups");
		lAppointmentGroupGridPane.setHgap(2);
		lAppointmentGroupGridPane.setVgap(2);
		
		int lCnt = 0;
		for (AppointmentGroup lAppointmentGroup : layoutHelp.skinnable.appointmentGroups())
		{
			// create the appointment group
			final Pane lPane = new Pane();
			lPane.setPrefSize(15, 15);
			lPane.getStyleClass().addAll("AppointmentGroup", lAppointmentGroup.getStyleClass());
			lAppointmentGroupGridPane.add(lPane, lCnt % 10, lCnt / 10 );
			lCnt++;

			// tooltip
			if (lAppointmentGroup.getDescription() != null) {
				Tooltip.install(lPane, new Tooltip(lAppointmentGroup.getDescription()));
			}

			// mouse 
			layoutHelp.setupMouseOverAsBusy(lPane);
			lPane.setOnMouseClicked( (mouseEvent) -> {
				mouseEvent.consume(); // consume before anything else, in case there is a problem in the handling

				// assign appointment group
				appointment.setAppointmentGroup(lAppointmentGroup);

				// refresh is done upon popup close
				popup.hide();
			});
		}
		return lAppointmentGroupGridPane;
	}
}