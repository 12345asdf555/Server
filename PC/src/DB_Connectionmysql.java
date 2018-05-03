



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


	public DB_Connectionmysql(String connet){
		try {

			Class.forName("com.mysql.jdbc.Driver");  
            conn = DriverManager.getConnection(connet);
            stmt= conn.createStatement();

       } catch (SQLException e) {

           System.out.println("Broken conn");
           e.printStackTrace();

       } catch (ClassNotFoundException e) {  

           System.out.println("Broken driver");
           e.printStackTrace();  

       } 
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
       	 
       	 	//BigDecimal voltage1 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
	         
            inSql = "insert into tb_live_data(felectricity,fvoltage,frateofflow,fgather_no,fwelder_id,fjunction_id,fstatus,fitemid,FUploadDateTime,FWeldTime,fmachine_id) values('"+ electricity +"','" + voltage + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
       	 
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
    	
    	
        /*java.sql.Connection conn = null;

        java.sql.Statement stmt


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

        }*/
    	
         //查焊机和采集设备号
         /*select = "select tb_welding_machine.fid,tb_gather.fgather_no from tb_gather left join tb_welding_machine on tb_gather.fid=tb_welding_machine.fgather_id where tb_gather.fgather_no";
         
         try {

          	ResultSet rs =stmt.executeQuery(select);
              
              while (rs.next()) {
            	  String fid = rs.getString("fid");
            	  String fgather_no = rs.getString("fgather_no");
            	  listarray.add(fgather_no);
              	  listarray.add(fid);
              }

          } catch (SQLException e) {

              System.out.println("Broken insert");

              e.printStackTrace();

          } 
         
         for(int i=0;i<=listarray.size()/2;i+=2){
        	 if(machine_id.equals(listarray.get(i))){
        		 fid = listarray.get(i+1);
        	 }
         }*/
         
         //查焊机
         /*inSql = "select fequipment_no, fstatus_id, fposition, finsframework_id from tb_welding_machine";
         
         try {

         	ResultSet rs =stmt.executeQuery(inSql);
             
             while (rs.next()) {
            	 
            	String fstatus_id = rs.getString("fstatus_id");
             	String fequipment_no = rs.getString("fequipment_no");
             	String fposition = rs.getString("fposition");
             	String finsframework_id = rs.getString("finsframework_id");
             	listarray1.add(fstatus_id);
             	listarray1.add(finsframework_id);
             	listarray1.add(fequipment_no);
             	listarray1.add(fposition);
             	
             }
             	
         } catch (SQLException e) {

             System.out.println("Broken insert");

             e.printStackTrace();

         }*/
         
         /*for(int i=0;i<listarray.size()/4;i+=4){
        	 
         }*/


         //查最大最小电流
         /*inSql = "select fmax_electricity, fmin_electricity, fmax_valtage, fmin_valtage from tb_welded_junction ";

         try {

         	ResultSet rs =stmt.executeQuery(inSql);
             
             while (rs.next()) {
             	int fmax_electricity1 = rs.getInt("fmax_electricity");
             	String fmax_electricity = String.valueOf(fmax_electricity1);
             	if(fmax_electricity.length()!=3){
             		int lenth=3-fmax_electricity.length();
             		for(int i=0;i<lenth;i++){
             			fmax_electricity="0"+fmax_electricity;
             		}
             	}
             	int fmin_electricity1 = rs.getInt("fmin_electricity");
             	String fmin_electricity = String.valueOf(fmin_electricity1);
             	if(fmin_electricity.length()!=3){
             		int lenth=3-fmin_electricity.length();
             		for(int i=0;i<lenth;i++){
             			fmin_electricity="0"+fmin_electricity;
             		}
             	}
             	int fmax_valtage1 = rs.getInt("fmax_valtage");
             	String fmax_valtage = String.valueOf(fmax_valtage1);
             	if(fmax_valtage.length()!=3){
             		int lenth=3-fmax_valtage.length();
             		for(int i=0;i<lenth;i++){
             			fmax_valtage="0"+fmax_valtage;
             		}
             	}
             	int fmin_valtage1 = rs.getInt("fmin_valtage");
             	String fmin_valtage = String.valueOf(fmin_valtage1);
             	if(fmin_valtage.length()!=3){
             		int lenth=3-fmin_valtage.length();
             		for(int i=0;i<lenth;i++){
             			fmin_valtage="0"+fmin_valtage;
             		}
             	}
             	
             	listarray2.add(fmax_electricity);
             	listarray2.add(fmin_electricity);
             	listarray2.add(fmax_valtage);
             	listarray2.add(fmin_valtage);
             	
             }

         } catch (SQLException e) {

             System.out.println("Broken insert");

             e.printStackTrace();

         } */
         
		 /*for(int i=0;i<listarray.size()/4;i+=4){
		        	 
		 }*/
         
         
         
         /*select = "select tb_welding_machine.fid from tb_gather left join tb_welding_machine on tb_gather.fid=tb_welding_machine.fgather_id where tb_gather.fgather_no = " + machine_id;
         
         try {

         	ResultSet rs =stmt.executeQuery(select);
             
             while (rs.next()) {
            	 
             	fmachine = rs.getString("fid");
             	
             }

         } catch (SQLException e) {

             System.out.println("Broken insert");

             e.printStackTrace();

         } */
         

        // if(state ==1)
         	
       /*  else {

             inSql = "insert into sj(id,bed_Number,state,Dtime) values('"+ id +"','" + bedNumber + "','leave','" + goodsC_date + "')";

        }*/

        /*try {

            stmt.close();

             conn.close(); 

        } catch (SQLException e) {

            System.out.println("Broken close");

            e.printStackTrace();

        }  */
    }
    
	
	protected void finalize( ){
		try {
			conn.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    
}