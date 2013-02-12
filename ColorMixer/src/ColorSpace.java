import java.awt.Color;

import Jama.Matrix;

public class ColorSpace {

	public static void main(String[] args){
		Color color = Color.RED;
		System.out.println("R: " + color.getRed() + ", G: " + color.getGreen() + ", B: " + color.getBlue());
		ColorSpace colorSpace1 = new ColorSpace(color.getRed(), color.getGreen(), color.getBlue());
		ColorSpace colorSpace2 = new ColorSpace(colorSpace1.getTristimulusX(), colorSpace1.getTristimulusY(), colorSpace1.getTristimulusZ());
	}
	
	public static final double[][] standardObserver1931Values = {{0.489989,  0.310008, 0.2},
																 {0.0176962, 0.81240,  0.010},
																 {0.0,       0.01,     0.99}};
	
	// Tristimulus XYZ Color Model components
	private double tristimulusX;
	private double tristimulusY;
	private double tristimulusZ;
	
	// Chromaticity coordinates
	private double chromaticityX;
	private double chromaticityY;
	private double chromaticityZ;
	
	// RGB Color Model components
	private int R;
	private int G;
	private int B;
	
	public double getTristimulusX() {
		return tristimulusX;
	}

	public double getTristimulusY() {
		return tristimulusY;
	}

	public double getTristimulusZ() {
		return tristimulusZ;
	}

	public double getChromaticityX() {
		return chromaticityX;
	}

	public double getChromaticityY() {
		return chromaticityY;
	}

	public double getChromaticityZ() {
		return chromaticityZ;
	}
	
	public double getR() {
		return R;
	}

	public double getG() {
		return G;
	}

	public double getB() {
		return B;
	}

	public ColorSpace(int R, int G, int B){
		this.R = R;
		this.G = G;
		this.B = B;

		// compute XYZ using the linear equation in the case of the standard observer
		double[][] RGBValues = {{R},{G},{B}};
		Matrix RGB = new Matrix(RGBValues);
		Matrix standardObserver1931 = new Matrix(standardObserver1931Values);
		
		// solve for XYZ
		Matrix XYZ = standardObserver1931.times(RGB);
		this.tristimulusX = XYZ.get(0, 0);
		this.tristimulusY = XYZ.get(1, 0);
		this.tristimulusZ = XYZ.get(2, 0);
		
		// calculate chromaticity coordinates
		this.chromaticityX = calculateChromaticityX(tristimulusX, tristimulusY, tristimulusZ);
		this.chromaticityY = calculateChromaticityY(tristimulusX, tristimulusY, tristimulusZ);
		this.chromaticityZ = calculateChromaticityZ(tristimulusX, tristimulusY, tristimulusZ);
	}
	
	public ColorSpace(double X, double Y, double Z){
		this.tristimulusX = X;
		this.tristimulusY = Y;
		this.tristimulusZ = Z;
		
		// calculate chromaticity coordinates
		this.chromaticityX = calculateChromaticityX(X, Y, Z);
		this.chromaticityY = calculateChromaticityY(X, Y, Z);
		this.chromaticityZ = calculateChromaticityZ(X, Y, Z);
		
		// compute RGB using the linear equation in the case of the standard observer
		double[][] XYZValues = {{X},{Y},{Z}};
		Matrix XYZ = new Matrix(XYZValues);
		Matrix standardObserver1931Inverse = new Matrix(standardObserver1931Values).inverse();
		
		// solve for RGB
		Matrix RGB = standardObserver1931Inverse.times(XYZ);
		this.R = (int)RGB.get(0, 0);
		this.G = (int)RGB.get(1, 0);
		this.B = (int)RGB.get(2, 0);
	}
	
	private double calculateChromaticityX(double X, double Y, double Z){
		return X / (X + Y + Z);
	}
	
	private double calculateChromaticityY(double X, double Y, double Z){
		return Y / (X + Y + Z);
	}

	private double calculateChromaticityZ(double X, double Y, double Z){
		return Z / (X + Y + Z);
	}
	
}
