/**
 * ListSpinnerBigIntegerList.java
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

import java.math.BigInteger;

/**
 * Items for Spinner providing an BigInteger range without actually creating a list with all values.
 * Beware: because this is still based on the list inteface, the maximum size of the list is limited by the type used for the index in the list: an integer (Integer.MAX_VALUE).
 * So the difference between the from and to values (to-from) cannot be larger than Integer.MAX_VALUE.
 * What this class allows is that this range can be anywhere in the BigInteger's range. 
 */
public class ListSpinnerBigIntegerList extends java.util.AbstractList<BigInteger>
{
	/**
	 * 
	 */
	public ListSpinnerBigIntegerList()
	{
		this( BigInteger.valueOf(Integer.MIN_VALUE / 2).add(BigInteger.ONE), BigInteger.valueOf(Integer.MAX_VALUE / 2), BigInteger.ONE);
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 */
	public ListSpinnerBigIntegerList(BigInteger from, BigInteger to)
	{
		this(from, to, from.compareTo(to) > 0 ? BigInteger.valueOf(-1) : BigInteger.ONE);
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @param step
	 */
	public ListSpinnerBigIntegerList(BigInteger from, BigInteger to, BigInteger step)
	{
		this.from = from;
		this.size = to.subtract(from).divide(step).add(BigInteger.ONE).intValue();
		if (this.size < 0) throw new IllegalArgumentException("This results in a negative size: " + from + ", " + to + "," + step);
		this.step = step;
	}
	private BigInteger from;
	private int size;
	private BigInteger step;
	
	
	// ===============================================================================
	// List interface
	
	@Override
	public BigInteger get(int index)
	{
		if (index < 0) throw new IllegalArgumentException("Index cannot be < 0: " + index);
		BigInteger lValue = this.from.add(BigInteger.valueOf(index).multiply(this.step));
		return lValue;
	}

	@Override
	public int indexOf(Object o)
	{
		// calculate the index
		BigInteger lValue = (BigInteger)o;
		BigInteger lIndexBigInteger = lValue.subtract(this.from).divide(this.step);
		int lIndex = lIndexBigInteger.intValue();
		if (lIndex > size) return -1;
		
		// check if that what is at the index matches with out value
		BigInteger lValueAtIndex = get(lIndex);
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