/**
 * ListSpinnerIntegerList.java
 *
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
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

package jfxtras.scene.control;

/**
 * Items for Spinner providing an integer range without actually creating a list with all values.
 */
public class ListSpinnerIntegerList extends java.util.AbstractList<Integer>
{
	/**
	 * 
	 */
	public ListSpinnerIntegerList()
	{
		this( (Integer.MIN_VALUE / 2) + 1, Integer.MAX_VALUE / 2, 1);
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 */
	public ListSpinnerIntegerList(int from, int to)
	{
		this(from, to, from > to ? -1 : 1);
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @param step
	 */
	public ListSpinnerIntegerList(int from, int to, int step)
	{
		this.from = from;
		this.size = ((to - from) / step) + 1;
		if (size < 0) throw new IllegalArgumentException("This results in a negative size: " + from + ", " + to + "," + step);
		this.step = step;
	}
	private int from;
	private int size;
	private int step;
	
	
	// ===============================================================================
	// List interface
	
	@Override
	public Integer get(int index)
	{
		if (index < 0) throw new IllegalArgumentException("Index cannot be < 0: " + index);
		int lValue = this.from + (index * this.step);
		return lValue;
	}

	@Override
	public int indexOf(Object o)
	{
		// calculate the index
		int lValue = ((Integer)o).intValue();
		int lIndex = (lValue - this.from) / this.step;
		if (lIndex < 0 || lIndex >= size) return -1;
		
		// check if that what is at the index matches with out value
		Integer lValueAtIndex = get(lIndex);
		if (o.equals(lValueAtIndex) == false) return -1;
		
		// found it
		return lIndex;
	}
	
	@Override
	public int size()
	{
		return this.size;
	}
}