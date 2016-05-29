/**
 * SimpleMetroArcGaugeTest.java
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

package jfxtras.scene.control.gauge.linear;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import jfxtras.internal.scene.control.gauge.linear.skin.AbstractLinearGaugeSkin;
import jfxtras.internal.scene.control.gauge.linear.skin.SimpleMetroArcGaugeSkin;
import jfxtras.test.TestUtil;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.scene.control.gauge.linear.elements.Indicator;
import jfxtras.scene.control.gauge.linear.elements.PercentMarker;
import jfxtras.scene.control.gauge.linear.elements.PercentSegment;
import jfxtras.test.AssertNode;
import jfxtras.test.AssertNode.A;
import jfxtras.test.JFXtrasGuiTest;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Tom Eugelink
 */
public class SimpleMetroArcGaugeTest extends JFXtrasGuiTest {
	
	@Override
	protected Parent getRootNode() {
		// use a pane to force the scene large enough, the gauge is placed top-left
		Pane lPane = new Pane();
		lPane.setMinSize(600, 600);
		
		// add a small label bottom left for debugging
		label = new Label();
		label.setLayoutY(lPane.getMinHeight() - 20);
		lPane.getChildren().add(label);		
		
		// add the circularePane (this is what we want to test)
		simpleMetroArcGauge = new SimpleMetroArcGauge();
		lPane.getChildren().add(simpleMetroArcGauge);		
		simpleMetroArcGauge.setStyle("-fx-border-color:black;");
		return lPane;
	}
	private SimpleMetroArcGauge simpleMetroArcGauge = null;
	private Label label = null;

	@Test
	public void defaultRendering() {
		setLabel("defaultRendering");
		
		// default properties
		Assert.assertEquals(0.0, simpleMetroArcGauge.getMinValue(), 0.01);
		Assert.assertEquals(0.0, simpleMetroArcGauge.getValue(), 0.01);
		Assert.assertEquals(100.0, simpleMetroArcGauge.getMaxValue(), 0.01);
		
		// default skin properties
		Assert.assertEquals(AbstractLinearGaugeSkin.Animated.YES, ((SimpleMetroArcGaugeSkin)simpleMetroArcGauge.getSkin()).getAnimated());
		Assert.assertEquals("0", ((SimpleMetroArcGaugeSkin)simpleMetroArcGauge.getSkin()).getValueFormat());
		
		// size
		assertWH(simpleMetroArcGauge, 200.0, 200.0);
		
		// assert the segments
		assertFind(".segment0");
		assertNotFind(".segment1");
		generateSource(".segment0");
		new AssertNode(find(".segment0")).assertXYWH(0.0, 0.0, 194.05002117156982, 176.61049842834473, 0.01).assertClass(javafx.scene.shape.Arc.class);
		
		// assert value text
		generateValueSource(".value");
		new AssertNode(find(".value")).assertXYWH(0.0, 67.0625, 36.375, 17.34375, 0.01).assertTextText("0");
		
		// assert the needle
		generateNeedleSource(".needle");
		new AssertNode(find(".needle")).assertXYWH(0.0, 0.0, 151.4755096435547, 172.77810668945312, 0.01).assertRotate(99.0, 108.9, 0.0, 0.01).assertClass(javafx.scene.shape.Path.class);
	}

	@Test
	public void valueRendering() {
		setLabel("valueRendering");
		
		// disable animation so we won't have any timing issues in this test
		TestUtil.runThenWaitForPaintPulse( () -> {
			simpleMetroArcGauge.setStyle("-fxx-animated:NO; " + simpleMetroArcGauge.getStyle());
		});
		Assert.assertEquals(AbstractLinearGaugeSkin.Animated.NO, ((SimpleMetroArcGaugeSkin)simpleMetroArcGauge.getSkin()).getAnimated());
		
		// set the value 
		TestUtil.runThenWaitForPaintPulse( () -> {
			simpleMetroArcGauge.setValue(45.0);
		});
		
		// assert values
		Assert.assertEquals(0.0, simpleMetroArcGauge.getMinValue(), 0.01);
		Assert.assertEquals(45.0, simpleMetroArcGauge.getValue(), 0.01);
		Assert.assertEquals(100.0, simpleMetroArcGauge.getMaxValue(), 0.01);

		// assert value text
		generateValueSource(".value");
		new AssertNode(find(".value")).assertXYWH(0.0, 67.0625, 72.75, 17.34375, 0.01).assertTextText("45");
		
		// assert the needle
		generateNeedleSource(".needle");
		new AssertNode(find(".needle")).assertXYWH(0.0, 0.0, 151.4755096435547, 172.77810668945312, 0.01).assertRotate(99.0, 108.9, 121.5, 0.01);
	}

	@Test
	public void formattedValue() {
		setLabel("valueRendering");
		
		// disable animation so we won't have any timing issues in this test
		TestUtil.runThenWaitForPaintPulse( () -> {
			simpleMetroArcGauge.setStyle("-fxx-animated:NO; -fxx-value-format:' ##0.0W';" + simpleMetroArcGauge.getStyle());
		});
		Assert.assertEquals(AbstractLinearGaugeSkin.Animated.NO, ((SimpleMetroArcGaugeSkin)simpleMetroArcGauge.getSkin()).getAnimated());
		
		// assert value text
		generateValueSource(".value");
		new AssertNode(find(".value")).assertXYWH(-63.0, 67.0625, 163.1875, 17.34375, 0.01).assertTextText(" 0.0W");
		
		// set the value 
		TestUtil.runThenWaitForPaintPulse( () -> {
			simpleMetroArcGauge.setValue(45.0);
		});
		
		// assert value text
		generateValueSource(".value");
		new AssertNode(find(".value")).assertXYWH(-63.0, 67.0625, 199.5625, 17.34375, 0.01).assertTextText(" 45.0W");
	}

	@Test
	public void withIndicators() {
		setLabel("withIndicators");
		TestUtil.runThenWaitForPaintPulse( () -> {
			simpleMetroArcGauge.indicators().add(new Indicator(0, "warning"));
			simpleMetroArcGauge.indicators().add(new Indicator(1, "error"));
			simpleMetroArcGauge.setStyle("-fxx-warning-indicator-visibility: visible; -fxx-error-indicator-visibility: visible;" + simpleMetroArcGauge.getStyle());
		});

		// assert the indicators visible
		generateSource(".warning-indicator");
		new AssertNode(find(".warning-indicator")).assertXYWH(70.785, 174.73499999999999, 0.0, 0.0, 0.01); // TBEERNOT: why are width and height 0?
		generateSource(".error-indicator");
		new AssertNode(find(".error-indicator")).assertXYWH(127.215, 174.73499999999999, 0.0, 0.0, 0.01); // TBEERNOT: why are width and height 0?
	}
	
	@Test
	public void fourEvenSegments() {
		setLabel("fourEvenSegments");
		TestUtil.runThenWaitForPaintPulse( () -> {
			for (int i = 0; i < 4; i++) {
				simpleMetroArcGauge.segments().add(new PercentSegment(simpleMetroArcGauge, i * 25.0, (i+1) * 25.0, "segment-" + i));
			}
		});
		
		assertWH(simpleMetroArcGauge, 200.0, 200.0);
		
		// assert the segments
		for (int i = 0; i < 4; i++) { generateSegmentSource(".segment" + i); }
		new AssertNode(find(".segment0")).assertXYWH(0.0, 0.0, 100.38267517089844, 176.61048889160156, 0.01).assertArcCenterRadiusAngleLength(99.0, 108.9, 94.05, 94.05, -135.0, -67.5, 0.01);
		new AssertNode(find(".segment1")).assertXYWH(0.0, 0.0, 100.0, 110.1483097076416, 0.01).assertArcCenterRadiusAngleLength(99.0, 108.9, 94.05, 94.05, -202.5, -67.5, 0.01);
		new AssertNode(find(".segment2")).assertXYWH(0.0, 0.0, 187.04415893554688, 110.14830207824707, 0.01).assertArcCenterRadiusAngleLength(99.0, 108.9, 94.05, 94.05, -270.0, -67.5, 0.01);
		new AssertNode(find(".segment3")).assertXYWH(0.0, 0.0, 194.05274963378906, 176.61048889160156, 0.01).assertArcCenterRadiusAngleLength(99.0, 108.9, 94.05, 94.05, -337.5, -67.5, 0.01);	
		assertNotFind(".segment4");
	}
	
	@Test
	public void fourUnevenSegments() {
		setLabel("fourUnevenSegments");
		TestUtil.runThenWaitForPaintPulse( () -> {
			simpleMetroArcGauge.segments().add(new PercentSegment(simpleMetroArcGauge, 10.0, 15.0));
			simpleMetroArcGauge.segments().add(new PercentSegment(simpleMetroArcGauge, 20.0, 30.0));
			simpleMetroArcGauge.segments().add(new PercentSegment(simpleMetroArcGauge, 30.0, 50.0));
			simpleMetroArcGauge.segments().add(new PercentSegment(simpleMetroArcGauge, 50.0, 90.0));
		});

		assertWH(simpleMetroArcGauge, 200.0, 200.0);
		
		// assert the segments
		for (int i = 0; i < 4; i++) { generateSegmentSource(".segment" + i); }
		new AssertNode(find(".segment0")).assertXYWH(0.0, 0.0, 103.67220687866211, 139.0930938720703, 0.01).assertArcCenterRadiusAngleLength(99.0, 108.9, 94.05, 94.05, -162.0, -13.5, 0.01);
		new AssertNode(find(".segment1")).assertXYWH(0.0, 0.0, 101.47879028320312, 110.21964263916016, 0.01).assertArcCenterRadiusAngleLength(99.0, 108.9, 94.05, 94.05, -189.0, -27.0, 0.01);
		new AssertNode(find(".segment2")).assertXYWH(0.0, 0.0, 100.0, 110.38131141662598, 0.01).assertArcCenterRadiusAngleLength(99.0, 108.9, 94.05, 94.05, -216.0, -54.0, 0.01);
		new AssertNode(find(".segment3")).assertXYWH(0.0, 0.0, 194.0507049560547, 139.0930938720703, 0.01).assertArcCenterRadiusAngleLength(99.0, 108.9, 94.05, 94.05, -270.0, -108.0, 0.01);
		assertNotFind(".segment4");
	}

	@Test
	public void fourMarkers() {
		setLabel("fourUnevenSegments");
		TestUtil.runThenWaitForPaintPulse( () -> {
			simpleMetroArcGauge.markers().add(new PercentMarker(simpleMetroArcGauge, 0.0));
			simpleMetroArcGauge.markers().add(new PercentMarker(simpleMetroArcGauge, 45.0));
			simpleMetroArcGauge.markers().add(new PercentMarker(simpleMetroArcGauge, 50.0));
			simpleMetroArcGauge.markers().add(new PercentMarker(simpleMetroArcGauge, 100.0));
		});

		assertWH(simpleMetroArcGauge, 200.0, 200.0);
		
		// assert the segments
		for (int i = 0; i < 4; i++) { generateMarkerSource(".marker" + i); }
		new AssertNode(find(".marker0")).assertXYWH(32.496607229405214, 175.4033927705948, 0.0, 0.0, 0.01).assertRotate(0.0, 0.0, -135.0, 0.01).assertScale(0.0, 0.0, 0.66, 0.66, 0.01);  // TBEERNOT: why are width and height 0?
		new AssertNode(find(".marker1")).assertXYWH(77.04446352935213, 17.448608986598515, 0.0, 0.0, 0.01).assertRotate(0.0, 0.0, -13.5, 0.01).assertScale(0.0, 0.0, 0.66, 0.66, 0.01);
		new AssertNode(find(".marker2")).assertXYWH(98.99999999999999, 14.850000000000009, 0.0, 0.0, 0.01).assertRotate(0.0, 0.0, 0.0, 0.01).assertScale(0.0, 0.0, 0.66, 0.66, 0.01);
		new AssertNode(find(".marker3")).assertXYWH(165.50339277059481, 175.4033927705948, 0.0, 0.0, 0.01).assertRotate(0.0, 0.0, 135.0, 0.01).assertScale(0.0, 0.0, 0.66, 0.66, 0.01);
		assertNotFind(".marker4");
	}

	// LABELS?
	
	// =============================================================================================================================================================================================================================
	// SUPPORT

	List<String> EXCLUDED_CLASSES = java.util.Arrays.asList();
	
	private void assertWH(SimpleMetroArcGauge pane, double w, double h) {
		Assert.assertEquals(w, pane.getWidth(), 0.01);
		Assert.assertEquals(h, pane.getHeight(), 0.01);
	}
	
	private void setLabel(String s) {
		TestUtil.runThenWaitForPaintPulse( () -> {
			label.setText(s);
		});
	}

	private void generateSource(String classFindExpression) {
		Node node = find(classFindExpression);
		AssertNode.generateSource("find(\"" + classFindExpression + "\")", node, EXCLUDED_CLASSES, false, A.XYWH);
	}

	private void generateValueSource(String classFindExpression) {
		Node node = find(classFindExpression);
		AssertNode.generateSource("find(\"" + classFindExpression + "\")", node, EXCLUDED_CLASSES, false, A.XYWH, A.TEXTTEXT);
	}

	private void generateNeedleSource(String classFindExpression) {
		Node node = find(classFindExpression);
		AssertNode.generateSource("find(\"" + classFindExpression + "\")", node, EXCLUDED_CLASSES, false, A.XYWH, A.ROTATE);
	}

	private void generateSegmentSource(String classFindExpression) {
		Node node = find(classFindExpression);
		AssertNode.generateSource("find(\"" + classFindExpression + "\")", node, EXCLUDED_CLASSES, false, A.XYWH, A.ARC);
	}

	private void generateMarkerSource(String classFindExpression) {
		Node node = find(classFindExpression);
		AssertNode.generateSource("find(\"" + classFindExpression + "\")", node, EXCLUDED_CLASSES, false, A.XYWH, A.ROTATE, A.SCALE);
	}
}