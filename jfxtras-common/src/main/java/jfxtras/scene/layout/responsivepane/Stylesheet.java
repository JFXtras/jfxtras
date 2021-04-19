/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.scene.layout.responsivepane;

import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 */
@DefaultProperty("file")
public class Stylesheet {

	/**
	 * For FXML
	 */
	public Stylesheet() {
	}
	
	/**
	 * 
	 * @param sizeAtLeast size in inches
	 * @param file
	 */
	public Stylesheet(Size sizeAtLeast, String file) {
		setSizeAtLeast(sizeAtLeast);
		setFile(file);
	}		
	
	/** File */
	public ObjectProperty<String> fileProperty() { return fileProperty; }
	final private SimpleObjectProperty<String> fileProperty = new SimpleObjectProperty<>(this, "file", null);
	public String getFile() { return fileProperty.getValue(); }
	public void setFile(String value) { fileProperty.setValue(value); }
	public Stylesheet withFile(String value) { setFile(value); return this; } 

	/** SizeAtLeast (in inches) */
	public ObjectProperty<Size> sizeAtLeastProperty() { return sizeAtLeastProperty; }
	final private SimpleObjectProperty<Size> sizeAtLeastProperty = new SimpleObjectProperty<>(this, "sizeAtLeast", Size.ZERO);
	public Size getSizeAtLeast() { return sizeAtLeastProperty.getValue(); }
	public void setSizeAtLeast(Size value) { sizeAtLeastProperty.setValue(value); }
	public Stylesheet withSizeAtLeast(Size value) { setSizeAtLeast(value); return this; } 
		
	public String describeSizeConstraints() {
		return getSizeAtLeast() + "";
	}
}