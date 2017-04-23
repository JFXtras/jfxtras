package jfxtras.scene.layout.responsivepane;

public class Diagonal extends Size {
	
	final Unit unit;	
	final double value;

	/**
	 * 
	 * @param value
	 * @param unit
	 */
	Diagonal(double value, Unit unit) {
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
		return unit.toInches(value);
	}


	// ========================================================================================================================================================================================================
	// CONVENIENCE
	
	static public Diagonal inch(double v) {
		return new Diagonal(v, Unit.INCH);
	}
	
	static public Diagonal cm(double v) {
		return new Diagonal(v, Unit.CM);
	}
	
	
	// ========================================================================================================================================================================================================
	// FXML
	
	/**
	 * @param s
	 * @return
	 */
	static public Diagonal valueOf(String s) {
		s = s.trim();
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
		return value + unit.suffix;
	}
}
