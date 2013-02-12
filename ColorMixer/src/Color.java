
public class Color {

	private double K; // Kubelka-Munk absorption coefficient
	private double S; // Kubelka-Munk scattering coefficient
	private double R; // R = Reflectance = 1 + (K/S)-[(K/S)^2+2(K/S)]^0.5 (assuming color is opaque)
	
	public double getK(){
		return K;
	}
	
	public double getS(){
		return S;
	}
	
	public double getReflectance(){
		return R;
	}
	
	public Color(double K, double S){
		this.K = K;
		this.S = S;
		calculateReflectance(K, S);
	}
	
	/**
	 * Mixes a collection of colors into this color
	 * Calculates a new K and S coefficient using a weighted average of all K and S coefficients
	 * In this implementation we assume each color has an equal concentration so each concentration 
	 * weight will be equal to 1/(1 + colors.size)
	 * @param colors
	 */
	public void mix(Color... colors){
		
		double concentration = 1 / (1 + colors.length);
		double K = this.K * concentration;
		double S = this.S * concentration;
		
		for(int i=0; i<colors.length; i++){
			K += colors[i].getK() * concentration;
			S += colors[i].getS() * concentration;
		}
		
		this.K = K;
		this.S = S;
		
		calculateReflectance(K, S);
	}
	
	private void calculateReflectance(double K, double S){
		this.R = 1.0 + (K/S) - Math.pow(Math.pow((K/S), 2) + 2*(K/S), 0.5);
	}
}
