package jfxtras.icalendarfx.content;

import java.util.List;

import jfxtras.icalendarfx.VChild;
import jfxtras.icalendarfx.VParent;

/** Maintains a sort order of {@link VChild} elements of a {@link VParent}
*
*  Individual children are added automatically, list-based children are added through calling
*  {@link #addChild(VChild) addChild} method
*  */
public interface Orderer
{
	/**
	 * List of children in proper order
	 * 
	 * Orphaned children are automatically removed
	 * Non-ordered children are included
	 */
	List<VChild> childrenUnmodifiable();
	
	/** Add the next child to the list */
	void orderChild(VChild newChild);

	/** Add the next child to the list */
	void orderChild(int index, VChild newChild);

	/** Replace oldChild with newChild in ordered list 
	 * @return true if success, false if failure
	 * */
	boolean replaceChild(VChild oldChild, VChild newChild);
}
