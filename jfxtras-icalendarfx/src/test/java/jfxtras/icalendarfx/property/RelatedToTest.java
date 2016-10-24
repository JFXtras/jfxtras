package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.Relationship.RelationshipType;
import jfxtras.icalendarfx.properties.component.relationship.RelatedTo;

public class RelatedToTest
{
    @Test
    public void canParseRelatedTo()
    {
        String expectedContent = "RELATED-TO:jsmith.part7.19960817T083000.xyzMail@example.com";
        RelatedTo madeProperty = RelatedTo.parse(expectedContent);
        assertEquals(expectedContent, madeProperty.toContent());
        RelatedTo expectedProperty = RelatedTo.parse("jsmith.part7.19960817T083000.xyzMail@example.com");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canParseRelatedTo2()
    {
        String expectedContent = "RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@example.com";
        RelatedTo madeProperty = RelatedTo.parse(expectedContent);
        assertEquals(expectedContent, madeProperty.toContent());
        RelatedTo expectedProperty = RelatedTo.parse("19960401-080045-4000F192713@example.com")
                .withRelationship(RelationshipType.SIBLING);
        assertEquals(expectedProperty, madeProperty);
        assertEquals(RelationshipType.SIBLING, madeProperty.getRelationship().getValue());
    }
    
    @Test
    public void canParseRelatedTo3()
    {
        String expectedContent = "RELATED-TO;RELTYPE=CUSTOM RELATIONSHIP:fc3577e0-8155-4fa2-a085-a15bdc50a5b4";
        RelatedTo madeProperty = RelatedTo.parse(expectedContent);
        System.out.println(madeProperty.toContent());
        assertEquals(expectedContent, madeProperty.toContent());
        RelatedTo expectedProperty = RelatedTo.parse("fc3577e0-8155-4fa2-a085-a15bdc50a5b4")
                .withRelationship("CUSTOM RELATIONSHIP");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(RelationshipType.UNKNOWN, madeProperty.getRelationship().getValue());
    }
}
