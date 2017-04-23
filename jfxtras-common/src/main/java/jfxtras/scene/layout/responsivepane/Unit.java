package jfxtras.scene.layout.responsivepane;

enum Unit {
	INCH("in"), CM("cm");
	
	private Unit(String suffix){
        this.suffix = suffix;
    }
    final String suffix;
    
	public double toInches(double value) {
		if (this.equals(CM)) {
			return value * 0.393701;
		}
		return value;
	}	    
}