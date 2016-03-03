package jfxtras.scene.control.agenda;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains the methods to convert to and from LocalDateTime to other Temporal classes
 * 
 * @author David Bal
 *
 */
public enum TemporalType
{
        HIJRAH_DATE (HijrahDate.class) {
            @Override
            public LocalDateTime toLocalDateTime(Temporal t) { return LocalDate.from(t).atStartOfDay(); }

            @Override
            public Temporal combine(Temporal initialTemporal, LocalDateTime adjuster) { return initialTemporal.with(LocalDate.from(adjuster)); }
        }
      , INSTANT (Instant.class) {
          @Override
          public LocalDateTime toLocalDateTime(Temporal t) { return LocalDateTime.ofInstant(Instant.from(t), ZoneId.systemDefault()); }

          @Override
          public Temporal combine(Temporal initialTemporal, LocalDateTime adjuster) { return initialTemporal.with(adjuster); }
    }
      , JAPANESE_DATE (JapaneseDate.class) {
          @Override
          public LocalDateTime toLocalDateTime(Temporal t) { return LocalDate.from(t).atStartOfDay(); }

          @Override
          public Temporal combine(Temporal initialTemporal, LocalDateTime adjuster) { return initialTemporal.with(LocalDate.from(adjuster)); }
    }
      , LOCAL_DATE (LocalDate.class) {
          @Override
          public LocalDateTime toLocalDateTime(Temporal t) { return LocalDate.from(t).atStartOfDay(); }

          @Override
          public Temporal combine(Temporal initialTemporal, LocalDateTime adjuster) { return initialTemporal.with(LocalDate.from(adjuster)); }
    }
      , LOCAL_DATE_TIME (LocalDateTime.class) {
          @Override
          public LocalDateTime toLocalDateTime(Temporal t) { return LocalDateTime.from(t); }

          @Override
          public Temporal combine(Temporal initialTemporal, LocalDateTime adjuster) { return adjuster; }
    }
      , MINGUO_DATE (MinguoDate.class) {
          @Override
          public LocalDateTime toLocalDateTime(Temporal t) { return LocalDate.from(t).atStartOfDay(); }

          @Override
          public Temporal combine(Temporal initialTemporal, LocalDateTime adjuster) { return initialTemporal.with(LocalDate.from(adjuster)); }
    }
      , OFFSET_DATE_TIME (OffsetDateTime.class) {
          @Override
          public LocalDateTime toLocalDateTime(Temporal t)
          {
              ZoneOffset o = ZonedDateTime.now().getOffset();
              return OffsetDateTime.from(t).withOffsetSameInstant(o).toLocalDateTime();
          }

          @Override
          public Temporal combine(Temporal initialTemporal, LocalDateTime adjuster) { return initialTemporal.with(adjuster); }
    }
      , THAI_BUDDHIST_DATE (ThaiBuddhistDate.class) {
          @Override
          public LocalDateTime toLocalDateTime(Temporal t) { return LocalDate.from(t).atStartOfDay(); }

          @Override
          public Temporal combine(Temporal initialTemporal, LocalDateTime adjuster) { return initialTemporal.with(LocalDate.from(adjuster)); }
    }
      , ZONED_DATE_TIME (ZonedDateTime.class) {
          @Override
          public LocalDateTime toLocalDateTime(Temporal t) { return ZonedDateTime.from(t).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime(); }

          @Override
          public Temporal combine(Temporal initialTemporal, LocalDateTime adjuster) { return initialTemporal.with(adjuster); }
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
    
    private Class<? extends Temporal> clazz;
    
    TemporalType(Class<? extends Temporal> clazz) { this.clazz = clazz; }

    /** makes LocalDateTime from temporal that matches the TemporalType */
    public abstract LocalDateTime toLocalDateTime(Temporal t);

    /** applies the parameter adjuster to the initialTemporal 
     * 
     * For example, If initialTemporal is a ZonedDateTime object representing 2007-12-03T10:15:30+01:00 Europe/Paris
     * and adjuster represents 2007-12-05T10:12:30 then the returned Temporal will be
     * 2007-12-05T10:12:30+01:00 Europe/Paris
     * 
     * Another example, if initialTemporal is a LocalDate object representating 2007-12-03
     * and adjuster represents 2007-12-05T10:12:30 then the returned Temporal will be
     * 2007-12-05
     */
    public abstract Temporal combine(Temporal initialTemporal, LocalDateTime adjuster);
}
