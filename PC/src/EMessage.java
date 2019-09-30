import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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

public class EMessage {
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
		
		//发送短信
		Calendar calendar1 = Calendar.getInstance();
        //calendar1.add(Calendar.DAY_OF_MONTH, +1);    // 控制日
        calendar1.set(Calendar.HOUR_OF_DAY, 8); // 控制时
        calendar1.set(Calendar.MINUTE, 0);    // 控制分
        calendar1.set(Calendar.SECOND, 0);    // 控制秒
        Date time1 = calendar1.getTime(); 

        Timer tExit3 = new Timer();
        tExit3.schedule(new TimerTask(){
			@Override
			public void run() {
				// TODO Auto-generated method stub

				Calendar calendar1 = Calendar.getInstance();

				//获取时间一天查询
				Calendar calendar21 = Calendar.getInstance();
		        calendar21.add(Calendar.DAY_OF_MONTH, -1);    // 控制日
		        calendar21.set(Calendar.HOUR_OF_DAY, 6); // 控制时
		        calendar21.set(Calendar.MINUTE, 0);    // 控制分
		        calendar21.set(Calendar.SECOND, 0);    // 控制秒
		        Date time1 = calendar21.getTime(); 
		        String sqltime1 = DateTools.format("yyyy-MM-dd hh:mm:ss", time1);
		        Calendar calendar22 = Calendar.getInstance();
		        calendar22.add(Calendar.DAY_OF_MONTH, -1);    // 控制日
		        calendar22.set(Calendar.HOUR_OF_DAY, 23); // 控制时
		        calendar22.set(Calendar.MINUTE, 59);    // 控制分
		        calendar22.set(Calendar.SECOND, 59);    // 控制秒
		        Date time2 = calendar22.getTime(); 
		        String sqltime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time2);

				try{
					Date time3 = new Date();
					time3 = new Date(time3.getTime() - 3600*24*1000);

					//请求的webservice的url
					URL url = new URL("http://smssh1.253.com/msg/send/json");

					//创建http链接
					HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

					//设置请求的方法类型
					httpURLConnection.setRequestMethod("POST");

					//设置请求的内容类型
					httpURLConnection.setRequestProperty("Content-type", "application/json");

					//设置发送数据
					httpURLConnection.setDoOutput(true);

					//设置接受数据
					httpURLConnection.setDoInput(true);

					try {
						Class.forName("com.mysql.jdbc.Driver");
	                    conn = DriverManager.getConnection(connet);
	                    stmt= conn.createStatement();
					} catch (ClassNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			        String sql = "SELECT SUM(a)/8/3600,fgather_no FROM "
			        		+ "(SELECT SUM(tb_work.fworktime) a,tb_work.fgather_no  FROM tb_work WHERE (tb_work.fgather_no = '0001' OR tb_work.fgather_no = '0002') AND tb_work.fUploadDataTime BETWEEN '"+sqltime1+"' AND '"+sqltime2+"' GROUP BY tb_work.fgather_no "
			        		+ "UNION "
			        		+ "SELECT SUM(tb_standby.fstandbytime) a,tb_standby.fgather_no  FROM tb_standby WHERE (tb_standby.fgather_no = '0001' OR tb_standby.fgather_no = '0002')  AND tb_standby.fUploadDataTime BETWEEN '"+sqltime1+"' AND '"+sqltime2+"' GROUP BY tb_standby.fgather_no) b "
			        		+ "WHERE 1=1 GROUP BY fgather_no";
			        ResultSet rs = stmt.executeQuery(sql);
			        ArrayList<String> listarray5 = new ArrayList<String>();
			        while(rs.next()){
			        	listarray5.add(rs.getString("SUM(a)/8/3600"));
			        	listarray5.add(rs.getString("fgather_no"));
			        }

					String  un  =  "CN0753433";
		            String  pw  =  "WYLbBdG13w6714";
		            String  phone  =  "13122316882";
		            String  content  =  "腾焊";
		            String  postJsonTpl  =  "\"account\":\""+un+"\",\"password\":\""+pw+"\",\"phone\":\""+phone+"\",\"report\":\"false\",\"msg\":\""+content+"\"";
		            String  jsonBody  =  "{" + String.format(postJsonTpl,  un,  pw,  phone,  content) + "}";

					//发送数据,使用输出流
					OutputStream outputStream = httpURLConnection.getOutputStream();
					//发送的soap协议的数据
					//String requestXmlString = requestXml("北京");

					//String content1 = "user_id="+ URLEncoder.encode("123", "gbk");

					//发送数据
					outputStream.write(jsonBody.getBytes());

					//接收数据
					InputStream inputStream = httpURLConnection.getInputStream();

					//定义字节数组
					byte[] b = new byte[1024];

					//定义一个输出流存储接收到的数据
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

					//开始接收数据
					int len = 0;
					while (true) {
						len = inputStream.read(b);
						if (len == -1) {
							//数据读完
							break;
						}
						byteArrayOutputStream.write(b, 0, len);
					}
					//从输出流中获取读取到数据(服务端返回的)
					String response = byteArrayOutputStream.toString();

					System.out.println(response);

				}catch(Exception e){
					e.printStackTrace();
				}
			}

        }, time1 , 1000*60*60*24);
	}
}
