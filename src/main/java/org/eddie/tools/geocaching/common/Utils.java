package org.eddie.tools.geocaching.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Utils {
	
	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
	
	private Utils() {
		//no instance
	}

	public static boolean safeEquals(Object o1, Object o2) {
		if (o1==o2) {
			return true;
		}
		if (o1==null || o2==null) {
			return false;
		}
		return o1.equals(o2);

	}
	
	public static int byteToInt(String byteStr) {
		return byteToInt(byteStr, c -> c.charValue()=='1');
	}
	
	public static int byteToInt(String byteStr, Function<Character,Boolean> decider) {
		int result = 0;
		for(char c : byteStr.toCharArray()) {
			result = result*2+ (decider.apply(c)?1:0);
		}
		return result;
	}
	
	public static void safeClose(Closeable c) {
		if (c!=null) {
			try {
				c.close();
			}
			catch(Exception e) {
				LOG.error("Problem closing closeable",e);
			}
		}
	}
	
	public static String reverse(String text) {
		char[] asArray = text.toCharArray();
		char[] target = new char[text.length()];
		for(int i=0;i<target.length;i++) {
			target[i] = asArray[target.length-i-1];
		}
		return new String(target);
	}

	public static <T> Map<T,Integer> addToMap(Map<T,Integer> map, T key, int count) {
		if (map.containsKey(key)) {
			map.put(key, map.get(key)+count);
		}
		else {
			map.put(key, count);
		}
		return map;
	}
	
	public static <T> int safeGetMap(Map<T,Integer> map, T key) {
		Integer e = map.get(key);
		if (e==null) {
			return 0;
		}
		return e;		
	}
	
	@SuppressWarnings("unchecked")
	public static <K,V> Map<K,V> toMap(Map<K,V> map, Object ...o) {
		for(int i=0;i<o.length/2;i++) {
			map.put((K)o[i*2], (V)o[i*2+1]);
		}
		return map;
	}
	
	public static Character[] toCharArray(String str) {
		Character[] result = new Character[str.length()];
		int i = 0;
		for(char c : str.toCharArray()) {
			result[i] = c;
			i++;
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Set<T> toSet(T[] array) {
		return new HashSet(Arrays.asList(array));
	}
	
	public static BigInteger pow(int a, int b) {
		BigInteger result = BigInteger.ONE;
		BigInteger ba = BigInteger.valueOf(a);
		for(int i=0;i<b;i++) {
			result = result.multiply(ba);
		}
		return result;
	}
	
	public static int powInt(int a, int b) {
		return pow(a,b).intValue();
	}
	
	public static int digit(BigInteger number, int pos) {
		String bdStr = number.toString();
		if (Math.abs(pos)>bdStr.length()) {
			throw new IllegalArgumentException("number '"+number+"' has no pos '"+pos+"'");
		}
		
		
		if (pos<0) {
			return Integer.valueOf(bdStr.substring(bdStr.length()+pos,bdStr.length()+pos+1));
		}
		else {
			return Integer.valueOf(bdStr.substring(pos-1,pos));
		}
	}
	
	public static String lpad(Object str, int length, char padChar) {
		String newStr = String.valueOf(str);
		while (newStr.length()<length) {
			newStr = padChar+newStr;
		}
		return newStr;
	}

	public static void writeFiltered(String text, String regex, Function<String,String> formatter, Writer target) {
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(target);
				String html = text;
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(html);
				while (m.find()) {
					String word = m.group(1);
					word = (formatter==null?word:formatter.apply(word));
					bw.write(word);
					bw.write("\n");
				}
			}
			catch(Exception ex) {
				throw new RuntimeException(ex);
			}
			finally {
				Utils.safeClose(bw);
			}


	}
	
	public static void stringToFile(String text, String filePath) {
		List<String> content = new ArrayList<>();
		content.add(text);
		listToFile(content,filePath,s -> s);
	}
	
	public static <T> void listToFile(List<T> list, String filePath, Function<T,String> itemMapper) {
		File file = new File(filePath);
		BufferedWriter fw= null;
		try {
			Files.createDirectories(Paths.get(file.getParentFile().getAbsolutePath()));
			fw = new BufferedWriter(new FileWriter(file));
			for(T element : list) {
				String line = itemMapper.apply(element);
				fw.write(line);
				fw.write("\n");
			}
		}
		catch(IOException io) {
			LOG.warn("Problem during file write to "+file.getAbsolutePath(),io);
		}
		finally {
			try {
				fw.close();
			} catch (IOException e) {
				LOG.debug("Problem during close");
			}
		}
	}
	
	public static <T> List<T> stringToList(String s, Function<String,T> mapper, String sep, T defaultValue) {
		List<T> result = new ArrayList<>();
		for(String p : s.split(sep)) {
			try {
				result.add(mapper.apply(p));
			}
			catch(Exception e) {
				result.add(defaultValue);
			}
		}
		return result;
	}
	
	
	public static <T> String listToString(Collection<T> list, Function<T,String> mapper, String sep, String defaultValue) {
		StringBuilder sb = new StringBuilder();
		boolean first=true;
		for(T e : list) {
			if (!first) {
				sb.append(sep);
			}
			first=false;
			try {
				sb.append(mapper.apply(e));
			}
			catch(Exception ee) {
				sb.append(defaultValue);
			}
		}
		return sb.toString();
	}
	
	public static <T> List<T> fileToList(String filePath, Function<String,T> lineMapper) {
		File file = new File(filePath);
		if (!file.isFile()) {
			return Collections.emptyList();
		}
		BufferedReader br = null;
		List<T> result = new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine())!=null) {
				result.add(lineMapper.apply(line));
			}
			return result;
		}
		catch(IOException io) {
			LOG.warn("Problem during file read to "+file.getAbsolutePath(),io);
		}
		finally {
			try {
				br.close();
			} catch (IOException e) {
				LOG.debug("Problem during close");
			}
		}
		return Collections.emptyList();
	}
	
	public static String fileToString(String filePath) {
		StringBuilder text = new StringBuilder();
		List<String> content = fileToList(filePath, s -> s);
		for(String line : content) {
			text.append(line).append("\n");
		}
		return text.toString();
	}

	
}
