package jfxtras.icalendarfx.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.icalendarfx.parameters.Relationship;

public interface PropRelationship<T> extends Property<T>
{
    Relationship getRelationship();
    ObjectProperty<Relationship> relationshipProperty();
    void setRelationship(Relationship relationship);
}
