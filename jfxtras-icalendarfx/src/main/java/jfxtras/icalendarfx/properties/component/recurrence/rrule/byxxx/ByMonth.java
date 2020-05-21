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
package jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx;
import static java.time.temporal.ChronoUnit.MONTHS;

import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRuleElement;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByRuleAbstract;

/** BYMONTH from RFC 5545, iCalendar 3.3.10, page 42 */
public class ByMonth extends ByRuleAbstract<Month, ByMonth>
{
    public void setValue(int... months)
    {
        Month[] monthArray = Arrays.stream(months)
                .mapToObj(i -> Month.of(i))
                .toArray(size -> new Month[size]);
        setValue(monthArray);
    }

    public ByMonth withValue(int... months)
    {
        setValue(months);
        return this;
    }
    
    /*
     * CONSTRUCTORS
     */
    public ByMonth()
    {
        super();
    }
    
    public ByMonth(int... months)
    {
        this();
        setValue(months);
    }
    
    public ByMonth(Month... months)
    {
        this();
        setValue(Arrays.asList(months));
    }
    
    public ByMonth(ByMonth source)
    {
        super(source);
    }

    @Override
    public String toString()
    {
        String days = getValue().stream()
                .map(d -> Integer.toString(d.getValue()))
                .collect(Collectors.joining(","));
        return RRuleElement.BY_MONTH + "=" + days;
    }

    @Override
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ChronoUnit chronoUnit, Temporal startTemporal)
    {
        switch (chronoUnit)
        {
        case HOURS:
        case MINUTES:
        case SECONDS:
        case DAYS:
        case WEEKS:
        case MONTHS:
            return inStream.filter(t ->
            { // filter out all but qualifying months
                Month myMonth = Month.from(t);
                for (Month month : getValue())
                {
                    if (month == myMonth) return true;
                }
                return false;
            });
        case YEARS:
            return inStream.flatMap(t -> 
            { // Expand to include matching all matching months
                List<Temporal> dates = new ArrayList<>();
                int monthNum = Month.from(t).getValue();
                for (Month month : getValue())
                {
                    int myMonthNum = month.getValue();
                    int monthShift = myMonthNum - monthNum;
                    Temporal newTemporal = t.plus(monthShift, MONTHS);
//                    if (! DateTimeUtilities.isBefore(newTemporal, startTemporal))
//                    {
                        dates.add(newTemporal);
//                    }
                }
                return dates.stream();
            });
        default:
            throw new RuntimeException("Not implemented ChronoUnit: " + chronoUnit); // probably same as DAILY
        }
    }
    
    @Override
    protected List<Message> parseContent(String content)
    {
    	String valueString = extractValue(content);
        Month[] monthArray = Arrays.asList(valueString.split(","))
                .stream()
                .map(s -> Month.of(Integer.parseInt(s)))
                .toArray(size -> new Month[size]);
        setValue(monthArray);
        return Collections.EMPTY_LIST;
    }
    
    public static ByMonth parse(String content)
    {
    	return ByMonth.parse(new ByMonth(), content);
    }
}
