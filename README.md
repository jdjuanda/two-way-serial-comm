# TwoWaySerialComm

Library for bidirectional serial communication between devices.


## Download

- [twowayserialcomm-0.0.1-SNAPSHOT.jar](https://github.com/voidplus/two-way-serial-comm/raw/master/workspace/de.voidplus.twowayserialcomm/target/twowayserialcomm-0.0.1-SNAPSHOT.jar)
- [twowayserialcomm-0.0.1-SNAPSHOT-jar-with-dependencies.jar](https://github.com/voidplus/two-way-serial-comm/raw/master/workspace/de.voidplus.twowayserialcomm/target/twowayserialcomm-0.0.1-SNAPSHOT-jar-with-dependencies.jar)

Don't forget to add the [native RXTX libraries](https://github.com/voidplus/two-way-serial-comm/tree/master/workspace/de.voidplus.twowayserialcomm/src/main/resources/lib/rxtx) to classpath.


## Dependencies

- [pom.xml](https://raw.githubusercontent.com/voidplus/two-way-serial-comm/master/workspace/de.voidplus.twowayserialcomm/pom.xml)
	- [RXTX v2.1.7](http://www.jcontrol.org/download/rxtx_en.html)


## Usage

### Java

Import and initialize:

```java
import de.voidplus.twowayserialcomm.*;

// ...

public TwoWaySerialComm com = new TwoWaySerialComm();
public HashMap<Long, String> msgs = new HashMap<Long, String>();
```

Connect:

```java
try {
	com.connect("/dev/tty.RNBT-33AA-RNI-SPP", 9600);
} catch (Exception e) {
	e.printStackTrace();
}
```

Disconnect:

```java
if(com.isConnected()){
	com.disconnect();
}
```

Read / Receive:

```java
if(com.isConnected()){
	this.msgs = com.read();
	if(!this.msgs.isEmpty()){
		for(String str : this.msgs.values()){
			System.out.println(str);
		}
	}
}
```

Write / Send:

```java
if(com.isConnected()){
	com.write("bar");
}
```

### Arduino, C

```c
#include <SoftwareSerial.h>

int bluetoothTx = 4;
int bluetoothRx = 3;
SoftwareSerial bluetooth(bluetoothTx, bluetoothRx);

#define LED_PIN 13
bool flag = false;

// simple json builder, b/c aJson needs to much storage
String buildKeyValueJson(String key, String value)
{
  String result = "";
  result = "{\""+key+"\":\""+value+"\"}";
  return result;
}

void setup()
{
  Serial.begin(9600);
  bluetooth.begin(115200);
  bluetooth.print("$");
  bluetooth.print("$");
  bluetooth.print("$");
  delay(350);
  bluetooth.println("U,9600,N");
  bluetooth.begin(9600);

  pinMode(LED_PIN, OUTPUT);
}

void loop()
{
  bluetooth.println("foo");
 
  String content = ""; 
  while(bluetooth.available() > 0) {
    content.concat((char)bluetooth.read());
  }
  if (content != "") {
    Serial.println(content);
    flag = !flag;
  }
  if(flag){
    digitalWrite(LED_PIN, HIGH);
  } else {
    digitalWrite(LED_PIN, LOW);
  }
  
}
```

[Arduino Breakboard](https://raw.githubusercontent.com/voidplus/two-way-serial-comm/master/arduino/breadboard.png) with the Bluetooth BlueSMiRF Silver module.


## Questions?

Don't be shy and feel free to contact me via [Twitter](http://twitter.voidplus.de).


## License

The library is Open Source Software released under the [License](https://raw.github.com/voidplus/two-way-serial-comm/master/LICENSE.txt).
