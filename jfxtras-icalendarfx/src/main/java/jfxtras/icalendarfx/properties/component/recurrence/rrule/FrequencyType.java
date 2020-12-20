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
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.properties.component.recurrence.rrule;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;

public enum FrequencyType
{
    YEARLY (ChronoUnit.YEARS),
    MONTHLY (ChronoUnit.MONTHS),
    WEEKLY (ChronoUnit.WEEKS),
    DAILY (ChronoUnit.DAYS),
    HOURLY (ChronoUnit.HOURS),
    MINUTELY (ChronoUnit.MINUTES),
    SECONDLY (ChronoUnit.SECONDS);
    
    ChronoUnit chronoUnit;
    FrequencyType(ChronoUnit chronoUnit)
    {
        this.chronoUnit = chronoUnit;
    }
    public ChronoUnit getChronoUnit() { return chronoUnit; }

    // Map to match up string name to enum
    private static Map<String, FrequencyType> propertyFromNameMap = makePropertiesFromNameMap();
    private static Map<String, FrequencyType> makePropertiesFromNameMap()
    {
        Map<String, FrequencyType> map = new HashMap<>();
        FrequencyType[] values = FrequencyType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    /** get enum from name */
    public static FrequencyType propertyFromName(String propertyName)
    {
        return propertyFromNameMap.get(propertyName.toUpperCase());
    }
}
