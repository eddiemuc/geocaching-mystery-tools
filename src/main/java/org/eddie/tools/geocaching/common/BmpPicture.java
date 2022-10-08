package org.eddie.tools.geocaching.common;

/**
 * This class can access and modify paletted BMP pictures with 8bit index. Nothing else...
 * Format see https://de.wikipedia.org/wiki/Windows_Bitmap
 *
 */
public class BmpPicture {
	
	byte[] header;
	byte[] infoBlock;
	
	int width;
	int widthInMemory;
	int heigth;
	
	byte[][] colorTable;
	
	byte[] pictureData;
	
	public int getWidth() {
		return width;
	}
	
	public int getHeigth() {
		return heigth;
	}
	
	
	private static void check(long found, int expected, String message) {
		if (found!=expected) {
			throw new RuntimeException("Cannot read this data: "+message+" (Exp: "+expected+", found: "+found+")");
		}
	}
	
	public static BmpPicture fromFile(String filename) {
		byte[] raw = ByteArrayUtils.readFile(filename);
		return new BmpPicture(raw);		
	}
	
	public static BmpPicture fromArray(byte[] raw) {
		return new BmpPicture(raw);
	}
	
	private BmpPicture(byte[] raw) {
				
		check(ByteArrayUtils.byteToLong(raw, 0, 2, true),19778,"Bitmap marker missing");
		check(ByteArrayUtils.byteToLong(raw, 10, 4, true),54+256*4,"Bitmap needs a palette");
		check(ByteArrayUtils.byteToLong(raw, 28, 2, true),8,"Can only work with BMW of 8bit");
		check(ByteArrayUtils.byteToLong(raw, 30, 4, true),0,"Compression method must be BI_RGB");
//		check(ByteArrayUtils.byteToLong(raw, 46, 4, true),256 ,"Must have 256 palette entries");
		int lWidth = (int)ByteArrayUtils.byteToLong(raw, 18, 4, true);
		int lWidthInMemory = lWidth;
		while (lWidthInMemory%4!=0) {
			lWidthInMemory++;
		}
		int lHeigth = (int)ByteArrayUtils.byteToLong(raw, 22, 4, true);
		check(raw.length,54+4*256+lWidthInMemory*lHeigth,"Data length not matching (FOund w="+lWidth+", h="+lHeigth+")");
		
		
		//read header
		header = ByteArrayUtils.part(raw, 0,14);
		infoBlock = ByteArrayUtils.part(raw, 14, 40);
		
		width = lWidth;
		heigth = lHeigth;
		widthInMemory = lWidthInMemory;
		
		
		colorTable = new byte[256][3];
		for(int i=0;i<255;i++) {
			for(int j=0;j<3;j++) {
				colorTable[i][j] = raw[54+i*4+j];
			}			
		}
		pictureData = ByteArrayUtils.part(raw, 54+256*4, widthInMemory*heigth);
		
	}
	
	public byte[] toArray() {
		
		byte[] result = new byte[54+256*4+widthInMemory*heigth];
		System.arraycopy(this.header,0, result, 0, this.header.length);
		System.arraycopy(this.infoBlock, 0,result, header.length, this.infoBlock.length);
		for(int i=0;i<256;i++) {
			for(int j=0;j<3;j++) {
				result[54+i*4+j] = colorTable[i][j];
			}
			result[54+i*4+3] = 0;
		}
		System.arraycopy(this.pictureData, 0, result, 54+256*4, this.pictureData.length);
		return result;
	}
	
	public BmpPicture saveToFile(String filename) {
		ByteArrayUtils.writeFile(filename, toArray());
		return this;
		
	}
	
	public BmpPicture clone() {
		return new BmpPicture(toArray());		
	}
	
	public BmpPicture setPalette(int index, byte r, byte g, byte b) {
		colorTable[index][0] = r;
		colorTable[index][1] = g;
		colorTable[index][3] = b;
		return this;
	}
	public BmpPicture setPalette(int index, byte[] rgb) {
		colorTable[index][0] = rgb[0];
		colorTable[index][1] = rgb[1];
		colorTable[index][2] = rgb[2];
		return this;
	}
	
	public static byte[] toColor(byte r, byte g, byte b) {
		return new byte[]{r,g,b};
	}
	
	public byte[] getPalette(int index) {
		return colorTable[index];		
	}
	
	public int getPoint(int x, int y) {
		return toInt(this.pictureData[y*this.widthInMemory+x]);
	}
	
	public BmpPicture setPoint(int x, int y, int paletteColor) {
		this.pictureData[y*this.widthInMemory+x] = toByte(paletteColor);
		return this;
	}
	
	private static int toInt(byte b) {
		int i = b;
		if (i<0) {
			i+=256;
		}
		return i;
	}
	
	private static byte toByte(int i) {
		return (byte)i;
	}

}
