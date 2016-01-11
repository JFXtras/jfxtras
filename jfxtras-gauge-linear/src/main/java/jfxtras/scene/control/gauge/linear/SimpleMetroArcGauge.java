/**
 * SimpleMetroArcGauge.java
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
import jfxtras.internal.scene.control.gauge.linear.skin.SimpleMetroArcGaugeSkin;

/**
 * = SimpleMetroArcGauge
 * 
 * This gauge is a simple flat possibly colorful (Microsoft Metro style) arc shaped gauge.
 * The needle ranges from about 7 o'clock (min) clockwise to 5 o'clock (max).
 * 
 * == CSS properties
 * include::src/main/asciidoc/scene/control/gauge/linear/cssProperties.adoc[]
 * 
 * == Segments
 * This gauge supports segments, which are colored parts of the arc over which the needle moves:
 * include::src/main/asciidoc/scene/control/gauge/linear/segments.adoc[]
 * 
 * == Marker
 * include::src/main/asciidoc/scene/control/gauge/linear/markers.adoc[]
 * 
 * == Indicators
 * This gauge has two indicators positions: 0 and 1, located at the bottom between the ends of the arc.
 * include::src/main/asciidoc/scene/control/gauge/linear/indicators.adoc[]
 * 
 * == Segment colorscheme
 * include::src/main/asciidoc/scene/control/gauge/linear/segmentsColorscheme.adoc[]
 *  
 * == Disclaimer
 * This is a blatant but approved visual copy of Gerrit Grunwald's Enzo SimpleGauge (https://bitbucket.org/hansolo/enzo/src).
 * include::src/main/asciidoc/scene/control/gauge/linear/disclaimer.adoc[]
 */
public class SimpleMetroArcGauge extends AbstractLinearGauge<SimpleMetroArcGauge> {
	
	// ==================================================================================================================
	// Constructor
	
	public SimpleMetroArcGauge() {
		setPrefSize(200, 200);
	}
	
	// ==================================================================================================================
	// LinearGauge

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override public String getUserAgentStylesheet() {
		return SimpleMetroArcGauge.class.getResource("/jfxtras/internal/scene/control/gauge/linear/" + SimpleMetroArcGauge.class.getSimpleName() + ".css").toExternalForm();
	}

	@Override public Skin<?> createDefaultSkin() {
		return new SimpleMetroArcGaugeSkin(this); 
	}
}
