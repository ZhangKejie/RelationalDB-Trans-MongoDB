## This is TransformService for LMS based on Java.##
---

## JDK Version ##
* <strong>JDK 1.8</Strong>

## Spring-Boot Version ##
* <strong>Spring-Boot 1.2.5</Strong>

## TransformService Version ##
* <strong>Ver. 1.7 (Released on 150822)</strong>

---

## Service API##

*   TransformAPI
    
   	POST

	http://${host}:6010/lms/trans 

	发送数据格式:{  
    "company_id":"2",  
    "datatree":{  
        "dbtype": "oracle",  
        "host": "127.0.0.1",  
        "port": "1521",  
        "dbname": "orcl",  
        "username": "test1",  
        "password": "root"  
    },  
    "tables": {  
        "name": "user1",  
        "columns": [  
            "*"  
        ],  
        "fk": "true"  
    	}  
    }  
	
*   InsertAPI
    
   	POST

	http://${host}:6010/lms/insert

	发送数据格式:

	{

	"company_code" : "公司编号 string 外键(company)",  
	"database" : {  
	"name" : "数据库名称 string",  
	"db_type" : "数据库类型 string",  
	"host" : "主机 string",  
	"port" : "端口 int",  
	"password" : "密码 string"  
	},  
	"table" : "表名 string",  
	"sql_id" : "表的主键 string",  
	"get_foreign_key":"取数据库外键 boolean",  
	"colums":[{},{}],  
	"collection_name" : "传输给mongo的对应 string",  
	"operator_id" : "操作用户id object() 外键(user)"  
	}

*   UpdateAPI
    
   	POST

	http://${host}:6010/lms/update

	发送数据格式:

	{  
	"company_code" : "公司编号 String",  
	"database.name" : "以前插入的数据库名称 String",  
	"table" : "表名 String"   
	}  

	
    
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

### Ver. 1.6 (20150820) ###
Add Primary Key.

### Ver. 1.7 (20150822) ###
Add insert and update api! 