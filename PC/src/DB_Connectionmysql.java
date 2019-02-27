



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
	public int countbase1 = 1;
	public int countbase2 = 1;
	public int countbase3 = 1;
	public int countbase4 = 1;
	public String inSqlbase1 = "";
	public String inSqlbase2 = "";
	public String inSqlbase3 = "";
	public String inSqlbase4 = "";
	public final String inSqlbase = "insert into tb_live_data(fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,felectricity,fvoltage,fstatus,fwirefeedrate,FUploadDateTime,FWeldTime,fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage,fwelder_itemid,fjunction_itemid,fmachine_itemid,fmachinemodel,fwirediameter,fmaterialgas) values";
	

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

	public void DB_Connectionmysqlrun(long welderid, long weldid, long gatherid, long itemid, long weldid2,String weldmodel, long junctionid, BigDecimal electricity, BigDecimal voltage, int status,BigDecimal fwirefeedrate, Timestamp timesql, int channel, BigDecimal maxelectricity,BigDecimal minelectricity, BigDecimal maxvoltage, BigDecimal minvoltage, BigDecimal fwirediameter,int fmaterialgas, ArrayList<String> listarray1, ArrayList<String> listarray2,ArrayList<String> listarray3) {
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
                
                BigDecimal voltage1 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
                BigDecimal maxvoltage1 = new BigDecimal(((double)Integer.valueOf(maxvoltage.toString()))/10);
                BigDecimal minvoltage1 = new BigDecimal(((double)Integer.valueOf(minvoltage.toString()))/10);
                
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
           	 		inSqlbase1 = inSqlbase + "('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage1 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage1 + "','" + minvoltage1 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "')";
           	 	}else{
           	 		inSqlbase1 = inSqlbase1 + ",('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage1 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage1 + "','" + minvoltage1 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "')";
           	 	}
                
                countbase1++;
                
                if(countbase1 == 100){
                	
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
                
                BigDecimal voltage2 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
                BigDecimal maxvoltage2 = new BigDecimal(((double)Integer.valueOf(maxvoltage.toString()))/10);
                BigDecimal minvoltage2 = new BigDecimal(((double)Integer.valueOf(minvoltage.toString()))/10);
                
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
           	 		inSqlbase2 = inSqlbase + "('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage2 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage2 + "','" + minvoltage2 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "')";
           	 	}else{
           	 		inSqlbase2 = inSqlbase2 + ",('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage2 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage2 + "','" + minvoltage2 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "')";
           	 	}
                
                countbase2++;
                
                if(countbase2 == 100){
                	
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
                
                BigDecimal voltage3 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
                BigDecimal maxvoltage3 = new BigDecimal(((double)Integer.valueOf(maxvoltage.toString()))/10);
                BigDecimal minvoltage3 = new BigDecimal(((double)Integer.valueOf(minvoltage.toString()))/10);
                
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
           	 		inSqlbase3 = inSqlbase + "('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage3 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage3 + "','" + minvoltage3 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "')";
           	 	}else{
           	 		inSqlbase3 = inSqlbase3 + ",('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage3 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage3 + "','" + minvoltage3 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "')";
           	 	}
                
                countbase3++;
                
                if(countbase3 == 100){
                	
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
                
                BigDecimal voltage4 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
                BigDecimal maxvoltage4 = new BigDecimal(((double)Integer.valueOf(maxvoltage.toString()))/10);
                BigDecimal minvoltage4 = new BigDecimal(((double)Integer.valueOf(minvoltage.toString()))/10);
                
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
           	 		inSqlbase4 = inSqlbase + "('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage4 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage4 + "','" + minvoltage4 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "')";
           	 	}else{
           	 		inSqlbase4 = inSqlbase4 + ",('"+ welderid +"','" + gathernum + "','"  + weldid + "','"  + junctionid + "','" + itemid + "','" + electricity + "','" + voltage4 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage4 + "','" + minvoltage4 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "')";
           	 	}
                
                countbase4++;
                
                if(countbase4 == 100){
                	
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
                            
                            count1 = 0;
                            inSql1 = "";
                            
                        } catch (SQLException e) {
                        	count1 = 0;
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
                            
                            count2 = 0;
                            inSql2 = "";
                            
                        } catch (SQLException e) {
                        	count2 = 0;
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
                            
                            count3 = 0;
                            inSql3 = "";
                            
                        } catch (SQLException e) {
                        	count3 = 0;
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
                            
                            count4 = 0;
                            inSql4 = "";
                            
                        } catch (SQLException e) {
                        	count4 = 0;
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