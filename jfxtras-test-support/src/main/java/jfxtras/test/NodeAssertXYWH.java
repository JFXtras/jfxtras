package jfxtras.test;

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
public class NodeAssertXYWH {
	
	public NodeAssertXYWH(Node node, double x, double y, double w, double h, Class clazz, double accuracy) {
		this.node = node;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.clazz = clazz;
		this.accuracy = accuracy;
	}
	final Node node;
	final double x;
	final double y;
	final double w;
	final double h;
	final Class clazz;
	final double accuracy;
	
	public void doAssert() {
		String lDescription = "node=" + this.node;
		Assert.assertEquals(lDescription, this.clazz, node.getClass());
		Assert.assertEquals(lDescription, this.x, node.getLayoutX(), accuracy);
		Assert.assertEquals(lDescription, this.y, node.getLayoutY(), accuracy);
		Assert.assertEquals(lDescription, this.w, width(node), accuracy);
		Assert.assertEquals(lDescription, this.h, height(node), accuracy);
	}
	
	static private double width(Node n) {
		return n.getLayoutBounds().getWidth() + n.getLayoutBounds().getMinX();
	}

	static private double height(Node n) {
		return n.getLayoutBounds().getHeight() + n.getLayoutBounds().getMinY();
	}
	
	static public void generateSource(String paneVariableName, List<Node> nodes, List<String> excludes) {
		int idx = 0;
		for (Node lNode : nodes) {
			if (excludes != null && excludes.contains(lNode.getClass().getName())) {
				continue;
			}
			System.out.println("new NodeAssertXYWH(" + paneVariableName + ".getChildren().get(" + idx + "), "+ lNode.getLayoutX() + ", " + lNode.getLayoutY() + ", " + width(lNode) + ", " + height(lNode) + ", " + lNode.getClass().getName() + ".class, 0.01).doAssert();");
			idx++;
		}
		TestUtil.sleep(3000);
	}
	

}
