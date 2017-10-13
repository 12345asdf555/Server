import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;  
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.ServerSocket;  
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.websocket.Session;

import javafx.scene.chart.PieChart.Data;


public class Server implements Runnable {  
	
 	private List<Handler> handlers = new ArrayList<Handler>();  
    public static final String SERVERIP = "121.196.222.216"; 
    public static final int SERVERPORT = 5555;
    public static final int SERVERPORTWEB = 5554;
    public String str = "";
    private Socket socket=null;
    public Socket websocketlink=null;
    public ServerSocket serverSocket = null;
    public boolean webtype = false;
    public int sqlwritetype=0;
    public int websendtype=0;
    
    public void run() {
    	
    	new Thread(reciver).start();
    	new Thread(mysql).start();
    	new Thread(websocketstart).start();
    	new Thread(websocketsend).start();
    	
    }  
      
    public Runnable reciver = new Runnable() {
		public void run() {
			
			System.out.println("S: Connecting...");  
			  

			
            try {
            	
				ServerSocket serverSocket = new ServerSocket(SERVERPORT);
				
				
				while (true) {  
					
					synchronized(this) {  
					
	                Socket client = serverSocket.accept();  
	  
	                System.out.println("S: Receiving...");  
	                  
	                try {  
	                    BufferedReader in = new BufferedReader(  
	                            new InputStreamReader(client.getInputStream()));  
	                      
	                    PrintWriter out = new PrintWriter(new BufferedWriter(  
	                            new OutputStreamWriter(client.getOutputStream())),true);  
	                      
	                    int zeroc=0;
	                    int i1=0;
	                    str = "";
	                    byte[] datas1 = new byte[54]; 
	                    client.getInputStream().read(datas1);
	                    for(i1=0;i1<datas1.length;i1++){
			                    	//判断为数字还是字母，若为字母+256取正数
			                    	if(datas1[i1]<0){
			                    		String r = Integer.toHexString(datas1[i1]+256);
			                    		String rr=r.toUpperCase();
			                        	//数字补为两位数
			                        	if(rr.length()==1){
			                    			rr='0'+rr;
			                        	}
			                        	//strdata为总接收数据
			                    		str += rr;
			                    	}
			                    	else{
			                    		String r = Integer.toHexString(datas1[i1]);

			                        	if(r.length()==1)
			                    			r='0'+r;
			                        	r=r.toUpperCase();
			                    		str+=r;	
			                    	}
	                    	}
	                    
	                    sqlwritetype=1;
	                    websendtype=1;
	                    
	                    //System.out.println(str);
	                	//new Thread(mysql).start();
	                    //new Thread(websocketstart).start();
	                    
	                } catch (Exception e) {  
	                    System.out.println("S: Error "+e.getMessage());  
	                    e.printStackTrace();  
	                } finally {  
	                    client.close();  
	                    System.out.println("S: Done.");  
	                } 
	                
				}
	                
	            }  
	        } catch (Exception e) {  
	            System.out.println("S: Error 2");  
	            e.printStackTrace();  
	        }  
            
            
		}
    };
    
    public Runnable mysql = new Runnable() {
		public void run() {
			while(true){
				
				 synchronized(this) {  
				
				while(sqlwritetype==1){
					
		            if (str.length() == 108) {  
		
		            //校验第一位是否为FA末位是否为F5
		       	     String check1 =str.substring(0,2);
		       	     String check11=str.substring(106,108);
		       	     if(check1.equals("FA") && check11.equals("F5")){
			        		
		           	     //校验长度
		           	     int check2=str.length();
		           	     if(check2==108){
		           	        			
		               	     //校验位校验
		               	     String check3=str.substring(2,104);
		               	     int check4=0;
		               	     for (int i11 = 0; i11 < check3.length()/2; i11++)
		               	     {
		               	    	String tstr1=check3.substring(i11*2, i11*2+2);
		               	    	check4+=Integer.valueOf(tstr1,16);
		               	     }
		               	     String check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1,3);
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
		                             SimpleDateFormat timeshow = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
		                             try {
		                            	java.util.Date time = timeshow.parse(timestr);
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
		                                	        	
		                             DB_Connectionmysql a =new DB_Connectionmysql(electricity,voltage,sensor_Num,machine_id,welder_id,code,status,timesql);
		                             System.out.println(str);
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
		
		               	    	 }
		                   	    sqlwritetype=0;
		               	     }
		               	        			
		               	     else{
		               	        //校验位错误
		               	    	 System.out.print("数据接收校验位错误");
		               	    	 sqlwritetype=0;
		               	     }
		                               
		           	     }
		           	        		
		           	     else{
		           	        //长度错误
		           	    	 System.out.print("数据接收长度错误");
		           	    	 sqlwritetype=0;
		           	     }
		       	        		
		   	        	}
		   	        	else{
		   	        		//首位不是FE
		   	        		System.out.print("数据接收首末位错误");
		   	        		sqlwritetype=0;
		   	        	}
		       	     
		           } else {
		        	   sqlwritetype=0;
		               System.out.println("Not receiver anything from client!");  
		           } 
			}
				 }
		}
		}
    };
    
    public Runnable websocketstart = new Runnable() {  
        private PrintWriter getWriter(Socket socket) throws IOException {  
            OutputStream socketOut = socket.getOutputStream();  
            return new PrintWriter(socketOut, true);  
        }  
        public Thread workThread;
        public Server server;
		public void run() {
		
			while(true){
				//建立websocket连接
				try {
				
				    boolean hasHandshake = false;
				
				    System.out.println("S: Connecting..."); 
					
					if(serverSocket==null){
						
						serverSocket = new ServerSocket(SERVERPORTWEB);
						
	                }  
					
					websocketlink = serverSocket.accept();

	                int i=0;
					//开启线程，接收不同的socket请求  
	                Handler handler = new Handler(websocketlink,str,handlers,i);  
	                handlers.add(handler);  
	                workThread = new Thread(handler);  
	                workThread.start();  
					  
					System.out.println("S: Receiving...");  
	
					
					//获取socket输入流信息  
	                InputStream in = websocketlink.getInputStream(); 
	                
	                PrintWriter pw = getWriter(websocketlink);
	                
	                //读入缓存(定义一个1M的缓存区)  
	                byte[] buf = new byte[1024]; 
	                
	                //读到字节（读取输入流数据到缓存）  
	                int len = in.read(buf, 0, 1024);
	                
	                //读到字节数组（定义一个容纳数据大小合适缓存区）  
	                byte[] res = new byte[len];  
	                
	                //将buf内中数据拷贝到res中  
	                System.arraycopy(buf, 0, res, 0, len); 
	                
	                //打印res缓存内容  
	                String key = new String(res);  
	                if(!hasHandshake && key.indexOf("Key") > 0){  
	                    //握手  
	                    //通过字符串截取获取key值  
	                    key = key.substring(0, key.indexOf("==") + 2);  
	                    key = key.substring(key.indexOf("Key") + 4, key.length()).trim();  
	                    //拼接WEBSOCKET传输协议的安全校验字符串  
	                    key+= "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";  
	                    //通过SHA-1算法进行更新  
	                    MessageDigest md = null;
						try {
							md = MessageDigest.getInstance("SHA-1");
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}    
	                    md.update(key.getBytes("utf-8"), 0, key.length());  
	                    byte[] sha1Hash = md.digest();    
	                    //进行Base64加密  
	                    sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();    
	                    key = encoder.encode(sha1Hash); 
	                    pw.println("HTTP/1.1 101 Switching Protocols");  
	                    pw.println("Upgrade: websocket");  
	                    pw.println("Connection: Upgrade");  
	                    pw.println("Sec-WebSocket-Accept: " + key);  
	                    pw.println();  
	                    pw.flush();  
	                    //将握手标志更新，只握一次  
	                    hasHandshake = true;  
	    				
	                }

	                
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
 };
 
 
 
 public Runnable websocketsend = new Runnable() {
        public Thread workThread;
		public void run() {

			while(true){
			
				synchronized(this) {  
				
				while(websendtype==1){
				
					for (int i=0;i<handlers.size();i++) {  
				    	
				    	Handler web = handlers.get(i);
				    	
				    	Handler handler = new Handler(web.websocketlink,str,handlers,i);
				    	workThread = new Thread(handler); 
				    	workThread.start();
				    	
	                }
					websendtype=0;
				}
				
				}
			}
		}
 	};
 
	 public static void main(String [] args ) { 
	     Thread desktopServerThread = new Thread(new Server());  
	     desktopServerThread.start();  
	
	 }  
 
}




	class Handler implements Runnable {
		
	    public Socket websocketlink;
		public String str;
	    Server server;
		private List<Handler> handlers;
		private int i;
	    
	    public Handler(Socket socket,String str,List<Handler> handlers,int i) {  
	        this.websocketlink = socket; 
	        this.str = str;
	        this.handlers = handlers;
	        this.i = i;
	    }  
	    
	    public void run() {
			String strdata = "";
			String strsend = "";
			try {
						
				if(str==null){
					
				}
				else
				{	
					
					byte[] str1=new byte[str.length()/2];
	
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
	                 }
	                     
					 String weldname = strdata.substring(10,14);
					 String welder=strdata.substring(14,18);
					 String electricity1=strdata.substring(26,30);
					 String voltage1=strdata.substring(30,34);
					 String status1=strdata.substring(38,40);
	                 
					 String electricity2=strdata.substring(52,56);
					 String voltage2=strdata.substring(56,60);
					 String status2=strdata.substring(64,66);
					 
					 String electricity3=strdata.substring(78,82);
					 String voltage3=strdata.substring(82,86);
					 String status3=strdata.substring(90,92);
					 
	                 DB_Connectionweb a =new DB_Connectionweb();
	                 
	                 String dbdata = a.getId();
	                 
	                 for(int i=0;i<dbdata.length();i+=12){
	                	 String status=dbdata.substring(0+i,2+i);
	                	 String framework=dbdata.substring(2+i,4+i);
	                     String weld=dbdata.substring(4+i,8+i); 
	                     String position=dbdata.substring(8+i,12+i);
	                     if(weldname.equals(weld)){
	                    	 strsend+=status1+framework+weld+position+welder+electricity1+voltage1
	                    			 +status2+framework+weld+position+welder+electricity2+voltage2
	                    			 +status3+framework+weld+position+welder+electricity3+voltage3;
	                     }
	                     else{
	                    	 strsend+=status+framework+weld+position+"0000"+"0000"+"0000"
	                    			 +status+framework+weld+position+"0000"+"0000"+"0000"
	                    			 +status+framework+weld+position+"0000"+"0000"+"0000";
	                     }
	                 }
	    
	                //数据发送
	                byte[] bb3=strsend.getBytes();
	                  
					ByteBuffer byteBuf = ByteBuffer.allocate(bb3.length);
					
					for(int j=0;j<bb3.length;j++){
						
						byteBuf.put(bb3[j]);
						
					}
					
					byteBuf.flip();
	                
	                //将内容返回给客户端  
	                responseClient(byteBuf, true, websocketlink); 
	                
	                System.out.println("实时数据已发送");
						
				}
					  
			   /*byte[] first = new byte[1];  
	           //这里会阻塞  
	           int read = in.read(first, 0, 1);  
	           //读取第一个字节是否有值,开始接收数据  
	           while(read > 0){  
	               //让byte和十六进制做与运算（二进制也就是11111111）  
	               //获取到第一个字节的数值  
	               int b = first[0] & 0xFF;  
	               //1为字符数据，8为关闭socket（只要低四位的值判断）  
	               byte opCode = (byte) (b & 0x0F);  
	               if(opCode == 8){  
	                   socket.getOutputStream().close();  
	                   break;  
	               }  
	               b = in.read();  
	               //只能描述127  
	               int payloadLength = b & 0x7F;  
	               if (payloadLength == 126) {  
	                   byte[] extended = new byte[2];  
	                   in.read(extended, 0, 2);  
	                   int shift = 0;  
	                   payloadLength = 0;  
	                   for (int i = extended.length - 1; i >= 0; i--) {  
	                       payloadLength = payloadLength + ((extended[i] & 0xFF) << shift);  
	                       shift += 2;  
	                   }  
	               } else if (payloadLength == 127) {  
	                   byte[] extended = new byte[8];  
	                   in.read(extended, 0, 8);  
	                   int shift = 0;  
	                   payloadLength = 0;  
	                   for (int i = extended.length - 1; i >= 0; i--) {  
	                       payloadLength = payloadLength + ((extended[i] & 0xFF) << shift);  
	                       shift += 8;  
	                   }  
	               }  
	               //掩码  
	               byte[] mask = new byte[4];  
	               in.read(mask, 0, 4);  
	               int readThisFragment = 1;  
	               ByteBuffer byteBuf = ByteBuffer.allocate(payloadLength + 30);  
	               byteBuf.put("浏览器: ".getBytes("UTF-8"));  
	               while(payloadLength > 0){  
	                    int masked = in.read();  
	                    masked = masked ^ (mask[(int) ((readThisFragment - 1) % 4)] & 0xFF);  
	                    byteBuf.put((byte) masked);  
	                    payloadLength--;  
	                    readThisFragment++;  
	               }  
	               byteBuf.flip();  
	               //将内容返回给客户端  
	               responseClient(byteBuf, true, socket);  
	               //打印内容    
	               in.read(first, 0, 1);  
	           }  
	           
	           
	       }*/
				
				/*String b="FE115555555555555555550EFD";
			    byte[] bb=new byte[b.length()/2];
	
				for (int i = 0; i < bb.length; i++)
				{
					String tstr1=b.substring(i*2, i*2+2);
					Integer k=Integer.valueOf(tstr1, 16);
					bb[i]=(byte)k.byteValue();
				}
				responseClient(bb,true,socket);*/
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				try {
					websocketlink.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				handlers.remove(i);
				System.out.println("实时数据发送失败");
				e.printStackTrace();
			}
		}  
	    
	    public void responseClient(ByteBuffer byteBuf, boolean finalFragment,Socket socket) throws IOException {  
	        
	    	OutputStream out = websocketlink.getOutputStream();  
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
