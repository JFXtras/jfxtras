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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRuleElementType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.WeekStart;
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
    /** Start of week - default start of week is Monday */
    public ObjectProperty<DayOfWeek> weekStartProperty() { return weekStart; }
    private ObjectProperty<DayOfWeek> weekStart =  new SimpleObjectProperty<>(this, RRuleElementType.WEEK_START.toString()); // bind to WeekStart element
    public DayOfWeek getWeekStart() { return (weekStart.get() == null) ? WeekStart.DEFAULT_WEEK_START : weekStart.get(); }
    private final static int MIN_DAYS_IN_WEEK = 4;

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
    
//    /**
//     * Listener to validate additions to value list
//     */
//    private ListChangeListener<Integer> validValueListener = (ListChangeListener.Change<? extends Integer> change) ->
//    {
//        while (change.next())
//        {
//            if (change.wasAdded())
//            {
//                Iterator<? extends Integer> i = change.getAddedSubList().iterator();
//                while (i.hasNext())
//                {
//                    int value = i.next();
//                    if ((value < -53) || (value > 53) || (value == 0))
//                    {
//                        throw new IllegalArgumentException("Invalid " + elementType().toString() + " value (" + value + "). Valid values are 1 to 53 or -53 to -1.");
//                    }
//                }
//            }
//        }
//    };

//    @Override
//    public void copyTo(ByRule destination)
//    {
//        ByWeekNumber destination2 = (ByWeekNumber) destination;
//        destination2.weekNumbers = new int[weekNumbers.length];
//        for (int i=0; i<weekNumbers.length; i++)
//        {
//            destination2.weekNumbers[i] = weekNumbers[i];
//        }
//    }
    
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (obj == this) return true;
//        if((obj == null) || (obj.getClass() != getClass())) {
//            return false;
//        }
//        ByWeekNumber testObj = (ByWeekNumber) obj;
//        
//        boolean weekNumbersEquals = Arrays.equals(getValue(), testObj.getValue());
//        return weekNumbersEquals;
//    }
//    
//    @Override
//    public int hashCode()
//    {
//        int hash = 7;
//        hash = (31 * hash) + getWeekNumbers().hashCode();
//        return hash;
//    }
    
//    @Override
//    public String toContent()
//    {
//        String days = getValue().stream()
//                .map(v -> v.toString())
//                .collect(Collectors.joining(","));
//        return RRuleElementType.BY_WEEK_NUMBER + "=" + days; //.substring(0, days.length()-1); // remove last comma
//    }
    
    @Override
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ChronoUnit chronoUnit, Temporal dateTimeStart )
    {
//        ChronoUnit originalChronoUnit = chronoUnit.get();
//        chronoUnit.set(WEEKS);

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
//                    System.out.println(correctYear + " " + );
//                    date.with(field, newValue)
//                    int year = correctYearTemporal.get(weekFields.weekBasedYear());
//                    for (int days = 0; days<7; days++)
//                    {
//                        Temporal adjustedTemporal = correctYearTemporal
//                                .with(TemporalAdjusters.lastDayOfYear())
//                                .minus(days, ChronoUnit.DAYS);
//                        int myYear = adjustedTemporal
//                                .get(weekFields.weekBasedYear());
//                        System.out.println("years:" + year + " " + myYear);
//                        if (year == myYear)
//                        {
//                            weeksInYear = adjustedTemporal.get(weekFields.weekOfWeekBasedYear());                            
//                        }
//                    }

//                    int myYear = correctYearTemporal.
//                    int weeksInYear = date.with(TemporalAdjusters.lastDayOfYear()).get(weekFields.weekOfWeekBasedYear());
//                    final Temporal startDate;
                    long between = 0;
                    int finalWeekNumber;
                    if (weekNumber > 0)
                    {
//                        startDate = correctYearTemporal
//                                .with(TemporalAdjusters.firstDayOfYear())
//                                .with(TemporalAdjusters.nextOrSame(weekFields.getFirstDayOfWeek()))
//                                .with(weekFields.weekOfWeekBasedYear(), weekNumber);
                        finalWeekNumber = weekNumber;
                    } else if (weekNumber < 0)
                    {
//                        date.with(weekFields.weekOfWeekBasedYear(), 53);
                        int weeksInYear = 53;
                        Temporal startWeek53 = correctYearTemporal.with(weekFields.weekOfWeekBasedYear(), weeksInYear);
//                        int dayOfYearWeek53 = startWeek53.get(ChronoField.DAY_OF_YEAR);
//                        if (dayOfYearWeek53 < )
                        Temporal lastDayOfYear = correctYearTemporal.with(TemporalAdjusters.lastDayOfYear());
                        
//                        Temporal endWeek53 = correctYearTemporal
//                                .with(TemporalAdjusters.lastDayOfYear())
//                                .plus(2, ChronoUnit.WEEKS)
//                                .with(TemporalAdjusters.nextOrSame(weekFields.getFirstDayOfWeek()))
////                                .minus(1, ChronoUnit.DAYS)
//                                .with(weekFields.weekOfWeekBasedYear(), 1);
//                        System.out.println("dayOfYear:" + " " + date + " " + correctYearTemporal + "  " + startWeek53 + " " + dayOfYearWeek53 + " " + (lastDayOfYear-dayOfYearWeek53) );
//                        System.out.println("daystogo:" + ChronoUnit.DAYS.between(startWeek53, lastDayOfYear));
                          between = ChronoUnit.DAYS.between(startWeek53, lastDayOfYear);
                         int adjustment = (between >= 3) ? 1 : 0;
                        finalWeekNumber = weeksInYear + weekNumber + adjustment;
//                        if (finalWeekNumber == 0)
//                        {
//                            if (ChronoUnit.DAYS.between(startWeek53, lastDayOfYear) < 0)
//                                {
//                                    finalWeekNumber = 1;
//                                }
//                        }
//                        System.out.println("weeks:" + weeksInYear + " " + newWeekNumber);
//                        if (newWeekNumber > 0)
//                        {
//                            finalWeekNumber = newWeekNumber;
//                        } else
//                        {
//                            finalWeekNumber = 0;
//                        }
//                        startDate = correctYearTemporal
//                                .with(TemporalAdjusters.firstDayOfYear())
//                                .with(TemporalAdjusters.nextOrSame(weekFields.getFirstDayOfWeek()))
//                                .with(weekFields.weekOfWeekBasedYear(), 53)
//                                .plus(weekNumber*7+1, ChronoUnit.DAYS);
                    } else
                    {
                        throw new IllegalArgumentException(elementType().toString() + " can't have a value of zero");
                    }
//                    Temporal newTemporal = correctYearTemporal.plus(finalWeekNumber, ChronoUnit.WEEKS);
                    
                    final Temporal startDate;
                    if (finalWeekNumber > 0)
                    {
                        startDate = correctYearTemporal
//                                .with(TemporalAdjusters.firstDayOfYear())
//                                .with(TemporalAdjusters.nextOrSame(weekFields.getFirstDayOfWeek()))
                                .with(weekFields.weekOfWeekBasedYear(), finalWeekNumber);
                    } else if (between < 0)
                    {
//                        System.out.println("between:" + between);
                        startDate = correctYearTemporal.minus(1, ChronoUnit.WEEKS);
                    } else
                    {
                        startDate = null;
                    }
//                        Temporal temporal2 = temporal1
//                                .with(weekFields.weekOfWeekBasedYear(), finalWeekNumber);
//                        System.out.println("one,two:" + temporal1 + " " + temporal2 + " " + temporal2.get(weekFields.weekOfWeekBasedYear()));
//                        if (! temporal1.equals(temporal2))
//                        {
//                        Temporal startDate = temporal2
//                                .with(TemporalAdjusters.previousOrSame(weekFields.getFirstDayOfWeek()));
                    if (startDate != null)
                    {
//                        System.out.println("startDate:" + startDate + " " + correctYear );
//                        if (Year.from(startDate).equals(correctYear))
//                        {
                            IntStream.range(0,7).forEach(days -> 
                            {
                                Temporal newTemporal = startDate.plus(days, ChronoUnit.DAYS);
//                                int myWeekNumber = newTemporal.get(weekFields.weekOfWeekBasedYear());
                                Year myYear = Year.from(newTemporal);
//                                System.out.println("myYear:" + myYear + " " +  dateTimeStart);
                                if (myYear.equals(correctYear))
                                {
                                    dates.add(newTemporal);
                                }
                            });
                        }
//                    }
                }
                return dates.stream().sorted(DateTimeUtilities.TEMPORAL_COMPARATOR);


//                    weekFields.getFirstDayOfWeek()
//                    final Temporal correctWeekTemporal = (weekNumber > 0) ? date : date.minus(1, ChronoUnit.WEEKS);
//                    
//                    correctWeekTemporal.with(TemporalAdjusters.previousOrSame(weekFields.getFirstDayOfWeek()));
//                    int daysInWeek = (int) ChronoUnit.DAYS.between(correctWeekTemporal.with(TemporalAdjusters.firstDayOfWeek()),
//                                                                   correctWeekTemporal.with(TemporalAdjusters.firstDayOfNextWeek()));
//                    int finalWeekNumber = 0;
//                    if (dayOfYear > 0)
//                    {
//                        if (dayOfYear <= daysInYear)
//                        {
//                            finalWeekNumber = dayOfYear;
//                        }
//                    } else if (dayOfYear < 0)
//                    {
//                        int newWeekNumber = daysInYear + dayOfYear + 1;
//                        if (newWeekNumber > 0)
//                        {
//                            finalWeekNumber = newWeekNumber;
//                        }
//                    } else
//                    {
//                        throw new IllegalArgumentException(elementType().toString() + " can't have a value of zero");
//                    }
//                    Temporal newTemporal = (finalWeekNumber != 0) ? correctYearTemporal.with(ChronoField.DAY_OF_YEAR, finalWeekNumber) : null;
//
//                    if ((newTemporal != null) && ! DateTimeUtilities.isBefore(newTemporal, dateTimeStart))
//                    {
//                        dates.add(newTemporal);
//                    }
//                }
//                return dates.stream();
                    
                
//                DayOfWeek dayOfWeek = DayOfWeek.from(date);
//                Set<Temporal> dates = new HashSet<>();
//                for (int myWeekNumber : getValue())
//                {
//                    Temporal startDate = date
//                            .with(weekFields.weekOfWeekBasedYear(), myWeekNumber)
//                            .with(TemporalAdjusters.previousOrSame(getWeekStart()));
//                    IntStream.range(0,7).forEach(days -> 
//                    {
//                        Temporal newTemporal = startDate.plus(days, ChronoUnit.DAYS);
//                        if (! DateTimeUtilities.isBefore(newTemporal, dateTimeStart))
//                        {
//                            dates.add(newTemporal);
//                        }
//                    });
//                }
//                return dates.stream().sorted(DateTimeUtilities.TEMPORAL_COMPARATOR);
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
        ByWeekNumber element = new ByWeekNumber();
        element.parseContent(content);
        return element;
    }

}
