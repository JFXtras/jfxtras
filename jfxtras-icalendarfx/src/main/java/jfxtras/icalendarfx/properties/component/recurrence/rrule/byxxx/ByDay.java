package jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx;

import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoUnit.DAYS;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRuleElementType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.WeekStart;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

/** BYDAY from RFC 5545, iCalendar 3.3.10, page 40
 * 
 * The BYDAY rule part specifies a COMMA-separated list of days of
 * the week; SU indicates Sunday; MO indicates Monday; TU indicates
 * Tuesday; WE indicates Wednesday; TH indicates Thursday; FR
 * indicates Friday; and SA indicates Saturday.
 *
 * Each BYDAY value can also be preceded by a positive (+n) or
 * negative (-n) integer.  If present, this indicates the nth
 * occurrence of a specific day within the MONTHLY or YEARLY "RRULE".
 *
 * For example, within a MONTHLY rule, +1MO (or simply 1MO)
 * represents the first Monday within the month, whereas -1MO
 * represents the last Monday of the month.  The numeric value in a
 * BYDAY rule part with the FREQ rule part set to YEARLY corresponds
 * to an offset within the month when the BYMONTH rule part is
 * present, and corresponds to an offset within the year when the
 * BYWEEKNO or BYMONTH rule parts are present.  If an integer
 * modifier is not present, it means all days of this type within the
 * specified frequency.  For example, within a MONTHLY rule, MO
 * represents all Mondays within the month.  The BYDAY rule part MUST
 * NOT be specified with a numeric value when the FREQ rule part is
 * not set to MONTHLY or YEARLY.  Furthermore, the BYDAY rule part
 * MUST NOT be specified with a numeric value with the FREQ rule part
 * set to YEARLY when the BYWEEKNO rule part is specified.
 * 
 * Element value is a ByDayPair that contains a DayOfWeek and an optional ordinal int.
 * if the ordinal int is 0 then it is ignored and all values matching the DayOfWeek are included.
 * 
 * */
public class ByDay extends ByRuleAbstract<ByDayPair, ByDay>
{   
    /** Start of week - default start of week is Monday */
    public ObjectProperty<DayOfWeek> weekStartProperty() { return weekStart; }
    private ObjectProperty<DayOfWeek> weekStart =  new SimpleObjectProperty<>(this, RRuleElementType.WEEK_START.toString()); // bind to WeekStart element
    public DayOfWeek getWeekStart() { return (weekStart.get() == null) ? WeekStart.DEFAULT_WEEK_START : weekStart.get(); }
    private final static int MIN_DAYS_IN_WEEK = 4;
    
    //CONSTRUCTORS
    /** Parse iCalendar compliant list of days of the week.  For example 1MO,2TU,4SA
     */
    public ByDay()
    {
        super();
    }
    
    public ByDay(ByDayPair... byDayPairs)
    {
        this();
        setValue(byDayPairs);
    }
    
    public ByDay(ByDay source)
    {
        super(source);
    }

    /** Constructor that uses {@link DayofWeek} values without a preceding integer.  All days of the 
     * provided types are included within the specified frequency */
    public ByDay(DayOfWeek... daysOfWeek)
    {
        this(Arrays.asList(daysOfWeek));
    }

    /** Constructor that uses {@link DayofWeek} Collection.  No ordinals are allowed. */
    public ByDay(Collection<DayOfWeek> daysOfWeek)
    {
        this();
        ByDayPair[] dayArray = daysOfWeek.stream()
            .map(d -> new ByDayPair(d,0))
            .toArray(size -> new ByDayPair[size]);
        setValue(FXCollections.observableArrayList(dayArray));
    }

    
    
    /** Checks if byDayPairs has ordinal values.  If so returns true, otherwise false */
    public boolean hasOrdinals()
    {
        return getValue()
                .stream()
                .filter(p -> (p.ordinal != 0))
                .findAny()
                .isPresent();
    }
    
    /** add individual {@link DayofWeek}, without ordinal value, to BYDAY rule
     * 
     * @param dayOfWeek {@link DayofWeek} to add, without ordinal
     * @return true if added, false if DayOfWeek already present
     */
    public boolean addDayOfWeek(DayOfWeek dayOfWeek)
    {
        boolean isPresent = getValue()
            .stream()
            .map(a -> a.dayOfWeek)
            .filter(d -> d == dayOfWeek)
            .findAny()
            .isPresent();
        if (! isPresent)
        {
            getValue().add(new ByDayPair(dayOfWeek, 0));
            return true;
        }
        return false;
    }

    /** remove individual DayofWeek from BYDAY rule
     * 
     * @param dayOfWeek {@link DayofWeek} to remove
     * @return true if removed, false if not present
     */
    public boolean removeDayOfWeek(DayOfWeek dayOfWeek)
    {
        ByDayPair p = getValue().stream()
                .filter(v -> v.dayOfWeek == dayOfWeek)
                .findAny()
                .orElse(null);
        if (p != null)
        {
            getValue().remove(p);
            return true;
        }
        return false;
    }
    
    /** Replace individual {@link DayofWeek} in BYDAY rule
     * If {@link ByDayPair} contains a non-zero ordinal, the replacement contains the same ordinal value
     * Note: a zero ordinal means include all matching {@link DayofWeek} values
     * 
     * @param original {@link DayofWeek} to remove
     * @param replacement {@link DayofWeek} to add
     * @return true if replaced, false if original is not present
     */
    public boolean replaceDayOfWeek(DayOfWeek originalDayOfWeek, DayOfWeek replacemenDayOfWeekt)
    {
        ByDayPair p = getValue().stream()
                .filter(v -> v.dayOfWeek == originalDayOfWeek)
                .findAny()
                .orElse(null);
        if (p != null)
        {
            int ordinal = p.getOrdinal();
            getValue().remove(p);
            getValue().add(new ByDayPair(replacemenDayOfWeekt, ordinal));
            return true;
        }
        return false;
    }
    
    /** Return a list of days of the week that don't have an ordinal (as every FRIDAY) */
    public List<DayOfWeek> dayOfWeekWithoutOrdinalList()
    {
        return getValue().stream()
                     .filter(d -> d.ordinal == 0)
                     .map(d -> d.dayOfWeek)
                     .collect(Collectors.toList());
    }
    
    @Override
    public String toContent()
    {
        String days = getValue().stream()
                .map(d ->
                {
                    String day = d.dayOfWeek.toString().substring(0, 2); // + ",";
                    return (d.ordinal == 0) ? day : d.ordinal + day;
                })
                .collect(Collectors.joining(","));
        return RRuleElementType.BY_DAY + "=" + days; //.substring(0, days.length()-1); // remove last comma
    }
    
    @Override // TODO - try to REMOVE startTemporal
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ChronoUnit chronoUnit, Temporal dateTimeStart)
    {
        /* TODO - according to iCalendar standard a ByDay rule doesn't need any specified days - should use day from DTSTART,
         * this is not implemented yet.  When implemented this line should be removed. */
        switch (chronoUnit)
        {
        case HOURS:
        case MINUTES:
        case SECONDS:
        case DAYS:
        {
            boolean isValid = getValue().stream().allMatch(v -> v.getOrdinal() == 0);
            if (! isValid)
            {
                throw new IllegalArgumentException("Numberic ordinal day values can't be set for FREQ as" + chronoUnit);
            }
            return inStream.filter(t ->
            { // filter out all but qualifying days
                DayOfWeek myDayOfWeek = DayOfWeek.from(t);
                for (ByDayPair byDayPair : getValue())
                {
                    if (byDayPair.dayOfWeek == myDayOfWeek) return true;
                }
                return false;
            });
        }
        case WEEKS:
        {
            boolean isValid = getValue().stream().allMatch(v -> v.getOrdinal() == 0);
            if (! isValid)
            {
                throw new IllegalArgumentException("Numberic ordinal day values can't be set for FREQ as " + chronoUnit);
            }
            WeekFields weekFields = WeekFields.of(getWeekStart(), MIN_DAYS_IN_WEEK);
            TemporalField dayOfWeekField = weekFields.dayOfWeek();
            return inStream.flatMap(t -> 
            { // Expand to be byDayPairs days in current week
                List<Temporal> dates = new ArrayList<>();
                for (ByDayPair byDayPair : getValue())
                {
                    int defaultFirstDayOfWeekValue = DayOfWeek.MONDAY.getValue();
                    int myFirstDayOfWeekValue = weekFields.getFirstDayOfWeek().getValue();
                    int dayOfWeekAdjustment = defaultFirstDayOfWeekValue - myFirstDayOfWeekValue + DayOfWeek.values().length;
                    int dayOfWeekValue = byDayPair.dayOfWeek.getValue() + dayOfWeekAdjustment;
                    dayOfWeekValue = (dayOfWeekValue > 7) ? dayOfWeekValue-7 : dayOfWeekValue;
                    Temporal newTemporal = t.with(dayOfWeekField, dayOfWeekValue);
                    dates.add(newTemporal);
                }
                if (getValue().size() > 1) Collections.sort(dates, DateTimeUtilities.TEMPORAL_COMPARATOR);
                return dates.stream();
            });
        }
        case MONTHS:
            return inStream.flatMap(date -> 
            {
                List<Temporal> dates = new ArrayList<>();
//                boolean sortNeeded = false;
                for (ByDayPair byDayPair : getValue())
                {
                    if (byDayPair.ordinal == 0)
                    { // add every matching day of week in month
//                        sortNeeded = true;
                        Month myMonth = Month.from(date);
                        for (int weekNum=1; weekNum<=5; weekNum++)
                        {
                            Temporal newTemporal = date.with(TemporalAdjusters.dayOfWeekInMonth(weekNum, byDayPair.dayOfWeek));
//                            if (Month.from(newTemporal) == myMonth && ! DateTimeUtilities.isBefore(newTemporal, dateTimeStart))
                            if (Month.from(newTemporal) == myMonth)
                            {
                                dates.add(newTemporal);
                            }
                        }
                    } else
                    {
                        Month myMonth = Month.from(date);
                        Temporal newTemporal = date.with(TemporalAdjusters.dayOfWeekInMonth(byDayPair.ordinal, byDayPair.dayOfWeek));

//                        if (Month.from(newTemporal) == myMonth && ! DateTimeUtilities.isBefore(newTemporal, dateTimeStart))
                        if (Month.from(newTemporal) == myMonth)
                        {
                            dates.add(newTemporal);
                        }
                    }
                }
                if (getValue().size() > 1) Collections.sort(dates, DateTimeUtilities.TEMPORAL_COMPARATOR);
//                dates.stream().forEach(d -> System.out.println(d + " " + dateTimeStart));
                return dates.stream();
            });
        case YEARS:
            return inStream.flatMap(date -> 
            {
                List<Temporal> dates = new ArrayList<>();
//                boolean sortNeeded = false;
                for (ByDayPair byDayPair : getValue())
                {
                    if (byDayPair.ordinal == 0)
                    { // add every matching day of week in year
//                        sortNeeded = true;
                        Temporal newDate = date
                                .with(TemporalAdjusters.firstDayOfYear())
                                .with(TemporalAdjusters.nextOrSame(byDayPair.dayOfWeek));
                        while (Year.from(newDate).equals(Year.from(date)))
                        {
//                            if (! DateTimeUtilities.isBefore(newDate, dateTimeStart)) dates.add(newDate);
                            dates.add(newDate);
                            newDate = newDate.plus(1, ChronoUnit.WEEKS);
                        }
                    } else
                    { // if never any ordinal numbers then sort is not required
                        Temporal newDate = date.with(dayOfWeekInYear(byDayPair.ordinal, byDayPair.dayOfWeek));
                        dates.add(newDate);
//                        if (! DateTimeUtilities.isBefore(newDate, dateTimeStart)) dates.add(newDate);
                    }
                }
                if (getValue().size() > 1) Collections.sort(dates, DateTimeUtilities.TEMPORAL_COMPARATOR);
                return dates.stream();
            }); 
        default:
            throw new RuntimeException("Not implemented ChronoUnit: " + chronoUnit);
        }
    }

    /** Finds nth occurrence of a week in a year.
     * Based on TemporalAdjusters.dayOfWeekInMonth */
    private TemporalAdjuster dayOfWeekInYear(int ordinal, DayOfWeek dayOfWeek)
    {
        int dowValue = dayOfWeek.getValue();
        return (temporal) -> {
            Temporal temp = (ordinal > 0) ? temporal.with(TemporalAdjusters.firstDayOfYear()) :
                temporal.plus(1, ChronoUnit.YEARS).with(TemporalAdjusters.firstDayOfYear());
            int curDow = temp.get(DAY_OF_WEEK);
            int dowDiff = (dowValue - curDow + 7) % 7;
            dowDiff = (ordinal > 0) ? dowDiff + (ordinal - 1) * 7 : dowDiff + (ordinal) * 7;
            return temp.plus(dowDiff, DAYS);
        };
    }
    
    /**
     * Contains both the day of the week and an optional positive or negative integer (ordinal).
     * If the integer is present it represents the nth occurrence of a specific day within the 
     * MONTHLY or YEARLY frequency rules.  For example, with a MONTHLY rule 1MO indicates the
     * first Monday of the month.
     * If ordinal is 0 then all the matching days are included within the specified frequency rule.
     */
    public static class ByDayPair
    {
        private DayOfWeek dayOfWeek;
        public DayOfWeek getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
        public ByDayPair withDayOfWeek(DayOfWeek dayOfWeek) { setDayOfWeek(dayOfWeek); return this; }
        
        private int ordinal = 0;
        public int getOrdinal() { return ordinal; }
        public void setOrdinal(int ordinal) { this.ordinal = ordinal; }
        public ByDayPair withOrdinal(int ordinal) { setOrdinal(ordinal); return this; }

        public ByDayPair(DayOfWeek dayOfWeek, int ordinal)
        {
            this.dayOfWeek = dayOfWeek;
            this.ordinal = ordinal;
        }
        
        public ByDayPair() { }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == this) return true;
            if((obj == null) || (obj.getClass() != getClass())) {
                return false;
            }
            ByDayPair testObj = (ByDayPair) obj;
            return (dayOfWeek == testObj.dayOfWeek)
                    && (ordinal == testObj.ordinal);
        }
        
        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = (31 * hash) + dayOfWeek.hashCode();
            hash = (31 * hash) + ordinal;
            return hash;
        }
        
        @Override
        public String toString()
        {
            return super.toString() + ", " + getDayOfWeek() + ", " + getOrdinal();
        }
        
    }
    
//    /** Match up iCalendar 2-character day of week to Java Time DayOfWeek */
//    @Deprecated
//    public enum ICalendarDayOfWeek
//    {
//        MO (DayOfWeek.MONDAY)
//      , TU (DayOfWeek.TUESDAY)
//      , WE (DayOfWeek.WEDNESDAY)
//      , TH (DayOfWeek.THURSDAY)
//      , FR (DayOfWeek.FRIDAY)
//      , SA (DayOfWeek.SATURDAY)
//      , SU (DayOfWeek.SUNDAY);
//      
//        private DayOfWeek dow;
//      
//        ICalendarDayOfWeek(DayOfWeek dow)
//        {
//          this.dow = dow;
//        }
//      
//        public DayOfWeek getDayOfWeek() { return dow; }
//    }
    
    @Override
    public List<String> parseContent(String dayPairs)
    {
        List<ByDayPair> dayPairsList = new ArrayList<ByDayPair>();
        Pattern p = Pattern.compile("(-?[0-9]+)?([A-Z]{2})");
        Matcher m = p.matcher(dayPairs);
        while (m.find())
        {
            String token = m.group();
            if (token.matches("^(-?[0-9]+.*)")) // start with ordinal number
            {
                Matcher m2 = p.matcher(token);
                if (m2.find())
                {
//                    DayOfWeek dayOfWeek = ICalendarDayOfWeek.valueOf(m2.group(2)).getDayOfWeek();
                    DayOfWeek dayOfWeek = DateTimeUtilities.dayOfWeekFromAbbreviation(m2.group(2));
                    int ordinal = Integer.parseInt(m2.group(1));
                    dayPairsList.add(new ByDayPair(dayOfWeek, ordinal));
                }
            } else
            { // has no ordinal number
                DayOfWeek dayOfWeek = DateTimeUtilities.dayOfWeekFromAbbreviation(token);
//                DayOfWeek dayOfWeek = ICalendarDayOfWeek.valueOf(token).getDayOfWeek();
                dayPairsList.add(new ByDayPair(dayOfWeek, 0));
            }
        }
        setValue(FXCollections.observableArrayList(dayPairsList));
        return errors();
    }

    public static ByDay parse(String content)
    {
        ByDay element = new ByDay();
        element.parseContent(content);
        return element;
    }
    
//    @Override
//    public boolean isValid()
//    {
//        // TODO Auto-generated method stub
//        return false;
//    }
}
