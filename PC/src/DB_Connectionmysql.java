



import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class DB_Connectionmysql {
	
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


	public DB_Connectionmysql(java.sql.Statement stmt){
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

    public void DB_Connectionmysqlrun(BigDecimal electricity,BigDecimal voltage,String sensor_Num,String machine_id,String welder_id,String code,int status,String fitemid,Timestamp timesql,ArrayList<String> listarray2){


    	
    	Date date = new Date();

        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

        Timestamp goodsC_date = Timestamp.valueOf(nowTime);

        for(int i=0;i<listarray2.size();i+=4){
       	 if(machine_id.equals(listarray2.get(i+2))){
       		 fmachine = listarray2.get(i);
       		 break;
       	 }
        }

        if(fmachine != null){
       	 
       	 	BigDecimal voltage1 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
	         
            inSql = "insert into tb_live_data(felectricity,fvoltage,frateofflow,fgather_no,fwelder_id,fjunction_id,fstatus,fitemid,FUploadDateTime,FWeldTime,fmachine_id) values('"+ electricity +"','" + voltage1 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
       	 
            try {
            	//System.out.println(fmachine);
                stmt.executeUpdate(inSql);
                //System.out.println("1");

            } catch (SQLException e) {

            	System.out.println(machine_id);
            	
                System.out.println("Broken insert");

                e.printStackTrace();

            } 
            
        }
 
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