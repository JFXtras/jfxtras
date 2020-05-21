/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.resources;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class loads Google's Roboto font and make it available in CSS files and style properties.
 * You can load all available fonts or individual fonts.
 * The to-be-used family name for -fx-font-family (between quotes) is available as a property on the AvailableFonts.
 *
 * Example
 * [source,java]
 * --
 * JFXtrasFontRoboto.loadAll();
 * text.setStyle("-fx-font-family: '" + JFXtrasFontRoboto.AvailableFonts.RobotoMedium.getFamilyName() + "';");
 * --
 *
 * [source,css]
 * --
 * .label {
 *     -fx-font-family: 'Roboto Light';
 * }
 * --
*
 *
 */
public class JFXtrasFontRoboto {
	
	/**
	 * Load all available fonts
	 */
	public static void loadAll() {
	    for (AvailableFonts f : AvailableFonts.values()) {
	    	load(f);
	    }
	}
	
	/**
	 * Load a single font (if not already loaded)
	 * @param font
	 */
	synchronized public static void load(AvailableFonts font) {
		if (!loadedFonts.contains(font)) {
			javafx.scene.text.Font.loadFont(JFXtrasFontRoboto.class.getResource("/" + font.getFilename()).toExternalForm(), 12);
			loadedFonts.add(font);
		}
	}
	static private final Set<AvailableFonts> loadedFonts = Collections.synchronizedSet(new HashSet<>()); 
	
	public enum AvailableFonts {
		RobotoItalic("Roboto Italic", "Roboto-Italic.ttf"),
		RobotoRegular("Roboto Regular", "Roboto-Regular.ttf"),
		RobotoBlack("Roboto Black", "Roboto-Black.ttf"),
		RobotoBlackItalic("Roboto Black Italic", "Roboto-BlackItalic.ttf"),
		RobotoBold("Roboto Bold", "Roboto-Bold.ttf"),
		RobotoBoldItalic("Roboto Bold Italic", "Roboto-BoldItalic.ttf"),
		RobotoLight("Roboto Light", "Roboto-Light.ttf"),
		RobotoLightItalic("Roboto Light Italic", "Roboto-LightItalic.ttf"),
		RobotoMedium("Roboto Medium", "Roboto-Medium.ttf"),
		RobotoMediumItalic("Roboto Medium Italic", "Roboto-MediumItalic.ttf"),
		RobotoThin("Roboto Thin", "Roboto-Thin.ttf"),
		RobotoThinItalic("Roboto Thin Italic", "Roboto-ThinItalic.ttf"),
		RobotoCondensedBold("Roboto Condensed Bold", "RobotoCondensed-Bold.ttf"),
		RobotoCondensedBoldItalic("Roboto Condensed Bold Italic", "RobotoCondensed-BoldItalic.ttf"),
		RobotoCondensedItalic("Roboto Condensed Italic", "RobotoCondensed-Italic.ttf"),
		RobotoCondensedLight("Roboto Condensed Light", "RobotoCondensed-Light.ttf"),
		RobotoCondensedLightItalic("Roboto Condensed Light Italic", "RobotoCondensed-LightItalic.ttf"),
		RobotoCondensedRegular("Roboto Condensed Regular", "RobotoCondensed-Regular.ttf")
		;
		
		AvailableFonts(String familyName, String filename) {
			this.familyName = familyName;
			this.filename = filename;
		}
		final private String familyName;
		final private String filename;
		
		public String getFamilyName() {
			return this.familyName;
		}
		private String getFilename() {
			return this.filename;
		}
	}
}
