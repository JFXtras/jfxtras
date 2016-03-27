/**
 * ListSpinner.java
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

import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * This is a spinner, showing one value at a time from a list.
 * This value is set and retrieved through the value property.
 * Basically a spinner shows a list of values and can do a "next" or "previous".
 * 
 * A spinner can be editable, the user can then type a value instead of selecting it.
 * If the value exists in the list, the spinner will simply jump to it. 
 * If the value does not exist, the AddCallback is called if defined.
 * 
 * - If the AddCallback returns null, spinner will only refresh the current index.
 * - If the AddCallback returns an Integer, spinner will jump to that index (usually the index where the new value was added to the list).   
 *
 * 
 * '''
 * 
 * In the default skin you can style the text in the control using CSS like so:
 * [source,css]
 * --
 * .ListSpinner .value { 
 *     -fx-font-weight: bold;
 * }
 * --
 * 
 * The "value" class applies to the text in both readonly and editable spinners, use the "readonly" or "editable" class to style either mode specifically.
 * There is a left-arrow, right-arrow, up-arrow and down-arrow class that uses a SVG path to draw the arrow, this can be overridden with another SVG to draw a different shape.
 * 
 * The default skin has a number of styleable properies which use the text representation of an enum for their value:
 * 
 * [source,css]
 * --
 * .ListSpinner { 
 *     -fxx-arrow-position: {LEADING, TRAILING, SPLIT}
 *     -fxx-arrow-direction: {VERTICAL, HORIZONTAL}
 *     -fxx-value-alignment: see javafx.geometry.Pos (https://docs.oracle.com/javase/8/javafx/api/javafx/geometry/Pos.html) 
 * }
 * --
 * 
 * @author Tom Eugelink
 */
public class ListSpinner<T> extends Control
{
	// TODO: implement SelectionModel?
	// ==================================================================================================================
	// CONSTRUCTOR

	/**
	 */
	public ListSpinner()
	{
		construct();
	}

	// ------------
	// model
	
	/**
	 * @param items The item list used to populate the spinner.
	 */
	public ListSpinner(ObservableList<T> items)
	{
		construct();
		setItems(items);
		first();
	}

	/**
	 * @param items The item list used to populate the spinner.
	 * @param startValue The initial value of the spinner (one of the items).
	 */
	public ListSpinner(ObservableList<T> items, T startValue)
	{
		construct();
		setItems(items);
		setValue(startValue);
	}

	// ------------
	// convenience
	
	/**
	 * @param list
	 */
	public ListSpinner(final java.util.List<T> list)
	{
		this( FXCollections.observableList(list) );
	}

	/**
	 * @param list
	 */
	public ListSpinner(T... list)
	{
		this( Arrays.asList(list) );
	}

	/**
	 * @param from
	 * @param to
	 */
	public ListSpinner(int from, int to)
	{
		this( (java.util.List<T>) new ListSpinnerIntegerList(from, to) );
	}

	/**
	 * @param from
	 * @param to
	 * @param step
	 */
	public ListSpinner(int from, int to, int step)
	{
		this( (java.util.List<T>) new ListSpinnerIntegerList(from, to, step) );
	}

	// ------------
	
	/*
	 * 
	 */
	private void construct()
	{
		// setup the CSS
		// the -fx-skin attribute in the CSS sets which Skin class is used
		this.getStyleClass().add(ListSpinner.class.getSimpleName());
		
		// react to changes of the value
		this.valueObjectProperty.addListener(new ChangeListener<T>()
		{
			@Override
			public void changed(ObservableValue<? extends T> property, T oldValue, T newValue)
			{
				// get the value of the new index
				int lIdx = getItems().indexOf(newValue);
				
				// set the value
				if (ListSpinner.equals(indexObjectProperty.getValue(), lIdx) == false)
				{
					indexObjectProperty.setValue(lIdx);
				}
			}
		});
		
		// react to changes of the index
		this.indexObjectProperty.addListener(new ChangeListener<Integer>()
		{
			@Override
			public void changed(ObservableValue<? extends Integer> property, Integer oldIndex, Integer newIndex)
			{
				// get the value of the new index
				T lValue = newIndex < 0 ? null : getItems().get(newIndex);
				
				// set the value
				if (ListSpinner.equals(valueObjectProperty.getValue(), lValue) == false)
				{
					valueObjectProperty.setValue(lValue);
				}
			}
		});
		
		// react to changes of the items
		this.itemsObjectProperty.addListener(new ChangeListener<ObservableList<T>>()
		{
			@Override
			public void changed(ObservableValue<? extends ObservableList<T>> property, ObservableList<T> oldList, ObservableList<T> newList)
			{
				if (oldList != null) oldList.removeListener(listChangeListener);
				if (newList != null) newList.addListener(listChangeListener);
			}
		});
	}
	
	/*
	 * react to observable list changes
	 * TODO: what is sticky, index or value? Now: index
	 */
	private ListChangeListener<T> listChangeListener = new ListChangeListener<T>()
	{
		@Override
		public void onChanged(javafx.collections.ListChangeListener.Change<? extends T> change)
		{
			// get current index
			int lIndex = getIndex();
			
			// is it still valid?
			if (lIndex >= getItems().size()) 
			{
				lIndex = getItems().size() - 1;
				setIndex(lIndex);
				return;
			}
			
			// (re)set the value of the index
			valueObjectProperty.setValue( getItems().get(lIndex) );
		}
	};

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override public String getUserAgentStylesheet()
	{
		return ListSpinner.class.getResource("/jfxtras/internal/scene/control/" + ListSpinner.class.getSimpleName() + ".css").toExternalForm();
	}

	@Override public Skin<?> createDefaultSkin() {
		return new jfxtras.internal.scene.control.skin.ListSpinnerSkin(this); 
	}

	// ==================================================================================================================
	// PROPERTIES

	/** Id */
	public ListSpinner<T> withId(String value) { setId(value); return this; }

	/** Value: the currently show value of the list. */
	public ObjectProperty<T> valueProperty() { return this.valueObjectProperty; }
	final private ObjectProperty<T> valueObjectProperty = new SimpleObjectProperty<T>(this, "value", null)
	{
		public void set(T value)
		{
			if (getItems().indexOf(value) < 0) throw new IllegalArgumentException("Value does not exist in the list: " + value); 
			super.set(value);
		}
	};
	// java bean API
	public T getValue() { return this.valueObjectProperty.getValue(); }
	public void setValue(T value) { this.valueObjectProperty.setValue(value); }
	public ListSpinner<T> withValue(T value) { setValue(value); return this; }
	
	/** Index: the currently show index in the list. */
	public ObjectProperty<Integer> indexProperty() { return this.indexObjectProperty; }
	final private ObjectProperty<Integer> indexObjectProperty = new SimpleObjectProperty<Integer>(this, "index", null)
	{
		public void set(Integer value)
		{
			if (value == null) throw new NullPointerException("Null not allowed as the value for index");
			if (value >= getItems().size()) throw new IllegalArgumentException("Index out of bounds: " + value + ", valid values are 0-" + (getItems().size() - 1)); 
			super.set(value);
		}
	};
	public Integer getIndex() { return this.indexObjectProperty.getValue(); }
	public void setIndex(Integer value) { this.indexObjectProperty.setValue(value); }
	public ListSpinner<T> withIndex(Integer value) { setIndex(value); return this; }
	
	/** Cyclic: what happens at the beginning or end of the list, stop or cycle to the other end. */
	public ObjectProperty<Boolean> cyclicProperty() { return this.cyclicObjectProperty; }
	final private ObjectProperty<Boolean> cyclicObjectProperty = new SimpleObjectProperty<Boolean>(this, "cyclic", false)
	{
		public void set(Boolean value)
		{
			if (value == null) throw new NullPointerException("Null not allowed as the value for cyclic");
			super.set(value);
		}
	};
	public Boolean isCyclic() { return this.cyclicObjectProperty.getValue(); }
	public void setCyclic(Boolean value) { this.cyclicObjectProperty.setValue(value); }
	public ListSpinner<T> withCyclic(Boolean value) { setCyclic(value); return this; }

	/** Editable: is the listspinner editable. It allows the user to type a value instead of only navigating to it, and if the AddCallback is defined, possibly also adding values. */
	public ObjectProperty<Boolean> editableProperty() { return this.editableObjectProperty; }
	final private ObjectProperty<Boolean> editableObjectProperty = new SimpleObjectProperty<Boolean>(this, "editable", false)
	{
		public void set(Boolean value)
		{
			if (value == null) throw new NullPointerException("Null not allowed as the value for editable");
			super.set(value);
		}
	};
	public Boolean isEditable() { return this.editableObjectProperty.getValue(); }
	public void setEditable(Boolean value) { this.editableObjectProperty.setValue(value); }
	public ListSpinner<T> withEditable(Boolean value) { setEditable(value); return this; }

	/** Postfix: a string to be placed after the value, this can for example be a unit like "kg" */
	public ObjectProperty<String> postfixProperty() { return this.postfixObjectProperty; }
	final private ObjectProperty<String> postfixObjectProperty = new SimpleObjectProperty<String>(this, "postfix", "");
	public String getPostfix() { return this.postfixObjectProperty.getValue(); }
	public void setPostfix(String value) { this.postfixObjectProperty.setValue(value); }
	public ListSpinner<T> withPostfix(String value) { setPostfix(value); return this; }

	/** Prefix: a string to be placed before the list value, this can for example be a currency */
	public ObjectProperty<String> prefixProperty() { return this.prefixObjectProperty; }
	final private ObjectProperty<String> prefixObjectProperty = new SimpleObjectProperty<String>(this, "prefix", "");
	public String getPrefix() { return this.prefixObjectProperty.getValue(); }
	public void setPrefix(String value) { this.prefixObjectProperty.setValue(value); }
	public ListSpinner<T> withPrefix(String value) { setPrefix(value); return this; }

	/** Items: the list. */
	public ObjectProperty<ObservableList<T>> itemsProperty() { return this.itemsObjectProperty; }
	final private ObjectProperty<ObservableList<T>> itemsObjectProperty = new SimpleObjectProperty<ObservableList<T>>(this, "items", null)
	{
		public void set(ObservableList<T> value)
		{
			if (value == null) throw new NullPointerException("Null not allowed as the value for items");
			super.set(value);
		}
	};
	public ObservableList<T> getItems() { return this.itemsObjectProperty.getValue(); }
	public void setItems(ObservableList<T> value) { this.itemsObjectProperty.setValue(value); }
	public ListSpinner<T> withItems(ObservableList<T> value) { setItems(value); return this; }

	/** CellFactory: generate the cell to render a value */
	public ObjectProperty<Callback<ListSpinner<T>, Node>> cellFactoryProperty() { return this.cellFactoryObjectProperty; }
	final private ObjectProperty<Callback<ListSpinner<T>, Node>> cellFactoryObjectProperty = new SimpleObjectProperty<Callback<ListSpinner<T>, Node>>(this, "cellFactory", new DefaultCellFactory());
	public Callback<ListSpinner<T>, Node> getCellFactory() { return this.cellFactoryObjectProperty.getValue(); }
	public void setCellFactory(Callback<ListSpinner<T>, Node> value) { this.cellFactoryObjectProperty.setValue(value); }
	public ListSpinner<T> withCellFactory(Callback<ListSpinner<T>, Node> value) { setCellFactory(value); return this; }

	/** StringConverter&lt;T&gt;: convert a value in the list to its string representation and (when in edit mode) vice versa. */
	public ObjectProperty<StringConverter<T>> stringConverterProperty() { return this.stringConverterObjectProperty; }
	final private ObjectProperty<StringConverter<T>> stringConverterObjectProperty = new SimpleObjectProperty<StringConverter<T>>(this, "stringConverter", new DefaultStringConverter());
	public StringConverter<T> getStringConverter() { return this.stringConverterObjectProperty.getValue(); }
	public void setStringConverter(StringConverter<T> value) { this.stringConverterObjectProperty.setValue(value); }
	public ListSpinner<T> withStringConverter(StringConverter<T> value) { setStringConverter(value); return this; }

	/** AddCallback: this callback is called in editable mode when a value is entered that is not found in the list. 
	 *  It is up to the coder to added it to the list or not.
	 *  @return the index where of the position the ListSpinner must show or null (do nothing expect refresh the currently show index)
	 */
	public ObjectProperty<Callback<T, Integer>> addCallbackProperty() { return this.addCallbackObjectProperty; }
	final private ObjectProperty<Callback<T, Integer>> addCallbackObjectProperty = new SimpleObjectProperty<Callback<T, Integer>>(this, "addCallback", null);
	public Callback<T, Integer> getAddCallback() { return this.addCallbackObjectProperty.getValue(); }
	public void setAddCallback(Callback<T, Integer> value) { this.addCallbackObjectProperty.setValue(value); }
	public ListSpinner<T> withAddCallback(Callback<T, Integer> value) { setAddCallback(value); return this; }

	// ==================================================================================================================
	// StringConverter
	
	/**
	 * A string converter that does a simple toString, but cannot convert to an object
	 * @see org.jfxextras.util.StringConverterFactory 
	 */
	class DefaultStringConverter extends StringConverter<T>
	{
		@Override
		public T fromString(String string)
		{
			throw new IllegalStateException("No StringConverter is set. An editable Spinner must have a StringConverter to be able to render and parse the value.");
		}

		@Override
		public String toString(T value)
		{
			return value == null ? "" : value.toString();
		}
	}
	
	// ==================================================================================================================
	// CellFactory
	
	/**
	 * Default cell factory
	 */
	class DefaultCellFactory implements Callback<ListSpinner<T>, Node>
	{
		private Label label = null;
		
		@Override
		public Node call(ListSpinner<T> spinner)
		{
			// get value
			T lValue = spinner.getValue();
			
			// label not yet created
			if (this.label == null) 
			{
				this.label = new Label();
			}
			this.label.setText( lValue == null ? "" : spinner.getPrefix() + getStringConverter().toString(lValue) + spinner.getPostfix() );
			return this.label;
		}
	};
	
	// ==================================================================================================================
	// EVENTS
	
	/** OnCycle: callback for when the list cycles to the other end in cyclic mode (for example to increase a year when a month ListSpinner skips from December to January) */
	public ObjectProperty<EventHandler<CycleEvent>> onCycleProperty() { return iOnCycleObjectProperty; }
	final private ObjectProperty<EventHandler<CycleEvent>> iOnCycleObjectProperty = new SimpleObjectProperty<EventHandler<CycleEvent>>(null);
	// java bean API
	public EventHandler<CycleEvent> getOnCycle() { return iOnCycleObjectProperty.getValue(); }
	public void setOnCycle(EventHandler<CycleEvent> value) { iOnCycleObjectProperty.setValue(value); }
	public ListSpinner<T> withOnCycle(EventHandler<CycleEvent> value) { setOnCycle(value); return this; }
	final static public String ONCYCLE_PROPERTY_ID = "onCycle";
	
	/**
	 * CycleEvent 
	 */
	static public class CycleEvent extends Event
	{
		/**
		 * The only valid EventType for the CycleEvent.
		 */
		public static final EventType<CycleEvent> CYCLE = new EventType<CycleEvent>(Event.ANY, "CYCLE");

		/**
		 * 
		 */
		public CycleEvent()
		{
			super(CYCLE);
		}

		/**
		 * 
		 * @param source
		 * @param target
		 */
		public CycleEvent(Object source, EventTarget target)
		{
			super(source, target, new EventType<CycleEvent>());
		}
		
		public Object getOldIdx() { return this.oldIdx; }
		private Object oldIdx;
		
		public Object getNewIdx() { return this.newIdx; }
		private Object newIdx;
		
		
		public boolean cycledDown() { return cycleDirection == CycleDirection.TOP_TO_BOTTOM; }
		public boolean cycledUp() { return cycleDirection == CycleDirection.BOTTOM_TO_TOP; }
		CycleDirection cycleDirection;
	}
	
	/**
	 * we're cycling, fire the event
	 */
	public void fireCycleEvent(CycleDirection cycleDirection)
	{
		EventHandler<CycleEvent> lCycleEventHandler = getOnCycle();
		if (lCycleEventHandler != null)
		{
			CycleEvent lCycleEvent = new CycleEvent();
			lCycleEvent.cycleDirection = cycleDirection;
			lCycleEventHandler.handle(lCycleEvent);
		}
	}
	static public enum CycleDirection { TOP_TO_BOTTOM, BOTTOM_TO_TOP }
	
	
	// ==================================================================================================================
	// BEHAVIOR

	/**
	 * 
	 */
	public void first()
	{
		// nothing to do
		if (getItems() == null || getItems().size() == 0) return;
		
		// set the new index (this will update the value)
		indexObjectProperty.setValue(0);
	}
	
	/**
	 * 
	 */
	public void decrement()
	{
		// nothing to do
		if (getItems() == null || getItems().size() == 0) return;
		
		// get the current index
		int lOldIdx = this.indexObjectProperty.getValue();
					
		// get the previous index (usually current - 1)
		int lIdx = lOldIdx - 1;
		
		// if end
		if (lIdx < 0)
		{
			// if we're not cyclic
			if (isCyclic() != null && isCyclic().booleanValue() == false)
			{
				// do nothing
				return;
			}
			
			// cycle to the other end: get the last value
			lIdx = getItems().size() - 1;
			
			// notify listener that we've cycled
			fireCycleEvent(CycleDirection.BOTTOM_TO_TOP);
		}

		// set the new index (this will update the value)
		indexObjectProperty.setValue(lIdx);
	}
	
	/**
	 * 
	 */
	public void increment()
	{
		// nothing to do
		if (getItems() == null || getItems().size() == 0) return;
		
		// get the current index
		int lOldIdx = this.indexObjectProperty.getValue();
		
		// get the next index (usually current + 1)
		int lIdx = lOldIdx + 1;
		
		// if null is return, there is no next index (usually current + 1)
		if (lIdx >= getItems().size())
		{
			// if we're not cyclic
			if (isCyclic() != null && isCyclic().booleanValue() == false)
			{
				// do nothing
				return;
			}
			
			// cycle to the other end: get the first value
			lIdx = 0;
			
			// notify listener that we've cycled
			fireCycleEvent(CycleDirection.TOP_TO_BOTTOM);
		}
		
		// set the new index (this will update the value)
		indexObjectProperty.setValue(lIdx);
	}

	/**
	 * Get the last index; if the data provide is endless, this method mail fail!
	 */
	public void last()
	{
		// nothing to do
		if (getItems() == null || getItems().size() == 0) return;
		
		// set the new index (this will update the value)
		indexObjectProperty.setValue(getItems().size() - 1);
	}

	/**
	 * Does a o1.equals(o2) but also checks if o1 or o2 are null.
	 * @param o1
	 * @param o2
	 * @return True if the two values are equal, false otherwise.
	 */
	static public boolean equals(Object o1, Object o2)
	{
		if ( o1 == null && o2 == null ) return true;
		if ( o1 != null && o2 == null ) return false;
		if ( o1 == null && o2 != null ) return false;
		// TODO: compare arrays if (o1.getClass().isArray() && o2.getClass().isArray()) return Arrays.equals( (Object[])o1, (Object[])o2 );		
		return o1.equals(o2);
	}

}
