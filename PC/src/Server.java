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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Map.Entry;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;



public class Server implements Runnable {  
	
 	//private List<Handler> handlers = new ArrayList<Handler>();  
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
    public String ip=null;
    public String ip1=null;
    public String connet1 = "jdbc:mysql://";
    public String connet2 = ":3306/"; 
    public String connet3 = "?user="; 
    public String connet4 = "&password=";
    public String connet5 = "&useUnicode=true&autoReconnect=true&characterEncoding=UTF8";
    public String connet;
    public byte b[];
    public DB_Connectioncode check;
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();
    public HashMap<String, SocketChannel> socketlist = new HashMap<>();
    public HashMap<String, SocketChannel> websocketlist = new HashMap<>();
    public HashMap<String, SocketChannel> clientList = new HashMap<>();
    public int socketcount=0;
    public int websocketcount=0;
    public int clientcount=0;
    public Selector selector = null;
    public ServerSocketChannel ssc = null;
    public Client client = new Client(this);
    public NettyServerHandler NS = new NettyServerHandler();
    private NettyWebSocketHandler NWS = new NettyWebSocketHandler();
	private Connection c;
	public java.sql.Connection conn = null;
    public java.sql.Statement stmt =null;
	private Date time;
	private ArrayList<String> dbdata;
    
    public String getconnet(){
    	return connet;
    }
    
    public ArrayList<String> getlistarray1(){
    	return listarray1;
    }
    
    public void run() {
    	
    	//读取IPconfig配置文件获取ip地址和数据库配置
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
		
		String[] values = ip.split(",");
		
		connet=connet1+values[0]+connet2+values[1]+connet3+values[2]+connet4+values[3]+connet5;
		
	    NS.ip = this.ip;
	    NS.ip1 = this.ip1;
	    NS.connet = this.connet;
		
	    //连接数据库
	    try {  

            Class.forName("com.mysql.jdbc.Driver");  
            conn = DriverManager.getConnection(connet);
            stmt= conn.createStatement();
            NS.mysql.db.stmt = stmt;
            NS.android.db.stmt = stmt;
            NS.mysql.db.connet = connet;
            NS.android.db.connet = connet;

        } catch (ClassNotFoundException e) {  
            System.out.println("Broken driver");
            e.printStackTrace();  
        } catch (SQLException e) {
            System.out.println("Broken conn");
            e.printStackTrace();
        }  
        
	    //开启线程每小时更新三张状态表
	    Date date = new Date();
        String nowtime = DateTools.format("HH:mm:ss",date);
        String[] timesplit = nowtime.split(":");
        String hour = timesplit[0];
       
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour)+1); // 控制时
        calendar.set(Calendar.MINUTE, 00);    // 控制分
        calendar.set(Calendar.SECOND, 00);    // 控制秒
        time = calendar.getTime(); 
        
	    Timer tExit1 = null; 
		tExit1 = new Timer();  
        tExit1.schedule(new TimerTask() {  

			@Override  
            public void run() {
  		
            	try {  

                    Class.forName("com.mysql.jdbc.Driver");  
                    conn = DriverManager.getConnection(connet);
                    stmt= conn.createStatement();
                    NS.stmt = stmt;
                    
                    Class.forName("com.mysql.jdbc.Driver");  
                    conn = DriverManager.getConnection(connet);
                    stmt = conn.createStatement();
                    	
                	Date date = new Date();
                    String nowtimefor = DateTools.format("yyyy-MM-dd",date);
                    String nowtime = DateTools.format("HH:mm:ss",date);
                    String[] timesplit = nowtime.split(":");
                    String hour = timesplit[0];
                    String time2 = nowtimefor+" "+hour+":00:00";
                    Date d1 = new Date((DateTools.parse("yyyy-MM-dd HH:mm:ss",time2).getTime())-3600000);
                    String time3 = DateTools.format("yyyy-MM-dd HH:mm:ss",d1);
                    
                	String timework = null;
                	String timestandby = null;
                	String timealarm = null;
                	String sqlfirstwork = "SELECT tb_work.fUploadDataTime FROM tb_work ORDER BY tb_work.fUploadDataTime DESC LIMIT 0,1";
                	String sqlfirststandby = "SELECT tb_standby.fUploadDataTime FROM tb_standby ORDER BY tb_standby.fUploadDataTime DESC LIMIT 0,1";
                	String sqlfirstalarm = "SELECT tb_alarm.fUploadDataTime FROM tb_alarm ORDER BY tb_alarm.fUploadDataTime DESC LIMIT 0,1";
                	ResultSet rs1 =stmt.executeQuery(sqlfirstwork);
                	while (rs1.next()) {
                		timework = rs1.getString("fUploadDataTime");
                	}
                	ResultSet rs2 =stmt.executeQuery(sqlfirststandby);
                	while (rs2.next()) {
                		timestandby = rs2.getString("fUploadDataTime");
                	}
                	ResultSet rs3 =stmt.executeQuery(sqlfirstalarm);
                	while (rs3.next()) {
                		timealarm = rs3.getString("fUploadDataTime");
                	}
                	
                    String sqlstandby = "INSERT INTO tb_standby(tb_standby.fwelder_id,tb_standby.fgather_no,tb_standby.fmachine_id,tb_standby.fjunction_id,"
                    		+ "tb_standby.fitemid,tb_standby.felectricity,tb_standby.fvoltage,tb_standby.frateofflow,tb_standby.fstandbytime,tb_standby.fstarttime,tb_standby.fendtime) SELECT "
                    		+ "tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,"
                    		+ "AVG(tb_live_data.felectricity),AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + time3 + "','" + time2 + "' FROM tb_live_data "
                    		+ "WHERE tb_live_data.fstatus = '0' AND tb_live_data.FWeldTime BETWEEN '" + timestandby + "' AND '" + time2 + "' "
                    		+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id";
                    
                    String sqlwork = "INSERT INTO tb_work(tb_work.fwelder_id,tb_work.fgather_no,tb_work.fmachine_id,tb_work.fjunction_id,tb_work.fitemid,"
                    		+ "tb_work.felectricity,tb_work.fvoltage,tb_work.frateofflow,tb_work.fworktime,tb_work.fstarttime,tb_work.fendtime) SELECT tb_live_data.fwelder_id,"
                    		+ "tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,AVG(tb_live_data.felectricity),"
                    		+ "AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + time3 + "','" + time2 + "' FROM tb_live_data "
                    		+ "WHERE tb_live_data.fstatus = '3' AND tb_live_data.FWeldTime BETWEEN '" + timework + "' AND '" + time2 + "' "
                    		+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id";
                    
                    String sqlalarm = "INSERT INTO tb_alarm(tb_alarm.fwelder_id,tb_alarm.fgather_no,tb_alarm.fmachine_id,tb_alarm.fjunction_id,tb_alarm.fitemid,"
                    		+ "tb_alarm.felectricity,tb_alarm.fvoltage,tb_alarm.frateofflow,tb_alarm.falarmtime,tb_alarm.fstarttime,tb_alarm.fendtime) SELECT tb_live_data.fwelder_id,"
                    		+ "tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,AVG(tb_live_data.felectricity),"
                    		+ "AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + time3 + "','" + time2 + "' FROM tb_live_data "
                    		+ "INNER JOIN tb_welded_junction ON tb_live_data.fjunction_id = tb_welded_junction.fwelded_junction_no "
                    		+ "WHERE fstatus= '3' and tb_welded_junction.fitemid = tb_live_data.fitemid and (tb_live_data.fvoltage > tb_welded_junction.fmax_valtage OR tb_live_data.felectricity > tb_welded_junction.fmax_electricity "
                    		+ "OR tb_live_data.fvoltage < tb_welded_junction.fmin_valtage OR tb_live_data.felectricity < tb_welded_junction.fmin_electricity)"
                    		+ " AND tb_live_data.FWeldTime BETWEEN '" + timealarm + "' AND '" + time2 + "' "
                    		+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id";
                    
                	stmt.executeUpdate(sqlstandby);
                	stmt.executeUpdate(sqlwork);
                	stmt.executeUpdate(sqlalarm);
                        
                } catch (ClassNotFoundException e) {  
                    System.out.println("Broken driver");
                    e.printStackTrace();  
                } catch (SQLException e) {
                    System.out.println("Broken conn");
                    e.printStackTrace();
                } catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            }  
        }, time , 1000*60*60);
	    
	    //获取最新焊口和焊机统计时间
	    DB_Connectioncode check = new DB_Connectioncode(stmt);
	    //DB_Connectionweb b =new DB_Connectionweb(connet);
  		//dbdata = b.getId();
		NS.websocket.dbdata = this.dbdata;
  		
		listarray1 = check.getId1();
		listarray2 = check.getId2();
		listarray3 = check.getId3();
		
		System.out.println(listarray1);
		System.out.println(listarray2);
		System.out.println(listarray3);
		
		NS.mysql.listarray1 = this.listarray1;
		NS.mysql.listarray2 = this.listarray2;
		NS.android.listarray1 = this.listarray1;
		NS.android.listarray2 = this.listarray2;
		NS.listarray1 = this.listarray1;
		NS.listarray2 = this.listarray2;
		NS.listarray3 = this.listarray3;
	    
		//开启线程每分钟更新焊口数据
		Timer tExit2 = null; 
		tExit2 = new Timer();  
        tExit2.schedule(new TimerTask() {  
            @Override  
            public void run() {
  		
	            try{
	            	if(stmt==null || stmt.isClosed()==true)
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
	        	}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
            	
        		DB_Connectioncode check = new DB_Connectioncode(stmt);
        		
        		listarray1 = check.getId1();
        		listarray2 = check.getId2();
        		listarray3 = check.getId3();
        		
        		NS.mysql.listarray1 = listarray1;
        		NS.mysql.listarray2 = listarray2;
        		NS.mysql.listarray3 = listarray3;
        		NS.android.listarray1 = listarray1;
        		NS.android.listarray2 = listarray2;
        		NS.listarray1 = listarray1;
        		NS.listarray2 = listarray2;
        		NS.listarray3 = listarray3;
            }  
        }, 0,60000);
        
        //工作线程
        new Thread(socketstart).start();
		new Thread(websocketstart).start();
		new Thread(sockettran).start();
		
		/*//开启线程每天查询邮件
        Calendar calendarmail = Calendar.getInstance();
        
        calendarmail.set(Calendar.HOUR_OF_DAY, 17); // 控制时
        calendarmail.set(Calendar.MINUTE, 50);    // 控制分
        calendarmail.set(Calendar.SECOND, 00);    // 控制秒
        time = calendarmail.getTime(); 
        
	    Timer tExit3 = null; 
		tExit3 = new Timer();  
        tExit3.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//获取焊工以及管理员信息
				ArrayList<String> listarraymail = new ArrayList<String>();
				ArrayList<String> listarraymailer = new ArrayList<String>();
				String sqlmail = "SELECT tb_catweldinf.fweldername,tb_catweldinf.fcheckintime,tb_catweldinf.ficworkime FROM tb_catweldinf";
				String sqlmailer = "SELECT femailname,femailaddress,femailtype FROM tb_catemailinf";
				ResultSet rs;
				try {
					rs = stmt.executeQuery(sqlmail);
	            	while (rs.next()) {
	            		listarraymail.add(rs.getString("fweldername"));
	            		listarraymail.add(rs.getString("fcheckintime"));
	            		listarraymail.add(rs.getString("ficworkime"));
	            	}
	            	rs = stmt.executeQuery(sqlmailer);
	            	while (rs.next()) {
	            		listarraymailer.add(rs.getString("femailname"));
	            		listarraymailer.add(rs.getString("femailaddress"));
	            		listarraymailer.add(rs.getString("femailtype"));
	            	}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String halfyearname = "";
				
				for(int i=0;i<listarraymail.size();i+=3){
					
					//半年提醒
					Calendar canow = Calendar.getInstance();
					Calendar ca = Calendar.getInstance();
					ca.setTime(new Date());
					ca.add(Calendar.MONTH, -5);
					ca.add(Calendar.DAY_OF_MONTH, -15);
					Date resultDate = ca.getTime(); // 结果  
					String nowtime = DateTools.format("yyyy-MM-dd HH:mm:ss",resultDate);
					
					String[] nowtimebuf = nowtime.split(" ");
					String[] checkintimebuf = listarraymail.get(i+1).split(" ");
					
					nowtime = nowtimebuf[0];
					String checkintime = checkintimebuf[0];
							
					if(nowtime.equals(checkintime)){
						if(halfyearname.equals("")){
							halfyearname = listarraymail.get(i);
						}else{
							halfyearname = listarraymail.get(i) + "、" + halfyearname ;
						}
						
						String sqlmailcheck2 = "update tb_catweldinf set fhalfyearsure = '" + DateTools.format("yyyy-MM-dd HH:mm:ss",new Date()) + "' WHERE fweldername = '" + listarraymail.get(i) + "'";
					    try {
							stmt.execute(sqlmailcheck2);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
				}
				
				if(!halfyearname.equals("")){
					try{
						
						for(int j=0;j<listarraymailer.size();j+=3){
							if(listarraymailer.get(j+2).equals("1")){
								Properties props = new Properties();
							    props.setProperty("mail.smtp.auth", "true");
							    props.setProperty("mail.transport.protocol", "smtp");
							    props.put("mail.smtp.host","smtp.qq.com");// smtp服务器地址
							    
							    Session session = Session.getInstance(props);
							    session.setDebug(true);
							    
							    Message msg = new MimeMessage(session);
							    msg.setSubject("员工入职半年提醒");
							    msg.setText(halfyearname + " 入职已满半年");
							    msg.setSentDate(new Date());
							    msg.setFrom(new InternetAddress("512836904@qq.com"));//发件人邮箱
							    msg.setRecipient(Message.RecipientType.TO,
							            new InternetAddress(listarraymailer.get(j+1))); //收件人邮箱
							    //msg.addRecipient(Message.RecipientType.CC, 
					    		//new InternetAddress("XXXXXXXXXXX@qq.com")); //抄送人邮箱
							    msg.saveChanges();

							    Transport transport = session.getTransport();
							    transport.connect("512836904@qq.com","sbmqftbsitpecaef");//发件人邮箱,授权码
							    
							    transport.sendMessage(msg, msg.getAllRecipients());
							    transport.close();
							    
							    String nowtime = DateTools.format("yyyy-MM-dd HH:mm:ss",new Date());
							    String sqlmailcheck1 = "INSERT INTO tb_catemailcheck (femailname, femailaddress, femailtext, femailstatus, femailtime) VALUES ('" + listarraymailer.get(j) + "' , '" + listarraymailer.get(j+1) + "' , '" + halfyearname + " 入职已满半年" + "' , '1' , '" + nowtime + "')";
							    stmt.execute(sqlmailcheck1);
							}
						}
						
				    }catch(Exception e){
				    	e.getStackTrace();
				    }
				}
					
				//ic卡有效期提醒
				String icworktime = "";
					
				for(int i=0;i<listarraymail.size();i+=3){
					
					//ic卡有效期提醒
					
					try {
						Date dateic = DateTools.parse("yyyy-MM-dd HH:mm:ss",listarraymail.get(i+2));
						Calendar canow = Calendar.getInstance();
						Calendar ca = Calendar.getInstance();
						ca.setTime(dateic);
						ca.add(Calendar.DAY_OF_MONTH, -60);
						Date resultDate = ca.getTime(); // 结果  
						String ictime = DateTools.format("yyyy-MM-dd HH:mm:ss",resultDate);
						
						String[] timebuf = ictime.split(" ");
						String[] checkictimebuf = DateTools.format("yyyy-MM-dd HH:mm:ss",canow.getTime()).split(" ");
						
						ictime = timebuf[0];
						String checkictime = checkictimebuf[0];
								
						if(ictime.equals(checkictime)){
							if(icworktime.equals("")){
								icworktime = listarraymail.get(i);
							}else{
								icworktime = listarraymail.get(i) + "、" + icworktime ;
							}
						}
						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				if(!icworktime.equals("")){
					try{
						
						for(int j=0;j<listarraymailer.size();j+=3){
							if(listarraymailer.get(j+2).equals("2")){
								Properties props = new Properties();
							    props.setProperty("mail.smtp.auth", "true");
							    props.setProperty("mail.transport.protocol", "smtp");
							    props.put("mail.smtp.host","smtp.qq.com");// smtp服务器地址
							    
							    Session session = Session.getInstance(props);
							    session.setDebug(true);
							    
							    Message msg = new MimeMessage(session);
							    msg.setSubject("员工ic卡到期提醒");
							    msg.setText(icworktime + " ic卡将要过期");
							    msg.setSentDate(new Date());
							    msg.setFrom(new InternetAddress("512836904@qq.com"));//发件人邮箱
							    msg.setRecipient(Message.RecipientType.TO,
							            new InternetAddress(listarraymailer.get(j+1))); //收件人邮箱
							    //msg.addRecipient(Message.RecipientType.CC, 
					    		//new InternetAddress("XXXXXXXXXXX@qq.com")); //抄送人邮箱
							    msg.saveChanges();

							    Transport transport = session.getTransport();
							    transport.connect("512836904@qq.com","sbmqftbsitpecaef");//发件人邮箱,授权码
							    
							    transport.sendMessage(msg, msg.getAllRecipients());
							    transport.close();
							    
							    String nowtime = DateTools.format("yyyy-MM-dd HH:mm:ss",new Date());
							    String sqlmailcheck = "INSERT INTO tb_catemailcheck (femailname, femailaddress, femailtext, femailstatus, femailtime) VALUES ('" + listarraymailer.get(j) + "' , '" + listarraymailer.get(j+1) + "' , '" + icworktime + " ic卡将要过期" + "' , '2' , '" + nowtime + "')";
							    stmt.execute(sqlmailcheck);
							}
						}
						
				    }catch(Exception e){
				    	e.getStackTrace();
				    }
				
				}
				
			}  
        	
				
        }, time,86400000);*/
    	
		//更新优化报表
    	/*String timework1 = null;
    	String timework2 = null;
    	String time1 = null;
    	
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
    		
		    String[] values = ip.split(",");
			
			connet=connet1+values[0]+connet2+values[1]+connet3+values[2]+connet4+values[3]+connet5;
		    
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connet);
	        stmt= conn.createStatement();
	        
	        String sqlfirstwork1 = "SELECT tb_live_data.fWeldTime FROM tb_live_data ORDER BY tb_live_data.fWeldTime ASC LIMIT 0,1";
	        ResultSet rs1 =stmt.executeQuery(sqlfirstwork1);
        	while (rs1.next()) {
        		timework1 = rs1.getString("fWeldTime");
        	}
        	
	        String[] values1 = ip1.split("to");
	        
        	long datetime1 = DateTools.parse("yy-MM-dd HH:mm:ss",values1[0]).getTime();
	        System.out.println(datetime1);
	        
	        String sqlfirstwork2 = "SELECT tb_live_data.fWeldTime FROM tb_live_data ORDER BY tb_live_data.fWeldTime DESC LIMIT 0,1";
	        ResultSet rs2 =stmt.executeQuery(sqlfirstwork2);
        	while (rs2.next()) {
        		timework2 = rs2.getString("fWeldTime");
        	}
        	
        	long datetime2 = DateTools.parse("yy-MM-dd HH:mm:ss",values1[1]).getTime();
	        System.out.println(datetime2);
        	
	        for(long i=datetime1;i<=datetime2;i+=3600000){
	        	
	        	Date d1 = new Date(i);
	        	String t1 = DateTools.format("yy-MM-dd HH:mm:ss", d1);
	        	Date d2 = new Date(i+3600000);
	        	String t2 = DateTools.format("yy-MM-dd HH:mm:ss", d2);
	        	System.out.println(t1);
	        	System.out.println(t2);
	        	
	        	String sqlstandby = "INSERT INTO tb_standby(tb_standby.fwelder_id,tb_standby.fgather_no,tb_standby.fmachine_id,tb_standby.fjunction_id,"
                		+ "tb_standby.fitemid,tb_standby.felectricity,tb_standby.fvoltage,tb_standby.frateofflow,tb_standby.fstandbytime,tb_standby.fstarttime,tb_standby.fendtime) SELECT "
                		+ "tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,"
                		+ "AVG(tb_live_data.felectricity),AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + t1 + "','" + t2 + "' FROM tb_live_data "
                		+ "WHERE tb_live_data.fstatus = '0' AND tb_live_data.FWeldTime BETWEEN '" + t1 + "' AND '" + t2 + "' "
                		+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id";
                
                String sqlwork = "INSERT INTO tb_work(tb_work.fwelder_id,tb_work.fgather_no,tb_work.fmachine_id,tb_work.fjunction_id,tb_work.fitemid,"
                		+ "tb_work.felectricity,tb_work.fvoltage,tb_work.frateofflow,tb_work.fworktime,tb_work.fstarttime,tb_work.fendtime) SELECT tb_live_data.fwelder_id,"
                		+ "tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,AVG(tb_live_data.felectricity),"
                		+ "AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + t1 + "','" + t2 + "' FROM tb_live_data "
                		+ "WHERE tb_live_data.fstatus = '3' AND tb_live_data.FWeldTime BETWEEN '" + t1 + "' AND '" + t2 + "' "
                		+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id";
                
                String sqlalarm = "INSERT INTO tb_alarm(tb_alarm.fwelder_id,tb_alarm.fgather_no,tb_alarm.fmachine_id,tb_alarm.fjunction_id,tb_alarm.fitemid,"
                		+ "tb_alarm.felectricity,tb_alarm.fvoltage,tb_alarm.frateofflow,tb_alarm.falarmtime,tb_alarm.fstarttime,tb_alarm.fendtime) SELECT tb_live_data.fwelder_id,"
                		+ "tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,AVG(tb_live_data.felectricity),"
                		+ "AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + t1 + "','" + t2 + "' FROM tb_live_data "
                		+ "LEFT JOIN tb_welded_junction ON tb_live_data.fjunction_id = tb_welded_junction.fwelded_junction_no "
                		+ "WHERE fstatus= '3' and tb_welded_junction.fitemid = tb_live_data.fitemid and (tb_live_data.fvoltage > tb_welded_junction.fmax_valtage OR tb_live_data.felectricity > tb_welded_junction.fmax_electricity "
                		+ "OR tb_live_data.fvoltage < tb_welded_junction.fmin_valtage OR tb_live_data.felectricity < tb_welded_junction.fmin_electricity)"
                		+ " AND tb_live_data.FWeldTime BETWEEN '" + t1 + "' AND '" + t2 + "' "
                		+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id";
                
                stmt.executeUpdate(sqlstandby);
                stmt.executeUpdate(sqlwork);
                stmt.executeUpdate(sqlalarm);
                
				Thread.sleep(50);
					
	        }
	        
	        System.out.println("Done");
	        
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

    }  
    

    public Runnable socketstart = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
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
	                public void initChannel(SocketChannel chsoc) throws Exception {
	                	chsoc.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));    
	                	chsoc.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));    
	                	chsoc.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));    
	                	chsoc.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8)); 
	                	chsoc.pipeline().addLast(NS);
	                	socketcount++;
						socketlist.put(Integer.toString(socketcount),chsoc);
						NS.socketlist = socketlist;
						NWS.socketlist = socketlist;
						
	                }
	            });
	            
	            //绑定端口，等待同步成功  
	            ChannelFuture f;
				f = b.bind(5557).sync();
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
		}  
    	
    };
    
    public Runnable websocketstart = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			EventLoopGroup bossGroup = new NioEventLoopGroup();
	        EventLoopGroup workerGroup = new NioEventLoopGroup();
	        
	        try{
	            ServerBootstrap serverBootstrap = new ServerBootstrap();
	            serverBootstrap
	            	.group(bossGroup, workerGroup)
	            	.channel(NioServerSocketChannel.class)
	            	.childHandler(new ChannelInitializer<SocketChannel>(){

						@Override
						protected void initChannel(SocketChannel chweb) throws Exception {
							// TODO Auto-generated method stub

							chweb.pipeline().addLast("httpServerCodec", new HttpServerCodec());
							chweb.pipeline().addLast("chunkedWriteHandler", new ChunkedWriteHandler());
							chweb.pipeline().addLast("httpObjectAggregator", new HttpObjectAggregator(8192));
							chweb.pipeline().addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler("ws://localhost:5550/SerialPortDemo/ws/张三"));
							chweb.pipeline().addLast("myWebSocketHandler", NWS);
							websocketcount++;
							websocketlist.put(Integer.toString(websocketcount),chweb);
							NS.websocketlist = websocketlist;
							
							//System.out.println(chweb);
						}
	            		
	            	});
	            
	            Channel ch = serverBootstrap.bind(5550).sync().channel();
	            ch.closeFuture().sync();
	            
	            /*ChannelFuture channelFuture = serverBootstrap.bind(5550).sync();
	            channelFuture.channel().closeFuture().sync();*/
	            
	        } catch (Exception ex) { 
	 			 ex.printStackTrace();
	        } finally{
	            bossGroup.shutdownGracefully();
	            workerGroup.shutdownGracefully();
	        }
		}
    };
    
    public Runnable sockettran = new Runnable() {

		@Override
		public void run() {
			if(ip1!=null){
				client.run();
			}
		}
    };

	 public static void main(String [] args) throws IOException 
	 {  
	     Thread desktopServerThread = new Thread(new Server());  
	     desktopServerThread.start();  
	 }

}
    
    /*public Runnable websocketstart = new Runnable() {  
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
	                
l	                //读到字节（读取输入流数据到缓存）  
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
 };*/
 
 
 
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

	 
	/*class Handler implements Runnable {
		
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
                    	 
	                     DB_Connectionweb b =new DB_Connectionweb(connet);
	                     DB_Connectioncode c =new DB_Connectioncode(code,connet);
	                     DB_Connectioncode c =new DB_Connectioncode();
		                 String dbdata = b.getId();
		                 String limit = c.getId();
                    	 
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

                    	 
		                 for(int i=0;i<dbdata.length();i+=13){
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
		                 }
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
	    
	}*/

