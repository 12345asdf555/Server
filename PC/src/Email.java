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
		
		//开启线程每天查询邮件
        Calendar calendarmail = Calendar.getInstance();
        
        calendarmail.set(Calendar.DAY_OF_MONTH, +1); // 控制天
        calendarmail.set(Calendar.HOUR_OF_DAY, 07); // 控制时
        calendarmail.set(Calendar.MINUTE, 00);    // 控制分
        calendarmail.set(Calendar.SECOND, 00);    // 控制秒
        time = calendarmail.getTime(); 
        
	    Timer tExit3 = null; 
		tExit3 = new Timer();  
        tExit3.schedule(new TimerTask() {

			private String ip;
			private String ip1;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//获取焊工以及管理员信息
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
	    		String sqlmail = "SELECT fname,fcheckintime,ficworkime FROM tb_welder";
	    		String sqlmailer = "SELECT femailname,femailaddress,femailtype FROM tb_catemailinf";
	    		ResultSet rs;
	    		try {
	    			rs = stmt.executeQuery(sqlmail);
	            	while (rs.next()) {
	            		listarraymail.add(rs.getString("fname"));
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
	    				
	    				String sqlmailcheck2 = "update tb_welder set fhalfyearsure = '" + DateTools.format("yyyy-MM-dd HH:mm:ss",new Date()) + "' WHERE fname = '" + listarraymail.get(i) + "'";
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
					Date dateic;
					try {
						dateic = DateTools.parse("yyyy-MM-dd HH:mm:ss",listarraymail.get(i+2));
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
				
				//长时间未工作提醒
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		        Calendar c = Calendar.getInstance();
		        c.setTime(new Date());
		        Date cd = c.getTime();
		        String cday = format.format(cd);
		        c.add(Calendar.DATE, - 5);
		        Date d = c.getTime();
		        String five_day = format.format(d);
				String fwelder = "SELECT fwelder_no,fname FROM tb_welder WHERE fid NOT IN (SELECT DISTINCT fwelder_id FROM tb_live_data WHERE FWeldTime>'"+five_day+"')";
//				ArrayList<String> welder_list = new ArrayList<String>();
				ResultSet fwelder_rs;
				String fwelder_str="";
				try {
					fwelder_rs = stmt.executeQuery(fwelder);
		        	while (fwelder_rs.next()) {
		/*	            		welder_list.add(fwelder_rs.getString("fwelder_no"));
		        		welder_list.add(fwelder_rs.getString("fname"));*/
		        		fwelder_str=fwelder_str+fwelder_rs.getString("fname")+",";
		        	}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(!fwelder_str.equals("")){
					try{
						for(int j=0;j<listarraymailer.size();j+=3){
							if(listarraymailer.get(j+2).equals("3")){
								Properties props = new Properties();
							    props.setProperty("mail.smtp.auth", "true");
							    props.setProperty("mail.transport.protocol", "smtp");
							    props.put("mail.smtp.host","smtp.qq.com");// smtp服务器地址
							    
							    Session session = Session.getInstance(props);
							    session.setDebug(true);
							    
							    Message msg = new MimeMessage(session);
							    msg.setSubject("员工长时间未工作提醒");
							    msg.setText(fwelder_str.substring(0, fwelder_str.length()-1) + " 超过5天未工作");
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
							    String sqlmailcheck = "INSERT INTO tb_catemailcheck (femailname, femailaddress, femailtext, femailstatus, femailtime) VALUES ('" + listarraymailer.get(j) + "' , '" + listarraymailer.get(j+1) + "' , '" + fwelder_str.substring(0, fwelder_str.length()-1) + " 超过5天未工作" + "' , '4' , '" + nowtime + "')";
							    stmt.execute(sqlmailcheck);
							}
						}
						
				    }catch(Exception e){
				    	e.getStackTrace();
				    }
				}
			
				//焊机校验
				String catmachine = "SELECT fid,fequipment_no,DATE_FORMAT(ftest,'%Y-%m-%d') testtime,DATE_FORMAT(fprevention,'%Y-%m-%d') pretime FROM tb_welding_machine WHERE 1=1";
				ArrayList<String> test_machineid_list = new ArrayList<String>();
				ArrayList<String> pre_machineid_list = new ArrayList<String>();
				ResultSet machine_rs;
				String test_machine_str="";
				String test_machine_id="";
				String pre_machine_str="";
				String pre_machine_id="";
				try {
					machine_rs = stmt.executeQuery(catmachine);
		        	while (machine_rs.next()) {
		        		if(machine_rs.getString("testtime").equals(cday)){
		        			test_machine_str = test_machine_str + machine_rs.getString("fequipment_no") + ",";
		        			test_machineid_list.add(machine_rs.getString("fid"));
		        		}
		        		if(machine_rs.getString("pretime").equals(cday)){
		        			pre_machine_str = pre_machine_str + machine_rs.getString("fequipment_no") + ",";
		        			pre_machineid_list.add(machine_rs.getString("fid"));
		        		}
		        	}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				if(!test_machine_str.equals("")){
					try{
						for(int j=0;j<listarraymailer.size();j+=3){
							if(listarraymailer.get(j+2).equals("4")){
								Properties props = new Properties();
							    props.setProperty("mail.smtp.auth", "true");
							    props.setProperty("mail.transport.protocol", "smtp");
							    props.put("mail.smtp.host","smtp.qq.com");// smtp服务器地址
							    
							    Session session = Session.getInstance(props);
							    session.setDebug(true);
							    
							    Message msg = new MimeMessage(session);
							    msg.setSubject("焊机校验提醒");
							    msg.setText(test_machine_str.substring(0, test_machine_str.length()-1) + " 焊机需要校验");
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
							    
							    test_machine_id = String.join(" or fid=", test_machineid_list);
							    c.setTime(new Date());
							    c.add(Calendar.MONTH, +4);
						        Date md = c.getTime();
						        String test_day = format.format(md);
						        String nowtime = DateTools.format("yyyy-MM-dd HH:mm:ss",new Date());
							    String sqlmailcheck = "UPDATE tb_welding_machine SET ftest='" + test_day + "' WHERE fid="+test_machine_id ;
							    stmt.execute(sqlmailcheck);
							    sqlmailcheck = "INSERT INTO tb_catemailcheck (femailname, femailaddress, femailtext, femailstatus, femailtime) VALUES ('" + listarraymailer.get(j) + "' , '" + listarraymailer.get(j+1) + "' , '" + test_machine_str.substring(0, test_machine_str.length()-1) + " 焊机需要校验" + "' , '5' , '" + nowtime + "')";
							    stmt.execute(sqlmailcheck);
							}
						}
						
				    }catch(Exception e){
				    	e.getStackTrace();
				    }
				}
				
				//焊机保养
				if(!pre_machine_str.equals("")){
					try{
						for(int j=0;j<listarraymailer.size();j+=3){
							if(listarraymailer.get(j+2).equals("5")){
								Properties props = new Properties();
							    props.setProperty("mail.smtp.auth", "true");
							    props.setProperty("mail.transport.protocol", "smtp");
							    props.put("mail.smtp.host","smtp.qq.com");// smtp服务器地址
							    
							    Session session = Session.getInstance(props);
							    session.setDebug(true);
							    
							    Message msg = new MimeMessage(session);
							    msg.setSubject("焊机保养提醒");
							    msg.setText(pre_machine_str.substring(0, pre_machine_str.length()-1) + " 焊机需要保养");
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
							    
							    pre_machine_id = String.join(" or fid=", pre_machineid_list);
							    c.setTime(new Date());
							    c.add(Calendar.MONTH, +6);
						        Date pd = c.getTime();
						        String pre_day = format.format(pd);
						        String nowtime = DateTools.format("yyyy-MM-dd HH:mm:ss",new Date());
							    String sqlmailcheck = "UPDATE tb_welding_machine SET fprevention='" + pre_day + "' WHERE fid="+pre_machine_id ;
							    stmt.execute(sqlmailcheck);
							    sqlmailcheck = "INSERT INTO tb_catemailcheck (femailname, femailaddress, femailtext, femailstatus, femailtime) VALUES ('" + listarraymailer.get(j) + "' , '" + listarraymailer.get(j+1) + "' , '" + pre_machine_str.substring(0, pre_machine_str.length()-1) + " 焊机需要保养" + "' , '6' , '" + nowtime + "')";
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
}

//旧版
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
		//开启线程每天查询邮件
        Calendar calendarmail = Calendar.getInstance();
        
        calendarmail.set(Calendar.HOUR_OF_DAY, 17); // 控制时
        calendarmail.set(Calendar.MINUTE, 50);    // 控制分
        calendarmail.set(Calendar.SECOND, 00);    // 控制秒
        time = calendarmail.getTime(); 
        
	    Timer tExit3 = null; 
		tExit3 = new Timer();  
        tExit3.schedule(new TimerTask() {

			private String ip;
			private String ip1;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//获取焊工以及管理员信息
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
					
					Date dateic;
					try {
						dateic = DateTools.parse("yyyy-MM-dd HH:mm:ss",listarraymail.get(i+2));
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
        	
				
        }, time,86400000);
	}
}*/
