/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
