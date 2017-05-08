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
