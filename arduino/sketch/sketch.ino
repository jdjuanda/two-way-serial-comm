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
  bluetooth.println(buildKeyValueJson("msg", "foo"));
 
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
