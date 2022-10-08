package org.eddie.tools.geocaching.common;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;

public final class ByteArrayUtils{
	
	private ByteArrayUtils() {
		//no instance
	}
	
	public static String arrayToString(Object array) {
		StringBuilder sb = new StringBuilder();
		toString(sb,array);
		return sb.toString();
	}
	
	private static void toString(StringBuilder sb, Object array) {
		if (array==null) {
			sb.append("<null>");
			return;
		}
		if (array.getClass().isArray()) {
			sb.append("[");
			boolean first = false;
			int l = Array.getLength(array);
			for(int i=0;i<l;i++) {
				if (!first) {
					sb.append(",");
				}
				first=false;
				toString(sb,Array.get(array, i));				
			}
			sb.append("]");
			return;
		}
		
		sb.append(String.valueOf(array));		
	}
	
	public static byte[] readFile(String filename) {
		File file = new File(filename);
		BufferedInputStream bis = null;
		byte[] result = new byte[(int)file.length()];
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[1000];
			int cnt = 0, pos=0;
			while ((cnt = bis.read(buffer))>0) {
				System.arraycopy(buffer, 0, result, pos, cnt);
				pos+=cnt;
			}
		}
		catch(IOException ie) {
			throw new RuntimeException("Error trying to read file '"+filename+"'",ie);
		}
		finally {
			safeClose(bis);
		}
		return result;
	}
	
	public static void writeFile(String filename, byte[] content) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(filename);
			os.write(content);
		}
		catch(IOException ie) {
			throw new RuntimeException("Error trying to read file '"+filename+"'",ie);
			
		}
		finally {
			safeClose(os);
		}
	}
	
	public static byte[] clone(byte[] content) {
		if (content==null) {
			return null;
		}
		byte[] c = new byte[content.length];
		System.arraycopy(content, 0, c, 0, content.length);
		return c;		
	}
	
	public static byte[] part(byte[] buffer, int startPos, int length) {
		byte[] part = new byte[length];
		System.arraycopy(buffer, startPos, part, 0, length);
		return part;
	}
	
	public static long byteToLong(byte[] buffer, int startPos, int length, boolean littleEndian) {
		long result = 0;
		long multiplier = 1;
		for(int i=0;i<length;i++) {
			int pos = littleEndian?i+startPos:startPos+length-1-i;
			short s = buffer[pos];
			if (s<0) {
				s+=256;
			}
			result+=s*multiplier;
			multiplier=multiplier*256;
		}
		return result;		
	}
	
	public static String toString(byte[] ba) {
		if (ba==null) {
			return "null";
		}
		StringBuilder sb = new StringBuilder("[");
		boolean first =true;
		for(int i=0;i<ba.length;i++) {
			if (!first) {
				sb.append(";");
			}
			first = false;
			short s = ba[i];
			if (s<0) {
				s+=256;
			}
			sb.append(s);
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static void safeClose(Closeable c) {
		if (c!=null) {
			try {
				c.close();
			}
			catch(Exception e) {
				//do nothing
			}
		}		
	}

}
