#include <Arduino.h>
#include <SPI.h>
#include "Adafruit_BLE.h"
#include "Adafruit_BluefruitLE_SPI.h"
#include "Adafruit_BluefruitLE_UART.h"
#include "BluefruitConfig.h"
#include "MPU9250.h"

#if SOFTWARE_SERIAL_AVAILABLE
  #include <SoftwareSerial.h>
#endif
  #define FACTORYRESET_ENABLE         1
  #define MINIMUM_FIRMWARE_VERSION    "0.6.6"
  #define MODE_LED_BEHAVIOUR          "MODE"
  #define DELAY                       20

MPU9250 axel1(Wire,0x69);
MPU9250 axel2(Wire,0x68);
Adafruit_BluefruitLE_SPI ble(BLUEFRUIT_SPI_CS, BLUEFRUIT_SPI_IRQ, BLUEFRUIT_SPI_RST);

void error(const __FlashStringHelper*err) {
  Serial.println(err);
  while (1);
}

void initAxel(int status, String axelName) {
  Serial.print("Initializing " + axelName + "...");
  if (status < 0) {
    Serial.println("FAILED");
    Serial.println("Check IMU wiring or try cycling power");
    Serial.print("Status: ");
    Serial.println(status);
  } else {
    Serial.println("OK");     
  }
}

void initBle() {
  if (!ble.begin(VERBOSE_MODE)) {
    error(F("Couldn't find Bluefruit, make sure it's in CoMmanD mode & check wiring?"));
  }
  Serial.println(F("OK!"));

  if ( FACTORYRESET_ENABLE ) {
    Serial.println(F("Performing a factory reset: "));
    if (!ble.factoryReset()) {
      error(F("Couldn't factory reset"));
    }
  }

  ble.echo(false);

  Serial.println("Requesting Bluefruit info:");
  ble.info();

  Serial.println(F("Please use Adafruit Bluefruit LE app to connect in UART mode"));
  Serial.println(F("Then Enter characters to send to Bluefruit"));
  Serial.println();

  ble.verbose(false);
  while (!ble.isConnected()) {
      delay(500);
  }
  
  Serial.println(F("******************************"));
  if (ble.isVersionAtLeast(MINIMUM_FIRMWARE_VERSION)) {
    Serial.println(F("Change LED activity to " MODE_LED_BEHAVIOUR));
    ble.sendCommandCheckOK("AT+HWModeLED=" MODE_LED_BEHAVIOUR);
  }

  Serial.println( F("Switching to DATA mode!") );
  ble.setMode(BLUEFRUIT_MODE_DATA);
  Serial.println(F("******************************"));  
}

String collectAcceslData() {
  axel1.readSensor();
  axel2.readSensor();

  String axxeData = "{";
  axxeData += "\"ax1\":" + String(axel1.getAccelX_mss(), 3) + ',' + "\"ax2\":" + String(axel2.getAccelX_mss(), 3) + ',';
  axxeData += "\"ay1\":" + String(axel1.getAccelY_mss(), 3) + ',' + "\"ay2\":" + String(axel2.getAccelY_mss(), 3) + ',';
  axxeData += "\"az1\":" + String(axel1.getAccelZ_mss(), 3) + ',' + "\"az2\":" + String(axel2.getAccelZ_mss(), 3) + ',';
  axxeData += "\"gx1\":" + String(axel1.getGyroX_rads(), 6) + ',' + "\"gx2\":" + String(axel2.getGyroX_rads(), 6) + ',';
  axxeData += "\"gy1\":" + String(axel1.getGyroY_rads(), 6) + ',' + "\"gy2\":" + String(axel2.getGyroY_rads(), 6) + ',';
  axxeData += "\"gz1\":" + String(axel1.getGyroZ_rads(), 6) + ',' + "\"gz2\":" + String(axel2.getGyroZ_rads(), 6) + "}\n";

  return axxeData;
}

void setup(void) {
  Serial.begin(115200);
  delay(500);
  Serial.println("Initializing hardware");
  delay(500);

  Serial.println(F("Initialilzing accelerometers: "));
  initAxel(axel1.begin(), "accelerometer 1");
  initAxel(axel2.begin(), "accelerometer 2");

  //Serial.println(F("Initialising the Bluefruit LE module: "));
  //initBle();
}


void translateSerialToBle() {
  char n, inputs[BUFSIZE+1];
  if (Serial.available()) {
    n = Serial.readBytes(inputs, BUFSIZE);
    inputs[n] = 0;
    Serial.print("Sending: ");
    Serial.println(inputs);

    ble.print(inputs);
  }
}

void translateBleToSerial() {
  while (ble.available()) {
    int c = ble.read();

    Serial.print((char)c);
    Serial.print(" [0x");
    if (c <= 0xF) Serial.print(F("0"));
    Serial.print(c, HEX);
    Serial.print("] ");
  }  
}

void loop(void) {  
  //translateSerialToBle();
  //translateBleToSerial();
  
  String data = collectAcceslData();
  Serial.println(data);

  //if (ble.isConnected()) {
  //  ble.print(data.c_str());
  //}

  #ifdef DELAY
  delay(DELAY);
  #endif
}
