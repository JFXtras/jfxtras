package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.alarm.Action;
import jfxtras.icalendarfx.properties.component.alarm.Action.ActionType;

public class ActionTest
{
    @Test
    public void canParseAction()
    {
        Action madeProperty = Action.parse("ACTION:AUDIO");
        String expectedContent = "ACTION:AUDIO";
        assertEquals(expectedContent, madeProperty.toContent());
        assertEquals(ActionType.AUDIO, madeProperty.getValue());
    }
    
    @Test
    public void canParseAction2()
    {
        Action madeProperty = new Action(ActionType.DISPLAY);
        String expectedContent = "ACTION:DISPLAY";
        assertEquals(expectedContent, madeProperty.toContent());
        assertEquals(ActionType.DISPLAY, madeProperty.getValue());
    }
    
    @Test
    public void canParseAction3()
    {
        Action madeProperty = Action.parse("DANCE");
        String expectedContent = "ACTION:DANCE";
        assertEquals(expectedContent, madeProperty.toContent());
        assertEquals(ActionType.UNKNOWN, madeProperty.getValue());
    }
    
    @Test
    public void canParseAction4()
    {
        Action madeProperty = Action.parse("EMAIL");
        String expectedContent = "ACTION:EMAIL";
        assertEquals(expectedContent, madeProperty.toContent());
        assertEquals(ActionType.EMAIL, madeProperty.getValue());
        Action copiedProperty = new Action(madeProperty);
        assertEquals(madeProperty, copiedProperty);
        assertFalse(copiedProperty == madeProperty);
    }
}
