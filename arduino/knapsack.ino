#include <SoftwareSerial.h>

#define RFID_LEN (16)
#define ID_START (5)
#define ID_END (13)

#define RF_STARTBYTE (2)
#define BT_STARTBYTE ('|')

int rfidReset = 3;
int btTX = 5;
int btRX = 4;
int rfidTX = 8;
int rfidRX = 6;

byte val = 0;

//SoftwareSerial bluetooth(btTX, btRX);
SoftwareSerial rfid(rfidTX, rfidRX);

void setup(){
  Serial.begin(9600);
  //bluetooth.begin(57600);
  rfid.begin(9600);

  pinMode(rfidReset, OUTPUT);
  digitalWrite(rfidReset, HIGH);
}

void loop(){
  digitalWrite(13, HIGH);
  int i=0;
  if (rfid.available() > 0 && rfid.peek() == RF_STARTBYTE) {
    send_byte((byte)BT_STARTBYTE);
    //send_byte((char)16);
    while(rfid.available()) {
      val = rfid.read();
      //tagID[i] = val;
      Serial.print(val, HEX);
      i++;
    }
    Serial.println();
    //printTag(tagID);
  }
  digitalWrite(13, LOW);
}


void resetRFID(){
  digitalWrite(rfidReset, LOW);
  digitalWrite(rfidReset, HIGH);
  delay(150);
}

void send_byte(byte c) {
  //bluetooth.listen();
  Serial.print(c, HEX);
  //bluetooth.print(c);
  //rfid.listen();
  return;
}
