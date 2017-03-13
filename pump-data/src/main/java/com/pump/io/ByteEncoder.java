package com.pump.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/** This object encodes a series of bytes (expressed as [0,255] integers).
 * <p>This data can be made available either in a dual-threaded push/pull model,
 * or in a single-threaded model by using the inner DataListener interface.
 */
public abstract class ByteEncoder implements AutoCloseable {

	/** This is notified when data is available or when an encoder is closed.
	 * This is is intended for single-threaded encoding.
	 */
	public static interface DataListener {
		public void chunkAvailable(ByteEncoder encoder) throws IOException;
		public void encoderClosed(ByteEncoder encoder) throws IOException;
	}
	
	protected boolean closed = false;
	private int waiting = 0;
	DataListener listener = null;

	/** Assign a DataListener for this encoder.
	 * 
	 * @param listener the listener to assign. This replaces any
	 * previously assigned listener.
	 */
	public synchronized void setListener(DataListener listener) {
		this.listener = listener;
	}
	
	/** This indicates that no more data will be made available.
	 * <p>Subsequent calls to <code>push()</code> will result in an <code>IllegalStateException</code>.
	 * Calling this method may immediately release new data (which you can access by calling <code>pullImmediately</code>).
	 * 
	 * @see #push(int)
	 * @see #pull()
	 */
	@Override
	public synchronized void close() throws IOException {
		if(closed)
			return;
		
		flush();
		closed = true;
		if(listener!=null) listener.encoderClosed(this);
		if(waiting>0)
			notify();
	}
	
	/** Pull a series of bytes (all within [0,255]) as
	 * they become available.
	 * 
	 * @return encoded bytes, or null if if <code>close()</code> 
	 * has been called.
	 * 
	 * @see #pullImmediately()
	 * @see #push(int)
	 * @see #close()
	 */
	public synchronized int[] pull() throws IOException {
		while(outgoingData==null && (!closed)) {
			waiting++;
			try {
				wait();
			} catch(InterruptedException e) {
			} finally {
				waiting--;
			}
			
		}
		int[] returnValue = outgoingData;
		outgoingData = null;
		if(waiting>0)
			notify();
		return returnValue;
	}
	
	/** Push a byte for this encoder to process.
	 * <p>As data accumulates it will eventually have to be
	 * retrieved via the <code>pull()</code> method. This method
	 * may block if data is waiting to be retrieved.
	 * 
	 * @param b a byte from [0, 255]
	 * 
	 * @see #pull()
	 * @see #close()
	 */
	public abstract void push(int b) throws IOException;
	
	/** This is exclusively called during <code>close()</code> to give this encoder
	 * an opportunity to write any remaining data.
	 * 
	 */
	protected abstract void flush() throws IOException;
	
	private static final int[] EMPTY_ARRAY = new int[] {};
	/** This will return one of three things: an array of
	 * available bytes, an empty array if no data is available,
	 * or null if this encoder has been closed.
	 * 
	 * @return One of three things: an array of
	 * available bytes, an empty array if no data is available,
	 * or null if this encoder has been closed.
	 * 
	 * @see #pull()
	 * @see #push(int)
	 * @see #close()
	 */
	public synchronized int[] pullImmediately() {
		int[] returnValue = outgoingData;
		outgoingData = null;
		if(returnValue==null && (!closed))
			return EMPTY_ARRAY;
		return returnValue;
	}
	
	private int[] outgoingData;
	
	/** Make a chunk of data available to be read.
	 * This method may block until previously pushed chunks are
	 * cleared.
	 * 
	 * @param data a chunk of data that is ready to be pulled.
	 */
	protected void pushChunk(int[] data) throws IOException {
		while(outgoingData!=null) {
			waiting++;
			try {
				wait();
			} catch(InterruptedException e) {
				
			} finally {
				waiting--;
			}
		}
		outgoingData = data;
		if(listener!=null) listener.chunkAvailable(this);
		if(waiting>0)
			notify();
	}

	public String encode(String string) {
		return encode(string, StandardCharsets.UTF_8);
	}

	public String encode(String string,Charset charset) {
		byte[] data = string.getBytes(charset);
		data = encode(data);
		return new String(data, charset);
	}
	
	public byte[] encode(byte[] data) {
		try(ByteArrayOutputStream byteOut = new ByteArrayOutputStream(data.length)) {
			try(OutputStream out = createOutputStream(byteOut)) {
				out.write(data);
			}
			return byteOut.toByteArray();
		} catch(IOException e) {
			//this shouldn't happen if we're dealing in-memory IO.
			throw new RuntimeException(e);
		}
	}
	
	protected class EncodedInputStream extends InputStream {
		class Chunk {
			int[] data;
			int pos;
			
			public Chunk(int[] array) {
				data = new int[array.length];
				System.arraycopy(array, 0, data, 0, array.length);
			}
		}
		
		InputStream in;
		List<Chunk> chunks = new LinkedList<>();
		
		protected EncodedInputStream(InputStream in) throws IOException {
			this.in = in;
			setListener(new DataListener() {
				@Override
				public void chunkAvailable(ByteEncoder encoder) throws IOException {
					int[] array = encoder.pull();
					if(array.length!=0)
						chunks.add(new Chunk(array));
				}

				@Override
				public void encoderClosed(ByteEncoder encoder) throws IOException {}
			});
			queueNext();
		}

		int ctr = 0;
		private synchronized void queueNext() throws IOException {
			while(chunks.size()==0) {
				int z = in.read();
				if(z==-1) {
					ByteEncoder.this.close();
					return;
				} else {
					push(z);
					ctr++;
				}
			}
		}

		@Override
		public synchronized int read() throws IOException {
			if(chunks.size()==0)
				return -1;
			Chunk chunk = chunks.get(0);
			int value = chunk.data[chunk.pos++];
			if(chunk.pos==chunk.data.length) {
				chunks.remove(0);
			}
			if(chunks.size()==0)
				queueNext();
			return value;
		}
	}

	public InputStream createInputStream(InputStream in) throws IOException {
		return new EncodedInputStream(in);
	}

	public OutputStream createOutputStream(final OutputStream out) {
		setListener(new DataListener() {
			@Override
			public void chunkAvailable(ByteEncoder encoder) throws IOException {
				int[] chunk = encoder.pull();
				for(int a = 0; a<chunk.length; a++) {
					out.write(chunk[a]);
				}
			}

			@Override
			public void encoderClosed(ByteEncoder encoder) throws IOException {}
		});
		return new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				push(b);
			}
			
			@Override
			public void close() throws IOException {
				super.close();
				ByteEncoder.this.close();
			}
		};
	}

	public synchronized DataListener getDataListener() {
		return listener;
	}
}