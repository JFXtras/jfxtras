/**
 * GenericLayoutConstraints.java
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

package jfxtras.scene.layout;

import javafx.scene.Node;

class GenericLayoutConstraints
{
	/**
	 * The layout constraints
	 *
	 */
	public static class C<T>
	{
		// minWidth
		public T minWidth(double value) { this.minWidth = value; return (T)this; }
		double minWidth = -1;
		double minWidthReset = UNKNOWN;
		
		// prefWidth
		public T prefWidth(double value) { this.prefWidth = value; return (T)this; }
		double prefWidth = -1;
		double prefWidthReset = UNKNOWN;
		
		// maxWidth
		public T maxWidth(double value) { this.maxWidth = value; return (T)this; }
		double maxWidth = -1;
		double maxWidthReset = UNKNOWN;

		// minHeight
		public T minHeight(double value) { this.minHeight = value; return (T)this; }
		double minHeight = -1;
		double minHeightReset = UNKNOWN;

		// prefHeight
		public T prefHeight(double value) { this.prefHeight = value; return (T)this; }
		double prefHeight = -1;
		double prefHeightReset = UNKNOWN;
		
		// maxHeight
		public T maxHeight(double value) { this.maxHeight = value; return (T)this; }
		double maxHeight = -1;
		double maxHeightReset = UNKNOWN;
		
		/**
		 * @param node
		 */
		protected void rememberResetValues(Node node)
		{
			if (node instanceof javafx.scene.layout.Region)
			{
				javafx.scene.layout.Region lRegion = (javafx.scene.layout.Region)node;
				
				// setup the reset values on the first apply
				if (minWidthReset == UNKNOWN) minWidthReset = lRegion.getMinWidth();
				if (prefWidthReset == UNKNOWN) prefWidthReset = lRegion.getPrefWidth();
				if (maxWidthReset == UNKNOWN) maxWidthReset = lRegion.getMaxWidth();
				if (minHeightReset == UNKNOWN) minHeightReset = lRegion.getMinHeight();
				if (prefHeightReset == UNKNOWN) prefHeightReset = lRegion.getPrefHeight();
				if (maxHeightReset == UNKNOWN) maxHeightReset = lRegion.getMaxHeight();
			}
		}
		
		/**
		 * @param node
		 */
		protected void apply(Node node)
		{
			if (node instanceof javafx.scene.layout.Region)
			{
				javafx.scene.layout.Region lRegion = (javafx.scene.layout.Region)node;
				
				// setup the reset values on the first apply
				rememberResetValues(lRegion);
				
				// either set or reset values
				lRegion.setMinWidth(minWidth >= 0 ? minWidth : minWidthReset);
				lRegion.setPrefWidth(prefWidth >= 0 ? prefWidth : prefWidthReset);
				lRegion.setMaxWidth(maxWidth >= 0 ? maxWidth : maxWidthReset);
				lRegion.setMinHeight(minHeight >= 0 ? minHeight : minHeightReset);
				lRegion.setPrefHeight(prefHeight >= 0 ? prefHeight : prefHeightReset);
				lRegion.setMaxHeight(maxHeight >= 0 ? maxHeight : maxHeightReset);
			}
		}
		
	}
	static final double UNKNOWN = -Double.MIN_NORMAL + 10.0;
	
	/**
	 * 
	 */
	static public void overrideMaxWidth(Node node, C c)
	{
		// just to prevent problems
		if (node == null) return;
		
		// make max issues go away
		if (node instanceof javafx.scene.layout.Region)
		{
			javafx.scene.layout.Region lRegion = (javafx.scene.layout.Region)node;
			lRegion.setMaxWidth( c.maxWidth >= 0 ? c.maxWidth : Double.MAX_VALUE);
		}
	}
	
	/**
	 * 
	 */
	static public void overrideMaxHeight(Node node, C c)
	{
		// just to prevent problems
		if (node == null) return;
		
		// make max issues go away
		if (node instanceof javafx.scene.layout.Region)
		{
			javafx.scene.layout.Region lRegion = (javafx.scene.layout.Region)node;
			lRegion.setMaxHeight( c.maxHeight >= 0 ? c.maxHeight : Double.MAX_VALUE);
		}
	}
	
}
