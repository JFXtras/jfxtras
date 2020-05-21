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
package jfxtras.icalendarfx.content;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.VChild;
import jfxtras.icalendarfx.VParent;
import jfxtras.icalendarfx.components.VComponentBase;
import jfxtras.icalendarfx.properties.VPropertyBase;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;


/**
 *  Maintains a sort order of {@link VChild} elements of a {@link VParent}
 *
 *  Individual children are added automatically, list-based children are added through calling
 *  {@link #addChild(VChild) addChild} method.
 * 
 * @see VParent
 * @see VCalendar
 * @see VComponentBase
 * @see VPropertyBase
 * @see RecurrenceRuleValue
 *  */ 
public class OrdererBase implements Orderer
{
	final private VParent parent;
    final private Map<Class<? extends VChild>, Method> childGetters;
    
    private List<VChild> orderedChildren = new ArrayList<>();

    /*
     * CONSTRUCTOR
     */
    /** Create an {@link OrdererBase} for the {@link VParent} parameter */
    public OrdererBase(VParent aParent, Map<Class<? extends VChild>, Method> map)
    {
        this.parent = aParent;
        this.childGetters = map;
    }

	@Override
	public List<VChild> childrenUnmodifiable()
	{
		return orderedChildren;
	}
	
    private List<VChild> allUnorderedChildren(VParent parent, Map<Class<? extends VChild>, Method> childGetters2)
    {
    	return Collections.unmodifiableList(childGetters2
			.entrySet()
    		.stream()
    		.map(e -> e.getValue())
    		.map(m -> {
				try {
					return m.invoke(parent);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
				return null;
			})
    		.filter(p -> p != null)
    		.flatMap(p -> 
    		{
    			if (Collection.class.isAssignableFrom(p.getClass()))
    			{
    				return ((Collection<VChild>) p).stream();
    			} else
    			{
    				return Arrays.stream(new VChild[]{ (VChild) p });
    			}
    		})
    		.collect(Collectors.toList())
		);
    }

	@Override
	public void orderChild(VChild newChild)
	{
		if (newChild == parent) throw new RuntimeException("Can't add you to yourself!");
		orderedChildren.add(newChild);
		newChild.setParent(parent);
	}
	
	/* Remove orphans matching newChild's class type
	 * NOT using because it is too expensive. 
	 * If addChild is used it's not required.
	 *  */
	private void removeOrphans(VChild newChild)
	{
		List<VChild> allUnorderedChildren = allUnorderedChildren(parent, childGetters);
		List<VChild> orphans = orderedChildren
				.stream()
				.filter(c -> c.getClass().equals(newChild.getClass()))
				.filter(c -> ! allUnorderedChildren.contains(c))
				.collect(Collectors.toList());
		orphans.forEach(c -> orderedChildren.remove(c));
	}

	@Override
	public void orderChild(int index, VChild newChild)
	{
		if (newChild != null)
		{
			orderedChildren.remove(newChild);
			orderedChildren.add(index, newChild);
			newChild.setParent(parent);
		}
	}
	
	@Override
	public boolean replaceChild(VChild oldChild, VChild newChild)
	{
		if (newChild == null)
		{
			if (oldChild != null)
			{
				return orderedChildren.remove(oldChild);
			}
		} else if (oldChild == null)
		{
			orderChild(newChild);
		} else
		{
			int index = orderedChildren.indexOf(oldChild);
			VChild result = orderedChildren.set(index, newChild);
			return result.equals(oldChild);
		}
		return false;
	}

    @Override
	public String toString()
    {
		return "OrdererBase [parent=" + parent + ", orderedChildren=" + orderedChildren + "]";
	}
}
