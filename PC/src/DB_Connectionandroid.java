



import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class DB_Connectionandroid {
	
	public String inSql;
	public String select;
	public  String datasend="";
	public String fmachine;
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
	
	public String getId() {
		return datasend;
	}
	public void setId(String datasend) {
		this.datasend = datasend;
	}

	public Server server;
	
	public java.sql.Connection conn = null;
	public java.sql.Statement stmt =null;


	public DB_Connectionandroid(java.sql.Statement stmt){
		this.stmt = stmt;
		/*try {

			Class.forName("com.mysql.jdbc.Driver");  
            conn = DriverManager.getConnection(connet);
            stmt= conn.createStatement();

       } catch (SQLException e) {

           System.out.println("Broken conn");
           e.printStackTrace();

       } catch (ClassNotFoundException e) {  

           System.out.println("Broken driver");
           e.printStackTrace();  

       } */
	}

    public void DB_Connectionandroidrun(BigDecimal electricity,BigDecimal voltage,BigInteger sensor_Num,String machine_id,String welder_id,String code,BigInteger year,BigInteger month,BigInteger day, BigInteger hour,BigInteger minute,BigInteger second,int status){


    	
    	Date date = new Date();

        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

        Timestamp goodsC_date = Timestamp.valueOf(nowTime);

        inSql = "insert into tb_data(electricity,voltage,sensor_Num,machine_id,welder_id,code,year,month,day,hour,minute,second,status,Dtime) values('"+ electricity +"','" + voltage + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + year + "','" + month + "','" + day + "','" + hour + "','" + minute + "','" + second + "','" + status + "','" + goodsC_date + "')";
          
    }
    
    /*protected void finalize( ){
		try {
			conn.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
    
    
}