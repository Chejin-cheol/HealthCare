#include <SoftwareSerial.h> //시리얼통신 라이브러리 호출

int distance = 0; 
int pushcount = 0;
int echoPin = 12; //ech 초음파
int trigPin = 13; //trig 초음파
int current, old , pick = 0, count=0;
boolean  pushUp ,  testStart , flag = false;

 
int blueTx=2;   //Tx (보내는핀 설정)at
int blueRx=3;   //Rx (받는핀 설정)
String myString="";
char endFlag;
SoftwareSerial mySerial(blueTx, blueRx);  //시리얼 통신을 위한 객체선언

void setup() {
  // put your setup code here, to run once:
    Serial.begin(9600);   //시리얼모니터
    mySerial.begin(9600); //블루투스 시리얼
    pinMode(trigPin, OUTPUT);
    pinMode(echoPin, INPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
     char flag ;
   
     if(mySerial.available())
      {
          flag = mySerial.read();
      }
        while(true)
      {      
          bool _switch = false;
          old = pick = getCM(); 
          while(flag == '1')
         {  
          
          if(mySerial.read() == "x"){
          flag=mySerial.read();
                                              }
          Serial.print(flag); Serial.println("flag값이야");
              if(!_switch)
              {
                  if(getCM() <=15)
                  {
                     _switch = true;
                  }
              }

              if(_switch)
              {
                  int cm = getCM();
                  if(cm >= (pick -3) and cm <= (pick+3) )
                  {  count ++;
                     mySerial.println(count);
                     delay(100);
                     Serial.println(count);
                     delay(100);
                    _switch = false;
                    delay(500);
                    
                  }
              }
               
              if(mySerial.available())
              {
                if(mySerial.read() == 'x')
                { count =0;
                     Serial.println("flag값이야"+flag);
                   break;
                }
              }
         }
         break; 
      }

}

float getCM()
{
              digitalWrite(trigPin, LOW);
              digitalWrite(echoPin, LOW);
              delayMicroseconds(2);
              digitalWrite(trigPin, HIGH);
              delayMicroseconds(10);
              digitalWrite(trigPin, LOW);
            
              unsigned long duration = pulseIn(echoPin, HIGH);
              return ((float)(340 * duration) / 10000) / 2; 
}