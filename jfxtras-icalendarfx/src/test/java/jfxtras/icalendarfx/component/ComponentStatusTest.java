package jfxtras.icalendarfx.component;

import org.junit.Test;

import jfxtras.icalendarfx.components.VEvent;

public class ComponentStatusTest
{
    @Test
    public void canCatchDuplicateProperty()
    {
        String contentLines = "BEGIN:VEVENT" + System.lineSeparator()
        + "SUMMARY:test summary1" + System.lineSeparator()
        + "SUMMARY:test summary2" + System.lineSeparator()
        + "END:VEVENT";
        try
        {
            VEvent.parse(contentLines);
        } catch (Exception e)
        {
            System.out.println("oops:" + e.getMessage());
        }
    }
}
