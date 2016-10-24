package jfxtras.icalendarfx.property.rrule;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;

public class RecurrenceRuleStreamTest
{
    /*
    DTSTART;VALUE=DATE:20160611
    RRULE:FREQ=MONTHLY;BYMONTHDAY=7,8,9,10,11,12,13;BYDAY=SA
     */
    @Test
    public void canStreamRRuleSpecial1()
    {
        String s = "FREQ=MONTHLY;BYMONTHDAY=7,8,9,10,11,12,13;BYDAY=SA";
        RecurrenceRule2 rRule = RecurrenceRule2.parse(s);
        Temporal dateTimeStart = LocalDate.of(2016, 6, 11);
        List<LocalDate> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDate.of(2016, 6, 11)
              , LocalDate.of(2016, 7, 9)
              , LocalDate.of(2016, 8, 13)
              , LocalDate.of(2016, 9, 10)
              , LocalDate.of(2016, 10, 8)
                ));
        List<Temporal> madeRecurrences = rRule.streamRecurrences(dateTimeStart).limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);

    }
    
    /*
    DTSTART;VALUE=DATE:20160630
    RRULE:FREQ=MONTHLY;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=-1
    
    Last workday of the month
     */
    @Test
    public void canStreamRRuleSpecial2()
    {
        String s = "FREQ=MONTHLY;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=-1";
        RecurrenceRule2 rRule = RecurrenceRule2.parse(s);
        Temporal dateTimeStart = LocalDate.of(2016, 6, 30);
        List<LocalDate> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDate.of(2016, 6, 30)
              , LocalDate.of(2016, 7, 29)
              , LocalDate.of(2016, 8, 31)
              , LocalDate.of(2016, 9, 30)
              , LocalDate.of(2016, 10, 31)
                ));
        List<Temporal> madeRecurrences = rRule.streamRecurrences(dateTimeStart).limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    @Test // Demonstrates correct ByRule processing order even if elements are ordered wrong in content
    public void canOrderByRules()
    {
        String s = "FREQ=MONTHLY;BYDAY=SA;BYMONTHDAY=7,8,9,10,11,12,13";
        RecurrenceRule2 rRule = RecurrenceRule2.parse(s);
        RecurrenceRule2 expectedRRule = new RecurrenceRule2()
                .withFrequency(FrequencyType.MONTHLY)
                .withByRules(new ByDay(DayOfWeek.SATURDAY), new ByMonthDay(7,8,9,10,11,12,13));
        assertEquals(s, expectedRRule.toContent());
        assertEquals(s, rRule.toContent());
        assertEquals(expectedRRule, rRule);
        List<?> expectedByRuleClasses = Arrays.asList(ByMonthDay.class, ByDay.class);
        List<?> byRuleClasses = rRule.byRules().stream().map(r -> r.getClass()).collect(Collectors.toList());
        assertEquals(expectedByRuleClasses, byRuleClasses);        
    }
    
    /*
     * Examples from RFC 5545, page 123-132
     */
    
    /*
     * Daily for 10 occurrences:
     * 
     DTSTART;TZID=America/New_York:19970902T090000
     RRULE:FREQ=DAILY;COUNT=10
     */
    @Test
    public void canStreamRRule1()
    {
        String s = "FREQ=DAILY;COUNT=10";
        RecurrenceRule2 rRule = RecurrenceRule2.parse(s);
        assertEquals(rRule.toContent(), s);
        Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
        List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 3, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 4, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 5, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 6, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 7, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 8, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 9, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 10, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 11, 9, 0), ZoneId.of("America/New_York"))
              ));
        List<Temporal> madeRecurrences = rRule.streamRecurrences(dateTimeStart).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
     * Daily until December 24, 1997:
     * 
    DTSTART;TZID=America/New_York:19970902T090000
    RRULE:FREQ=DAILY;UNTIL=19971224T000000Z
    */
   @Test
   public void canStreamRRule2()
   {
       String s = "FREQ=DAILY;UNTIL=19971224T000000Z";
       RecurrenceRule2 rRule = RecurrenceRule2.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = Stream
               .iterate(dateTimeStart, a -> a.plus(1, ChronoUnit.DAYS))
               .limit(113)
               .collect(Collectors.toList());
       List<Temporal> madeRecurrences = rRule.streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every other day - forever:
    * 
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=DAILY;INTERVAL=2
   */
  @Test
  public void canStreamRRule3()
  {
      String s = "RRULE:FREQ=DAILY;INTERVAL=2";
      RecurrenceRule rRule = RecurrenceRule.parse(s);
      assertEquals(rRule.toContent(), s);
      Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
      List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
              ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
            , ZonedDateTime.of(LocalDateTime.of(1997, 9, 4, 9, 0), ZoneId.of("America/New_York"))
            , ZonedDateTime.of(LocalDateTime.of(1997, 9, 6, 9, 0), ZoneId.of("America/New_York"))
            , ZonedDateTime.of(LocalDateTime.of(1997, 9, 8, 9, 0), ZoneId.of("America/New_York"))
            , ZonedDateTime.of(LocalDateTime.of(1997, 9, 10, 9, 0), ZoneId.of("America/New_York"))
            ));
      List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(5).collect(Collectors.toList());
      assertEquals(expectedRecurrences, madeRecurrences);
  }
  
    /*
     * Every 10 days, 5 occurrences:
     * 
    DTSTART;TZID=America/New_York:19970902T090000
    RRULE:FREQ=DAILY;INTERVAL=10;COUNT=5
    */
   @Test
   public void canStreamRRule4()
   {
       String s = "RRULE:FREQ=DAILY;INTERVAL=10;COUNT=5";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 12, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 22, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 12, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(5).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every day in January, for 3 years:
    * 
   DTSTART;TZID=America/New_York:19980101T090000
   RRULE:FREQ=YEARLY;UNTIL=20000131T140000Z;BYMONTH=1;BYDAY=SU,MO,TU,WE,TH,FR,SA
   or
   RRULE:FREQ=DAILY;UNTIL=20000131T140000Z;BYMONTH=1
   */
   @Test
   public void canStreamRRule5()
   {
       String s = "RRULE:FREQ=YEARLY;UNTIL=20000131T140000Z;BYMONTH=1;BYDAY=SU,MO,TU,WE,TH,FR,SA";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals("RRULE:" + rRule.getValue().toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1998, 1, 1, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = Stream
               .iterate(dateTimeStart, a -> a.plus(1, ChronoUnit.DAYS))
               .limit(31)
               .collect(Collectors.toList());
       expectedRecurrences.addAll(Stream
               .iterate(dateTimeStart.plus(1, ChronoUnit.YEARS), a -> a.plus(1, ChronoUnit.DAYS))
               .limit(31)
               .collect(Collectors.toList()));
       expectedRecurrences.addAll(Stream
               .iterate(dateTimeStart.plus(2, ChronoUnit.YEARS), a -> a.plus(1, ChronoUnit.DAYS))
               .limit(31)
               .collect(Collectors.toList()));
       
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
       
       String s2 = "RRULE:FREQ=DAILY;UNTIL=20000131T140000Z;BYMONTH=1";
       RecurrenceRule rRule2 = RecurrenceRule.parse(s2);
       List<Temporal> madeRecurrences2 = rRule2.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences2);
   }
   
   /*
    * Weekly for 10 occurrences:
    * 
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=WEEKLY;COUNT=10
    */

   @Test
   public void canStreamRRule6()
   {
       String s = "RRULE:FREQ=WEEKLY;COUNT=10";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = Stream
               .iterate(dateTimeStart, a -> a.plus(1, ChronoUnit.WEEKS))
               .limit(10)
               .collect(Collectors.toList());
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
  
   /*
    * Weekly until December 24, 1997:
    * 
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=WEEKLY;UNTIL=19971224T000000Z
    */
   @Test
   public void canStreamRRule7()
   {
       String s = "RRULE:FREQ=WEEKLY;UNTIL=19971224T000000Z";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = Stream
               .iterate(dateTimeStart, a -> a.plus(1, ChronoUnit.WEEKS))
               .limit(17)
               .collect(Collectors.toList());
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every other week - forever:
    * 
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=WEEKLY;INTERVAL=2;WKST=SU
    */
   @Test
   public void canStreamRRule8()
   {
       String s = "RRULE:FREQ=WEEKLY;INTERVAL=2;WKST=SU";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = Stream
               .iterate(dateTimeStart, a -> a.plus(2, ChronoUnit.WEEKS))
               .limit(100)
               .collect(Collectors.toList());
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(100).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Weekly on Tuesday and Thursday for five weeks:
    * 
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=WEEKLY;UNTIL=19971007T000000Z;WKST=SU;BYDAY=TU,TH
   or
   RRULE:FREQ=WEEKLY;COUNT=10;WKST=SU;BYDAY=TU,TH
    */
   @Test
   public void canStreamRRule9()
   {
       String s = "RRULE:FREQ=WEEKLY;UNTIL=19971007T000000Z;WKST=SU;BYDAY=TU,TH";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 4, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 9, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 11, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 16, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 18, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 23, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 25, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 30, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 2, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
       
       String s2 = "RRULE:FREQ=WEEKLY;COUNT=10;WKST=SU;BYDAY=TU,TH";
       RecurrenceRule rRule2 = RecurrenceRule.parse(s2);
       List<Temporal> madeRecurrences2 = rRule2.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences2);
   }
   
   /*
    * Every other week on Monday, Wednesday, and Friday until December
    * 24, 1997, starting on Monday, September 1, 1997:
    * 
   DTSTART;TZID=America/New_York:19970901T090000
   RRULE:FREQ=WEEKLY;INTERVAL=2;UNTIL=19971224T000000Z;WKST=SU;BYDAY=MO,WE,FR
    */
   @Test
   public void canStreamRRule10()
   {
       String s = "RRULE:FREQ=WEEKLY;INTERVAL=2;UNTIL=19971224T000000Z;WKST=SU;BYDAY=MO,WE,FR";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 1, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = Stream
               .iterate(dateTimeStart, a -> a.plus(2, ChronoUnit.WEEKS))
               .flatMap(v -> 
               {
                   List<Temporal> t = new ArrayList<>();
                   t.add(v); // monday
                   t.add(v.plus(2, ChronoUnit.DAYS)); // wednesday
                   t.add(v.plus(4, ChronoUnit.DAYS)); // friday
                   return t.stream();
               })
               .limit(25)
               .collect(Collectors.toList());
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   @Test // tests the RecurrenceRule2 property value streaming date/times
   public void canStreamRRule10b()
   {
    RecurrenceRule2 rrule = RecurrenceRule2.parse("FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR");
    List<Temporal> madeRecurrences = rrule.streamRecurrences(LocalDate.of(2017, 1, 2))
            .limit(10)
            .collect(Collectors.toList());
    List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
            LocalDate.of(2017, 1, 2)
          , LocalDate.of(2017, 1, 4)
          , LocalDate.of(2017, 1, 6)
          , LocalDate.of(2017, 1, 16)
          , LocalDate.of(2017, 1, 18)
          , LocalDate.of(2017, 1, 20)
          , LocalDate.of(2017, 1, 30)
          , LocalDate.of(2017, 2, 1)
          , LocalDate.of(2017, 2, 3)
          , LocalDate.of(2017, 2, 13)
          ));
    assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every other week on Tuesday and Thursday, for 8 occurrences:
    * 
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=8;WKST=SU;BYDAY=TU,TH
    */
   @Test
   public void canStreamRRule11()
   {
       String s = "RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=8;WKST=SU;BYDAY=TU,TH";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 4, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 16, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 18, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 30, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 14, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 16, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Monthly on the first Friday for 10 occurrences:
    * 
     DTSTART;TZID=America/New_York:19970905T090000
     RRULE:FREQ=MONTHLY;COUNT=10;BYDAY=1FR
    */
   @Test
   public void canStreamRRule12()
   {
       String s = "RRULE:FREQ=MONTHLY;COUNT=10;BYDAY=1FR";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 5, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 5, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 3, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 7, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 5, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 2, 6, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 6, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 4, 3, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 5, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 6, 5, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Monthly on the first Friday until December 24, 1997:
    * 
   DTSTART;TZID=America/New_York:19970905T090000
   RRULE:FREQ=MONTHLY;UNTIL=19971224T000000Z;BYDAY=1FR
    */
   @Test
   public void canStreamRRule13()
   {
       String s = "RRULE:FREQ=MONTHLY;UNTIL=19971224T000000Z;BYDAY=1FR";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 5, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 5, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 3, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 7, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 5, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every other month on the first and last Sunday of the month for 10 occurrences:
    * 
   DTSTART;TZID=America/New_York:19970907T090000
   RRULE:FREQ=MONTHLY;INTERVAL=2;COUNT=10;BYDAY=1SU,-1SU
    */
   @Test
   public void canStreamRRule14()
   {
       String s = "RRULE:FREQ=MONTHLY;INTERVAL=2;COUNT=10;BYDAY=1SU,-1SU";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 7, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 7, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 28, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 30, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 4, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 25, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 29, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 5, 3, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 5, 31, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Monthly on the second-to-last Monday of the month for 6 months:
    * 
   DTSTART;TZID=America/New_York:19970922T090000
   RRULE:FREQ=MONTHLY;COUNT=6;BYDAY=-2MO
    */
   @Test
   public void canStreamRRule15()
   {
       String s = "RRULE:FREQ=MONTHLY;COUNT=6;BYDAY=-2MO";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 22, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 22, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 20, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 17, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 22, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 19, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 2, 16, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Monthly on the third-to-the-last day of the month, forever:
    * 
   DTSTART;TZID=America/New_York:19970928T090000
   RRULE:FREQ=MONTHLY;BYMONTHDAY=-3
    */
   @Test
   public void canStreamRRule16()
   {
       String s = "RRULE:FREQ=MONTHLY;BYMONTHDAY=-3";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 28, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 28, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 29, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 28, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 29, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 29, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 2, 26, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(6).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Monthly on the 2nd and 15th of the month for 10 occurrences:
    * 
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=MONTHLY;COUNT=10;BYMONTHDAY=2,15
    */
   @Test
   public void canStreamRRule17()
   {
       String s = "RRULE:FREQ=MONTHLY;BYMONTHDAY=2,15";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 15, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 15, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 15, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 15, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 15, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(10).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Monthly on the first and last day of the month for 10 occurrences:
    * 
   DTSTART;TZID=America/New_York:19970930T090000
   RRULE:FREQ=MONTHLY;COUNT=10;BYMONTHDAY=1,-1
    */
   @Test
   public void canStreamRRule18()
   {
       String s = "RRULE:FREQ=MONTHLY;COUNT=10;BYMONTHDAY=1,-1";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 30, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 31, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 30, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 31, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 31, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 2, 1, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(10).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every 18 months on the 10th thru 15th of the month for 10 occurrences:
    * 
   DTSTART;TZID=America/New_York:19970910T090000
   RRULE:FREQ=MONTHLY;INTERVAL=18;COUNT=10;BYMONTHDAY=10,11,12,13,14,15
    */
   @Test
   public void canStreamRRule19()
   {
       String s = "RRULE:FREQ=MONTHLY;INTERVAL=18;COUNT=10;BYMONTHDAY=10,11,12,13,14,15";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 10, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 11, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 12, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 13, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 14, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 15, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 3, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 3, 11, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 3, 12, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 3, 13, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(10).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every Tuesday, every other month:
    * 
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=MONTHLY;INTERVAL=2;BYDAY=TU
    */
   @Test
   public void canStreamRRule20()
   {
       String s = "RRULE:FREQ=MONTHLY;INTERVAL=2;BYDAY=TU";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 9, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 16, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 23, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 30, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 4, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 11, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 18, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 25, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 6, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 13, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 20, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 27, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 3, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 17, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 24, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 31, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(18).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
      
   /*
    * Yearly in June and July for 10 occurrences:
    * 
   DTSTART;TZID=America/New_York:19970610T090000
   RRULE:FREQ=YEARLY;COUNT=10;BYMONTH=6,7
    */
   @Test
   public void canStreamRRule21()
   {
       String s = "RRULE:FREQ=YEARLY;COUNT=10;BYMONTH=6,7";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 6, 10, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 6, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 7, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 6, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 7, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 6, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 7, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2000, 6, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2000, 7, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2001, 6, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2001, 7, 10, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(10).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every other year on January, February, and March for 10 occurrences:
    * 
   DTSTART;TZID=America/New_York:19970310T090000
   RRULE:FREQ=YEARLY;INTERVAL=2;COUNT=10;BYMONTH=1,2,3
    */
   @Test
   public void canStreamRRule22()
   {
       String s = "RRULE:FREQ=YEARLY;INTERVAL=2;COUNT=10;BYMONTH=1,2,3";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 3, 10, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 3, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 1, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 2, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 3, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2001, 1, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2001, 2, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2001, 3, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2003, 1, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2003, 2, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2003, 3, 10, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(10).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
      
   /*
    * Every third year on the 1st, 100th, and 200th day for 10 occurrences:
    *       
   DTSTART;TZID=America/New_York:19970101T090000
   RRULE:FREQ=YEARLY;INTERVAL=3;COUNT=10;BYYEARDAY=1,100,200
    */
   @Test
   public void canStreamRRule23()
   {
       String s = "RRULE:FREQ=YEARLY;INTERVAL=3;COUNT=10;BYYEARDAY=1,100,200";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 1, 1, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 1, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 4, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 7, 19, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2000, 1, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2000, 4, 9, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2000, 7, 18, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2003, 1, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2003, 4, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2003, 7, 19, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2006, 1, 1, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(10).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every 20th Monday of the year, forever:
    * 
   DTSTART;TZID=America/New_York:19970519T090000
   RRULE:FREQ=YEARLY;BYDAY=20MO
    */
   @Test
   public void canStreamRRule24()
   {
       String s = "RRULE:FREQ=YEARLY;BYDAY=20MO";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 5, 19, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 5, 19, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 5, 18, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 5, 17, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(3).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Monday of week number 20 (where the default start of the week is Monday), forever:
    * 
   DTSTART;TZID=America/New_York:19970512T090000
   RRULE:FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO
    */
   @Test
   public void canStreamRRule25()
   {
       String s = "RRULE:FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 5, 12, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 5, 12, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 5, 11, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 5, 17, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(3).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every Thursday in March, forever:
    * 
   DTSTART;TZID=America/New_York:19970313T090000
   RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=TH
    */
   @Test
   public void canStreamRRule26()
   {
       String s = "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=TH";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 3, 13, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 3, 13, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 3, 20, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 3, 27, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 5, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 12, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 19, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 26, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 3, 4, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 3, 11, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 3, 18, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 3, 25, 9, 0), ZoneId.of("America/New_York"))
               ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(11).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every Thursday, but only during June, July, and August, forever:
    * 
   DTSTART;TZID=America/New_York:19970605T090000
   RRULE:FREQ=YEARLY;BYDAY=TH;BYMONTH=6,7,8
    */
   @Test
   public void canStreamRRule27()
   {
       String s = "RRULE:FREQ=YEARLY;BYDAY=TH;BYMONTH=6,7,8";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 6, 5, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = Stream
               .iterate(dateTimeStart, a -> a.plus(1, ChronoUnit.WEEKS))
               .limit(13)
               .collect(Collectors.toList());
       Temporal dateTimeStart2 = ZonedDateTime.of(LocalDateTime.of(1998, 6, 4, 9, 0), ZoneId.of("America/New_York"));
       expectedRecurrences.addAll(Stream
               .iterate(dateTimeStart2, a -> a.plus(1, ChronoUnit.WEEKS))
               .limit(13)
               .collect(Collectors.toList()));
       Temporal dateTimeStart3 = ZonedDateTime.of(LocalDateTime.of(1999, 6, 3, 9, 0), ZoneId.of("America/New_York"));
       expectedRecurrences.addAll(Stream
               .iterate(dateTimeStart3, a -> a.plus(1, ChronoUnit.WEEKS))
               .limit(13)
               .collect(Collectors.toList()));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(39).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
      
   /*
    * Every Friday the 13th, forever:
    * 
   DTSTART;TZID=America/New_York:19970902T090000
   EXDATE;TZID=America/New_York:19970902T090000
   RRULE:FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13
   
   Note: EXDATE is left out of test. EXDATE handled as property in calendar component and can't be tested here.
    */
   @Test
   public void canStreamRRule28()
   {
       String s = "RRULE:FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1998, 2, 13, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1998, 2, 13, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 13, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 11, 13, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 8, 13, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2000, 10, 13, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(5).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * The first Saturday that follows the first Sunday of the month, forever:
    * 
   DTSTART;TZID=America/New_York:19970913T090000
   RRULE:FREQ=MONTHLY;BYDAY=SA;BYMONTHDAY=7,8,9,10,11,12,13
    */
   @Test
   public void canStreamRRule29()
   {
       String s = "RRULE:FREQ=MONTHLY;BYDAY=SA;BYMONTHDAY=7,8,9,10,11,12,13";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 13, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 13, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 11, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 8, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 13, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 2, 7, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 7, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 4, 11, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 5, 9, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 6, 13, 9, 0), ZoneId.of("America/New_York"))
               ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(10).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
      
   /*
    * Every 4 years, the first Tuesday after a Monday in November, forever (U.S. Presidential Election day):
    * 
   DTSTART;TZID=America/New_York:19961105T090000
   RRULE:FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYDAY=TU;BYMONTHDAY=2,3,4,5,6,7,8
    */
   @Test
   public void canStreamRRule30()
   {
       String s = "RRULE:FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYDAY=TU;BYMONTHDAY=2,3,4,5,6,7,8";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1996, 11, 5, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1996, 11, 5, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2000, 11, 7, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2004, 11, 2, 9, 0), ZoneId.of("America/New_York"))
               ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(3).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * The third instance into the month of one of Tuesday, Wednesday, or Thursday, for the next 3 months:
    * 
   DTSTART;TZID=America/New_York:19970904T090000
   RRULE:FREQ=MONTHLY;COUNT=3;BYDAY=TU,WE,TH;BYSETPOS=3
    */
   @Test
   public void canStreamRRule31()
   {
       String s = "RRULE:FREQ=MONTHLY;COUNT=3;BYDAY=TU,WE,TH;BYSETPOS=3";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 4, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 4, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 7, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 6, 9, 0), ZoneId.of("America/New_York"))
               ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * The second-to-last weekday of the month:
    * 
   DTSTART;TZID=America/New_York:19970929T090000
   RRULE:FREQ=MONTHLY;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=-2
    */
   @Test
   public void canStreamRRule32()
   {
       String s = "RRULE:FREQ=MONTHLY;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=-2";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 29, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 29, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 30, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 27, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 30, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 29, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 2, 26, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 30, 9, 0), ZoneId.of("America/New_York"))
               ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(7).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every 3 hours from 9:00 AM to 5:00 PM on a specific day:
    * NOTE: RFC 5545 erroneously has the UNTIL set at 19970902T170000Z
    * 
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=HOURLY;INTERVAL=3;UNTIL=19970902T210000Z
    */
   @Test
   public void canStreamRRule33()
   {
       String s = "RRULE:FREQ=HOURLY;INTERVAL=3;UNTIL=19970902T210000Z";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 12, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 15, 0), ZoneId.of("America/New_York"))
               ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(3).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every 15 minutes for 6 occurrences:
    * 
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=MINUTELY;INTERVAL=15;COUNT=6
    */
   @Test
   public void canStreamRRule34()
   {
       String s = "RRULE:FREQ=MINUTELY;INTERVAL=15;COUNT=6";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 15), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 30), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 45), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 10, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 10, 15), ZoneId.of("America/New_York"))
               ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(6).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every hour and a half for 4 occurrences:
    * 
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=MINUTELY;INTERVAL=90;COUNT=4
    */
   @Test
   public void canStreamRRule35()
   {
       String s = "RRULE:FREQ=MINUTELY;INTERVAL=90;COUNT=4";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 10, 30), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 12, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 13, 30), ZoneId.of("America/New_York"))
               ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(4).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
      
   /*
    * Every 20 minutes from 9:00 AM to 4:40 PM every day:
    * 
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=DAILY;BYHOUR=9,10,11,12,13,14,15,16;BYMINUTE=0,20,40
   or
   RRULE:FREQ=MINUTELY;INTERVAL=20;BYHOUR=9,10,11,12,13,14,15,16
    */
   @Test
   public void canStreamRRule36()
   {
       String s = "RRULE:FREQ=DAILY;BYHOUR=9,10,11,12,13,14,15,16;BYMINUTE=0,20,40";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = Stream
               .iterate(dateTimeStart, a -> a.plus(20, ChronoUnit.MINUTES))
               .limit(24)
               .collect(Collectors.toList());
       Temporal dateTimeStart2 = ZonedDateTime.of(LocalDateTime.of(1997, 9, 3, 9, 0), ZoneId.of("America/New_York"));
       expectedRecurrences.addAll(Stream
               .iterate(dateTimeStart2, a -> a.plus(20, ChronoUnit.MINUTES))
               .limit(24)
               .collect(Collectors.toList()));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(48).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * An example where the days generated makes a difference because of WKST:
    * 
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=DAILY;BYHOUR=9,10,11,12,13,14,15,16;BYMINUTE=0,20,40
   or
   RRULE:FREQ=MINUTELY;INTERVAL=20;BYHOUR=9,10,11,12,13,14,15,16
    */
   @Test
   public void canStreamRRule37()
   {
       String s = "RRULE:FREQ=DAILY;BYHOUR=9,10,11,12,13,14,15,16;BYMINUTE=0,20,40";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = Stream
               .iterate(dateTimeStart, a -> a.plus(20, ChronoUnit.MINUTES))
               .limit(24)
               .collect(Collectors.toList());
       Temporal dateTimeStart2 = ZonedDateTime.of(LocalDateTime.of(1997, 9, 3, 9, 0), ZoneId.of("America/New_York"));
       expectedRecurrences.addAll(Stream
               .iterate(dateTimeStart2, a -> a.plus(20, ChronoUnit.MINUTES))
               .limit(24)
               .collect(Collectors.toList()));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(48).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
       
       String s2 = "RRULE:FREQ=MINUTELY;INTERVAL=20;BYHOUR=9,10,11,12,13,14,15,16";
       RecurrenceRule rRule2 = RecurrenceRule.parse(s2);
       assertEquals(rRule2.toContent(), s2);
       List<Temporal> madeRecurrences2 = rRule2.getValue().streamRecurrences(dateTimeStart).limit(48).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences2);
   }
   
   /*
    * 
   DTSTART;TZID=America/New_York:19970805T090000
   RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=4;BYDAY=TU,SU;WKST=MO
    */
   @Test
   public void canStreamRRule38()
   {
       String s = "RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=4;BYDAY=TU,SU;WKST=MO";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 8, 5, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 8, 5, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 8, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 8, 19, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 8, 24, 9, 0), ZoneId.of("America/New_York"))
               ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(4).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * An example where the days generated makes a difference because of WKST:
    * 
   DTSTART;TZID=America/New_York:19970805T090000
   RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=4;BYDAY=TU,SU;WKST=SU
    */
   @Test
   public void canStreamRRule39()
   {
       String s = "RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=4;BYDAY=TU,SU;WKST=SU";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 8, 5, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 8, 5, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 8, 17, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 8, 19, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 8, 31, 9, 0), ZoneId.of("America/New_York"))
               ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(4).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * An example where an invalid date (i.e., February 30) is ignored.
    * 
   DTSTART;TZID=America/New_York:20070115T090000
   RRULE:FREQ=MONTHLY;BYMONTHDAY=15,30;COUNT=5
    */
   @Test
   public void canStreamRRule40()
   {
       String s = "RRULE:FREQ=MONTHLY;BYMONTHDAY=15,30;COUNT=5";
       RecurrenceRule rRule = RecurrenceRule.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(2007, 1, 15, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(2007, 1, 15, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2007, 1, 30, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2007, 2, 15, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2007, 3, 15, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2007, 3, 30, 9, 0), ZoneId.of("America/New_York"))
               ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(5).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
}
