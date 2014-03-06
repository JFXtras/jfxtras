/**
 * ToggleGroupValue.java
 *
 * Copyright (c) 2011-2014, JFXtras
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
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 * An extended ToggleGroup that adds a value property.
 * Toggles should be added to this group using the add method, which takes a toggle and a value. 
 * Whenever the selected toggle changes, the corresponding value is set in the value property.
 * Vice versa, when the value property is set, the corresponding toggle is selected.
 * Note: of course values have to be unique and null is used for when no toggle is selected.
 * 
 * @author Tom Eugelink
 *
 * @param <T>
 */
public class ToggleGroupValue<T> extends ToggleGroup
{
	/**
	 * 
	 */
	public ToggleGroupValue()
	{
		construct();
	}
	
	/**
	 * 
	 */
	private void construct()
	{
		selectedToggleProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable toggleProperty)
			{
				// get selected toggle
				Toggle lToggle = selectedToggleProperty().get();
				if (lToggle == null) 
				{
					valueObjectProperty.set(null);
				}
				else
				{
					T lValue = (T)lToggle.getUserData();
					valueObjectProperty.set( lValue );
				}
			}
		});
// totally dummy listener just which prevents the other to get unregistered		
valueProperty().addListener(new InvalidationListener()
{
	@Override
	public void invalidated(Observable arg0)
	{
		System.out.println("value invalidated " + valueObjectProperty);
	}
});
	}
	
	/**
	 * Convenience method for toggle's setToggleGroup and setUserData.
	 * @param toggle
	 * @param value
	 */
	public void add(Toggle toggle, T value)
	{
		toggle.setToggleGroup(this);
		toggle.setUserData(value);
	}
	
	/** Value: */
	public ObjectProperty<T> valueProperty() { return this.valueObjectProperty; }
	final private ObjectProperty<T> valueObjectProperty = new SimpleObjectProperty<T>(this, "value", null)
	{
		public void set(T value)
		{
//System.out.println("set " + value);			
			// do it
			super.set(value);

			// if null
			if (value == null)
			{
				// deselect
				selectToggle(null);
				return;
			}

			// scan all toggles
			for (Toggle lToggle : getToggles())
			{
				// if user data is equal 
				if (lToggle.getUserData() != null && lToggle.getUserData().equals(value))
				{
					// set toggle if required
					if (getSelectedToggle() != lToggle)
					{
						selectToggle(lToggle);
						return;
					}
				}
			}
		}
	};
	// java bean API
	public T getValue() { return this.valueObjectProperty.getValue(); }
	public void setValue(T value) { this.valueObjectProperty.setValue(value); }
	public ToggleGroupValue<T> withValue(T value) { setValue(value); return this; }
}
