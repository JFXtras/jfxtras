package jfxtras.icalendarfx.properties.component.recurrence;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.util.StringConverter;
import jfxtras.icalendarfx.properties.PropBaseDateTime;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

/**
 * Abstract class for Exceptions and Recurrences
 * 
 * @author David Bal
 *
 * @param <U> - subclass
 * @param <T> - property value type
 * @see ExceptionDates
 * @see RecurrenceDates
 */
public abstract class PropertyBaseRecurrence<U> extends PropBaseDateTime<ObservableSet<Temporal>, U>
{
    private ZoneId zone;
    private DateTimeType myType;
//    private final SetChangeListener<Temporal> recurrenceListener = (SetChangeListener<Temporal>) (SetChangeListener.Change<? extends Temporal> change) ->
//    {
//        if (change.wasAdded())
//        {
//            Temporal newTemporal = change.getElementAdded();
//            DateTimeType newType = DateTimeType.of(newTemporal);
//            if (newType != myType)
//            {
//                change.getSet().remove(newTemporal);
//                throw new DateTimeException("Can't add new element of type " + newType + ". New elements must match type of existing elements (" + myType + ")");
//            }
//            if (newTemporal instanceof ZonedDateTime)
//            {
//                ZoneId myZone = ((ZonedDateTime) newTemporal).getZone();
//                if (! myZone.equals(zone))
//                {
//                    change.getSet().remove(newTemporal);
//                    throw new DateTimeException("Can't add new element with ZoneId of " + myZone + ". New elements must match ZoneId of existing elements (" + zone + ")");
//                }
//            }
//        }
//    };
    
    private final StringConverter<ObservableSet<Temporal>> CONVERTER = new StringConverter<ObservableSet<Temporal>>()
    {
        @Override
        public String toString(ObservableSet<Temporal> object)
        {
            return object.stream()
                    .sorted()
                    .map(t -> DateTimeUtilities.temporalToString(t))
                    .collect(Collectors.joining(","));
        }

        @Override
        public ObservableSet<Temporal> fromString(String string)
        {
            Set<Temporal> list = Arrays.stream(string.split(","))
                    .map(s -> DateTimeUtilities.temporalFromString(s))
                    .collect(Collectors.toSet());
            TreeSet<Temporal> treeSet = new TreeSet<Temporal>(DateTimeUtilities.TEMPORAL_COMPARATOR);
            treeSet.addAll(list);
            return FXCollections.observableSet(list);
        }
    };
    
    /*
     * CONSTRUCTORS
     */
    public PropertyBaseRecurrence(ObservableSet<Temporal> value)
    {
        this();
        setValue(value);
        if (! isValid())
        {
            throw new IllegalArgumentException("Error in parsing " + value);
        }
    }
    
    public PropertyBaseRecurrence(Temporal...temporals)
    {
        this();
        Set<Temporal> tree = new TreeSet<>(DateTimeUtilities.TEMPORAL_COMPARATOR);
        tree.addAll(Arrays.asList(temporals));
        setValue(FXCollections.observableSet(tree));
        if (! isValid())
        {
            throw new IllegalArgumentException("Error in parsing " + temporals);
        }
    }
    
    public PropertyBaseRecurrence()
    {
        super(FXCollections.observableSet(new TreeSet<>(DateTimeUtilities.TEMPORAL_COMPARATOR)));
        setConverter(CONVERTER);
    }

    public PropertyBaseRecurrence( PropertyBaseRecurrence<U> source)
    {
        super(source);
    }

    // Listen to additions to collection to ensure time zone is consistent
    private void setupListener()
    {
        if (! getValue().isEmpty())
        {
            Temporal sampleValue = getValue().iterator().next();
            myType = DateTimeType.of(sampleValue);
            SetChangeListener<Temporal> recurrenceListener = (SetChangeListener<Temporal>) (SetChangeListener.Change<? extends Temporal> change) ->
            {
                if (change.wasAdded())
                {
                    Temporal newTemporal = change.getElementAdded();
                    DateTimeType newType = DateTimeType.of(newTemporal);
                    if (newType != myType)
                    {
                        change.getSet().remove(newTemporal);
                        throw new DateTimeException("Can't add new element of type " + newType + ". New elements must match type of existing elements (" + myType + ")");
                    }
                    if (newTemporal instanceof ZonedDateTime)
                    {
                        ZoneId myZone = ((ZonedDateTime) newTemporal).getZone();
                        if (! myZone.equals(zone))
                        {
                            change.getSet().remove(newTemporal);
                            throw new DateTimeException("Can't add new element with ZoneId of " + myZone + ". New elements must match ZoneId of existing elements (" + zone + ")");
                        }
                    }
                }
            };
            getValue().addListener(recurrenceListener);
            if (sampleValue instanceof ZonedDateTime)
            {
                zone = ((ZonedDateTime) sampleValue).getZone();
            }
        }

    }

    @Override
    public void setValue(ObservableSet<Temporal> value)
    {
        if (! value.isEmpty())
        {
            Temporal sampleValue = value.iterator().next();
            if (sampleValue instanceof LocalDate)
            {
                setValueType(ValueType.DATE); // must set value parameter to force output of VALUE=DATE
            } else if (! (sampleValue instanceof LocalDateTime) && ! (sampleValue instanceof ZonedDateTime))
            {
                throw new RuntimeException("can't convert property value to type: " + sampleValue.getClass().getSimpleName() +
                        ". Accepted types are: " + propertyType().allowedValueTypes());                
            }
        }
        super.setValue(value);
        setupListener();
    }
    
    @Override
    public boolean isValid()
    {
        if (! getValue().isEmpty())
        {
            Temporal sampleValue = getValue().iterator().next();
            // ensure all ZoneId values are the same
            if (sampleValue instanceof ZonedDateTime)
            {
                zone = ((ZonedDateTime) sampleValue).getZone();
                boolean valuesMatchZone = getValue()
                        .stream()
                        .map(t -> ((ZonedDateTime) t).getZone())
                        .allMatch(z -> z.equals(zone));
                return valuesMatchZone && super.isValid();
            }
        }
        return super.isValid();
    }
        
    @Override
    protected ObservableSet<Temporal> copyValue(ObservableSet<Temporal> source)
    {
        ObservableSet<Temporal> newCollection = FXCollections.observableSet(new TreeSet<>(DateTimeUtilities.TEMPORAL_COMPARATOR));
        newCollection.addAll(source);
        return newCollection;
    }
}
