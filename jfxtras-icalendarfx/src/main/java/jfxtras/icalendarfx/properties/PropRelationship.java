package jfxtras.icalendarfx.properties;

import jfxtras.icalendarfx.parameters.Relationship;
import jfxtras.icalendarfx.properties.VProperty;

public interface PropRelationship<T> extends VProperty<T>
{
    Relationship getRelationship();
    void setRelationship(Relationship relationship);
}
