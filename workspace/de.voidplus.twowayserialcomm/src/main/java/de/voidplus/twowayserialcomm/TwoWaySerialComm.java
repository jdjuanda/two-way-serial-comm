package de.voidplus.twowayserialcomm;

import gnu.io.*;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import org.json.simple.JSONObject;

public class TwoWaySerialComm {
	
    private OutputStream output;
    private InputStream input;
    
    private SerialReader serial;
    private CommPort commPort;
    private SerialPort serialPort;
    
	public TwoWaySerialComm() {
	}
	public TwoWaySerialComm(String portName, Integer baudrate) {
		this.connect(portName, baudrate);
	}

	/**
	 * Connect to the port with specific baudrate.
	 * @param portName
	 * @param baudrate
	 */
	public void connect(String portName, Integer baudrate) {
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			if (portIdentifier.isCurrentlyOwned()) {
				System.out.println("Error: Port is currently in use");
			} else {
				this.commPort = portIdentifier.open(this.getClass().getName(),2000);
				if (commPort instanceof SerialPort) {
					
					this.serialPort = (SerialPort) commPort;
					this.serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					
					// IN
					this.input = serialPort.getInputStream();
					this.serial = new SerialReader(this.input);
					
					serialPort.addEventListener(this.serial);
					serialPort.notifyOnDataAvailable(true);
					
					// OUT
					this.output = serialPort.getOutputStream();
					// (new Thread(new SerialReader(in))).start();
					(new Thread(new SerialWriter(this.output))).start();
					
					Thread.sleep(5000);
					this.output.write("yes".getBytes());
	
				} else {
					System.out.println("Error: Only serial ports are handled by this example.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Disconnect the serial communication.
	 */
	public void disconnect() {
		if (this.isConnected()) {
			try {
				this.output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.commPort.close();
			System.out.println("Disconnected from Port " + commPort.getName());
			this.commPort = null;
		} else {
			System.out.println("There is nothing to disconnect");
		}
	}
	
	/**
	 * Check if the device is connected.
	 * @return
	 */
	public boolean isConnected(){
		return (this.commPort!=null);
	}
	
	public static String[] listAvailablePorts() {
		Vector localVector = new Vector();
		try {
			Enumeration localEnumeration = CommPortIdentifier
					.getPortIdentifiers();
			while (localEnumeration.hasMoreElements()) {
				CommPortIdentifier localCommPortIdentifier = (CommPortIdentifier) localEnumeration
						.nextElement();
				if (localCommPortIdentifier.getPortType() == 1) {
					String str = localCommPortIdentifier.getName();
					localVector.addElement(str);
				}
			}
		} catch (UnsatisfiedLinkError localUnsatisfiedLinkError) {
			// errorMessage("ports", localUnsatisfiedLinkError);
		} catch (Exception localException) {
			// errorMessage("ports", localException);
		}
		String[] arrayOfString = new String[localVector.size()];
		localVector.copyInto(arrayOfString);
		return arrayOfString;
	}
	
	/**
	 * Send JSON message to device.
	 * @param str
	 */
	public void write(String str) {
		if (this.isConnected()) {
			try {
				this.output.write(str.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Get all received JSON objects.
	 * @return
	 */
	public HashMap<Long, JSONObject> read() {
		if (this.isConnected()) {
			return this.serial.getMessages();
		}
		return new HashMap<Long, JSONObject>();
	}
	
}