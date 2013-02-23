package colormixer;
import java.awt.Color;

import Jama.Matrix;

public class ColorSpace {

	public static void main(String[] args){
		Color color = Color.CYAN;
		System.out.println("R: " + color.getRed() + ", G: " + color.getGreen() + ", B: " + color.getBlue());
		
		
		ColorSpace colorSpace1 = createNewColorSpaceFromRGB(color.getRed(), color.getGreen(), color.getBlue());
		ColorSpace colorSpace2 = createNewColorSpaceFromTristimulusXYZ(colorSpace1.getTristimulusX(), colorSpace1.getTristimulusY(), colorSpace1.getTristimulusZ());
		
		System.out.println("R: " + colorSpace1.getRYB_R() + ", Y: " + colorSpace1.getRYB_Y() + ", B: " + colorSpace1.getRYB_B());
		
		//double[] RYB = rgbToRYB((double)color.getRed(), (double)color.getGreen(), (double)color.getBlue());
		//System.out.println("R: " + (int)RYB[0] + ", Y: " + (int)RYB[1] + ", B: " + (int)RYB[2]);
		
		
		//double[] RGB = rybToRGB(RYB[0], RYB[1], RYB[2]);
		//System.out.println("R: " + (int)RGB[0] + ", G: " + (int)RGB[1] + ", B: " + (int)RGB[2]);
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
	private int RGB_R;
	private int RGB_G;
	private int RGB_B;
	
	// RYB Color Model components
	private int RYB_R;
	private int RYB_Y;
	private int RYB_B;

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

	public double getRGB_R() {
		return RGB_R;
	}

	public double getRGB_G() {
		return RGB_G;
	}

	public double getRGB_B() {
		return RGB_B;
	}
	
	public double getRYB_R() {
		return RYB_R;
	}

	public double getRYB_Y() {
		return RYB_Y;
	}

	public double getRYB_B() {
		return RYB_B;
	}

	private ColorSpace(){
		// make the constructor private
	}
	
	public static ColorSpace createNewColorSpaceFromRGB(int R, int G, int B){
		ColorSpace colorspace = new ColorSpace();
		colorspace.RGB_R = R;
		colorspace.RGB_G = G;
		colorspace.RGB_B = B;
		
		double[] RGBToRYB = rgbToRYB(colorspace.RGB_R, colorspace.RGB_G, colorspace.RGB_B);
		colorspace.RYB_R = (int)RGBToRYB[0];
		colorspace.RYB_Y = (int)RGBToRYB[1];
		colorspace.RYB_B = (int)RGBToRYB[2];

		// compute XYZ using the linear equation in the case of the standard observer
		double[][] RGBValues = {{R},{G},{B}};
		Matrix RGB = new Matrix(RGBValues);
		Matrix standardObserver1931 = new Matrix(standardObserver1931Values);

		// solve for XYZ
		Matrix XYZ = standardObserver1931.times(RGB);
		colorspace.tristimulusX = XYZ.get(0, 0);
		colorspace.tristimulusY = XYZ.get(1, 0);
		colorspace.tristimulusZ = XYZ.get(2, 0);

		// calculate chromaticity coordinates
		colorspace.chromaticityX = calculateChromaticityX(colorspace.tristimulusX, colorspace.tristimulusY, colorspace.tristimulusZ);
		colorspace.chromaticityY = calculateChromaticityY(colorspace.tristimulusX, colorspace.tristimulusY, colorspace.tristimulusZ);
		colorspace.chromaticityZ = calculateChromaticityZ(colorspace.tristimulusX, colorspace.tristimulusY, colorspace.tristimulusZ);
		
		return colorspace;
	}
	
	public static ColorSpace createNewColorSpaceFromRYB(int R, int Y, int B){
		ColorSpace colorspace = new ColorSpace();
		colorspace.RYB_R = R;
		colorspace.RYB_Y = Y;
		colorspace.RYB_B = B;
		
		double[] RYBToRGB = rybToRGB(colorspace.RYB_R, colorspace.RYB_Y, colorspace.RYB_B);
		colorspace.RGB_R = (int)RYBToRGB[0];
		colorspace.RGB_G = (int)RYBToRGB[1];
		colorspace.RGB_B = (int)RYBToRGB[2];

		// compute XYZ using the linear equation in the case of the standard observer
		double[][] RGBValues = {{colorspace.RGB_R},{colorspace.RGB_G},{colorspace.RGB_B}};
		Matrix RGB = new Matrix(RGBValues);
		Matrix standardObserver1931 = new Matrix(standardObserver1931Values);

		// solve for XYZ
		Matrix XYZ = standardObserver1931.times(RGB);
		colorspace.tristimulusX = XYZ.get(0, 0);
		colorspace.tristimulusY = XYZ.get(1, 0);
		colorspace.tristimulusZ = XYZ.get(2, 0);

		// calculate chromaticity coordinates
		colorspace.chromaticityX = calculateChromaticityX(colorspace.tristimulusX, colorspace.tristimulusY, colorspace.tristimulusZ);
		colorspace.chromaticityY = calculateChromaticityY(colorspace.tristimulusX, colorspace.tristimulusY, colorspace.tristimulusZ);
		colorspace.chromaticityZ = calculateChromaticityZ(colorspace.tristimulusX, colorspace.tristimulusY, colorspace.tristimulusZ);
		
		return colorspace;
	}

	public static ColorSpace createNewColorSpaceFromTristimulusXYZ(double X, double Y, double Z){
		ColorSpace colorspace = new ColorSpace();
		colorspace.tristimulusX = X;
		colorspace.tristimulusY = Y;
		colorspace.tristimulusZ = Z;

		// calculate chromaticity coordinates
		colorspace.chromaticityX = calculateChromaticityX(X, Y, Z);
		colorspace.chromaticityY = calculateChromaticityY(X, Y, Z);
		colorspace.chromaticityZ = calculateChromaticityZ(X, Y, Z);

		// compute RGB using the linear equation in the case of the standard observer
		double[][] XYZValues = {{X},{Y},{Z}};
		Matrix XYZ = new Matrix(XYZValues);
		Matrix standardObserver1931Inverse = new Matrix(standardObserver1931Values).inverse();

		// solve for RGB
		Matrix RGB = standardObserver1931Inverse.times(XYZ);
		colorspace.RGB_R = (int)RGB.get(0, 0);
		colorspace.RGB_G = (int)RGB.get(1, 0);
		colorspace.RGB_B = (int)RGB.get(2, 0);
		
		double[] RYB = rgbToRYB(colorspace.RGB_R, colorspace.RGB_G, colorspace.RGB_B);
		colorspace.RYB_R = (int)RYB[0];
		colorspace.RYB_Y = (int)RYB[1];
		colorspace.RYB_B = (int)RYB[2];
		
		return colorspace;
	}

	private static double calculateChromaticityX(double X, double Y, double Z){
		return X / (X + Y + Z);
	}

	private static double calculateChromaticityY(double X, double Y, double Z){
		return Y / (X + Y + Z);
	}

	private static double calculateChromaticityZ(double X, double Y, double Z){
		return Z / (X + Y + Z);
	}
	
	// credit to http://www.insanit.net/tag/rgb-to-ryb/
	private static double[] rgbToRYB (double R, double G, double B){
		// remove the whiteness from the color
		double w = Math.min(Math.min(R, G), B);
		double r = R - w;
		double g = G - w;
		double b = B - w;
		
		double mg = Math.max(Math.max(r, g), b);
		
		// get the yellow out of the red and green
		double y = Math.min(r, g);
		r -= y;
		g -= y;
		
		// if this unfortunate conversion combines blue and green, then cut each in half to preserve the value's maximum range
		if(b != 0 && g != 0)
		{
			b /= 2.0;
	        g /= 2.0;
		}
		
		// redistribute the remaining green
		y += g;
		b += g;
		
		
		// normalize to values
	    double my = Math.max(r, Math.max(y, b));
	    if (my != 0) {
	        double n = mg / my;
	        r *= n;
	        y *= n;
	        b *= n;
	    }
	    
	    // add back in white
	    r += w;
	    y += w;
	    b += w;
		
		double[] RYB = new double[3];
		RYB[0] = r;
		RYB[1] = y;
		RYB[2] = b;
		
		return RYB;
	}
	
	// credit to http://www.insanit.net/tag/rgb-to-ryb/
	private static double[] rybToRGB (double R, double Y, double B){
		// remove the whiteness from the color
		double w = Math.min(Math.min(R, Y), B);
		double r = R - w;
		double y = Y - w;
		double b = B - w;
		
		double my = Math.max(Math.max(r, y), b);
		
		// get the green out of the yellow and blue
		double g = Math.min(y, b);
		y -= g;
		b -= g;
		
		if(b != 0 && g != 0){
			b *= 2.0;
			g *= 2.0;
		}
		
		// redistribute the remaining yellow
		r += y;
		g += y;
		
		// normalize to values
		double mg = Math.max(Math.max(r, g), b);
		if(mg != 0){
			double n = my / mg;
			r *= n;
			g *= n;
			b *= n;
		}
		
		// add the white back in
		r += w;
		g += w;
		b += w;
		
		double[] RGB = new double[3];
		RGB[0] = r;
		RGB[1] = g;
		RGB[2] = b;
		
		return RGB;
	}

}