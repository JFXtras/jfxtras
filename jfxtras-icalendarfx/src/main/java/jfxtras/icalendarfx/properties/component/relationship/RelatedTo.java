package jfxtras.icalendarfx.properties.component.relationship;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.parameters.ParameterType;
import jfxtras.icalendarfx.parameters.Relationship;
import jfxtras.icalendarfx.parameters.Relationship.RelationshipType;
import jfxtras.icalendarfx.properties.PropRelationship;
import jfxtras.icalendarfx.properties.PropertyBase;

/**
 * RELATED-TO
 * RFC 5545, 3.8.4.5, page 115
 * 
 * This property is used to represent a relationship or
 * reference between one calendar component and another.
 * 
 * Example:
 * RECURRENCE-ID;VALUE=DATE:19960401
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public class RelatedTo extends PropertyBase<String, RelatedTo> implements PropRelationship<String>
{
    /**
     * RELTYPE
     * Relationship Type
     * RFC 5545, 3.2.15, page 25
     * 
     * To specify the type of hierarchical relationship associated
     *  with the calendar component specified by the property.
     * 
     * Example:
     * RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@
     *  example.com
     */
    @Override
    public Relationship getRelationship() { return (relationship == null) ? null : relationship.get(); }
    @Override
    public ObjectProperty<Relationship> relationshipProperty()
    {
        if (relationship == null)
        {
            relationship = new SimpleObjectProperty<>(this, ParameterType.RELATIONSHIP_TYPE.toString());
            orderer().registerSortOrderProperty(relationship);
        }
        return relationship;
    }
    private ObjectProperty<Relationship> relationship;
    @Override
    public void setRelationship(Relationship relationship) { relationshipProperty().set(relationship); }
    public void setRelationship(String value) { setRelationship(Relationship.parse(value)); }
    public RelatedTo withRelationship(Relationship altrep) { setRelationship(altrep); return this; }
    public RelatedTo withRelationship(RelationshipType value) { setRelationship(new Relationship(value)); return this; }
    public RelatedTo withRelationship(String content) { setRelationship(content); return this; }
    
//    public RelatedTo(CharSequence contentLine)
//    {
//        super(contentLine);
//    }

    public RelatedTo(RelatedTo source)
    {
        super(source);
    }
    
    public RelatedTo()
    {
        super();
    }
    
    public static RelatedTo parse(String propertyContent)
    {
        RelatedTo property = new RelatedTo();
        property.parseContent(propertyContent);
        return property;
    }
}
