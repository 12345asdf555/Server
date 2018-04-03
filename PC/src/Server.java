import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;  
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;  
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;



public class Server implements Runnable {  
	
 	private List<Handler> handlers = new ArrayList<Handler>();  
    public static final String SERVERIP = "121.196.222.216"; 
    public static final int SERVERPORT = 5555;
    public static final int SERVERPORTWEB = 5554;
    public String str = "";
    public Socket socket=null;
    public Socket websocketlink=null;
    public ServerSocket serverSocket = null;
    public boolean webtype = false;
    public int sqlwritetype=0;
    public int websendtype=0;
    public int sockettype=0;
    public String ip;
    public String ip1;
    public String connet1 = "jdbc:mysql://";
    public String connet2 = ":3306/AL_DB?" + "user=root&password=123456&useUnicode=true&characterEncoding=UTF8"; 
    public String connet;
    public byte b[];
    public DB_Connectioncode check;
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();
    public HashMap<String, Socket> websocket = new HashMap<>();
    public HashMap<String, SocketChannel> clientList = new HashMap<>();
    public int websocketcount=0;
    public int clientcount=0;
    public Selector selector = null;
    public ServerSocketChannel ssc = null;
    private NettyServerHandler NS = new NettyServerHandler();
    
    public String getconnet(){
    	return connet;
    }
    
    public ArrayList<String> getlistarray1(){
    	return listarray1;
    }
    
    public void run() {
    	
		try {
			FileInputStream in = new FileInputStream("IPconfig.txt");  
            InputStreamReader inReader = new InputStreamReader(in, "UTF-8");  
            BufferedReader bufReader = new BufferedReader(inReader);  
            String line = null; 
            int writetime=0;
			
		    while((line = bufReader.readLine()) != null){ 
		    	if(writetime==0){
	                ip=line;
	                writetime++;
		    	}
		    	else{
		    		ip1=line;
		    		writetime=0;
		    	}
            }  

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		connet=connet1+ip+connet2;
		

	    NS.ip = this.ip;
	    NS.ip1 = this.ip1;
	    NS.connet = this.connet;
		
		/*Timer tExit = null; 
		tExit = new Timer();  
        tExit.schedule(new TimerTask() {  
            @Override  
            public void run() {*/
  		
        		DB_Connectioncode check = new DB_Connectioncode(connet);
        		
        		listarray1 = check.getId1();
        		System.out.println(listarray1);
        		listarray2 = check.getId2();
        		System.out.println(listarray2);
        		listarray3 = check.getId3();
        		System.out.println(listarray3);
	
            /*}  
        }, 0,600000);*/

		NS.listarray1 = this.listarray1;
		NS.listarray2 = this.listarray2;
		NS.listarray3 = this.listarray3;
		
		new Thread(websocketstart).start();
		
		EventLoopGroup bossGroup = new NioEventLoopGroup(); 
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{  
            ServerBootstrap b=new ServerBootstrap();  
            b.group(bossGroup,workerGroup)
            	.channel(NioServerSocketChannel.class)
            	.option(ChannelOption.SO_BACKLOG,1024)
            	.childHandler(NS);  
            
          
            b = b.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                   ch.pipeline().addLast(NS);
                }
            });
            
            //绑定端口，等待同步成功  
            ChannelFuture f;
			f = b.bind(5555).sync();
            //等待服务端关闭监听端口  
            f.channel().closeFuture().sync(); 
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {  
            //释放线程池资源  
            bossGroup.shutdownGracefully();  
            workerGroup.shutdownGracefully();  
        }  
			 
        /*new Thread(websocketstart).start();
        new Thread(websocketsend).start();
   
        try {
		selector = Selector.open();
		ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress("172.16.188.99", 5555));
	 } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	 }*/
        
        /*Reciver reciver=new Reciver();
        reciver.setCallback(new Mysql(),new Websocket(),new Socketsend(),connet,listarray1,listarray2,listarray3,websocket,ip1,ssc,selector,true);
        reciver.reciver();*/

    }  
    

    
    public Runnable websocketstart = new Runnable() {  
        private PrintWriter getWriter(Socket socket) throws IOException {  
            OutputStream socketOut = socket.getOutputStream();  
            return new PrintWriter(socketOut, true);  
        }  
        
        public Server server;
        public Thread workThread;
		private HashMap<String, Socket> websocket = new HashMap<>();;
		public void run() {
		
			while(true){
				//建立websocket连接
				try {
				
				    boolean hasHandshake = false;
					
					if(serverSocket==null){
						
						serverSocket = new ServerSocket(SERVERPORTWEB);
						
	                }  
					
					websocketlink = serverSocket.accept();

					websocketcount++;
		
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

	                websocket.put(Integer.toString(websocketcount),websocketlink);
	                
	                NS.websocket = this.websocket;
	                
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
 };
 
 
 
 /*public Runnable websocketsend = new Runnable() {
        public Thread workThread;
		public void run() {

			while(true){
			
				synchronized(this) {  
				
				while(websendtype==1){
				
					Reciver reciver=new Reciver();
	                reciver.setCallback(new Mysql(),new Websocket(),new Socketsend(),connet,listarray1,listarray2,listarray3,websocket,ip1,ssc,selector,false);
	                reciver.reciver();
				}
				
				}
			}
		}
 	};*/
 
 	
	 public static void main(String [] args) throws IOException 
	 {  
	     Thread desktopServerThread = new Thread(new Server());  
	     desktopServerThread.start();  
	 }

	 
	 
	class Handler implements Runnable {
		
		public DB_Connectionmysql db_connection;
	    public Socket websocketlink;
		public String str;
	    Server server;
		private List<Handler> handlers;
		private int i;
		private int websendtype;
		private boolean datawritetype = false;
		private String connet;
		public ArrayList<String> listarray2 = new ArrayList<String>();
		public ArrayList<String> listarray3 = new ArrayList<String>();
		public String limit;
	    
	    public Handler(Socket socket,String str,List<Handler> handlers,int i,int websendtype,String connet,ArrayList<String> listarray2,ArrayList<String> listarray3) {  
	        this.websocketlink = socket; 
	        this.str = str;
	        this.handlers = handlers;
	        this.i = i;
	        this.websendtype = websendtype;
	        this.connet = connet;
	        this.listarray2 = listarray2;
	        this.listarray3 = listarray3;
	        
	    }  
	    
	    public void run() {
			String strdata = "";
			String strsend = "";
			Timestamp timesql1 = null;
			Timestamp timesql2 = null;
			Timestamp timesql3 = null;
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
                    	 websendtype=0;
                    	 str="";
 					 }
	    
	                 datawritetype = true;
	                 
	                //数据发送
	                byte[] bb3=strsend.getBytes();
	                  
					ByteBuffer byteBuf = ByteBuffer.allocate(bb3.length);
					
					for(int j=0;j<bb3.length;j++){
						
						byteBuf.put(bb3[j]);
						
					}
					
					byteBuf.flip();
					
	                //将内容返回给客户端  
	                responseClient(byteBuf, true, websocketlink); 
	                
				}
				
			} catch (IOException e) {
				
				websendtype=0;
				
				if(datawritetype = true){
					
					try {
						websendtype=0; 
						websocketlink.close();
						for(int j=0;j<handlers.size();j++){
							if(handlers.get(j).websocketlink == websocketlink){
								handlers.remove(j);
							}
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					
				}
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
}
