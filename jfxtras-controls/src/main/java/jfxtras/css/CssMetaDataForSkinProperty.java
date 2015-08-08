/**
 * CssMetaDataForSkinProperty.java
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
