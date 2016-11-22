package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;

public class CommentTest
{
    @Test
    public void canParseComment()
    {
        String content = "COMMENT:The meeting needs to be canceled";
        Comment madeProperty = Comment.parse(content);
        assertEquals(content, madeProperty.toContent());
        Comment expectedProperty = Comment.parse("The meeting needs to be canceled");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canParseComment2()
    {
        String content = "COMMENT;ALTREP=\"CID:part3.msg.970415T083000@example.com\";LANGUAGE=en:The meeting needs to be canceled";
        Comment madeProperty = Comment.parse(content);
        String foldedContent = ICalendarUtilities.foldLine(content).toString();
        assertEquals(foldedContent, madeProperty.toContent());
        Comment expectedProperty = Comment.parse("The meeting needs to be canceled")
                .withAlternateText("CID:part3.msg.970415T083000@example.com")
                .withLanguage("en");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canParseComment3()
    {
        String content = "The meeting needs to be canceled";
        Comment madeProperty = Comment.parse(content);
        assertEquals("COMMENT:" + content, madeProperty.toContent());
        Comment expectedProperty = Comment.parse("The meeting needs to be canceled");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canSetParent()
    {
        String content = "COMMENT;LANGUAGE=en:Department Party";
        Comment property1 = Comment.parse(content);
        VEvent v = new VEvent().withComments(property1);
        assertTrue(v == property1.getParent());
        Comment propertyCopy = new Comment();
       property1.copyInto(propertyCopy);
//        propertyCopy.copyChildrenFrom(property1);
        assertEquals(propertyCopy, property1);
        v.getComments().add(propertyCopy);
        assertTrue(v == property1.getParent());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void canCatchDifferentCopyType()
    {
        String content = "COMMENT;LANGUAGE=en:Department Party";
        Comment property1 = Comment.parse(content);
        Summary propertyCopy = new Summary();
        property1.copyInto(propertyCopy);
//        propertyCopy.copyChildrenFrom(property1);
    }
}
