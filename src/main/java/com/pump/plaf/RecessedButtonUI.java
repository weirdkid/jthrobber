/**
 * This software is released as part of the Pumpernickel project.
 * 
 * All com.pump resources in the Pumpernickel project are distributed under the
 * MIT License:
 * https://raw.githubusercontent.com/mickleness/pumpernickel/master/License.txt
 * 
 * More information about the Pumpernickel project is available here:
 * https://mickleness.github.io/pumpernickel/
 */
package com.pump.plaf;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 * This resembles the "recessed" button UI seen in Mac OS 10.15.
 * <p>
 * <img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/resources/filledbuttonui/RecessedButtonUI.png"
 * alt="RecessedButtonUI Screenshot">
 * <P>
 * It is not intended to be an exact replica, but it is very similar.
 * <P>
 * According to <a href="http://nadeausoftware.com/node/87">this</a> article,
 * the "recessed" and "roundRect" look is originally intended to indicate: <br>
 * "to indicate a choice in limiting the scope of an operation, such as the buttons at the top of a Finder when searching."
 */
public class RecessedButtonUI extends QButtonUI {

	public static final ButtonShape RECESSED_SHAPE = new ButtonShape(3, 4);

	/**
	 * The <code>SimpleButtonFill</code> used to achieve the "recessed" look.
	 */
	public static final SimpleButtonFill RECESSED_FILL = new SimpleButtonFill() {
		private float[] fillWeights = new float[] { 0 };
		private Color strokeColor = new Color(0xff6F6F6F);

		private Color[] normalColors = new Color[] { new Color(0x3B000000, true) };
		private Color[] darkerColors = new Color[] { new Color(0x4F000000, true) };

		@Override
		public Paint getDarkerFill(Rectangle fillRect) {
			return PlafPaintUtils.getVerticalGradient("recessedUI.darker",
					fillRect.height, fillRect.y, fillWeights, darkerColors);
		}

		@Override
		public Paint getDarkestFill(Rectangle fillRect) {
			return null;
		}

		@Override
		public Paint getNormalFill(Rectangle fillRect) {
			return PlafPaintUtils.getVerticalGradient("recessedUI.normal",
					fillRect.height, fillRect.y, fillWeights, normalColors);
		}

		@Override
		public Paint getRolloverFill(Rectangle fillRect) {
			return null;
		}

		@Override
		public Paint getStroke(AbstractButton button, Rectangle fillRect) {
			return strokeColor;
		}
	};

	private static RecessedButtonUI recessedButtonUI = new RecessedButtonUI();

	/**
	 * Create a new instance of this ButtonUI for a component.
	 * <p>
	 * This method is required if you want to make this ButtonUI the default UI
	 * by invoking: <br>
	 * <code>UIManager.getDefaults().put("ButtonUI", "com.pump.plaf.RecessedButtonUI");</code>
	 */
	public static ComponentUI createUI(JComponent c) {
		return recessedButtonUI;
	}

	public RecessedButtonUI() {
		super(RECESSED_FILL, RECESSED_SHAPE);
	}

	/** Returns false. This button is designed to be translucent. */
	@Override
	public boolean isFillOpaque() {
		return false;
	}
};