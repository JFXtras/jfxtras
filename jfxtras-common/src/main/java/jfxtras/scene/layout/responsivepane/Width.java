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
