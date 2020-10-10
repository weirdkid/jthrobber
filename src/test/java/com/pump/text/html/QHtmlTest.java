package com.pump.text.html;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLEditorKit;

import com.pump.graphics.vector.FillOperation;
import com.pump.graphics.vector.ImageOperation;
import com.pump.graphics.vector.Operation;
import com.pump.graphics.vector.StringOperation;
import com.pump.graphics.vector.VectorGraphics2D;
import com.pump.graphics.vector.VectorImage;
import com.pump.text.html.css.CssColorParser;

import junit.framework.TestCase;

/**
 * This renders a series of HTML and makes sure misc visual features are
 * painted. Most tests check to see that the QHTMLEditorKit produces certain
 * results AND that the default HTMLEditorKit fails to produce those same
 * results. In the unlikely event that the HTMLEditorKit is ever enhanced and
 * some of its shortcomings are addressed: we should remove some of the extra
 * work the QHTMLEditorKit is doing.
 */
public class QHtmlTest extends TestCase {

	/**
	 * Test that "darkorchid" is resolved correctly.
	 */
	public void test_named_color_h3_inline() throws Exception {
		//@formatter:off
		String html = "<html>\n" + 
					  "  <h3 style=\"color: darkorchid;font-size: 150%;\">Lorem Ipsum</h3>\n" + 
				      "</html>";
		//@formatter:on
		Color darkorchid = CssColorParser.getNamedColor("darkorchid");

		List<Operation> ops1 = getOperations(true, html);
		assertEquals(darkorchid, ops1.get(1).getContext().getPaint());

		List<Operation> ops2 = getOperations(false, html);
		assertEquals(Color.black, ops2.get(1).getContext().getPaint());
	}

	/**
	 * Test that "darkorchid" is resolved correctly.
	 */
	public void test_named_color_h1_internalSheet() throws Exception {
		//@formatter:off
		String html = "<html>\n" + 
				"  <head>\n" + 
				"    <style>\n" + 
				"      h1   {color: darkorchid;}\n" + 
				"    </style>\n" + 
				"  </head>\n" + 
				"  <body>\n" + 
				"    <h1 style=\"font-size: 150%;\">Lorem Ipsum</h1>\n" + 
				"  </body>\n" + 
				"</html>";
		//@formatter:on

		Color darkorchid = CssColorParser.getNamedColor("darkorchid");

		List<Operation> ops1 = getOperations(true, html);
		assertEquals(darkorchid, ops1.get(1).getContext().getPaint());

		List<Operation> ops2 = getOperations(false, html);
		assertEquals(Color.black, ops2.get(1).getContext().getPaint());
	}

	/**
	 * Test that a 4-digit hex code with an alpha component is rendered
	 * correctly.
	 */
	public void test_hex_alpha_h3_inline() throws Exception {
		//@formatter:off
		String html = "<html>\n" + 
				"  <h3 style=\"color: #321C;font-size: 150%;\">Lorem Ipsum</h3>\n" + 
				"</html>";
		//@formatter:on
		List<Operation> ops1 = getOperations(true, html);
		assertEquals(204, ops1.get(1).getContext().getColor().getAlpha());

		List<Operation> ops2 = getOperations(false, html);
		assertEquals(255, ops2.get(1).getContext().getColor().getAlpha());
	}

	/**
	 * Test that a 8-digit hex code with an alpha component is rendered
	 * correctly.
	 */
	public void test_hex_alpha_h1_internalSheet() throws Exception {
		//@formatter:off
		String html = "<html>\n" + 
				"  <head>\n" + 
				"    <style>\n" + 
				"      h1   {color: #00331199;}\n" + 
				"    </style>\n" + 
				"  </head>\n" + 
				"  <body>\n" + 
				"    <h1 style=\"font-size: 150%;\">Lorem Ipsum</h1>\n" + 
				"  </body>\n" + 
				"</html>";
		//@formatter:on
		List<Operation> ops1 = getOperations(true, html);
		assertEquals(153, ops1.get(1).getContext().getColor().getAlpha());

		List<Operation> ops2 = getOperations(false, html);
		assertEquals(255, ops2.get(1).getContext().getColor().getAlpha());
	}

	/**
	 * Test a text-shadow for an h1 tag.
	 */
	public void test_text_shadow_h1_internalSheet() throws Exception {
		//@formatter:off
		String html = "<html>\n" + 
				"  <head>\n" + 
				"    <style>\n" + 
				"      h1   {color:darkorchid; text-shadow: 2px 2px 4px plum;}\n" + 
				"    </style>\n" + 
				"  </head>\n" + 
				"  <body>\n" + 
				"    <h1 style=\"font-size: 150%;\">Lorem Ipsum</h1>\n" + 
				"  </body>\n" + 
				"</html>";
		//@formatter:on

		testTextShadow(html);
	}

	/**
	 * Test a text-shadow for an h3 tag.
	 */
	public void test_text_shadow_h3_inline() throws Exception {
		//@formatter:off
		String html = "<html>\n" + 
				"  <h3 style=\"color:darkorchid; text-shadow: 2px 2px 4px plum;font-size: 150%;\">Lorem Ipsum</h3>\n" + 
				"</html>";
		//@formatter:on

		testTextShadow(html);
	}

	/**
	 * Test a text-shadow for a span tag.
	 */
	public void test_text_shadow_span_inline() throws Exception {
		//@formatter:off
		String html = "<html>\n" + 
				"  <span style=\"color:darkorchid; text-shadow: 2px 2px 4px plum;font-size: 150%;\">Lorem Ipsum</span>\n" + 
				"</html>";
		//@formatter:on

		testTextShadow(html);
	}

	/**
	 * This asserts that HTML produces exactly 3 layers: a background, an image
	 * (the shadow), and text
	 */
	private void testTextShadow(String html) {
		List<Operation> ops1 = getOperations(true, html);

		assertEquals(3, ops1.size());

		// background:
		assertTrue(ops1.get(0) instanceof FillOperation);

		// shadow:
		assertTrue(ops1.get(1) instanceof ImageOperation);

		// text:
		assertTrue(ops1.get(2) instanceof StringOperation);

		List<Operation> ops2 = getOperations(false, html);

		assertEquals(2, ops2.size());

		// background:
		assertTrue(ops2.get(0) instanceof FillOperation);

		// text:
		assertTrue(ops2.get(1) instanceof StringOperation);
	}

	private List<Operation> getOperations(boolean useQHTMLKit, String html) {

		HTMLEditorKit kit = useQHTMLKit ? new QHTMLEditorKit()
				: new HTMLEditorKit();

		JEditorPane p = new JEditorPane();
		p.setEditorKit(kit);
		p.setText(html);

		p.setSize(new Dimension(1000, 1000));

		VectorImage vi = new VectorImage();
		VectorGraphics2D vig = vi.createGraphics();
		p.paint(vig);
		vig.dispose();

		return vi.getOperations();
	}

}