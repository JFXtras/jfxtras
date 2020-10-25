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
 * DISCLAIMED. IN NO EVENT SHALL JFXRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.properties.component.descriptive;

import java.text.DecimalFormat;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.VPropertyBase;
import jfxtras.icalendarfx.properties.component.descriptive.GeographicPosition;

/**
 * GEO
 * Geographic Position
 * RFC 5545 iCalendar 3.8.1.6. page 86
 * 
 * This property specifies information related to the global
 * position for the activity specified by a calendar component.
 * 
 * This property value specifies latitude and longitude,
 * in that order (i.e., "LAT LON" ordering).
 * 
 * Example:
 * GEO:37.386013;-122.082932
 *  
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 */
public class GeographicPosition extends VPropertyBase<String, GeographicPosition>
{
    public Double getLatitude() { return latitude; }
    private Double latitude;
    public void setLatitude(Double latitude)
    {
    	this.latitude = latitude;
    	buildNewValue();
	}
    public GeographicPosition withLatitude(Double latitude) { setLatitude(latitude); return this; }
    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.######");

    public Double getLongitude() { return longitude; }
    private Double longitude;
    public void setLongitude(Double longitude)
    {
    	this.longitude = longitude;
    	buildNewValue();
	}
    public GeographicPosition withLongitude(Double longitude) { setLongitude(longitude); return this; }

    /*
     * CONSTRUCTORS
     */
    public GeographicPosition(GeographicPosition source)
    {
        super(source);
        updateParts(getValue());
    }
    
    public GeographicPosition(double latitude, double longitude)
    {
        super();
        setLatitude(latitude);
        setLongitude(longitude);
    }
    
    public GeographicPosition()
    {
        super();
    }
    
    /** Applies string converter only to description and exception parts of the RequestStatus property
     * This leaves the semicolon delimiters unescaped
     */
    @Override
    protected String valueContent()
    {
        StringBuilder builder = new StringBuilder(20);
        builder.append(DECIMAL_FORMAT.format(getLatitude()) + ";");
        builder.append(DECIMAL_FORMAT.format(getLongitude()));
        return builder.toString();
    }
    
    public static GeographicPosition parse(String content)
    {
    	return GeographicPosition.parse(new GeographicPosition(), content);
    }
    
    private void updateParts(String newValue)
    {
        String[] values = newValue.split(";");
        if (values.length == 2)
        {
            setLatitude(Double.parseDouble(values[0]));
            setLongitude(Double.parseDouble(values[1]));
        } else
        {
            throw new IllegalArgumentException("Can't parse geographic position value:" + newValue);
        }
    }

    @Override
	public void setValue(String value)
    {
		super.setValue(value);
		updateParts(value);
	}
    
	private void buildNewValue()
    {
        if ((getLatitude() != null) && (getLongitude() != null))
        {
            StringBuilder builder = new StringBuilder(20);
            builder.append(DECIMAL_FORMAT.format(getLatitude()) + ";");
            builder.append(DECIMAL_FORMAT.format(getLongitude()));
            super.setValue(builder.toString());
        }
    }
}
