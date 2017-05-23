package jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.time.DayOfWeek;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.WeekStart;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByRuleIntegerAbstract;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

/** 
 * By Week Number
 * BYWEEKNO
 * RFC 5545, iCalendar 3.3.10, page 42
 * 
 * The BYWEEKNO rule part specifies a COMMA-separated list of
      ordinals specifying weeks of the year.  Valid values are 1 to 53
      or -53 to -1.  This corresponds to weeks according to week
      numbering as defined in [ISO.8601.2004].  A week is defined as a
      seven day period, starting on the day of the week defined to be
      the week start (see WKST).  Week number one of the calendar year
      is the first week that contains at least four (4) days in that
      calendar year.  This rule part MUST NOT be used when the FREQ rule
      part is set to anything other than YEARLY.  For example, 3
      represents the third week of the year.

         Note: Assuming a Monday week start, week 53 can only occur when
         Thursday is January 1 or if it is a leap year and Wednesday is
         January 1.
   *         
   * @author David Bal
   * 
 * */
public class ByWeekNumber extends ByRuleIntegerAbstract<ByWeekNumber>
{    
    private final static int MIN_DAYS_IN_WEEK = 4;
    /** Start of week - default start of week is Monday */
    private DayOfWeek getWeekStart()
    {
    	if (getParent() != null)
    	{
    		WeekStart weekStart = ((RecurrenceRuleValue) getParent()).getWeekStart();
			return (weekStart == null) ? WeekStart.DEFAULT_WEEK_START : weekStart.getValue();
    	} else
    	{
    		return WeekStart.DEFAULT_WEEK_START;
    	}
	}

    /*
     * CONSTRUCTORS
     */
    public ByWeekNumber()
    {
        super();
    }
    
    public ByWeekNumber(Integer...weekNumbers)
    {
        super(weekNumbers);
    }
    
    public ByWeekNumber(ByWeekNumber source)
    {
        super(source);
    }
        
    @Override
    Predicate<Integer> isValidValue()
    {
        return (value) -> (value >= -53) && (value <= 53) && (value != 0);
    }
    
    @Override
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ChronoUnit chronoUnit, Temporal dateTimeStart )
    {

        switch (chronoUnit)
        {
        case YEARS:
            WeekFields weekFields = WeekFields.of(getWeekStart(), MIN_DAYS_IN_WEEK);
            Stream<Temporal> outStream = inStream.flatMap(date -> 
            { // Expand to include all days matching week numbers
                List<Temporal> dates = new ArrayList<>();
                for (int weekNumber : getValue())
                {
                    Temporal correctYearTemporal = (weekNumber > 0) ? date : date.minus(1, ChronoUnit.YEARS);
                    correctYearTemporal = correctYearTemporal
                            .with(TemporalAdjusters.firstDayOfYear())
                            .with(TemporalAdjusters.nextOrSame(weekFields.getFirstDayOfWeek())); // get first week entirely in correct year
                    Year correctYear = Year.from(correctYearTemporal);
                    long between = 0;
                    int finalWeekNumber;
                    if (weekNumber > 0)
                    {
                        finalWeekNumber = weekNumber;
                    } else if (weekNumber < 0)
                    {
                        int weeksInYear = 53;
                        Temporal startWeek53 = correctYearTemporal.with(weekFields.weekOfWeekBasedYear(), weeksInYear);
                        Temporal lastDayOfYear = correctYearTemporal.with(TemporalAdjusters.lastDayOfYear());
                         between = ChronoUnit.DAYS.between(startWeek53, lastDayOfYear);
                         int adjustment = (between >= 3) ? 1 : 0;
                        finalWeekNumber = weeksInYear + weekNumber + adjustment;
                    } else
                    {
                        throw new IllegalArgumentException(name().toString() + " can't have a value of zero");
                    }
                    
                    final Temporal startDate;
                    if (finalWeekNumber > 0)
                    {
                        startDate = correctYearTemporal
                                .with(weekFields.weekOfWeekBasedYear(), finalWeekNumber);
                    } else if (between < 0)
                    {
                        startDate = correctYearTemporal.minus(1, ChronoUnit.WEEKS);
                    } else
                    {
                        startDate = null;
                    }
                    if (startDate != null)
                    {
                        IntStream.range(0,7).forEach(days -> 
                        {
                            Temporal newTemporal = startDate.plus(days, ChronoUnit.DAYS);
                            Year myYear = Year.from(newTemporal);
                            if (myYear.equals(correctYear))
                            {
                                dates.add(newTemporal);
                            }
                        });
                    }
                }
                return dates.stream().sorted(DateTimeUtilities.TEMPORAL_COMPARATOR);
            });
            return outStream;
        case DAYS:
        case WEEKS:
        case MONTHS:
        case HOURS:
        case MINUTES:
        case SECONDS:
            throw new IllegalArgumentException("BYWEEKNO is not available for " + chronoUnit + " frequency."); // Not available
        default:
            break;
        }
        return null;    
    }

    public static ByWeekNumber parse(String content)
    {
    	return ByWeekNumber.parse(new ByWeekNumber(), content);
    }

}
