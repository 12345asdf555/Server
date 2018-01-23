import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

public class Websocket implements Callback {

	Timestamp timesql1;
	Timestamp timesql2;
	Timestamp timesql3;
	private String limit;
	private String connet;
	private String strsend;
	private String strdata;
	private String websocketfail;
	private ArrayList<String> listarray2;
	private ArrayList<String> listarray3;
	private boolean datawritetype = false;
	private HashMap<String, Socket> websocket;

	@Override
	public void taskResult(String str,String connet,ArrayList<String> listarray1,ArrayList<String> listarray2,ArrayList<String> listarray3,HashMap<String, Socket> websocket,String ip1) {
		// TODO Auto-generated method stub
		this.websocket = websocket; 
        this.strdata = str;
        this.connet = connet;
        this.listarray2 = listarray2;
        this.listarray3 = listarray3;
        
        
        try {
			
			if(str==""){
				
			}
			else
			{	
				
				/*byte[] str1=new byte[str.length()/2];

				for (int i = 0; i < str1.length; i++)
				{
					String tstr1=str.substring(i*2, i*2+2);
					Integer k=Integer.valueOf(tstr1, 16);
					str1[i]=(byte)k.byteValue();
				}
            	
				//串口数据处理
				for(int i=0;i<str1.length;i++){
                 	
                 	//判断为数字还是字母，若为字母+256取正数
                 	if(str1[i]<0){
                 		String r = Integer.toHexString(str1[i]+256);
                 		String rr=r.toUpperCase();
                     	//数字补为两位数
                     	if(rr.length()==1){
                 			rr='0'+rr;
                     	}
                     	//strdata为总接收数据
                 		strdata += rr;
                 	}
                 	else{
                 		String r = Integer.toHexString(str1[i]);
                     	if(r.length()==1)
                 			r='0'+r;
                     	r=r.toUpperCase();
                 		strdata+=r;	
                 	}
                 }*/
                     
				 strdata=str;
				 int weldname1 = Integer.valueOf(strdata.subSequence(10, 14).toString(),16);
				 String weldname = String.valueOf(weldname1);
				 if(weldname.length()!=4){
                	 int lenth=4-weldname.length();
                	 for(int i=0;i<lenth;i++){
                		 weldname="0"+weldname;
                	 }
                 }
				 String welder=strdata.substring(14,18);
				 long code1 = Integer.valueOf(strdata.subSequence(18, 26).toString(),16);
                 String code = String.valueOf(code1);
                 if(code.length()!=8){
                	 int lenth=8-code.length();
                	 for(int i=0;i<lenth;i++){
                		 code="0"+code;
                	 }
                 }
				 String electricity1=strdata.substring(26,30);
				 String voltage1=strdata.substring(30,34);
				 String status1=strdata.substring(38,40);
				 
				 long year1 = Integer.valueOf(str.subSequence(40, 42).toString(),16);
                 String yearstr1 = String.valueOf(year1);
                 long month1 = Integer.valueOf(str.subSequence(42, 44).toString(),16);
                 String monthstr1 = String.valueOf(month1);
                 if(monthstr1.length()!=2){
                	 int lenth=2-monthstr1.length();
                	 for(int i=0;i<lenth;i++){
                		 monthstr1="0"+monthstr1;
                	 }
                 }
                 long day1 = Integer.valueOf(str.subSequence(44, 46).toString(),16);
                 String daystr1 = String.valueOf(day1);
                 if(daystr1.length()!=2){
                	 int lenth=2-daystr1.length();
                	 for(int i=0;i<lenth;i++){
                		 daystr1="0"+daystr1;
                	 }
                 }
                 long hour1 = Integer.valueOf(str.subSequence(46, 48).toString(),16);
                 String hourstr1 = String.valueOf(hour1);
                 if(hourstr1.length()!=2){
                	 int lenth=2-hourstr1.length();
                	 for(int i=0;i<lenth;i++){
                		 hourstr1="0"+hourstr1;
                	 }
                 }
                 long minute1 = Integer.valueOf(str.subSequence(48, 50).toString(),16);
                 String minutestr1 = String.valueOf(minute1);
                 if(minutestr1.length()!=2){
                	 int lenth=2-minutestr1.length();
                	 for(int i=0;i<lenth;i++){
                		 minutestr1="0"+minutestr1;
                	 }
                 }
                 long second1 = Integer.valueOf(str.subSequence(50, 52).toString(),16);
                 String secondstr1 = String.valueOf(second1);
                 if(secondstr1.length()!=2){
                	 int lenth=2-secondstr1.length();
                	 for(int i=0;i<lenth;i++){
                		 secondstr1="0"+secondstr1;
                	 }
                 }
   	    		 
                 String timestr1 = yearstr1+"-"+monthstr1+"-"+daystr1+" "+hourstr1+":"+minutestr1+":"+secondstr1;
                 SimpleDateFormat timeshow1 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                 try {
					
					Date time1 = DateTools.parse("yy-MM-dd HH:mm:ss",timestr1);
                	//java.util.Date time4 = timeshow3.parse(timestr3);
					timesql1 = new Timestamp(time1.getTime());
					
				 } catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				 }
                 
				 
				 String electricity2=strdata.substring(52,56);
				 String voltage2=strdata.substring(56,60);
				 String status2=strdata.substring(64,66);
				 
				 long year2 = Integer.valueOf(str.subSequence(66, 68).toString(),16);
                 String yearstr2 = String.valueOf(year2);
                 long month2 = Integer.valueOf(str.subSequence(68, 70).toString(),16);
                 String monthstr2 = String.valueOf(month2);
                 if(monthstr2.length()!=2){
                	 int lenth=2-monthstr2.length();
                	 for(int i=0;i<lenth;i++){
                		 monthstr2="0"+monthstr2;
                	 }
                 }
                 long day2 = Integer.valueOf(str.subSequence(70, 72).toString(),16);
                 String daystr2 = String.valueOf(day2);
                 if(daystr2.length()!=2){
                	 int lenth=2-daystr2.length();
                	 for(int i=0;i<lenth;i++){
                		 daystr2="0"+daystr2;
                	 }
                 }
                 long hour2 = Integer.valueOf(str.subSequence(72, 74).toString(),16);
                 String hourstr2 = String.valueOf(hour2);
                 if(hourstr2.length()!=2){
                	 int lenth=2-hourstr2.length();
                	 for(int i=0;i<lenth;i++){
                		 hourstr2="0"+hourstr2;
                	 }
                 }
                 long minute2 = Integer.valueOf(str.subSequence(74, 76).toString(),16);
                 String minutestr2 = String.valueOf(minute2);
                 if(minutestr2.length()!=2){
                	 int lenth=2-minutestr2.length();
                	 for(int i=0;i<lenth;i++){
                		 minutestr2="0"+minutestr2;
                	 }
                 }
                 long second2 = Integer.valueOf(str.subSequence(76, 78).toString(),16);
                 String secondstr2 = String.valueOf(second2);
                 if(secondstr2.length()!=2){
                	 int lenth=2-secondstr2.length();
                	 for(int i=0;i<lenth;i++){
                		 secondstr2="0"+secondstr2;
                	 }
                 }
   	    		 
                 String timestr2 = yearstr2+"-"+monthstr2+"-"+daystr2+" "+hourstr2+":"+minutestr2+":"+secondstr2;
                 SimpleDateFormat timeshow2 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                 try {

					Date time2 = DateTools.parse("yy-MM-dd HH:mm:ss",timestr2);
                	//java.util.Date time4 = timeshow3.parse(timestr3);
					timesql2 = new Timestamp(time2.getTime());
					
				 } catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				 }
				 
				 
				 String electricity3=strdata.substring(78,82);
				 String voltage3=strdata.substring(82,86);
				 String status3=strdata.substring(90,92);
				 
				 long year3 = Integer.valueOf(str.subSequence(92, 94).toString(),16);
                 String yearstr3 = String.valueOf(year3);
                 long month3 = Integer.valueOf(str.subSequence(94, 96).toString(),16);
                 String monthstr3 = String.valueOf(month3);
                 if(monthstr3.length()!=2){
                	 int lenth=2-monthstr3.length();
                	 for(int i=0;i<lenth;i++){
                		 monthstr3="0"+monthstr3;
                	 }
                 }
                 long day3 = Integer.valueOf(str.subSequence(96, 98).toString(),16);
                 String daystr3 = String.valueOf(day3);
                 if(daystr3.length()!=2){
                	 int lenth=2-daystr3.length();
                	 for(int i=0;i<lenth;i++){
                		 daystr3="0"+daystr3;
                	 }
                 }
                 long hour3 = Integer.valueOf(str.subSequence(98, 100).toString(),16);
                 String hourstr3 = String.valueOf(hour3);
                 if(hourstr3.length()!=2){
                	 int lenth=2-hourstr3.length();
                	 for(int i=0;i<lenth;i++){
                		 hourstr3="0"+hourstr3;
                	 }
                 }
                 long minute3 = Integer.valueOf(str.subSequence(100, 102).toString(),16);
                 String minutestr3 = String.valueOf(minute3);
                 if(minutestr3.length()!=2){
                	 int lenth=2-minutestr3.length();
                	 for(int i=0;i<lenth;i++){
                		 minutestr3="0"+minutestr3;
                	 }
                 }
                 long second3 = Integer.valueOf(str.subSequence(102, 104).toString(),16);
                 String secondstr3 = String.valueOf(second3);
                 if(secondstr3.length()!=2){
                	 int lenth=2-secondstr3.length();
                	 for(int i=0;i<lenth;i++){
                		 secondstr3="0"+secondstr3;
                	 }
                 }
   	    		 
                 String timestr3 = yearstr3+"-"+monthstr3+"-"+daystr3+" "+hourstr3+":"+minutestr3+":"+secondstr3;
                 SimpleDateFormat timeshow3 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                 try {
                	Date time3 = DateTools.parse("yy-MM-dd HH:mm:ss",timestr3);
                	//java.util.Date time4 = timeshow3.parse(timestr3);
					timesql3 = new Timestamp(time3.getTime());
				 } catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				 }
				 
                 try{
                	 
                     /*DB_Connectionweb b =new DB_Connectionweb(connet);
                     DB_Connectioncode c =new DB_Connectioncode(code,connet);
                     DB_Connectioncode c =new DB_Connectioncode();
	                 String dbdata = b.getId();
	                 String limit = c.getId();*/
                	 
                	 for(int i=0;i<listarray3.size();i+=5){
                		 String weldjunction = listarray3.get(i);
                		 if(weldjunction.equals(code)){
                			 String maxe = listarray3.get(i+1);
                			 String mixe = listarray3.get(i+2);
                			 String maxv = listarray3.get(i+3);
                			 String mixv = listarray3.get(i+4);
                			 limit = maxe + mixe + maxv + mixv;
                			 
                		 }
                	 }
                	 
                	 for(int i=0;i<listarray2.size();i+=3){
                		 String fequipment_no = listarray2.get(i);
                		 String fgather_no = listarray2.get(i+1);
                		 String finsframework_id = listarray2.get(i+2);
                		 if(weldname.equals(fgather_no)){
	                    	 strsend+=status1+finsframework_id+fequipment_no+welder+electricity1+voltage1+timesql1+limit
	                    			 +status2+finsframework_id+fequipment_no+welder+electricity2+voltage2+timesql2+limit
	                    			 +status3+finsframework_id+fequipment_no+welder+electricity3+voltage3+timesql3+limit;
	                     }
	                     else{
	                    	 strsend+="09"+finsframework_id+fequipment_no+"0000"+"0000"+"0000"+"000000000000000000000"+"000000000000"
	                    			 +"09"+finsframework_id+fequipment_no+"0000"+"0000"+"0000"+"000000000000000000000"+"000000000000"
	                    			 +"09"+finsframework_id+fequipment_no+"0000"+"0000"+"0000"+"000000000000000000000"+"000000000000";
	                     }
                	 }

                	 
	                 /*for(int i=0;i<dbdata.length();i+=13){
	                	 String status=dbdata.substring(0+i,2+i);
	                	 String framework=dbdata.substring(2+i,4+i);
	                     String weld=dbdata.substring(4+i,8+i); 
	                     if(weldname.equals(weld)){
	                    	 strsend+=status1+framework+weld+welder+electricity1+voltage1+timesql1
	                    			 +status2+framework+weld+welder+electricity2+voltage2+timesql2
	                    			 +status3+framework+weld+welder+electricity3+voltage3+timesql3;
	                     }
	                     else{
	                    	 strsend+="09"+framework+weld+"0000"+"0000"+"0000"+"000000000000000000000"+"000000000000"
	                    			 +"09"+framework+weld+"0000"+"0000"+"0000"+"000000000000000000000"+"000000000000"
	                    			 +"09"+framework+weld+"0000"+"0000"+"0000"+"000000000000000000000"+"000000000000";
	                     }
	                 }*/
                 }catch (Exception e) {
						// TODO Auto-generated catch block
                	 System.out.println("数据库读取数据错误");
                	 e.printStackTrace();
					 }
    
                 datawritetype = true;
                 
                //数据发送
                byte[] bb3=strsend.getBytes();
                  
				ByteBuffer byteBuf = ByteBuffer.allocate(bb3.length);
				
				for(int j=0;j<bb3.length;j++){
					
					byteBuf.put(bb3[j]);
					
				}
				
				byteBuf.flip();
				
				strsend="";
				
				for (Entry<String, Socket> entry : websocket.entrySet()) {
					//将内容返回给客户端  
					websocketfail=entry.getKey();
	                responseClient(byteBuf, true, entry.getValue());
				} 
                
			}
			
		} catch (IOException e) {
			
			if(datawritetype = true){
				
				websocket.remove(websocketfail); 
				
			}
		}

	}
        
        public void responseClient(ByteBuffer byteBuf, boolean finalFragment,Socket socket) throws IOException {  
	        
	    	OutputStream out = socket.getOutputStream();  
	        int first = 0x00;  
	        
	        //是否是输出最后的WebSocket响应片段  
	            if (finalFragment) {  
	                first = first + 0x80;  
	                first = first + 0x1;  
	            }  
	            
	            out.write(first); 
	            
	            if (byteBuf.limit() < 126) {  
	                out.write(byteBuf.limit());  
	            } else if (byteBuf.limit() < 65536) {  
		            out.write(126);  
		            out.write(byteBuf.limit() >>> 8);  
		            out.write(byteBuf.limit() & 0xFF);  
	            } else {  
	            // Will never be more than 2^31-1  
		            out.write(127);  
		            out.write(0);  
		            out.write(0);  
		            out.write(0);  
		            out.write(0);  
		            out.write(byteBuf.limit() >>> 24);  
		            out.write(byteBuf.limit() >>> 16);  
		            out.write(byteBuf.limit() >>> 8);  
		            out.write(byteBuf.limit() & 0xFF);  
	            }  
	            // Write the content  
	            out.write(byteBuf.array(), 0, byteBuf.limit());  
	            out.flush();  
	    }  

}
