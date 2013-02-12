
public class Color {

	private double K; // Kubelka-Munk absorption coefficient
	private double S; // Kubelka-Munk scattering coefficient
	private double R; // R = Reflectance = 1 + (K/S)-[(K/S)^2+2(K/S)]^0.5 (assuming color is opaque)
	
	/**
	 * Returns the Color's Kubelka-Munk absorption coefficient
	 * @return
	 */
	public double getK(){
		return K;
	}
	
	/**
	 * Return's the Color's Kubelka-Munk scattering coefficient
	 * @return
	 */
	public double getS(){
		return S;
	}
	
	/**
	 * Returns the Color's Reflectance
	 * @return
	 */
	public double getReflectance(){
		return R;
	}
	
	/**
	 * Creates a new Color
	 * @param K Kubelka-Munk absorption coefficient
	 * @param S Kubelka-Munk scattering coefficient
	 */
	public Color(double K, double S){
		this.K = K;
		this.S = S;
		this.R = calculateReflectance(K, S); 
	}
	
	/**
	 * Mixes a collection of colors into this color
	 * Calculates a new K and S coefficient using a weighted average of all K and S coefficients
	 * In this implementation we assume each color has an equal concentration so each concentration 
	 * weight will be equal to 1/(1 + colors.size)
	 * @param colors
	 */
	public void mix(Color... colors){
		// caculate a concentration weight for a equal concentration of all colors in mix
		double concentration = 1 / (1 + colors.length);
		
		// calculate first iteration
		double K = this.K * concentration;
		double S = this.S * concentration;
		
		// sum the weighted average
		for(int i=0; i<colors.length; i++){
			K += colors[i].getK() * concentration;
			S += colors[i].getS() * concentration;
		}
		
		// update with results
		this.K = K;
		this.S = S;
		this.R = calculateReflectance(K, S);
	}
	
	private double calculateReflectance(double K, double S){
		return 1.0 + (K/S) - Math.pow(Math.pow((K/S), 2.0) + 2.0*(K/S), 0.5);
	}
}
