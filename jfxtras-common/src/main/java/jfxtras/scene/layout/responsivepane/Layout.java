package jfxtras.scene.layout.responsivepane;

import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

/**
 *
 */
@DefaultProperty("root")
public class Layout {
	
	/**
	 * For FXML
	 */
	public Layout() {
	}
	
	/**
	 * 
	 */
	public Layout(Size sizeAtLeast, Node root) {
		setSizeAtLeast(sizeAtLeast);
		setRoot(root);
	}
	
	/**
	 * 
	 */
	public Layout(Size sizeAtLeast, Orientation orientation, Node root) {
		setSizeAtLeast(sizeAtLeast);
		setOrientation(orientation);
		setRoot(root);
	}
	
	/** Root */
	public ObjectProperty<Node> rootProperty() { return rootProperty; }
	final private SimpleObjectProperty<Node> rootProperty = new SimpleObjectProperty<>(this, "root", null);
	public Node getRoot() { return rootProperty.getValue(); }
	public void setRoot(Node value) { rootProperty.setValue(value); }
	public Layout withRoot(Node value) { setRoot(value); return this; } 

	/** sizeAtLeast */
	public ObjectProperty<Size> sizeAtLeastProperty() { return sizeAtLeastProperty; }
	final private SimpleObjectProperty<Size> sizeAtLeastProperty = new SimpleObjectProperty<>(this, "sizeAtLeast", Size.ZERO);
	public Size getSizeAtLeast() { return sizeAtLeastProperty.getValue(); }
	public void setSizeAtLeast(Size value) { sizeAtLeastProperty.setValue(value); }
	public Layout withSizeAtLeast(Size value) { setSizeAtLeast(value); return this; }

	/** Orientation */
	public ObjectProperty<Orientation> orientationProperty() { return orientationProperty; }
	final private SimpleObjectProperty<Orientation> orientationProperty = new SimpleObjectProperty<>(this, "orientation", null);
	public Orientation getOrientation() { return orientationProperty.getValue(); }
	public void setOrientation(Orientation value) { orientationProperty.setValue(value); }
	public Layout withOrientation(Orientation value) { setOrientation(value); return this; }
	
	public String describeSizeConstraints() {
		return getSizeAtLeast() + (getOrientation() == null ? "" : "-" + getOrientation());
	}
	
	public String toString() {
		return super.toString()
		    + (getRoot() == null || getRoot().getId() == null ? "" : ", root-id=" + getRoot().getId())
		    + (getRoot() == null? "" : ", root=" + getRoot())
		    ;
	}
}