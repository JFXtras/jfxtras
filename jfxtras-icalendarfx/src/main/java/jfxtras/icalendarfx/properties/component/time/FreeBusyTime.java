package jfxtras.icalendarfx.properties.component.time;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Pair;
import javafx.util.StringConverter;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.parameters.FreeBusyType;
import jfxtras.icalendarfx.parameters.ParameterType;
import jfxtras.icalendarfx.parameters.FreeBusyType.FreeBusyTypeEnum;
import jfxtras.icalendarfx.properties.PropFreeBusy;
import jfxtras.icalendarfx.properties.PropertyBase;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

/**
 * FREEBUSY
 * Free/Busy Time
 * RFC 5545, 3.8.2.6, page 100
 * 
 * This property defines one or more free or busy time intervals.
 * 
 * These time periods can be specified as either a start
 * and end DATE-TIME or a start DATE-TIME and DURATION.  The date and
 * time MUST be a UTC time format.  Internally, the values are stored only as 
 * start DATE-TIME and DURATION.  Any values entered as start and end as both
 * DATE-TIME are converted to the start DATE-TIME and DURATION.
 * 
 * Examples:
 * FREEBUSY;FBTYPE=BUSY-UNAVAILABLE:19970308T160000Z/PT8H30M
 * FREEBUSY;FBTYPE=FREE:19970308T160000Z/PT3H,19970308T200000Z/PT1H
 *  ,19970308T230000Z/19970309T000000Z
 * 
 * Note: The above example is converted and outputed as the following:
 * FREEBUSY;FBTYPE=FREE:19970308T160000Z/PT3H,19970308T200000Z/PT1H
 *  ,19970308T230000Z/PT1H
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VFreeBusy
 */
public class FreeBusyTime extends PropertyBase<List<Pair<ZonedDateTime, TemporalAmount>>, FreeBusyTime> implements PropFreeBusy<List<Pair<ZonedDateTime, TemporalAmount>>>
{
    public final static StringConverter<List<Pair<ZonedDateTime, TemporalAmount>>> CONVERTER = new StringConverter<List<Pair<ZonedDateTime, TemporalAmount>>>()
    {
        @Override
        public String toString(List<Pair<ZonedDateTime, TemporalAmount>> object)
        {
            return object.stream().map(p ->
            {
                StringBuilder builder = new StringBuilder(30);
                builder.append(DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER.format(p.getKey()));
                builder.append("/");
                builder.append(p.getValue().toString());
                return builder.toString();
            })
            .collect(Collectors.joining(","));
        }

        @Override
        public List<Pair<ZonedDateTime, TemporalAmount>> fromString(String string)
        {
            List<Pair<ZonedDateTime, TemporalAmount>> periodList = new ArrayList<>();
            Arrays.asList(string.split(",")).forEach(pair ->
            {
                String[] time = pair.split("/");
                ZonedDateTime startInclusive = ZonedDateTime.parse(time[0], DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER);
                final TemporalAmount duration;
                if (time[1].charAt(time[1].length()-1) == 'Z')
                {
                    ZonedDateTime endExclusive = ZonedDateTime.parse(time[1], DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER);                
                    duration = Duration.between(startInclusive, endExclusive);
                } else
                {
                    duration = Duration.parse(time[1]);
                }
                periodList.add(new Pair<ZonedDateTime, TemporalAmount>(startInclusive, duration));
            });
            return periodList;
        }
    };
    
    /**
     * FBTYPE: Incline Free/Busy Time Type
     * RFC 5545, 3.2.9, page 20
     * 
     * To specify the free or busy time type.
     * 
     * Values can be = "FBTYPE" "=" ("FREE" / "BUSY" / "BUSY-UNAVAILABLE" / "BUSY-TENTATIVE"
     */
    @Override
    public FreeBusyType getFreeBusyType() { return (freeBusyType == null) ? null : freeBusyType.get(); }
    @Override
    public ObjectProperty<FreeBusyType> freeBusyTypeProperty()
    {
        if (freeBusyType == null)
        {
            freeBusyType = new SimpleObjectProperty<>(this, ParameterType.INLINE_ENCODING.toString());
            orderer().registerSortOrderProperty(freeBusyType);
        }
        return freeBusyType;
    }
    private ObjectProperty<FreeBusyType> freeBusyType;
    @Override
    public void setFreeBusyType(FreeBusyType freeBusyType)
    {
        if (freeBusyType != null)
        {
            freeBusyTypeProperty().set(freeBusyType);
        }
    }
    public void setFreeBusyType(FreeBusyTypeEnum type) { setFreeBusyType(new FreeBusyType(type)); }
    public FreeBusyTime withFreeBusyType(FreeBusyType freeBusyType) { setFreeBusyType(freeBusyType); return this; }
    public FreeBusyTime withFreeBusyType(FreeBusyTypeEnum type) { setFreeBusyType(type); return this; }
    public FreeBusyTime withFreeBusyType(String freeBusyType) { setFreeBusyType(FreeBusyType.parse(freeBusyType)); return this; }

    /*
     * CONSTRUCTORS
     */
    
    public FreeBusyTime(FreeBusyTime source)
    {
        super(source);
    }

    public FreeBusyTime(List<Pair<ZonedDateTime, TemporalAmount>> values)
    {
        super();
        setConverter(CONVERTER);
        setValue(values);
    }

    public FreeBusyTime()
    {
        super();
        setConverter(CONVERTER);
    }
    
    public static FreeBusyTime parse(String propertyContent)
    {
        FreeBusyTime property = new FreeBusyTime();
        property.parseContent(propertyContent);
        return property;
    }
}
