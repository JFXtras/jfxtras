package jfxtras.icalendarfx;

import java.util.List;

/**
 * <p>Parent calendar components.  Parent components can have children.</p>
 * 
 * <p>Note: Adding children is not exposed, but rather handled internally when a calendar 
 * element is set or changed.</p>
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
     * Copy this {@link VParent} into destination {@link VParent}
     */
    void copyInto(VParent destination);
}
