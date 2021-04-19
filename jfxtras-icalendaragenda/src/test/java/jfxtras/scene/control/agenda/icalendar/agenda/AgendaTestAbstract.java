/**
 * Copyright (c) 2011-2021, JFXtras
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
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.scene.control.agenda.icalendar.agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.test.JFXtrasGuiTest;

public abstract class AgendaTestAbstract extends JFXtrasGuiTest
{
    public final static List<AppointmentGroup> DEFAULT_APPOINTMENT_GROUPS = IntStream.range(0, 24)
            .mapToObj(i -> new Agenda.AppointmentGroupImpl()
                  .withStyleClass("group" + i)
                  .withDescription("group" + (i < 10 ? "0" : "") + i))
            .collect(Collectors.toList());
    
    final public static List<String> CATEGORIES = IntStream.range(0, 24)
            .mapToObj(i -> new String("group" + (i < 10 ? "0" : "") + i))
            .collect(Collectors.toList());
    
//    final protected Map<String, Agenda.AppointmentGroup> appointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
    public static LocalDateTime dateTimeStamp;
    private VCalendar calendar = new VCalendar();
    
    @Override
    public Parent getRootNode()
    {
        Locale.setDefault(Locale.ENGLISH);
        
        vbox = new VBox();

        // setup appointment groups       
        agenda = new ICalendarAgenda(calendar);
        agenda.setDisplayedLocalDateTime(LocalDate.of(2015, 11, 8).atStartOfDay());
//        agenda.setDisplayedLocalDateTime(LocalDate.of(2014, 1, 1).atStartOfDay());
        agenda.setPrefSize(1000, 800);
//        agenda.setPrefSize(600, 400);
        agenda.appointmentGroups().clear();
        agenda.appointmentGroups().addAll(DEFAULT_APPOINTMENT_GROUPS);
//        agenda.setOneAllThisAndFutureDialogCallback(EditChoiceDialog.EDIT_DIALOG_CALLBACK);
        
//        for (Agenda.AppointmentGroup lAppointmentGroup : agenda.appointmentGroups()) {
//            appointmentGroupMap.put(lAppointmentGroup.getDescription(), lAppointmentGroup);
//        }
        
        // accept new appointments
        agenda.newAppointmentCallbackProperty().set(new Callback<Agenda.LocalDateTimeRange, Agenda.Appointment>()
        {
            @Override
            public Agenda.Appointment call(ICalendarAgenda.LocalDateTimeRange dateTimeRange)
            {
                return new Agenda.AppointmentImplTemporal()
                        .withStartTemporal( dateTimeRange.getStartLocalDateTime().atZone(ZoneId.systemDefault()))
                        .withEndTemporal( dateTimeRange.getEndLocalDateTime().atZone(ZoneId.systemDefault()))
                        .withSummary("New")
                        .withDescription("")
                        .withAppointmentGroup(agenda.appointmentGroups().get(0));
//                Appointment appointment = new Agenda.AppointmentImplLocal()
//                        .withStartLocalDateTime( dateTimeRange.getStartLocalDateTime())
//                        .withEndLocalDateTime( dateTimeRange.getEndLocalDateTime())
//                        .withSummary("New")
//                        .withDescription("")
////                        .withAppointmentGroup(appointmentGroupMap.get("group00"));
//                        .withAppointmentGroup(agenda.appointmentGroups().get(0));
//                return appointment;
            }
        });
        
//        // override default UID generator callback 
//        agenda.setUidGeneratorCallback((Void) ->
//        {
//            String dateTime = DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(LocalDateTime.of(2015, 11, 8, 0, 0));
//            String domain = "jfxtras.org";
//            return dateTime + "-" + "0" + domain;
//        });
                
        vbox.getChildren().add(agenda);
        return vbox;
    }
    
    protected VBox vbox = null; // cannot make this final and assign upon construction
//    final protected Map<String, Agenda.AppointmentGroup> appointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
    protected ICalendarAgenda agenda = null; // cannot make this final and assign upon construction
}
