#include <SoftwareSerial.h> //시리얼통신 라이브러리 호출

int distance = 0; 
int pushcount = 0;
int echoPin = 12; //ech 초음파
int trigPin = 13; //trig 초음파
int current, old , pick = 0;
boolean pushUp ,  testStart = false;

 
int blueTx=2;   //Tx (보내는핀 설정)at
int blueRx=3;   //Rx (받는핀 설정)
String myString="";
char endFlag;
SoftwareSerial mySerial(blueTx, blueRx);  //시리얼 통신을 위한 객체선언
 
void setup() 
{
    Serial.begin(9600);   //시리얼모니터
    mySerial.begin(9600); //블루투스 시리얼
    pinMode(trigPin, OUTPUT);
    pinMode(echoPin, INPUT);
}
void loop()
{
              digitalWrite(trigPin, LOW);
              digitalWrite(echoPin, LOW);
              delayMicroseconds(2);
              digitalWrite(trigPin, HIGH);
              delayMicroseconds(10);
              digitalWrite(trigPin, LOW);
            
              unsigned long duration = pulseIn(echoPin, HIGH);
              float distance = ((float)(340 * duration) / 10000) / 2; 

              if(old == 0)
              {
                pick = distance;
              }

              Serial.print("pick = ");
              Serial.println(pick);
               
while(true)
{

  
    if (mySerial.available()) {     
     char c  =  (char)mySerial.read(); 
     Serial.println(c);
          while(c == '1')
          {   
              digitalWrite(trigPin, LOW);
              digitalWrite(echoPin, LOW);
              delayMicroseconds(2);
              digitalWrite(trigPin, HIGH);
              delayMicroseconds(10);
              digitalWrite(trigPin, LOW);
            
              unsigned long duration = pulseIn(echoPin, HIGH);
              float distance = ((float)(340 * duration) / 10000) / 2; 

              current = distance ;
                           
              if(  ((old - current) < 0 ) && !pushUp )
              {  
                 
                  if(current <= 15)
                  {      
                      pushUp = true;
                  }
              }


            if( ( (old - current) > 0 ) && pushUp )
            {   
               
               if((pick - 3) <= current)
               {   
                    Serial.print(++pushcount);
                    Serial.println(" 회");             
                        mySerial.write('a');  //시리얼 모니터 내용을 블루추스 측에 WRITE


                    pushUp = false;
                 }
            }  
                  old = current;

                if(((char)mySerial.read()) == '2')
                {
                  pick = old = distance = current =  0;
                  pushUp = false;
                  return;
                }
             
          }     
    }
    
    if (Serial.available()) {         
      mySerial.write(Serial.read());  //시리얼 모니터 내용을 블루추스 측에 WRITE
    }

  }

}
