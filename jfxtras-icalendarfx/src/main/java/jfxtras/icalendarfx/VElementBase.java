package jfxtras.icalendarfx;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.VChild;
import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.VElementBase;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VComponentElement;
import jfxtras.icalendarfx.parameters.VParameter;
import jfxtras.icalendarfx.parameters.VParameterElement;
import jfxtras.icalendarfx.properties.VProperty;
import jfxtras.icalendarfx.properties.VPropertyElement;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRuleElement;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RRulePart;
import jfxtras.icalendarfx.utilities.Pair;

/**
 * Base class for all VCalendar elements. 
 * 
 * Contains a map of all no-arg constructors and related methods to support all calendar elements.
 * 
 * @author David Bal
 *
 */
public abstract class VElementBase implements VElement
{
	protected static final String BEGIN = "BEGIN:";
	protected static final String END = "END:";
    
    /** Parse content line into calendar element.
     * If element contains children {@link #parseContent(String)} is invoked recursively to parse child elements also
     * 
     * @param content  calendar content string to parse
     * @return  log of information and error messages
     * @throws IllegalArgumentException  if calendar content is not valid, such as null
     */
	abstract protected List<Message> parseContent(String content);
	
	protected static void throwMessageExceptions(List<Message> messages, VElement element) throws IllegalArgumentException
	{
		// keep messages that are labeled as exceptions or produced by parsing itself (not children)
		// now throw all messages as errors
		String error = messages
        	.stream()
        	.filter(m -> ! m.message.startsWith("Unknown"))
//        	.filter(m -> (m.effect == MessageEffect.THROW_EXCEPTION) || (m.element == element))
        	.map(m -> m.element.name() + ":" + m.message)
        	.collect(Collectors.joining(System.lineSeparator()));
        if (! error.isEmpty())
        {
        	throw new IllegalArgumentException(error);
        }
	}
	
	// All no-arg constructors made from calendar element enums
	private static final  Map<Pair<Class<? extends VElement>, String>, Constructor<? extends VElement>> NO_ARG_CONSTRUCTORS = makeNoArgConstructorMap();
    private static Map<Pair<Class<? extends VElement>, String>, Constructor<? extends VElement>> makeNoArgConstructorMap()
    {
    	Map<Pair<Class<? extends VElement>, String>, Constructor<? extends VElement>> map = new HashMap<>();

    	// Add VComponent elements
    	VComponentElement[] values0 = VComponentElement.values();
    	Arrays.stream(values0)
    		.forEach(v ->
	    	{
	    		Pair<Class<? extends VElement>, String> key = new Pair<>(VComponent.class, v.toString());
				try {
					Constructor<? extends VElement> constructor = v.elementClass().getConstructor();
					map.put(key, constructor);
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
	    	});
    	
    	// Add VProperty elements
    	VPropertyElement[] values1 = VPropertyElement.values();
    	Arrays.stream(values1)
    		.forEach(v ->
	    	{
	    		Pair<Class<? extends VElement>, String> key = new Pair<>(VProperty.class, v.toString());
				try {
					Constructor<? extends VElement> constructor = v.elementClass().getConstructor();
					map.put(key, constructor);
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
	    	});
    	
    	// Add VParameter elements
    	VParameterElement[] values2 = VParameterElement.values();
    	Arrays.stream(values2)
    		.forEach(v ->
	    	{
	    		Pair<Class<? extends VElement>, String> key = new Pair<>(VParameter.class, v.toString());
				try {
					Constructor<? extends VElement> constructor = v.elementClass().getConstructor();
					map.put(key, constructor);
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
	    	});
    	
    	// Add RRULE parts
    	RRuleElement[] values3 = RRuleElement.values();
    	Arrays.stream(values3)
    		.forEach(v ->
	    	{
	    		Pair<Class<? extends VElement>, String> key = new Pair<>(RRulePart.class, v.toString());
				try {
					Constructor<? extends VElement> constructor = v.elementClass().getConstructor();
					map.put(key, constructor);
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
	    	});

        return map;
    }
    private static final Set<String> NAMES = NO_ARG_CONSTRUCTORS
    		.entrySet()
    		.stream()
    		.map(e -> e.getKey().getValue())
    		.collect(Collectors.toSet());

	public static VChild newEmptyVElement(Class<? extends VElement> superclass, String name)
	{
		try {
			if (name == null) return null;
			String name2 = (name.startsWith("X-")) ? "X-" : name;
			Constructor<? extends VElement> constructor = NO_ARG_CONSTRUCTORS.get(new Pair<>(superclass, name2));
			if (constructor == null) return null;
			return (VChild) constructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    /**
     * Creates a new VElement by parsing a String of iCalendar content text
     * @param <T>
     *
     * @param content  the text to parse, not null
     * @return  the parsed DaylightSavingTime
     */
    protected static <T extends VElementBase> T parse(T element, String valueContent)
    {
    	if (valueContent == null) return null;
    	boolean isContentValid = element.isContentValid(valueContent);
    	if (! isContentValid)
		{
    		throw new IllegalArgumentException("Invalid element:" + valueContent);
		}
        List<Message> messages = element.parseContent(valueContent);
        throwMessageExceptions(messages, element);
        return element;
    }
    
    /*
     * Checks to make sure BEGIN and END delimeters are present or any other
     * requirements met.
     */
	protected boolean isContentValid(String valueContent)
	{
		return true;
//		return valueContent != null; // override in subclasses
	}

	/**
	 * Return element name from calendar content
	 * e.g VEVENT, SUMMARY, TZID
	 * 
	 * Doesn't check if content's valid.  Returns null if no valid property name exists.
	 * 
	 * @param content
	 * @return element name
	 */
	protected static String elementName(String content)
	{
		if (content == null) return null;
		int indexOfBegin = content.indexOf(BEGIN);
		boolean isMultiline = indexOfBegin != -1;
		if (isMultiline)
		{
			int indexOfLineSeparator = content.indexOf(System.lineSeparator());
			if (indexOfLineSeparator == -1) return content.substring(indexOfBegin + BEGIN.length()); // if no line separator assume content is just one line and return all text after begin to end
			return content.substring(indexOfBegin + BEGIN.length(), indexOfLineSeparator);
		} else
		{
	        int i1 = content.indexOf(':');
	        i1 = (i1 == -1) ? Integer.MAX_VALUE : i1;
	        int i2 = content.indexOf(';');
	        i2 = (i2 == -1) ? Integer.MAX_VALUE : i2;
	        int i = Math.min(i1,i2);
	        if (i == Integer.MAX_VALUE)
	        {
	        	return null;
	        }
	        String possibleName = content.substring(0, i).toUpperCase();
	        boolean isNonStandard = (possibleName.startsWith("X-"));
	        if (isNonStandard) return possibleName;
	        boolean isStandard = NAMES.contains(possibleName);
	        if (isStandard) return possibleName;
	        return null;
		}
	}
	
	protected static class Message
	{
		public Message(VElement element, String message, MessageEffect effect) {
			super();
			this.element = element;
			this.message = message;
			this.effect = effect;
		}
		public VElement element;
		public String message;
		public MessageEffect effect;
		
		@Override
		public String toString() {
			return "Message [element=" + element + ", message=" + message + ", effect=" + effect + "]";
		}
	}
	
	public enum MessageEffect {
		MESSAGE_ONLY, THROW_EXCEPTION
	}
}
