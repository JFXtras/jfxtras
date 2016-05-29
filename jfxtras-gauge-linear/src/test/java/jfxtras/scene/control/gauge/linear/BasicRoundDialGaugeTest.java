/**
 * BasicRoundDialGaugeTest.java
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
import jfxtras.internal.scene.control.gauge.linear.skin.BasicRoundDailGaugeSkin;
import jfxtras.scene.control.gauge.linear.BasicRoundDailGauge;
import jfxtras.scene.control.gauge.linear.elements.Indicator;
import jfxtras.scene.control.gauge.linear.elements.PercentMarker;
import jfxtras.scene.control.gauge.linear.elements.PercentSegment;
import jfxtras.test.TestUtil;
import jfxtras.test.AssertNode;
import jfxtras.test.AssertNode.A;
import jfxtras.test.JFXtrasGuiTest;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Tom Eugelink
 */
public class BasicRoundDialGaugeTest extends JFXtrasGuiTest {
	
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
		BasicRoundDailGauge = new BasicRoundDailGauge();
		lPane.getChildren().add(BasicRoundDailGauge);		
		BasicRoundDailGauge.setStyle("-fx-border-color:black;");
		return lPane;
	}
	private BasicRoundDailGauge BasicRoundDailGauge = null;
	private Label label = null;

	@Test
	public void defaultRendering() {
		setLabel("defaultRendering");
		
		// default properties
		Assert.assertEquals(0.0, BasicRoundDailGauge.getMinValue(), 0.01);
		Assert.assertEquals(0.0, BasicRoundDailGauge.getValue(), 0.01);
		Assert.assertEquals(100.0, BasicRoundDailGauge.getMaxValue(), 0.01);
		
		// default skin properties
		Assert.assertEquals(AbstractLinearGaugeSkin.Animated.YES, ((BasicRoundDailGaugeSkin)BasicRoundDailGauge.getSkin()).getAnimated());
		Assert.assertEquals("0", ((BasicRoundDailGaugeSkin)BasicRoundDailGauge.getSkin()).getValueFormat());
		
		// size
		assertWH(BasicRoundDailGauge, 200.0, 200.0);
		
		// assert value text
		generateValueSource(".value");
		new AssertNode(find(".value")).assertXYWH(0.0, 37.72265625, 20.4609375, 9.755859375, 0.01).assertTextText("0");
		
		// assert the needle
		generateNeedleSource(".needle");
		new AssertNode(find(".needle")).assertXYWH(96.0, 99.0, 6.0, 75.0, 0.01).assertRotate(3.0, 0.0, 45.0, 0.01);
		
		// assert the segments
		assertNotFind(".segment0");
	}

	@Test
	public void valueRendering() {
		setLabel("valueRendering");
		
		// disable animation so we won't have any timing issues in this test
		TestUtil.runThenWaitForPaintPulse( () -> {
			BasicRoundDailGauge.setStyle("-fxx-animated:NO; " + BasicRoundDailGauge.getStyle());
		});
		Assert.assertEquals(AbstractLinearGaugeSkin.Animated.NO, ((BasicRoundDailGaugeSkin)BasicRoundDailGauge.getSkin()).getAnimated());
		
		// set the value 
		TestUtil.runThenWaitForPaintPulse( () -> {
			BasicRoundDailGauge.setValue(45.0);
		});
		
		// assert values
		Assert.assertEquals(0.0, BasicRoundDailGauge.getMinValue(), 0.01);
		Assert.assertEquals(45.0, BasicRoundDailGauge.getValue(), 0.01);
		Assert.assertEquals(100.0, BasicRoundDailGauge.getMaxValue(), 0.01);

		// assert value text
		generateValueSource(".value");
		new AssertNode(find(".value")).assertXYWH(0.0, 37.72265625, 40.921875, 9.755859375, 0.01).assertTextText("45");
		
		// assert the needle
		generateNeedleSource(".needle");
		new AssertNode(find(".needle")).assertXYWH(96.0, 99.0, 6.0, 75.0, 0.01).assertRotate(3.0, 0.0, 166.5, 0.01);
	}

	@Test
	public void formattedValue() {
		setLabel("valueRendering");
		
		// disable animation so we won't have any timing issues in this test
		TestUtil.runThenWaitForPaintPulse( () -> {
			BasicRoundDailGauge.setStyle("-fxx-animated:NO; -fxx-value-format:' ##0.0W';" + BasicRoundDailGauge.getStyle());
		});
		Assert.assertEquals(AbstractLinearGaugeSkin.Animated.NO, ((BasicRoundDailGaugeSkin)BasicRoundDailGauge.getSkin()).getAnimated());
		
		// assert value text
		generateValueSource(".value");
		new AssertNode(find(".value")).assertXYWH(-35.0, 37.72265625, 91.79296875, 9.755859375, 0.01).assertTextText(" 0.0W");
		
		// set the value 
		TestUtil.runThenWaitForPaintPulse( () -> {
			BasicRoundDailGauge.setValue(45.0);
		});
		
		// assert value text
		generateValueSource(".value");
		new AssertNode(find(".value")).assertXYWH(0.0, 37.72265625, 112.25390625, 9.755859375, 0.01).assertTextText(" 45.0W");
	}

	@Test
	public void withIndicators() {
		setLabel("withIndicators");
		TestUtil.runThenWaitForPaintPulse( () -> {
			BasicRoundDailGauge.indicators().add(new Indicator(0, "warning"));
			BasicRoundDailGauge.indicators().add(new Indicator(1, "error"));
			BasicRoundDailGauge.setStyle("-fxx-warning-indicator-visibility: visible; -fxx-error-indicator-visibility: visible;" + BasicRoundDailGauge.getStyle());
		});

		// assert the indicators visible
		generateSource(".warning-indicator");
		new AssertNode(find(".warning-indicator")).assertXYWH(77.99892859875953, 120.00107140124047, 0.0, 0.0, 0.01);
		generateSource(".error-indicator");
		new AssertNode(find(".error-indicator")).assertXYWH(69.66565628432441, 94.35389638830515, 0.0, 0.0, 0.01);
	}
	
	@Test
	public void fourEvenSegments() {
		setLabel("fourEvenSegments");
		TestUtil.runThenWaitForPaintPulse( () -> {
			for (int i = 0; i < 4; i++) {
				BasicRoundDailGauge.segments().add(new PercentSegment(BasicRoundDailGauge, i * 25.0, (i+1) * 25.0, "segment-" + i));
			}
		});
		
		assertWH(BasicRoundDailGauge, 200.0, 200.0);
		
		// assert the segments
		for (int i = 0; i < 4; i++) { generateSegmentSource(".segment" + i); }
		new AssertNode(find(".segment0")).assertXYWH(0.0, 0.0, 100.38267517089844, 167.4105224609375, 0.01).assertArcCenterRadiusAngleLength(99.0, 99.0, 95.03999999999999, 95.03999999999999, -135.0, -67.5, 0.01);
		new AssertNode(find(".segment1")).assertXYWH(0.0, 0.0, 100.0, 100.24829196929932, 0.01).assertArcCenterRadiusAngleLength(99.0, 99.0, 95.03999999999999, 95.03999999999999, -202.5, -67.5, 0.01);
		new AssertNode(find(".segment2")).assertXYWH(0.0, 0.0, 187.95880126953125, 100.24829864501953, 0.01).assertArcCenterRadiusAngleLength(99.0, 99.0, 95.03999999999999, 95.03999999999999, -270.0, -67.5, 0.01);
		new AssertNode(find(".segment3")).assertXYWH(0.0, 0.0, 195.0427703857422, 167.4105224609375, 0.01).assertArcCenterRadiusAngleLength(99.0, 99.0, 95.03999999999999, 95.03999999999999, -337.5, -67.5, 0.01);
		assertNotFind(".segment4");
	}
	
	@Test
	public void fourUnevenSegments() {
		setLabel("fourUnevenSegments");
		TestUtil.runThenWaitForPaintPulse( () -> {
			BasicRoundDailGauge.segments().add(new PercentSegment(BasicRoundDailGauge, 10.0, 15.0));
			BasicRoundDailGauge.segments().add(new PercentSegment(BasicRoundDailGauge, 20.0, 30.0));
			BasicRoundDailGauge.segments().add(new PercentSegment(BasicRoundDailGauge, 30.0, 50.0));
			BasicRoundDailGauge.segments().add(new PercentSegment(BasicRoundDailGauge, 50.0, 90.0));
		});

		assertWH(BasicRoundDailGauge, 200.0, 200.0);
		
		// assert the segments
		for (int i = 0; i < 4; i++) { generateSegmentSource(".segment" + i); }
		new AssertNode(find(".segment0")).assertXYWH(0.0, 0.0, 103.67221260070801, 129.49900817871094, 0.01).assertArcCenterRadiusAngleLength(99.0, 99.0, 95.03999999999999, 95.03999999999999, -162.0, -13.5, 0.01);
		new AssertNode(find(".segment1")).assertXYWH(0.0, 0.0, 101.47879028320312, 100.31964111328125, 0.01).assertArcCenterRadiusAngleLength(99.0, 99.0, 95.03999999999999, 95.03999999999999, -189.0, -27.0, 0.01);
		new AssertNode(find(".segment2")).assertXYWH(0.0, 0.0, 100.0, 100.48131656646729, 0.01).assertArcCenterRadiusAngleLength(99.0, 99.0, 95.03999999999999, 95.03999999999999, -216.0, -54.0, 0.01);
		new AssertNode(find(".segment3")).assertXYWH(0.0, 0.0, 195.04071044921875, 129.49900817871094, 0.01).assertArcCenterRadiusAngleLength(99.0, 99.0, 95.03999999999999, 95.03999999999999, -270.0, -108.0, 0.01);
		assertNotFind(".segment4");
	}

	@Test
	public void fourMarkers() {
		setLabel("fourUnevenSegments");
		TestUtil.runThenWaitForPaintPulse( () -> {
			BasicRoundDailGauge.markers().add(new PercentMarker(BasicRoundDailGauge, 0.0));
			BasicRoundDailGauge.markers().add(new PercentMarker(BasicRoundDailGauge, 45.0));
			BasicRoundDailGauge.markers().add(new PercentMarker(BasicRoundDailGauge, 50.0));
			BasicRoundDailGauge.markers().add(new PercentMarker(BasicRoundDailGauge, 100.0));
		});

		assertWH(BasicRoundDailGauge, 200.0, 200.0);
		
		// assert the segments
		for (int i = 0; i < 4; i++) { generateMarkerSource(".marker" + i); }
		new AssertNode(find(".marker0")).assertXYWH(45.797285783524174, 152.20271421647584, 0.0, 0.0, 0.01).assertRotate(0.0, 0.0, 45.0, 0.01).assertScale(0.0, 0.0, 0.66, 0.66, 0.01);
		new AssertNode(find(".marker1")).assertXYWH(81.4355708234817, 25.838887189278807, 0.0, 0.0, 0.01).assertRotate(0.0, 0.0, 166.5, 0.01).assertScale(0.0, 0.0, 0.66, 0.66, 0.01);
		new AssertNode(find(".marker2")).assertXYWH(98.99999999999999, 23.760000000000005, 0.0, 0.0, 0.01).assertRotate(0.0, 0.0, 180.0, 0.01).assertScale(0.0, 0.0, 0.66, 0.66, 0.01);
		new AssertNode(find(".marker3")).assertXYWH(152.20271421647584, 152.2027142164758, 0.0, 0.0, 0.01).assertRotate(0.0, 0.0, 315.0, 0.01).assertScale(0.0, 0.0, 0.66, 0.66, 0.01);
		assertNotFind(".marker4");
	}

	// LABELS?
	
	// =============================================================================================================================================================================================================================
	// SUPPORT

	List<String> EXCLUDED_CLASSES = java.util.Arrays.asList();
	
	private void assertWH(BasicRoundDailGauge pane, double w, double h) {
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