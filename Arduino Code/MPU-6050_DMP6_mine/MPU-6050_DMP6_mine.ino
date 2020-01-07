// I2C device class (I2Cdev) demonstration Arduino sketch for MPU6050 class using DMP (MotionApps v2.0)
// 6/21/2012 by Jeff Rowberg <jeff@rowberg.net>
// Updates should (hopefully) always be available at https://github.com/jrowberg/i2cdevlib
//
// Changelog:
//      2013-05-08 - added seamless Fastwire support
//                 - added note about gyro calibration
//      2012-06-21 - added note about Arduino 1.0.1 + Leonardo compatibility error
//      2012-06-20 - improved FIFO overflow handling and simplified read process
//      2012-06-19 - completely rearranged DMP initialization code and simplification
//      2012-06-13 - pull gyro and accel data from FIFO packet instead of reading directly
//      2012-06-09 - fix broken FIFO read sequence and change interrupt detection to RISING
//      2012-06-05 - add gravity-compensated initial reference frame acceleration output
//                 - add 3D math helper file to DMP6 example sketch
//                 - add Euler output and Yaw/Pitch/Roll output formats
//      2012-06-04 - remove accel offset clearing for better results (thanks Sungon Lee)
//      2012-06-01 - fixed gyro sensitivity to be 2000 deg/sec instead of 250
//      2012-05-30 - basic DMP initialization working

/* ============================================
I2Cdev device library code is placed under the MIT license
Copyright (c) 2012 Jeff Rowberg

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
===============================================
*/

// I2Cdev and MPU6050 must be installed as libraries, or else the .cpp/.h files
// for both classes must be in the include path of your project
#include "I2Cdev.h"

#include "MPU6050_6Axis_MotionApps20.h"
//#include "MPU6050.h" // not necessary if using MotionApps include file

// Arduino Wire library is required if I2Cdev I2CDEV_ARDUINO_WIRE implementation
// is used in I2Cdev.h
#if I2CDEV_IMPLEMENTATION == I2CDEV_ARDUINO_WIRE
    #include "Wire.h"
#endif


// class default I2C address is 0x68
// specific I2C addresses may be passed as a parameter here
// AD0 low = 0x68 (default for SparkFun breakout and InvenSense evaluation board)
// AD0 high = 0x69
MPU6050 mpu;
//MPU6050 mpu(0x69); // <-- use for AD0 high

/* =========================================================================
   NOTE: In addition to connection 3.3v, GND, SDA, and SCL, this sketch
   depends on the MPU-6050's INT pin being connected to the Arduino's
   external interrupt #0 pin. On the Arduino Uno and Mega 2560, this is
   digital I/O pin 2.
 * ========================================================================= */

/* =========================================================================
   NOTE: Arduino v1.0.1 with the Leonardo board generates a compile error
   when using Serial.write(buf, len). The Teapot output uses this method.
   The solution requires a modification to the Arduino USBAPI.h file, which
   is fortunately simple, but annoying. This will be fixed in the next IDE
   release. For more info, see these links:

   http://arduino.cc/forum/index.php/topic,109987.0.html
   http://code.google.com/p/arduino/issues/detail?id=958
 * ========================================================================= */



// uncomment "OUTPUT_READABLE_QUATERNION" if you want to see the actual
// quaternion components in a [w, x, y, z] format (not best for parsing
// on a remote host such as Processing or something though)
//#define OUTPUT_READABLE_QUATERNION

// uncomment "OUTPUT_READABLE_EULER" if you want to see Euler angles
// (in degrees) calculated from the quaternions coming from the FIFO.
// Note that Euler angles suffer from gimbal lock (for more info, see
// http://en.wikipedia.org/wiki/Gimbal_lock)
//#define OUTPUT_READABLE_EULER

// uncomment "OUTPUT_READABLE_YAWPITCHROLL" if you want to see the yaw/
// pitch/roll angles (in degrees) calculated from the quaternions coming
// from the FIFO. Note this also requires gravity vector calculations.
// Also note that yaw/pitch/roll angles suffer from gimbal lock (for
// more info, see: http://en.wikipedia.org/wiki/Gimbal_lock)
//#define OUTPUT_READABLE_YAWPITCHROLL
//#define USE_NEW_DMPGETYAWPITCHROLL
//#define MY_RANGE

// uncomment "OUTPUT_READABLE_REALACCEL" if you want to see acceleration
// components with gravity removed. This acceleration reference frame is
// not compensated for orientation, so +X is always +X according to the
// sensor, just without the effects of gravity. If you want acceleration
// compensated for orientation, us OUTPUT_READABLE_WORLDACCEL instead.
//#define OUTPUT_READABLE_REALACCEL

// uncomment "OUTPUT_READABLE_WORLDACCEL" if you want to see acceleration
// components with gravity removed and adjusted for the world frame of
// reference (yaw is relative to initial orientation, since no magnetometer
// is present in this case). Could be quite handy in some cases.
//#define OUTPUT_READABLE_WORLDACCEL

// uncomment "OUTPUT_TEAPOT" if you want output that matches the
// format used for the InvenSense teapot demo
#define OUTPUT_TEAPOT
#define STABLE_Z_YAW_START_FROM_ZERO
//#define SWITCH_PITCH_AND_ROLL_AXIS
//#define ROTATE_180_DEGREE_Z_YAW

static float EPS = 1.1920928955078125E-7f;

// Ritardo dell' "if (DELAY_blink + 250 < millis())" e valore dell'ultimo valore buono
long DELAY_blink = 0;

#define ROSSO_out 13 // (Arduino is 13, Teensy is 11, Teensy++ is 6)
#define LED_HIGH 255 // If you use a PWM pin instead 13 with an external LED you can give 10-15 of values for analogWrite to a low brightness instead HIGHT for digitalWrite
#define LED_LOW    0

byte ROSSO_state = 0;

Quaternion myQuatOffset;

#define INTERRUPT_PIN 2  // use pin 2 on Arduino Uno & most boards

// MPU control/status vars
bool dmpReady = false;  // set true if DMP init was successful
uint8_t mpuIntStatus;   // holds actual interrupt status byte from MPU
uint8_t devStatus;      // return status after each device operation (0 = success, !0 = error)
uint16_t packetSize;    // expected DMP packet size (default is 42 bytes)
uint16_t fifoCount;     // count of all bytes currently in FIFO
uint8_t fifoBuffer[64]; // FIFO storage buffer

// orientation/motion vars
Quaternion q;           // [w, x, y, z]         quaternion container
VectorInt16 aa;         // [x, y, z]            accel sensor measurements
VectorInt16 aaReal;     // [x, y, z]            gravity-free accel sensor measurements
VectorInt16 aaWorld;    // [x, y, z]            world-frame accel sensor measurements
VectorFloat gravity;    // [x, y, z]            gravity vector
float euler[3];         // [psi, theta, phi]    Euler angle container
float ypr[3];           // [yaw, pitch, roll]   yaw/pitch/roll container and gravity vector

// packet structure for InvenSense teapot demo
uint8_t teapotPacket[14] = { '$', 0x02, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '\r', '\n' };



// ================================================================
// ===               INTERRUPT DETECTION ROUTINE                ===
// ================================================================

volatile bool mpuInterrupt = false;     // indicates whether MPU interrupt pin has gone high
void dmpDataReady() {
    mpuInterrupt = true;
}



// ================================================================
// ===                      INITIAL SETUP                       ===
// ================================================================

void setup() {
    // join I2C bus (I2Cdev library doesn't do this automatically)
    #if I2CDEV_IMPLEMENTATION == I2CDEV_ARDUINO_WIRE
        Wire.begin();
        Wire.setClock(400000); // 400kHz I2C clock. Comment this line if having compilation difficulties
    #elif I2CDEV_IMPLEMENTATION == I2CDEV_BUILTIN_FASTWIRE
        Fastwire::setup(400, true);
    #endif

    // initialize serial communication
    // (115200 chosen because it is required for Teapot Demo output, but it's
    // really up to you depending on your project)
    Serial.begin(115200);
    while (!Serial); // wait for Leonardo enumeration, others continue immediately

    // NOTE: 8MHz or slower host processors, like the Teensy @ 3.3V or Arduino
    // Pro Mini running at 3.3V, cannot handle this baud rate reliably due to
    // the baud timing being too misaligned with processor ticks. You must use
    // 38400 or slower in these cases, or use some kind of external separate
    // crystal solution for the UART timer.

    // initialize device
    Serial.println(F("Initializing I2C devices..."));
    mpu.initialize();
    pinMode(INTERRUPT_PIN, INPUT);

    // verify connection
    Serial.println(F("Testing device connections..."));
    Serial.println(mpu.testConnection() ? F("MPU6050 connection successful") : F("MPU6050 connection failed"));

    // wait for ready
    //Serial.println(F("\nSend any character to begin DMP programming and demo: "));
    //while (Serial.available() && Serial.read()); // empty buffer
    //while (!Serial.available());                 // wait for data
    //while (Serial.available() && Serial.read()); // empty buffer again

    // load and configure the DMP
    Serial.println(F("Initializing DMP..."));
    devStatus = mpu.dmpInitialize();

    // supply your own gyro offsets here, scaled for min sensitivity
    mpu.setXGyroOffset(220);
    mpu.setYGyroOffset(76);
    mpu.setZGyroOffset(-85);
    mpu.setZAccelOffset(1788/10); // 1688 factory default for my test chip (I have divided it for 10, for remove annoing latency)

    // make sure it worked (returns 0 if so)
    if (devStatus == 0) {
        // turn on the DMP, now that it's ready
        Serial.println(F("Enabling DMP..."));
        mpu.setDMPEnabled(true);

        // enable Arduino interrupt detection
        Serial.println(F("Enabling interrupt detection (Arduino external interrupt 0)..."));
        attachInterrupt(digitalPinToInterrupt(INTERRUPT_PIN), dmpDataReady, RISING);
        mpuIntStatus = mpu.getIntStatus();

        // set our DMP Ready flag so the main loop() function knows it's okay to use it
        Serial.println(F("DMP ready! Waiting for first interrupt..."));
        dmpReady = true;

        // get expected DMP packet size for later comparison
        packetSize = mpu.dmpGetFIFOPacketSize();
    } else {
        // ERROR!
        // 1 = initial memory load failed
        // 2 = DMP configuration updates failed
        // (if it's going to break, usually the code will be 1)
        Serial.print(F("DMP Initialization failed (code "));
        Serial.print(devStatus);
        Serial.println(F(")"));
    }

    // configure LED for output
    pinMode(ROSSO_out, OUTPUT);
    digitalWrite(ROSSO_out, LOW);
}



// ================================================================
// ===                    MAIN PROGRAM LOOP                     ===
// ================================================================

void loop() {
    // if programming failed, don't try to do anything
    if (!dmpReady) return;

    // wait for MPU interrupt or extra packet(s) available
    while (!mpuInterrupt && fifoCount < packetSize) {
        if (mpuInterrupt && fifoCount < packetSize) {
          // try to get out of the infinite loop 
          fifoCount = mpu.getFIFOCount();
        }        
        // other program behavior stuff here
        // .
        // .
        // .
        // if you are really paranoid you can frequently test in between other
        // stuff to see if mpuInterrupt is true, and if so, "break;" from the
        // while() loop to immediately process the MPU data
        // .
        // .
        // .
    }

    // reset interrupt flag and get INT_STATUS byte
    mpuInterrupt = false;
    mpuIntStatus = mpu.getIntStatus();

    // get current FIFO count
    fifoCount = mpu.getFIFOCount();

    // check for overflow (this should never happen unless our code is too inefficient)
    if ((mpuIntStatus & 0x10) || fifoCount == 1024) {
        // reset so we can continue cleanly
        mpu.resetFIFO();
        Serial.println(F("FIFO overflow!"));

    // otherwise, check for DMP data ready interrupt (this should happen frequently)
    } else if (mpuIntStatus & 0x02) {
        // wait for correct available data length, should be a VERY short wait
        while (fifoCount < packetSize) fifoCount = mpu.getFIFOCount();

        // read a packet from FIFO
        mpu.getFIFOBytes(fifoBuffer, packetSize);
        
        // track FIFO count here in case there is > 1 packet available
        // (this lets us immediately read more without waiting for an interrupt)
        fifoCount -= packetSize;

        #ifdef OUTPUT_TEAPOT
            mpu.dmpGetQuaternion(&q, fifoBuffer);
            mpu.dmpGetGravity(&gravity, &q);
            mpu.dmpGetYawPitchRoll(ypr, &q, &gravity);
                            
            static boolean stabilizzato = false;
            static float myYawOffset = 0.0f;

            if (!stabilizzato) {    
              blinkERR();
              
              static long start = millis();      
              boolean stabile = false;

              // Si interrompe quando è stabile su mezzo grado per un secondo                                     
              if ((int)(2.0f * ypr[0] * 180/M_PI) == (int)(myYawOffset)) {
                stabile = true;
              }
              if (stabile) {
                if (start + 1000 < millis()) stabilizzato = true;
              } else {
                start = millis();
              }

              myYawOffset = 2.0f * ypr[0] * 180/M_PI;

              setOffset(&myQuatOffset, &q);
              
              teapotPacket[2] = 0;
              teapotPacket[3] = 0;               
              teapotPacket[4] = 0;
              teapotPacket[5] = 0;               
              teapotPacket[6] = 0;
              teapotPacket[7] = 0;  
              teapotPacket[8] = 0;
              teapotPacket[9] = 0;  
              Serial.write(teapotPacket, 14);
              teapotPacket[11]++; // packetCount, loops at 0xFF on purpose
            }

            if (stabilizzato) {
              digitalWrite(ROSSO_out, HIGH);

              float yaw_1 = getYaw_mine(q);
              float pitch_1 = getPitch_mine(q);
              float roll_1 = getRoll_mine(q);  
  
              float yaw_2 = getYaw_mine(myQuatOffset);
              float yaw_3 = yaw_1 - yaw_2;
              
              if(isnan(yaw_3)) return;
              if(isnan(pitch_1)) return;
              if(isnan(roll_1)) return;

              Quaternion *quat_mine = createFromYawPitchRoll_mine( yaw_3, pitch_1, roll_1 );            

              uint8_t QuatW_byte1 = uint16_t(quat_mine->w * 16384.0f) & 0xFF; // lower byte
              uint8_t QuatW_byte2 = uint16_t(quat_mine->w * 16384.0f) >> 8;   // upper byte              

              uint8_t QuatX_byte1 = uint16_t(quat_mine->x * 16384.0f) & 0xFF; // lower byte
              uint8_t QuatX_byte2 = uint16_t(quat_mine->x * 16384.0f) >> 8;   // upper byte              

              uint8_t QuatY_byte1 = uint16_t(quat_mine->y * 16384.0f) & 0xFF; // lower byte
              uint8_t QuatY_byte2 = uint16_t(quat_mine->y * 16384.0f) >> 8;   // upper byte              

              uint8_t QuatZ_byte1 = uint16_t(quat_mine->z * 16384.0f) & 0xFF; // lower byte
              uint8_t QuatZ_byte2 = uint16_t(quat_mine->z * 16384.0f) >> 8;   // upper byte  

#ifdef STABLE_Z_YAW_START_FROM_ZERO
              teapotPacket[2] = QuatW_byte2;
              teapotPacket[3] = QuatW_byte1;
              teapotPacket[4] = QuatX_byte2;
              teapotPacket[5] = QuatX_byte1;
              teapotPacket[6] = QuatY_byte2;
              teapotPacket[7] = QuatY_byte1;
              teapotPacket[8] = QuatZ_byte2;
              teapotPacket[9] = QuatZ_byte1;
#else              
              teapotPacket[2] = fifoBuffer[0];
              teapotPacket[3] = fifoBuffer[1];
              teapotPacket[4] = fifoBuffer[4];
              teapotPacket[5] = fifoBuffer[5];
              teapotPacket[6] = fifoBuffer[8];
              teapotPacket[7] = fifoBuffer[9];
              teapotPacket[8] = fifoBuffer[12];
              teapotPacket[9] = fifoBuffer[13];
#endif
              Serial.write(teapotPacket, 14);
              teapotPacket[11]++; // packetCount, loops at 0xFF on purpose
            }
        #endif
 
    }
}

void blinkERR(){
    if (DELAY_blink + 500 < millis()) {
      DELAY_blink = millis();  
      if (ROSSO_state == LED_LOW) {
        ROSSO_state = LED_HIGH;   
      }else{
        ROSSO_state = LED_LOW;
      }
      analogWrite(ROSSO_out, ROSSO_state);    // turn the LED on/off by making the voltage by state
    }
}


// Something conversion methos for correct Yaw
    void setOffset(Quaternion *offset, Quaternion *q) {
       offset->w = q->w;
       offset->x = q->x;
       offset->y = q->y;
       offset->z = q->z;
    }

    float getYaw_mine(Quaternion &q)
    {
        float x2 = q.x * q.x;
        float z2 = q.z * q.z;
        return atan2(2.0f * q.z * q.w - 2.0f * q.y * q.x, 1.0f - 2.0f * z2 - 2.0f * x2);
    }

    float getPitch_mine(Quaternion &q)
    {
        return asin(2.0f * q.z * q.y + 2.0f * q.x * q.w);
    }

    float getRoll_mine(Quaternion &q)
    {
        float x2 = q.x * q.x;
        float y2 = q.y * q.y;
        return -atan2(2.0f * q.y * q.w - 2.0f * q.z * q.x, 1.0f - 2.0f * y2 - 2.0f * x2);
    } 
    

    static Quaternion* createFromYawPitchRoll_mine(float yaw, float pitch, float roll) {

        yaw *= 0.5f;
        pitch *= 0.5f;        
        roll *= 0.5f;
        float sinYaw = sin(yaw);
        float cosYaw = cos(yaw);        
        float sinPitch = sin(pitch);
        float cosPitch = cos(pitch);
        float sinRoll = sin(roll);
        float cosRoll = cos(roll);

        Quaternion* q = new Quaternion();
        
#ifdef SWITCH_PITCH_AND_ROLL_AXIS
    #ifdef ROTATE_180_DEGREE_Z_YAW
        q->w =   cosRoll * cosPitch * cosYaw + sinRoll * sinPitch * sinYaw;
        q->x = -(sinRoll * cosPitch * cosYaw - cosRoll * sinPitch * sinYaw);
        q->y = -(cosRoll * sinPitch * cosYaw + sinRoll * cosPitch * sinYaw);
        q->z = -(sinRoll * sinPitch * cosYaw - cosRoll * cosPitch * sinYaw);
    #else   
        q->w = cosRoll * cosPitch * cosYaw + sinRoll * sinPitch * sinYaw;
        q->x = sinRoll * cosPitch * cosYaw - cosRoll * sinPitch * sinYaw;
        q->y = cosRoll * sinPitch * cosYaw + sinRoll * cosPitch * sinYaw;
        q->z = cosRoll * cosPitch * sinYaw - sinRoll * sinPitch * cosYaw;
    #endif          
#else    
    #ifdef ROTATE_180_DEGREE_Z_YAW
        q->w = cosRoll * cosPitch * cosYaw + sinRoll * sinPitch * sinYaw;
        q->x = -(cosRoll * sinPitch * cosYaw + sinRoll * cosPitch * sinYaw);
        q->y = -(cosRoll * sinPitch * sinYaw - sinRoll * cosPitch * cosYaw);
        q->z = -(sinRoll * sinPitch * cosYaw - cosRoll * cosPitch * sinYaw);
    #else           
        q->w = cosRoll * cosPitch * cosYaw + sinRoll * sinPitch * sinYaw;
        q->x = cosRoll * sinPitch * cosYaw + sinRoll * cosPitch * sinYaw;
        q->y = cosRoll * sinPitch * sinYaw - sinRoll * cosPitch * cosYaw;
        q->z = cosRoll * cosPitch * sinYaw - sinRoll * sinPitch * cosYaw;
    #endif          
#endif            
        return q;
    }

