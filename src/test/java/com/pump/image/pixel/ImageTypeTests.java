/**
 * This software is released as part of the Pumpernickel project.
 * 
 * All com.pump resources in the Pumpernickel project are distributed under the
 * MIT License:
 * https://github.com/mickleness/pumpernickel/raw/master/License.txt
 * 
 * More information about the Pumpernickel project is available here:
 * https://mickleness.github.io/pumpernickel/
 */
package com.pump.image.pixel;

import java.util.Arrays;

import org.junit.Test;

import com.pump.image.pixel.converter.IntPixelConverter;

import junit.framework.TestCase;

/**
 * Unit tests related to ImageType conversions.
 */
public class ImageTypeTests extends TestCase {

	/**
	 * Our original conversion from ARGB_PRE to ARGB just divided each color
	 * channel by the alpha. But in this case that results in "0x100", and not
	 * "0xff". So we had to modify the conversion to use "Math.min(0xff,
	 * component / alpha)"
	 * <p>
	 * This problem was observed with some NSImages, such as "IconLocked".
	 */
	@Test
	public void testARGBPreDivision_small() {
		int[] pixel = new int[] { 0xc3c3c3c3 };
		ImageType.INT_ARGB.convertFromARGBPre(pixel, 1);
		assertEquals(0xc3ffffff, pixel[0]);
	}

	/**
	 * This is an extension of the test above that uses several dozen pixels
	 * instead of one.
	 */
	@Test
	public void testARGBPreDivision_medium() {
		// @formatter:off
		int[][] in = new int[][] {
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1010101, 0x2020202, 0x2020202, 0x2020202, 0x1010101, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x3030303, 0x12121212, 0x1c1c1c1c, 0x1f1f1f1f, 0x1c1c1c1c, 0x15151515, 0x6060606, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xa0a0a0a, 0x25252525, 0x41414141, 0x5c5b5c5b, 0x74717272, 0x81808080, 0x83838383, 0x81818181, 0x77777777, 0x61616161, 0x42424242, 0x28272727, 0xf0f0f0f, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x3030303, 0x14131313, 0x42414141, 0x8a8a8a8a, 0xc3c3c3c3, 0xe4e3e3e3, 0xf1eceeef, 0xf4f2f4f4, 0xf6f5f5f6, 0xf5f4f5f5, 0xf2f1f2f2, 0xe8e5e7e8, 0xcac9caca, 0x92929292, 0x4e4e4e4e, 0x15141414, 0x4030303, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1010101, 0x2f2f2e2e, 0x7d77797a, 0xc4babcbe, 0xe4e3e4e4, 0xf8f8f8f8, 0xfffafdff, 0xffecf0f4, 0xffe1e6ea, 0xffdee2e6, 0xffe0e5e9, 0xffe8eef1, 0xfff5f9fb, 0xfbfafbfb, 0xe8e8e8e8, 0xc5c5c5c5, 0x8b8b8b8b, 0x37373737, 0x2020201, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x7070707, 0x33333333, 0x8c8b8c8c, 0xdfd9dcdc, 0xfffeffff, 0xfff7f9fa, 0xffe3e7e9, 0xffc9cfd3, 0xf3aab2b9, 0xe5929aa2, 0xe0878f97, 0xe48c949c, 0xf09fa7ae, 0xfebdc3c7, 0xffdde1e2, 0xfff5f8f9, 0xffffffff, 0xeaeae9e9, 0x9b9a9b9b, 0x423f4242, 0x9080909, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x33323232, 0x97959596, 0xe8e5e7e7, 0xffffffff, 0xffeff3f5, 0xfbbdc5cb, 0xf087929b, 0xdb5a6974, 0xb542515b, 0x9636434c, 0x89303c45, 0x932d3b46, 0xaf33424d, 0xd245555f, 0xee69777f, 0xfba9b2b8, 0xffdee2e6, 0xfefafbfb, 0xf4f2f4f4, 0xb0a9afb0, 0x33323333, 0x5050505, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x3030303, 0x1d1d1d1d, 0x7d777879, 0xe6d9dddf, 0xfffdffff, 0xffeaeef1, 0xfdb0b9bf, 0xd278848e, 0x8d3f4c5b, 0x50111e2e, 0x35071016, 0x27060b0e, 0x24050a0e, 0x2701090d, 0x2f00070b, 0x48051117, 0x7f1e2f3c, 0xce51606c, 0xff98a1a8, 0xffd5dadf, 0xfffbfcfe, 0xe8e8e8e8, 0x94949393, 0x2b2b2b2b, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xb0a0a0b, 0x5f56595c, 0xc7bbbfc2, 0xfffbfdfd, 0xffedf2f5, 0xf7b7c2c8, 0xcb6b7b86, 0x74353f48, 0x2910161f, 0x4000204, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1000001, 0x1f020e14, 0x6b051526, 0xbe3d4a56, 0xf6949da5, 0xffe0e3e8, 0xfefcfdfe, 0xe2dddfe0, 0x68656666, 0xa0a0a0a, 0x1010101, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1e171819, 0xa48a9299, 0xf6e7edf2, 0xfffbfcfd, 0xffc8d0d6, 0xcb809099, 0x68415057, 0x160d1012, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x2000101, 0xc000005, 0x510c1621, 0xce4a5964, 0xfbb1b9c0, 0xffecf2f6, 0xfbf5f9fa, 0xa4a0a2a3, 0x39393939, 0x7070707, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xb0b0b0a, 0x46434343, 0xcab1b7bc, 0xfff2f9ff, 0xfeebf0f5, 0xeeabb7c1, 0x884f5b64, 0x170f1214, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1b01090d, 0x78253842, 0xd77a8792, 0xffd0d6dc, 0xfdfcfcfd, 0xd3d2d3d3, 0x706e7070, 0xd0d0d0d, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x201d1f20, 0x756d7275, 0xdeccd3d8, 0xfff4fcff, 0xf6d6dfe6, 0xba939ba2, 0x4c383c3f, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x4010203, 0x2d0f191f, 0xa84f5b65, 0xfeb3bbc2, 0xfff7f9fb, 0xf2ebf0f2, 0x9c909698, 0x19171818, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
		};
		int[][] expected = new int[][] {
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1ffffff, 0x2ffffff, 0x2ffffff, 0x2ffffff, 0x1ffffff, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x3ffffff, 0x12ffffff, 0x1cffffff, 0x1fffffff, 0x1cffffff, 0x15ffffff, 0x6ffffff, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xaffffff, 0x25ffffff, 0x41ffffff, 0x5cfdfffd, 0x74f9fbfb, 0x81fefefe, 0x83ffffff, 0x81ffffff, 0x77ffffff, 0x61ffffff, 0x42ffffff, 0x28f9f9f9, 0xfffffff, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x3ffffff, 0x14f3f3f3, 0x42fcfcfc, 0x8affffff, 0xc3ffffff, 0xe4fefefe, 0xf1fafcfd, 0xf4fdffff, 0xf6fefeff, 0xf5feffff, 0xf2feffff, 0xe8fcfeff, 0xcafeffff, 0x92ffffff, 0x4effffff, 0x15f3f3f3, 0x4c0c0c0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1ffffff, 0x2ffffafa, 0x7df3f7f9, 0xc4f2f5f8, 0xe4feffff, 0xf8ffffff, 0xfffafdff, 0xffecf0f4, 0xffe1e6ea, 0xffdee2e6, 0xffe0e5e9, 0xffe8eef1, 0xfff5f9fb, 0xfbfeffff, 0xe8ffffff, 0xc5ffffff, 0x8bffffff, 0x37ffffff, 0x2ffff80, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x7ffffff, 0x33ffffff, 0x8cfeffff, 0xdff9fcfc, 0xfffeffff, 0xfff7f9fa, 0xffe3e7e9, 0xffc9cfd3, 0xf3b3bbc2, 0xe5a3acb5, 0xe09aa3ac, 0xe49da6af, 0xf0a9b2b9, 0xfebec4c8, 0xffdde1e2, 0xfff5f8f9, 0xffffffff, 0xeafffefe, 0x9bfeffff, 0x42f4ffff, 0x9e3ffff, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x33fafafa, 0x97fcfcfe, 0xe8fcfefe, 0xffffffff, 0xffeff3f5, 0xfbc0c8cf, 0xf0909ba5, 0xdb697a87, 0xb55d7280, 0x965c7281, 0x89597080, 0x934e6679, 0xaf4a6070, 0xd2546773, 0xee708088, 0xfbacb5bb, 0xffdee2e6, 0xfefbfcfc, 0xf4fdffff, 0xb0f5feff, 0x33faffff, 0x5ffffff, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x3ffffff, 0x1dffffff, 0x7df3f5f7, 0xe6f1f5f8, 0xfffdffff, 0xffeaeef1, 0xfdb2bbc1, 0xd292a0ad, 0x8d7289a5, 0x50366093, 0x35214d6a, 0x2727485b, 0x24234763, 0x27063b55, 0x2f00263b, 0x48113c51, 0x7f3c5e78, 0xce647786, 0xff98a1a8, 0xffd5dadf, 0xfffbfcfe, 0xe8ffffff, 0x94fffefe, 0x2bffffff, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xbe8e8ff, 0x5fe7eff7, 0xc7f0f5f9, 0xfffbfdfd, 0xffedf2f5, 0xf7bdc9cf, 0xcb869ba8, 0x74748b9e, 0x296389c1, 0x40080ff, 0x0, 0x0, 0x0, 0x0, 0x0, 0x10000ff, 0x1f1073a5, 0x6b0b325a, 0xbe526373, 0xf69aa3ab, 0xffe0e3e8, 0xfefdfeff, 0xe2fafcfd, 0x68f8fbfb, 0xaffffff, 0x1ffffff, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1ec4ccd5, 0xa4d7e3ee, 0xf6f0f6fb, 0xfffbfcfd, 0xffc8d0d6, 0xcba1b5c0, 0x68a0c4d6, 0x1697bad1, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x2008080, 0xc00006a, 0x51254568, 0xce5b6e7c, 0xfbb4bcc3, 0xffecf2f6, 0xfbf9fdfe, 0xa4f9fcfe, 0x39ffffff, 0x7ffffff, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xbffffe8, 0x46f5f5f5, 0xcae0e7ee, 0xfff2f9ff, 0xfeecf1f6, 0xeeb7c4cf, 0x8894abbc, 0x17a6c8de, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1b09557b, 0x784e778c, 0xd791a0ad, 0xffd0d6dc, 0xfdfefeff, 0xd3feffff, 0x70fbffff, 0xdffffff, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, 
			{ 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x20e8f8ff, 0x75eef9ff, 0xdeebf3f9, 0xfff4fcff, 0xf6dee8ef, 0xbacad5de, 0x4cbccad4, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x44080c0, 0x2d558eb0, 0xa8788a99, 0xfeb4bcc3, 0xfff7f9fb, 0xf2f8fdff, 0x9cecf6f9, 0x19ebf5f5, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0} 
		};
		// @on

		for (int a = 0; a < in.length; a++) {
			ImageType.INT_ARGB.convertFromARGBPre(in[a], in[a].length);
			assertEquals(null, expected[a], in[a]);
		}
	}
	
	/**
	 * Convert pixel data into several ARGB formats and back into an INT_ARGB format.
	 */
	public void testARGBConversions() {
		int[] intArray = new int[] { 0xe6f1f5f8, 0xfffdffff, 0xffeaeef1, 0xfdb2bbc1, 0xd292a0ad, 0x8d7289a5, 0x50366093, 0x35214d6a, 0x2727485b };
		
		testConversions(ImageType.INT_ARGB, intArray, ImageType.BYTE_ABGR);
		testConversions(ImageType.INT_ARGB, intArray, ImageType.BYTE_ARGB);
		testConversions(ImageType.INT_ARGB, intArray, ImageType.BYTE_BGRA);
		testConversions(ImageType.INT_ARGB, intArray, ImageType.BYTE_ABGR, ImageType.BYTE_ARGB, ImageType.BYTE_BGRA);
		testConversions(ImageType.INT_ARGB, intArray, ImageType.BYTE_ARGB, ImageType.BYTE_ABGR, ImageType.BYTE_BGRA);
		testConversions(ImageType.INT_ARGB, intArray, ImageType.BYTE_ABGR, ImageType.BYTE_BGRA, ImageType.BYTE_ARGB);
		testConversions(ImageType.INT_ARGB, intArray, ImageType.BYTE_ARGB, ImageType.BYTE_BGRA, ImageType.BYTE_ABGR);
		testConversions(ImageType.INT_ARGB, intArray, ImageType.BYTE_BGRA, ImageType.BYTE_ABGR, ImageType.BYTE_ARGB);
		testConversions(ImageType.INT_ARGB, intArray, ImageType.BYTE_BGRA, ImageType.BYTE_ARGB, ImageType.BYTE_ABGR);
	}
	
	private void testConversions(ImageTypeInt inputType, int[] input, ImageType... types) {
		PixelIterator<?> iter = new BufferedIntPixelIterator(input, input.length, 1, 0, input.length, inputType.code);
		iter = types[0].createConverter(iter);
		for (int a = 1; a < types.length; a++) {
			iter = types[a].createConverter(iter);
		}
		IntPixelConverter lastIter = inputType.createConverter(iter);
		
		int[] actual = new int[input.length];
		lastIter.next(actual);
		
		assertEquals(Arrays.asList(types).toString(), input, actual);
	}

	
	/**
	 * Convert pixel data into several RGB formats and back into an INT_RGB format.
	 */
	public void testRGBConversions() {
		int[] intArray = new int[] { 0xe1f5f8, 0xfdffff, 0xeaeef1, 0xb2bbc1, 0x92a0ad, 0x7289a5, 0x366093, 0x214d6a, 0x27485b };
		
		testConversions(ImageType.INT_RGB, intArray, ImageType.BYTE_BGR);
		testConversions(ImageType.INT_RGB, intArray, ImageType.BYTE_RGB);
		testConversions(ImageType.INT_RGB, intArray, ImageType.INT_RGB);
		testConversions(ImageType.INT_RGB, intArray, ImageType.INT_BGR);
		
		testConversions(ImageType.INT_RGB, intArray, ImageType.BYTE_BGR, ImageType.BYTE_RGB, ImageType.INT_RGB, ImageType.INT_BGR);
		testConversions(ImageType.INT_RGB, intArray, ImageType.BYTE_BGR, ImageType.BYTE_RGB, ImageType.INT_BGR, ImageType.INT_RGB);
		testConversions(ImageType.INT_RGB, intArray, ImageType.BYTE_BGR, ImageType.INT_RGB, ImageType.BYTE_RGB, ImageType.INT_BGR);
		testConversions(ImageType.INT_RGB, intArray, ImageType.BYTE_BGR, ImageType.INT_RGB, ImageType.INT_BGR, ImageType.BYTE_RGB);
		testConversions(ImageType.INT_RGB, intArray, ImageType.BYTE_BGR, ImageType.INT_BGR, ImageType.BYTE_RGB, ImageType.INT_RGB);
		testConversions(ImageType.INT_RGB, intArray, ImageType.BYTE_BGR, ImageType.INT_BGR, ImageType.INT_RGB, ImageType.BYTE_RGB);

		testConversions(ImageType.INT_RGB, intArray, ImageType.BYTE_RGB, ImageType.BYTE_BGR, ImageType.INT_RGB, ImageType.INT_BGR);
		testConversions(ImageType.INT_RGB, intArray, ImageType.BYTE_RGB, ImageType.BYTE_BGR, ImageType.INT_BGR, ImageType.INT_RGB);
		testConversions(ImageType.INT_RGB, intArray, ImageType.BYTE_RGB, ImageType.INT_RGB, ImageType.BYTE_BGR, ImageType.INT_BGR);
		testConversions(ImageType.INT_RGB, intArray, ImageType.BYTE_RGB, ImageType.INT_RGB, ImageType.INT_BGR, ImageType.BYTE_BGR);
		testConversions(ImageType.INT_RGB, intArray, ImageType.BYTE_RGB, ImageType.INT_BGR, ImageType.BYTE_BGR, ImageType.INT_RGB);
		testConversions(ImageType.INT_RGB, intArray, ImageType.BYTE_RGB, ImageType.INT_BGR, ImageType.INT_RGB, ImageType.BYTE_BGR);

		testConversions(ImageType.INT_RGB, intArray, ImageType.INT_RGB, ImageType.BYTE_RGB, ImageType.BYTE_BGR, ImageType.INT_BGR);
		testConversions(ImageType.INT_RGB, intArray, ImageType.INT_RGB, ImageType.BYTE_RGB, ImageType.INT_BGR, ImageType.BYTE_BGR);
		testConversions(ImageType.INT_RGB, intArray, ImageType.INT_RGB, ImageType.BYTE_BGR, ImageType.BYTE_RGB, ImageType.INT_BGR);
		testConversions(ImageType.INT_RGB, intArray, ImageType.INT_RGB, ImageType.BYTE_BGR, ImageType.INT_BGR, ImageType.BYTE_RGB);
		testConversions(ImageType.INT_RGB, intArray, ImageType.INT_RGB, ImageType.INT_BGR, ImageType.BYTE_RGB, ImageType.BYTE_BGR);
		testConversions(ImageType.INT_RGB, intArray, ImageType.INT_RGB, ImageType.INT_BGR, ImageType.BYTE_BGR, ImageType.BYTE_RGB);

		testConversions(ImageType.INT_RGB, intArray, ImageType.INT_BGR, ImageType.BYTE_RGB, ImageType.INT_RGB, ImageType.BYTE_BGR);
		testConversions(ImageType.INT_RGB, intArray, ImageType.INT_BGR, ImageType.BYTE_RGB, ImageType.BYTE_BGR, ImageType.INT_RGB);
		testConversions(ImageType.INT_RGB, intArray, ImageType.INT_BGR, ImageType.INT_RGB, ImageType.BYTE_RGB, ImageType.BYTE_BGR);
		testConversions(ImageType.INT_RGB, intArray, ImageType.INT_BGR, ImageType.INT_RGB, ImageType.BYTE_BGR, ImageType.BYTE_RGB);
		testConversions(ImageType.INT_RGB, intArray, ImageType.INT_BGR, ImageType.BYTE_BGR, ImageType.BYTE_RGB, ImageType.INT_RGB);
		testConversions(ImageType.INT_RGB, intArray, ImageType.INT_BGR, ImageType.BYTE_BGR, ImageType.INT_RGB, ImageType.BYTE_RGB);
	}
	
	
	void assertEquals(String msg, int[] expectedArray, int[] actualArray) {
		for (int a = 0; a < expectedArray.length; a++) {
			String str = msg == null ? "" : msg;
			str = str + " " + Integer.toHexString(expectedArray[a])+", "+Integer.toHexString(actualArray[a]);
			str = str.trim();
			
			assertEquals(str, expectedArray[a], actualArray[a]);
		}
	}
}