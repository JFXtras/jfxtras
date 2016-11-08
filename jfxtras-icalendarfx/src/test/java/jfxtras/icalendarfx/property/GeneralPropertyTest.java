package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import javafx.util.Pair;
import jfxtras.icalendarfx.parameters.ValueParameter;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.icalendarfx.properties.component.misc.NonStandardProperty;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;

public class GeneralPropertyTest
{
    @Test
    public void canMakeParameterList()
    {
        String contentLine = "DESCRIPTION;ALTREP=\"CID:part3.msg.970415T083000@example.com\";LANGUAGE=en:Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management";
        List<Pair<String, String>> valueList = ICalendarUtilities.contentToParameterListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>("ALTREP", "\"CID:part3.msg.970415T083000@example.com\""));
        expectedList.add(new Pair<>("LANGUAGE", "en"));
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, "Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management"));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canMakeParameterList2()
    {
        String contentLine = "DESCRIPTION:";
        List<Pair<String, String>> valueList = ICalendarUtilities.contentToParameterListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, ""));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canMakeParameterList3()
    {
        String contentLine = ":";
        List<Pair<String, String>> valueList = ICalendarUtilities.contentToParameterListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, ""));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canMakeParameterList4()
    {
        String contentLine = "SUMMARY:Department Party";
        List<Pair<String, String>> valueList = ICalendarUtilities.contentToParameterListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, "Department Party"));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canMakeParameterList5()
    {
        String contentLine = "Department Party";
        List<Pair<String, String>> valueList = ICalendarUtilities.contentToParameterListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, "Department Party"));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canMakeParameterList6()
    {
        String contentLine = ";LANGUAGE=en:TEST SUMMARY";
        List<Pair<String, String>> valueList = ICalendarUtilities.contentToParameterListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>("LANGUAGE", "en"));
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, "TEST SUMMARY"));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canMakeParameterList7()
    {
        String contentLine = "LANGUAGE=en:TEST SUMMARY";
        List<Pair<String, String>> valueList = ICalendarUtilities.contentToParameterListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>("LANGUAGE", "en"));
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, "TEST SUMMARY"));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canChangeValueType()
    {
        String content = "X-MYPROP:1";
        NonStandardProperty madeProperty = NonStandardProperty.parse(content);
        madeProperty.valueTypeProperty().set(new ValueParameter(ValueType.INTEGER));
        assertEquals(1, madeProperty.getValue());
    }
    
    @Test
    public void canEscapeSpecialCharacters()
    {
        String content = "the dog, frog; ran \\ far\naway\\;";
        String contentEscaped = "the dog\\, frog\\; ran \\\\ far\\naway\\\\\\;";
        String content2 = (String) ValueType.TEXT.getConverter().fromString(contentEscaped);
        String contentEscaped2 = ValueType.TEXT.getConverter().toString(content);
        assertEquals(content2, content);
        assertEquals(contentEscaped2, contentEscaped);
    }
    
    @Test // can handle lowercase in property and parameter name
    public void canHandleLowercase()
    {
        String content = "sUmmARY;lanGUAGE=en:TEST SUMMARY";
        Summary madeProperty = Summary.parse(content);
        assertEquals("SUMMARY;LANGUAGE=en:TEST SUMMARY", madeProperty.toContent());
        Summary expectedProperty = Summary.parse("TEST SUMMARY")
                .withLanguage("en");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void canCatchDuplicateParameter()
    {
        String contentLines = "SUMMARY;LANGUAGE=en;LANGUAGE=fr:TEST SUMMARY";
        Summary.parse(contentLines);
    }
    
    @Test
    public void canCheckEqualityEmptyProperty()
    {
        Summary p1 = new Summary();
        Summary p2 = new Summary();
        assertEquals(p1, p2);
        assertEquals("SUMMARY:", p2.toContent());
        assertEquals(p1.toContent(), p2.toContent());
    }
    
    @Test // property with non-standard parameters
    public void canHandleNonStandardParameters()
    {
        String content = "SUMMARY;X-PARAM1=VALUE1;X-PARAM2=VALUE2:Department Party";
        Summary madeProperty = Summary.parse(content);
        assertEquals(content, madeProperty.toContent());
        Summary expectedProperty = new Summary()
                .withValue("Department Party")
                .withNonStandard("X-PARAM1=VALUE1", "X-PARAM2=VALUE2");
        assertEquals(expectedProperty, madeProperty);
    }
    
//    @Test // property with non-standard parameters
//    public void canHandleIANAParameters()
//    {
//        String content = "SUMMARY;TESTPARAM1=VALUE1;TESTPARAM2=VALUE2:Department Party";
//        IANAParameter.setRegisteredIANAParameters(Arrays.asList("TESTPARAM1", "TESTPARAM2"));
//        Summary madeProperty = Summary.parse(content);
//        assertEquals(content, madeProperty.toContent());
//        Summary expectedProperty = new Summary()
//                .withValue("Department Party")
//                .withIana("TESTPARAM1=VALUE1", "TESTPARAM2=VALUE2");
//        assertEquals(expectedProperty, madeProperty);
//    }
    
    @Test
    public void canIgnoreUnrecognizedParameter()
    {
        String content = "SUMMARY;IGNORE-THIS-PARAMETER=VALUE1:test value";
        Summary madeProperty = Summary.parse(content);
        assertEquals("SUMMARY:test value", madeProperty.toContent());
    }
}
