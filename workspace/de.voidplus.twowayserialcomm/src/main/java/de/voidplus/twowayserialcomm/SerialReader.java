package de.voidplus.twowayserialcomm;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Handles the input coming from the serial port. A new line character is
 * treated as the end of a block in this example.
 */
public class SerialReader implements SerialPortEventListener {
	
	private InputStream input;
	private byte[] buffer = new byte[1024];
	private HashMap<Long, String> msgs;

	public SerialReader(InputStream input) {
		this.input = input;
		this.msgs = new HashMap<Long, String>();
	}
	
	/**
	 * Handle raw data.
	 */
	public void serialEvent(SerialPortEvent arg0) {
		int data;
		try {
			int len = 0;
			while ((data = input.read()) > -1) {
				if (data == '\n') {
					break;
				}
				buffer[len++] = (byte) data;
			}
			synchronized (this){
				this.msgs.put(
					System.currentTimeMillis(),
					new String(buffer, 0, len)
				);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
	}

	/**
	 * Get all received String messages.
	 * @return
	 */
	public HashMap<Long, String> getMessages() {
		synchronized (this){
			HashMap<Long, String> result = new HashMap<Long, String>(this.msgs);
			this.msgs.clear();
			return result;
		}
	}

}