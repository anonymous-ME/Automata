// Including the ESP8266 WiFi library
#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include "DHT.h"


#define LED0 2

#define LED 5
// Uncomment one of the lines below for whatever DHT sensor type you're using!
//#define DHTTYPE DHtemp1   // DHT 11
//#define DHTTYPE DHT21   // DHT 21 (AM2301)
#define DHTTYPE DHT22   // DHT 22  (AM2302), AM2321

// Replace with your network details
const char* ssid = "ubutu-wifi";
const char* password = "ubuntuwifi";

long pirdata;
int count = 0;
// Web Server on port 80
ESP8266WebServer server(80); 

// DHT Sensor
const int DHTPin1 = 4;

float temp;
float pretemp = 0;
////LLLLLLLLLLLLLLLDDDDDDDDDDDDDDDDDDDDDDDDRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
const int LDR = A0; // Defining LDR PIN 
int ldrdata = 0;  // Varible to store Input values
////LLLLLLLLLLLLLLLDDDDDDDDDDDDDDDDDDDDDDDRRRRRRRRRRRRRRRRRRRRRRRRRRRRR


// Initialize DHT sensor.
DHT dht1(DHTPin1, DHTTYPE);



// Temporary variables
static char celsiusTemp1[7];
static char fahrenheitTemp1[7];
static char humidityTemp1[7];


int sensor1 = 13;
int countemp = 0;
int total1= 0;

//void setup() {
    
//}

void CheckWiFiConnectivity()
  {
    while(WiFi.status() != WL_CONNECTED)
    {
      for(int i=0; i < 10; i++)
      {
        digitalWrite(LED0, !HIGH);
        delay(250);
        digitalWrite(LED0, !LOW);
        delay(250);
        Serial.print(".");
      }
      Serial.println("");
    }
  }

//handles server request and responses

void ontwo(){
  digitalWrite(LED,LOW);
  String s = "true";
  s = "{\"fans\" : true}";
  server.send(200, "text/plain", s);
}

void offtwo(){
  digitalWrite(LED,HIGH);
  String s = "false";
  s = "{\"fans\" : false}";
  server.send(200, "text/plain", s);
}


void handleRoot() {
  String s = "you did not input anything";
  server.send(200, "text/plain", s);
}

void pir_data(){
  String  s = String("\"pir\":") + String(count) +String("}");;
  server.send(200, "text/plain", s);
}

void query(){
  int stat = digitalRead(LED);
  //Serial.println(stat);
  String s;
  if(stat == 0)
    s = "{\"fans\" : true,";
  else if(stat == 1)
    s = "{\"fans\" : false,";
  
  s += String("\"location_id\": \"Room #5040 CC-3\"") + String(",");
  s += String("\"temperature\":") + String(temp) +String("}");
  server.send(200,"text/plain",s);
  
}

// only runs once on boot
void setup() {
  // Initializing serial port for debugging 
  pinMode(sensor1, INPUT); // declare sensor as input
  pinMode(LED, OUTPUT);
  Serial.begin(115200);
  delay(10);
  
  digitalWrite(LED,HIGH);
  dht1.begin();

  // Connecting to WiFi network
  if(WiFi.status() == WL_CONNECTED)
    {
      WiFi.disconnect();
      WiFi.mode(WIFI_OFF);
      delay(50);
    }
  WiFi.begin("ubuntu-wifi", "ubuntuwifi");      // The SSID That We Want To Connect To

  // Printing Message For User That Connetion Is On Process ---------------
  Serial.println("!--- Connecting To " + WiFi.SSID() + " ---!");

  // WiFi Connectivity ----------------------------------------------------
  CheckWiFiConnectivity();
  Serial.println("connected to pepsimaster");
  Serial.println(WiFi.localIP());
  server.on("/", handleRoot);
  server.on("/fan/0",ontwo);
  server.on("/fan/1",offtwo);
  server.on("/query",query);
  server.on("/pirdata",pir_data);
  server.begin();
  Serial.println("HTTP server started");
}

// runs over and over again
void loop() {
 
  // Read temperature as Celsius (the default)
  temp = dht1.readTemperature();
  
  if (!isnan(temp)) 
    pretemp = temp;
  
  // Check if any reads failed and exit early (to try again).
  if (isnan(temp)) {
    temp = pretemp;
    Serial.println("Failed to read from DHT sensorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr!");
          
  }
  else{

    // You can delete the following Serial.print's, it's just for debugging purposes
    Serial.print("Temperature: ");
    Serial.print(temp);
    Serial.println(" *C ");
    

  }
////LLLLLLLLLLLLLLLDDDDDDDDDDDDDDDDDDDDDDDDRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
  ldrdata = analogRead(LDR);      // Reading Input
  Serial.print("LDR value is : " );                        
  Serial.println(ldrdata);
////LLLLLLLLLLLLLLLDDDDDDDDDDDDDDDDDDDDDDDRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
  



  pirdata = digitalRead(sensor1);

  if(pirdata == HIGH){
    count++;
    Serial.println("Motion detected11111111111111111111!");
  }
  else {
    Serial.println("Motion absent!");
  }
  //delay(1000);
  server.handleClient();
}   

