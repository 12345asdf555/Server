



import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class DB_Connectionmysql {
	
	public String select;
	public  String datasend="";
	public String fmachine;
	public String connet;
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();
	
	public String getId() {
		return datasend;
	}
	public void setId(String datasend) {
		this.datasend = datasend;
	}

	public Server server;
	public java.sql.Connection conn = null;
	public java.sql.Statement stmt =null;

	public int work = 1;
	public int count1 = 1;
	public int count2 = 1;
	public int count3 = 1;
	public int count4 = 1;
	public String inSql1 = "";
	public String inSql2 = "";
	public String inSql3 = "";
	public String inSql4 = "";
	public final String inSql = "insert into tb_live_data(felectricity,fvoltage,frateofflow,fgather_no,fwelder_id,fjunction_id,fstatus,fitemid,FUploadDateTime,FWeldTime,fmachine_id) values";
	
	public int workbase = 1;
	public int workplc = 1;
	public int countbase1 = 1;
	public int countbase2 = 1;
	public int countbase3 = 1;
	public int countbase4 = 1;
	public int countplc1 = 1;
	public int countplc2 = 1;
	public int countplc3 = 1;
	public int countplc4 = 1;
	public String inSqlbase1 = "";
	public String inSqlbase2 = "";
	public String inSqlbase3 = "";
	public String inSqlbase4 = "";
	public String inSqlplc1 = "";
	public String inSqlplc2 = "";
	public String inSqlplc3 = "";
	public String inSqlplc4 = "";
	public final String inSqlbase = "insert into tb_live_data(fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,felectricity,fvoltage,fstatus,fwirefeedrate,FUploadDateTime,FWeldTime,fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage,fwelder_itemid,fjunction_itemid,fmachine_itemid,fmachinemodel,fwirediameter,fmaterialgas,frateofflow) values";
	public final String inSqlplc = "insert into tb_live_data(fwelder_id,fgather_no,fjunction_id,felectricity,fvoltage,fstatus,FUploadDateTime,FWeldTime,fd1000,fd1001,fd1002,fd1003,fd1004,fd1005,fd1006,fd1007,fd1008,fd1009,fd1010,fd1011,fd1012,fd1013,fd1014,fd1015,fd1016,fd1017,fd1018,fd1019,fd1020,fd1021,fd1022,fd1023,fd1024,fd1025,fd1026,fd1027,fd1028,fd1029,fd1030,fd1031,fd1032,fd1033,fd1034,fd1035,fd1036,fd1037,fd1038,fd1039,fd1040,fd1041,fd1042,fd1043,fd1044,fd1045,fd1046,fd1047,fd1048,fd1049,fd1050,fd1051,fd1052,fd1053,fd1054,fd1055,fd1056,fd1057,fd1058,fd1059,fd1060,fd1061,fd1062,fd1063,fd1064,fd1065,fd1066,fd1067,fd1068,fd1069,fd1070,fd1071,fd1072) values";
	

	public DB_Connectionmysql(){
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

	public void DB_Connectionmysqlrun(long welderid, long weldid, long gatherid, long itemid, long weldid2, String weldmodel, long junctionid, BigDecimal electricity, BigDecimal voltage, int status, BigDecimal fwirefeedrate, Timestamp timesql, int channel, BigDecimal maxelectricity, BigDecimal minelectricity, BigDecimal maxvoltage, BigDecimal minvoltage, BigDecimal fwirediameter, int fmaterialgas, ArrayList<String> listarray1, ArrayList<String> listarray2, ArrayList<String> listarray3, BigDecimal frateofflow) {
		// TODO Auto-generated method stub
		Date date;
    	String nowTime;
    	Timestamp goodsC_date;
    	String weldernum = "0000";
    	String welderins = "00";
    	String junctionnum = "0000";
    	String junctionins = "00";
    	String gathernum = "0000";
    	String weldnum = "0000";
    	String ins = "00";
    	
		synchronized (this) {
    	 	switch(Integer.valueOf(workbase)){
        	case 1:
        		date = new Date();
                nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss",date);
                goodsC_date = Timestamp.valueOf(nowTime);
                
                if("137".equals(weldmodel)){
                	electricity = new BigDecimal(((double)Integer.valueOf(electricity.toString()))/10);
                	maxelectricity = new BigDecimal(((double)Integer.valueOf(maxelectricity.toString()))/10);
                	minelectricity = new BigDecimal(((double)Integer.valueOf(minelectricity.toString()))/10);
                }
                
                BigDecimal voltage1 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
                BigDecimal maxvoltage1 = new BigDecimal(((double)Integer.valueOf(maxvoltage.toString()))/10);
                BigDecimal minvoltage1 = new BigDecimal(((double)Integer.valueOf(minvoltage.toString()))/10);
                BigDecimal fwirefeedrate1 = new BigDecimal(((double)Integer.valueOf(fwirefeedrate.toString()))/10);
                BigDecimal frateofflow1 = new BigDecimal(((double)Integer.valueOf(frateofflow.toString()))/10);
                
                for(int a=0;a<listarray1.size();a+=3){
                	if(welderid == Integer.valueOf(listarray1.get(a))){
                		weldernum = listarray1.get(a+1);
                		welderins = listarray1.get(a+2);
                		break;
                	}
                }
                
                for(int a=0;a<listarray3.size();a+=7){
                	if(junctionid == Integer.valueOf(listarray3.get(a+5))){
                		junctionnum = listarray3.get(a);
                		junctionins = listarray3.get(a+6);
                		break;
                	}
                }
                
                for(int a=0;a<listarray2.size();a+=4){
                	if(gatherid == Integer.valueOf(listarray2.get(a))){
                		gathernum = listarray2.get(a+2);
                		weldnum = listarray2.get(a+1);
                		ins = listarray2.get(a+3);
                		break;
                	}
                }
                
                if(ins == null || ins.equals("null")){
                	ins = "00";
                }
                if(junctionins.equals(null) || junctionins.equals("null")){
                	junctionins = "00";
                }
                if(welderins.equals(null) || welderins.equals("null")){
                	welderins = "00";
                }
                
                if(countbase1==1){
           	 		inSqlbase1 = inSqlbase + "('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage1 + "','" + status + "','" + fwirefeedrate1 + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage1 + "','" + minvoltage1 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + frateofflow1 + "')";
           	 	}else{
           	 		inSqlbase1 = inSqlbase1 + ",('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage1 + "','" + status + "','" + fwirefeedrate1 + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage1 + "','" + minvoltage1 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + frateofflow1 + "')";
           	 	}
                
                countbase1++;
                
                if(countbase1 == 5){
                	
                	try {
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
                        stmt.executeUpdate(inSqlbase1);
                        workbase = workbase + 1;
                        if(workbase==5){
                    		workbase = 1;
                    	}
                        
                        countbase1 = 1;
                        inSqlbase1 = "";
                        
                    } catch (SQLException e) {
                    	countbase1 = 1;
                        inSqlbase1 = "";
                        System.out.println("Broken insert");
                        e.printStackTrace();
                    } 
                }
                break;
                
        	case 2:
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                if("137".equals(weldmodel)){
                	electricity = new BigDecimal(((double)Integer.valueOf(electricity.toString()))/10);
                	maxelectricity = new BigDecimal(((double)Integer.valueOf(maxelectricity.toString()))/10);
                	minelectricity = new BigDecimal(((double)Integer.valueOf(minelectricity.toString()))/10);
                }
                
                BigDecimal voltage2 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
                BigDecimal maxvoltage2 = new BigDecimal(((double)Integer.valueOf(maxvoltage.toString()))/10);
                BigDecimal minvoltage2 = new BigDecimal(((double)Integer.valueOf(minvoltage.toString()))/10);
                BigDecimal fwirefeedrate2 = new BigDecimal(((double)Integer.valueOf(fwirefeedrate.toString()))/10);
                BigDecimal frateofflow2 = new BigDecimal(((double)Integer.valueOf(frateofflow.toString()))/10);
                
                for(int a=0;a<listarray1.size();a+=3){
                	if(welderid == Integer.valueOf(listarray1.get(a))){
                		weldernum = listarray1.get(a+1);
                		welderins = listarray1.get(a+2);
                	}
                }
                
                for(int a=0;a<listarray3.size();a+=7){
                	if(junctionid == Integer.valueOf(listarray3.get(a+5))){
                		junctionnum = listarray3.get(a);
                		junctionins = listarray3.get(a+6);
                	}
                }
                
                for(int a=0;a<listarray2.size();a+=4){
                	if(gatherid == Integer.valueOf(listarray2.get(a))){
                		gathernum = listarray2.get(a+2);
                		weldnum = listarray2.get(a+1);
                		ins = listarray2.get(a+3);
                	}
                }
                
                if(ins == null || ins.equals("null")){
                	ins = "00";
                }else if(junctionins.equals(null) || junctionins.equals("null")){
                	junctionins = "00";
                }else if(welderins.equals(null) || welderins.equals("null")){
                	welderins = "00";
                }
                
                if(countbase2==1){
           	 		inSqlbase2 = inSqlbase + "('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage2 + "','" + status + "','" + fwirefeedrate2 + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage2 + "','" + minvoltage2 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + frateofflow2 + "')";
           	 	}else{
           	 		inSqlbase2 = inSqlbase2 + ",('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage2 + "','" + status + "','" + fwirefeedrate2 + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage2 + "','" + minvoltage2 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + frateofflow2 + "')";
           	 	}
                
                countbase2++;
                
                if(countbase2 == 5){
                	
                	try {
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
                        stmt.executeUpdate(inSqlbase2);
                        workbase = workbase + 1;
                        if(workbase==5){
                    		workbase = 1;
                    	}
                        
                        countbase2 = 1;
                        inSqlbase2 = "";
                        
                    } catch (SQLException e) {
                    	countbase2 = 1;
                        inSqlbase2 = "";
                        System.out.println("Broken insert");
                        e.printStackTrace();
                    } 
                }
                break;
                
        	case 3:
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                if("137".equals(weldmodel)){
                	electricity = new BigDecimal(((double)Integer.valueOf(electricity.toString()))/10);
                	maxelectricity = new BigDecimal(((double)Integer.valueOf(maxelectricity.toString()))/10);
                	minelectricity = new BigDecimal(((double)Integer.valueOf(minelectricity.toString()))/10);
                }
                
                BigDecimal voltage3 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
                BigDecimal maxvoltage3 = new BigDecimal(((double)Integer.valueOf(maxvoltage.toString()))/10);
                BigDecimal minvoltage3 = new BigDecimal(((double)Integer.valueOf(minvoltage.toString()))/10);
                BigDecimal fwirefeedrate3 = new BigDecimal(((double)Integer.valueOf(fwirefeedrate.toString()))/10);
                BigDecimal frateofflow3 = new BigDecimal(((double)Integer.valueOf(frateofflow.toString()))/10);
                
                for(int a=0;a<listarray1.size();a+=3){
                	if(welderid == Integer.valueOf(listarray1.get(a))){
                		weldernum = listarray1.get(a+1);
                		welderins = listarray1.get(a+2);
                	}
                }
                
                for(int a=0;a<listarray3.size();a+=7){
                	if(junctionid == Integer.valueOf(listarray3.get(a+5))){
                		junctionnum = listarray3.get(a);
                		junctionins = listarray3.get(a+6);
                	}
                }
                
                for(int a=0;a<listarray2.size();a+=4){
                	if(gatherid == Integer.valueOf(listarray2.get(a))){
                		gathernum = listarray2.get(a+2);
                		weldnum = listarray2.get(a+1);
                		ins = listarray2.get(a+3);
                	}
                }
                
                if(ins == null || ins.equals("null")){
                	ins = "00";
                }else if(junctionins.equals(null) || junctionins.equals("null")){
                	junctionins = "00";
                }else if(welderins.equals(null) || welderins.equals("null")){
                	welderins = "00";
                }
                
                if(countbase3==1){
           	 		inSqlbase3 = inSqlbase + "('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage3 + "','" + status + "','" + fwirefeedrate3 + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage3 + "','" + minvoltage3 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + frateofflow3 + "')";
           	 	}else{
           	 		inSqlbase3 = inSqlbase3 + ",('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage3 + "','" + status + "','" + fwirefeedrate3 + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage3 + "','" + minvoltage3 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + frateofflow3 + "')";
           	 	}
                
                countbase3++;
                
                if(countbase3 == 5){
                	
                	try {
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
                        stmt.executeUpdate(inSqlbase3);
                        workbase = workbase + 1;
                        if(workbase==5){
                    		workbase = 1;
                    	}
                        
                        countbase3 = 1;
                        inSqlbase3 = "";
                        
                    } catch (SQLException e) {
                    	countbase3 = 1;
                        inSqlbase3 = "";
                        System.out.println("Broken insert");
                        e.printStackTrace();
                    } 
                }
                break;
                
        	case 4:
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                if("137".equals(weldmodel)){
                	electricity = new BigDecimal(((double)Integer.valueOf(electricity.toString()))/10);
                	maxelectricity = new BigDecimal(((double)Integer.valueOf(maxelectricity.toString()))/10);
                	minelectricity = new BigDecimal(((double)Integer.valueOf(minelectricity.toString()))/10);
                }
                
                BigDecimal voltage4 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
                BigDecimal maxvoltage4 = new BigDecimal(((double)Integer.valueOf(maxvoltage.toString()))/10);
                BigDecimal minvoltage4 = new BigDecimal(((double)Integer.valueOf(minvoltage.toString()))/10);
                BigDecimal fwirefeedrate4 = new BigDecimal(((double)Integer.valueOf(fwirefeedrate.toString()))/10);
                BigDecimal frateofflow4 = new BigDecimal(((double)Integer.valueOf(frateofflow.toString()))/10);
                
                for(int a=0;a<listarray1.size();a+=3){
                	if(welderid == Integer.valueOf(listarray1.get(a))){
                		weldernum = listarray1.get(a+1);
                		welderins = listarray1.get(a+2);
                	}
                }
                
                for(int a=0;a<listarray3.size();a+=7){
                	if(junctionid == Integer.valueOf(listarray3.get(a+5))){
                		junctionnum = listarray3.get(a);
                		junctionins = listarray3.get(a+6);
                	}
                }
                
                for(int a=0;a<listarray2.size();a+=4){
                	if(gatherid == Integer.valueOf(listarray2.get(a))){
                		gathernum = listarray2.get(a+2);
                		weldnum = listarray2.get(a+1);
                		ins = listarray2.get(a+3);
                	}
                }
                
                if(ins == null || ins.equals("null")){
                	ins = "00";
                }else if(junctionins.equals(null) || junctionins.equals("null")){
                	junctionins = "00";
                }else if(welderins.equals(null) || welderins.equals("null")){
                	welderins = "00";
                }
                
                if(countbase4==1){
           	 		inSqlbase4 = inSqlbase + "('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage4 + "','" + status + "','" + fwirefeedrate4 + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage4 + "','" + minvoltage4 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + frateofflow4 + "')";
           	 	}else{
           	 		inSqlbase4 = inSqlbase4 + ",('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage4 + "','" + status + "','" + fwirefeedrate4 + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage4 + "','" + minvoltage4 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + frateofflow4 + "')";
           	 	}
                
                countbase4++;
                
                if(countbase4 == 5){
                	
                	try {
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
                        stmt.executeUpdate(inSqlbase4);
                        workbase = workbase + 1;
                        if(workbase==5){
                    		workbase = 1;
                    	}
                        
                        countbase4 = 1;
                        inSqlbase4 = "";
                        
                    } catch (SQLException e) {
                    	countbase4 = 1;
                        inSqlbase4 = "";
                        System.out.println("Broken insert");
                        e.printStackTrace();
                    } 
                }
                break;
            }
		}
	}
	
	public void DB_Connectionmysqlplc(ArrayList<String> listarrayplc, int machine, int d1000, int d1001, int d1002, int d1003, int d1004, int d1005, int d1006, int d1007, int d1008, int d1009, int d1010, int d1011, int d1012, int d1013, int d1014, int d1015, int d1016, int d1017, int d1018, int d1019, int d1020, int d1021, int d1022, int d1023, int d1024, int d1025, int d1026, int d1027, int d1028, int d1029, int d1030, int d1031, int d1032, int d1033, int d1034, int d1035, int d1036, int d1037, int d1038, int d1039, int d1040, int d1041, int d1042, int d1043, int d1044, int d1045, int d1046, int d1047, int d1048, int d1049, int d1050, int d1051, int d1052, int d1053, int d1054, int d1055, int d1056, int d1057, int d1058, int d1059, int d1060, int d1061, int d1062, int d1063, int d1064, int d1065, int d1066, int d1067, int d1068, int d1069, int d1070, int d1071, int d1072) {

	 	Date date;
		String nowTime;
		Timestamp goodsC_date;
		int plcstatus;
		Integer welderid = null;
		switch(Integer.valueOf(workplc)){
    	case 1:
    		date = new Date();
            nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss",date);
            goodsC_date = Timestamp.valueOf(nowTime);
            
        	if(listarrayplc.contains(Integer.toString(machine))){
        		welderid = Integer.valueOf(listarrayplc.get(listarrayplc.indexOf(Integer.toString(machine))+1));
        	}else{
        		welderid = 0;
        	}
        	
        	if(Integer.valueOf(d1016) != 0 || Integer.valueOf(d1026) != 0 ||Integer.valueOf(d1036) != 0 ||Integer.valueOf(d1046) != 0){
        		plcstatus = 3;
        	}else{
        		plcstatus = 0;
        	}
        	
            if(countplc1==1){
       	 		inSqlplc1 = inSqlplc + "('"+ welderid +"','" + machine + "','"  + "0" + "','"  + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
       	 	}else{
       	 		inSqlplc1 = inSqlplc1 + ",('"+ welderid +"','" + machine + "','" + "0" + "','"  + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
       	 	}
            
            countplc1++;
            
            if(countplc1 == 100){
            	
            	try {
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
                    stmt.executeUpdate(inSqlplc1);
                    workplc = workplc + 1;
                    if(workplc==5){
                		workplc = 1;
                	}
                    
                    countplc1 = 1;
                    inSqlplc1 = "";
                    
                } catch (SQLException e) {
                	countplc1 = 1;
                    inSqlplc1 = "";
                    System.out.println("Broken insert");
                    e.printStackTrace();
                } 
            }
            break;
            
    	case 2:
    		date = new Date();
            nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss",date);
            goodsC_date = Timestamp.valueOf(nowTime);
            
        	if(listarrayplc.contains(Integer.toString(machine))){
        		welderid = Integer.valueOf(listarrayplc.get(listarrayplc.indexOf(Integer.toString(machine))+1));
        	}else{
        		welderid = 0;
        	}

        	if(Integer.valueOf(d1016) != 0 || Integer.valueOf(d1026) != 0 ||Integer.valueOf(d1036) != 0 ||Integer.valueOf(d1046) != 0){
        		plcstatus = 3;
        	}else{
        		plcstatus = 0;
        	}
        	
            if(countplc2==1){
       	 		inSqlplc2 = inSqlplc + "('"+ welderid +"','" + machine + "','"  + "0" + "','"  + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
       	 	}else{
       	 		inSqlplc2 = inSqlplc2 + ",('"+ welderid +"','" + machine + "','" + "0" + "','"  + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
       	 	}
            
            countplc2++;
            
            if(countplc2 == 100){
            	
            	try {
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
                    stmt.executeUpdate(inSqlplc2);
                    workplc = workplc + 1;
                    if(workplc==5){
                		workplc = 1;
                	}
                    
                    countplc2 = 1;
                    inSqlplc2 = "";
                    
                } catch (SQLException e) {
                	countplc2 = 1;
                    inSqlplc2 = "";
                    System.out.println("Broken insert");
                    e.printStackTrace();
                } 
            }
            break;
            
    	case 3:
    		date = new Date();
            nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss",date);
            goodsC_date = Timestamp.valueOf(nowTime);
            
        	if(listarrayplc.contains(Integer.toString(machine))){
        		welderid = Integer.valueOf(listarrayplc.get(listarrayplc.indexOf(Integer.toString(machine))+1));
        	}else{
        		welderid = 0;
        	}

        	if(Integer.valueOf(d1016) != 0 || Integer.valueOf(d1026) != 0 ||Integer.valueOf(d1036) != 0 ||Integer.valueOf(d1046) != 0){
        		plcstatus = 3;
        	}else{
        		plcstatus = 0;
        	}
        	
            if(countplc3==1){
       	 		inSqlplc3 = inSqlplc + "('"+ welderid +"','" + machine + "','"  + "0" + "','"  + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
       	 	}else{
       	 		inSqlplc3 = inSqlplc3 + ",('"+ welderid +"','" + machine + "','" + "0" + "','"  + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
       	 	}
            
            countplc3++;
            
            if(countplc3 == 100){
            	
            	try {
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
                    stmt.executeUpdate(inSqlplc3);
                    workplc = workplc + 1;
                    if(workplc==5){
                		workplc = 1;
                	}
                    
                    countplc3 = 1;
                    inSqlplc3 = "";
                    
                } catch (SQLException e) {
                	countplc3 = 1;
                    inSqlplc3 = "";
                    System.out.println("Broken insert");
                    e.printStackTrace();
                } 
            }
            break;
            
    	case 4:
    		date = new Date();
            nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss",date);
            goodsC_date = Timestamp.valueOf(nowTime);
            
        	if(listarrayplc.contains(Integer.toString(machine))){
        		welderid = Integer.valueOf(listarrayplc.get(listarrayplc.indexOf(Integer.toString(machine))+1));
        	}else{
        		welderid = 0;
        	}

        	if(Integer.valueOf(d1016) != 0 || Integer.valueOf(d1026) != 0 ||Integer.valueOf(d1036) != 0 ||Integer.valueOf(d1046) != 0){
        		plcstatus = 3;
        	}else{
        		plcstatus = 0;
        	}
        	
            if(countplc4==1){
       	 		inSqlplc4 = inSqlplc + "('"+ welderid +"','" + machine + "','"  + "0" + "','"  + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
       	 	}else{
       	 		inSqlplc4 = inSqlplc4 + ",('"+ welderid +"','" + machine + "','" + "0" + "','"  + "0" + "','" + "0" + "','" + plcstatus + "','" + goodsC_date + "','" + goodsC_date + "','" + d1000 + "','" + d1001 + "','" + d1002 + "','" + d1003 + "','" + d1004 + "','" + d1005 + "','" + d1006 + "','" + d1007 + "','" + d1008 + "','" + d1009 + "','" + d1010 + "','" + d1011 + "','" + d1012 + "','" + d1013 + "','" + d1014 + "','" + d1015 + "','" + d1016 + "','" + d1017 + "','" + d1018 + "','" + d1019 + "','" + d1020 + "','" + d1021 + "','" + d1022 + "','" + d1023 + "','" + d1024 + "','" + d1025 + "','" + d1026 + "','" + d1027 + "','" + d1028 + "','" + d1029 + "','" + d1030 + "','" + d1031 + "','" + d1032 + "','" + d1033 + "','" + d1034 + "','" + d1035 + "','" + d1036 + "','" + d1037 + "','" + d1038 + "','" + d1039 + "','" + d1040 + "','" + d1041 + "','" + d1042 + "','" + d1043 + "','" + d1044 + "','" + d1045 + "','" + d1046 + "','" + d1047 + "','" + d1048 + "','" + d1049 + "','" + d1050 + "','" + d1051 + "','" + d1052 + "','" + d1053 + "','" + d1054 + "','" + d1055 + "','" + d1056 + "','" + d1057 + "','" + d1058 + "','" + d1059 + "','" + d1060 + "','" + d1061 + "','" + d1062 + "','" + d1063 + "','" + d1064 + "','" + d1065 + "','" + d1066 + "','" + d1067 + "','" + d1068 + "','" + d1069 + "','" + d1070 + "','" + d1071 + "','" + d1072 + "')";
       	 	}
            
            countplc4++;
            
            if(countplc4 == 100){
            	
            	try {
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
                    stmt.executeUpdate(inSqlplc4);
                    workplc = workplc + 1;
                    if(workplc==5){
                		workplc = 1;
                	}
                    
                    countplc4 = 1;
                    inSqlplc4 = "";
                    
                } catch (SQLException e) {
                	countplc4 = 1;
                    inSqlplc4 = "";
                    System.out.println("Broken insert");
                    e.printStackTrace();
                } 
            }
            break;
        }
	
	}
	
    public void DB_Connectionmysqlrun(BigDecimal electricity,BigDecimal voltage,String sensor_Num,String machine_id,String welder_id,String code,int status,String fitemid,Timestamp timesql,ArrayList<String> listarray1,ArrayList<String> listarray2,ArrayList<String> listarray3){
    	
    	Date date;
    	String nowTime;
    	Timestamp goodsC_date;
    	String welder = "0";
    	String junctionnum = "0";
    	String fmachine = null;
    	synchronized (this) {
    	 	switch(Integer.valueOf(work)){
        	case 1:
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for(int i=0;i<listarray1.size();i+=2){
               	 	if(welder_id.equals(listarray1.get(i))){
               	 		welder = listarray1.get(i+1);
               	 		break;
               	 	}
                }
                
                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
               	 		break;
               	 	}
                }

                for(int i=0;i<listarray3.size();i+=6){
               	 	if(code.equals(listarray3.get(i+5))){
               	 		junctionnum = listarray3.get(i);
               	 		break;
               	 	}
                }
                
                if(fmachine != null){
               	 
               	 	BigDecimal voltage1 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
        	         
               	 	if(count1==1){
               	 		inSql1 = inSql + "('"+ electricity +"','" + voltage1 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
               	 	}else{
               	 		inSql1 = inSql1 + ",('"+ electricity +"','" + voltage1 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
               	 	}
                    
                    count1++;
                    
                    if(count1 == 100){
                    	
                    	try {
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
                            stmt.executeUpdate(inSql1);
                            work = work + 1;
                            if(work==5){
                        		work = 1;
                        	}
                            
                            count1 = 1;
                            inSql1 = "";
                            
                        } catch (SQLException e) {
                        	count1 = 1;
                            inSql1 = "";
                            System.out.println("Broken insert");
                            e.printStackTrace();
                        } 
                     }
                 }
         
                break;
                
        	case 2:
        		
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for(int i=0;i<listarray1.size();i+=2){
               	 	if(welder_id.equals(listarray1.get(i))){
               	 		welder = listarray1.get(i+1);
               	 		break;
               	 	}
                }
                
                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
               	 		break;
               	 	}
                }

                for(int i=0;i<listarray3.size();i+=6){
               	 	if(code.equals(listarray3.get(i+5))){
               	 		junctionnum = listarray3.get(i);
               	 		break;
               	 	}
                }

                if(fmachine != null){
               	 
               	 	BigDecimal voltage2 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
        	         
               	 	if(count2==1){
            	 		inSql2 = inSql + "('"+ electricity +"','" + voltage2 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
            	 	}else{
            	 		inSql2 = inSql2 + ",('"+ electricity +"','" + voltage2 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
            	 	}
               	 	
                    count2++;
                    
                    if(count2 == 100){
                    	
                    	try {
                        	
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
                            stmt.executeUpdate(inSql2);
                            work = work + 1;
                            if(work==5){
                        		work = 1;
                        	}
                            
                            count2 = 1;
                            inSql2 = "";
                            
                        } catch (SQLException e) {
                        	count2 = 1;
                            inSql2 = "";
                            System.out.println("Broken insert");
                            e.printStackTrace();
                        } 
                     }
                 }
        		
                break;
                
        	case 3:
        		
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for(int i=0;i<listarray1.size();i+=2){
               	 	if(welder_id.equals(listarray1.get(i))){
               	 		welder = listarray1.get(i+1);
               	 		break;
               	 	}
                }
                
                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
               	 		break;
               	 	}
                }

                for(int i=0;i<listarray3.size();i+=6){
               	 	if(code.equals(listarray3.get(i+5))){
               	 		junctionnum = listarray3.get(i);
               	 		break;
               	 	}
                }

                if(fmachine != null){
               	 
               	 	BigDecimal voltage3 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
        	         
               	 	if(count3==1){
            	 		inSql3 = inSql + "('"+ electricity +"','" + voltage3 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
            	 	}else{
            	 		inSql3 = inSql3 + ",('"+ electricity +"','" + voltage3 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
            	 	}
               	 
                    count3++;
                    
                    if(count3 == 100){
                    	
                    	try {
                        	
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
                            stmt.executeUpdate(inSql3);
                            work = work + 1;
                            if(work==5){
                        		work = 1;
                        	}
                            
                            count3 = 1;
                            inSql3 = "";
                            
                        } catch (SQLException e) {
                        	count3 = 1;
                            inSql3 = "";
                            System.out.println("Broken insert");
                            e.printStackTrace();
                        } 
                     }
                 }
        		
                break;
                
        	case 4:
        		
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for(int i=0;i<listarray1.size();i+=2){
               	 	if(welder_id.equals(listarray1.get(i))){
               	 		welder = listarray1.get(i+1);
               	 		break;
               	 	}
                }
                
                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
               	 		break;
               	 	}
                }

                for(int i=0;i<listarray3.size();i+=6){
               	 	if(code.equals(listarray3.get(i+5))){
               	 		junctionnum = listarray3.get(i);
               	 		break;
               	 	}
                }
                
                if(fmachine != null){
               	 
               	 	BigDecimal voltage4 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
        	         
               	 	if(count4==1){
            	 		inSql4 = inSql + "('"+ electricity +"','" + voltage4 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
            	 	}else{
            	 		inSql4 = inSql4 + ",('"+ electricity +"','" + voltage4 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
            	 	}
               	 
                    count4++;
                    
                    if(count4 == 100){
                    	
                    	try {
                        	
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
                            stmt.executeUpdate(inSql4);
                            work = work + 1;
                            if(work==5){
                        		work = 1;
                        	}
                            
                            count4 = 1;
                            inSql4 = "";
                            
                        } catch (SQLException e) {
                        	count4 = 1;
                            inSql4 = "";
                            System.out.println("Broken insert");
                            e.printStackTrace();
                        } 
                     }
                 }
                
                break;
                
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