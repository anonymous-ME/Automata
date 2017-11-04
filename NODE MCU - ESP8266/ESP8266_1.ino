#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>

#define     LED0      2  

ESP8266WebServer server(80); 

const int trigPin = 5;//D1
const int echoPin = 4;//D2
const int ind = 0;

const int trigPin1=13; //D7
const int echoPin1= 12; //D6
const int ind1 = 2;

int automatic = 1;
int manual = 0;

IPAddress ip(192, 168, 43, 100);


// defines variables
long duration, duration1;
int distance,distance1;

//delay set and flags set
int d = 20;
int detectA = 0,detectB=0;
int halfdetected=0,resetcounter=0;
int initialA,initialB;
int counter=0, persons  = 0;


void swap(int &a, int &b){
  int temp = a;
  a = b;
  b = temp;
}

void bsort(int *a, int len)
{
  for(int i = 0; i<len; i++){
    for(int j=1; j<len - i; j++){
      if(a[j-1]>a[j]){
        swap(a[j],a[j-1]);
      }
    }
  }
}

//normalises the initial distance over 100 results
int finddistance(int trigPin,int echoPin){
  int lt[100];
  int lt_length = 100;
  for(int i=0; i<lt_length; i++){
    digitalWrite(trigPin, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPin, LOW);
    // Reads the echoPin, returns the sound wave travel time in microseconds
    duration = pulseIn(echoPin, HIGH);
    distance= duration*0.034/2;
    lt[i] = distance;
    delay(d);
   }
   bsort(lt, lt_length);
   Serial.println();
   Serial.println(trigPin);
   Serial.println(lt[50]);
   return lt[50];
}

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

//===================roothandle==========================

void ontwo(){
  digitalWrite(2,LOW);
  String s = "on";
  s = "{\"light\" : true}";
  server.send(200, "text/plain", s);
}

void offtwo(){
  digitalWrite(2,HIGH);
  String s = "off";
  s = "{\"light\" : false}";
  server.send(200, "text/plain", s);
}

void response(){
  String s;
  s = String("{\"persons\":") + String(persons) +String("}");
  server.send(200, "text/plain", s);
}

void handleRoot() {
  String s = "you did not input anything";
  server.send(200, "text/plain", s);
}

void query(){
  int stat = digitalRead(2);
  //Serial.println(stat);
  String s;
  if(stat == 0)
    s = "{\"light\" : true,";
  else if(stat == 1)
    s = "{\"light\" : false,";
  
  s += String("\"persons\":") + String(persons) +String("}");
  server.send(200,"text/plain",s);
  
}


void setup() {
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an Output
  pinMode(echoPin, INPUT);  // Sets the echoPin as an Input
  pinMode(trigPin1, OUTPUT);// Sets the trigPin as an Output
  pinMode(echoPin1, INPUT); // Sets the echoPin as an Input
  pinMode(ind, OUTPUT);     
  pinMode(ind1, OUTPUT);
  
  Serial.begin(115200); // Starts the serial communication
  Serial.println("i have begun");
  //clear the triggerpin in setup
  digitalWrite(trigPin, LOW);
  digitalWrite(trigPin1, LOW);
  
  delay(100);
  //
  initialA= finddistance(trigPin,echoPin);
  initialB= finddistance(trigPin1,echoPin1);
  Serial.println(initialA);
  Serial.println(initialB);

  
  if(WiFi.status() == WL_CONNECTED)
    {
      WiFi.disconnect();
      WiFi.mode(WIFI_OFF);
      delay(50);
    }
    
    WiFi.begin("ubuntu-wifi", "ubuntuwifi");      // The SSID That We Want To Connect To
    //WiFi.config(ip);

    // Printing Message For User That Connetion Is On Process ---------------
    Serial.println("!--- Connecting To " + WiFi.SSID() + " ---!");

    // WiFi Connectivity ----------------------------------------------------
    CheckWiFiConnectivity();
    Serial.println("connected to pepsimaster");
    Serial.println(WiFi.localIP());
    server.on("/", handleRoot);
    server.on("/light/0",ontwo);
    server.on("/light/1",offtwo);
    server.on("/countpersons",response);
    server.on("/query",query);
    server.begin();
    Serial.println("HTTP server started");
}
  
void loop() {
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(echoPin, HIGH);
  
  delay(d);
  distance= duration*0.034/2;

  //whenever A detects 
  if(initialA - distance > 10){
    if(detectA == 0){
      detectA = 1;
      digitalWrite(ind, HIGH);
      if(detectB == 1){
        persons--;
        halfdetected = 0;
        counter = 0;
      }
      else{
        halfdetected = 1;
      }
      Serial.print("Distance and persons ");
      Serial.println(distance + String(" ") + persons);
    }
    else if(halfdetected == 1)
      counter = 0;
  }

  
  
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin1, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin1, LOW);
  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration1 = pulseIn(echoPin1, HIGH);
  
  delay(d);
  
  // Calculating the distance
  
  
  distance1= duration1*0.034/2;

  //whenever B detects
  if(initialB - distance1 > 10){
    if(detectB==0){
      detectB = 1;
      digitalWrite(ind1, HIGH);
      if(detectA == 1){
        persons++;
        halfdetected = 0;
        counter = 0;
      }
      else{
        halfdetected = 1;
      }
      Serial.print("Distance1: and persons");
      Serial.println(distance1 + String(" ") + persons);
    }
    else if(halfdetected==1)
      counter = 0;
  }


  if(halfdetected == 1){
    counter++;
    if(counter > 10){
      halfdetected = 0;
      detectA=0;
      detectB =0;
      counter = 0;
      digitalWrite(ind, LOW);
      digitalWrite(ind1, LOW);
    }
  }
  else
   counter = 0;

  //resets everything if the distances are consistent for more than 10 times
  if(halfdetected == 0 && detectA && initialA-distance < 10 && initialB-distance1 < 10){
    resetcounter++;
  }
  else
    resetcounter  = 0;
  if(resetcounter >= 10){
      detectA = 0;
      detectB = 0; 
      digitalWrite(ind, LOW);
      digitalWrite(ind1, LOW); 
  }

  server.handleClient();
}
