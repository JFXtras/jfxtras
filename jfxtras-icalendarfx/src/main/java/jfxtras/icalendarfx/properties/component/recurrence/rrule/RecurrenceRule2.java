package jfxtras.icalendarfx.properties.component.recurrence.rrule;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import jfxtras.icalendarfx.VChild;
import jfxtras.icalendarfx.VParent;
import jfxtras.icalendarfx.VParentBase;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByHour;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMinute;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByRule;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.BySecond;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByYearDay;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;
import jfxtras.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

/**
 * RRULE
 * Recurrence Rule
 * RFC 5545 iCalendar 3.3.10 page 38
 * 
 * Contains the following Recurrence Rule elements:
 * COUNT
 * UNTIL
 * FREQUENCY
 * INTERVAL
 * BYxxx RULES in a List
 * 
 * The value part of the recurrence rule.  It supports the following elements: <br>
 * ( "FREQ" "=" freq ) <br>
 * ( "UNTIL" "=" enddate ) <br>
 * ( "COUNT" "=" 1*DIGIT ) <br>
 * ( "INTERVAL" "=" 1*DIGIT ) <br>
 * ( "BYSECOND" "=" byseclist ) <br>
 * ( "BYMINUTE" "=" byminlist ) <br>
 * ( "BYHOUR" "=" byhrlist ) <br>
 * ( "BYDAY" "=" bywdaylist ) <br>
 * ( "BYMONTHDAY" "=" bymodaylist ) <br>
 * ( "BYYEARDAY" "=" byyrdaylist ) <br>
 * ( "BYWEEKNO" "=" bywknolist ) <br>
 * ( "BYMONTH" "=" bymolist ) <br>
 * ( "BYSETPOS" "=" bysplist ) <br>
 * ( "WKST" "=" weekday ) <br>
 * 
 * In addition to methods to support iCalendar recurrence rule parts, there is a method
 * {@link #streamRecurrences(Temporal)}  that produces a stream of start date/times for the recurrences
 * defined by the rule.
 * 
 * @author David Bal
 * @see RecurrenceRule
 *
 */
// TODO - LISTENER TO PREVENT COUNT AND UNTIL FROM BOTH BEING SET
public class RecurrenceRule2 extends VParentBase implements VChild
{
    private VParent myParent;
    @Override public void setParent(VParent parent) { myParent = parent; }
    @Override public VParent getParent() { return myParent; }
    
    private static final String NAME = "RRULE";
    @Override public String name() { return NAME; }
    
    /** 
     * BYxxx Rules
     * RFC 5545, iCalendar 3.3.10 Page 42
     * 
     * List contains any of the following.  The following list also indicates the processing order:
     * {@link ByMonth} {@link ByWeekNo} {@link ByYearDay} {@link ByMonthDay} {@link ByDay} {@link ByHour}
     * {@link ByMinute} {@link BySecond} {@link BySetPos}
     * 
     * BYxxx rules modify the recurrence set by either expanding or limiting it.
     * 
     * Each BYxxx rule can only occur once
     *  */
    public ObservableList<ByRule<?>> byRules() { return byRules; }
    final private ObservableList<ByRule<?>> byRules; // = FXCollections.observableArrayList();
//    public void setByRules(ObservableList<ByRule<?>> byRules) { this.byRules = byRules; }
//    public RecurrenceRule3 withByRules(ObservableList<ByRule<?>> byRules) { setByRules(byRules); return this; }
    public RecurrenceRule2 withByRules(ByRule<?>...byRules)
    {
        for (ByRule<?> myByRule : byRules)
        {
            byRules().add(myByRule);
        }
        return this;
    }
    public RecurrenceRule2 withByRules(String...byRules)
    {
        Arrays.stream(byRules).forEach(c -> parseContent(c));
        return this;
    }
    
    /** Return ByRule associated with class type */
    public ByRule<?> lookupByRule(Class<? extends ByRule<?>> byRuleClass)
    {
        Optional<ByRule<?>> rule = byRules()
                .stream()
                .filter(r -> byRuleClass.isInstance(r))
                .findFirst();
        return (rule.isPresent()) ? rule.get() : null;
    }
    
    /**
     * COUNT:
     * RFC 5545 iCalendar 3.3.10, page 41
     * 
     * The COUNT rule part defines the number of occurrences at which to
     * range-bound the recurrence.  The "DTSTART" property value always
     * counts as the first occurrence.
     */
    public ObjectProperty<Count> countProperty()
    {
        if (count == null)
        {
            count = new SimpleObjectProperty<>(this, RRuleElementType.COUNT.toString());
            orderer().registerSortOrderProperty(count);
            // TODO - add listener to ensure COUNT and UNTIL are not both set
            // listener to ensure >0 throw new IllegalArgumentException("COUNT can't be less than 0. (" + count + ")");
//            else throw new IllegalArgumentException("can't set COUNT if UNTIL is already set.");
        }
        return count;
    }
    private ObjectProperty<Count> count;
    public Count getCount() { return (count == null) ? null : countProperty().get(); }
    public void setCount(Count count) { countProperty().set(count); }
    public void setCount(int count) { setCount(new Count(count)); }
    public RecurrenceRule2 withCount(Count count)
    {
        setCount(count);
        return this;
    }
    public RecurrenceRule2 withCount(int count)
    {
        setCount(count);
        return this;
    }
    
    /** 
     * FREQUENCY
     * FREQ
     * RFC 5545 iCalendar 3.3.10 p40
     * 
     * required element
     * 
     * The FREQ rule part identifies the type of recurrence rule.  This
     * rule part MUST be specified in the recurrence rule.  Valid values
     * include SECONDLY, to specify repeating events based on an interval
     * of a second or more; MINUTELY, to specify repeating events based
     * on an interval of a minute or more; HOURLY, to specify repeating
     * events based on an interval of an hour or more; DAILY, to specify
     * repeating events based on an interval of a day or more; WEEKLY, to
     * specify repeating events based on an interval of a week or more;
     * MONTHLY, to specify repeating events based on an interval of a
     * month or more; and YEARLY, to specify repeating events based on an
     * interval of a year or more.
     */
    public ObjectProperty<Frequency> frequencyProperty() { return frequency; }
    final private ObjectProperty<Frequency> frequency; // initialized in constructor
    public Frequency getFrequency() { return frequency.get(); }
    public void setFrequency(Frequency frequency) { frequencyProperty().set(frequency); }
    public void setFrequency(String frequency) { setFrequency(Frequency.parse(frequency)); }
    public void setFrequency(FrequencyType frequency) { setFrequency(new Frequency(frequency)); }
    public RecurrenceRule2 withFrequency(Frequency frequency)
    {
        setFrequency(frequency);
        return this;
    }
    public RecurrenceRule2 withFrequency(String frequency)
    {
        setFrequency(frequency);
        return this;
    }
    public RecurrenceRule2 withFrequency(FrequencyType frequency)
    {
        setFrequency(frequency);
        return this;
    }
    
    /**
     * INTERVAL
     * RFC 5545 iCalendar 3.3.10, page 40
     * 
     * The INTERVAL rule part contains a positive integer representing at
     * which intervals the recurrence rule repeats.  The default value is
     * "1", meaning every second for a SECONDLY rule, every minute for a
     * MINUTELY rule, every hour for an HOURLY rule, every day for a
     * DAILY rule, every week for a WEEKLY rule, every month for a
     * MONTHLY rule, and every year for a YEARLY rule.  For example,
     * within a DAILY rule, a value of "8" means every eight days.
     */
    public ObjectProperty<Interval> intervalProperty()
    {
        if (interval == null)
        {
            interval = new SimpleObjectProperty<>(this, RRuleElementType.INTERVAL.toString());
            orderer().registerSortOrderProperty(interval);
        }
        return interval;
    }
    private ObjectProperty<Interval> interval;
    public Interval getInterval()
    {
        return (intervalProperty() == null) ? null : intervalProperty().get();
    }
    public void setInterval(Interval interval)
    {
        intervalProperty().set(interval);
    }
    public void setInterval(Integer interval)
    {
        setInterval(new Interval(interval));
    }
    public RecurrenceRule2 withInterval(int interval)
    {
        setInterval(interval);
        return this;
    }
    public RecurrenceRule2 withInterval(Interval interval)
    {
        setInterval(interval);
        return this;
    }
    
    /**
     * UNTIL:
     * RFC 5545 iCalendar 3.3.10, page 41
     * 
     * The UNTIL rule part defines a DATE or DATE-TIME value that bounds
     * the recurrence rule in an inclusive manner.  If the value
     * specified by UNTIL is synchronized with the specified recurrence,
     * this DATE or DATE-TIME becomes the last instance of the
     * recurrence.  The value of the UNTIL rule part MUST have the same
     * value type as the "DTSTART" property.  Furthermore, if the
     * "DTSTART" property is specified as a date with local time, then
     * the UNTIL rule part MUST also be specified as a date with local
     * time.  If the "DTSTART" property is specified as a date with UTC
     * time or a date with local time and time zone reference, then the
     * UNTIL rule part MUST be specified as a date with UTC time.  In the
     * case of the "STANDARD" and "DAYLIGHT" sub-components the UNTIL
     * rule part MUST always be specified as a date with UTC time.  If
     * specified as a DATE-TIME value, then it MUST be specified in a UTC
     * time format.  If not present, and the COUNT rule part is also not
     * present, the "RRULE" is considered to repeat forever
     */
    public SimpleObjectProperty<Until> untilProperty()
    {
        if (until == null)
        {
            until = new SimpleObjectProperty<>(this, RRuleElementType.UNTIL.toString());
            orderer().registerSortOrderProperty(until);
            // TODO - add listener to ensure COUNT and UNTIL are not both set
            // TODO - LISTENER TO ENSURE UTC OR DATE
            // if ((DateTimeType.of(until) != DateTimeType.DATE) && (DateTimeType.of(until) != DateTimeType.DATE_WITH_UTC_TIME))
        }
        return until;
    }
    private SimpleObjectProperty<Until> until;
    public Until getUntil() { return (until == null) ? null : untilProperty().getValue(); }
    public void setUntil(Until until) { untilProperty().set(until); }
    public void setUntil(Temporal until)
    {
        setUntil(new Until(until));
        //  TODO - all properties need to make new object here or ORDERER listener won't fire
        //  I NEED TO REPLACE THE SET-VALUE METHOD FOR ALL PROPERTIES
//        System.out.println("old until:" + getUntil());
//        if (getUntil() == null)
//        {
//            setUntil(new Until(until));
//        } else
//        {
//            getUntil().setValue(until);
//        }
    }
    public void setUntil(String until) { setUntil(DateTimeUtilities.temporalFromString(until)); }
    public RecurrenceRule2 withUntil(Temporal until)
    {
        setUntil(until);
        return this;
    }
    public RecurrenceRule2 withUntil(String until)
    {
        setUntil(until);
        return this;
    }
    public RecurrenceRule2 withUntil(Until until)
    {
        setUntil(until);
        return this;
    }
    
    /**
     * Week Start
     * WKST:
     * RFC 5545 iCalendar 3.3.10, page 42
     * 
     * The WKST rule part specifies the day on which the workweek starts.
     * Valid values are MO, TU, WE, TH, FR, SA, and SU.  This is
     * significant when a WEEKLY "RRULE" has an interval greater than 1,
     * and a BYDAY rule part is specified.  This is also significant when
     * in a YEARLY "RRULE" when a BYWEEKNO rule part is specified.  The
     * default value is MO.
     */
    public SimpleObjectProperty<WeekStart> weekStartProperty()
    {
        if (weekStart == null)
        {
            weekStart = new SimpleObjectProperty<>(this, RRuleElementType.WEEK_START.toString());
            orderer().registerSortOrderProperty(weekStart);
            weekStart.addListener((obs, oldValue, newValue) ->
            {
                // bind to values in the Byxxx rules that use it
                addWeekStartBindings(lookupByRule(ByDay.class));
                addWeekStartBindings(lookupByRule(ByWeekNumber.class));                
            });
        }
        return weekStart;
    }
    private SimpleObjectProperty<WeekStart> weekStart;
    public WeekStart getWeekStart() { return (weekStart == null) ? null : weekStartProperty().get(); }
    public void setWeekStart(WeekStart weekStart) { weekStartProperty().set(weekStart); }
    public void setWeekStart(DayOfWeek weekStart) { weekStartProperty().set(new WeekStart(weekStart)); }
    public RecurrenceRule2 withWeekStart(WeekStart weekStart) { setWeekStart(weekStart); return this; }
    public RecurrenceRule2 withWeekStart(DayOfWeek weekStart) { setWeekStart(weekStart); return this; }
    
   // bind to values in the Byxxx rules that use Week Start
    private void addWeekStartBindings(ByRule<?> rule)
    {
        if (getWeekStart() != null)
        {
            if (rule instanceof ByDay)
            {
                ((ByDay) rule).weekStartProperty().bind(getWeekStart().valueProperty());
            } else if (rule instanceof ByWeekNumber)
            {
                ((ByWeekNumber) rule).weekStartProperty().bind(getWeekStart().valueProperty());
            }
        }
    }
    
//    /**
//     * List of all recurrence rule elements found in object.
//     * The list is unmodifiable.
//     * 
//     * @return - the list of elements
//     * @deprecated  not needed due to addition of Orderer, may be deleted
//     */
//    @Deprecated
//    public List<RRuleElementType> elements()
//    {
//        List<RRuleElementType> populatedElements = Arrays.stream(RRuleElementType.values())
//            .filter(p -> (p.getElement(this) != null))
//            .collect(Collectors.toList());
//      return Collections.unmodifiableList(populatedElements);
//    }
    
    /*
     * SORT ORDER FOR CHILD ELEMENTS
     */
//    final private Orderer orderer;
//    @Override
//    public Orderer orderer() { return orderer; }

//    /** Callback to enable copy-element-behavior from {@link Orderer#copyChildrenFrom(VParent) } */
//    private Callback<VElement, Void> copyElementChildCallback = (child) ->
//    {
//        RRuleElementType type = RRuleElementType.enumFromClass(child.getClass());
//        type.copyElement((RRuleElement<?>) child, this);
//        return null;
//    };
    
    @Override
    @Deprecated

    protected Callback<VChild, Void> copyIntoCallback()
    {        
        return (child) ->
        {
            RRuleElementType type = RRuleElementType.enumFromClass(child.getClass());
            type.copyElement((RRuleElement<?>) child, this);
            return null;
        };
    }
    
    @Override
    public void copyInto(VParent destination)
    {
        super.copyInto(destination);
        childrenUnmodifiable().forEach((childSource) -> 
        {
            RRuleElementType type = RRuleElementType.enumFromClass(childSource.getClass());
            if (type != null)
            {
                type.copyElement((RRuleElement<?>) childSource, (RecurrenceRule2) destination);
            } 
        });
    }
    
//    /** 
//     * SORT ORDER
//     * 
//     * Element sort order map.  Key is element, value is the sort order.  The map is automatically
//     * populated when parsing the content lines to preserve the existing property order.
//     * 
//     * When producing the content lines, if a element is not present in the map, it is put at
//     * the end of the sorted ones in the order appearing in {@link #RecurrenceRuleElement} 
//     * Generally, this map shouldn't be modified.  Only modify it when you want
//     * to force a specific property order (e.g. unit testing).
//     */
//    @Deprecated
//    public Map<RRuleElementType, Integer> elementSortOrder() { return elementSortOrder; }
//    @Deprecated
//    final private Map<RRuleElementType, Integer> elementSortOrder = new HashMap<>();
//    private Integer elementCounter = 0;
    
    /*
     * CONSTRUCTORS
     */
    
    public RecurrenceRule2()
    {
        frequency = new SimpleObjectProperty<>(this, RRuleElementType.FREQUENCY.toString());
        orderer().registerSortOrderProperty(frequencyProperty());
        byRules = FXCollections.observableArrayList();
        orderer().registerSortOrderProperty(byRules);
        
        // Listener that ensures user doesn't add same ByRule a second time.  Also keeps the byRules list sorted.
        byRules().addListener((ListChangeListener<? super ByRule<?>>) (change) ->
        {
            while (change.next())
            {
                if (change.wasAdded())
                {
                    change.getAddedSubList().stream().forEach(c ->
                    {
                        ByRule<?> newByRule = c;
                        long alreadyPresent = change.getList()
                                .stream()
                                .map(r -> r.elementType())
                                .filter(p -> p.equals(c.elementType()))
                                .count();
                        if (alreadyPresent > 1)
                        {
                            throw new IllegalArgumentException("Can't add " + newByRule.getClass().getSimpleName() + " (" + c.elementType() + ") more than once.");
                        }
                        addWeekStartBindings(newByRule);
                    });
                    
                    // Sort after addition
//                    orderer().unregisterSortOrderProperty(byRules());
                    Collections.sort(change.getList());
//                    orderer().registerSortOrderProperty(byRules());
                }
            }
        });
    }

    // Copy constructor
    public RecurrenceRule2(RecurrenceRule2 source)
    {
        this();
        source.copyInto(this);
//        copyChildrenFrom(source);
    }
    
    /** Parse component from content line */
    @Override
    public List<String> parseContent(String contentLine)
    {
        ICalendarUtilities.contentToParameterListPair(contentLine)
                .stream()
                .forEach(entry ->
                {
                    RRuleElementType element = RRuleElementType.enumFromName(entry.getKey());
                    if (element != null)
                    {
                        element.parse(this, entry.getValue());
                    } else
                    {
                        throw new IllegalArgumentException("Unsupported Recurrence Rule element: " + entry.getKey());                        
                    }
                });
        return errors();
    }

    /**
     * STREAM RECURRENCES
     * 
     * Resulting stream of start date/times by applying Frequency temporal adjuster and all, if any,
     *
     * Starts on startDateTime, which MUST be a valid occurrence date/time, but not necessarily the
     * first date/time (DTSTART) in the sequence. A later startDateTime can be used to more efficiently
     * get to later dates in the stream.
     * 
     * @param start - starting point of stream (MUST be a valid occurrence date/time)
     * @return
     */
    public Stream<Temporal> streamRecurrences(Temporal start)
    {
        int interval = (getInterval() == null) ? Interval.DEFAULT_INTERVAL : getInterval().getValue();
        Stream<Temporal> frequencyStream = getFrequency().streamRecurrences(start, interval);
        
        Stream<Temporal> recurrenceStream = frequencyStream
                .flatMap(value ->
                {
                    // process byRules
                    chronoUnit = getFrequency().getValue().getChronoUnit(); // initial chronoUnit from Frequency
                    myStream = Arrays.asList(value).stream();
                    byRules().stream()
                            .sorted()
                            .forEach(rule ->
                            {
                                myStream = rule.streamRecurrences(myStream, chronoUnit, start);
                                chronoUnit = rule.getChronoUnit();
                            });
                    // must filter out too early recurrences
                    return myStream.filter(r -> ! DateTimeUtilities.isBefore(r, start));
                });
        
        if (getCount() != null)
        {
            return recurrenceStream.limit(getCount().getValue());
        } else if (getUntil() != null)
        {
            ZoneId zone = (start instanceof ZonedDateTime) ? ((ZonedDateTime) start).getZone() : null;
            Temporal convertedUntil = DateTimeType.of(start).from(getUntil().getValue(), zone);
            return takeWhile(recurrenceStream, a -> ! DateTimeUtilities.isAfter(a, convertedUntil));
        }
        return recurrenceStream;
    }
    private ChronoUnit chronoUnit; // must be field instead of local variable due to use in lambda expression
    private Stream<Temporal> myStream; // must be field instead of local variable due to use in lambda expression
    
    /**
     * Determines if recurrence set is goes on forever
     * 
     * @return - true if recurrence set is infinite, false otherwise
     */
    public boolean isInfinite()
    {
        return ((getCount() == null) && (getUntil() == null));
    }
    
    
    // takeWhile - From http://stackoverflow.com/questions/20746429/limit-a-stream-by-a-predicate
    static <T> Spliterator<T> takeWhile(Spliterator<T> splitr, Predicate<? super T> predicate)
    {
      return new Spliterators.AbstractSpliterator<T>(splitr.estimateSize(), 0) {
        boolean stillGoing = true;
        @Override public boolean tryAdvance(Consumer<? super T> consumer) {
          if (stillGoing) {
            boolean hadNext = splitr.tryAdvance(elem -> {
              if (predicate.test(elem)) {
                consumer.accept(elem);
              } else {
                stillGoing = false;
              }
            });
            return hadNext && stillGoing;
          }
          return false;
        }
      };
    }

    static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> predicate) {
       return StreamSupport.stream(takeWhile(stream.spliterator(), predicate), false);
    }
    
    @Override
    public String toContent()
    {
        return orderer().sortedContent().stream()
              .collect(Collectors.joining(";"));
    }
    
    @Override
    public List<String> errors()
    {
        List<String> errors = new ArrayList<>();
        if (getFrequency() == null)
        {
            errors.add("FREQ is not present.  FREQ is REQUIRED and MUST NOT occur more than once");
        }
        boolean isUntilPresent = getUntil() != null;
        boolean isCountPresent = getCount() != null;
        if (isUntilPresent && isCountPresent)
        {
            errors.add("UNTIL and COUNT are both present.  UNTIL or COUNT rule parts are OPTIONAL, but they MUST NOT both occur.");
        }
        childrenUnmodifiable().forEach(c -> errors.addAll(c.errors()));
//        byRules().forEach(b -> errors.addAll(b.errors()));
        return errors;
    }
    
    @Override
    public String toString()
    {
        return super.toString() + ", " + toContent();
    }
    
    // Note: can't check equals or hashCode of parents - causes stack overflow
    
//    @Override
//    public boolean equals(Object obj)
//    {
//        boolean childrenEquals = super.equals(obj);
//        if (! childrenEquals) return false;
//        PropertyBase<?,?> testObj = (PropertyBase<?,?>) obj;
//        boolean parentEquals = Objects.equal(this.getParent(), testObj.getParent());
//        return parentEquals;
//    }
//    
//    @Override
//    public int hashCode()
//    {
//        int hash = super.hashCode();
//        final int prime = 31;
//        hash = prime * hash + ((getParent() == null) ? 0 : getParent().hashCode());
//        return hash;
//    }

//    @Override
//    public boolean equals(Object obj)
//    {
//        if (obj == this) return true;
//        if((obj == null) || (obj.getClass() != getClass())) {
//            return false;
//        }
//        RecurrenceRule2 testObj = (RecurrenceRule2) obj;
//
//        List<RRuleElementType> myElements = elements();
//        // elements only found in testObj.  Some may be equal because default value may match assigned value
//        List<RRuleElementType> testElements = testObj.elements()
//                .stream()
//                .filter(e -> ! myElements.contains(e))
//                .collect(Collectors.toList());
//        
//        for (RRuleElementType t : myElements)
//        {
//            RRuleElement<?> myElement = t.getElement(this);
//            RRuleElement<?> otherElement = t.getElement(testObj);
//            testElements.remove(t);
//            if (! myElement.equals(otherElement))
//            {
//                return false;
//            }
//        }
//        // check elements matching default values
//        for (RRuleElementType t : testElements)
//        {
//            RRuleElement<?> otherElement = t.getElement(testObj);
//            try
//            {
//                RRuleElement<?> defaultElement = otherElement.getClass().newInstance();
//                if (! otherElement.equals(defaultElement))
//                {
//                    return false;
//                }
//            } catch (InstantiationException | IllegalAccessException e1)
//            {
//                e1.printStackTrace();
//            }
//        }
//        return true;
//    }
//    
//    @Override
//    public int hashCode()
//    {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((byRules == null) ? 0 : byRules.hashCode());
//        result = prime * result + ((count == null) ? 0 : count.hashCode());
//        result = prime * result + ((frequency == null) ? 0 : frequency.hashCode());
//        result = prime * result + ((interval == null) ? 0 : interval.hashCode());
//        result = prime * result + ((until == null) ? 0 : until.hashCode());
//        result = prime * result + ((weekStart == null) ? 0 : weekStart.hashCode());
//        return result;
//    }
    
    public static RecurrenceRule2 parse(String propertyContent)
    {
        RecurrenceRule2 property = new RecurrenceRule2();
        property.parseContent(propertyContent);
        return property;
    }
}
