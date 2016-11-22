package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import jfxtras.icalendarfx.ICalendarTestAbstract;
import jfxtras.icalendarfx.components.VEvent;

public class EqualsTest extends ICalendarTestAbstract
{
    @Test
    public void equalsTest1()
    {
        VEvent component1 = getWeekly3();
        VEvent component2 = getWeekly3();
        assertEquals(component1, component2);
    }
    
    @Test
    public void notEqualsTest1()
    {
        VEvent component1 = getWeekly3();
        VEvent component2 = getWeekly1();
        assertFalse(component1.equals(component2));
    }

}
