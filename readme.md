## This is TransformService for LMS based on Java.##
---

## JDK Version ##
* <strong>JDK 1.8</Strong>

##Spring-Boot Version##
* <strong>Spring-Boot 1.2.5</Strong>

## TransformService Version ##
* <strong>Ver. 1.0 (Released on 150812)</strong>

---

## Transform Examples ##
*	 根据获得的DataBaseType连接不同类型的数据库


		 public void SQLConnect(String dbtype,String url,String port,String dbname,String username,String password){

        	if (dbtype.equals("mysql")){

            	this.MySQLConnect(url,port,dbname,username,password);

       		 }else if (dbtype.equals("oracle")) {

            	this.OracleConnect(url,port,dbname,username,password);

       		 }else if(dbtype.equals("sqlserver")){

            	this.SQLServerConnect(url,port,dbname,username,password);
        	}
    	}
	
	

*   解析json对象的接口

	
		public static Object jsonParse(final String jsonStr) {
        	if (jsonStr == null) throw new NullPointerException("JsonString shouldn't be null");
        	try {
           	 if (isJsonObject(jsonStr)) {  //判断数据为json对象
                final Pattern pattern = Pattern.compile(regex);
                final Matcher matcher = pattern.matcher(jsonStr);
                final Map<String, Object> map = new HashMap<String, Object>();
                final JSONObject jsonObject = new JSONObject(jsonStr);
                try {
                    for (; matcher.find(); ) {//循坏查找符合条件的key
                        String groupName = matcher.group(1);     //利用正在表达式提取出json对象的对象名
                        Object obj = jsonObject.opt(groupName);
                        if (isJsonObject(obj + "") || isJsonArray(obj + "")) {
                            matcher.region(matcher.end() + (obj + "").replace("\\", "").length(), matcher.regionEnd());//设定搜索范围
                            map.put(groupName, jsonParse(obj + ""));//解析内存json对象
                        } else {
                            map.put(groupName, obj + "");//若内层已无json对象直接放入map
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

                return map;
            } else if (isJsonArray(jsonStr)) {//判断数据为json数组
                List<Object> list = new ArrayList<Object>();
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {//循环解析json数组
                        Object object = jsonArray.opt(i);
                        list.add(jsonParse(object + ""));
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                return list;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return jsonStr;}

*	取数据库的方法

		 try {
            dbUtil.rs = dbUtil.stat.executeQuery(sql);
            ResultSetMetaData rsmd = dbUtil.rs.getMetaData();
            while (dbUtil.rs.next()){
                Map<String,Object> jsonObject = new HashMap<String, Object>();
                for (int i=1;i<=rsmd.getColumnCount();i++){
                    String key = rsmd.getColumnName(i);
                    String value = dbUtil.rs.getString(i);
                    //System.out.println(key + ":" + value);
                    jsonObject.put(key,value);
                }
                list.add(jsonObject);
                //JSONObject json = JSONObject.fromObject(jsonObject);
                //jsonArray.add(json);
            }
            jsonArray = JSONArray.fromObject(list);
            str = jsonArray.toString();
           // System.out.println(str);
        } catch (SQLException e) {
            e.printStackTrace();
        }


---

## TransformService History ##
### Ver. 1.0 (20150812) ###
* Original version.


