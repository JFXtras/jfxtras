/**
 * Copyright (c) 2011-2021, JFXtras
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
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.scene.layout.responsivepane;

import javafx.scene.Scene;

public class Width extends Size {
	
	public final static Width ZERO = new Width(0.0000000000, Unit.INCH);
	
	Unit unit;	
	double value;

	/**
	 * 
	 * @param value
	 * @param unit
	 */
	Width(double value, Unit unit) {
		this.value = value;
		this.unit = unit;
	}
	
	// ========================================================================================================================================================================================================
	// Actual relevant size
	
	/**
	 * Convert the width to inches
	 * @param responsivePane 
	 * @return
	 */
	double toInches(ResponsivePane responsivePane) {
		
		// convert width to diagonal by using the current size of the pane
		Scene lScene = responsivePane.getScene();
		double lHeightInInches = lScene.getHeight() / responsivePane.determinePPI();
		double lWidthInInches = unit.toInches(value);		
		double lDiagonalInInches = Math.sqrt( (lWidthInInches * lWidthInInches) + (lHeightInInches * lHeightInInches));
		if (responsivePane.getTrace()) System.out.println(toString()+ " using the actual scene height of " + lHeightInInches +"in (" + lScene.getHeight() + "px), results in a diagonal of " + lDiagonalInInches + "in");
		return lDiagonalInInches;
	}


	// ========================================================================================================================================================================================================
	// CONVENIENCE
	
	static public Width inch(double v) {
		return new Width(v, Unit.INCH);
	}
	
	static public Width cm(double v) {
		return new Width(v, Unit.CM);
	}
	
	// ========================================================================================================================================================================================================
	// FXML
	
	/**
	 * @param s
	 * @return
	 */
	static public Width valueOf(String s) {
		if (s.endsWith(Unit.INCH.suffix)) {
			return inch(Double.parseDouble(s.substring(0, s.length() - Unit.INCH.suffix.length())));
		}
		if (s.endsWith(Unit.CM.suffix)) {
			return cm(Double.parseDouble(s.substring(0, s.length() - Unit.CM.suffix.length())));
		}
		throw new IllegalArgumentException("Don't know how to parse '" + s + "'");
	}
	
	
	// ========================================================================================================================================================================================================
	// SUPPORT
	
	public String toString() {
		return "width=" + value + unit.suffix;
	}
}
