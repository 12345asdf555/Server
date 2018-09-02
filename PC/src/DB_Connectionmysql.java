



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
	public int work1 = 1;
	public int count1 = 1;
	public int count2 = 1;
	public int count3 = 1;
	public int count4 = 1;
	public int count5 = 1;
	public int count6 = 1;
	public int count7 = 1;
	public int count8 = 1;
	public String inSql1 = "";
	public String inSql2 = "";
	public String inSql3 = "";
	public String inSql4 = "";
	public String inSql5 = "";
	public String inSql6 = "";
	public String inSql7 = "";
	public String inSql8 = "";
	public final String inSql = "insert into tb_live_data(felectricity,fvoltage,frateofflow,fgather_no,fwelder_id,fjunction_id,fstatus,fwirefeedrate,fweldingrate,fweldheatinput,fhatwirecurrent,fvibrafrequency,fitemid,FUploadDateTime,FWeldTime,fmachine_id) values";
	public final String inSql11 = "insert into tb_live_data(felectricity,fvoltage,frateofflow,fgather_no,fwelder_id,fjunction_id,fstatus,fitemid,FUploadDateTime,FWeldTime,fmachine_id) values";
	
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
	
public void DB_Connectionmysqlrun1(BigDecimal electricity,BigDecimal voltage,String sensor_Num,String machine_id,String welder_id,String code,int status,String fitemid,Timestamp timesql,ArrayList<String> listarray2){
    	
    	Date date;
    	String nowTime;
    	Timestamp goodsC_date;
    	synchronized (this) {
    	 	switch(Integer.valueOf(work)){
        	case 1:
        		
        		fmachine = null;
        		
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
               	 		break;
               	 	}
                }

                if(fmachine != null){
               	 
               	 	BigDecimal voltage1 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
        	         
               	 	if(count5==1){
               	 		inSql5 = inSql11 + "('"+ electricity +"','" + voltage1 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
               	 	}else{
               	 		inSql5 = inSql5 + ",('"+ electricity +"','" + voltage1 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
               	 	}
                    
                    count5++;
                    
                    if(count5 == 100){
                    	
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
                            stmt.executeUpdate(inSql5);
                            work = work + 1;
                            if(work==5){
                        		work = 1;
                        	}
                            
                            count5 = 0;
                            inSql5 = "";
                            
                        } catch (SQLException e) {
                        	count5 = 0;
                            inSql5 = "";
                            System.out.println("Broken insert");
                            e.printStackTrace();
                        } 
                     }
                 }
         
                break;
                
        	case 2:
        		
        		fmachine = null;
        		
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
               	 		break;
               	 	}
                }

                if(fmachine != null){
               	 
               	 	BigDecimal voltage2 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
        	         
               	 	if(count6==1){
            	 		inSql6 = inSql11 + "('"+ electricity +"','" + voltage2 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
            	 	}else{
            	 		inSql6 = inSql6 + ",('"+ electricity +"','" + voltage2 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
            	 	}
               	 	
                    count6++;
                    
                    if(count6 == 100){
                    	
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
                            stmt.executeUpdate(inSql6);
                            work = work + 1;
                            if(work==5){
                        		work = 1;
                        	}
                            
                            count6 = 0;
                            inSql6 = "";
                            
                        } catch (SQLException e) {
                        	count6 = 0;
                            inSql6 = "";
                            System.out.println("Broken insert");
                            e.printStackTrace();
                        } 
                     }
                 }
        		
                break;
                
        	case 3:
        		
        		fmachine = null;
        		
        		
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
               	 		break;
               	 	}
                }

                if(fmachine != null){
               	 
               	 	BigDecimal voltage3 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
        	         
               	 	if(count7==1){
            	 		inSql7 = inSql11 + "('"+ electricity +"','" + voltage3 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
            	 	}else{
            	 		inSql7 = inSql7 + ",('"+ electricity +"','" + voltage3 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
            	 	}
               	 
                    count7++;
                    
                    if(count7 == 100){
                    	
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
                            stmt.executeUpdate(inSql7);
                            work = work + 1;
                            if(work==5){
                        		work = 1;
                        	}
                            
                            count7 = 0;
                            inSql7 = "";
                            
                        } catch (SQLException e) {
                        	count7 = 0;
                            inSql7 = "";
                            System.out.println("Broken insert");
                            e.printStackTrace();
                        } 
                     }
                 }
        		
                break;
                
        	case 4:
        		
        		fmachine = null;
        		
        		
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
               	 		break;
               	 	}
                }

                if(fmachine != null){
               	 
               	 	BigDecimal voltage4 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
        	         
               	 	if(count8==1){
            	 		inSql8 = inSql11 + "('"+ electricity +"','" + voltage4 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
            	 	}else{
            	 		inSql8 = inSql8 + ",('"+ electricity +"','" + voltage4 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
            	 	}
               	 
                    count8++;
                    
                    if(count8 == 100){
                    	
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
                            stmt.executeUpdate(inSql8);
                            work = work + 1;
                            if(work==5){
                        		work = 1;
                        	}
                            
                            count8 = 0;
                            inSql8 = "";
                            
                        } catch (SQLException e) {
                        	count8 = 0;
                            inSql8 = "";
                            System.out.println("Broken insert");
                            e.printStackTrace();
                        } 
                     }
                 }
                
                break;
                
        	 }
		 }
     }
	
	
	
	

    public void DB_Connectionmysqlrun(BigDecimal electricity,BigDecimal voltage,String sensor_Num,int status,BigDecimal wirefeedrate,BigDecimal weldingrate,BigDecimal weldheatinput,BigDecimal hatwirecurrent,BigDecimal vibrafrequency,String machine_id,String welder_id,String code,String fitemid,Timestamp timesql,ArrayList<String> listarray2){
    	
    	Date date;
    	String nowTime;
    	Timestamp goodsC_date;
    	synchronized (this) {
    	 	switch(Integer.valueOf(work1)){
        	case 1:
        		
        		fmachine = null;
        		
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
               	 		break;
               	 	}
                }

                if(fmachine != null){
               	 
               	 	BigDecimal voltage1 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
        	         
               	 	if(count1==1){
               	 		inSql1 = inSql + "('"+ electricity + "','" + voltage1 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + wirefeedrate + "','" + weldingrate + "','" + weldheatinput + "','" + hatwirecurrent + "','" + vibrafrequency + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
               	 	}else{
               	 		inSql1 = inSql1 + ",('"+ electricity + "','" + voltage1 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + wirefeedrate + "','" + weldingrate + "','" + weldheatinput + "','" + hatwirecurrent + "','" + vibrafrequency + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
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
                            work1 = work1 + 1;
                            if(work1==5){
                        		work1 = 1;
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
        		
        		fmachine = null;
        		
        		
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
               	 		break;
               	 	}
                }

                if(fmachine != null){
               	 
               	 	BigDecimal voltage2 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
        	         
               	 	if(count2==1){
            	 		inSql2 = inSql + "('"+ electricity + "','" + voltage2 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + wirefeedrate + "','" + weldingrate + "','" + weldheatinput + "','" + hatwirecurrent + "','" + vibrafrequency + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
            	 	}else{
            	 		inSql2 = inSql2 + ",('"+ electricity + "','" + voltage2 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + wirefeedrate + "','" + weldingrate + "','" + weldheatinput + "','" + hatwirecurrent + "','" + vibrafrequency + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
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
                            work1 = work1 + 1;
                            if(work1==5){
                        		work1 = 1;
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
        		
        		fmachine = null;
        		
        		
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
               	 		break;
               	 	}
                }

                if(fmachine != null){
               	 
               	 	BigDecimal voltage3 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
        	         
               	 	if(count3==1){
            	 		inSql3 = inSql + "('"+ electricity + "','" + voltage3 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + wirefeedrate + "','" + weldingrate + "','" + weldheatinput + "','" + hatwirecurrent + "','" + vibrafrequency + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
            	 	}else{
            	 		inSql3 = inSql3 + ",('"+ electricity + "','" + voltage3 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + wirefeedrate + "','" + weldingrate + "','" + weldheatinput + "','" + hatwirecurrent + "','" + vibrafrequency + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
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
                            work1 = work1 + 1;
                            if(work1==5){
                        		work1 = 1;
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
        		
        		fmachine = null;
        		
        		
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
               	 		break;
               	 	}
                }

                if(fmachine != null){
               	 
               	 	BigDecimal voltage4 = new BigDecimal(((double)Integer.valueOf(voltage.toString()))/10);
        	         
               	 	if(count4==1){
            	 		inSql4 = inSql + "('"+ electricity + "','" + voltage4 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + wirefeedrate + "','" + weldingrate + "','" + weldheatinput + "','" + hatwirecurrent + "','" + vibrafrequency + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
            	 	}else{
            	 		inSql4 = inSql4 + ",('"+ electricity + "','" + voltage4 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + wirefeedrate + "','" + weldingrate + "','" + weldheatinput + "','" + hatwirecurrent + "','" + vibrafrequency + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
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
                            work1 = work1 + 1;
                            if(work1==5){
                        		work1 = 1;
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



/*import java.math.BigDecimal;
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

	public DB_Connectionmysql(){
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
    	
    	Date date;
    	String nowTime;
    	Timestamp goodsC_date;
    	synchronized (this) {
    	 	switch(Integer.valueOf(work)){
        	case 1:
        		date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
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

                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
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

                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
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

                for(int i=0;i<listarray2.size();i+=4){
               	 	if(machine_id.equals(listarray2.get(i+2))){
               	 		fmachine = listarray2.get(i);
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
    
    protected void finalize( ){
		try {
			conn.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    
}*/