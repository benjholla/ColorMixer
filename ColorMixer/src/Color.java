
public class Color {

	// Kubelka-Munk absorption coefficient to scattering coefficient ratios for each channel (also known as the Absorbance)
	private double A_r; // RED channel absorbance
	private double A_g; // GREEN channel absorbance
	private double A_b; // BLUE channel absorbance
	
	// R = Reflectance = 1 + (K/S)-[(K/S)^2+2(K/S)]^0.5 (assuming color is opaque) for each channel
	private double R_r; // RED channel reflectance
	private double R_g; // GREEN channel reflectance
	private double R_b; // BLUE channel reflectance
	
	/**
	 * Returns a Reflectance measure.  Assumes the color is opaque.
	 * Reflectance = 1 + (K/S)-[(K/S)^2+2(K/S)]^0.5
	 * @param absortionRatio Kubelka-Munk absorption coefficient to scattering coefficient ratio
	 * @return
	 */
	private double calculateReflectance(double absortionRatio){
		return 1.0 + absortionRatio - Math.pow(Math.pow((absortionRatio), 2.0) + 2.0*(absortionRatio), 0.5);
	}
	
	/**
	 * Returns an Absorbance (K/S) measure for a given RGB channel.  
	 * @param RGBChannelValue (integer value between 0 and 255).
	 * @return
	 */
	private double calculateAbsorbance(double RGBChannelValue){
		return Math.pow((1.0-RGBChannelValue), 2.0) / (2.0 * RGBChannelValue);
	}
	
	/**
	 * Creates a new Color
	 * @param color The color to create
	 */
	public Color(java.awt.Color color){
		// normalize the RGB color values
		double red = color.getRed() == 0 ? 0.00001 : (double)color.getRed()/255.0;
		double green = color.getGreen() == 0 ? 0.00001 : (double)color.getGreen()/255.0;
		double blue = color.getBlue() == 0 ? 0.00001 : (double)color.getBlue()/255.0;
		
		// calculate an Absorbance measure for each channel of the color
		this.A_r = calculateAbsorbance(red);
		this.A_g = calculateAbsorbance(green);
		this.A_b = calculateAbsorbance(blue);
		
		// calculate a Reflectance for each channel of the color
		this.R_r = calculateReflectance(A_r); 
		this.R_g = calculateReflectance(A_g); 
		this.R_b = calculateReflectance(A_b); 
	}
	
	/**
	 * Mixes a collection of colors into this color
	 * Calculates a new K and S coefficient using a weighted average of all K and S coefficients
	 * In this implementation we assume each color has an equal concentration so each concentration 
	 * weight will be equal to 1/(1 + colors.size)
	 * @param colors The Colors to mix into this Color
	 */
	public void mix(Color... colors){
		// just a little error checking
		if(colors == null || colors.length == 0){
			return;
		}
		
		// calculate a concentration weight for a equal concentration of all colors in mix
		double concentration = 1.0 / (1.0 + (double)colors.length);
		
		// calculate first iteration
		double A_r = this.A_r * concentration;
		double A_g = this.A_g * concentration;
		double A_b = this.A_b * concentration;
		
		// sum the weighted average
		for(int i=0; i<colors.length; i++){
			System.out.println("R: " + colors[i].getRGBColor().getRed() + ", G: " + colors[i].getRGBColor().getGreen() + ", B: " + colors[i].getRGBColor().getBlue());
			
			A_r += colors[i].A_r * concentration;
			A_g += colors[i].A_g * concentration;
			A_b += colors[i].A_b * concentration;
		}
		
		// update with results
		this.A_r = A_r;
		this.A_g = A_g;
		this.A_b = A_b;
		this.R_r = calculateReflectance(this.A_r);
		this.R_g = calculateReflectance(this.A_g);
		this.R_b = calculateReflectance(this.A_b);
		
		System.out.println("R: " + getRGBColor().getRed() + ", G: " + getRGBColor().getGreen() + ", B: " + getRGBColor().getBlue());
	}
	
	/**
	 * Mixes another color into this color
	 * Calculates a new K and S coefficient using by averaging the K and S values
	 * In this implementation we assume each color has an equal concentration
	 * @param color The Color to mix into this Color
	 */
	public void mix(Color color){
		System.out.println("R: " + color.getRGBColor().getRed() + ", G: " + color.getRGBColor().getGreen() + ", B: " + color.getRGBColor().getBlue());
		
		// just a little error checking
		if(color == null){
			return;
		}
		
		// calculate new KS (Absorbance) for mix with one color of equal concentration
		double A_r = (this.A_r + color.A_r) / 2.0;
		double A_g = (this.A_g + color.A_g) / 2.0;
		double A_b = (this.A_b + color.A_b) / 2.0;
		
		// update with results
		this.A_r = A_r;
		this.A_g = A_g;
		this.A_b = A_b;
		this.R_r = calculateReflectance(this.A_r);
		this.R_g = calculateReflectance(this.A_g);
		this.R_b = calculateReflectance(this.A_b);
		
		System.out.println("R: " + getRGBColor().getRed() + ", G: " + getRGBColor().getGreen() + ", B: " + getRGBColor().getBlue());
	}
	
	// Source -> http://www.cis.rit.edu/people/faculty/ferwerda/publications/2011/blatner11_cic.pdf
	public java.awt.Color getRGBColor(){
		return new java.awt.Color((int)(R_r*255.0), (int)(R_g*255.0), (int)(R_b*255.0));
	}
	
}
