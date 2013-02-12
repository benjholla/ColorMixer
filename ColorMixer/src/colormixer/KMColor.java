/*
 * The MIT License (MIT)
 * Copyright (c) 2013 Ben Holland
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), to deal in the Software without restriction, 
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package colormixer;

import java.awt.Color;

/**
 * @author Ben Holland
 * 
 * A helper class for mixing RGB Colors.  Uses a simplified Kubelka-Munk model.
 * Assumes all colors are opaque and that all colors have equal weight when blending.
 * 
 * Example Usage: 
 * KMColor color = new KMColor(java.awt.Color.RED);
 * color.mix(java.awt.Color.ORANGE);
 * java.awt.Color result = color.getRGBColor();
 * 
 * References: 
 * 
 * 1) Chet S. Haase and Gary W. Meyer. 1992. Modeling pigmented materials for realistic image synthesis. 
 *    ACM Trans. Graph. 11, 4 (October 1992), 305-335. DOI=10.1145/146443.146452 http://doi.acm.org/10.1145/146443.146452
 *    
 * 2) Blatner, A.M., Ferwerda, J.A., Darling, B.A. and Bailey, R.J. (2011) TangiPaint: a tangible digital painting system. 
 *    Proceedings IS&T/SID 19th Color Imaging Conference, 102-107.
 */
public class KMColor {
	
	// Kubelka-Munk absorption coefficient to scattering coefficient ratios for each channel (also known as the Absorbance)
	private double A_r; // RED channel absorbance
	private double A_g; // GREEN channel absorbance
	private double A_b; // BLUE channel absorbance
	
	/**
	 * Returns a Reflectance measure.  Assumes the color is opaque.
	 * @param absortionRatio Kubelka-Munk absorption coefficient to scattering coefficient ratio
	 * @return
	 */
	private double calculateReflectance(double absorbtionRatio){
		return 1.0 + absorbtionRatio - Math.sqrt(Math.pow(absorbtionRatio, 2.0) + (2.0 * absorbtionRatio));
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
	public KMColor(java.awt.Color color){
		// normalize the RGB color values
		double red = color.getRed() == 0 ? 0.00001 : (double)color.getRed()/255.0;
		double green = color.getGreen() == 0 ? 0.00001 : (double)color.getGreen()/255.0;
		double blue = color.getBlue() == 0 ? 0.00001 : (double)color.getBlue()/255.0;
		
		// calculate an Absorbance measure for each channel of the color
		this.A_r = calculateAbsorbance(red);
		this.A_g = calculateAbsorbance(green);
		this.A_b = calculateAbsorbance(blue);
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
			KMColor color = new KMColor(colors[i]);
			A_r += color.A_r * concentration;
			A_g += color.A_g * concentration;
			A_b += color.A_b * concentration;
		}
		
		// update with results
		this.A_r = A_r;
		this.A_g = A_g;
		this.A_b = A_b;
	}
	
	/**
	 * Mixes another color into this color
	 * Calculates a new K and S coefficient using by averaging the K and S values
	 * In this implementation we assume each color has an equal concentration
	 * @param color The Color to mix into this Color
	 */
	public void mix(Color color){
		// calculate new KS (Absorbance) for mix with one color of equal concentration
		KMColor kmColor = new KMColor(color);
		this.A_r = (this.A_r + kmColor.A_r) / 2.0;
		this.A_g = (this.A_g + kmColor.A_g) / 2.0;
		this.A_b = (this.A_b + kmColor.A_b) / 2.0;
	}
	
	public Color getColor(){
		int red = (int)(calculateReflectance(this.A_r) * 255.0);
		int green = (int)(calculateReflectance(this.A_g) * 255.0);
		int blue = (int)(calculateReflectance(this.A_b) * 255.0);
		return new java.awt.Color(red, green, blue);
	}
	
}
