package jfxtras.css;

import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * Creating CssMetaData for a property in a skin has some casting, this class hides that away.
 *
 * @param <S>
 * @param <SK>
 * @param <V>
 */
public abstract class CssMetaDataForSkinProperty<S extends Styleable, SK extends Skin<?>, V> extends CssMetaData<S, V> {

	protected CssMetaDataForSkinProperty(String cssId, StyleConverter<?, V> styleConverter, V initialValue) {
		super(cssId, styleConverter, initialValue);
	}
	
	public V getInitialValue() {
		return super.getInitialValue(null);
	}
	
    @Override public boolean isSettable(S n) {
    	Control c = (Control)n;
    	SK s = (SK)c.getSkin();
    	return !getProperty(s).isBound(); 
    }
    @Override public StyleableProperty<V> getStyleableProperty(S n) { 
    	Control c = (Control)n;
    	SK s = (SK)c.getSkin();
    	return (StyleableProperty<V>)getProperty(s); 
    }
    
    abstract protected javafx.beans.property.Property<V> getProperty(SK s);
}
