package jfxtras.icalendarfx.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import jfxtras.icalendarfx.ICalendarTestAbstract;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.icalendarfx.properties.calendar.Version;

public class CopyCalendarTest extends ICalendarTestAbstract
{
    @Test
    public void canCopyCalendar()
    {
        VCalendar c = new VCalendar()
                .withProductIdentifier(new ProductIdentifier())
                .withVersion(new Version())
                .withCalendarScale(new CalendarScale())
                .withVTodos(new VTodo()
                        .withDateTimeCompleted("COMPLETED:19960401T150000Z")
                        .withDateTimeDue("TZID=America/Los_Angeles:19960401T050000")
                        .withPercentComplete(35))
                .withVTimeZones(getTimeZone1())
                .withVEvents(getYearly1())
                .withVEvents(getMonthly6());
        VCalendar c2 = new VCalendar(c);
        assertEquals(c, c2);
        assertFalse(c == c2);
    }
    
    @Test
    public void canCopyCalendar2()
    {
        VCalendar c = new VCalendar();
        VEvent yearly1 = getYearly1();
		c.addChild(yearly1);
		VEvent yearly2 = new VEvent(yearly1);
		assertEquals(c, yearly2.getParent());
    }
}
