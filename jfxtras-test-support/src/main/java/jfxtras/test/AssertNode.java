package jfxtras.test;

import java.util.Arrays;
import java.util.List;

import javafx.scene.Node;

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
	
	public AssertNode assertClass(Class clazz) {
		Assert.assertEquals(description, clazz, node.getClass());
		return this;
	}
	
	public AssertNode assertClassName(String className) {
		Assert.assertEquals(description, className, node.getClass().getName());
		return this;
	}
	
	static private double width(Node n) {
		return n.getLayoutBounds().getWidth() + n.getLayoutBounds().getMinX();
	}

	static private double height(Node n) {
		return n.getLayoutBounds().getHeight() + n.getLayoutBounds().getMinY();
	}
	
	static public enum A {XYWH, CLASS, CLASSNAME}
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
			for (A a : lAsserts) {
				if (a == A.XYWH) {
					System.out.print(lNewline + ".assertXYWH(" + lNode.getLayoutX() + ", " + lNode.getLayoutY() + ", " + width(lNode) + ", " + height(lNode) + ", 0.01)");
				}
				if (a == A.CLASS) {
					System.out.print(lNewline + ".assertClass(" + lNode.getClass().getName() + ".class)");
				}
				if (a == A.CLASSNAME) {
					System.out.print(lNewline + ".assertClassName(\"" + lNode.getClass().getName() + "\")");
				}
			}
			System.out.println(";");
			
			idx++;
		}
	}
}
