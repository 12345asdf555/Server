import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Android {

	private String str;
	private java.sql.Statement stmt;
	public DB_Connectionandroid db;
	public ArrayList<String> listarray2;
	
	public Android() {
		db = new DB_Connectionandroid();
	}
    
	public void Androidrun(String str) {
		// TODO Auto-generated constructor stub
		this.str = str;
		
		//记录开始处理Android数据时间
		db.DB_androidinit1(str);
		String a = str.substring(str.length()-26, str.length());
		if(!str.substring(str.length()-26, str.length()).equals("FE1555555555555555555512FD")){
			//解析数据插入数据库
			byte[] b = null;
			try {
				b = str.getBytes("ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         str="";
	         for(int i=0;i<b.length;i++){
	          	
	          	//判断为数字还是字母，若为字母+256取正数
	          	if(b[i]<0){
	          		String r = Integer.toHexString(b[i]+256);
	          		String rr=r.toUpperCase();
	              	//数字补为两位数
	              	if(rr.length()==1){
	          			rr='0'+rr;
	              	}
	              	//strdata为总接收数据
	          		str += rr;
	          	}
	          	else{
	          		String r = Integer.toHexString(b[i]);
	              	if(r.length()==1)
	          			r='0'+r;
	              	r=r.toUpperCase();
	          		str+=r;	
	          	}
	          }
			
		}
		
         	String [] stringArr = str.split("FD");
   	    	
            for(int i =0;i < stringArr.length;i++)
	        {
            	try{
            	
        	     //校验第一位是否为FE
	       	     String check1 =stringArr[i].substring(0,2);
	       	     if(check1.equals("FE")){
	       	    	 
	       	    	 //校验长度
	           	     int check2=stringArr[i].length();
	           	     if(check2==54){
	           	    	 
                    	 if(stringArr[i].length()>30){
                    		 
                    		BigDecimal electricity = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(4, 8).toString(),16));
                            BigDecimal voltage = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(8, 12).toString(),16));
                            BigDecimal sensor_Num1 = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(12, 16).toString(),16));
                            String sensor_Num = String.valueOf(sensor_Num1);
                            if(sensor_Num.length()<4){
                           	 int num=4-sensor_Num.length();
                           	 for(int i1=0;i1<num;i1++){
                           		 sensor_Num="0"+sensor_Num;
                           	 }
                            }
                            String gather_id1 = Integer.toString(Integer.valueOf(stringArr[i].subSequence(16, 20).toString(),16));
                            String gather_id = String.valueOf(gather_id1);
                            if(gather_id.length()<4){
                           	 int num=4-gather_id.length();
                           	 for(int i1=0;i1<num;i1++){
                           		gather_id="0"+gather_id;
                           	 }
                            }
                            String welder_id1 = Integer.toString(Integer.valueOf(stringArr[i].subSequence(20, 24).toString(),16));
                            String welder_id = String.valueOf(welder_id1);
                            if(welder_id.length()<4){
                           	 int num=4-welder_id.length();
                           	 for(int i1=0;i1<num;i1++){
                           		 welder_id="0"+welder_id;
                           	 }
                            }
                            String code1 = Integer.toString(Integer.valueOf(stringArr[i].subSequence(24, 32).toString(),16));
                            String code = String.valueOf(code1);
                            if(code.length()<8){
                           	 int num=8-code.length();
                           	 for(int i1=0;i1<num;i1++){
                           		 code="0"+code;
                           	 }
                            }
                            BigInteger year = new BigInteger(Integer.valueOf(stringArr[i].subSequence(32, 34).toString(),16).toString());
                            String yearstr = String.valueOf(year);
                            BigInteger month = new BigInteger(Integer.valueOf(stringArr[i].subSequence(34, 36).toString(),16).toString());
                            String monthstr = String.valueOf(month);
                            BigInteger day = new BigInteger(Integer.valueOf(stringArr[i].subSequence(36, 38).toString(),16).toString());
                            String daystr = String.valueOf(day);
                            BigInteger hour = new BigInteger(Integer.valueOf(stringArr[i].subSequence(38, 40).toString(),16).toString());
                            String hourstr = String.valueOf(hour);
                            BigInteger minute = new BigInteger(Integer.valueOf(stringArr[i].subSequence(40, 42).toString(),16).toString());
                            String minutestr = String.valueOf(minute);
                            BigInteger second = new BigInteger(Integer.valueOf(stringArr[i].subSequence(42, 44).toString(),16).toString());
                            String secondstr = String.valueOf(second);
                            Integer status = Integer.valueOf(stringArr[i].subSequence(44, 46).toString());
                    	
                            String timestr = yearstr+"-"+monthstr+"-"+daystr+" "+hourstr+":"+minutestr+":"+secondstr;
                            Date time = DateTools.parse("yy-MM-dd HH:mm:ss",timestr);
							Timestamp timesql = new Timestamp(time.getTime());
                            
                            db.DB_Connectionandroidrun(electricity,voltage,sensor_Num,gather_id,welder_id,code,timesql,status,listarray2); 
                            str="";
                            
                    	 } 
	           	     
	           	     }   
	           	     else{
	           	    	 System.out.print("数据接收长度错误");
	           	    	 str="";
	           	     }
       	         }
	       	     else{
	   	        	 System.out.print("数据接收首末位错误");
	   	        	 str="";
	       	     }
	       	     
            	} catch (Exception e) {
        			str="";
                    System.out.println("S: Error 2");  
                    e.printStackTrace();  
                }     
   	     	} 	
         
            //更新状态报表和实时表
            db.DB_androidupdate();
            
            //记录处理Android数据结束时间
            db.DB_androidinit2();
            
	}

}
