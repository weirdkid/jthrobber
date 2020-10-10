package com.pump.text.html.css;

/**
 * This parses a CSS attribute from a String into another Object.
 * 
 * <T> the type of object this creates, like a Color or a ShadowAttribute.
 */
public interface CssPropertyParser<T> {
	String getPropertyName();

	T parse(String value);

	/**
	 * This returns the key that should be used when this property is parsed and
	 * stored in an AttributeSet. By default this returns
	 * {@link #getPropertyName()}. So for example the "text-shadow" property,
	 * which is not represented by a CSS.Attribute property, is identified by
	 * the String "text-shadow". But the "color" property, which can be
	 * identified by CSS.Attribute.color, should return that Attribute object.
	 */
	default Object getAttributeKey() {
		return getPropertyName();
	}
}