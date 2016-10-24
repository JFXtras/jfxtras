package jfxtras.icalendarfx.misc;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.junit.Test;

import javafx.collections.FXCollections;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.parameters.Encoding.EncodingType;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;

public class OrdererTest
{
    @Test
    public void canReplaceListElement()
    {
        VEvent builtComponent = new VEvent()
                .withCategories("category1")
                .withSummary("test");
        builtComponent.withCategories(FXCollections.observableArrayList(new Categories("category3"), new Categories("category4")));
        assertEquals(2, builtComponent.getCategories().size());
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:category3" + System.lineSeparator() +
                "SUMMARY:test" + System.lineSeparator() +
                "CATEGORIES:category4" + System.lineSeparator() +
                "END:VEVENT";
        VEvent expectedVEvent = VEvent.parse(expectedContent);
        assertEquals(expectedVEvent, builtComponent);
    }

    @Test
    public void canReplaceListElement2()
    {
        VEvent v = new VEvent()
                .withAttachments(new Attachment<URI>(URI.class, "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com"))
                .withSummary("test")
                .withAttachments(Attachment.parse("ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW"));
        v.withAttachments(FXCollections.observableArrayList(
                Attachment.parse("ATTACH;FMTTYPE=application/postscript:ftp://example.com/reports/r-960812.ps"),
                new Attachment<String>(String.class, "TG9yZW")
                .withFormatType("text/plain")
                .withEncoding(EncodingType.BASE64)
                .withValueType(ValueType.BINARY)
                ));
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                "ATTACH;FMTTYPE=application/postscript:ftp://example.com/reports/r-960812.ps" + System.lineSeparator() +
                "SUMMARY:test" + System.lineSeparator() +
                "ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW" + System.lineSeparator() +
                "END:VEVENT";
        VEvent expectedVEvent = VEvent.parse(expectedContent);
        assertEquals(expectedVEvent, v);
    }
    
    @Test
    public void canReplaceIndividualElement()
    {
        VEvent v = new VEvent()
                .withComments("dogs")
                .withSummary("test")
                .withDescription("cats");
        v.setSummary("birds");
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                "COMMENT:dogs" + System.lineSeparator() +
                "SUMMARY:birds" + System.lineSeparator() +
                "DESCRIPTION:cats" + System.lineSeparator() +
                "END:VEVENT";
        VEvent expectedVEvent = VEvent.parse(expectedContent);
        assertEquals(expectedVEvent, v);
    }
    
    @Test
    public void canRemoveIndividualElement()
    {
        VEvent v = new VEvent()
                .withComments("dogs")
                .withSummary("test")
                .withDescription("cats");
        v.setSummary((Summary) null);
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                "COMMENT:dogs" + System.lineSeparator() +
                "DESCRIPTION:cats" + System.lineSeparator() +
                "END:VEVENT";
        VEvent expectedVEvent = VEvent.parse(expectedContent);
        assertEquals(expectedVEvent, v);
    }
}
