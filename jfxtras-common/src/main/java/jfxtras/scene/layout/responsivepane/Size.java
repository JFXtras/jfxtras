package jfxtras.scene.layout.responsivepane;

public abstract class Size {

	public final static Diagonal ZERO = new Diagonal(0.0000000000, Unit.INCH);	

	abstract double toInches(ResponsivePane responsivePane);
	
	// ========================================================================================================================================================================================================
	// FXML
	
	/**
	 * @param s
	 * @return
	 */
	static public Size valueOf(String s) {
		
		// width if proper prefix
		if (s.startsWith("w:")) {
			return Width.valueOf(s.substring("w:".length()));
		}
		if (s.startsWith("width:")) {
			return Width.valueOf(s.substring("width:".length()));
		}
		
		// else it must be a diagonal
		if (Character.isDigit(s.charAt(0))) {
			return Diagonal.valueOf(s);
		}
		
		// else it is a device
		return new DeviceSizePlaceHolder(s);
	}
	
	public static class DeviceSizePlaceHolder extends Size {

		DeviceSizePlaceHolder(String device) {
			this.device = device;
		}
		
		final String device;
		
		@Override
		double toInches(ResponsivePane responsivePane) {
			size = responsivePane.getDeviceSizes().get(device);
			return size.toInches(responsivePane);
		}
		Size size;
		
		public String toString() {
			return (size == null? "null" : size.toString());
		}
	}

}
