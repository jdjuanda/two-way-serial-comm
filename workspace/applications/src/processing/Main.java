package processing;

import java.util.HashMap;

import de.voidplus.twowayserialcomm.*;
import processing.core.*;

public class Main extends PApplet implements PConstants {

	public static final boolean FULLSCREEN = false;
	public static final String  TITLE = "TwoWaySerialComm";
	
	public static final String PORT = "/dev/tty.RNBT-33AA-RNI-SPP";
	public static final Integer BAUDRATE = 9600; // 115200
	
	public TwoWaySerialComm com;
	public HashMap<Long, String> msgs;
	
	public void setup(){
		this.initScreen();
		background(240);
		smooth();
		
		com = new TwoWaySerialComm();
		msgs = new HashMap<Long, String>();
	}

	public void draw(){
		background(240);
		
		if(com.isConnected()){
			// reading messages
			this.msgs = com.read();
			if(!this.msgs.isEmpty()){
				for(String str : this.msgs.values()){	
					System.out.println(str);
				}
			}
		}
	}
	
	public void exit(){
		if(com.isConnected()){
			com.disconnect();
		}
	}
	
	public void keyReleased(){
		switch(key){
		case 'c': // connect
			try {
				com.connect(PORT, BAUDRATE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 'd': // disconnect
			if(com.isConnected()){
				com.disconnect();
			}
			break;
		case 'w': // writing messages
			if(com.isConnected()){
				System.out.println("Sending ...");
				com.write("bar");
			}
			break;
		}
	}
	
	/**
	 * Init display mode.
	 */
	public void initScreen(){
		if (Main.FULLSCREEN) {
			size(
				(int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
				(int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
				OPENGL
			);
		} else {
			size(800, 600, OPENGL);
			this.frame.setTitle(Main.TITLE);
		}
		frameRate(200);
		loop();
	}

	/**
	 * Start sketch in specific mode.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (Main.FULLSCREEN) {
			PApplet.main(new String[] { "--present", "processing.Main" });
		} else {
			PApplet.main(new String[] { "processing.Main" });
		}
	}
	
}