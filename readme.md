## This is TransformService for LMS based on Java.##
---

## JDK Version ##
* <strong>JDK 1.8</Strong>

## Spring-Boot Version ##
* <strong>Spring-Boot 1.2.5</Strong>

## TransformService Version ##
* <strong>Ver. 1.0 (Released on 150812)</strong>

---

## Service API##

*   TransformServiceAPI
    
   	POST

	http://${host}:6003/lms/trans 

	发送数据格式:{dbtype:"mysql",url:"127.0.0.1",port:"3306",dbname:"chatroom",username:"root",password:"123456",tables:{name:"account",columns:["*"],fk:"true"}}
	

---

## TransformService History ##
### Ver. 1.0 (20150812) ###
* Original version.

### Ver. 1.1 (20150815) ###
*Add foreign keys search.

### Ver. 1.2 (20150815) ###
*Add file lib.

### Ver. 1.3 (20150815) ###
Return error message.

### Ver. 1.4 (20150817) ###
Support Oracle!

### Ver. 1.5 (20150818) ###
All Database feilds are changed to lowercase.