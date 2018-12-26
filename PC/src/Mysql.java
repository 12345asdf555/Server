import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.ParseException;

import net.sf.json.JSONObject;

public class Mysql {

	public java.sql.Connection conn = null;
	public java.sql.Statement stmt =null;
	public String connet;
	public ArrayList<String> listarray2;
	public ArrayList<String> listarray3;
	public ArrayList<String> listarray4 = new ArrayList<String>();
	public DB_Connectionmysql db;
	public int flag=0,over_value = 0,standby_over_value = 0;
	public JSONObject json = new JSONObject();
	public JSONObject standby_json = new JSONObject();
	
	public Mysql() {
		db = new DB_Connectionmysql();
		this.db = db;
	}
    
	public void Mysqlrun1(String str) {
		// TODO Auto-generated constructor stub
		try{
			//System.out.println(str.length());
            if (str.length() == 170) {  

            	//校验第一位是否为FA末位是否为F5
       	     String check1 =str.substring(0,2);
       	     String check11=str.substring(168,170);
       	     if(check1.equals("FA") && check11.equals("F5")){
	        		
       	    	 //校验长度
           	     int check2=str.length();
           	     if(check2==170){
           	        			
           	    	 //校验位校验
               	     String check3=str.substring(2,164);
               	     String check5="";
               	     int check4=0;
               	     for (int i11 = 0; i11 < check3.length()/2; i11++)
               	     {
               	    	String tstr1=check3.substring(i11*2, i11*2+2);
               	    	check4+=Integer.valueOf(tstr1,16);
               	     }
               	     if((Integer.toHexString(check4)).toUpperCase().length()==2){
               	    	check5 = ((Integer.toHexString(check4)).toUpperCase());
               	     }else{
               	    	check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1,3);
               	     }
               	     String check6 = str.substring(164,166);
               	     if(check5.equals(check6)){
               	    	 
               	        				
               	    	 for(int i=0;i<98;i+=46){
               	    		 
               	    		 BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(26+i, 30+i).toString(),16)); //电流        
                             BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(30+i, 34+i).toString(),16));     //电压
                             
                             BigDecimal wirefeedrate = new BigDecimal(Integer.valueOf(str.subSequence(40+i, 44+i).toString(),16)); //送丝速度
                             BigDecimal weldingrate = new BigDecimal(Integer.valueOf(str.subSequence(44+i, 48+i).toString(),16));  //焊接速度
                             BigDecimal weldheatinput = new BigDecimal(Integer.valueOf(str.subSequence(48+i, 52+i).toString(),16)); //焊接热输入        
                             BigDecimal hatwirecurrent = new BigDecimal(Integer.valueOf(str.subSequence(52+i, 56+i).toString(),16)); //热丝电流
                             BigDecimal vibrafrequency = new BigDecimal(Integer.valueOf(str.subSequence(56+i, 60+i).toString(),16)); //振动频率
                             
                                                         
                      
                             long sensor_Num1 = Integer.valueOf(str.subSequence(34+i, 38+i).toString(),16);   //传感器（流量）
                             String sensor_Num = String.valueOf(sensor_Num1);
                             if(sensor_Num.length()<4){
                            	 int num=4-sensor_Num.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 sensor_Num="0"+sensor_Num;
                            	 }
                             }
                             long machine_id1 = Integer.valueOf(str.subSequence(10, 14).toString(),16);
                             String machine_id = String.valueOf(machine_id1);
                             if(machine_id.length()<4){
                            	 int num=4-machine_id.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 machine_id="0"+machine_id;
                            	 }
                             }
                             long welder_id1 = Integer.valueOf(str.subSequence(14, 18).toString(),16);
                             String welder_id = String.valueOf(welder_id1);
                             if(welder_id.length()<4){
                            	 int num=4-welder_id.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 welder_id="0"+welder_id;
                            	 }
                             }
                             long code1 = Integer.valueOf(str.subSequence(18, 26).toString(),16);
                             String code = String.valueOf(code1);
                             if(code.length()<8){
                            	 int num=8-code.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 code="0"+code;
                            	 }
                             }
                             
                             
                             long year = Integer.valueOf(str.subSequence(60+i, 62+i).toString(),16);   //年
                             String yearstr = String.valueOf(year);
                             long month = Integer.valueOf(str.subSequence(62+i, 64+i).toString(),16);  //月
                             String monthstr = String.valueOf(month);
                             long day = Integer.valueOf(str.subSequence(64+i, 66+i).toString(),16);    //日
                             String daystr = String.valueOf(day);
                             long hour = Integer.valueOf(str.subSequence(66+i, 68+i).toString(),16);   //时
                             String hourstr = String.valueOf(hour);
                             long minute = Integer.valueOf(str.subSequence(68+i, 70+i).toString(),16);  //分
                             String minutestr = String.valueOf(minute);
                             long second = Integer.valueOf(str.subSequence(70+i, 72+i).toString(),16);  //秒
                             String secondstr = String.valueOf(second);
                             int status = Integer.parseInt(str.subSequence(38+i, 40+i).toString(),16);   //状态
               	    		 
                             String timestr = yearstr+"-"+monthstr+"-"+daystr+" "+hourstr+":"+minutestr+":"+secondstr;
                             
                             try {
	                             Date time = DateTools.parse("yy-MM-dd HH:mm:ss",timestr);
                            	 //java.util.Date time1 = timeshow.parse(timestr);
								 Timestamp timesql = new Timestamp(time.getTime());
	
	               	    		 /*BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(26+i, 30+i).toString(),16));
	                             BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(30+i, 34+i).toString(),16));
	                             long sensor_Num = Integer.valueOf(str.subSequence(34+i, 38+i).toString(),16);
	                             long machine_id = Integer.valueOf(str.subSequence(10, 14).toString(),16);
	                             long welder_id = Integer.valueOf(str.subSequence(14, 18).toString(),16);
	                             long code = Integer.valueOf(str.subSequence(18, 26).toString(),16);
	                             long year = Integer.valueOf(str.subSequence(40+i, 42+i).toString(),16);
	                             long month = Integer.valueOf(str.subSequence(42+i, 44+i).toString(),16);
	                             long day = Integer.valueOf(str.subSequence(44+i, 46+i).toString(),16);
	                             long hour = Integer.valueOf(str.subSequence(46+i, 48+i).toString(),16);
	                             long minute = Integer.valueOf(str.subSequence(48+i, 50+i).toString(),16);
	                             long second = Integer.valueOf(str.subSequence(50+i, 52+i).toString(),16);
	                             int status = Integer.parseInt(str.subSequence(38+i, 40+i).toString());*/
	                                	
								 String fitemid = str.substring(166, 168);
							 
								 db.DB_Connectionmysqlrun(electricity,voltage,sensor_Num,status,wirefeedrate,weldingrate,weldheatinput,hatwirecurrent,vibrafrequency,machine_id,welder_id,code,fitemid,timesql,listarray2);
								 //System.out.println(str);
                             } catch (Exception e) {
								str="";
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

               	    	 }
 	                     //System.out.println(str);
               	    	 //db=null;
               	    	 //System.gc();
                   	     str="";
               	     }
               	        			
               	     else{
               	        //校锟斤拷位锟斤拷锟斤拷
               	    	 System.out.print("str");
               	    	 System.out.print("数据接收校验位错误");
               	    	 str="";
               	     }
                               
           	     }
           	        		
           	     else{
           	        //锟斤拷锟饺达拷锟斤拷
           	    	 System.out.print("数据接收长度错误");
           	    	 str="";
           	     }
       	        		
   	        	}
   	        	else{
   	        		//锟斤拷位锟斤拷锟斤拷FA
   	        		System.out.print("数据接收首末位错误");
   	        		str="";
   	        	}
       	     
           }/*else if(str.length()>=300 && str.length()!= 118){
        	
       	    	String [] stringArr = str.split("FD");
       	    	
                for(int i =0;i < stringArr.length;i++)
		        {
	        	     //鏍￠獙绗竴浣嶆槸鍚︿负FE
		       	     String check1 =stringArr[i].substring(0,2);
		       	     if(check1.equals("FE")){
		       	    	 
		       	    	 //鏍￠獙闀垮害
		           	     int check2=stringArr[i].length();
		           	     if(check2==54){
		           	    	 
                        	 if(stringArr[i].length()>30){
                        		 
                        		BigDecimal electricity = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(4, 8).toString(),16));
                                BigDecimal voltage = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(8, 12).toString(),16));
                                BigInteger sensor_Num = new BigInteger(stringArr[i].subSequence(12, 16).toString());
                                String machine_id = stringArr[i].subSequence(16, 20).toString();
                                String welder_id = stringArr[i].subSequence(20, 24).toString();
                                String code = stringArr[i].subSequence(24, 32).toString();
                                BigInteger year = new BigInteger(Integer.valueOf(stringArr[i].subSequence(32, 34).toString(),16).toString());
                                BigInteger month = new BigInteger(Integer.valueOf(stringArr[i].subSequence(34, 36).toString(),16).toString());
                                BigInteger day = new BigInteger(Integer.valueOf(stringArr[i].subSequence(36, 38).toString(),16).toString());
                                BigInteger hour = new BigInteger(Integer.valueOf(stringArr[i].subSequence(38, 40).toString(),16).toString());
                                BigInteger minute = new BigInteger(Integer.valueOf(stringArr[i].subSequence(40, 42).toString(),16).toString());
                                BigInteger second = new BigInteger(Integer.valueOf(stringArr[i].subSequence(42, 44).toString(),16).toString());
                                Integer status = Integer.valueOf(stringArr[i].subSequence(44, 46).toString());
                        	
                                DB_Connectionandroid a = new DB_Connectionandroid(electricity,voltage,sensor_Num,machine_id,welder_id,code,year,month,day,hour,minute,second,status,connet); 
                                str="";
                                
                        	 } 
		           	     
		           	     }   
		           	     else{
		           	    //锟斤拷锟饺达拷锟斤拷
		           	    	 System.out.print("鏁版嵁鎺ユ敹闀垮害閿欒");
		           	    	 str="";
		           	     }
	       	         }
		       	     else{
		       	    	 //锟斤拷位锟斤拷锟斤拷FE
		       	    	System.out.println("12");
		   	        	 System.out.print("鏁版嵁鎺ユ敹棣栨湯浣嶉敊璇�");
		   	        	 str="";
		       	     }
	       	     }
        	   
	           }else if(str.length() == 118){
	        	   
	        	   str="";
	        	   
	           }*/
            
		} catch (Exception e) {
			str="";
            System.out.println("S: Error 2");  
            e.printStackTrace();  
        } 
	}
	
	public void Mysqlrun(NettyServerHandler context, String str) {
		// TODO Auto-generated constructor stub
		NettyServerHandler NS = context;
		try{
			
            if (str.length() == 110) {  

            //校验第一位是否为FA末位是否为F5
       	     String check1 =str.substring(0,2);
       	     String check11=str.substring(108,110);
       	     if(check1.equals("FA") && check11.equals("F5")){
	        		
       	    	 //校验长度
           	     int check2=str.length();
           	     if(check2==110){
           	        			
           	    	 //校验位校验
               	     String check3=str.substring(2,104);
               	     String check5="";
               	     int check4=0;
               	     for (int i11 = 0; i11 < check3.length()/2; i11++)
               	     {
               	    	String tstr1=check3.substring(i11*2, i11*2+2);
               	    	check4+=Integer.valueOf(tstr1,16);
               	     }
               	     if((Integer.toHexString(check4)).toUpperCase().length()==2){
               	    	check5 = ((Integer.toHexString(check4)).toUpperCase());
               	     }else{
               	    	check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1,3);
               	     }
               	     String check6 = str.substring(104,106);
               	     if(check5.equals(check6)){
               	        				
               	    	 for(int i=0;i<78;i+=26){
               	    		 try{
               	    			 BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(26+i, 30+i).toString(),16));
                                 BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(30+i, 34+i).toString(),16));
                                 long sensor_Num1 = Integer.valueOf(str.subSequence(34+i, 38+i).toString(),16);
                                 String sensor_Num = String.valueOf(sensor_Num1);
                                 if(sensor_Num.length()<4){
                                	 int num=4-sensor_Num.length();
                                	 for(int i1=0;i1<num;i1++){
                                		 sensor_Num="0"+sensor_Num;
                                	 }
                                 }
                                 long machine_id1 = Integer.valueOf(str.subSequence(10, 14).toString(),16);
                                 String machine_id = String.valueOf(machine_id1);
                                 if(machine_id.length()<4){
                                	 int num=4-machine_id.length();
                                	 for(int i1=0;i1<num;i1++){
                                		 machine_id="0"+machine_id;
                                	 }
                                 }
                                 long welder_id1 = Integer.valueOf(str.subSequence(14, 18).toString(),16);
                                 String welder_id = String.valueOf(welder_id1);
                                 if(welder_id.length()<4){
                                	 int num=4-welder_id.length();
                                	 for(int i1=0;i1<num;i1++){
                                		 welder_id="0"+welder_id;
                                	 }
                                 }
                                 long code1 = Integer.valueOf(str.subSequence(18, 26).toString(),16);
                                 String code = String.valueOf(code1);
                                 if(code.length()<8){
                                	 int num=8-code.length();
                                	 for(int i1=0;i1<num;i1++){
                                		 code="0"+code;
                                	 }
                                 }
                                 long year = Integer.valueOf(str.subSequence(40+i, 42+i).toString(),16);
                                 String yearstr = String.valueOf(year);
                                 long month = Integer.valueOf(str.subSequence(42+i, 44+i).toString(),16);
                                 String monthstr = String.valueOf(month);
                                 long day = Integer.valueOf(str.subSequence(44+i, 46+i).toString(),16);
                                 String daystr = String.valueOf(day);
                                 long hour = Integer.valueOf(str.subSequence(46+i, 48+i).toString(),16);
                                 String hourstr = String.valueOf(hour);
                                 long minute = Integer.valueOf(str.subSequence(48+i, 50+i).toString(),16);
                                 String minutestr = String.valueOf(minute);
                                 long second = Integer.valueOf(str.subSequence(50+i, 52+i).toString(),16);
                                 String secondstr = String.valueOf(second);
                                 int status = Integer.parseInt(str.subSequence(38+i, 40+i).toString(),16);
                   	    		 
                                 String timestr = yearstr+"-"+monthstr+"-"+daystr+" "+hourstr+":"+minutestr+":"+secondstr;
                                 
	                             Date time = DateTools.parse("yy-MM-dd HH:mm:ss",timestr);
                            	 //java.util.Date time1 = timeshow.parse(timestr);
								 Timestamp timesql = new Timestamp(time.getTime());
	
	               	    		 /*BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(26+i, 30+i).toString(),16));
	                             BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(30+i, 34+i).toString(),16));
	                             long sensor_Num = Integer.valueOf(str.subSequence(34+i, 38+i).toString(),16);
	                             long machine_id = Integer.valueOf(str.subSequence(10, 14).toString(),16);
	                             long welder_id = Integer.valueOf(str.subSequence(14, 18).toString(),16);
	                             long code = Integer.valueOf(str.subSequence(18, 26).toString(),16);
	                             long year = Integer.valueOf(str.subSequence(40+i, 42+i).toString(),16);
	                             long month = Integer.valueOf(str.subSequence(42+i, 44+i).toString(),16);
	                             long day = Integer.valueOf(str.subSequence(44+i, 46+i).toString(),16);
	                             long hour = Integer.valueOf(str.subSequence(46+i, 48+i).toString(),16);
	                             long minute = Integer.valueOf(str.subSequence(48+i, 50+i).toString(),16);
	                             long second = Integer.valueOf(str.subSequence(50+i, 52+i).toString(),16);
	                             int status = Integer.parseInt(str.subSequence(38+i, 40+i).toString());*/
	                                	
								 String fitemid = str.substring(106, 108);
							 
								 db.DB_Connectionmysqlrun1(electricity,voltage,sensor_Num,machine_id,welder_id,code,status,fitemid,timesql,listarray2);

    								 
    								/*if(flag==0){ 
    	 								ResultSet dictionary = null;
    		 							String dic_str = "SELECT fvaluename FROM tb_dictionary WHERE fvalue='82' OR fvalue='83'";
    	 								try {
    	 									
    	 									if(stmt==null || stmt.isClosed()==true || !conn.isValid(1))
    	 		        		        	{
    	 		        		        		try {
    	 		        							Class.forName("com.mysql.jdbc.Driver");
    	 		        							conn = DriverManager.getConnection(connet);
    	 		        							stmt = conn.createStatement();
    	 		        		        	    } catch (ClassNotFoundException e) {  
    	 		        		                    System.out.println("Broken driver");
    	 		        		                    e.printStackTrace();
    	 		        		                    return;
    	 		        		                } catch (SQLException e) {
    	 		        		                    System.out.println("Broken conn");
    	 		        		                    e.printStackTrace();
    	 		        		                    return;
    	 		        		                }  
    	 		        		        	}
    	 									
    	 									dictionary =stmt.executeQuery(dic_str);
    	 									dictionary.next();
    	 									over_value=dictionary.getInt(1);
    	 									dictionary.next();
    	 									standby_over_value=dictionary.getInt(1)*60;
    	 									flag=1;
    	 								} catch (SQLException e) {
    	 									e.printStackTrace();
    	 								}
    								 }*/
								 try {
									
    								if(stmt==null || stmt.isClosed()==true || !conn.isValid(1))
    	        		        	{
    	        		        		try {
    	        							Class.forName("com.mysql.jdbc.Driver");
    	        							conn = DriverManager.getConnection(connet);
    	        							stmt = conn.createStatement();
    	        		        	    } catch (ClassNotFoundException e) {  
    	        		                    System.out.println("Broken driver");
    	        		                    e.printStackTrace();
    	        		                    return;
    	        		                } catch (SQLException e) {
    	        		                    System.out.println("Broken conn");
    	        		                    e.printStackTrace();
    	        		                    return;
    	        		                }  
    	        		        	}
    								 
    								 String weldid = null;
    								 for(int j=0;j<listarray2.size();j+=4){
    					               	 	if(machine_id.equals(listarray2.get(j+2))){
    					               	 	weldid = listarray2.get(j);
    					               	 		break;
    					               	 	}
    					             }
    								 BigDecimal maxelectricity = null;
    								 BigDecimal minelectricity = null;
    							     BigDecimal maxvoltage = null;
    								 BigDecimal minvoltage = null;
    		                    	 for(int k=0;k<listarray3.size();k+=5){
    		                    		 String weldjunction = listarray3.get(k);
    		                    		 if(weldjunction.equals(code)){
    		                    			 maxelectricity = new BigDecimal(listarray3.get(k+1));
    		                    			 minelectricity = new BigDecimal(listarray3.get(k+2));
    		                    			 maxvoltage = new BigDecimal(listarray3.get(k+3));
    		                    			 minvoltage = new BigDecimal(listarray3.get(k+4));
    		                    			 
    		                    		 }
    		                    	 }
//    		                    	 System.out.println(status);
    		    					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		    					if(maxelectricity == null || maxelectricity.equals("null")){
    		    						maxelectricity = new BigDecimal(0);
    		    					}
    		    					if(minelectricity == null || minelectricity.equals("null")){
    		    						minelectricity = new BigDecimal(0);
    		    					}
    		    					if(minvoltage == null || minvoltage.equals("null")){
    		    						minvoltage = new BigDecimal(0);
    		    					}
    		    					if(maxvoltage == null || maxvoltage.equals("null")){
    		    						maxvoltage = new BigDecimal(0);
    		    					}
    		    					synchronized (json) {
	    		    					if((electricity.compareTo(maxelectricity)==1||electricity.compareTo(minelectricity)==(-1)) && status==3){
	    		    						if(weldid!=null){
	    			    						if(json.has(String.valueOf(weldid))){
	    			    							json.put(weldid,json.get(weldid)+String.valueOf(electricity)+","+voltage+","+df.format(time)+","+maxelectricity+","+minelectricity+","+maxvoltage+","+minvoltage+";");
	    			    						}else{
	    			    							json.put(weldid,electricity+","+voltage+","+df.format(time)+","+maxelectricity+","+minelectricity+","+maxvoltage+","+minvoltage+";");
	    			    						}
	    		    						}
	    		    					}else{
	    		    						if(!json.isEmpty()){
	    		    							if(json.containsKey(String.valueOf(weldid))){
	    			    						String body_str[] = json.get(String.valueOf(weldid)).toString().split(";");
	    			    						if(body_str.length>=over_value){//over_value
	    			    							List<String> bodytimeList = new ArrayList<String>();
	    			    							for(int bodytime=0;bodytime<body_str.length;bodytime++){
	    			    								bodytimeList.add(body_str[bodytime].split(",")[2]);
	    			    							}
	    			    							Collections.sort(bodytimeList);
	    			    							String first_body_detail = bodytimeList.get(0);
	    		    								String last_body_detail = bodytimeList.get(bodytimeList.size()-1);
	    		    								BigInteger overtime;
//	    		    								overtime = new BigInteger(String.valueOf((df.parse(last_body_detail[2]).getTime()-df.parse(first_body_detail[2]).getTime())/1000+1));
	    		    								overtime = new BigInteger(String.valueOf(body_str.length));
	    		    								//System.out.println(overtime);
	    		    								ResultSet id = null;
	    		    								String sqlhead = "INSERT INTO tb_over_head"
	    		    										+ "(fwelder_id, fmachine_id, fjunction_id, fitemid, fstarttime, fendtime, fovertime) "
	    		    										+ "VALUES ("+welder_id+","+weldid+","+code+","+fitemid+",'"+first_body_detail+"','"+last_body_detail+"','"+overtime+"')";
	    		    								String findid = "SELECT @@IDENTITY AS id";
	    		    								try {
	    		    									stmt.execute(sqlhead);
	    		    									id =stmt.executeQuery(findid);
	    		    									id.next();
	    		    								} catch (SQLException e) {
	    		    									e.printStackTrace();
	    		    								}
	    	    									String sqlbody = "INSERT INTO tb_over_body (fhead_id, felectricity, fvoltage, FWeldTime, fmax_electricity, fmin_electricity, fmax_voltage, fmin_voltage) VALUES ";
	    			    							for(int i1=0;i1<body_str.length;i1++){
	    			    								String body_detail[] = body_str[i1].split(",");
	    			    								if(null!=id.getString(1)){
	    		    										sqlbody += "("+id.getString(1)+","+body_detail[0]+","+body_detail[1]+",'"+body_detail[2]+"',"+body_detail[3]+","+body_detail[4]+","+body_detail[5]+","+body_detail[6]+"),";
	    			    								}
	    			    							}
	    	    									try {
	    	    										int len = sqlbody.length();
	    	    										stmt.execute(sqlbody.substring(0, len-1));
	    	    										
	    	    									} catch (SQLException e) {
	    	    										e.printStackTrace();
	    	    									}
	    			    							body_str=null;
	    			    							json.remove(weldid);
	    			    						}else{
	    			    							body_str=null;
	    			    							json.remove(weldid);
	    			    						}
	    		    						}
	    		    						}
	    		    					}
    		    					}
    		    					synchronized(standby_json){
    		    						synchronized(listarray4){
		    		    					if(status==0){
		    		    						if(weldid!=null){
		    			    						if(standby_json.has(String.valueOf(weldid))){
		    			    							standby_json.put(weldid,standby_json.get(weldid)+String.valueOf(electricity)+","+voltage+","+df.format(time)+","+maxelectricity+","+minelectricity+","+maxvoltage+","+minvoltage+";");
		    			    						}else{
		    			    							standby_json.put(weldid,electricity+","+voltage+","+df.format(time)+","+maxelectricity+","+minelectricity+","+maxvoltage+","+minvoltage+";");
		    			    						}
		    			    						String head_length[] = standby_json.get(weldid).toString().split(";");
		    			    						if(head_length.length>standby_over_value){
		    			    							String fmachid=weldid;
		    		    								if(fmachid.length()!=4){
		    						                       	 int lenth=4-fmachid.length();
		    						                       	 for(int i1=0;i1<lenth;i1++){
		    						                       		fmachid="0"+fmachid;
		    						                       	 }
		    					                         }
		    			    							if(!listarray4.contains(fmachid)){
		    			    								listarray4.add(fmachid);
		    			    							}
		    			    						}
		    		    						}
		    		    					}else{
		    		    						if(!standby_json.isEmpty()){
		    		    							boolean fl = standby_json.containsKey(weldid);
		    		    							if(standby_json.containsKey(weldid)){
		    				    						String shead_str[] = standby_json.get(String.valueOf(weldid)).toString().split(";");
		    				    						if(shead_str.length>=standby_over_value){//standby_over_value
		    				    							List<String> standtimeList = new ArrayList<String>();
			    			    							for(int bodytime=0;bodytime<shead_str.length;bodytime++){
			    			    								standtimeList.add(shead_str[bodytime].split(",")[2]);
			    			    							}
			    			    							Collections.sort(standtimeList);
		    				    							String first_shead_detail = standtimeList.get(0);
		    			    								String last_shead_detail = standtimeList.get(standtimeList.size()-1);
		    			    								BigInteger overtime;
//		    			    								overtime = new BigInteger(String.valueOf((df.parse(last_shead_detail[2]).getTime()-df.parse(first_shead_detail[2]).getTime())/1000+1));
		    	//		    								System.out.println(overtime);
		    			    								overtime = new BigInteger(String.valueOf(shead_str.length));
		    			    								ResultSet id = null;
		    			    								String sqlhead = "INSERT INTO tb_standby_over"
		    			    										+ "(fwelder_id, fmachine_id, fjunction_id, fitemid, fstarttime, fendtime, fovertime) "
		    			    										+ "VALUES ("+welder_id+","+weldid+","+code+","+fitemid+",'"+first_shead_detail+"','"+last_shead_detail+"','"+overtime+"')";
		    			    								stmt.execute(sqlhead);
		    			    								shead_str=null;
		    			    								standby_json.remove(weldid);
		    			    								String fmachid=weldid;
		    			    								if(fmachid.length()!=4){
		    							                       	 int lenth=4-fmachid.length();
		    							                       	 for(int i1=0;i1<lenth;i1++){
		    							                       		fmachid="0"+fmachid;
		    							                       	 }
		    						                         }
		    				    							listarray4.remove(fmachid);
		    				    						}else{
		    				    							shead_str=null;
		    				    							standby_json.remove(weldid);
		    				    							String fmachid=weldid;
		    			    								if(fmachid.length()!=4){
		    							                       	 int lenth=4-fmachid.length();
		    							                       	 for(int i1=0;i1<lenth;i1++){
		    							                       		fmachid="0"+fmachid;
		    							                       	 }
		    						                         }
		    				    							listarray4.remove(fmachid);
		    				    						}
		    			    						}
		    		    						}
		    		    					}
		    								 
		    		    					NS.websocket.listarray4 = listarray4;
	    		    					}
    		    					}
    								 //System.out.println(str);
                                 } catch (Exception e) {
                                	e.printStackTrace();
                	    			System.out.println(str);
                          	    	System.out.println("sumerror");
    							 }
                                 
               	    		 }catch(Exception e){
               	    			e.printStackTrace();
               	    			System.out.println(str);
                      	    	System.out.println("readerror");
               	    		 }
               	    	 }
 	                     //System.out.println(str);
               	    	 //db=null;
               	    	 //System.gc();
                   	     str="";
               	     }
               	        			
               	     else{
               	    	System.out.println(str);
               	    	System.out.println("counterror");
               	    	str="";
               	     }
                               
           	     }
           	        		
           	     else{
           	    	System.out.println("numerror");
           	    	str="";
           	     }
       	        		
   	        	}
   	        	else{
   	        		//��λ����FA
   	        		System.out.println("11");
   	        		System.out.println("headerror");
   	        		str="";
   	        	}
       	     
           }/*else if(str.length()>=300 && str.length()!= 118){
        	
       	    	String [] stringArr = str.split("FD");
       	    	
                for(int i =0;i < stringArr.length;i++)
		        {
	        	     //校验第一位是否为FE
		       	     String check1 =stringArr[i].substring(0,2);
		       	     if(check1.equals("FE")){
		       	    	 
		       	    	 //校验长度
		           	     int check2=stringArr[i].length();
		           	     if(check2==54){
		           	    	 
                        	 if(stringArr[i].length()>30){
                        		 
                        		BigDecimal electricity = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(4, 8).toString(),16));
                                BigDecimal voltage = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(8, 12).toString(),16));
                                BigInteger sensor_Num = new BigInteger(stringArr[i].subSequence(12, 16).toString());
                                String machine_id = stringArr[i].subSequence(16, 20).toString();
                                String welder_id = stringArr[i].subSequence(20, 24).toString();
                                String code = stringArr[i].subSequence(24, 32).toString();
                                BigInteger year = new BigInteger(Integer.valueOf(stringArr[i].subSequence(32, 34).toString(),16).toString());
                                BigInteger month = new BigInteger(Integer.valueOf(stringArr[i].subSequence(34, 36).toString(),16).toString());
                                BigInteger day = new BigInteger(Integer.valueOf(stringArr[i].subSequence(36, 38).toString(),16).toString());
                                BigInteger hour = new BigInteger(Integer.valueOf(stringArr[i].subSequence(38, 40).toString(),16).toString());
                                BigInteger minute = new BigInteger(Integer.valueOf(stringArr[i].subSequence(40, 42).toString(),16).toString());
                                BigInteger second = new BigInteger(Integer.valueOf(stringArr[i].subSequence(42, 44).toString(),16).toString());
                                Integer status = Integer.valueOf(stringArr[i].subSequence(44, 46).toString());
                        	
                                DB_Connectionandroid a = new DB_Connectionandroid(electricity,voltage,sensor_Num,machine_id,welder_id,code,year,month,day,hour,minute,second,status,connet); 
                                str="";
                                
                        	 } 
		           	     
		           	     }   
		           	     else{
		           	    //���ȴ���
		           	    	 System.out.print("数据接收长度错误");
		           	    	 str="";
		           	     }
	       	         }
		       	     else{
		       	    	 //��λ����FE
		       	    	System.out.println("12");
		   	        	 System.out.print("数据接收首末位错误");
		   	        	 str="";
		       	     }
	       	     }
        	   
	           }else if(str.length() == 118){
	        	   
	        	   str="";
	        	   
	           }*/
            
		} catch (Exception e) {
			str="";
            System.out.println("S: Error 2");  
            e.printStackTrace();  
        } 
	}
		


	/*@Override
	public void taskResult(String str,String connet,ArrayList<String> listarray1,ArrayList<String> listarray2,ArrayList<String> listarray3,HashMap<String, Socket> websocket,String ip1) {
		// TODO Auto-generated method stub
		
		this.str=str;
        this.connet=connet;
        //this.listarray1=listarray1;
		
		try{
			
            if (str.length() == 110) {  

            //У���һλ�Ƿ�ΪFAĩλ�Ƿ�ΪF5
       	     String check1 =str.substring(0,2);
       	     String check11=str.substring(108,110);
       	     if(check1.equals("FA") && check11.equals("F5")){
	        		
           	     //У�鳤��
           	     int check2=str.length();
           	     if(check2==110){
           	        			
               	     //У��λУ��
               	     String check3=str.substring(2,104);
               	     String check5="";
               	     int check4=0;
               	     for (int i11 = 0; i11 < check3.length()/2; i11++)
               	     {
               	    	String tstr1=check3.substring(i11*2, i11*2+2);
               	    	check4+=Integer.valueOf(tstr1,16);
               	     }
               	     if((Integer.toHexString(check4)).toUpperCase().length()==2){
               	    	check5 = ((Integer.toHexString(check4)).toUpperCase());
               	     }else{
               	    	check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1,3);
               	     }
               	     String check6 = str.substring(104,106);
               	     if(check5.equals(check6)){
               	        				
               	    	 for(int i=0;i<78;i+=26){
               	    		 
               	    		 BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(26+i, 30+i).toString(),16));
                             BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(30+i, 34+i).toString(),16));
                             long sensor_Num1 = Integer.valueOf(str.subSequence(34+i, 38+i).toString(),16);
                             String sensor_Num = String.valueOf(sensor_Num1);
                             if(sensor_Num.length()<4){
                            	 int num=4-sensor_Num.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 sensor_Num="0"+sensor_Num;
                            	 }
                             }
                             long machine_id1 = Integer.valueOf(str.subSequence(10, 14).toString(),16);
                             String machine_id = String.valueOf(machine_id1);
                             if(machine_id.length()<4){
                            	 int num=4-machine_id.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 machine_id="0"+machine_id;
                            	 }
                             }
                             long welder_id1 = Integer.valueOf(str.subSequence(14, 18).toString(),16);
                             String welder_id = String.valueOf(welder_id1);
                             if(welder_id.length()<4){
                            	 int num=4-welder_id.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 welder_id="0"+welder_id;
                            	 }
                             }
                             long code1 = Integer.valueOf(str.subSequence(18, 26).toString(),16);
                             String code = String.valueOf(code1);
                             if(code.length()<8){
                            	 int num=8-code.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 code="0"+code;
                            	 }
                             }
                             long year = Integer.valueOf(str.subSequence(40+i, 42+i).toString(),16);
                             String yearstr = String.valueOf(year);
                             long month = Integer.valueOf(str.subSequence(42+i, 44+i).toString(),16);
                             String monthstr = String.valueOf(month);
                             long day = Integer.valueOf(str.subSequence(44+i, 46+i).toString(),16);
                             String daystr = String.valueOf(day);
                             long hour = Integer.valueOf(str.subSequence(46+i, 48+i).toString(),16);
                             String hourstr = String.valueOf(hour);
                             long minute = Integer.valueOf(str.subSequence(48+i, 50+i).toString(),16);
                             String minutestr = String.valueOf(minute);
                             long second = Integer.valueOf(str.subSequence(50+i, 52+i).toString(),16);
                             String secondstr = String.valueOf(second);
                             int status = Integer.parseInt(str.subSequence(38+i, 40+i).toString());
               	    		 
                             String timestr = yearstr+"-"+monthstr+"-"+daystr+" "+hourstr+":"+minutestr+":"+secondstr;
                             try {
	                            Date time = DateTools.parse("yy-MM-dd HH:mm:ss",timestr);
                            	//java.util.Date time1 = timeshow.parse(timestr);
								Timestamp timesql = new Timestamp(time.getTime());

               	    		 BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(26+i, 30+i).toString(),16));
                             BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(30+i, 34+i).toString(),16));
                             long sensor_Num = Integer.valueOf(str.subSequence(34+i, 38+i).toString(),16);
                             long machine_id = Integer.valueOf(str.subSequence(10, 14).toString(),16);
                             long welder_id = Integer.valueOf(str.subSequence(14, 18).toString(),16);
                             long code = Integer.valueOf(str.subSequence(18, 26).toString(),16);
                             long year = Integer.valueOf(str.subSequence(40+i, 42+i).toString(),16);
                             long month = Integer.valueOf(str.subSequence(42+i, 44+i).toString(),16);
                             long day = Integer.valueOf(str.subSequence(44+i, 46+i).toString(),16);
                             long hour = Integer.valueOf(str.subSequence(46+i, 48+i).toString(),16);
                             long minute = Integer.valueOf(str.subSequence(48+i, 50+i).toString(),16);
                             long second = Integer.valueOf(str.subSequence(50+i, 52+i).toString(),16);
                             int status = Integer.parseInt(str.subSequence(38+i, 40+i).toString());
                                	
								String fitemid = str.substring(106, 108);
							 
							//DB_Connectionmysql a = new DB_Connectionmysql(electricity,voltage,sensor_Num,machine_id,welder_id,code,status,fitemid,timesql,connet,listarray1);
							} catch (Exception e) {
								str="";
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

               	    	 }
 	                    //System.out.println(str);
                   	    str="";
               	     }
               	        			
               	     else{
               	        //У��λ����
               	    	 System.out.print("���ݽ���У��λ����");
               	    	 str="";
               	     }
                               
           	     }
           	        		
           	     else{
           	        //���ȴ���
           	    	 System.out.print("���ݽ��ճ��ȴ���");
           	    	 str="";
           	     }
       	        		
   	        	}
   	        	else{
   	        		//��λ����FA
   	        		System.out.println("11");
   	        		System.out.print("���ݽ�����ĩλ����");
   	        		str="";
   	        	}
       	     
           }else if(str.length()>=300 && str.length()!= 118){
        	
       	    	String [] stringArr = str.split("FD");
       	    	
                for(int i =0;i < stringArr.length;i++)
		        {
	        	     //У���һλ�Ƿ�ΪFE
		       	     String check1 =stringArr[i].substring(0,2);
		       	     if(check1.equals("FE")){
		       	    	 
		       	    	 //У�鳤��
		           	     int check2=stringArr[i].length();
		           	     if(check2==54){
		           	    	 
                        	 if(stringArr[i].length()>30){
                        		 
                        		BigDecimal electricity = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(4, 8).toString(),16));
                                BigDecimal voltage = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(8, 12).toString(),16));
                                BigInteger sensor_Num = new BigInteger(stringArr[i].subSequence(12, 16).toString());
                                String machine_id = stringArr[i].subSequence(16, 20).toString();
                                String welder_id = stringArr[i].subSequence(20, 24).toString();
                                String code = stringArr[i].subSequence(24, 32).toString();
                                BigInteger year = new BigInteger(Integer.valueOf(stringArr[i].subSequence(32, 34).toString(),16).toString());
                                BigInteger month = new BigInteger(Integer.valueOf(stringArr[i].subSequence(34, 36).toString(),16).toString());
                                BigInteger day = new BigInteger(Integer.valueOf(stringArr[i].subSequence(36, 38).toString(),16).toString());
                                BigInteger hour = new BigInteger(Integer.valueOf(stringArr[i].subSequence(38, 40).toString(),16).toString());
                                BigInteger minute = new BigInteger(Integer.valueOf(stringArr[i].subSequence(40, 42).toString(),16).toString());
                                BigInteger second = new BigInteger(Integer.valueOf(stringArr[i].subSequence(42, 44).toString(),16).toString());
                                Integer status = Integer.valueOf(stringArr[i].subSequence(44, 46).toString());
                        	
                                DB_Connectionandroid a = new DB_Connectionandroid(electricity,voltage,sensor_Num,machine_id,welder_id,code,year,month,day,hour,minute,second,status,connet); 
                                str="";
                                
                        	 } 
		           	     
		           	     }   
		           	     else{
		           	    //���ȴ���
		           	    	 System.out.print("���ݽ��ճ��ȴ���");
		           	    	 str="";
		           	     }
	       	         }
		       	     else{
		       	    	 //��λ����FE
		       	    	System.out.println("12");
		   	        	 System.out.print("���ݽ�����ĩλ����");
		   	        	 str="";
		       	     }
	       	     }
        	   
	           }else if(str.length() == 118){
	        	   
	        	   str="";
	        	   
	           }
            
		} catch (Exception e) {
			str="";
            System.out.println("S: Error 2");  
            e.printStackTrace();  
        }  
		
	}*/

}
