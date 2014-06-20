package de.voidplus.twowayserialcomm;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Handles the input coming from the serial port. A new line character is
 * treated as the end of a block in this example.
 */
public class SerialReader implements SerialPortEventListener {
	
	private InputStream input;
	private byte[] buffer = new byte[1024];
	private HashMap<Long, JSONObject> msgs;
	private JSONParser parser;

	public SerialReader(InputStream input) {
		this.input = input;
		this.msgs = new HashMap<Long, JSONObject>();
		this.parser = new JSONParser();
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
				boolean okay = true;
				JSONObject val = new JSONObject();
				String raw = new String(buffer, 0, len);
				// System.out.println("raw json string: "+raw);
				if(!raw.equals("")){
					try {
						val = (JSONObject) parser.parse(raw);
					} catch (ParseException e) {
						okay = false;
						// e.printStackTrace();
					}
				}
				if(okay){
					this.msgs.put(System.currentTimeMillis(), val);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
	}

	/**
	 * Get all received JSON messages.
	 * @return
	 */
	public HashMap<Long, JSONObject> getMessages() {
		synchronized (this){
			HashMap<Long, JSONObject> result = new HashMap<Long, JSONObject>(this.msgs);
			this.msgs.clear();
			return result;
		}
	}

}