/**
 * TemporalUtilities.java
 *
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
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

package jfxtras.scene.control.agenda;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains the methods to convert to and from LocalDateTime to other Temporal classes
 * 
 * @author David Bal
 * @see Agenda.AppointmentImplTemporal
 *
 */

public final class TemporalUtilities
{
    private TemporalUtilities() { }

    /** applies the parameter adjuster to the initialTemporal
     * adjuster must be either LocalDate or LocalDateTime
     * 
     * For example, If initialTemporal is a ZonedDateTime object representing 2007-12-03T10:15:30+01:00 Europe/Paris
     * and adjuster represents 2007-12-05T10:12:30 then the returned Temporal will be
     * 2007-12-05T10:12:30+01:00 Europe/Paris
     *
     * Another example, if initialTemporal is a LocalDate object representing 2007-12-03
     * and adjuster represents 2007-12-05T10:12:30 then the returned Temporal will be
     * 2007-12-05
     * 
     */
    public static Temporal combine(Temporal initialTemporal, TemporalAdjuster adjuster)
    {
        return TemporalType.from(initialTemporal.getClass()).combineByType(initialTemporal, adjuster);
    }

    /** makes LocalDateTime from temporal followed rules in TemporalType */
    public static LocalDateTime toLocalDateTime(Temporal temporal)
    {
        return TemporalType.from(temporal.getClass()).toLocalDateTimeByType(temporal);
    }
    
    public enum TemporalType
    {
        HIJRAH_DATE (HijrahDate.class) {
            @Override
            protected LocalDateTime toLocalDateTimeByType(Temporal t) { return LocalDate.from(t).atStartOfDay(); }

            @Override
            protected Temporal combineByType(Temporal initialTemporal, TemporalAdjuster adjuster) { return combineDateBased(initialTemporal, adjuster); }
        }
      , INSTANT (Instant.class) {
          @Override
          protected LocalDateTime toLocalDateTimeByType(Temporal t) { return LocalDateTime.ofInstant(Instant.from(t), ZoneId.systemDefault()); }

          @Override
          protected Temporal combineByType(Temporal initialTemporal, TemporalAdjuster adjuster) { return initialTemporal.with(adjuster); }
        }
      , JAPANESE_DATE (JapaneseDate.class) {
          @Override
          protected LocalDateTime toLocalDateTimeByType(Temporal t) { return LocalDate.from(t).atStartOfDay(); }

          @Override
          protected Temporal combineByType(Temporal initialTemporal, TemporalAdjuster adjuster) { return combineDateBased(initialTemporal, adjuster); }
        }
      , LOCAL_DATE (LocalDate.class) {
          @Override
          protected LocalDateTime toLocalDateTimeByType(Temporal t) { return LocalDate.from(t).atStartOfDay(); }

          @Override
          protected Temporal combineByType(Temporal initialTemporal, TemporalAdjuster adjuster) { return combineDateBased(initialTemporal, adjuster); }
        }
      , LOCAL_DATE_TIME (LocalDateTime.class) {
          @Override
          protected LocalDateTime toLocalDateTimeByType(Temporal t) { return LocalDateTime.from(t); }

          @Override
          protected Temporal combineByType(Temporal initialTemporal, TemporalAdjuster adjuster) { return initialTemporal.with(adjuster); }
        }
      , MINGUO_DATE (MinguoDate.class) {
          @Override
          protected LocalDateTime toLocalDateTimeByType(Temporal t) { return LocalDate.from(t).atStartOfDay(); }

          @Override
          protected Temporal combineByType(Temporal initialTemporal, TemporalAdjuster adjuster) { return combineDateBased(initialTemporal, adjuster); }
        }
      , OFFSET_DATE_TIME (OffsetDateTime.class) {
          @Override
          protected LocalDateTime toLocalDateTimeByType(Temporal t)
          {
              ZoneOffset o = ZonedDateTime.now().getOffset();
              return OffsetDateTime.from(t).withOffsetSameInstant(o).toLocalDateTime();
          }

          @Override
          protected Temporal combineByType(Temporal initialTemporal, TemporalAdjuster adjuster) { return initialTemporal.with(adjuster); }
        }
      , THAI_BUDDHIST_DATE (ThaiBuddhistDate.class) {
          @Override
          protected LocalDateTime toLocalDateTimeByType(Temporal t) { return LocalDate.from(t).atStartOfDay(); }

          @Override
          protected Temporal combineByType(Temporal initialTemporal, TemporalAdjuster adjuster) { return combineDateBased(initialTemporal, adjuster); }
        }
      , ZONED_DATE_TIME (ZonedDateTime.class) {
          @Override
          protected LocalDateTime toLocalDateTimeByType(Temporal t) { return ZonedDateTime.from(t).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime(); }

          @Override
          protected Temporal combineByType(Temporal initialTemporal, TemporalAdjuster adjuster) { return initialTemporal.with(adjuster); }
        };
            
        // Map to match up Temporal class to TemporalType enum
        private static Map<Class<? extends Temporal>, TemporalType> temporalClassToTemporalTypeMap = makeMap();
        private static Map<Class<? extends Temporal>, TemporalType> makeMap()
        {
            Map<Class<? extends Temporal>, TemporalType> map = new HashMap<>();
            TemporalType[] values = TemporalType.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].clazz, values[i]);
            }
            return map;
        }
        public static TemporalType from(Class<? extends Temporal> clazz)
        {
            return temporalClassToTemporalTypeMap.get(clazz);
        }
    
        /* combine for all date-based Temporal classes, such as LocalDate, JapaneseDate, etc ) */
        private static Temporal combineDateBased(Temporal initialTemporal, TemporalAdjuster adjuster)
        {
            if (adjuster instanceof LocalDate)
            {
                return initialTemporal.with(adjuster);
            } else if (adjuster instanceof LocalDateTime)
            {
                return (LocalDateTime) adjuster; // adding time to initialTemporal
            } else
            {
                throw new DateTimeException("Unsupported TemporalAdjuster:" + adjuster);
            }
        }
            
        private Class<? extends Temporal> clazz;
        /** Class backing the TemporalType */
        public Class<? extends Temporal> getTemporalClass() { return clazz; }
        
        TemporalType(Class<? extends Temporal> clazz) { this.clazz = clazz; }
    
        /** makes LocalDateTime from temporal that matches the TemporalType */
        protected abstract LocalDateTime toLocalDateTimeByType(Temporal t);
    
        /** applies the parameter adjuster to the initialTemporal
         * adjuster must be either LocalDate or LocalDateTime
         * If adjuster contains time, but initialTemporal doesn't, then the returned Temporal
         * will be changed to contain time (as LocalDateTime).
         * <br>
         * For example, If initialTemporal is a ZonedDateTime object representing 2007-12-03T10:15:30+01:00 Europe/Paris
         * and adjuster represents 2007-12-05T10:12:30 then the returned Temporal will be
         * 2007-12-05T10:12:30+01:00 Europe/Paris
         * <br>
         * Another example, if initialTemporal is a LocalDate object representing 2007-12-03
         * and adjuster represents 2007-12-05T10:12:30 then the returned Temporal will be
         * 2007-12-05T10:12:30
         */
        protected abstract Temporal combineByType(Temporal initialTemporal, TemporalAdjuster adjuster);
    }
    
}
