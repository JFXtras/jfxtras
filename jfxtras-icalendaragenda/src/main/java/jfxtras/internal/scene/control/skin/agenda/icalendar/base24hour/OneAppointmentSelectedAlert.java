/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
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
package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour;

import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
        
        /*
         * Listener to delete selected appointments when delete key is pressed
         */
        getDialogPane().setOnKeyPressed((event) ->
        {
        	((Button) getDialogPane().lookupButton(deleteButtonType)).fire();
        });
    }
}
