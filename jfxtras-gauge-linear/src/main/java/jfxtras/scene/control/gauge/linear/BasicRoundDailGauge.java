/**
 * BasicArcGauge.java
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

package jfxtras.scene.control.gauge.linear;

import javafx.scene.control.Skin;
import jfxtras.internal.scene.control.gauge.linear.skin.BasicRoundDailGaugeSkin;
import jfxtras.scene.control.gauge.linear.elements.AbsoluteLabel;

/**
 * = BasicArcGauge
 * 
 * This gauge is a simple semi-real round gauge, featuring an outer ring with shade effect, ticks with labels, a big textual version of the current indicated value and a long slender needle.
 * The needle moves through an arc ranging from about 7 o'clock (min) clockwise to 5 o'clock (max).
 * 
 * == Standard CSS properties
 * include::src/main/asciidoc/scene/control/gauge/linear/cssProperties.adoc[]
 * 
 * == Additional CSS properties
 * The gauge supports the following additional CSS styleable properties:
 * 
 * - -fxx-tick-color: the color used to draw the ticks on the face plate
 * 
 * == Colors and colorschemes
 * The gauge is able to draw in different colors, by specifying values for the following CSS selectors:
 * 
 * - -fxx-backplate-color
 * - -fxx-needle-color
 * - -fxx-tick-color
 * - -fxx-value-color
 * - -fxx-knob-color 
 * - -fxx-tick-color
 * 
 * A few ready-to-use colors are available through colorscheme's:
 * 
 * - colorscheme-light
 * - colorscheme-dark
 * - colorscheme-green
 * - colorscheme-red
 *
 * === Example
 * [source,java]
 * --
 *     final BasicArcGauge lBasicArcGauge = new BasicArcGauge();
 *     lBasicArcGauge.getStyleClass().add("colorscheme-green");
 * --
 * Note: these colorscheme's co-exist with the segment's colorscheme's.
 * 
 * == Labels
 * This gauge per default has percentage labels starting at 0%, stepping up 10% up to and including 100%.
 * include::src/main/asciidoc/scene/control/gauge/linear/labels.adoc[]
 * 
 * == Segments
 * This gauge supports segments, which are colored parts of the arc rendered behind the ticks, over which the needle moves:
 * include::src/main/asciidoc/scene/control/gauge/linear/segments.adoc[]
 * Note: these colorscheme's co-exist with the needle's colorscheme's.
 * 
 * == Marker
 * include::src/main/asciidoc/scene/control/gauge/linear/markers.adoc[]
 * Note: Marker colors are also set in the colorschemes.
 *
 * == Indicators
 * This gauge has six indicators positions: 0 up to and including 5, located around the knob.
 * include::src/main/asciidoc/scene/control/gauge/linear/indicators.adoc[]
 * 
 * == Segment colorscheme
 * include::src/main/asciidoc/scene/control/gauge/linear/segmentsColorscheme.adoc[]
 *  
 * == Disclaimer
 * This is a blatant but approved visual copy of Gerrit Grunwald's Enzo RadialSteel (https://bitbucket.org/hansolo/enzo/src).
 * include::src/main/asciidoc/scene/control/gauge/linear/disclaimer.adoc[]
 */
public class BasicRoundDailGauge extends AbstractLinearGauge<BasicRoundDailGauge> {
	
	// ==================================================================================================================
	// Constructor
	
	/**
	 * 
	 */
	public BasicRoundDailGauge() {
		setPrefSize(200, 200);
		
		// create the default label
		for (double d = 0.0; d <= 100.0; d += 10.0) {
			labels().add(new AbsoluteLabel(d, Math.round(d) + "%"));
		}
	}

	// ==================================================================================================================
	// LinearGauge

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override public String getUserAgentStylesheet() {
		return BasicRoundDailGauge.class.getResource("/jfxtras/internal/scene/control/gauge/linear/" + BasicRoundDailGauge.class.getSimpleName() + ".css").toExternalForm();
	}

	@Override public Skin<?> createDefaultSkin() {
		return new BasicRoundDailGaugeSkin(this); 
	}
}
