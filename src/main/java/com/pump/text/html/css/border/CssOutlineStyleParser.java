package com.pump.text.html.css.border;

import com.pump.text.html.css.CssPropertyParser;

/**
 * The outline-style CSS property sets the style of an element's outline. An
 * outline is a line that is drawn around an element, outside the border.
 * <p>
 * The only two differences between an outline style and a border style are:
 * <ol>
 * <li>"hidden" is not supported for outliness</li>
 * <li>"auto" is not supported for borders.</li>
 * </ol>
 */
public class CssOutlineStyleParser
		implements CssPropertyParser<CssBorderStyleValue> {

	public static final String PROPERTY_OUTLINE_STYLE = "outline-style";

	@Override
	public String getPropertyName() {
		return PROPERTY_OUTLINE_STYLE;
	}

	@Override
	public CssBorderStyleValue parse(String cssString) {
		// for the most part we just piggyback on the CssBorderStyleValue

		if (cssString.trim().toLowerCase().equals("auto")) {
			// "auto" is supported for outlines, but not borders
			return new CssBorderStyleValue(cssString,
					CssBorderStyleValue.Value.SOLID);
		} else if (cssString.trim().toLowerCase().equals("hidden")) {
			throw new IllegalArgumentException(
					"\"hidden\" is supported for border-style, but not outline-style");
		}
		return new CssBorderStyleValue(cssString);
	}
}
