#include <SoftwareSerial.h>

int bluetoothTx = 2;
int bluetoothRx = 3;
SoftwareSerial bluetooth(bluetoothTx, bluetoothRx);

#define LED_PIN 13
bool blinkState = false;

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
  bluetooth.println(buildKeyValueJson("msg", "foobar"));
  digitalWrite(LED_PIN, blinkState);  
}
