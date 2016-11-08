package jfxtras.icalendarfx.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.icalendarfx.parameters.Range;

public interface PropRecurrenceID<T> extends Property<T>
{
    Range getRange();
    ObjectProperty<Range> rangeProperty();
    void setRange(Range range);
}
