import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Mysql implements Callback{

	private String str;
    private String connet;
	private ArrayList<String> listarray1;
	
	
	@Override
	public void taskResult(String str,String connet,ArrayList<String> listarray1,ArrayList<String> listarray2,ArrayList<String> listarray3,HashMap<String, Socket> websocket,String ip1) {
		// TODO Auto-generated method stub
		
		this.str=str;
        this.connet=connet;
        this.listarray1=listarray1;
		
		try{
			
            if (str.length() == 108) {  

            //У���һλ�Ƿ�ΪFAĩλ�Ƿ�ΪF5
       	     String check1 =str.substring(0,2);
       	     String check11=str.substring(106,108);
       	     if(check1.equals("FA") && check11.equals("F5")){
	        		
           	     //У�鳤��
           	     int check2=str.length();
           	     if(check2==108){
           	        			
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
                                	
							 
							DB_Connectionmysql a = new DB_Connectionmysql(electricity,voltage,sensor_Num,machine_id,welder_id,code,status,timesql,connet,listarray1);
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
   	        		//��λ����FE
   	        		System.out.print("���ݽ�����ĩλ����");
   	        		str="";
   	        	}
       	     
           }else if(str.length()>=108 && str.length()!= 118){
        	
       	    	String [] stringArr = str.split("FD");
       	    	
                for(int i =0;i < stringArr.length;i++)
		        {
	        	     //У���һλ�Ƿ�ΪFAĩλ�Ƿ�ΪF5
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
		   	        	 System.out.print("���ݽ�����ĩλ����");
		   	        	 str="";
		       	     }
	       	     }
        	   
	           }else if(str.length() == 118){
	        	   
	        	   str="";
	        	   
	           }else {
	        	   
	        	   str="";
	               System.out.println("Not receiver anything from client!");  
	               
	           }
            
		} catch (Exception e) {
			str="";
            System.out.println("S: Error 2");  
            e.printStackTrace();  
        }  
		
	}

}
