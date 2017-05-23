package jfxtras.icalendarfx.properties;

import jfxtras.icalendarfx.parameters.AlarmTriggerRelationship;
import jfxtras.icalendarfx.properties.VProperty;

public interface PropAlarmTrigger<T> extends VProperty<T>
{
    AlarmTriggerRelationship getAlarmTrigger();
    void setAlarmTrigger(AlarmTriggerRelationship AlarmTrigger);
}
