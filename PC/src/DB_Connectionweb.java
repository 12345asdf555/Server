



import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Date;

public class DB_Connectionweb {
	public String inSql;
	public  String datasend="";
	
	public String getId() {
		return datasend;
	}
	public void setId(String datasend) {
		this.datasend = datasend;
	}
    public Server server;

	/*String Connection="jdbc:mysql://121.196.222.216:3306/Weld?"+

            "user=root&password=123456&characterEncoding=UTF8";

    String uri = "jdbc:mysql://121.196.222.216:3306/Weld?";

    String user = "user=root&password=123456&characterEncoding=UTF8";



     String connet = "jdbc:mysql://121.196.222.216:3306/Weld?"

             + "user=root&password=123456&useUnicode=true&characterEncoding=UTF8";

    String inSql = null;*/

    public DB_Connectionweb(String connet)

    {
    	
        java.sql.Connection conn = null;

        java.sql.Statement stmt =null;


        try {  

            Class.forName("com.mysql.jdbc.Driver");  

        } catch (ClassNotFoundException e) {  

            System.out.println("Broken driver");

            e.printStackTrace();  

        }  

         try {

             conn = DriverManager.getConnection(connet);

             stmt= conn.createStatement();



        } catch (SQLException e) {

            System.out.println("Broken conn");

            e.printStackTrace();

        }



         Date date = new Date();

         String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

         Timestamp goodsC_date = Timestamp.valueOf(nowTime);



        // if(state ==1)

             inSql = "select fequipment_no, fstatus_id, fposition, finsframework_id from tb_welding_machine";

       /*  else {

             inSql = "insert into sj(id,bed_Number,state,Dtime) values('"+ id +"','" + bedNumber + "','leave','" + goodsC_date + "')";

        }*/

         try {

        	ResultSet rs =stmt.executeQuery(inSql);
            
            while (rs.next()) {
            	String fequipment_no = rs.getString("fequipment_no");
            	String fstatus_id = rs.getString("fstatus_id");
            	String fposition = rs.getString("fposition");
            	String finsframework_id = rs.getString("finsframework_id");
            	datasend+=fstatus_id+finsframework_id+fequipment_no+fposition;
            	
            }

        } catch (SQLException e) {

            System.out.println("Broken insert");

            e.printStackTrace();

        } 



        try {

            stmt.close();

             conn.close(); 

        } catch (SQLException e) {

            System.out.println("Broken close");

            e.printStackTrace();

        }  

         return; 



    }

    
}