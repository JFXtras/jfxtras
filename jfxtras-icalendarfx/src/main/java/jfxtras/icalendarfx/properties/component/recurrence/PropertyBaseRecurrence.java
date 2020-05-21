/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.properties.component.recurrence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.properties.PropBaseDateTime;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.icalendarfx.properties.component.recurrence.PropertyBaseRecurrence;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.icalendarfx.utilities.StringConverter;
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
public abstract class PropertyBaseRecurrence<U> extends PropBaseDateTime<Set<Temporal>, U>
{
    private final StringConverter<Set<Temporal>> CONVERTER = new StringConverter<Set<Temporal>>()
    {
        @Override
        public String toString(Set<Temporal> object)
        {
            return object.stream()
                    .sorted(DateTimeUtilities.TEMPORAL_COMPARATOR)
                    .map(t -> DateTimeUtilities.temporalToString(t))
                    .collect(Collectors.joining(","));
        }

        @Override
        public Set<Temporal> fromString(String string)
        {
            Set<Temporal> set = Arrays.stream(string.split(","))
                    .map(s -> DateTimeUtilities.temporalFromString(s))
                    .collect(Collectors.toSet());
            TreeSet<Temporal> treeSet = new TreeSet<Temporal>(DateTimeUtilities.TEMPORAL_COMPARATOR);
            treeSet.addAll(set);
            return set;
        }
    };
    
    /*
     * CONSTRUCTORS
     */
    public PropertyBaseRecurrence(Set<Temporal> value)
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
        setValue(tree);
        if (! isValid())
        {
            throw new IllegalArgumentException("Error in parsing " + temporals);
        }
    }
    
    public PropertyBaseRecurrence()
    {
        super(new TreeSet<>(DateTimeUtilities.TEMPORAL_COMPARATOR));
        setConverter(CONVERTER);
    }

    public PropertyBaseRecurrence( PropertyBaseRecurrence<U> source)
    {
        super(source);
    }

    @Override
    public void setValue(Set<Temporal> value)
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
                        ". Accepted types are: " + allowedValueTypes);                
            }
        }
        super.setValue(value);
    }
        
    @Override
    public List<String> errors()
    {
    	List<String> errors = super.errors();
    	Set<Temporal> recurrenceDates = getValue();
    	
    	// error check - all Temporal types must be same
    	if ((recurrenceDates != null) && (! recurrenceDates.isEmpty()))
		{
        	Temporal sampleTemporal = recurrenceDates.stream()
            		.findAny()
            		.get();
    		DateTimeType sampleType = DateTimeUtilities.DateTimeType.of(sampleTemporal);
        	Optional<DateTimeType> notMatchDateTimeType = recurrenceDates
        		.stream()
        		.map(v -> DateTimeUtilities.DateTimeType.of(v))
        		.filter(v -> ! v.equals(sampleType))
        		.findAny();
        	if (notMatchDateTimeType.isPresent())
        	{
        		errors.add("Recurrences DateTimeType \"" + notMatchDateTimeType.get() +
                        "\" doesn't match previous recurrences DateTimeType \"" + sampleType + "\"");
        	}
            
            // ensure all ZoneId values are the same
            if (sampleTemporal instanceof ZonedDateTime)
            {
                ZoneId zone = ((ZonedDateTime) sampleTemporal).getZone();
                Optional<ZoneId> notMatchZone = recurrenceDates
                        .stream()
                        .map(t -> ((ZonedDateTime) t).getZone())
                		.filter(z -> ! z.equals(zone))
                		.findAny();
                if (notMatchZone.isPresent())
                {
                	errors.add("ZoneId \"" + notMatchZone.get() + "\" doesn't match previous ZoneId \"" + zone + "\"");
            	}
                
            }
        }
        return errors;
    }
        
    @Override
    protected Set<Temporal> copyValue(Set<Temporal> source)
    {
        Set<Temporal> newCollection = new TreeSet<>(DateTimeUtilities.TEMPORAL_COMPARATOR);
        newCollection.addAll(source);
        return newCollection;
    }
}
