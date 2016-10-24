package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour;

import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import jfxtras.scene.control.agenda.Agenda.Appointment;


/**
 * Alert with options to affect one selected appointment 
 * 
 * @author David Bal
 *
 */
public class OneAppointmentSelectedAlert extends Alert
{
    /**
     * 
     * @param appointment - selected Appointment
     * @param resources - for internationalization
     */
    public OneAppointmentSelectedAlert(Appointment appointment, ResourceBundle resources)
    {
        super(AlertType.CONFIRMATION);
        initModality(Modality.NONE);
        getDialogPane().getStyleClass().add("choice-dialog");

        // Buttons
        ButtonType editButtonType = new ButtonType(resources.getString("edit"));
        ButtonType deleteButtonType = new ButtonType(resources.getString("delete"));
        ButtonType cancelButtonType = ButtonType.CANCEL;
        getButtonTypes().setAll(editButtonType, deleteButtonType, cancelButtonType);
//        getDialogPane().lookupButton(editButtonType).setId("oneSelectedEditButton");
//        getDialogPane().lookupButton(deleteButtonType).setId("oneSelectedDeleteButton");
//        getDialogPane().lookupButton(ButtonType.CANCEL).setId("oneSelectedCancelButton");
        
        // set id
        getDialogPane().setId("newAppointmentDialog");
        getDialogPane().lookupButton(editButtonType).setId("OneAppointmentSelectedEditButton");
        getDialogPane().lookupButton(deleteButtonType).setId("OneAppointmentSelectedDeleteButton");
        getDialogPane().lookupButton(cancelButtonType).setId("OneAppointmentSelectedCancelButton"); // this id doesn't work with TestFx find
        
        // assign labels
        setTitle(resources.getString("alert.one.appointment.title"));
        String appointmentTime = AgendaDateTimeUtilities.formatRange(appointment.getStartTemporal(), appointment.getEndTemporal());
        setHeaderText(appointment.getSummary() + System.lineSeparator() + appointmentTime);
        setContentText(resources.getString("alert.one.appointment.content"));
    }
}
