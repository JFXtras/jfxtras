/**
 * CircularPaneTest.java
 *
 * Copyright (c) 2011-2016, JFXtras
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

package jfxtras.scene.layout.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import jfxtras.scene.layout.CircularPane;
import jfxtras.test.AssertNode;
import jfxtras.test.AssertNode.A;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

/**
 * TestFX is able to layout in the getRootNode() method a single node per class.
 * This would result in one class with usually one test method per to-be-tested layout, and thus is a LOT of classes.
 * By placing CircularPane in a presized Pane, it is possible to test different layouts in separate methods, all in a single class.
 * The drawback is that CircularPane is never tested stand alone, as the root node, so for each test it must be decided if we can put it as a test in here, or if it needs a test class on its own.
 * 
 * @author Tom Eugelink
 *
 */
public class CircularPaneTest extends JFXtrasGuiTest {

	@Override
	protected Parent getRootNode() {
		// use a pane to force the scene large enough, the circular pane is placed top-left
		Pane lPane = new Pane();
		lPane.setMinSize(600, 600);
		label = new Label();
		label.setLayoutY(lPane.getMinHeight() - 20);
		lPane.getChildren().add(label);		
		
		// add the circularePane (this is what we want to test)
		circularPane = new CircularPane();
		circularPane.setShowDebug(Color.GREEN);
		lPane.getChildren().add(circularPane);		
		circularPane.setStyle("-fx-border-color:black;");
		return lPane;
	}
	private CircularPane circularPane = null;
	private Label label = null;
	
	@Test
	public void singleNode() {
		setLabel("singleNode");
		
		// insert 1 circle
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.getChildren().add(new javafx.scene.shape.Rectangle(30,30));
		});

		//generateSource(circularPane);
		assertWH(circularPane, 42.42640687119285, 42.42640687119285);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(6.2132034355964265, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	@Test
	public void twoNodes() {
		setLabel("twoNodes");

		// insert 2 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			for (int i = 0; i < 2; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		//generateSource(circularPane);
		assertWH(circularPane, 84.8528137423857, 42.42640687119285);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(48.63961030678928, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(6.2132034355964265, 6.21320343559643, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	@Test
	public void twoNodesAt90Degrees() {
		setLabel("twoNodesAt90Degrees");

		// insert 2 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setStartAngle(90.0);
			for (int i = 0; i < 2; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		//generateSource(circularPane);
		assertWH(circularPane, 42.42640687119285, 84.8528137423857);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(6.21320343559643, 48.63961030678928, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(6.2132034355964265, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	@Test
	public void twoNodesAt45Degrees() {
		setLabel("twoNodesAt45Degrees");

		// insert 2 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setStartAngle(45.0);
			for (int i = 0; i < 2; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		//generateSource(circularPane);
		assertWH(circularPane, 72.42640687119285, 72.42640687119285);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(36.21320343559643, 36.21320343559643, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(6.2132034355964265, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	@Test
	public void twoNodesAt30Degrees() {
		setLabel("twoNodesAt30Degrees");

		// insert 2 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setStartAngle(30.0);
			for (int i = 0; i < 2; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		//generateSource(circularPane);
		assertWH(circularPane, 79.16875301294053, 63.63961030678928);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(42.9555495773441, 27.426406871192846, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(6.2132034355964265, 6.213203435596428, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	@Test
	public void twoCirclesAt30Degrees() {
		setLabel("twoCirclesAt30Degrees");

		// insert 2 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setChildrenAreCircular(true);
			circularPane.setStartAngle(30.0);
			for (int i = 0; i < 2; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(15);
				circularPane.getChildren().add(c);
			}
		});

		//generateSource(circularPane);
		assertWH(circularPane, 55.98076211353316, 45.0);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(40.98076211353316, 29.999999999999996, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(15.0, 15.0, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
	}

	@Test
	public void threeNodes() {
		setLabel("threeNodes");

		// insert 2 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setStartAngle(30.0);
			for (int i = 0; i < 3; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		//generateSource(circularPane);
		assertWH(circularPane, 79.16875301294053, 84.85281374238573);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(42.9555495773441, 27.426406871192853, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(6.2132034355964265, 48.63961030678929, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(2)).assertXYWH(6.2132034355964265, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	@Test
	public void eightNodes() {
		setLabel("eightNodes");

		// insert 8 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		//generateSource(circularPane);
		assertWH(circularPane, 144.85281374238568, 144.8528137423857);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(78.63961030678927, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(108.63961030678927, 36.213203435596405, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(2)).assertXYWH(108.63961030678927, 78.63961030678931, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(3)).assertXYWH(78.63961030678932, 108.63961030678928, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(4)).assertXYWH(36.21320343559643, 108.63961030678928, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(5)).assertXYWH(6.2132034355964265, 78.63961030678928, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(6)).assertXYWH(6.2132034355964265, 36.213203435596434, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(7)).assertXYWH(36.21320343559642, 6.213203435596434, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	@Test
	public void eightNodesAnimated() {
		setLabel("eightNodesAnimated");

		circularPane.setOnAnimateInFinished((event) -> {
			// these assertions must be identical to those in eightNodes
			assertWH(circularPane, 153.29, 153.29); 
			new AssertNode(circularPane.getChildren().get(0)).assertXYWH(61.645975386273626, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
			new AssertNode(circularPane.getChildren().get(1)).assertXYWH(100.84286433256493, 22.44908643998233, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
			new AssertNode(circularPane.getChildren().get(2)).assertXYWH(117.07874733695084, 61.645975386273626, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
			new AssertNode(circularPane.getChildren().get(3)).assertXYWH(100.84286433256494, 100.84286433256491, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
			new AssertNode(circularPane.getChildren().get(4)).assertXYWH(61.64597538627363, 117.07874733695084, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
			new AssertNode(circularPane.getChildren().get(5)).assertXYWH(22.449086439982338, 100.84286433256494, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
			new AssertNode(circularPane.getChildren().get(6)).assertXYWH(6.2132034355964265, 61.64597538627363, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
			new AssertNode(circularPane.getChildren().get(7)).assertXYWH(22.44908643998233, 22.449086439982338, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		});
		
		// insert 8 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
			circularPane.setAnimationInterpolation(CircularPane::animateOverTheArc);
		});
	}

	@Test
	public void eightCircles() {
		setLabel("eightCircles");

		// insert 8 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setChildrenAreCircular(true);
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(15);
				circularPane.getChildren().add(c);
			}
		});

		//generateSource(circularPane);
		assertWH(circularPane, 102.42640687119285, 102.42640687119285);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(66.21320343559643, 15.0, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(87.42640687119285, 36.213203435596405, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(2)).assertXYWH(87.42640687119285, 66.21320343559643, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(3)).assertXYWH(66.21320343559645, 87.42640687119285, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(4)).assertXYWH(36.21320343559643, 87.42640687119285, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(5)).assertXYWH(15.0, 66.21320343559643, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(6)).assertXYWH(15.0, 36.21320343559643, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(7)).assertXYWH(36.21320343559642, 15.0, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
	}
	
	@Test
	public void eightNodesGap() {
		setLabel("eightNodesGap");

		// insert 8 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setGap(10.0);
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		//generateSource(circularPane);
		assertWH(circularPane, 168.99494936611666, 168.99494936611666);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(95.71067811865473, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(132.7817459305202, 43.28427124746187, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(2)).assertXYWH(132.78174593052023, 95.71067811865477, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(3)).assertXYWH(95.71067811865478, 132.7817459305202, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(4)).assertXYWH(43.284271247461895, 132.78174593052023, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(5)).assertXYWH(6.2132034355964265, 95.71067811865476, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(6)).assertXYWH(6.2132034355964265, 43.2842712474619, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(7)).assertXYWH(43.28427124746189, 6.213203435596434, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}


	@Test
	public void eightNodesForcedDiameter() {
		setLabel("eightNodesForcedDiameter");

		// insert 8 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setDiameter(120.0);
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		//generateSource(circularPane);
		assertWH(circularPane, 114.09506182625597, 114.09506182625597);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(56.890595352840414, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(77.88185839065953, 27.204466473415533, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(2)).assertXYWH(77.88185839065954, 56.89059535284043, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(3)).assertXYWH(56.89059535284044, 77.88185839065953, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(4)).assertXYWH(27.204466473415554, 77.88185839065954, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(5)).assertXYWH(6.2132034355964265, 56.89059535284042, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(6)).assertXYWH(6.2132034355964265, 27.204466473415557, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(7)).assertXYWH(27.20446647341555, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	@Test
	public void animateFromTheOrigin() {
		setLabel("animateFromTheOrigin");

		// insert 8 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			for (int i = 0; i < 5; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
			circularPane.setAnimationInterpolation(CircularPane::animateFromTheOrigin);
			circularPane.setAnimationDuration(Duration.millis(500));
		});

		// start animation
		circularPane.animateIn();
		
		// wait for the animation to finish
		sleep(1000); 

		//generateSource(circularPane);
		assertWH(circularPane, 111.07377520931499, 107.71393385567751);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(61.75009104025391, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(74.86057177371856, 46.563114153433865, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(2)).assertXYWH(40.53688760465749, 71.50073042008108, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(3)).assertXYWH(6.2132034355964265, 46.563114153433865, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(4)).assertXYWH(19.32368416906106, 6.213203435596434, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}


	@Test
	public void animateOverTheArc() {
		setLabel("animateOverTheArc");

		// insert 8 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			for (int i = 0; i < 5; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
			circularPane.setAnimationInterpolation(CircularPane::animateOverTheArc);
			circularPane.setAnimationDuration(Duration.millis(500));
		});

		// start animation
		circularPane.animateIn();
		
		// wait for the animation to finish
		sleep(1000); 

		//generateSource(circularPane);
		assertWH(circularPane, 111.07377520931499, 107.71393385567751);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(61.75009104025391, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(74.86057177371856, 46.563114153433865, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(2)).assertXYWH(40.53688760465749, 71.50073042008108, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(3)).assertXYWH(6.2132034355964265, 46.563114153433865, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(4)).assertXYWH(19.32368416906106, 6.213203435596434, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	// =============================================================================================================================================================================================================================
	// SUPPORT

	List<String> EXCLUDED_CLASSES = java.util.Arrays.asList("jfxtras.scene.layout.CircularPane$Bead", "jfxtras.scene.layout.CircularPane$Connector");
	
	private void assertWH(CircularPane pane, double w, double h) {
		Assert.assertEquals(w, pane.getWidth(), 0.01);
		Assert.assertEquals(h, pane.getHeight(), 0.01);
	}
	
	private void setLabel(String s) {
		TestUtil.runThenWaitForPaintPulse( () -> {
			label.setText(s);
		});
	}

	private void generateSource(Pane pane) {
		System.out.println("> " + label.getText()); 
		System.out.println("assertWH(circularPane, " + pane.getWidth() + ", " + pane.getHeight() + ");");
		AssertNode.generateSource("circularPane", pane.getChildren(), EXCLUDED_CLASSES, false, A.XYWH, A.CLASS);
		TestUtil.sleep(3000);
	}
}
