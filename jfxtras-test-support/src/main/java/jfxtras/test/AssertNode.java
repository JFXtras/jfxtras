/**
 * AssertNode.java
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

package jfxtras.test;

import java.util.Arrays;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;

import org.junit.Assert;

/**
 * A utility class to easily assert X, Y, Width, Height and node's class name (incuding the node's sequence / index) in a test.
 * Use generateSource() to generate the assert source code if you are sure the test is correct.
 * Copy that source into the test.
 * Call NodeXYWHAssert.assertNodes() with the list that is defined in the source.
 * 
 * @author user
 *
 */
public class AssertNode {
	
	public AssertNode(Node node) {
		this.node = node;
		this.description = "node=" + this.node;
	}
	final Node node;
	final String description;
	
	
	public AssertNode assertXYWH(double x, double y, double w, double h, double accuracy) {
		Assert.assertEquals(description + ", X", x, node.getLayoutX(), accuracy);
		Assert.assertEquals(description + ", Y", y, node.getLayoutY(), accuracy);
		Assert.assertEquals(description + ", W", w, width(node), accuracy);
		Assert.assertEquals(description + ", H", h, height(node), accuracy);
		return this;
	}
	
	public AssertNode assertRotate(double x, double y, double angle, double accuracy) {
		Rotate r = null;
		for (Transform t : node.getTransforms()) {
			if (t instanceof Rotate) {
				r = (Rotate)t;
				break;
			}
		}
		Assert.assertEquals(description + ", PivotX", x, r.getPivotX(), accuracy);
		Assert.assertEquals(description + ", PivotY", y, r.getPivotY(), accuracy);
		Assert.assertEquals(description + ", Angle", angle, r.getAngle(), accuracy);
		return this;
	}
	
	public AssertNode assertScale(double x, double y, double scaleX, double scaleY, double accuracy) {
		Scale s = null;
		for (Transform t : node.getTransforms()) {
			if (t instanceof Scale) {
				s = (Scale)t;
				break;
			}
		}
		Assert.assertEquals(description + ", PivotX", x, s.getPivotX(), accuracy);
		Assert.assertEquals(description + ", PivotY", y, s.getPivotY(), accuracy);
		Assert.assertEquals(description + ", X", scaleX, s.getX(), accuracy);
		Assert.assertEquals(description + ", Y", scaleY, s.getY(), accuracy);
		return this;
	}
	
	public AssertNode assertArcCenterRadiusAngleLength(double x, double y, double radiusX, double radiusY, double startAngle, double length, double accuracy) {
		Arc arc = (Arc)node;
		Assert.assertEquals(description + ", CenterX", x, arc.getCenterX(), accuracy);
		Assert.assertEquals(description + ", CenterY", y, arc.getCenterY(), accuracy);
		Assert.assertEquals(description + ", RadiusX", radiusX, arc.getRadiusX(), accuracy);
		Assert.assertEquals(description + ", RadiusY", radiusY, arc.getRadiusY(), accuracy);
		Assert.assertEquals(description + ", StartAngle", startAngle, arc.getStartAngle(), accuracy);
		Assert.assertEquals(description + ", Length", length, arc.getLength(), accuracy);
		return this;
	}
	
	public AssertNode assertClass(Class clazz) {
		Assert.assertEquals(description, clazz, node.getClass());
		return this;
	}
	
	public AssertNode assertClassName(String className) {
		Assert.assertEquals(description, className, node.getClass().getName());
		return this;
	}
	
	public AssertNode assertTextText(String text) {
		Assert.assertEquals(description, text, ((javafx.scene.text.Text)node).getText());
		return this;
	}
	
	static private double width(Node n) {
		return n.getLayoutBounds().getWidth() + n.getLayoutBounds().getMinX();
	}

	static private double height(Node n) {
		return n.getLayoutBounds().getHeight() + n.getLayoutBounds().getMinY();
	}
	
	static public enum A {XYWH, ROTATE, SCALE, ARC, CLASS, CLASSNAME, TEXTTEXT}
	static public void generateSource(String paneVariableName, List<Node> nodes, List<String> excludedNodeClasses, boolean newline, A... asserts) {
		
		// init
		int idx = 0;
		String lNewline = (newline ? "\n    " : "");
		// if no asserts are specified, use the default
		List<A> lAsserts = Arrays.asList( (asserts != null && asserts.length > 0 ? asserts : new A[]{A.XYWH, A.CLASS}) );
		
		// for all nodes
		for (Node lNode : nodes) {
			
			// skip node because of class?
			if (excludedNodeClasses != null && excludedNodeClasses.contains(lNode.getClass().getName())) {
				continue;
			}
			
			// output an assertline
			System.out.print("new AssertNode(" + paneVariableName + ".getChildren().get(" + idx + "))");
			generateAsserts(lNode, lNewline, lAsserts);
			System.out.println(";");
			
			idx++;
		}
	}
	
	static public void generateSource(String variableName, Node node, List<String> excludedNodeClasses, boolean newline, A... asserts) {
		
		// init
		String lNewline = (newline ? "\n    " : "");
		// if no asserts are specified, use the default
		List<A> lAsserts = Arrays.asList( (asserts != null && asserts.length > 0 ? asserts : new A[]{A.XYWH, A.CLASS}) );
		
		// output an assertline
		System.out.print("new AssertNode(" + variableName + ")");
		generateAsserts(node, lNewline, lAsserts);
		System.out.println(";");
	}

	private static void generateAsserts(Node node, String newline, List<A> asserts) {
		for (A a : asserts) {
			if (a == A.XYWH) {
				System.out.print(newline + ".assertXYWH(" + node.getLayoutX() + ", " + node.getLayoutY() + ", " + width(node) + ", " + height(node) + ", 0.01)");
			}
			if (a == A.ROTATE) {
				Rotate r = null;
				for (Transform t : node.getTransforms()) {
					if (t instanceof Rotate) {
						r = (Rotate)t;
						break;
					}
				}
				System.out.print(newline + ".assertRotate(" + r.getPivotX() + ", " + r.getPivotY() + ", " + r.getAngle() + ", 0.01)");
			}
			if (a == A.SCALE) {
				Scale s = null;
				for (Transform t : node.getTransforms()) {
					if (t instanceof Scale) {
						s = (Scale)t;
						break;
					}
				}
				System.out.print(newline + ".assertScale(" + s.getPivotX() + ", " + s.getPivotY() + ", " + s.getX() + ", " + s.getY() + ", 0.01)");
			}
			if (a == A.ARC) {
				Arc arc = (Arc)node;
				System.out.print(newline + ".assertArcCenterRadiusAngleLength(" + arc.getCenterX() + ", " + arc.getCenterY() + ", " + arc.getRadiusX() + ", " + arc.getRadiusY() + ", " + arc.getStartAngle() + ", " + arc.getLength() + ", 0.01)");
			}
			if (a == A.CLASS) {
				System.out.print(newline + ".assertClass(" + node.getClass().getName() + ".class)");
			}
			if (a == A.CLASSNAME) {
				System.out.print(newline + ".assertClassName(\"" + node.getClass().getName() + "\")");
			}
			if (a == A.TEXTTEXT) {
				System.out.print(newline + ".assertTextText(\"" + ((javafx.scene.text.Text)node).getText() + "\")");
			}
		}
	}
}
