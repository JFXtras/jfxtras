/**
 * Copyright (c) 2011-2021, JFXtras
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
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx;

import java.util.List;

import jfxtras.icalendarfx.VChild;
import jfxtras.icalendarfx.VElement;

/**
 * <p>Parent calendar components (e.g. VCALENDAR, VEVENT, SUMMARY, RRULE value).  Parent components can have children.</p>
 * 
 * <p>Note: Implementation of adding children for different parent types is not exposed,
 * but rather handled internally when a calendar element is set or changed.</p>
 * 
 * @author David Bal
 */
public interface VParent extends VElement
{
    /** 
     * <p>Returns unmodifiable list of {@link VChild} elements.</p>
     * 
     * @return  unmodifiable list of children
     */
    List<VChild> childrenUnmodifiable();

    /**
     * Add child element to parent by parsing content text
     * 
     * @param child element to add to ordered list
     */
    @Deprecated // do I want this??? - it acts like parseInto
    void addChild(String childContent);
    
    /**
     * Add child element to parent.
     * 
     * @param child element to add to ordered list
     */
    void addChild(VChild child);

    /**
     * 
     * @param index index where child element is to be put
     * @param child element to add to ordered list
     */
	void addChild(int index, VChild child);

	/**
     * Remove child from parent.
     * 
     * @param child element to add to ordered list
     * @return true is success, false if failure
     */
    boolean removeChild(VChild child);

    /**
     * 
     * @param index index of old child element to be removed
     * @param child new child element to put at index
     * @return
     */
	boolean replaceChild(int index, VChild child);

	/**
	 * 
	 * @param oldChild old child element to be removed
	 * @param newChild new child element to put at index where oldChild was
	 * @return
	 */
	boolean replaceChild(VChild oldChild, VChild newChild);
	
    /**
     * 
     * @param index  index of child element to be removed
     * @return true is success, false if failure
     */
    boolean removeChild(int index);
    
	/** Add the child to the end of the ordered list 
	 * Should only be used for list-based children that are added by accessing the
	 * list.  A better alternative would be to use the {@link addChild} method which
	 * automatically adds a child and orders it.  
	 * */
	void orderChild(VChild child);
	
	/** Insert the child at the index in the ordered list */
	void orderChild(int index, VChild child);

	/** Replace the oldChild with the newChild in the ordered list */
	void orderChild(VChild oldChild, VChild newChild);
}
