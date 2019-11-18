import java.io.BufferedReader;  
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;  
import java.net.Socket;
import java.net.URL;
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
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

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
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
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
	public ArrayList<String> listarray4 = new ArrayList<String>();
	public HashMap<String, SocketChannel> socketlist = new HashMap<>();
	public HashMap<String, SocketChannel> websocketlist = new HashMap<>();
	public HashMap<String, SocketChannel> clientList = new HashMap<>();
	public int socketcount=0;
	public int websocketcount=0;
	public int clientcount=0;
	public Selector selector = null;
	public ServerSocketChannel ssc = null;
	public Client client = new Client(this);
	public NettyServerHandler NS = new NettyServerHandler(this);
	public NettyServerHandlerTest NStest = new NettyServerHandlerTest();
	private NettyWebSocketHandler NWS = new NettyWebSocketHandler();
	private Connection c;
	public java.sql.Connection conn = null;
	public java.sql.Statement stmt =null;
	private Date time;
	private Date time1;
	private ArrayList<String> dbdata;
	public String outlinestatus = "A";

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
			stmt = conn.createStatement();
			NS.stmt = stmt;
			NS.conn = conn;
			NS.mysql.db.conn = conn;
			NS.mysql.db.stmt = stmt;
			NS.android.db.conn = conn;
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

					//华域统计到work表
					/*Date date = new Date();
					String nowtimefor = DateTools.format("yyyy-MM-dd",date);
					String nowtime = DateTools.format("HH:mm:ss",date);
					String[] timesplit = nowtime.split(":");
					String hour = timesplit[0];
					String time2 = nowtimefor+" "+hour+":00:00";
					Date d1 = new Date((DateTools.parse("yyyy-MM-dd HH:mm:ss",time2).getTime())-3600000);
					String time3 = DateTools.format("yyyy-MM-dd HH:mm:ss",d1);

					String timestandby = null;
					String timework = null;
					String sqlfirststandby = "SELECT tb_standby.fUploadDataTime FROM tb_standby ORDER BY tb_standby.fUploadDataTime DESC LIMIT 0,1";
					String sqlfirstwork = "SELECT tb_work.fUploadDataTime FROM tb_work ORDER BY tb_work.fUploadDataTime DESC LIMIT 0,1";
					ResultSet rs1 =stmt.executeQuery(sqlfirstwork);
					while (rs1.next()) {
						timework = rs1.getString("fUploadDataTime");
					}
					ResultSet rs2 =stmt.executeQuery(sqlfirststandby);
					while (rs2.next()) {
						timestandby = rs2.getString("fUploadDataTime");
					}
					
					if(timework == null || timework.equals("null")){
						timework = "2000-01-01 01:01:01";
					}
					if(timestandby == null || timestandby.equals("null")){
						timestandby = "2000-01-01 01:01:01";
					}
					String sqlwork = "INSERT INTO tb_work(tb_work.fwelder_id,tb_work.fgather_no,tb_work.fmachine_id,tb_work.fjunction_id,tb_work.fitemid,"
                    		+ "tb_work.felectricity,tb_work.fvoltage,tb_work.frateofflow,tb_work.fworktime,tb_work.fstarttime,tb_work.fendtime,tb_work.fwelder_no,tb_work.fjunction_no,tb_work.fweld_no,tb_work.fchannel,tb_work.fmax_electricity,tb_work.fmin_electricity,tb_work.fmax_voltage,tb_work.fmin_voltage,tb_work.fwelder_itemid,tb_work.fjunction_itemid,tb_work.fmachine_itemid,tb_work.fwirefeedrate,tb_work.fmachinemodel,tb_work.fwirediameter,tb_work.fmaterialgas,tb_work.fstatus,tb_work.fd1000,tb_work.fd1001,tb_work.fd1002,tb_work.fd1003,tb_work.fd1004,tb_work.fd1005,tb_work.fd1006,tb_work.fd1007,tb_work.fd1008,tb_work.fd1009,tb_work.fd1010,tb_work.fd1011,tb_work.fd1012,tb_work.fd1013,tb_work.fd1014,tb_work.fd1015,tb_work.fd1016,tb_work.fd1017,tb_work.fd1018,tb_work.fd1019,tb_work.fd1020,tb_work.fd1021,tb_work.fd1022,tb_work.fd1023,tb_work.fd1024,tb_work.fd1025,tb_work.fd1026,tb_work.fd1027,tb_work.fd1028,tb_work.fd1029,tb_work.fd1030,tb_work.fd1031,tb_work.fd1032,tb_work.fd1033,tb_work.fd1034,tb_work.fd1035,tb_work.fd1036,tb_work.fd1037,tb_work.fd1038,tb_work.fd1039,tb_work.fd1040,tb_work.fd1041,tb_work.fd1042,tb_work.fd1043,tb_work.fd1044,tb_work.fd1045,tb_work.fd1046,tb_work.fd1047,tb_work.fd1048,tb_work.fd1049,tb_work.fd1050,tb_work.fd1051,tb_work.fd1052,tb_work.fd1053,tb_work.fd1054,tb_work.fd1055,tb_work.fd1056,tb_work.fd1057,tb_work.fd1058,tb_work.fd1059,tb_work.fd1060,tb_work.fd1061,tb_work.fd1062,tb_work.fd1063,tb_work.fd1064,tb_work.fd1065,tb_work.fd1066,tb_work.fd1067,tb_work.fd1068,tb_work.fd1069,tb_work.fd1070,tb_work.fd1071,tb_work.fd1072) SELECT tb_live_data.fwelder_id,"
                     		+ "tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,AVG(tb_live_data.felectricity),"
                    		+ "AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + time3 + "','" + time2 + "',tb_live_data.fwelder_no,tb_live_data.fjunction_no,tb_live_data.fweld_no,tb_live_data.fchannel,tb_live_data.fmax_electricity,tb_live_data.fmin_electricity,tb_live_data.fmax_voltage,tb_live_data.fmin_voltage,tb_live_data.fwelder_itemid,tb_live_data.fjunction_itemid,tb_live_data.fmachine_itemid,AVG(tb_live_data.fwirefeedrate),tb_live_data.fmachinemodel,tb_live_data.fwirediameter,tb_live_data.fmaterialgas,tb_live_data.fd1000,tb_live_data.fd1001,tb_live_data.fd1002,tb_live_data.fd1003,tb_live_data.fd1004,tb_live_data.fd1005,tb_live_data.fd1006,tb_live_data.fd1007,tb_live_data.fd1008,tb_live_data.fd1009,tb_live_data.fd1010,tb_live_data.fd1011,tb_live_data.fd1012,tb_live_data.fd1013,tb_live_data.fd1014,tb_live_data.fd1015,tb_live_data.fd1016,tb_live_data.fd1017,tb_live_data.fd1018,tb_live_data.fd1019,tb_live_data.fd1020,tb_live_data.fd1021,tb_live_data.fd1022,tb_live_data.fd1023,tb_live_data.fd1024,tb_live_data.fd1025,tb_live_data.fd1026,tb_live_data.fd1027,tb_live_data.fd1028,tb_live_data.fd1029,tb_live_data.fd1030,tb_live_data.fd1031,tb_live_data.fd1032,tb_live_data.fd1033,tb_live_data.fd1034,tb_live_data.fd1035,tb_live_data.fd1036,tb_live_data.fd1037,tb_live_data.fd1038,tb_live_data.fd1039,tb_live_data.fd1040,tb_live_data.fd1041,tb_live_data.fd1042,tb_live_data.fd1043,tb_live_data.fd1044,tb_live_data.fd1045,tb_live_data.fd1046,tb_live_data.fd1047,tb_live_data.fd1048,tb_live_data.fd1049,tb_live_data.fd1050,tb_live_data.fd1051,tb_live_data.fd1052,tb_live_data.fd1053,tb_live_data.fd1054,tb_live_data.fd1055,tb_live_data.fd1056,tb_live_data.fd1057,tb_live_data.fd1058,tb_live_data.fd1059,tb_live_data.fd1060,tb_live_data.fd1061,tb_live_data.fd1062,tb_live_data.fd1063,tb_live_data.fd1064,tb_live_data.fd1065,tb_live_data.fd1066,tb_live_data.fd1067,tb_live_data.fd1068,tb_live_data.fd1069,tb_live_data.fd1070,tb_live_data.fd1071,tb_live_data.fd1072 FROM tb_live_data "
                    		+ "WHERE tb_live_data.fstatus = '3' AND tb_live_data.FWeldTime BETWEEN '" + timework + "' AND '" + time2 + "' "
                    		+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id,tb_live_data.fstatus,tb_live_data.fmachine_id";
					
					String sqlstandby = "INSERT INTO tb_standby(tb_standby.fwelder_id,tb_standby.fgather_no,tb_standby.fmachine_id,tb_standby.fjunction_id,"
							+ "tb_standby.fitemid,tb_standby.felectricity,tb_standby.fvoltage,tb_standby.frateofflow,tb_standby.fstandbytime,tb_standby.fstarttime,tb_standby.fendtime,tb_standby.fwelder_no,tb_standby.fjunction_no,tb_standby.fweld_no,tb_standby.fchannel,tb_standby.fmax_electricity,tb_standby.fmin_electricity,tb_standby.fmax_voltage,tb_standby.fmin_voltage,tb_standby.fwelder_itemid,tb_standby.fjunction_itemid,tb_standby.fmachine_itemid,tb_standby.fwirefeedrate,tb_standby.fmachinemodel,tb_standby.fwirediameter,tb_standby.fmaterialgas,tb_standby.fstatus,tb_standby.fd1000,tb_standby.fd1001,tb_standby.fd1002,tb_standby.fd1003,tb_standby.fd1004,tb_standby.fd1005,tb_standby.fd1006,tb_standby.fd1007,tb_standby.fd1008,tb_standby.fd1009,tb_standby.fd1010,tb_standby.fd1011,tb_standby.fd1012,tb_standby.fd1013,tb_standby.fd1014,tb_standby.fd1015,tb_standby.fd1016,tb_standby.fd1017,tb_standby.fd1018,tb_standby.fd1019,tb_standby.fd1020,tb_standby.fd1021,tb_standby.fd1022,tb_standby.fd1023,tb_standby.fd1024,tb_standby.fd1025,tb_standby.fd1026,tb_standby.fd1027,tb_standby.fd1028,tb_standby.fd1029,tb_standby.fd1030,tb_standby.fd1031,tb_standby.fd1032,tb_standby.fd1033,tb_standby.fd1034,tb_standby.fd1035,tb_standby.fd1036,tb_standby.fd1037,tb_standby.fd1038,tb_standby.fd1039,tb_standby.fd1040,tb_standby.fd1041,tb_standby.fd1042,tb_standby.fd1043,tb_standby.fd1044,tb_standby.fd1045,tb_standby.fd1046,tb_standby.fd1047,tb_standby.fd1048,tb_standby.fd1049,tb_standby.fd1050,tb_standby.fd1051,tb_standby.fd1052,tb_standby.fd1053,tb_standby.fd1054,tb_standby.fd1055,tb_standby.fd1056,tb_standby.fd1057,tb_standby.fd1058,tb_standby.fd1059,tb_standby.fd1060,tb_standby.fd1061,tb_standby.fd1062,tb_standby.fd1063,tb_standby.fd1064,tb_standby.fd1065,tb_standby.fd1066,tb_standby.fd1067,tb_standby.fd1068,tb_standby.fd1069,tb_standby.fd1070,tb_standby.fd1071,tb_standby.fd1072) SELECT "
							+ "tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,"
							+ "AVG(tb_live_data.felectricity),AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + time3 + "','" + time2 + "',tb_live_data.fwelder_no,tb_live_data.fjunction_no,tb_live_data.fweld_no,tb_live_data.fchannel,tb_live_data.fmax_electricity,tb_live_data.fmin_electricity,tb_live_data.fmax_voltage,tb_live_data.fmin_voltage,tb_live_data.fwelder_itemid,tb_live_data.fjunction_itemid,tb_live_data.fmachine_itemid,AVG(tb_live_data.fwirefeedrate),tb_live_data.fmachinemodel,tb_live_data.fwirediameter,tb_live_data.fmaterialgas,tb_live_data.fstatus,tb_live_data.fd1000,tb_live_data.fd1001,tb_live_data.fd1002,tb_live_data.fd1003,tb_live_data.fd1004,tb_live_data.fd1005,tb_live_data.fd1006,tb_live_data.fd1007,tb_live_data.fd1008,tb_live_data.fd1009,tb_live_data.fd1010,tb_live_data.fd1011,tb_live_data.fd1012,tb_live_data.fd1013,tb_live_data.fd1014,tb_live_data.fd1015,tb_live_data.fd1016,tb_live_data.fd1017,tb_live_data.fd1018,tb_live_data.fd1019,tb_live_data.fd1020,tb_live_data.fd1021,tb_live_data.fd1022,tb_live_data.fd1023,tb_live_data.fd1024,tb_live_data.fd1025,tb_live_data.fd1026,tb_live_data.fd1027,tb_live_data.fd1028,tb_live_data.fd1029,tb_live_data.fd1030,tb_live_data.fd1031,tb_live_data.fd1032,tb_live_data.fd1033,tb_live_data.fd1034,tb_live_data.fd1035,tb_live_data.fd1036,tb_live_data.fd1037,tb_live_data.fd1038,tb_live_data.fd1039,tb_live_data.fd1040,tb_live_data.fd1041,tb_live_data.fd1042,tb_live_data.fd1043,tb_live_data.fd1044,tb_live_data.fd1045,tb_live_data.fd1046,tb_live_data.fd1047,tb_live_data.fd1048,tb_live_data.fd1049,tb_live_data.fd1050,tb_live_data.fd1051,tb_live_data.fd1052,tb_live_data.fd1053,tb_live_data.fd1054,tb_live_data.fd1055,tb_live_data.fd1056,tb_live_data.fd1057,tb_live_data.fd1058,tb_live_data.fd1059,tb_live_data.fd1060,tb_live_data.fd1061,tb_live_data.fd1062,tb_live_data.fd1063,tb_live_data.fd1064,tb_live_data.fd1065,tb_live_data.fd1066,tb_live_data.fd1067,tb_live_data.fd1068,tb_live_data.fd1069,tb_live_data.fd1070,tb_live_data.fd1071,tb_live_data.fd1072 FROM tb_live_data "
							+ "WHERE tb_live_data.fstatus = '0' AND tb_live_data.FWeldTime BETWEEN '" + timestandby + "' AND '" + time2 + "' "
							+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id,tb_live_data.fstatus,tb_live_data.fmachine_id";
					
					stmt.executeUpdate(sqlwork);
					stmt.executeUpdate(sqlstandby);*/
					
					
					//基本版
					//获取上次统计时间，为空插入赋默认值
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
					String timewarn = null;
					String sqlfirstwork = "SELECT tb_work.fUploadDataTime FROM tb_work ORDER BY tb_work.fUploadDataTime DESC LIMIT 0,1";
					String sqlfirststandby = "SELECT tb_standby.fUploadDataTime FROM tb_standby ORDER BY tb_standby.fUploadDataTime DESC LIMIT 0,1";
					String sqlfirstalarm = "SELECT tb_alarm.fUploadDataTime FROM tb_alarm ORDER BY tb_alarm.fUploadDataTime DESC LIMIT 0,1";
					String sqlfirstwarn = "SELECT tb_warn.fUploadDataTime FROM tb_warn ORDER BY tb_warn.fUploadDataTime DESC LIMIT 0,1";
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
					ResultSet rs4 =stmt.executeQuery(sqlfirstwarn);
					while (rs4.next()) {
						timewarn = rs4.getString("fUploadDataTime");
					}

					if(timework == null || timework.equals("null")){
						timework = "2000-01-01 01:01:01";
					}
					if(timestandby == null || timestandby.equals("null")){
						timestandby = "2000-01-01 01:01:01";
					}
					if(timealarm == null || timealarm.equals("null")){
						timealarm = "2000-01-01 01:01:01";
					}
					if(timewarn == null || timewarn.equals("null")){
						timewarn = "2000-01-01 01:01:01";
					}

					//统计四张状态表
					String sqlstandby = "INSERT INTO tb_standby(tb_standby.fwelder_id,tb_standby.fgather_no,tb_standby.fmachine_id,tb_standby.fjunction_id,"
							+ "tb_standby.fitemid,tb_standby.felectricity,tb_standby.fvoltage,tb_standby.frateofflow,tb_standby.fstandbytime,tb_standby.fstarttime,tb_standby.fendtime,tb_standby.fwelder_no,tb_standby.fjunction_no,tb_standby.fweld_no,tb_standby.fchannel,tb_standby.fmax_electricity,tb_standby.fmin_electricity,tb_standby.fmax_voltage,tb_standby.fmin_voltage,tb_standby.fwelder_itemid,tb_standby.fjunction_itemid,tb_standby.fmachine_itemid,tb_standby.fwirefeedrate,tb_standby.fmachinemodel,tb_standby.fwirediameter,tb_standby.fmaterialgas,tb_standby.fstatus) SELECT "
							+ "tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,"
							+ "AVG(tb_live_data.felectricity),AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + time3 + "','" + time2 + "',tb_live_data.fwelder_no,tb_live_data.fjunction_no,tb_live_data.fweld_no,tb_live_data.fchannel,tb_live_data.fmax_electricity,tb_live_data.fmin_electricity,tb_live_data.fmax_voltage,tb_live_data.fmin_voltage,tb_live_data.fwelder_itemid,tb_live_data.fjunction_itemid,tb_live_data.fmachine_itemid,AVG(tb_live_data.fwirefeedrate),tb_live_data.fmachinemodel,tb_live_data.fwirediameter,tb_live_data.fmaterialgas,tb_live_data.fstatus FROM tb_live_data "
							+ "WHERE tb_live_data.fstatus = '0' AND tb_live_data.FWeldTime BETWEEN '" + timestandby + "' AND '" + time2 + "' "
							+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id,tb_live_data.fstatus,tb_live_data.fmachine_id,tb_live_data.fwirediameter";

					String sqlwork = "INSERT INTO tb_work(tb_work.fwelder_id,tb_work.fgather_no,tb_work.fmachine_id,tb_work.fjunction_id,tb_work.fitemid,"
							+ "tb_work.felectricity,tb_work.fvoltage,tb_work.frateofflow,tb_work.fworktime,tb_work.fstarttime,tb_work.fendtime,tb_work.fwelder_no,tb_work.fjunction_no,tb_work.fweld_no,tb_work.fchannel,tb_work.fmax_electricity,tb_work.fmin_electricity,tb_work.fmax_voltage,tb_work.fmin_voltage,tb_work.fwelder_itemid,tb_work.fjunction_itemid,tb_work.fmachine_itemid,tb_work.fwirefeedrate,tb_work.fmachinemodel,tb_work.fwirediameter,tb_work.fmaterialgas,tb_work.fstatus) SELECT tb_live_data.fwelder_id,"
							+ "tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,AVG(tb_live_data.felectricity),"
							+ "AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + time3 + "','" + time2 + "',tb_live_data.fwelder_no,tb_live_data.fjunction_no,tb_live_data.fweld_no,tb_live_data.fchannel,tb_live_data.fmax_electricity,tb_live_data.fmin_electricity,tb_live_data.fmax_voltage,tb_live_data.fmin_voltage,tb_live_data.fwelder_itemid,tb_live_data.fjunction_itemid,tb_live_data.fmachine_itemid,AVG(tb_live_data.fwirefeedrate),tb_live_data.fmachinemodel,tb_live_data.fwirediameter,tb_live_data.fmaterialgas,tb_live_data.fstatus FROM tb_live_data "
							+ "WHERE (tb_live_data.fstatus = '3' OR fstatus= '5' OR fstatus= '7' OR fstatus= '99') AND tb_live_data.FWeldTime BETWEEN '" + timework + "' AND '" + time2 + "' "
							+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id,tb_live_data.fstatus,tb_live_data.fmachine_id,tb_live_data.fwirediameter";

					String sqlwarn = "INSERT INTO tb_warn(tb_warn.fwelder_id,tb_warn.fgather_no,tb_warn.fmachine_id,tb_warn.fjunction_id,"
							+ "tb_warn.fitemid,tb_warn.felectricity,tb_warn.fvoltage,tb_warn.frateofflow,tb_warn.fwarntime,tb_warn.fstarttime,tb_warn.fendtime,tb_warn.fwelder_no,tb_warn.fjunction_no,tb_warn.fweld_no,tb_warn.fchannel,tb_warn.fmax_electricity,tb_warn.fmin_electricity,tb_warn.fmax_voltage,tb_warn.fmin_voltage,tb_warn.fwelder_itemid,tb_warn.fjunction_itemid,tb_warn.fmachine_itemid,tb_warn.fwirefeedrate,tb_warn.fmachinemodel,tb_warn.fwirediameter,tb_warn.fmaterialgas,tb_warn.fstatus) SELECT "
							+ "tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,"
							+ "AVG(tb_live_data.felectricity),AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + time3 + "','" + time2 + "',tb_live_data.fwelder_no,tb_live_data.fjunction_no,tb_live_data.fweld_no,tb_live_data.fchannel,tb_live_data.fmax_electricity,tb_live_data.fmin_electricity,tb_live_data.fmax_voltage,tb_live_data.fmin_voltage,tb_live_data.fwelder_itemid,tb_live_data.fjunction_itemid,tb_live_data.fmachine_itemid,AVG(tb_live_data.fwirefeedrate),tb_live_data.fmachinemodel,tb_live_data.fwirediameter,tb_live_data.fmaterialgas,tb_live_data.fstatus FROM tb_live_data "
							+ "WHERE tb_live_data.fstatus != '0' AND tb_live_data.fstatus != '3' AND tb_live_data.fstatus != '5' AND tb_live_data.fstatus != '7' AND tb_live_data.FWeldTime BETWEEN '" + timewarn + "' AND '" + time2 + "' "
							+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id,tb_live_data.fstatus,tb_live_data.fmachine_id,tb_live_data.fwirediameter";

					String sqlalarm = "INSERT INTO tb_alarm(tb_alarm.fwelder_id,tb_alarm.fgather_no,tb_alarm.fmachine_id,tb_alarm.fjunction_id,tb_alarm.fitemid,"
							+ "tb_alarm.felectricity,tb_alarm.fvoltage,tb_alarm.frateofflow,tb_alarm.falarmtime,tb_alarm.fstarttime,tb_alarm.fendtime,tb_alarm.fwelder_no,tb_alarm.fjunction_no,tb_alarm.fweld_no,tb_alarm.fchannel,tb_alarm.fmax_electricity,tb_alarm.fmin_electricity,tb_alarm.fmax_voltage,tb_alarm.fmin_voltage,tb_alarm.fwelder_itemid,tb_alarm.fjunction_itemid,tb_alarm.fmachine_itemid,tb_alarm.fwirefeedrate,tb_alarm.fmachinemodel,tb_alarm.fwirediameter,tb_alarm.fmaterialgas,tb_alarm.fstatus) SELECT tb_live_data.fwelder_id,"
							+ "tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,AVG(tb_live_data.felectricity),"
							+ "AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + time3 + "','" + time2 + "',tb_live_data.fwelder_no,tb_live_data.fjunction_no,tb_live_data.fweld_no,tb_live_data.fchannel,tb_live_data.fmax_electricity,tb_live_data.fmin_electricity,tb_live_data.fmax_voltage,tb_live_data.fmin_voltage,tb_live_data.fwelder_itemid,tb_live_data.fjunction_itemid,tb_live_data.fmachine_itemid,AVG(tb_live_data.fwirefeedrate),tb_live_data.fmachinemodel,tb_live_data.fwirediameter,tb_live_data.fmaterialgas,tb_live_data.fstatus FROM tb_live_data "
							+ "INNER JOIN tb_welded_junction ON tb_live_data.fjunction_id = tb_welded_junction.fwelded_junction_no "
							+ "WHERE (fstatus= '98' OR fstatus= '99')"
							+ " AND tb_live_data.FWeldTime BETWEEN '" + timealarm + "' AND '" + time2 + "' "
							+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id,tb_live_data.fstatus,tb_live_data.fmachine_id,tb_live_data.fwirediameter";

					stmt.executeUpdate(sqlstandby);
					stmt.executeUpdate(sqlwork);
					stmt.executeUpdate(sqlwarn);
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
		}, 0 , 1000*60);
		
		//断网续传数据每天更新三张状态表
		Date date1 = new Date();
		String nowtime1 = DateTools.format("yyyy-MM-dd",date);
		String[] timesplit1 = nowtime1.split("-");
		String day1 = timesplit1[2];

		Calendar calendar1 = Calendar.getInstance();

		calendar1.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day1)+1); // 控制天
		calendar1.set(Calendar.HOUR_OF_DAY, 00); // 控制时
		calendar1.set(Calendar.MINUTE, 00);    // 控制分
		calendar1.set(Calendar.SECOND, 00);    // 控制秒
		time1 = calendar1.getTime(); 

		Timer tExit11 = null; 
		tExit11 = new Timer();  
		tExit11.schedule(new TimerTask() {  

			@Override  
			public void run() {

				String Afirsttime = null;
				String Bfirsttime = null;
				String Alasttime = null;
				String Blasttime = null;
				
				try {  

					Class.forName("com.mysql.jdbc.Driver");  
					conn = DriverManager.getConnection(connet);
					stmt= conn.createStatement();
					NS.stmt = stmt;

					//基本版
					//获取上次统计时间，为空插入赋默认值
					Date date = new Date();
					String nowtimefor = DateTools.format("yyyy-MM-dd",date);
					String nowtime = DateTools.format("HH:mm:ss",date);
					String[] timesplit = nowtime.split(":");
					String hour = timesplit[0];
					String time2 = nowtimefor+" "+hour+":00:00";
					Date d1 = new Date((DateTools.parse("yyyy-MM-dd HH:mm:ss",time2).getTime())-3600000);
					
					String sqlAfirst = "SELECT DATE_FORMAT(tb_dataA.FWeldTime,'%Y-%m-%d') FROM tb_dataA ORDER BY tb_dataA.FWeldTime desc LIMIT 0,1 ";
					String sqlBfirst = "SELECT DATE_FORMAT(tb_dataB.FWeldTime,'%Y-%m-%d') FROM tb_dataB ORDER BY tb_dataB.FWeldTime desc LIMIT 0,1 ";
					String sqlAlast = "SELECT DATE_FORMAT(tb_dataA.FWeldTime,'%Y-%m-%d') FROM tb_dataA ORDER BY tb_dataA.FWeldTime asc LIMIT 0,1 ";
					String sqlBlast = "SELECT DATE_FORMAT(tb_dataB.FWeldTime,'%Y-%m-%d') FROM tb_dataB ORDER BY tb_dataB.FWeldTime asc LIMIT 0,1 ";
					String sqlAtrun = "TRUNCATE TABLE tb_dataA";
					String sqlBtrun = "TRUNCATE TABLE tb_dataB";
					
					if(outlinestatus.equals("A")){
						ResultSet rs1 =stmt.executeQuery(sqlAfirst);
						while (rs1.next()) {
							Alasttime = rs1.getString("DATE_FORMAT(tb_dataA.FWeldTime,'%Y-%m-%d')") + " 23:59:59";
						}
						ResultSet rs3 =stmt.executeQuery(sqlAlast);
						while (rs3.next()) {
							Afirsttime = rs3.getString("DATE_FORMAT(tb_dataA.FWeldTime,'%Y-%m-%d')") + " 06:00:00";
						}
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						if(null != Alasttime && !"null".equals(Alasttime) && null != Afirsttime && !"null".equals(Afirsttime)) {
							long Alast = sdf.parse(Alasttime).getTime()+1000;
							long Afirst = sdf.parse(Afirsttime).getTime();
							//统计四张状态表
							for(long i=Afirst;i<Alast;i+=3600000){
								String datebuf1 = DateTools.format("yyyy-MM-dd HH:mm:ss", new Date(i));
								String datebuf2 = DateTools.format("yyyy-MM-dd HH:mm:ss", new Date(i+3600000));
								String sqlstandby = "INSERT INTO tb_standby(tb_standby.fwelder_id,tb_standby.fgather_no,tb_standby.fmachine_id,tb_standby.fjunction_id,"
										+ "tb_standby.fitemid,tb_standby.felectricity,tb_standby.fvoltage,tb_standby.frateofflow,tb_standby.fstandbytime,tb_standby.fstarttime,tb_standby.fendtime,tb_standby.fwelder_no,tb_standby.fjunction_no,tb_standby.fweld_no,tb_standby.fchannel,tb_standby.fmax_electricity,tb_standby.fmin_electricity,tb_standby.fmax_voltage,tb_standby.fmin_voltage,tb_standby.fwelder_itemid,tb_standby.fjunction_itemid,tb_standby.fmachine_itemid,tb_standby.fwirefeedrate,tb_standby.fmachinemodel,tb_standby.fwirediameter,tb_standby.fmaterialgas,tb_standby.fstatus) SELECT "
										+ "tb_dataA.fwelder_id,tb_dataA.fgather_no,tb_dataA.fmachine_id,tb_dataA.fjunction_id,tb_dataA.fitemid,"
										+ "AVG(tb_dataA.felectricity),AVG(tb_dataA.fvoltage),AVG(tb_dataA.frateofflow),COUNT(tb_dataA.fid),'" + datebuf1 + "','" + datebuf2 + "',tb_dataA.fwelder_no,tb_dataA.fjunction_no,tb_dataA.fweld_no,tb_dataA.fchannel,tb_dataA.fmax_electricity,tb_dataA.fmin_electricity,tb_dataA.fmax_voltage,tb_dataA.fmin_voltage,tb_dataA.fwelder_itemid,tb_dataA.fjunction_itemid,tb_dataA.fmachine_itemid,AVG(tb_dataA.fwirefeedrate),tb_dataA.fmachinemodel,tb_dataA.fwirediameter,tb_dataA.fmaterialgas,tb_dataA.fstatus FROM tb_dataA "
										+ "WHERE tb_dataA.fstatus = '0' AND tb_dataA.FWeldTime BETWEEN '" + datebuf1 + "' AND '" + datebuf2 + "' "
										+ "GROUP BY tb_dataA.fwelder_id,tb_dataA.fgather_no,tb_dataA.fjunction_id,tb_dataA.fstatus,tb_dataA.fmachine_id,tb_dataA.fwirediameter";

								String sqlwork = "INSERT INTO tb_work(tb_work.fwelder_id,tb_work.fgather_no,tb_work.fmachine_id,tb_work.fjunction_id,tb_work.fitemid,"
										+ "tb_work.felectricity,tb_work.fvoltage,tb_work.frateofflow,tb_work.fworktime,tb_work.fstarttime,tb_work.fendtime,tb_work.fwelder_no,tb_work.fjunction_no,tb_work.fweld_no,tb_work.fchannel,tb_work.fmax_electricity,tb_work.fmin_electricity,tb_work.fmax_voltage,tb_work.fmin_voltage,tb_work.fwelder_itemid,tb_work.fjunction_itemid,tb_work.fmachine_itemid,tb_work.fwirefeedrate,tb_work.fmachinemodel,tb_work.fwirediameter,tb_work.fmaterialgas,tb_work.fstatus) SELECT tb_dataA.fwelder_id,"
										+ "tb_dataA.fgather_no,tb_dataA.fmachine_id,tb_dataA.fjunction_id,tb_dataA.fitemid,AVG(tb_dataA.felectricity),"
										+ "AVG(tb_dataA.fvoltage),AVG(tb_dataA.frateofflow),COUNT(tb_dataA.fid),'" + datebuf1 + "','" + datebuf2 + "',tb_dataA.fwelder_no,tb_dataA.fjunction_no,tb_dataA.fweld_no,tb_dataA.fchannel,tb_dataA.fmax_electricity,tb_dataA.fmin_electricity,tb_dataA.fmax_voltage,tb_dataA.fmin_voltage,tb_dataA.fwelder_itemid,tb_dataA.fjunction_itemid,tb_dataA.fmachine_itemid,AVG(tb_dataA.fwirefeedrate),tb_dataA.fmachinemodel,tb_dataA.fwirediameter,tb_dataA.fmaterialgas,tb_dataA.fstatus FROM tb_dataA "
										+ "WHERE (tb_dataA.fstatus = '3' OR fstatus= '5' OR fstatus= '7' OR fstatus= '99') AND tb_dataA.FWeldTime BETWEEN '" + datebuf1 + "' AND '" + datebuf2 + "' "
										+ "GROUP BY tb_dataA.fwelder_id,tb_dataA.fgather_no,tb_dataA.fjunction_id,tb_dataA.fstatus,tb_dataA.fmachine_id,tb_dataA.fwirediameter";

								String sqlwarn = "INSERT INTO tb_warn(tb_warn.fwelder_id,tb_warn.fgather_no,tb_warn.fmachine_id,tb_warn.fjunction_id,"
										+ "tb_warn.fitemid,tb_warn.felectricity,tb_warn.fvoltage,tb_warn.frateofflow,tb_warn.fwarntime,tb_warn.fstarttime,tb_warn.fendtime,tb_warn.fwelder_no,tb_warn.fjunction_no,tb_warn.fweld_no,tb_warn.fchannel,tb_warn.fmax_electricity,tb_warn.fmin_electricity,tb_warn.fmax_voltage,tb_warn.fmin_voltage,tb_warn.fwelder_itemid,tb_warn.fjunction_itemid,tb_warn.fmachine_itemid,tb_warn.fwirefeedrate,tb_warn.fmachinemodel,tb_warn.fwirediameter,tb_warn.fmaterialgas,tb_warn.fstatus) SELECT "
										+ "tb_dataA.fwelder_id,tb_dataA.fgather_no,tb_dataA.fmachine_id,tb_dataA.fjunction_id,tb_dataA.fitemid,"
										+ "AVG(tb_dataA.felectricity),AVG(tb_dataA.fvoltage),AVG(tb_dataA.frateofflow),COUNT(tb_dataA.fid),'" + datebuf1 + "','" + datebuf2 + "',tb_dataA.fwelder_no,tb_dataA.fjunction_no,tb_dataA.fweld_no,tb_dataA.fchannel,tb_dataA.fmax_electricity,tb_dataA.fmin_electricity,tb_dataA.fmax_voltage,tb_dataA.fmin_voltage,tb_dataA.fwelder_itemid,tb_dataA.fjunction_itemid,tb_dataA.fmachine_itemid,AVG(tb_dataA.fwirefeedrate),tb_dataA.fmachinemodel,tb_dataA.fwirediameter,tb_dataA.fmaterialgas,tb_dataA.fstatus FROM tb_dataA "
										+ "WHERE tb_dataA.fstatus != '0' AND tb_dataA.fstatus != '3' AND tb_dataA.fstatus != '5' AND tb_dataA.fstatus != '7' AND tb_dataA.FWeldTime BETWEEN '" + datebuf1 + "' AND '" + datebuf2 + "' "
										+ "GROUP BY tb_dataA.fwelder_id,tb_dataA.fgather_no,tb_dataA.fjunction_id,tb_dataA.fstatus,tb_dataA.fmachine_id,tb_dataA.fwirediameter";

								String sqlalarm = "INSERT INTO tb_alarm(tb_alarm.fwelder_id,tb_alarm.fgather_no,tb_alarm.fmachine_id,tb_alarm.fjunction_id,tb_alarm.fitemid,"
										+ "tb_alarm.felectricity,tb_alarm.fvoltage,tb_alarm.frateofflow,tb_alarm.falarmtime,tb_alarm.fstarttime,tb_alarm.fendtime,tb_alarm.fwelder_no,tb_alarm.fjunction_no,tb_alarm.fweld_no,tb_alarm.fchannel,tb_alarm.fmax_electricity,tb_alarm.fmin_electricity,tb_alarm.fmax_voltage,tb_alarm.fmin_voltage,tb_alarm.fwelder_itemid,tb_alarm.fjunction_itemid,tb_alarm.fmachine_itemid,tb_alarm.fwirefeedrate,tb_alarm.fmachinemodel,tb_alarm.fwirediameter,tb_alarm.fmaterialgas,tb_alarm.fstatus) SELECT tb_dataA.fwelder_id,"
										+ "tb_dataA.fgather_no,tb_dataA.fmachine_id,tb_dataA.fjunction_id,tb_dataA.fitemid,AVG(tb_dataA.felectricity),"
										+ "AVG(tb_dataA.fvoltage),AVG(tb_dataA.frateofflow),COUNT(tb_dataA.fid),'" + datebuf1 + "','" + datebuf2 + "',tb_dataA.fwelder_no,tb_dataA.fjunction_no,tb_dataA.fweld_no,tb_dataA.fchannel,tb_dataA.fmax_electricity,tb_dataA.fmin_electricity,tb_dataA.fmax_voltage,tb_dataA.fmin_voltage,tb_dataA.fwelder_itemid,tb_dataA.fjunction_itemid,tb_dataA.fmachine_itemid,AVG(tb_dataA.fwirefeedrate),tb_dataA.fmachinemodel,tb_dataA.fwirediameter,tb_dataA.fmaterialgas,tb_dataA.fstatus FROM tb_dataA "
										+ "INNER JOIN tb_welded_junction ON tb_dataA.fjunction_id = tb_welded_junction.fwelded_junction_no "
										+ "WHERE (fstatus= '98' OR fstatus= '99')"
										+ " AND tb_dataA.FWeldTime BETWEEN '" + datebuf1 + "' AND '" + datebuf2 + "' "
										+ "GROUP BY tb_dataA.fwelder_id,tb_dataA.fgather_no,tb_dataA.fjunction_id,tb_dataA.fstatus,tb_dataA.fmachine_id,tb_dataA.fwirediameter";

								stmt.executeUpdate(sqlstandby);
								stmt.executeUpdate(sqlwork);
								stmt.executeUpdate(sqlwarn);
								stmt.executeUpdate(sqlalarm);
							}
							stmt.executeUpdate(sqlAtrun);
						}
						outlinestatus = "B";
					}else if(outlinestatus.equals("B")){
						ResultSet rs2 =stmt.executeQuery(sqlBfirst);
						while (rs2.next()) {
							Blasttime = rs2.getString("DATE_FORMAT(tb_dataB.FWeldTime,'%Y-%m-%d')") + " 23:59:59";
						}
						ResultSet rs4 =stmt.executeQuery(sqlBlast);
						while (rs4.next()) {
							Bfirsttime = rs4.getString("DATE_FORMAT(tb_dataB.FWeldTime,'%Y-%m-%d')") + " 06:00:00";
						}
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						if(null != Blasttime && !"null".equals(Blasttime) && null != Bfirsttime || !"null".equals(Bfirsttime)) {
							long Blast = sdf.parse(Blasttime).getTime()+1000;
							long Bfirst = sdf.parse(Bfirsttime).getTime();
							//统计四张状态表
							for(long i=Bfirst;i<Blast;i+=3600000){
								String datebuf1 = DateTools.format("yyyy-MM-dd HH:mm:ss", new Date(i));
								String datebuf2 = DateTools.format("yyyy-MM-dd HH:mm:ss", new Date(i+3600000));
								String sqlstandby = "INSERT INTO tb_standby(tb_standby.fwelder_id,tb_standby.fgather_no,tb_standby.fmachine_id,tb_standby.fjunction_id,"
										+ "tb_standby.fitemid,tb_standby.felectricity,tb_standby.fvoltage,tb_standby.frateofflow,tb_standby.fstandbytime,tb_standby.fstarttime,tb_standby.fendtime,tb_standby.fwelder_no,tb_standby.fjunction_no,tb_standby.fweld_no,tb_standby.fchannel,tb_standby.fmax_electricity,tb_standby.fmin_electricity,tb_standby.fmax_voltage,tb_standby.fmin_voltage,tb_standby.fwelder_itemid,tb_standby.fjunction_itemid,tb_standby.fmachine_itemid,tb_standby.fwirefeedrate,tb_standby.fmachinemodel,tb_standby.fwirediameter,tb_standby.fmaterialgas,tb_standby.fstatus) SELECT "
										+ "tb_dataB.fwelder_id,tb_dataB.fgather_no,tb_dataB.fmachine_id,tb_dataB.fjunction_id,tb_dataB.fitemid,"
										+ "AVG(tb_dataB.felectricity),AVG(tb_dataB.fvoltage),AVG(tb_dataB.frateofflow),COUNT(tb_dataB.fid),'" + datebuf1 + "','" + datebuf2 + "',tb_dataB.fwelder_no,tb_dataB.fjunction_no,tb_dataB.fweld_no,tb_dataB.fchannel,tb_dataB.fmax_electricity,tb_dataB.fmin_electricity,tb_dataB.fmax_voltage,tb_dataB.fmin_voltage,tb_dataB.fwelder_itemid,tb_dataB.fjunction_itemid,tb_dataB.fmachine_itemid,AVG(tb_dataB.fwirefeedrate),tb_dataB.fmachinemodel,tb_dataB.fwirediameter,tb_dataB.fmaterialgas,tb_dataB.fstatus FROM tb_dataB "
										+ "WHERE tb_dataB.fstatus = '0' AND tb_dataB.FWeldTime BETWEEN '" + datebuf1 + "' AND '" + datebuf2 + "' "
										+ "GROUP BY tb_dataB.fwelder_id,tb_dataB.fgather_no,tb_dataB.fjunction_id,tb_dataB.fstatus,tb_dataB.fmachine_id,tb_dataB.fwirediameter";

								String sqlwork = "INSERT INTO tb_work(tb_work.fwelder_id,tb_work.fgather_no,tb_work.fmachine_id,tb_work.fjunction_id,tb_work.fitemid,"
										+ "tb_work.felectricity,tb_work.fvoltage,tb_work.frateofflow,tb_work.fworktime,tb_work.fstarttime,tb_work.fendtime,tb_work.fwelder_no,tb_work.fjunction_no,tb_work.fweld_no,tb_work.fchannel,tb_work.fmax_electricity,tb_work.fmin_electricity,tb_work.fmax_voltage,tb_work.fmin_voltage,tb_work.fwelder_itemid,tb_work.fjunction_itemid,tb_work.fmachine_itemid,tb_work.fwirefeedrate,tb_work.fmachinemodel,tb_work.fwirediameter,tb_work.fmaterialgas,tb_work.fstatus) SELECT tb_dataB.fwelder_id,"
										+ "tb_dataB.fgather_no,tb_dataB.fmachine_id,tb_dataB.fjunction_id,tb_dataB.fitemid,AVG(tb_dataB.felectricity),"
										+ "AVG(tb_dataB.fvoltage),AVG(tb_dataB.frateofflow),COUNT(tb_dataB.fid),'" + datebuf1 + "','" + datebuf2 + "',tb_dataB.fwelder_no,tb_dataB.fjunction_no,tb_dataB.fweld_no,tb_dataB.fchannel,tb_dataB.fmax_electricity,tb_dataB.fmin_electricity,tb_dataB.fmax_voltage,tb_dataB.fmin_voltage,tb_dataB.fwelder_itemid,tb_dataB.fjunction_itemid,tb_dataB.fmachine_itemid,AVG(tb_dataB.fwirefeedrate),tb_dataB.fmachinemodel,tb_dataB.fwirediameter,tb_dataB.fmaterialgas,tb_dataB.fstatus FROM tb_dataB "
										+ "WHERE (tb_dataB.fstatus = '3' OR fstatus= '5' OR fstatus= '7' OR fstatus= '99') AND tb_dataB.FWeldTime BETWEEN '" + datebuf1 + "' AND '" + datebuf2 + "' "
										+ "GROUP BY tb_dataB.fwelder_id,tb_dataB.fgather_no,tb_dataB.fjunction_id,tb_dataB.fstatus,tb_dataB.fmachine_id,tb_dataB.fwirediameter";

								String sqlwarn = "INSERT INTO tb_warn(tb_warn.fwelder_id,tb_warn.fgather_no,tb_warn.fmachine_id,tb_warn.fjunction_id,"
										+ "tb_warn.fitemid,tb_warn.felectricity,tb_warn.fvoltage,tb_warn.frateofflow,tb_warn.fwarntime,tb_warn.fstarttime,tb_warn.fendtime,tb_warn.fwelder_no,tb_warn.fjunction_no,tb_warn.fweld_no,tb_warn.fchannel,tb_warn.fmax_electricity,tb_warn.fmin_electricity,tb_warn.fmax_voltage,tb_warn.fmin_voltage,tb_warn.fwelder_itemid,tb_warn.fjunction_itemid,tb_warn.fmachine_itemid,tb_warn.fwirefeedrate,tb_warn.fmachinemodel,tb_warn.fwirediameter,tb_warn.fmaterialgas,tb_warn.fstatus) SELECT "
										+ "tb_dataB.fwelder_id,tb_dataB.fgather_no,tb_dataB.fmachine_id,tb_dataB.fjunction_id,tb_dataB.fitemid,"
										+ "AVG(tb_dataB.felectricity),AVG(tb_dataB.fvoltage),AVG(tb_dataB.frateofflow),COUNT(tb_dataB.fid),'" + datebuf1 + "','" + datebuf2 + "',tb_dataB.fwelder_no,tb_dataB.fjunction_no,tb_dataB.fweld_no,tb_dataB.fchannel,tb_dataB.fmax_electricity,tb_dataB.fmin_electricity,tb_dataB.fmax_voltage,tb_dataB.fmin_voltage,tb_dataB.fwelder_itemid,tb_dataB.fjunction_itemid,tb_dataB.fmachine_itemid,AVG(tb_dataB.fwirefeedrate),tb_dataB.fmachinemodel,tb_dataB.fwirediameter,tb_dataB.fmaterialgas,tb_dataB.fstatus FROM tb_dataB "
										+ "WHERE tb_dataB.fstatus != '0' AND tb_dataB.fstatus != '3' AND tb_dataB.fstatus != '5' AND tb_dataB.fstatus != '7' AND tb_dataB.FWeldTime BETWEEN '" + datebuf1 + "' AND '" + datebuf2 + "' "
										+ "GROUP BY tb_dataB.fwelder_id,tb_dataB.fgather_no,tb_dataB.fjunction_id,tb_dataB.fstatus,tb_dataB.fmachine_id,tb_dataB.fwirediameter";

								String sqlalarm = "INSERT INTO tb_alarm(tb_alarm.fwelder_id,tb_alarm.fgather_no,tb_alarm.fmachine_id,tb_alarm.fjunction_id,tb_alarm.fitemid,"
										+ "tb_alarm.felectricity,tb_alarm.fvoltage,tb_alarm.frateofflow,tb_alarm.falarmtime,tb_alarm.fstarttime,tb_alarm.fendtime,tb_alarm.fwelder_no,tb_alarm.fjunction_no,tb_alarm.fweld_no,tb_alarm.fchannel,tb_alarm.fmax_electricity,tb_alarm.fmin_electricity,tb_alarm.fmax_voltage,tb_alarm.fmin_voltage,tb_alarm.fwelder_itemid,tb_alarm.fjunction_itemid,tb_alarm.fmachine_itemid,tb_alarm.fwirefeedrate,tb_alarm.fmachinemodel,tb_alarm.fwirediameter,tb_alarm.fmaterialgas,tb_alarm.fstatus) SELECT tb_dataB.fwelder_id,"
										+ "tb_dataB.fgather_no,tb_dataB.fmachine_id,tb_dataB.fjunction_id,tb_dataB.fitemid,AVG(tb_dataB.felectricity),"
										+ "AVG(tb_dataB.fvoltage),AVG(tb_dataB.frateofflow),COUNT(tb_dataB.fid),'" + datebuf1 + "','" + datebuf2 + "',tb_dataB.fwelder_no,tb_dataB.fjunction_no,tb_dataB.fweld_no,tb_dataB.fchannel,tb_dataB.fmax_electricity,tb_dataB.fmin_electricity,tb_dataB.fmax_voltage,tb_dataB.fmin_voltage,tb_dataB.fwelder_itemid,tb_dataB.fjunction_itemid,tb_dataB.fmachine_itemid,AVG(tb_dataB.fwirefeedrate),tb_dataB.fmachinemodel,tb_dataB.fwirediameter,tb_dataB.fmaterialgas,tb_dataB.fstatus FROM tb_dataB "
										+ "INNER JOIN tb_welded_junction ON tb_dataB.fjunction_id = tb_welded_junction.fwelded_junction_no "
										+ "WHERE (fstatus= '98' OR fstatus= '99')"
										+ " AND tb_dataB.FWeldTime BETWEEN '" + datebuf1 + "' AND '" + datebuf2 + "' "
										+ "GROUP BY tb_dataB.fwelder_id,tb_dataB.fgather_no,tb_dataB.fjunction_id,tb_dataB.fstatus,tb_dataB.fmachine_id,tb_dataB.fwirediameter";

								stmt.executeUpdate(sqlstandby);
								stmt.executeUpdate(sqlwork);
								stmt.executeUpdate(sqlwarn);
								stmt.executeUpdate(sqlalarm);
							}
							stmt.executeUpdate(sqlBtrun);
						}
						outlinestatus = "A";
					}
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
				
				/*if(outlinestatus.equals("A")){
				}else if(outlinestatus.equals("B")){
				}*/
				
			}  
		}, time1 , 1000*60*60*24);
		//}, 60000,600000);

		//获取最新焊口和焊机统计时间
		check = new DB_Connectioncode(stmt,conn,connet);
		NS.websocket.dbdata = this.dbdata;

		listarray1 = check.getId1();
		listarray2 = check.getId2();
		listarray3 = check.getId3();
		listarray4 = check.getId4();

		System.out.println(listarray1);
		System.out.println(listarray2);
		System.out.println(listarray3);
		System.out.println(listarray4);

		NS.mysql.listarray1 = this.listarray1;
		NS.mysql.listarray2 = this.listarray2;
		NS.mysql.listarray3 = this.listarray3;
		NS.websocket.listarray1 = this.listarray1;
		NS.websocket.listarray2 = this.listarray2;
		NS.websocket.listarray3 = this.listarray3;
		NS.android.listarray1 = this.listarray1;
		NS.android.listarray2 = this.listarray2;
		NS.listarray1 = this.listarray1;
		NS.listarray2 = this.listarray2;
		NS.listarray3 = this.listarray3;
		NS.listarray4 = this.listarray4;
		NStest.websocket.listarray1 = this.listarray1;
		NStest.websocket.listarray2 = this.listarray2;
		NStest.websocket.listarray3 = this.listarray3;
		NStest.listarray1 = this.listarray1;
		NStest.listarray2 = this.listarray2;
		NStest.listarray3 = this.listarray3;
		NStest.listarray4 = this.listarray4;

		//开启线程每分钟更新焊口数据
		Timer tExit2 = null; 
		tExit2 = new Timer();  
		tExit2.schedule(new TimerTask() {  
			@Override  
			public void run() {

				try{
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

					DB_Connectioncode check = new DB_Connectioncode(stmt,conn,connet);

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
					NS.listarray4 = listarray4;
					NStest.listarray1 = listarray1;
					NStest.listarray2 = listarray2;
					NStest.listarray3 = listarray3;
					NStest.listarray4 = listarray4;
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
			}  
		}, 0,60000);

		//工作线程
		new Thread(socketstart).start();
		new Thread(socketstarttest).start();
		new Thread(websocketstart).start();
		new Email().run();
		//new Thread(sockettran).start();
		//new EMessage().run();
		//new UpReport();

	}  

	//开启5551端口获取焊机数据(mysql)
	public Runnable socketstart = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			EventLoopGroup bossGroup = new NioEventLoopGroup(1); 
			EventLoopGroup workerGroup = new NioEventLoopGroup(128);
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
						chsoc.pipeline().addLast(
								new ReadTimeoutHandler(100),
								new WriteTimeoutHandler(100),
								NS);
						synchronized (socketlist) {
							socketcount++;
							socketlist.put(Integer.toString(socketcount),chsoc);
							NS.socketlist = socketlist;
						}
					}
				});

				//绑定端口，等待同步成功  
				ChannelFuture f;
				f = b.bind(5561).sync();
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

	//开启5552端口获取焊机数据(websocket)
	public Runnable socketstarttest = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			EventLoopGroup bossGroup = new NioEventLoopGroup(1); 
			EventLoopGroup workerGroup = new NioEventLoopGroup(128);
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
						chsoc.pipeline().addLast(
								new ReadTimeoutHandler(100),
								new WriteTimeoutHandler(100),
								NStest);
						synchronized (socketlist) {
							socketcount++;
							socketlist.put(Integer.toString(socketcount),chsoc);
							NStest.socketlist = socketlist;
							NWS.socketlist = socketlist;
						}
					}
				});

				//绑定端口，等待同步成功  
				ChannelFuture f;
				f = b.bind(5562).sync();
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
	
	//开启5550端口处理网页实时数据
	public Runnable websocketstart = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub

			EventLoopGroup bossGroup = new NioEventLoopGroup(1);
			EventLoopGroup workerGroup = new NioEventLoopGroup(128);

			try{
				ServerBootstrap serverBootstrap = new ServerBootstrap();
				serverBootstrap
				.group(bossGroup, workerGroup)
				.option(ChannelOption.SO_BACKLOG,1024)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>(){

					@Override
					protected void initChannel(SocketChannel chweb) throws Exception {
						// TODO Auto-generated method stub
						chweb.pipeline().addLast("httpServerCodec", new HttpServerCodec());
						chweb.pipeline().addLast("chunkedWriteHandler", new ChunkedWriteHandler());
						chweb.pipeline().addLast("httpObjectAggregator", new HttpObjectAggregator(1024*1024*1024));
						chweb.pipeline().addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler("ws://119.3.100.103:5550/SerialPortDemo/ws/张三",null,true,65535));
						chweb.pipeline().addLast("myWebSocketHandler", NWS);
						synchronized (websocketlist) {
							websocketcount++;
							websocketlist.put(Integer.toString(websocketcount),chweb);
							NS.websocketlist = websocketlist;
							NStest.websocketlist = websocketlist;
						}

						//System.out.println(chweb);
					}

				});

				Channel ch = serverBootstrap.bind(5563).sync().channel();
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

	//多层级转发
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

