import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AlertEmail {

	private String ip;
	private String ip1;
	public String connet1 = "jdbc:mysql://";
	public String connet2 = ":3306/"; 
	public String connet3 = "?user="; 
	public String connet4 = "&password=";
	public String connet5 = "&useUnicode=true&autoReconnect=true&characterEncoding=UTF8";
	private String connet;
	public java.sql.Connection conn = null;
	public java.sql.Statement stmt =null;
	private static int state = 1;

	public void Alert(String str) {
		String msg0 = new BigInteger(str.substring(84,86),16).toString(10);
		String equipment_no = Integer.toString(Integer.parseInt(str.substring(20,24)));
		String index = "";
		switch(msg0)
		{case "10":index ="缺相保护";break;
		case "11":index ="无输入网电";break;
		case "12":index ="过压保护";break;
		case "13":index ="欠压保护";break;
		case "14":index ="网电频率超出限值（46Hz—64Hz）";break;
		case "20":index ="过流保护（IGBT直通）";break;
		case "21":index ="逆变器工作，但无输出电压";break;
		case "22":index ="开机自检保护（IGBT部分）";break;
		case "26":index ="霍尔采样失效";break;
		case "27":index ="开机自检";break;
		case "31":index ="温度传感器开路";break;
		case "32":index ="温度传感器短路";break;
		case "33":index ="水循环保护";break;
		case "40":index ="速度反馈丢失";break;
		case "41":index ="送丝机过流/过功率保护";break;
		case "50":index ="数据寄存器数据误码";break;
		case "51":index ="数据寄存器无法进行读写";break;
		case "52":index ="I2C通讯失效";break;
		case "53":index ="485通讯失效";break;
		case "55":index ="打卡通讯报错";break;
		case "70":index ="超暂载率使用保护";break;
		case "90":index ="DC-BUS无电压";break;
		case "92":index ="DC-BUS检测线开路";break;}

		if(!msg0.equals("0")&&!msg0.equals("3")&&!msg0.equals("5")&&!msg0.equals("7"))
		{
			state++;
			if(state == 3)
			{
				try
				{
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

					//发邮件
					stmt = conn.createStatement();

					ArrayList<String> listarraymail = new ArrayList<String>();
					ArrayList<String> listarraymailer = new ArrayList<String>();

					//String sql = "SELECT ftest,fequipment_no FROM tb_welding_machine WHERE DateDiff(DATE(now()),DATE(ftest)) = 30";							
					String sql = "SELECT fequipment_no FROM tb_welding_machine WHERE fid ="+ equipment_no;							
					String sqlmail = "SELECT fid,femailname,femailaddress,femailtype FROM tb_catemailinf";	            
					String now = "SELECT Now() sendtime";

					ResultSet rs;
					rs = stmt.executeQuery(sqlmail);	        
					// 展开结果集数据库
					while(rs.next()){
						// 通过字段检索邮件地址
						listarraymailer.add(rs.getString("femailname"));
						listarraymailer.add(rs.getString("femailtype"));
						listarraymailer.add(rs.getString("femailaddress"));
					}

					rs = stmt.executeQuery(sql);
					while(rs.next()){
						listarraymail.add(rs.getString("fequipment_no"));
					}

					for(int i=0;i<listarraymailer.size();i+=3) 
					{
						if(listarraymail.size()!=0 && listarraymailer.get(i+1).equals("2"))//发送报警邮件
						{
							Properties props = new Properties(); props.setProperty("mail.smtp.auth","true"); 
							props.setProperty("mail.transport.protocol", "smtp");
							props.put("mail.smtp.host","smtp.qq.com");				              
							Session session = Session.getInstance(props); session.setDebug(true);				              
							Message msg = new MimeMessage(session); 
							msg.setSubject("设备报警提醒");				              

							String message = "";
							for(int j=0;j<listarraymail.size();j++)
							{
								if(j==listarraymail.size()-1)
								{message += listarraymail.get(j);  }
								else
								{message += listarraymail.get(j)+"、";  }

							}
							String Text ="设备"+message+index;										
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
							// 展开结果集数据库					         
							String sqlsave = "INSERT INTO tb_catemailcheck (femailname,femailaddress,femailtext,femailstatus,femailtime) VALUES('"+listarraymailer.get(i)+"'"+","+"'"+listarraymailer.get(i+2)+"'"+","+"'"+Text+"'"+","+"'"+listarraymailer.get(i+1)+"'"+","+"'"+sendtime+"'"+")";
							stmt.executeUpdate(sqlsave);  
						}
					}
					// 完成后关闭,换位置了
					rs.close();
					stmt.close();
					conn.close();
				}
				catch(Exception e) {
					e.getStackTrace();
				}
			}
		}					
		else 
		{
			state = 1;
		}
	}
}
