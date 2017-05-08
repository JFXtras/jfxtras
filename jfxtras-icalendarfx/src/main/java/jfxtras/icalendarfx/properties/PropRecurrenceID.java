package jfxtras.icalendarfx.properties;

import jfxtras.icalendarfx.parameters.Range;
import jfxtras.icalendarfx.properties.VProperty;

public interface PropRecurrenceID<T> extends VProperty<T>
{
    Range getRange();
    void setRange(Range range);
}
