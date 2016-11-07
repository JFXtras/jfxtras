package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Period;

import org.junit.Test;

import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.properties.component.alarm.Action;
import jfxtras.icalendarfx.properties.component.alarm.RepeatCount;
import jfxtras.icalendarfx.properties.component.alarm.Action.ActionType;
import jfxtras.icalendarfx.properties.component.relationship.Attendee;

public class VAlarmTest
{
    @Test
    public void canBuildVAlarm()
    {        
        VAlarm builtComponent = new VAlarm()
                .withAction(new Action(ActionType.DISPLAY))
                .withAttendees(Attendee.parse("mailto:jsmith@example.com"))
                .withDuration(Period.ofDays(-2))
                .withTrigger(Duration.ofMinutes(-15))
                .withRepeatCount(new RepeatCount(2));
        String componentName = builtComponent.name();
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "ACTION:DISPLAY" + System.lineSeparator() +
                "ATTENDEE:mailto:jsmith@example.com" + System.lineSeparator() +
                "DURATION:-P2D" + System.lineSeparator() +
                "TRIGGER:-PT15M" + System.lineSeparator() +
                "REPEAT:2" + System.lineSeparator() +
                "END:" + componentName;
                
        VAlarm madeComponent = VAlarm.parse(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContent());
        assertTrue(builtComponent.isValid());
    }
    
    @Test
    public void canTestVAlarmIsValid()
    {
        VAlarm builtComponent = new VAlarm();
        assertFalse(builtComponent.isValid());
        builtComponent.setAction(Action.parse("DISPLAY"));
        builtComponent.setTrigger(Duration.ofMinutes(-15));
        assertTrue(builtComponent.isValid());
        builtComponent.setDuration(Period.ofDays(-2));
        assertFalse(builtComponent.isValid());
        builtComponent.setRepeatCount(new RepeatCount(2));
        assertTrue(builtComponent.isValid());
    }
}
