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
 * DISCLAIMED. IN NO EVENT SHALL JFXRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

import javafx.util.Pair;
import jfxtras.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

/**
 * Date and Time utilities
 * 
 * @author David Bal
 *
 */
public final class AgendaDateTimeUtilities
{
    private AgendaDateTimeUtilities() {}
    /**
     * Format start and end of recurrence date or date/time to concisely describe range
     * 
     * @param start - start of recurrence
     * @param end - end of recurrence
     * @return - formatted date or date/time string
     */
    public static String formatRange(Temporal start, Temporal end)
    {
        DateTimeFormatter startFormatter = (DateTimeType.of(start) == DateTimeType.DATE) ? Settings.DATE_FORMAT : Settings.DATE_TIME_FORMAT;
        final String startString = startFormatter.format(start);
        final String endString;
        if (end != null)
        {
            Period days = Period.between(LocalDate.from(start), LocalDate.from(end));
            if (start == end)
            {
                endString = "";
            } else if (days.isZero() && end.isSupported(ChronoUnit.NANOS)) // same day
            {
                endString = " - " + Settings.TIME_FORMAT_END.format(end);
            } else
            {
                DateTimeFormatter endFormatterDifferentDay = (DateTimeType.of(start) == DateTimeType.DATE) ? Settings.DATE_FORMAT : Settings.DATE_TIME_FORMAT;
                endString = " - " + endFormatterDifferentDay.format(end);            
            }
        } else
        {
            endString = (Settings.resources == null) ? " - forever" : " - " + Settings.resources.getString("forever");
        }
        return startString + endString;
    }
    
    /**
     * Format start and end of recurrence date or date/time to concisely describe range
     * 
     * @param range - a {@link Pair} representing start and end of a recurrence
     * @return - formatted date or date/time string
     */
    public static String formatRange(Pair<Temporal,Temporal> range)
    {
        Temporal start = range.getKey();
        Temporal end = range.getValue();
        return formatRange(start, end);
    }
}
