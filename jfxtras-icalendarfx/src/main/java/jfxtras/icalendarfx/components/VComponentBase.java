package jfxtras.icalendarfx.components;

import jfxtras.icalendarfx.VParent;
import jfxtras.icalendarfx.VParentBase;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VComponentBase;
import jfxtras.icalendarfx.components.VComponentElement;
import jfxtras.icalendarfx.content.MultiLineContent;;

/**
 * <p>Base class implementation of a {@link VComponent}</p>
 * 
 * @author David Bal
 */
public abstract class VComponentBase<T> extends VParentBase<T> implements VComponent
{   
    protected VParent parent;
    @Override public void setParent(VParent parent) { this.parent = parent; }
    @Override public VParent getParent() { return parent; }
    
    final private VComponentElement componentType;
    @Override
    public String name() { return componentType.toString(); }

    /*
     * CONSTRUCTORS
     */
    /**
     * Create default component by setting {@link componentName}, and setting content line generator.
     */
    VComponentBase()
    {
    	super();
    	componentType = VComponentElement.fromClass(this.getClass());
        contentLineGenerator = new MultiLineContent(
                orderer,
                BEGIN + name(),
                END + name(),
                400);
    }
    
    /**
     * Creates a deep copy of a component
     */
    VComponentBase(VComponentBase<T> source)
    {
    	super(source);
    	componentType = VComponentElement.fromClass(this.getClass());
        contentLineGenerator = new MultiLineContent(
                orderer,
                BEGIN + name(),
                END + name(),
                400);
        setParent(source.getParent());
    }
   
    /**
     * Hook to add subcomponent such as {@link #VAlarm}, {@link #StandardTime} and {@link #DaylightSavingTime}
     * 
     * @param subcomponent
     */
    void addSubcomponent(VComponent subcomponent)
    { // no opp by default
    }
    
	@Override
	protected boolean isContentValid(String valueContent)
	{
		boolean isElementValid = super.isContentValid(valueContent);
		if (! isElementValid) return false;
		boolean isBeginPresent = valueContent.startsWith(BEGIN + name());
		if (! isBeginPresent) return false;
		int lastLineIndex = valueContent.lastIndexOf(System.lineSeparator());
		if (lastLineIndex == -1) return false;
		boolean isEndPresent = valueContent
				.substring(lastLineIndex)
				.startsWith(END + name());
		return ! isEndPresent;
	}
    
//    /**
//     * Creates a new VComponent by parsing a String of iCalendar content text
//     * @param <T>
//     *
//     * @param content  the text to parse, not null
//     * @return  the parsed DaylightSavingTime
//     */
//    public static <T extends VComponentBase<?>> T parse(String content)
//    {
//        boolean isMultiLineElement = content.startsWith("BEGIN");
//        if (! isMultiLineElement)
//        {
//        	throw new IllegalArgumentException("VComponent must begin with BEGIN [" + content + "]");
//        }
//        int firstLineBreakIndex = content.indexOf(System.lineSeparator());
//        String name = content.substring(6,firstLineBreakIndex);
//    	T component = (T) Elements.newEmptyVElement(VComponent.class, name);
//        List<Message> messages = component.parseContent(content);
//        throwMessageExceptions(messages);
//        return component;
//    }
}
