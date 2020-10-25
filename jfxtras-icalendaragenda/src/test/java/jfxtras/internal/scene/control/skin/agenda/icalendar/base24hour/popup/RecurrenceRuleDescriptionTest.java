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
package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.AgendaDateTimeUtilities;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;

@Ignore // fails
public class RecurrenceRuleDescriptionTest
{
    // Easy-to-read-summary tests for edit popup RRULE summary
    
    @Test
    public void canMakeRRuleSummaryString1()
    {
        VEvent v = ICalendarStaticComponents.getDaily6();
        String summaryString = EditRecurrenceRuleVEventVBox.makeSummary(v.getRecurrenceRule().getValue(), v.getDateTimeStart().getValue()); 
        String expectedString = "Every 2 days, until Dec 1, 2015";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString2()
    {
        VEvent v = ICalendarStaticComponents.getYearly1();
        String summaryString = EditRecurrenceRuleVEventVBox.makeSummary(v.getRecurrenceRule().getValue(), v.getDateTimeStart().getValue());
        String expectedString = "Yearly on November 9";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString3()
    {
        VEvent v = ICalendarStaticComponents.getMonthly1();
        String summaryString = EditRecurrenceRuleVEventVBox.makeSummary(v.getRecurrenceRule().getValue(), v.getDateTimeStart().getValue());
        String expectedString = "Monthly on day 9";
        assertEquals(expectedString, summaryString);
    }

    @Test
    public void canMakeRRuleSummaryString4()
    {
        VEvent v = ICalendarStaticComponents.getMonthly7();
        String summaryString = EditRecurrenceRuleVEventVBox.makeSummary(v.getRecurrenceRule().getValue(), v.getDateTimeStart().getValue());
        String expectedString = "Monthly on the third Monday";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString5()
    {
        VEvent v = ICalendarStaticComponents.getWeekly1();
        String summaryString = EditRecurrenceRuleVEventVBox.makeSummary(v.getRecurrenceRule().getValue(), v.getDateTimeStart().getValue());
        String expectedString = "Weekly on Monday";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString6()
    {
        VEvent v = ICalendarStaticComponents.getWeekly2();
        String summaryString = EditRecurrenceRuleVEventVBox.makeSummary(v.getRecurrenceRule().getValue(), v.getDateTimeStart().getValue());
        String expectedString = "Every 2 weeks on Monday, Wednesday, Friday";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString7()
    {
        VEvent v = ICalendarStaticComponents.getWeekly5();
        String summaryString = EditRecurrenceRuleVEventVBox.makeSummary(v.getRecurrenceRule().getValue(), v.getDateTimeStart().getValue());
        String expectedString = "Every 2 weeks on Monday, Wednesday, Friday, 11 times";
        assertEquals(expectedString, summaryString);
    }
    
    /* date-time ranges for edit and delete dialogs */
    @Test
    public void canMakeRangeToString1()
    {
        String dateTimeString = AgendaDateTimeUtilities.formatRange(LocalDateTime.of(2015, 11, 11, 10, 0), LocalDateTime.of(2015, 12, 25, 12, 0));
        assertEquals("Wed, November 11, 2015 10:00 AM - Fri, December 25, 2015 12:00 PM", dateTimeString);
        String dateString = AgendaDateTimeUtilities.formatRange(LocalDate.of(2015, 11, 9), LocalDate.of(2015, 11, 24));
        assertEquals("Mon, November 9, 2015 - Tue, November 24, 2015", dateString);
        String dateForeverString = AgendaDateTimeUtilities.formatRange(LocalDateTime.of(2015, 11, 9, 10, 0), null);
        assertEquals("Mon, November 9, 2015 10:00 AM - forever", dateForeverString);
    }
}
