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
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.scene.control.agenda.icalendar.factories;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.Optional;

import jfxtras.icalendarfx.components.VDisplayable;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VLocatable;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.Agenda.AppointmentImplTemporal;

/**
 * Default factory to create {@link AppointmentImplTemporal} for {@link Agenda}
 * 
 * @author David Bal
 *
 */
public class DefaultRecurrenceFactory extends RecurrenceFactory<Appointment>
{
    private Collection<AppointmentGroup> appointmentGroups;
            
    public DefaultRecurrenceFactory(Collection<AppointmentGroup> appointmentGroups)
    {
        super();
        this.appointmentGroups = appointmentGroups;
    }

    /** Make {@link AppointmentImplTemporal} from {@link VDisplayable} */
    @Override
    Appointment makeRecurrence(VDisplayable<?> vComponent, Temporal startTemporal)
    {
        Boolean isWholeDay = vComponent.getDateTimeStart().getValue() instanceof LocalDate;
        final String description;
        final Temporal endTemporal;
        final String location;
        if (vComponent instanceof VLocatable<?>)
        { // VTODO and VEVENT
            VLocatable<?> VComponentLocatableBase = (VLocatable<?>) vComponent;
            description = (VComponentLocatableBase.getDescription() != null) ? VComponentLocatableBase.getDescription().getValue() : null;
            location = (VComponentLocatableBase.getLocation() != null) ? VComponentLocatableBase.getLocation().getValue() : null;
            TemporalAmount adjustment = VComponentLocatableBase.getActualDuration();
            endTemporal = startTemporal.plus(adjustment);
        } else if (vComponent instanceof VJournal)
        {
            VJournal vJournal = (VJournal) vComponent;
            description = (vJournal.getDescriptions() != null) ? vJournal.getDescriptions().get(0).getValue() : null;
            location = null;
            endTemporal = null;
        } else
        {
            throw new RuntimeException("Unsupported VComponent type:" + vComponent.getClass());
        }

        /* Find AppointmentGroup (Agenda's version of CATEGORY)
         * control can only handle one category.  Checks only first category
         */
        final AppointmentGroup appointmentGroup;
        if (vComponent.getCategories() != null)
        {
            String firstCategory = vComponent.getCategories().get(0).getValue().get(0);
            Optional<AppointmentGroup> myGroup = appointmentGroups
                    .stream()
                    .filter(g -> g.getDescription().equals(firstCategory))
                    .findAny();
            appointmentGroup = (myGroup.isPresent()) ? myGroup.get() : null;
        } else
        {
            appointmentGroup = null;
        }
        
        // Make appointment
        Appointment appt = new Agenda.AppointmentImplTemporal()
                .withStartTemporal(startTemporal)
                .withEndTemporal(endTemporal)
                .withDescription(description)
                .withSummary( (vComponent.getSummary() != null) ? vComponent.getSummary().getValue() : null)
                .withLocation(location)
                .withWholeDay(isWholeDay)
                .withAppointmentGroup(appointmentGroup);
        return appt;
    }

}
