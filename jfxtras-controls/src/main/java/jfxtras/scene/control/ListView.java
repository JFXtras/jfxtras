/**
 * ListView.java
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

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.internal.scene.control.skin.ListViewSkinJFXtras;

/**
 * Drop in replacement for ListView, adds a two-way bindable selectedItem property.
 * 
 * @param <T>
 */
public class ListView<T> extends javafx.scene.control.ListView<T>
{
	// =====================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public ListView()
	{
		super();
		construct();
	}

	/**
	 * 
	 * @param arg0
	 */
	public ListView(ObservableList<T> arg0)
	{
		super(arg0);
		construct();
	}

	/**
	 * 
	 */
	private void construct()
	{
		// use a custom skin to enable refreshing
		setStyle("-fx-skin: \"jfxtras.internal.scene.control.skin.ListViewSkinJFXtras\";");
		
		// construct the properties
		constructSelectedItem();
	}
	
	// =====================================================================================================
	// REFRESH
	
	/**
	 * Force the contents to be refreshed
	 */
	public void refresh()
	{
		((ListViewSkinJFXtras<T>)getSkin()).refresh();
	}
	
	
	// =====================================================================================================
	// BINDABLE SELECTED ITEM PROPERTY
	
	/** 
	 * A direct accessable and two way bindable selected item property (unlike the one in selection model). 
	 * If in multi select mode, this will be the last selected item.
	 * If you bind bidirectional to this property in multi selected mode, it will keep selecting just one item. 
	 */
	public ObjectProperty<T> selectedItemProperty() { return this.selectedItemObjectProperty; }
	final private ObjectProperty<T> selectedItemObjectProperty = new SimpleObjectProperty<T>(this, "selectedItem", null);
	// java bean API
	public T getSelectedItem() { return this.selectedItemObjectProperty.getValue(); }
	public void setSelectedItem(T value) { this.selectedItemObjectProperty.setValue(value); }
	public ListView<T> withSelectedItem(T value) { setSelectedItem(value); return this; }
	// construct
	private void constructSelectedItem()
	{
		// when the selectedItem in the selectionModel changes, update our selectedItem
		getSelectionModel().selectedItemProperty().addListener(new InvalidationListener()
		{			
			@Override
			public void invalidated(Observable arg0)
			{
				if (getSelectionModel().selectedItemProperty().get() != selectedItemObjectProperty.get())
				{
					selectedItemObjectProperty.set( getSelectionModel().selectedItemProperty().get() );
				}
			}
		});
		// and vice versa
		selectedItemObjectProperty.addListener(new InvalidationListener()
		{			
			@Override
			public void invalidated(Observable arg0)
			{
				if (getSelectionModel().selectedItemProperty().get() != selectedItemObjectProperty.get())
				{
					getSelectionModel().select( selectedItemObjectProperty.get() );
				}
			}
		});
	}
	
}
