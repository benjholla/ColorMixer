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
 */
public class KMColor {
	
	// Kubelka-Munk absorption coefficient to scattering coefficient ratios for each channel (also known as the Absorbance)
	private double A_r; // RED channel absorbance
	private double A_y; // YELLOW channel absorbance
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
	 * Returns an absorbance (K/S) measure for a given RGB channel.  
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
		// convert RBG to RYB color space
		ColorSpace colorspace = ColorSpace.createNewColorSpaceFromRGB(color.getRed(), color.getGreen(), color.getBlue());
		
		// normalize the RGB color values
		double red = colorspace.getRYB_R() == 0 ? 0.00001 : (double)colorspace.getRYB_R()/255.0;
		double yellow = colorspace.getRYB_Y() == 0 ? 0.00001 : (double)colorspace.getRYB_Y()/255.0;
		double blue = colorspace.getRYB_B() == 0 ? 0.00001 : (double)colorspace.getRYB_B()/255.0;
		
		// calculate an Absorbance measure for each channel of the color
		this.A_r = calculateAbsorbance(red);
		this.A_y = calculateAbsorbance(yellow);
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
		double A_g = this.A_y * concentration;
		double A_b = this.A_b * concentration;
		
		// sum the weighted average
		for(int i=0; i<colors.length; i++){
			KMColor color = new KMColor(colors[i]);
			A_r += color.A_r * concentration;
			A_g += color.A_y * concentration;
			A_b += color.A_b * concentration;
		}
		
		// update with results
		this.A_r = A_r;
		this.A_y = A_g;
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
		this.A_y = (this.A_y + kmColor.A_y) / 2.0;
		this.A_b = (this.A_b + kmColor.A_b) / 2.0;
	}
	
	/**
	 * Returns a standard RGB color as a java.awt.Color object
	 * @return
	 */
	public Color getColor(){
		// de-normalize values
		int red = (int)(calculateReflectance(this.A_r) * 255.0);
		int yellow = (int)(calculateReflectance(this.A_y) * 255.0);
		int blue = (int)(calculateReflectance(this.A_b) * 255.0);
		
		// convert back to rgb
		ColorSpace colorspace = ColorSpace.createNewColorSpaceFromRYB(red, yellow, blue);	
		return new java.awt.Color((int)colorspace.getRGB_R(), (int)colorspace.getRGB_G(), (int)colorspace.getRGB_B());
	}
	
}
