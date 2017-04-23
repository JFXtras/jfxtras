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
	 * @param sizeInInchesAtLeast size in inches
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