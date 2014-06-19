package de.voidplus.twowayserialcomm;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * Handles the input coming from the serial port. A new line character is
 * treated as the end of a block in this example.
 */
public class SerialReader implements SerialPortEventListener {
	
	private InputStream input;
	private byte[] buffer = new byte[1024];
	private String msg;

	public SerialReader(InputStream input) {
		this.input = input;
		this.msg = "";
	}

	public void serialEvent(SerialPortEvent arg0) {
		try {
			int data, len = 0;
			while ((data = input.read()) > -1) {
				if (data == '\n') {
					break;
				}
				buffer[len++] = (byte) data;
			}
			
			synchronized (this){
				this.msg = new String(buffer,0,len).trim();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getMessage() {
		synchronized (this){
			return this.msg;
		}
	}

}