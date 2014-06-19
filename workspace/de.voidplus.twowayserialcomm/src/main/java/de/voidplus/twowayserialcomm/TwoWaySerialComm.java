package de.voidplus.twowayserialcomm;

import gnu.io.*;

import java.io.*;
import java.util.Enumeration;
import java.util.Vector;

public class TwoWaySerialComm {
	
    OutputStream output;
    InputStream input;
    
    SerialReader serial;
    CommPort commPort;
    SerialPort serialPort;
    
	public TwoWaySerialComm() {
	}

	public void connect(String portName, Integer baudrate) throws Exception {
		
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
				// Thread.sleep(3000);
				// out.write("yes".getBytes());
				
				// OUT
				this.output = serialPort.getOutputStream();
				// (new Thread(new SerialReader(in))).start();
				(new Thread(new SerialWriter(this.output))).start();

			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
	}
	
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
	
	void write(String str) {
		if (this.isConnected()) {
			try {
				this.output.write(str.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String read() {
		if (this.isConnected()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return (this.serial.getMessage());
		}
		return "";
	}
	
}