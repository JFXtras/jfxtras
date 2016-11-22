package jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BySecond extends ByRuleIntegerAbstract<BySecond>
{
    public BySecond()
    {
        super();
    }
    
    public BySecond(Integer... minutes)
    {
        super(minutes);
    }
    
    public BySecond(BySecond source)
    {
        super(source);
    }
    
    @Override
    Predicate<Integer> isValidValue()
    {
        return (value) -> (value >= 0) && (value <= 59);
    }
    
    @Override
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ChronoUnit chronoUnit, Temporal dateTimeStart)
    {
        if (dateTimeStart.isSupported(ChronoField.SECOND_OF_MINUTE))
        {
            switch (chronoUnit)
            {
            case SECONDS:
                return inStream.filter(d ->
                        { // filter out all but qualifying hours
                            int mySecondOfMinute = d.get(ChronoField.SECOND_OF_MINUTE);
                            for (int secondOfMinute : getValue())
                            {
                                if (secondOfMinute > 0)
                                {
                                    if (secondOfMinute == mySecondOfMinute) return true;
                                }
                            }
                            return false;
                        });
            case HOURS:
            case MINUTES:
            case DAYS:
            case WEEKS:
            case MONTHS:
            case YEARS:
                return inStream.flatMap(d -> 
                { // Expand to be include all hours of day
                    List<Temporal> dates = new ArrayList<>();
                    for (int minuteOfHour : getValue())
                    {
                        Temporal newTemporal = d.with(ChronoField.SECOND_OF_MINUTE, minuteOfHour);
//                        if (! DateTimeUtilities.isBefore(newTemporal, dateTimeStart))
//                        {
                            dates.add(newTemporal);
//                        }
                    }
                    return dates.stream();
                });
            default:
                throw new IllegalArgumentException("Not implemented: " + chronoUnit);
            }
        } else
        {
            return inStream; // ignore rule when not supported (RFC 5545 requirement)
        }
    }

    public static ByMinute parse(String content)
    {
        ByMinute element = new ByMinute();
        element.parseContent(content);
        return element;
    }
}
