package org.eddie.tools.geocaching.common;

/**
 * This class can access and modify GIF pictures with 8bit index. Nothing else...
 * It can only manipulate and get palette colors for now...
 *
 */
public class GifPicture {
	
	private byte[] wholedata;
	private static final int PALETTE_START_POS = 6+7;
	
	public int getWidth() {
		return (int)ByteArrayUtils.byteToLong(this.wholedata,6,2,true);
	}
	
	public int getHeigth() {
		return (int)ByteArrayUtils.byteToLong(this.wholedata,8,2,true);
	}
	
	
	public static GifPicture fromFile(String filename) {
		byte[] raw = ByteArrayUtils.readFile(filename);
		return new GifPicture(raw);		
	}
	
	public static GifPicture fromArray(byte[] raw) {
		return new GifPicture(raw);
	}
	
	private GifPicture(byte[] raw) {		
		this.wholedata = raw;	
	}
	
	public byte[] toArray() {
		return this.wholedata;
	}
	
	public GifPicture saveToFile(String filename) {
		ByteArrayUtils.writeFile(filename, toArray());
		return this;		
	}
	
	public GifPicture clone() {
		return new GifPicture(toArray());		
	}
	
	public GifPicture setPalette(int index, byte[] rgb) {
		this.wholedata[index*3+PALETTE_START_POS] = rgb[0];
		this.wholedata[index*3+PALETTE_START_POS+1] = rgb[1];
		this.wholedata[index*3+PALETTE_START_POS+2] = rgb[2];
		return this;
	}
	
	public static byte[] toColor(byte r, byte g, byte b) {
		return new byte[]{r,g,b};
	}
	
	public byte[] getPalette(int index) {
		byte[] result = new byte[3];
		System.arraycopy(this.wholedata, index*3+PALETTE_START_POS, result, 0, 3);
		return result;		
	}
}
