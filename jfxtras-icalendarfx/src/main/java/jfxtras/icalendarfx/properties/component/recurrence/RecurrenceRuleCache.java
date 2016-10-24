package jfxtras.icalendarfx.properties.component.recurrence;

import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

import jfxtras.icalendarfx.components.VRepeatable;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

/**
 * Handles caching an array of Temporal date/time values to speed up producing a stream
 * of recurrence instances for a recurrence rule (RRULE).
 * RFC 5545 3.8.5.2, page 121
 * The recurrence set is the complete set of recurrence instances for a calendar component.
 * 
 * @author David Bal
 *
 */
public class RecurrenceRuleCache
{
    // Variables for start date or date/time cache used as starting Temporal for stream
    private static final int CACHE_RANGE = 51; // number of values in cache
    private static final int CACHE_SKIP = 21; // store every nth value in the cache
    private int skipCounter = 0; // counter that increments up to CACHE_SKIP, indicates time to record a value, then resets to 0
    public Temporal[] temporalCache; // the start date or date/time cache
    private Temporal dateTimeStartLast; // last dateTimeStart, when changes indicates clearing the cache is necessary
    private RecurrenceRule2 rRuleLast; // last rRule, when changes indicates clearing the cache is necessary
    public int cacheStart = 0; // start index where cache values are stored (starts in middle)
    public int cacheEnd = 0; // end index where cache values are stored
    private VRepeatable<?> component; // the VComponent
    
    public RecurrenceRuleCache(VRepeatable<?> component)
    {
        this.component = component;
    }

    /**
     * finds previous value in recurrence set before input parameter value
     * 
     * @param value - start value
     * @return - previous recurrence instance
     */
    public Temporal previousValue(Temporal value)
    {
        final Temporal start; 
        if (cacheEnd == 0)
        {
            start = component.getDateTimeStart().getValue();
        } else
        { // try to get start from cache
            Temporal m  = null;
            for (int i=cacheEnd; i>cacheStart; i--)
            {
                if (DateTimeUtilities.isBefore(temporalCache[i], value))
                {
                    m = temporalCache[i];
                    break;
                }
            }
            start = (m != null) ? m : component.getDateTimeStart().getValue();
        }
        Iterator<Temporal> i = component.streamRecurrences(start).iterator();
//        Iterator<Temporal> i = streamNoCache(start).iterator();
        Temporal lastT = null;
        while (i.hasNext())
        {
            Temporal t = i.next();
            if (! DateTimeUtilities.isBefore(t, value)) break;
            lastT = t;
        }
        return lastT;
    }
    
    /**
     * Returns the previous start date/time value of the recurrence set on or before targetStart in the cache.  If no value
     * is present in the cache then the DTSTART value is returned.  This value is guaranteed to be
     * a valid recurrence date/time.  It can be used as a starting point for calculating future
     * recurrences more efficiently than using DTSTART.
     * 
     * @param targetStart - target date/time to get previous recurrence.
     * @return closest recurrence DTSTART value, without going over.
     */
    public Temporal getClosestStart(Temporal targetStart)
    {
        final Temporal match;
        final Temporal dateTimeStart;
        final RecurrenceRule2 recurrenceRule;
        
        dateTimeStart = component.getDateTimeStart().getValue();
        
        recurrenceRule = (component.getRecurrenceRule() != null) ? component.getRecurrenceRule().getValue() : null;

        // adjust start to ensure its not before dateTimeStart
        final Temporal start2 = (DateTimeUtilities.isBefore(targetStart, dateTimeStart)) ? dateTimeStart : targetStart;
        final Temporal latestCacheValue;

        
        if (recurrenceRule == null)
        { // if individual event
            return null;
        }

        // check if cache needs to be cleared (changes to RRULE or DTSTART)
        if ((dateTimeStartLast != null) && (rRuleLast != null))
        {
            boolean startChanged = ! dateTimeStart.equals(dateTimeStartLast);
            boolean rRuleChanged = ! recurrenceRule.equals(rRuleLast);
            if (startChanged || rRuleChanged)
            {
                temporalCache = null;
                cacheStart = 0;
                cacheEnd = 0;
                skipCounter = 0;
                dateTimeStartLast = dateTimeStart;
                rRuleLast = recurrenceRule;
            }
        } else
        { // save current DTSTART and RRULE for next time
            dateTimeStartLast = dateTimeStart;
            rRuleLast = recurrenceRule;
        }
        
        // use cache if available to find matching start date or date/time
        if (temporalCache != null)
        {
            // Reorder cache to maintain centered and sorted
            final int len = temporalCache.length;
            final Temporal[] p1;
            final Temporal[] p2;
            if (cacheEnd < cacheStart) // high values have wrapped from end to beginning
            {
                p1 = Arrays.copyOfRange(temporalCache, cacheStart, len);
                p2 = Arrays.copyOfRange(temporalCache, 0, Math.min(cacheEnd+1,len));
            } else if (cacheEnd > cacheStart) // low values have wrapped from beginning to end
            {
                p2 = Arrays.copyOfRange(temporalCache, cacheStart, len);
                p1 = Arrays.copyOfRange(temporalCache, 0, Math.min(cacheEnd+1,len));
            } else
            {
                p1 = null;
                p2 = null;
            }
            if (p1 != null)
            { // copy elements to accommodate wrap and restore sort order
                int p1Index = 0;
                int p2Index = 0;
                for (int i=0; i<len; i++)
                {
                    if (p1Index < p1.length)
                    {
                        temporalCache[i] = p1[p1Index];
                        p1Index++;
                    } else if (p2Index < p2.length)
                    {
                        temporalCache[i] = p2[p2Index];
                        p2Index++;
                    } else
                    {
                        cacheEnd = i;
                        break;
                    }
                }
            }

            // Find match in cache
            latestCacheValue = temporalCache[cacheEnd];
            if ((! DateTimeUtilities.isBefore(start2, temporalCache[cacheStart])))
            {
                Temporal m = latestCacheValue;
                for (int i=cacheStart; i<cacheEnd+1; i++)
                {
                    if (DateTimeUtilities.isAfter(temporalCache[i], start2))
                    {
                        m = temporalCache[i-1];
                        break;
                    }
                }
                match = m;
            } else
            { // all cached values too late - start over
                cacheStart = 0;
                cacheEnd = 0;
                temporalCache[cacheStart] = dateTimeStart;
                match = dateTimeStart;
            }
        } else
        { // no previous cache.  initialize new array with dateTimeStart as first value.
            temporalCache = new Temporal[CACHE_RANGE];
            temporalCache[cacheStart] = dateTimeStart;
            match = dateTimeStart;
        }
        return match;
    }
    
    
    /** add to cache while streaming recurrences */
    public Stream<Temporal> makeCache(Stream<Temporal> inStream)
    {
        Temporal earliestCacheValue = temporalCache[cacheStart];
        Temporal latestCacheValue = temporalCache[cacheEnd];
//        System.out.println("makeCache:" + earliestCacheValue + " " + latestCacheValue + " " + component.getRecurrences());
        Stream<Temporal> outStream = inStream
                .peek(t ->
                { // save new values in cache
                    if (component.getRecurrenceRule() != null)
                    {
                        if (DateTimeUtilities.isBefore(t, earliestCacheValue))
                        {
                            if (skipCounter == CACHE_SKIP)
                            {
                                cacheStart--;
                                if (cacheStart < 0) cacheStart = CACHE_RANGE - 1;
                                if (cacheStart == cacheEnd) cacheEnd--; // just overwrote oldest value - push cacheEnd down
                                temporalCache[cacheStart] = t;
                                skipCounter = 0;
                            } else skipCounter++;
                        }
                        if (DateTimeUtilities.isAfter(t, latestCacheValue))
                        {
                            if (skipCounter == CACHE_SKIP)
                            {
                                cacheEnd++;
                                if (cacheEnd == CACHE_RANGE) cacheEnd = 0;
                                if (cacheStart == cacheEnd) cacheStart++; // just overwrote oldest value - push cacheStart up
                                temporalCache[cacheEnd] = t;
                                skipCounter = 0;
                            } else skipCounter++;
                        }
                        // check if start or end needs to wrap
                        if (cacheEnd < 0) cacheEnd = CACHE_RANGE - 1;
                        if (cacheStart == CACHE_RANGE) cacheStart = 0;
                    }
                });
        return outStream;
    }

//    /** Stream of date/times that indicate the start of the event(s).
//     * For a VEvent without RRULE the stream will contain only one date/time element.
//     * A VEvent with a RRULE the stream contains more than one date/time element.  It will be infinite 
//     * if COUNT or UNTIL is not present.  The stream has an end when COUNT or UNTIL condition is met.
//     * Starts on startDateTime, which must be a valid event date/time, not necessarily the
//     * first date/time (DTSTART) in the sequence. 
//     * 
//     * @param start - starting date or date/time for which occurrence start date or date/time
//     * are generated by the returned stream
//     * @return stream of starting dates or date/times for occurrences after rangeStart
//     */
//    @Deprecated
//    public Stream<Temporal> streamNoCache(Temporal start)
//    {
//        final Comparator<Temporal> temporalComparator;
//        if (start instanceof LocalDate)
//        {
//            temporalComparator = (t1, t2) -> ((LocalDate) t1).compareTo((LocalDate) t2);
//        } else if (start instanceof LocalDateTime)
//        {
//            temporalComparator = (t1, t2) -> ((LocalDateTime) t1).compareTo((LocalDateTime) t2);            
//        } else if (start instanceof ZonedDateTime)
//        {
//            temporalComparator = (t1, t2) -> ((ZonedDateTime) t1).compareTo((ZonedDateTime) t2);
//        } else
//        {
//            throw new DateTimeException("Unsupported Temporal type:" + start.getClass().getSimpleName());
//        }
//        final Stream<Temporal> stream1;
//        if (component.getRecurrenceRule() == null)
//        { // if individual event
//            stream1 = Arrays.asList(component.getDateTimeStart().getValue())
//                    .stream()
//                    .map(v -> (Temporal) v)
//                    .filter(d -> ! DateTimeUtilities.isBefore(d, start));
//        } else
//        { // if has recurrence rule
//            stream1 = component.getRecurrenceRule().getValue().streamRecurrences(component.getDateTimeStart().getValue());
//        }
//        // If present, add recurrence list
//        final Stream<Temporal> stream2;
//        if (component.getRecurrences() == null)
//        {
//            stream2 = stream1;
//        } else
//        {
//            stream2 = RecurrenceRuleCache.merge(
//                         stream1
//                       , component.getRecurrences()
//                               .stream()
//                               .flatMap(r -> r.getValue().stream())
//                               .map(v -> (Temporal) v)
//                               .sorted(temporalComparator)
//                       , temporalComparator);
//        }
//        
//        final Stream<Temporal> stream3;
//        if ((component instanceof VComponentDisplayable) && (((VComponentDisplayable<?>) component).getExceptions() != null))
//        {
//            /** Remove date/times in exDates set */
//            List<Temporal> exceptions = ((VComponentDisplayable<?>) component).getExceptions()
//                    .stream()
//                    .flatMap(r -> r.getValue().stream())
//                    .map(v -> (Temporal) v)
//                    .sorted(temporalComparator)
//                    .collect(Collectors.toList());
//            stream3 = stream2.filter(d -> ! exceptions.contains(d));
//        } else
//        {
//            stream3 = stream2;
//        }
//        return stream3.filter(t -> ! DateTimeUtilities.isBefore(t, start));
//    }

}
