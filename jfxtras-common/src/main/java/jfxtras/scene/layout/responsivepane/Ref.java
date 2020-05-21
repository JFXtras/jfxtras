/**
 * Copyright (c) 2011-2020, JFXtras
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
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.scene.layout.responsivepane;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * This class represents a placeholder for nodes in the refs list
 */
public class Ref extends StackPane {
	
	public Ref() {
		construct();
	}
	
	public Ref(String to) { 
		setTo(to);
		construct();
	}
	
	public Ref(String to, String id) { 
		setTo(to);
		setId(id);
		construct();
	}
	
	private void construct() {
		// when the scene changes to not null, pull the referred node  
		sceneProperty().addListener( (observable) -> {
			getChildren().clear();
			if (sceneProperty().get() != null) {
				pullRef();
			}
		});
	}

	/** To */
	public ObjectProperty<String> toProperty() { return toProperty; }
	final private SimpleObjectProperty<String> toProperty = new SimpleObjectProperty<>(this, "to", null);
	public String getTo() { return toProperty.getValue(); }
	public void setTo(String value) { toProperty.setValue(value); }
	public Ref withTo(String value) { setTo(value); return this; } 

	
	void pullRef() {
		
		// find my containing ResponsiveLayout
		if (lResponsivePane == null) {
			Node parent = this.getParent();
			while (parent != null && !(parent instanceof ResponsivePane)) {
				parent = parent.getParent();
			}
			lResponsivePane = (ResponsivePane)parent;
		}
		
		// find the referred to node
		String lRefTo = getTo();
		Node lReferredNode = lResponsivePane.findResuableNode(lRefTo);
		if (lResponsivePane.getTrace()) System.out.println("Ref " + getId() + " referring to " + lRefTo + " becomes " + lReferredNode);
		
		// pull the referred node into this ref
		getChildren().clear();
		if (lReferredNode != null) {
			getChildren().add(lReferredNode);
		}
		
		// show debug information
		if (!lResponsivePane.getDebug() && !lResponsivePane.getTrace()) {
			setStyle(null);
		}
		else {
			// draw a border around the reference
			this.setStyle("-fx-border-color: red; -fx-border-width: 1; -fx-border-style: dashed;");
			
			// and an ID in the top left
			Text label = new Text((getId() == null ? "" : getId() + "->") + getTo());
			label.setStyle("-fx-fill:RED; -fx-effect: dropshadow(gaussian, WHITE, 3,1.0, 0,0);");
			label.setManaged(false);
			label.setLayoutX(3.0);
			label.setLayoutY(label.prefHeight(0));
			label.setMouseTransparent(true);
			getChildren().add(label);
			// never push outside of the ref
			Rectangle lClip = new Rectangle(0,-label.prefHeight(0),0,0);
			lClip.widthProperty().bind(this.widthProperty().subtract(label.layoutXProperty())); 
			lClip.heightProperty().bind(this.heightProperty());
			label.setClip(lClip); // Not sure why this is not clipping right
		}
	}
	private ResponsivePane lResponsivePane = null;
}