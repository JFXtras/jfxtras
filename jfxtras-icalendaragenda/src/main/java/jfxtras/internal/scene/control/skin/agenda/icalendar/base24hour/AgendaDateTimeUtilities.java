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
