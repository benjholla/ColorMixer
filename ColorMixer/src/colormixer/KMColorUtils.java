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
 * A utilities class that provides convenience functions for dealing with KMColors
 */
public class KMColorUtils {
	
	/**
	 * Simple wrapper method for mixing two colors
	 * @param colorA
	 * @param colorB
	 * @return
	 */
	public static Color mix(Color colorA, Color colorB){
		KMColor color = new KMColor(colorA);
		color.mix(colorB);
		return color.getColor();
	}
	
	/**
	 * Simple wrapper method for mixing a collection of colors
	 * @param colors
	 * @return
	 */
	public static Color mix(Color... colors){
		if(colors.length >= 1){
			KMColor color = new KMColor(colors[0]);
			for(int i=1; i<colors.length; i++){
				color.mix(colors[i]);
			}
			return color.getColor();
		}
		return null;
	}

}
