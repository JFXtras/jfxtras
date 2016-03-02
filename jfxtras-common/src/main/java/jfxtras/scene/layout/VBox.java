/**
 * VBox.java
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

import java.util.WeakHashMap;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;

/**
 * A drop-in replacement for JavaFX's VBox using layout constraints.
 * So instead of:
 * [source,java]
 * --
 * 	VBox lVBox = new VBox(5.0);
 * 	Button b1 = new Button("short");
 * 	lVBox.getChildren().add(b1);
 * 	VBox.setVgrow(b1, Priority.ALWAYS);
 * --
 *
 * You can write:
 * [source,java]
 * --
 *	VBox lVBox = new VBox(5.0);
 *	lVBox.add(new Button("short"), new VBox.C().vgrow(Priority.ALWAYS));
 * --
 *
 * This class is not a reimplementation of VBox, but only applies a different API.
 * 
 * @author Tom Eugelink
 *
 */
public class VBox extends javafx.scene.layout.VBox
{
	// ========================================================================================================================================================
	// Constructors
	
	/**
	 * 
	 */
	public VBox()
	{
		super();
		construct();
	}
	
	/**
	 * 
	 * @param spacing
	 */
	public VBox(double spacing)
	{
		super(spacing);
		construct();
	}

	/**
	 * 
	 * @param spacing
	 * @param nodes
	 */
	public VBox(double spacing, Node... nodes) {
		super(spacing, nodes);
		construct();
	}

	/**
	 * 
	 * @param nodes
	 */
	public VBox(Node... nodes) {
		super(nodes);
		construct();
	}

	/**
	 * 
	 */
	private void construct()
	{
		getChildren().addListener(new ListChangeListener<Node>()
		{
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Node> changes)
			{
				while (changes.next())
				{
					for (Node lNode : changes.getAddedSubList())
					{
						C lC = cMap.get(lNode);
						if (lC != null) lC.apply(lNode);
					}
				}
			}
		});
	}
	
	
	// ========================================================================================================================================================
	// Properties
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public VBox withSpacing(double value)
	{
		super.setSpacing(value);
		return this;
	}
	
	
	// ========================================================================================================================================================
	// Layout constraints
	
	/**
	 * The layout constraints
	 */
	public static class C extends GenericLayoutConstraints.C<C>
	{
		// vgrow
		public C vgrow(javafx.scene.layout.Priority value) { this.vgrow = value; return this; }
		private javafx.scene.layout.Priority vgrow = null;
		private javafx.scene.layout.Priority vgrowReset = null;
		
		// margin
		public C margin(javafx.geometry.Insets value) { this.margin= value; return this; }
		javafx.geometry.Insets margin = null;
		javafx.geometry.Insets marginReset = null;
		
		/**
		 * @param node
		 */
		protected void rememberResetValues(Node node)
		{
			super.rememberResetValues(node);
			vgrowReset = javafx.scene.layout.VBox.getVgrow(node);
			marginReset = javafx.scene.layout.VBox.getMargin(node);
		}
		
		/**
		 * 
		 * @param node
		 */
		protected void apply(Node node)
		{
			// sanatize the node
			super.apply(node);

			// apply constraints
			if (vgrow != null) GenericLayoutConstraints.overrideMaxHeight(node, this);
			javafx.scene.layout.VBox.setVgrow(node, vgrow != null ? vgrow : vgrowReset);
			javafx.scene.layout.VBox.setMargin(node, margin != null ? margin : marginReset);
		}
	}
	
	/**
	 * The collection of layout constraints
	 */
	private WeakHashMap<Node, C> cMap = new WeakHashMap<>();

	/**
	 * 
	 * @param node
	 */
	public VBox add(Node node)
	{
		// add node
		getChildren().add(node);
		return this;
	}

	/**
	 * 
	 * @param node
	 * @param c
	 */
	public VBox add(Node node, C c)
	{
		// remember constraints
		cMap.put(node, c);
		c.rememberResetValues(node);
		
		// add node
		getChildren().add(node);
		return this;
	}

	/**
	 * Remove a node completely
	 * @param node
	 */
	public VBox remove(Node node)
	{
		// remove node
		getChildren().remove(node);
		
		// remove constraints
		cMap.remove(node);
		return this;
	}

	/**
	 * set constraint without adding the node (in case the node might end up here because of an animation or something)
	 * @param node
	 * @param c
	 */
	public void setConstraint(Node node, C c)
	{
		// remember constraints
		cMap.put(node, c);
		c.rememberResetValues(node);
	}

	/**
	 * Remove a constraint, not the node.
	 * @param node
	 */
	public void removeConstraintsFor(Node node)
	{
		cMap.remove(node);
	}
	
	/**
	 * Remove a node, not the constraints.
	 * @param node
	 */
	public void removeNode(Node node)
	{
		getChildren().remove(node);
	}
}
