package org.eddie.tools.geocaching.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class ReaderIterable implements Iterable<String> {
	
	private Reader reader;
	
	public ReaderIterable(File file) {
		try {
			reader = new FileReader(file);
		}
		catch(IOException ioe) {
			handleException(ioe);
		}
	}
	
	private static void handleException(Exception e) {
		if (e instanceof RuntimeException) {
			throw (RuntimeException)e;
		}
		throw new RuntimeException("New Exception",e);
	}
	

	public Iterator<String> iterator() {
		return new ReaderIterator(reader);
	}
	
	private static class ReaderIterator implements Iterator<String> {
		
		private BufferedReader reader;
		private String nextLine;
		
		public ReaderIterator(Reader reader) {
			this.reader = new BufferedReader(reader);
			nextLineToBuffer();
		}

		public boolean hasNext() {
			return nextLine!=null;
		}

		public String next() {
			String currLine = nextLine;
			nextLineToBuffer();
			return currLine;
		}
		
		private void nextLineToBuffer() {
			try {
				nextLine = reader.readLine();
				if (nextLine==null) {
					ByteArrayUtils.safeClose(reader);
				}
			}
			catch(IOException ioe) {
				ByteArrayUtils.safeClose(reader);
				handleException(ioe);
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
			
		}
		
	}
	
	

}
