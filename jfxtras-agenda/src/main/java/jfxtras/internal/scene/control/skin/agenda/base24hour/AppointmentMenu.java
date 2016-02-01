/**
 * AppointmentMenu.java
 *
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.internal.scene.control.skin.agenda.base24hour;

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

//TODO: internationalize the labels and tooltips
class AppointmentMenu extends Rectangle {

	/**
	 * 
	 * @param pane
	 * @param appointment
	 * @param layoutHelp
	 */
	AppointmentMenu(Pane pane, Appointment appointment, LayoutHelp layoutHelp) {
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
	void showMenu(MouseEvent mouseEvent) {
		// has the client done his own popup?
		Callback<Appointment, Void> lEditCallback = layoutHelp.skinnable.getEditAppointmentCallback();
		if (lEditCallback != null) {
			lEditCallback.call(appointment);
			return;
		}

		// only if not already showing
		if (popup != null && popup.isShowing()) {
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
		BorderPane lBorderPane = new BorderPane() {
			// As of 1.8.0_40 CSS files are added in the scope of a control, the popup does not fall under the control, so the stylesheet must be reapplied 
			// When JFxtras is based on 1.8.0_40+: @Override 
			public String getUserAgentStylesheet() {
				return layoutHelp.skinnable.getUserAgentStylesheet();
			}
		};
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
		if ((appointment.isWholeDay() != null && appointment.isWholeDay() == true) || appointment.getEndLocalDateTime() != null) {
			lVBox.getChildren().add(createWholedayCheckbox());
		}
		
		// summary
		lVBox.getChildren().add(new Text("Summary:"));
		lVBox.getChildren().add(createSummaryTextField());

		// location
		lVBox.getChildren().add(new Text("Location:"));
		lVBox.getChildren().add(createLocationTextField());

		// actions
		lVBox.getChildren().add(new Text("Actions:"));  
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
		startTextField.setLocalDateTime(appointment.getStartLocalDateTime());
		
		// event handling
		startTextField.localDateTimeProperty().addListener( (observable, oldValue, newValue) ->  {
			
			// remember
			LocalDateTime lOldStart = appointment.getStartLocalDateTime();

			// set
			appointment.setStartLocalDateTime(newValue);

			// update end date
			if (appointment.getEndLocalDateTime() != null) {
				
				// enddate = start + duration
				long lDurationInNano = appointment.getEndLocalDateTime().getNano() - lOldStart.getNano();
				LocalDateTime lEndLocalDateTime = appointment.getStartLocalDateTime().plusNanos(lDurationInNano);
				appointment.setEndLocalDateTime(lEndLocalDateTime);

				// update field
				endTextField.setLocalDateTime(appointment.getEndLocalDateTime());
			}

			// refresh is done upon popup close
			layoutHelp.callAppointmentChangedCallback(appointment);
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
		endTextField.setLocalDateTime(appointment.getEndLocalDateTime());
		endTextField.setVisible(appointment.getEndLocalDateTime() != null);

		endTextField.localDateTimeProperty().addListener( (observable, oldValue, newValue) ->  {
			appointment.setEndLocalDateTime(newValue);
			// refresh is done upon popup close
			layoutHelp.callAppointmentChangedCallback(appointment);
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
				appointment.setEndLocalDateTime(null);
			}
			else {
				LocalDateTime lEndTime = appointment.getStartLocalDateTime().plusMinutes(30);
				appointment.setEndLocalDateTime(lEndTime);
				endTextField.setLocalDateTime(appointment.getEndLocalDateTime());
			}
			endTextField.setVisible(appointment.getEndLocalDateTime() != null);
			// refresh is done upon popup close
			layoutHelp.callAppointmentChangedCallback(appointment);
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
			layoutHelp.callAppointmentChangedCallback(appointment);
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
			layoutHelp.callAppointmentChangedCallback(appointment);
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
			deleteImageViewButton = createActionButton("delete-icon", "Delete");
			deleteImageViewButton.setOnMouseClicked( (mouseEvent) -> {
				popup.hide();
				layoutHelp.skinnable.appointments().remove(appointment);
				// refresh is done via the collection events
			});
			lHBox.getChildren().add(deleteImageViewButton);
		}
		
		// action
		if (layoutHelp.skinnable.getActionCallback() != null)
		{
			actionImageViewButton = createActionButton("action-icon", "Action");
			actionImageViewButton.setOnMouseClicked( (mouseEvent) -> {
				popup.hide();
				layoutHelp.skinnable.getActionCallback().call(appointment);
				// any refresh is done via the collection events
			});
			lHBox.getChildren().add(actionImageViewButton);
		}
		
		return lHBox;
	}
	private ImageViewButton deleteImageViewButton = null;
	private ImageViewButton actionImageViewButton = null;

	private ImageViewButton createActionButton(String styleClass, String tooltipText) {
		ImageViewButton lImageViewButton = new ImageViewButton();
		lImageViewButton.getStyleClass().add(styleClass);
		lImageViewButton.setPickOnBounds(true);
		Tooltip.install(lImageViewButton, new Tooltip(tooltipText)); 
		return lImageViewButton;
	}

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
				layoutHelp.callAppointmentChangedCallback(appointment);
				popup.hide();
			});
		}
		return lAppointmentGroupGridPane;
	}
}