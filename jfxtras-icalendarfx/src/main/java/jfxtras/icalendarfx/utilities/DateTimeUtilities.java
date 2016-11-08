package jfxtras.icalendarfx.utilities;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.util.Pair;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.parameters.ParameterType;
import jfxtras.icalendarfx.properties.component.time.TimeTransparency.TimeTransparencyType;


/**
 * Temporal date and date-time types supported by iCalendar.
 *  DATE
 *  DATE_WITH_LOCAL_TIME
 *  DATE_WITH_LOCAL_TIME_AND_TIME_ZONE
 *  DATE_WITH_UTC_TIME:
 * see iCalendar RFC 5545, page 32-33
 * 
 * includes methods to format a Temporal representing a DateTimeType as a String
 * 
 * @author David Bal
 *
 */
public final class DateTimeUtilities
{
    private DateTimeUtilities() { }
    
    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault(); // default ZoneId to convert to
    /** 
     * Default DateTimeType to use when none is specified.  For example, when a date-only component is converted to
     * a date-time one.
     */
    public static final DateTimeType DEFAULT_DATE_TIME_TYPE = DateTimeType.DATE_WITH_UTC_TIME;

    public final static DateTimeFormatter LOCAL_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendValue(MONTH_OF_YEAR, 2)
            .appendValue(DAY_OF_MONTH, 2)
            .toFormatter();
    final static DateTimeFormatter LOCAL_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(HOUR_OF_DAY, 2)
            .appendValue(MINUTE_OF_HOUR, 2)
            .appendValue(SECOND_OF_MINUTE, 2)
            .toFormatter();
    public final static DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(LOCAL_DATE_FORMATTER)
            .appendLiteral('T')
            .append(LOCAL_TIME_FORMATTER)
            .toFormatter();
    public final static DateTimeFormatter ZONED_DATE_TIME_UTC_FORMATTER = new DateTimeFormatterBuilder()
            .append(LOCAL_DATE_TIME_FORMATTER)
            .appendOffsetId()
            .toFormatter();
//    @Deprecated
    public final static DateTimeFormatter ZONED_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .optionalStart()
            .appendLiteral('[')
            .parseCaseInsensitive()
            .appendZoneRegionId()
            .appendLiteral(']')
            .optionalEnd()
            .append(LOCAL_DATE_TIME_FORMATTER)
            .optionalStart()
            .appendOffsetId()            
            .optionalEnd()
            .toFormatter();
    final static DateTimeFormatter ZONE_FORMATTER = new DateTimeFormatterBuilder()
            .optionalStart()
            .parseCaseInsensitive()
            .appendLiteral("TZID=")
            .appendZoneRegionId()
            .optionalEnd()
            .toFormatter();
    
    /** Compares two temporals of the same type */
    public final static Comparator<Temporal> TEMPORAL_COMPARATOR = (t1, t2) -> 
    {
        if (t1.getClass().equals(t2.getClass()))
        {
            return getTemporalComparator(t1).compare(t1, t2);
        } else
        {
            throw new DateTimeException("For comparision, Temporal classes must be equal (" + t1.getClass().getSimpleName() + ", " + t2.getClass().getSimpleName() + ")");
        }
//        LocalDateTime ld1 = (t1.isSupported(ChronoUnit.NANOS)) ? LocalDateTime.from(t1) : LocalDate.from(t1).atStartOfDay();
//        LocalDateTime ld2 = (t2.isSupported(ChronoUnit.NANOS)) ? LocalDateTime.from(t2) : LocalDate.from(t2).atStartOfDay();
//        return ld1.compareTo(ld2);
    };
    
    /** Compares two temporals of the LocalDate, LocalDateTime and ZonedDateTime
     * Makes LocalDate at start of day.  Add system default ZoneId to LocalDate and LocalDateTime */
    public final static Comparator<Temporal> TEMPORAL_COMPARATOR2 = (t1, t2) -> 
    {
        ZonedDateTime z1 = (ZonedDateTime) DateTimeType.DATE_WITH_UTC_TIME.from(t1);
        ZonedDateTime z2 = (ZonedDateTime) DateTimeType.DATE_WITH_UTC_TIME.from(t2);
        return z1.compareTo(z2);
    };
    
    /** Returns correct comparator based on Temporal parameter */
    public final static Comparator<Temporal> getTemporalComparator(Temporal t) 
    {
        if (t instanceof LocalDate)
        {
            return (t1, t2) -> ((LocalDate) t1).compareTo((LocalDate) t2);
        } else if (t instanceof LocalDateTime)
        {
            return (t1, t2) -> ((LocalDateTime) t1).compareTo((LocalDateTime) t2);            
        } else if (t instanceof ZonedDateTime)
        {
            return (t1, t2) -> ((ZonedDateTime) t1).compareTo((ZonedDateTime) t2);
        } else
        {
            throw new DateTimeException("Unsupported Temporal type:" + t.getClass().getSimpleName());
        }
    }
    
    /** Determines if Temporal is before t2
     * Works for LocalDate, LocalDateTime and ZonedDateTime
     * 
     * @param t1 first Temporal
     * @param t2 second Temporal (to compare with t1)
     * @return true if t1 is before t2, false otherwise
     */
    public static boolean isBefore(Temporal t1, Temporal t2)
    {
        if (t1.getClass().equals(t2.getClass()))
        {
            if (t1 instanceof LocalDate)
            {
                return ((LocalDate) t1).isBefore((LocalDate) t2);
            } else if (t1 instanceof LocalDateTime)
            {
                return ((LocalDateTime) t1).isBefore((LocalDateTime) t2);
            } else if (t1 instanceof ZonedDateTime)
            {
                return ((ZonedDateTime) t1).isBefore((ZonedDateTime) t2);
            } else
            {
                throw new DateTimeException("Unsupported Temporal class: " + t1.getClass());
            }
        }
        throw new DateTimeException("For comparision, Temporal classes must be equal (" + t1.getClass().getSimpleName() + ", " + t2.getClass().getSimpleName() + ")");
    }

    /** Determines if Temporal is after t2
     * Works for LocalDate, LocalDateTime and ZonedDateTime
     * 
     * @param t1 first Temporal
     * @param t2 second Temporal (to compare with t1)
     * @return true if t1 is after t2, false otherwise
     */
    public static boolean isAfter(Temporal t1, Temporal t2)
    {
        if (t1.getClass().equals(t2.getClass()))
        {
            if (t1 instanceof LocalDate)
            {
                return ((LocalDate) t1).isAfter((LocalDate) t2);
            } else if (t1 instanceof LocalDateTime)
            {
                return ((LocalDateTime) t1).isAfter((LocalDateTime) t2);
            } else if (t1 instanceof ZonedDateTime)
            {
                return ((ZonedDateTime) t1).isAfter((ZonedDateTime) t2);
            } else
            {
                throw new DateTimeException("Unsupported Temporal class: " + t1.getClass());
            }
        } throw new DateTimeException("For comparision, Temporal classes must be equal (" + t1.getClass().getSimpleName() + ", " + t2.getClass().getSimpleName() + ")");
    }
    
    private final static int CONFLICT_CHECK_QUANTITY = 400;
    /** Check if schedule conflict exists for {@link TimeTransparencyType.OPAQUE OPAQUE} events 
     * using default check quantity
     * 
     * @param vEvent  event to test
     * @param vEvents  existing events
     * @return  UID and start of recurrence of conflicting event, null otherwise
     */
    public static String checkScheduleConflict(VEvent vEvent, List<VEvent> vEvents)
    {
        return checkScheduleConflict(vEvent, vEvents, CONFLICT_CHECK_QUANTITY);
    }
    /** Check if schedule conflict exists for {@link TimeTransparencyType.OPAQUE OPAQUE} events.
     * 
     * @param vEvent  event to test
     * @param vEvents  existing events
     * @param checkQuantity  amount of recurrences to be tested
     * @return  UID and start of recurrence of conflicting event, null otherwise
     */
    // TODO - Should this method compare FreeBusy times too?
    public static String checkScheduleConflict(VEvent vEvent, List<VEvent> vEvents, int checkQuantity)
    {
        // must be opaque to cause conflict, opaque is default
        TimeTransparencyType newTransparency = (vEvent.getTimeTransparency() == null) ? TimeTransparencyType.OPAQUE : vEvent.getTimeTransparency().getValue();
        if (newTransparency == TimeTransparencyType.TRANSPARENT)
        {
            return null;
        }
        
        /*
         * Make list of recurrence start date/times for VEvent to be tested
         */
        LocalDate dtstart = LocalDate.from(vEvent.getDateTimeStart().getValue());
        TemporalAmount duration = vEvent.getActualDuration();
        List<Temporal> newStarts = vEvent.streamRecurrences().limit(checkQuantity).collect(Collectors.toList());
        Temporal lastStart = newStarts.get(newStarts.size()-1);

        /*
         * Make sorted list of events before lastStart from all VEvents
         */
        List<Triple> eventTimes = vEvents.stream()
            .filter(v -> 
            { // only keep OPAQUE events
                TimeTransparencyType myTransparency = (v.getTimeTransparency() == null) ? TimeTransparencyType.OPAQUE : v.getTimeTransparency().getValue();
                return myTransparency == TimeTransparencyType.OPAQUE;
            })
            .flatMap(v ->
            {
                TemporalAmount actualDuration = v.getActualDuration();
                Temporal myDTStart = v.getDateTimeStart().getValue().with(dtstart);
                return v.streamRecurrences(myDTStart)
                    .limit(checkQuantity)
                    .filter(t -> ! DateTimeUtilities.isAfter(t, lastStart))
                    .map(t -> 
                    {
                        String uid = (v.getUniqueIdentifier() != null) ? v.getUniqueIdentifier().getValue() : null;
                        return new Triple(t, t.plus(actualDuration), uid);
                    });
            })
            .sorted((t1, t2) -> DateTimeUtilities.TEMPORAL_COMPARATOR2.compare(t1.start, t2.start))
            .collect(Collectors.toList());

        /*
         *  Search for a conflict
         *  1.  New start before existing end, new end after existing start
         */
        for (Temporal newStart : newStarts)
        {
            Temporal newEnd = newStart.plus(duration);
            Triple firstConflict = eventTimes.stream()
                .filter(triple ->
                {
                    boolean newStartBeforeEnd = DateTimeUtilities.isBefore(newStart, triple.end);
                    boolean newEndAfterStart = DateTimeUtilities.isAfter(newEnd, triple.start);
                    return (newStartBeforeEnd && newEndAfterStart);
                })
                .findAny()
                .orElseGet(() -> null);
            if (firstConflict != null)
            {
                String uid = (firstConflict.uid != null) ? firstConflict.uid + ", " : "";
                return (firstConflict == null) ? null : uid + DateTimeUtilities.temporalToString(firstConflict.start);
            }
        }
        return null; // no conflicts found
    }
    // Triple class for checkScheduleConflict
    private static class Triple
    {
        public Triple(Temporal start, Temporal end, String uid)
        {
            this.start = start;
            this.end = end;
            this.uid = uid;
        }
        Temporal start;
        Temporal end;
        String uid;
    }
    
    /** Check if schedule conflict exists for {@link TimeTransparencyType.OPAQUE OPAQUE} events.
     * Finds conflict faster, but not necessarily the first conflict that occurs
     * 
     * @param vEvent  event to test
     * @param vEvents  existing events
     * @param checkQuantity  amount of recurrences to be tested
     * @return  UID and start of recurrence of conflicting event, null otherwise
     */
    // TODO - DOESN'T WORK, FIX IF WANTED
    public static String checkScheduleConflict2(VEvent vEvent, List<VEvent> vEvents, int checkQuantity)
    {
        // must be opaque to cause conflict, opaque is default
        TimeTransparencyType newTransparency = (vEvent.getTimeTransparency() == null) ? TimeTransparencyType.OPAQUE : vEvent.getTimeTransparency().getValue();
        if (newTransparency == TimeTransparencyType.TRANSPARENT)
        {
            return null;
        }
        
        LocalDate dtstart = LocalDate.from(vEvent.getDateTimeStart().getValue());
        TemporalAmount duration = vEvent.getActualDuration();
        List<Temporal> newStarts = vEvent.streamRecurrences().limit(checkQuantity).collect(Collectors.toList());
        Temporal lastStart = newStarts.get(newStarts.size()-1);
                   
        /*
         *  Search for a conflict
         *  Note: The first conflict found may not be the first occurring conflict.
         *  Make list of Pairs containing start and end temporals
         */
        for (VEvent v : vEvents)
        {
            // can only conflict with opaque events, opaque is default
            TimeTransparencyType myTransparency = (v.getTimeTransparency() == null) ? TimeTransparencyType.OPAQUE : v.getTimeTransparency().getValue();
            if (myTransparency == TimeTransparencyType.OPAQUE)
            {
                List<Pair<Temporal,Temporal>> eventTimes = new ArrayList<>();
                Temporal myDTStart = v.getDateTimeStart().getValue().with(dtstart);
                TemporalAmount actualDuration = v.getActualDuration();
                Iterator<Temporal> existingStartIterator = v.streamRecurrences(myDTStart).limit(checkQuantity).iterator();
                while (existingStartIterator.hasNext())
                {
                    Temporal t = existingStartIterator.next();
                    if (DateTimeUtilities.isAfter(t, lastStart)) break; // quit adding if after last new start temporal
                    eventTimes.add(new Pair<>(t, t.plus(actualDuration)));
                }
                
                for (Temporal newStart : newStarts)
                {
                    Temporal newEnd = newStart.plus(duration);
                    Pair<Temporal, Temporal> conflict = eventTimes.stream()
                        .filter(p ->
                        {
                            Temporal existingStart = p.getKey();
                            Temporal existingEnd = p.getValue();
                            // test start
                            boolean isOnOrAfter = ! DateTimeUtilities.isBefore(newStart, existingStart);
                            if (isOnOrAfter)
                            {
                                return ! DateTimeUtilities.isAfter(newStart, existingEnd);
                            }
                            // test end
                            boolean isAfter = DateTimeUtilities.isAfter(newEnd, existingStart);
                            if (isAfter)
                            {
                                return DateTimeUtilities.isBefore(newEnd, existingEnd);
                            }
                            return false;
                        })
                        .findAny()
                        .orElseGet(() -> null);
                    if (conflict != null)
                    {
                        String uid = (v.getUniqueIdentifier() != null) ? v.getUniqueIdentifier().getValue() + ", " : "";
                        return (conflict == null) ? null : uid + conflict.getKey();
                    }
                }
            }
        }
        return null; // no conflicts found
    }
    
    /**
     * returns week of month.
     * For example, a LocalDate representing March 10, 2016 returns 2, for the 2nd Thursday.
     * 
     * @param dateBasedTemporal - date based Temporal, such as LocalDate
     * @return - ordinal week in month, such as 2nd (as in 2nd Wednesday in month)
     */
    public static int weekOrdinalInMonth(Temporal dateBasedTemporal)
    {
        Temporal start = dateBasedTemporal.with(TemporalAdjusters.firstDayOfMonth());
        int ordinalWeekNumber = 0;
        while (! DateTimeUtilities.isBefore(dateBasedTemporal, start))
        {
            ordinalWeekNumber++;
            start = start.plus(1, ChronoUnit.WEEKS);
        }
        return ordinalWeekNumber;
    }
    
    /**
     * Calculate TemporalAmount between two Temporals.
     * Both temporals must be the same type and representations of a DateTimeType.
     * 
     * @param startInclusive - the start temporal, not null
     * @param endExclusive - the end temporal, not null
     * @return - Period for DATE, Duration for all other DateTimeTypes
     */
    public static TemporalAmount temporalAmountBetween(Temporal startInclusive, Temporal endExclusive)
    {
        final TemporalAmount duration;
        if (startInclusive instanceof LocalDate || endExclusive instanceof LocalDate)
        {
            duration = Period.between(LocalDate.from(startInclusive), LocalDate.from(endExclusive));
        } else
        {
            duration = Duration.between(startInclusive, endExclusive);
        }
        return duration;
    }
    
//    /**
//     * Parse iCalendar date or date/time string into LocalDate, LocalDateTime or ZonedDateTime for following formats:
//     * FORM #1: DATE WITH LOCAL TIME e.g. 19980118T230000 (LocalDateTime)
//     * FORM #2: DATE WITH UTC TIME e.g. 19980119T070000Z (ZonedDateTime)
//     * FORM #3: DATE WITH LOCAL TIME AND TIME ZONE REFERENCE e.g. TZID=America/New_York:19980119T020000 (ZonedDateTime)
//     * FORM #4: DATE ONLY e.g. VALUE=DATE:19970304 (LocalDate)
//     * 
//     * 
//     * @param temporalPropertyLine
//     * @return
//     */
//    @Deprecated // use new 
//    public static Temporal parse(String temporalPropertyLine)
//    {
//        Map<String, String> parameterMap = ICalendarUtilities.propertyLineToParameterMap(temporalPropertyLine);
////        System.out.println("parameterMap:" + parameterMap);
//        return Arrays.stream(DateTimeType.values())
//                .map(d -> d.parse(parameterMap))
//                .filter(d -> d != null)
//                .findAny()
//                .get();
//    }
    
    public static Temporal parse(String temporalString, ZoneId zone)
    {
        return Arrays.stream(DateTimeType.values())
                .map(d -> d.parse(temporalString, zone))
                .filter(d -> d != null)
                .findAny()
                .get();        
    }
//    
//    /**
//     * Matches 
//     * 
//     * @param temporalString
//     * @return
//     */
//    private static Temporal parseTemporalString(String temporalString)
//    {
//        return Arrays.stream(DateTimeType.values())
//            .map(d -> d.parse(temporalString))
//            .filter(d -> d != null)
//            .findFirst()
//            .get();
//    }
    
//    /** Parse iCalendar date or date/time string into LocalDate, LocalDateTime or ZonedDateTime for following formats:
//     * FORM #1: DATE WITH LOCAL TIME e.g. 19980118T230000 (LocalDateTime)
//     * FORM #2: DATE WITH UTC TIME e.g. 19980119T070000Z (ZonedDateTime)
//     * FORM #3: DATE WITH LOCAL TIME AND TIME ZONE REFERENCE e.g. TZID=America/New_York:19980119T020000 (ZonedDateTime)
//     * FORM #4: DATE ONLY e.g. VALUE=DATE:19970304 (LocalDate)
//     * 
//     * Note: strings can contain optionally contain "VALUE" "=" ("DATE-TIME" / "DATE")) before the date-time portion of the string.
//     * e.g. VALUE=DATE:19960401         VALUE=DATE-TIME:19980101T050000Z
//     * 
//     * Based on ISO.8601.2004
//     */
//    @Deprecated // obsolete - use parameter map instead
    public static Temporal parseOld(String temporalString)
    {
        final String temporalStringAdjusted;
        if (temporalString.matches("^VALUE=DATE-TIME:.*"))
        {
            temporalStringAdjusted = temporalString.substring(temporalString.indexOf("VALUE=DATE-TIME:")+"VALUE=DATE-TIME:".length()).trim();
        } else
        {
            temporalStringAdjusted = temporalString;
        }
        System.out.println("temporalStringAdjusted:"  + temporalStringAdjusted + " " + Arrays.stream(DateTimeType.values())
                .filter(d -> temporalStringAdjusted.matches(d.getPattern()))
                .findFirst()
                .get());
//                .parse(temporalString);
        return null;
    }
    
    /**
     * produced ISO.8601 date and date-time string for given Temporal of type
     * LocalDate, LocalDateTime or ZonedDateTime
     * 
     * @param temporal
     * @return
     */
    public static String temporalToString(Temporal temporal)
    {
        if (temporal instanceof ZonedDateTime)
        {
            ZonedDateTime value = (ZonedDateTime) temporal;
            ZoneId z = value.getZone();
            if (z.normalized().equals(ZoneOffset.UTC))
            {
                return DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER.format(value);
            } else
            {
                return DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(value); // Time zone is added through TimeZoneIdentifier parameter
            }
        } else if (temporal instanceof LocalDateTime)
        {
            LocalDateTime value = (LocalDateTime) temporal;
            return DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(value);
        } else if (temporal instanceof LocalDate)
        {
            return DateTimeUtilities.LOCAL_DATE_FORMATTER.format(temporal);
        }
        {
            throw new DateTimeException("Unsuported Date-Time class:" + temporal.getClass().getSimpleName());
        }
//        return DateTimeType.of(temporal).formatDateTimeType(temporal);
    }
    
    /**
     * Parse ISO.8601 string into LocalDate, LocalDateTime or ZonedDateTime Temporal object.
     */ 
    public static Temporal temporalFromString(String string)
    {
        List<Pair<String, String>> list = ICalendarUtilities.contentToParameterListPair(string);
        Map<String, String> map = list.stream().collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
//        Map<String, String> map = ICalendarUtilities.propertyLineToParameterMap(string);
        StringBuilder builder = new StringBuilder(50);
        String value = map.get(ICalendarUtilities.PROPERTY_VALUE_KEY);
        if (map.get(ParameterType.TIME_ZONE_IDENTIFIER.toString()) != null)
        {
            if (value.charAt(value.length()-1) != 'Z')
            {
                builder.append("[");
                builder.append(map.get(ParameterType.TIME_ZONE_IDENTIFIER.toString()));
                builder.append("]");
            }
        }
        builder.append(value);
        String string2 = builder.toString();

        final String form0 = "^[0-9]{8}";
        final String form1 = "^[0-9]{8}T([0-9]{6})";
        final String form2 = "^[0-9]{8}T([0-9]{6})Z";
        final String form3 = "^(\\[.*/.*\\])[0-9]{8}T([0-9]{6}Z?)";
        if (string2.matches(form0))
        {
            return LocalDate.parse(string2, DateTimeUtilities.LOCAL_DATE_FORMATTER);                                                
        } else if (string2.matches(form1))
        {
            return LocalDateTime.parse(string2, DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER);                                                
        } else if (string2.matches(form2))
        {
            return ZonedDateTime.parse(string2, DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER);                                                
        } else if (string2.matches(form3))
        {
            return ZonedDateTime.parse(string2, DateTimeUtilities.ZONED_DATE_TIME_FORMATTER);                                                
        } else
        {
            throw new DateTimeException("Can't parse date-time string:" + string);                        
        }
    }
    
    
    /**
     * Convert 2-character string to DayOfWeek following below convention:
     * weekday     = "SU" / "MO" / "TU" / "WE" / "TH" / "FR" / "SA"
     * Corresponding to SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, and SATURDAY days of the week.
     * 
     * @param dayOfWeekAbbreviation - 2 character abbreviation representing day of the week
     * @return - associated DayOfWeek
     */
    public static DayOfWeek dayOfWeekFromAbbreviation(String dayOfWeekAbbreviation)
    {
        Optional<DayOfWeek> optional = Arrays.stream(DayOfWeek.values())
                .filter(d -> d.toString().substring(0, 2).equals(dayOfWeekAbbreviation.toUpperCase()))
                .findAny();
        return (optional.isPresent()) ? optional.get() : null;
            
    }
    
    /**
     * Produces property name and attribute, if necessary.
     * For example:
     * LocalDate : DTSTART;VALUE=DATE:
     * LocalDateTime : DTSTART:
     * ZonedDateTime (UTC) : DTSTART:
     * ZonedDateTime : DTEND;TZID=America/New_York:
     * 
     * @param propertyName
     * @param temporal - temporal of LocalDate, LocalDateTime or ZonedDateTime
     * @return
     */
//    @Deprecated
//    public static String dateTimePropertyTag(String propertyName, Temporal temporal)
//    {
//        return DateTimeType.of(temporal).propertyTag(propertyName, temporal);
//    }
    
    
    /**
     * Returns LocalDateTime from Temporal that is an instance of either LocalDate, LocalDateTime or ZonedDateTime
     * If the parameter is type LocalDate the returned LocalDateTime is atStartofDay.
     * If the parameter is type ZonedDateTime the zoneID is changed to ZoneId.systemDefault() before taking the
     * LocalDateTime.
     */
    @Deprecated
    public static LocalDateTime toLocalDateTime(Temporal temporal)
    {
        return (LocalDateTime) DateTimeType.DATE_WITH_LOCAL_TIME.from(temporal);
    }
    
    
    /**
     * produced ISO.8601 date and date-time string for given Temporal of type
     * LocalDate, LocalDateTime or ZonedDateTime
     * 
     * @param temporal
     * @return
     */
    @Deprecated
    public static String format(Temporal temporal)
    {
        return DateTimeType.of(temporal).formatDateTimeType(temporal);
    }
    
    /**
     * Produces property name and attribute, if necessary.
     * For example:
     * LocalDate : DTSTART;VALUE=DATE:
     * LocalDateTime : DTSTART:
     * ZonedDateTime (UTC) : DTSTART:
     * ZonedDateTime : DTEND;TZID=America/New_York:
     * 
     * @param propertyName
     * @param temporal - temporal of LocalDate, LocalDateTime or ZonedDateTime
     * @return
     */
    @Deprecated
    public static String dateTimePropertyTag(String propertyName, Temporal temporal)
    {
        return DateTimeType.of(temporal).propertyTag(propertyName, temporal);
    }
//    
//    // TODO - NEED TO GO IN DATE TIME UTILITIES
//    public static boolean checkScheduleConflict(VCalendar vCalendar, VComponentPersonalBase<?> vComponent)
//    {
//        LocalDate dtstart = LocalDate.from(vComponent.getDateTimeStart().getValue());
//        List<Temporal> makeRecurrences(getVEvents(), dtstart, 1000)
//    }
    
    public enum DateTimeType
    {
//        DATE ("^(VALUE=DATE:)?[0-9]{8}")
        DATE ("^[0-9]{8}")
        {
            @Override
            public Temporal from(Temporal temporal, ZoneId zone)
            {
                return this.from(temporal);
            }
    
            @Override
            public Temporal from(Temporal temporal)
            {
                switch(DateTimeType.of(temporal))
                {
                case DATE:
                    return temporal; // do nothing
                case DATE_WITH_LOCAL_TIME:
                    return LocalDate.from(temporal);
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                case DATE_WITH_UTC_TIME:
                    return ZonedDateTime.from(temporal).withZoneSameInstant(DEFAULT_ZONE).toLocalDate();
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + temporal.getClass().getSimpleName());
                }
            }

            @Override
            String formatDateTimeType(Temporal temporal)
            {
                return LOCAL_DATE_FORMATTER.format(temporal);
            }

            @Override
            String propertyTag(String propertyName, Temporal temporal)
            {
                return propertyName + ";VALUE=DATE:";
            }

//            @Override
//            Temporal parse(String temporalString)
//            {
//                if (temporalString.matches("^VALUE=DATE:.*"))
//                {
//                    temporalString = temporalString.substring(temporalString.indexOf("VALUE=DATE:")+"VALUE=DATE:".length()).trim();
//                }
//                return LocalDate.parse(temporalString, LOCAL_DATE_FORMATTER);
//            }

            @Override
            boolean is(Temporal temporal)
            {
                return temporal instanceof LocalDate;
            }

            @Deprecated
            @Override
            Temporal parse(Map<String, String> parameterMap)
            {
                String temporalString = parameterMap.get(ICalendarUtilities.PROPERTY_VALUE_KEY);
                if (temporalString.matches(getPattern()))
                {
                    return LocalDate.parse(temporalString, LOCAL_DATE_FORMATTER);                    
                }
                return null;
            }

            @Override
            public Temporal parse(String temporalString, ZoneId zone)
            {
                if (temporalString.matches(getPattern()))
                {
                    return LocalDate.parse(temporalString, LOCAL_DATE_FORMATTER);                    
                }
                return null;
            }
        }
      , DATE_WITH_LOCAL_TIME ("^[0-9]{8}T([0-9]{6})")
        {
            @Override
            public Temporal from(Temporal temporal, ZoneId zone)
            {
                return this.from(temporal);
            }
    
            @Override
            public Temporal from(Temporal temporal)
            {
                switch(DateTimeType.of(temporal))
                {
                case DATE:
                    return LocalDate.from(temporal).atStartOfDay();
                case DATE_WITH_LOCAL_TIME:
                    return temporal;  // do nothing
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                case DATE_WITH_UTC_TIME:
                    return ZonedDateTime.from(temporal).withZoneSameInstant(DEFAULT_ZONE).toLocalDateTime();
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + temporal.getClass().getSimpleName());
                }
            }

            @Override
            String formatDateTimeType(Temporal temporal)
            {
                return LOCAL_DATE_TIME_FORMATTER.format(temporal);
            }

            @Override
            String propertyTag(String propertyName, Temporal temporal)
            {
                return propertyName + ":";
            }

//            @Override
//            Temporal parse(String temporalString)
//            {
//                return LocalDateTime.parse(temporalString, LOCAL_DATE_TIME_FORMATTER);
//            }

            @Override
            boolean is(Temporal temporal)
            {
                return temporal instanceof LocalDateTime;
            }

            @Deprecated
            @Override
            Temporal parse(Map<String, String> parameterMap)
            {
                String tzidParameter = parameterMap.get("TZID"); // time zone parameter
                String temporalString = parameterMap.get(ICalendarUtilities.PROPERTY_VALUE_KEY);
                boolean isTzidEmpty = tzidParameter == null;
                boolean isPatternMatch = temporalString.matches(getPattern());
                if (isTzidEmpty && isPatternMatch)
                {
                    return LocalDateTime.parse(temporalString, LOCAL_DATE_TIME_FORMATTER);
                }
                return null;
            }

            @Override
            public Temporal parse(String temporalString, ZoneId zone)
            {
                boolean isTzidEmpty = zone == null;
                boolean isPatternMatch = temporalString.matches(getPattern());
                if (isTzidEmpty && isPatternMatch)
                {
                    return LocalDateTime.parse(temporalString, LOCAL_DATE_TIME_FORMATTER);
                }
                return null;
            }
        }
      , DATE_WITH_UTC_TIME ("^[0-9]{8}T([0-9]{6})Z")
        {
            @Override
            public Temporal from(Temporal temporal, ZoneId zone)
            {
                return this.from(temporal);
            }
    
            @Override
            public Temporal from(Temporal temporal)
            {
                switch(DateTimeType.of(temporal))
                {
                case DATE:
                    return LocalDate.from(temporal).atStartOfDay().atZone(ZoneId.of("Z"));
                case DATE_WITH_LOCAL_TIME:
                    return LocalDateTime.from(temporal).atZone(DEFAULT_ZONE).withZoneSameInstant(ZoneId.of("Z"));
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                    return ZonedDateTime.from(temporal).withZoneSameInstant(ZoneId.of("Z"));
                case DATE_WITH_UTC_TIME:
                    return temporal;  // do nothing
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + temporal.getClass().getSimpleName());
                }
            }

            @Override
            String formatDateTimeType(Temporal temporal)
            {
                return ZONED_DATE_TIME_UTC_FORMATTER.format(temporal);
            }

            @Override
            String propertyTag(String propertyName, Temporal temporal)
            {
                return propertyName + ":";
            }

//            @Override
//            Temporal parse(String temporalString)
//            {
//                return ZonedDateTime.parse(temporalString, ZONED_DATE_TIME_UTC_FORMATTER);
//            }

            @Override
            boolean is(Temporal temporal)
            {
                if (temporal instanceof ZonedDateTime)
                {
                    ZoneId z = ((ZonedDateTime) temporal).getZone();
                    return z == ZoneId.of("Z");
                }
                return false;
            }

            @Deprecated
            @Override
            Temporal parse(Map<String, String> parameterMap)
            {
                String temporalString = parameterMap.get(ICalendarUtilities.PROPERTY_VALUE_KEY);
                boolean isPatternMatch = temporalString.matches(getPattern());
                if (isPatternMatch)
                {
                    return ZonedDateTime.parse(temporalString, ZONED_DATE_TIME_UTC_FORMATTER);
                }
                return null;
            }

            @Override
            public Temporal parse(String temporalString, ZoneId zone)
            {
                boolean isPatternMatch = temporalString.matches(getPattern());
                if (isPatternMatch)
                {
                    return ZonedDateTime.parse(temporalString, ZONED_DATE_TIME_UTC_FORMATTER);
                }
                return null;
            }
        }
      , DATE_WITH_LOCAL_TIME_AND_TIME_ZONE ("^[0-9]{8}T([0-9]{6})Z?")
        {
            @Override
            public Temporal from(Temporal temporal, ZoneId zone)
            {
                switch(DateTimeType.of(temporal))
                {
                case DATE:
                    return LocalDate.from(temporal).atStartOfDay().atZone(zone);
                case DATE_WITH_LOCAL_TIME:
                    return LocalDateTime.from(temporal).atZone(zone);
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                case DATE_WITH_UTC_TIME:
                    return ZonedDateTime.from(temporal).withZoneSameInstant(zone);
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + temporal.getClass().getSimpleName());
                }
            }
    
            @Override
            public Temporal from(Temporal temporal)
            {
                throw new DateTimeException("Can't make DATE_WITH_LOCAL_TIME_AND_TIME_ZONE without time zone.  Use from(Temporal temporal, ZoneId zone) instead");
            }

            @Override
            String formatDateTimeType(Temporal temporal)
            {
                return LOCAL_DATE_TIME_FORMATTER.format(temporal); // don't use ZONED_DATE_TIME_FORMATTER because time zone is added to property tag
            }

            @Override
            String propertyTag(String propertyName, Temporal temporal)
            {
                String zone = ZONE_FORMATTER.format(temporal);
                return propertyName + ";" + zone + ":";
            }
//
//            @Override
//            Temporal parse(String temporalString)
//            {
//                return ZonedDateTime.parse(temporalString, ZONED_DATE_TIME_FORMATTER);
//            }

            @Override
            boolean is(Temporal temporal)
            {
                return temporal instanceof ZonedDateTime;
            }

            @Deprecated
            @Override
            Temporal parse(Map<String, String> parameterMap)
            {
              String tzidParameter = parameterMap.get("TZID"); // time zone parameter
              String temporalString = parameterMap.get(ICalendarUtilities.PROPERTY_VALUE_KEY);
              boolean isTzidEmpty = tzidParameter == null;
              boolean isPatternMatch = temporalString.matches(getPattern());
              if (! isTzidEmpty && isPatternMatch)
              {
                  ZoneId zone = ZoneId.of(tzidParameter);
                  LocalDateTime localDateTime = LocalDateTime.parse(temporalString, LOCAL_DATE_TIME_FORMATTER);
                  return localDateTime.atZone(zone);
              }
              return null;
            }

            @Override
            public Temporal parse(String temporalString, ZoneId zone)
            {
                boolean isTzidEmpty = zone == null;
                boolean isPatternMatch = temporalString.matches(getPattern());
                if (! isTzidEmpty && isPatternMatch)
                {
                    LocalDateTime localDateTime = LocalDateTime.parse(temporalString, LOCAL_DATE_TIME_FORMATTER);
                    return localDateTime.atZone(zone);
                }
                return null;
            }
        };
        
        private String pattern;
        public String getPattern() { return pattern; }

        DateTimeType(String pattern)
        {
            this.pattern = pattern;
        }

        /** Find DateTimeType of Temporal parameter temporal */
        public static DateTimeType of(Temporal temporal)
        {
            return Arrays.stream(DateTimeType.values())
                    .filter(d -> d.is(temporal))
                    .findFirst()
                    .get();
        }
        
        /**
         * returns true if temporal is of the DateTimeType, false otherwise
         */
        abstract boolean is(Temporal temporal);
        
//        /** Parse iCalendar date or date/time string into LocalDate, LocalDateTime or ZonedDateTime for following formats:
//         * FORM #1: DATE WITH LOCAL TIME e.g. 19980118T230000 (LocalDateTime)
//         * FORM #2: DATE WITH UTC TIME e.g. 19980119T070000Z (ZonedDateTime)
//         * FORM #3: DATE WITH LOCAL TIME AND TIME ZONE REFERENCE e.g. TZID=America/New_York:19980119T020000 (ZonedDateTime)
//         * FORM #4: DATE ONLY e.g. VALUE=DATE:19970304 (LocalDate)
//         * 
//         * Note: strings can contain optionally contain "VALUE" "=" ("DATE-TIME" / "DATE")) before the date-time portion of the string.
//         * e.g. VALUE=DATE:19960401         VALUE=DATE-TIME:19980101T050000Z
//         * 
//         * Based on ISO.8601.2004
//         */
//        @Deprecated // use map version below
//        abstract Temporal parse(String temporalString);

        /**
         * Parses parameter map to Temporal.  Returns if no match.
         * 
         * @param parameterMap - map of parameters from propertyLineToParameterMap
         * @return - parsed Temporal, if matches, null otherwise
         */
        @Deprecated
        abstract Temporal parse(Map<String,String> parameterMap);
        
        public abstract Temporal parse(String value, ZoneId zone);

//        /**
//         * Produces property name and attribute, if necessary.
//         * For example:
//         * LocalDate : DTSTART;VALUE=DATE:
//         * LocalDateTime : DTSTART:
//         * ZonedDateTime (UTC) : DTSTART:
//         * ZonedDateTime : DTEND;TZID=America/New_York:
//         * 
//         * @param propertyName - e.g. DTSTART
//         * @param temporal - temporal of LocalDate, LocalDateTime or ZonedDateTime
//         * @return
//         */
        @Deprecated
        abstract String propertyTag(String propertyName, Temporal temporal);
    
        /** Format temporal to embedded DateTimeFormatter */
        @Deprecated
        abstract String formatDateTimeType(Temporal temporal);
        
        /** Convert temporal to new DateTimeType - for DATE_WITH_LOCAL_TIME_AND_TIME_ZONE */
        public abstract Temporal from(Temporal temporal, ZoneId zone);
        
        /** Convert temporal to new DateTimeType  - for all types, but DATE_WITH_LOCAL_TIME_AND_TIME_ZONE */
        public abstract Temporal from(Temporal temporal);
    }
}