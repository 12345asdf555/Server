import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.ParseException;

public class Email {
	private Date time;
	public String connet1 = "jdbc:mysql://";
    public String connet2 = ":3306/"; 
    public String connet3 = "?user="; 
    public String connet4 = "&password=";
    public String connet5 = "&useUnicode=true&autoReconnect=true&characterEncoding=UTF8";
	private String connet;
	public java.sql.Connection conn = null;
    public java.sql.Statement stmt =null;

	public void run(){
		
		//寮�鍚嚎绋嬫瘡澶╂煡璇㈤偖浠�
        Calendar calendarmail = Calendar.getInstance();
        
        calendarmail.set(Calendar.HOUR_OF_DAY, 00); // 鎺у埗鏃�
        calendarmail.set(Calendar.MINUTE, 00);    // 鎺у埗鍒�
        calendarmail.set(Calendar.SECOND, 00);    // 鎺у埗绉�
        time = calendarmail.getTime(); 
        
	    Timer tExit3 = null; 
		tExit3 = new Timer();  
        tExit3.schedule(new TimerTask() {

			private String ip;
			private String ip1;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//鑾峰彇鐒婂伐浠ュ強绠＄悊鍛樹俊鎭�
				try {
					FileInputStream in;
					in = new FileInputStream("IPconfig.txt");
				 
		            InputStreamReader inReader;
					inReader = new InputStreamReader(in, "UTF-8");
		            BufferedReader bufReader = new BufferedReader(inReader);  
		            String line = null; 
		            int writetime=0;
		    		
		    	    try {
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
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
		    		
		    	    String[] values = ip.split(",");
		    		
		    		connet=connet1+values[0]+connet2+values[1]+connet3+values[2]+connet4+values[3]+connet5;
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
	    	    
	    		try {
					Class.forName("com.mysql.jdbc.Driver");
	    			conn = DriverManager.getConnection(connet);
					stmt= conn.createStatement();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	    		try {
	    			ArrayList<String> listarraymail = new ArrayList<String>();
					ArrayList<String> listarraymailer = new ArrayList<String>();


					String sql = "SELECT ftest,fequipment_no FROM tb_welding_machine WHERE DateDiff(DATE(now()),DATE(ftest)) = 30";
					String sqlmail = "SELECT fid,femailname,femailaddress,femailtype FROM tb_catemailinf";
					String sqltime = "UPDATE tb_welding_machine SET ftest = now() WHERE fequipment_no = " ;		            
					String now = "SELECT Now() sendtime";

					ResultSet rs;
					rs = stmt.executeQuery(sqlmail);	        
					// 展开结果集数据库
					while(rs.next()){
						// 通过字段检索
						listarraymailer.add(rs.getString("femailname"));
						listarraymailer.add(rs.getString("femailtype"));
						listarraymailer.add(rs.getString("femailaddress"));
					}


					rs = stmt.executeQuery(sql);	        
					while(rs.next()){
						listarraymail.add(rs.getString("fequipment_no"));
					}
					for(int k=0;k<listarraymail.size();k++)
					{
						stmt.executeUpdate(sqltime+"'"+listarraymail.get(k)+"'");
					}	            	

					for(int i=0;i<listarraymailer.size();i+=3) 
					{

						if(listarraymail.size()!=0 && listarraymailer.get(i+1).equals("5"))//发送保养邮件
						{
							Properties props = new Properties(); 
							props.setProperty("mail.smtp.auth","true"); 
							props.setProperty("mail.transport.protocol", "smtp");
							props.put("mail.smtp.host","smtp.qq.com");				              
							Session session = Session.getInstance(props); 
							session.setDebug(true);				              
							Message msg = new MimeMessage(session); 
							msg.setSubject("设备保养提醒");	            
							String message = "";
							for(int j=0;j<listarraymail.size();j++)
							{
								if(j==listarraymail.size()-1)
								{message += listarraymail.get(j);  }
								else
								{message += listarraymail.get(j)+"、";  }

							}

							String Text ="设备"+message+"入场超过一个月，需要进行检修，检修项目如下："+"\n"+"1.定期除尘"+"\n"+"2.除尘后，检查机内接插线是否牢固可靠"+"\n"+"3.机壳可靠性接地"+"\n"+"4.电源三相网电是否正常"+"\n"+"5.定期清理送丝轮下焊丝渣";	

							msg.setText(Text); 


							msg.setSentDate(new Date()); 
							msg.setFrom(new InternetAddress("614895019@qq.com"));

							msg.setRecipient(Message.RecipientType.TO, new
									InternetAddress(listarraymailer.get(i+2))); 

							Transport transport = session.getTransport();
							transport.connect("614895019@qq.com","tmqbcxjismumbbca");

							transport.sendMessage(msg, msg.getAllRecipients()); 
							transport.close();
							String sendtime = "";
							rs = stmt.executeQuery(now);  
							while(rs.next())
							{
								sendtime = rs.getString("sendtime");
							}		         
							String sqlsave = "INSERT INTO tb_catemailcheck (femailname,femailaddress,femailtext,femailstatus,femailtime) VALUES('"+listarraymailer.get(i)+"'"+","+"'"+listarraymailer.get(i+2)+"'"+","+"'"+Text+"'"+","+"'"+listarraymailer.get(i+1)+"'"+","+"'"+sendtime+"'"+")";
							stmt.executeUpdate(sqlsave); 

						}

					}
					rs.close();
					stmt.close();
					conn.close();
	    		}catch(Exception e) {
	    			e.getStackTrace();
	    		}
	    		
			}
        }, time, 1000 * 60 * 60 * 24);
	}
}

//鏃х増
/*public class Email {
	private Date time;
	public String connet1 = "jdbc:mysql://";
    public String connet2 = ":3306/"; 
    public String connet3 = "?user="; 
    public String connet4 = "&password=";
    public String connet5 = "&useUnicode=true&autoReconnect=true&characterEncoding=UTF8";
	private String connet;
	public java.sql.Connection conn = null;
    public java.sql.Statement stmt =null;

	public void run(){
		//寮�鍚嚎绋嬫瘡澶╂煡璇㈤偖浠�
        Calendar calendarmail = Calendar.getInstance();
        
        calendarmail.set(Calendar.HOUR_OF_DAY, 17); // 鎺у埗鏃�
        calendarmail.set(Calendar.MINUTE, 50);    // 鎺у埗鍒�
        calendarmail.set(Calendar.SECOND, 00);    // 鎺у埗绉�
        time = calendarmail.getTime(); 
        
	    Timer tExit3 = null; 
		tExit3 = new Timer();  
        tExit3.schedule(new TimerTask() {

			private String ip;
			private String ip1;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//鑾峰彇鐒婂伐浠ュ強绠＄悊鍛樹俊鎭�
				try {
					FileInputStream in;
					in = new FileInputStream("IPconfig.txt");
				 
		            InputStreamReader inReader;
					inReader = new InputStreamReader(in, "UTF-8");
		            BufferedReader bufReader = new BufferedReader(inReader);  
		            String line = null; 
		            int writetime=0;
		    		
		    	    try {
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
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
		    		
		    	    String[] values = ip.split(",");
		    		
		    		connet=connet1+values[0]+connet2+values[1]+connet3+values[2]+connet4+values[3]+connet5;
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
	    	    
	    		try {
					Class.forName("com.mysql.jdbc.Driver");
	    			conn = DriverManager.getConnection(connet);
					stmt= conn.createStatement();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
					
					//鍗婂勾鎻愰啋
					Calendar canow = Calendar.getInstance();
					Calendar ca = Calendar.getInstance();
					ca.setTime(new Date());
					ca.add(Calendar.MONTH, -5);
					ca.add(Calendar.DAY_OF_MONTH, -15);
					Date resultDate = ca.getTime(); // 缁撴灉  
					String nowtime = DateTools.format("yyyy-MM-dd HH:mm:ss",resultDate);
					
					String[] nowtimebuf = nowtime.split(" ");
					String[] checkintimebuf = listarraymail.get(i+1).split(" ");
					
					nowtime = nowtimebuf[0];
					String checkintime = checkintimebuf[0];
							
					if(nowtime.equals(checkintime)){
						if(halfyearname.equals("")){
							halfyearname = listarraymail.get(i);
						}else{
							halfyearname = listarraymail.get(i) + "銆�" + halfyearname ;
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
							    props.put("mail.smtp.host","smtp.qq.com");// smtp鏈嶅姟鍣ㄥ湴鍧�
							    
							    Session session = Session.getInstance(props);
							    session.setDebug(true);
							    
							    Message msg = new MimeMessage(session);
							    msg.setSubject("鍛樺伐鍏ヨ亴鍗婂勾鎻愰啋");
							    msg.setText(halfyearname + " 鍏ヨ亴宸叉弧鍗婂勾");
							    msg.setSentDate(new Date());
							    msg.setFrom(new InternetAddress("512836904@qq.com"));//鍙戜欢浜洪偖绠�
							    msg.setRecipient(Message.RecipientType.TO,
							            new InternetAddress(listarraymailer.get(j+1))); //鏀朵欢浜洪偖绠�
							    //msg.addRecipient(Message.RecipientType.CC, 
					    		//new InternetAddress("XXXXXXXXXXX@qq.com")); //鎶勯�佷汉閭
							    msg.saveChanges();

							    Transport transport = session.getTransport();
							    transport.connect("512836904@qq.com","sbmqftbsitpecaef");//鍙戜欢浜洪偖绠�,鎺堟潈鐮�
							    
							    transport.sendMessage(msg, msg.getAllRecipients());
							    transport.close();
							    
							    String nowtime = DateTools.format("yyyy-MM-dd HH:mm:ss",new Date());
							    String sqlmailcheck1 = "INSERT INTO tb_catemailcheck (femailname, femailaddress, femailtext, femailstatus, femailtime) VALUES ('" + listarraymailer.get(j) + "' , '" + listarraymailer.get(j+1) + "' , '" + halfyearname + " 鍏ヨ亴宸叉弧鍗婂勾" + "' , '1' , '" + nowtime + "')";
							    stmt.execute(sqlmailcheck1);
							}
						}
						
				    }catch(Exception e){
				    	e.getStackTrace();
				    }
				}
					
				//ic鍗℃湁鏁堟湡鎻愰啋
				String icworktime = "";
					
				for(int i=0;i<listarraymail.size();i+=3){
					
					//ic鍗℃湁鏁堟湡鎻愰啋
					
					Date dateic;
					try {
						dateic = DateTools.parse("yyyy-MM-dd HH:mm:ss",listarraymail.get(i+2));
						Calendar canow = Calendar.getInstance();
						Calendar ca = Calendar.getInstance();
						ca.setTime(dateic);
						ca.add(Calendar.DAY_OF_MONTH, -60);
						Date resultDate = ca.getTime(); // 缁撴灉  
						String ictime = DateTools.format("yyyy-MM-dd HH:mm:ss",resultDate);
						
						String[] timebuf = ictime.split(" ");
						String[] checkictimebuf = DateTools.format("yyyy-MM-dd HH:mm:ss",canow.getTime()).split(" ");
						
						ictime = timebuf[0];
						String checkictime = checkictimebuf[0];
								
						if(ictime.equals(checkictime)){
							if(icworktime.equals("")){
								icworktime = listarraymail.get(i);
							}else{
								icworktime = listarraymail.get(i) + "銆�" + icworktime ;
							}
						}
					} catch (java.text.ParseException e) {
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
							    props.put("mail.smtp.host","smtp.qq.com");// smtp鏈嶅姟鍣ㄥ湴鍧�
							    
							    Session session = Session.getInstance(props);
							    session.setDebug(true);
							    
							    Message msg = new MimeMessage(session);
							    msg.setSubject("鍛樺伐ic鍗″埌鏈熸彁閱�");
							    msg.setText(icworktime + " ic鍗″皢瑕佽繃鏈�");
							    msg.setSentDate(new Date());
							    msg.setFrom(new InternetAddress("512836904@qq.com"));//鍙戜欢浜洪偖绠�
							    msg.setRecipient(Message.RecipientType.TO,
							            new InternetAddress(listarraymailer.get(j+1))); //鏀朵欢浜洪偖绠�
							    //msg.addRecipient(Message.RecipientType.CC, 
					    		//new InternetAddress("XXXXXXXXXXX@qq.com")); //鎶勯�佷汉閭
							    msg.saveChanges();

							    Transport transport = session.getTransport();
							    transport.connect("512836904@qq.com","sbmqftbsitpecaef");//鍙戜欢浜洪偖绠�,鎺堟潈鐮�
							    
							    transport.sendMessage(msg, msg.getAllRecipients());
							    transport.close();
							    
							    String nowtime = DateTools.format("yyyy-MM-dd HH:mm:ss",new Date());
							    String sqlmailcheck = "INSERT INTO tb_catemailcheck (femailname, femailaddress, femailtext, femailstatus, femailtime) VALUES ('" + listarraymailer.get(j) + "' , '" + listarraymailer.get(j+1) + "' , '" + icworktime + " ic鍗″皢瑕佽繃鏈�" + "' , '2' , '" + nowtime + "')";
							    stmt.execute(sqlmailcheck);
							}
						}
						
				    }catch(Exception e){
				    	e.getStackTrace();
				    }
				
				}
				
			}  
        	
				
        }, time,86400000);
	}
}*/
