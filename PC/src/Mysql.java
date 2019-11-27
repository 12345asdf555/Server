import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Mysql {

	public java.sql.Statement stmt;
	public ArrayList<String> listarray1;
	public ArrayList<String> listarray2;
	public ArrayList<String> listarray3;
	public DB_Connectionmysql db;

	public Mysql() {
		db = new DB_Connectionmysql();
		this.db = db;
	}

	public void Mysqlbase(String str) {
		// TODO Auto-generated method stub
		Date time;
		Timestamp timesql = null;
		if (str.length() == 290) {

			try{
				//鏍￠獙绗竴浣嶆槸鍚︿负FA鏈綅鏄惁涓篎5
				String check1 =str.substring(0,2);
				String check11=str.substring(288,290);
				if(check1.equals("7E") && check11.equals("7D")){

					long welderid = Integer.valueOf(str.substring(40, 44));
					long weldid = Integer.valueOf(str.substring(20, 24));
					long gatherid = Integer.valueOf(str.substring(16, 20));
					long itemid = Integer.valueOf(str.substring(286, 288));
					String weldmodel = Integer.valueOf(str.subSequence(12, 14).toString(),16).toString();

					for(int a=0;a<161;a+=80){
						try{
							long junctionid = Integer.valueOf(str.substring(76+a, 84+a));
							BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(56+a, 60+a).toString(),16));
							BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(60+a, 64+a).toString(),16));
							int status = Integer.parseInt(str.subSequence(84+a, 86+a).toString(),16);
							BigDecimal fwirefeedrate = new BigDecimal(Integer.valueOf(str.subSequence(64+a, 68+a).toString(),16));
							String year = Integer.valueOf(str.subSequence(44+a, 46+a).toString(),16).toString();
							String month = Integer.valueOf(str.subSequence(46+a, 48+a).toString(),16).toString();
							String day = Integer.valueOf(str.subSequence(48+a, 50+a).toString(),16).toString();
							String hour = Integer.valueOf(str.subSequence(50+a, 52+a).toString(),16).toString();
							String minute = Integer.valueOf(str.subSequence(52+a, 54+a).toString(),16).toString();
							String second = Integer.valueOf(str.subSequence(54+a, 56+a).toString(),16).toString();
							String strdate = year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;

							try {
								time = DateTools.parse("yy-MM-dd HH:mm:ss",strdate);
								timesql = new Timestamp(time.getTime());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							int channel = Integer.valueOf(str.subSequence(106+a, 108+a).toString(),16);
							BigDecimal maxelectricity = new BigDecimal(Integer.valueOf(str.subSequence(90+a, 94+a).toString(),16));
							BigDecimal minelectricity = new BigDecimal(Integer.valueOf(str.subSequence(94+a, 98+a).toString(),16));
							BigDecimal maxvoltage = new BigDecimal(Integer.valueOf(str.subSequence(98+a, 102+a).toString(),16));
							BigDecimal minvoltage = new BigDecimal(Integer.valueOf(str.subSequence(102+a, 106+a).toString(),16));
							BigDecimal frateofflow = new BigDecimal(Integer.valueOf(str.subSequence(108+a, 112+a).toString(),16));
							BigDecimal fwirediameter = new BigDecimal(Integer.valueOf(str.subSequence(86+a, 88+a).toString(),16));
							int fmaterialgas = Integer.parseInt(str.subSequence(88+a, 90+a).toString(),16);

							db.DB_Connectionmysqlrun(welderid,weldid,gatherid,itemid,weldid,weldmodel,junctionid,electricity,voltage,status,fwirefeedrate,timesql,channel,maxelectricity,minelectricity,maxvoltage,minvoltage,fwirediameter,fmaterialgas,listarray1,listarray2,listarray3,frateofflow);	 
						}catch(Exception e){
							System.out.println(str);
							System.out.println(str.substring(76+a, 84+a));
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Mysqlplc(String str,ArrayList<String> listarrayplc) {
		int machine = Integer.valueOf(str.substring(2,4));
		int d1000 = Integer.valueOf(str.substring(4,8),16);
		int d1001 = Integer.valueOf(str.substring(8,12),16);
		int d1002 = Integer.valueOf(str.substring(12,16),16);
		int d1003 = Integer.valueOf(str.substring(16,20),16);
		int d1004 = Integer.valueOf(str.substring(20,24),16);
		int d1005 = Integer.valueOf(str.substring(24,28),16);
		int d1006 = Integer.valueOf(str.substring(28,32),16);
		int d1007 = Integer.valueOf(str.substring(32,36),16);
		int d1008 = Integer.valueOf(str.substring(36,40),16);
		int d1009 = Integer.valueOf(str.substring(40,44),16);
		int d1010 = Integer.valueOf(str.substring(44,48),16);
		int d1011 = Integer.valueOf(str.substring(48,52),16);
		int d1012 = Integer.valueOf(str.substring(52,56),16);
		int d1013 = Integer.valueOf(str.substring(56,60),16);
		int d1014 = Integer.valueOf(str.substring(60,64),16);
		int d1015 = Integer.valueOf(str.substring(64,68),16);
		int d1016 = Integer.valueOf(str.substring(68,72),16);
		int d1017 = Integer.valueOf(str.substring(72,76),16);
		int d1018 = Integer.valueOf(str.substring(76,80),16);
		int d1019 = Integer.valueOf(str.substring(80,84),16);
		int d1020 = Integer.valueOf(str.substring(84,88),16);
		int d1021 = Integer.valueOf(str.substring(88,92),16);
		int d1022 = Integer.valueOf(str.substring(92,96),16);
		int d1023 = Integer.valueOf(str.substring(96,100),16);
		int d1024 = Integer.valueOf(str.substring(100,104),16);
		int d1025 = Integer.valueOf(str.substring(104,108),16);
		int d1026 = Integer.valueOf(str.substring(108,112),16);
		int d1027 = Integer.valueOf(str.substring(112,116),16);
		int d1028 = Integer.valueOf(str.substring(116,120),16);
		int d1029 = Integer.valueOf(str.substring(120,124),16);
		int d1030 = Integer.valueOf(str.substring(124,128),16);
		int d1031 = Integer.valueOf(str.substring(128,132),16);
		int d1032 = Integer.valueOf(str.substring(132,136),16);
		int d1033 = Integer.valueOf(str.substring(136,140),16);
		int d1034 = Integer.valueOf(str.substring(140,144),16);
		int d1035 = Integer.valueOf(str.substring(144,148),16);
		int d1036 = Integer.valueOf(str.substring(148,152),16);
		int d1037 = Integer.valueOf(str.substring(152,156),16);
		int d1038 = Integer.valueOf(str.substring(156,160),16);
		int d1039 = Integer.valueOf(str.substring(160,164),16);
		int d1040 = Integer.valueOf(str.substring(164,168),16);
		int d1041 = Integer.valueOf(str.substring(168,172),16);
		int d1042 = Integer.valueOf(str.substring(172,176),16);
		int d1043 = Integer.valueOf(str.substring(176,180),16);
		int d1044 = Integer.valueOf(str.substring(180,184),16);
		int d1045 = Integer.valueOf(str.substring(184,188),16);
		int d1046 = Integer.valueOf(str.substring(188,192),16);
		int d1047 = Integer.valueOf(str.substring(192,196),16);
		int d1048 = Integer.valueOf(str.substring(196,200),16);
		int d1049 = Integer.valueOf(str.substring(200,204),16);
		int d1050 = Integer.valueOf(str.substring(204,208),16);
		int d1051 = Integer.valueOf(str.substring(208,212),16);
		int d1052 = Integer.valueOf(str.substring(212,216),16);
		int d1053 = Integer.valueOf(str.substring(216,220),16);
		int d1054 = Integer.valueOf(str.substring(220,224),16);
		int d1055 = Integer.valueOf(str.substring(224,228),16);
		int d1056 = Integer.valueOf(str.substring(228,232),16);
		int d1057 = Integer.valueOf(str.substring(232,236),16);
		int d1058 = Integer.valueOf(str.substring(236,240),16);
		int d1059 = Integer.valueOf(str.substring(240,244),16);
		int d1060 = Integer.valueOf(str.substring(244,248),16);
		int d1061 = Integer.valueOf(str.substring(248,252),16);
		int d1062 = Integer.valueOf(str.substring(252,256),16);
		int d1063 = Integer.valueOf(str.substring(256,260),16);
		int d1064 = Integer.valueOf(str.substring(260,264),16);
		int d1065 = Integer.valueOf(str.substring(264,268),16);
		int d1066 = Integer.valueOf(str.substring(268,272),16);
		int d1067 = Integer.valueOf(str.substring(272,276),16);
		int d1068 = Integer.valueOf(str.substring(276,280),16);
		int d1069 = Integer.valueOf(str.substring(280,284),16);
		int d1070 = Integer.valueOf(str.substring(284,288),16);
		int d1071 = Integer.valueOf(str.substring(288,292),16);
		int d1072 = Integer.valueOf(str.substring(292,296),16);
		db.DB_Connectionmysqlplc(listarrayplc,machine,d1000,d1001,d1002,d1003,d1004,d1005,d1006,d1007,d1008,d1009,d1010,d1011,d1012,d1013,d1014,d1015,d1016,d1017,d1018,d1019,d1020,d1021,d1022,d1023,d1024,d1025,d1026,d1027,d1028,d1029,d1030,d1031,d1032,d1033,d1034,d1035,d1036,d1037,d1038,d1039,d1040,d1041,d1042,d1043,d1044,d1045,d1046,d1047,d1048,d1049,d1050,d1051,d1052,d1053,d1054,d1055,d1056,d1057,d1058,d1059,d1060,d1061,d1062,d1063,d1064,d1065,d1066,d1067,d1068,d1069,d1070,d1071,d1072);
	}

	public void Mysqlrun(String str) {
		// TODO Auto-generated constructor stub
		try{

			if (str.length() == 110) {  

				//鏍￠獙绗竴浣嶆槸鍚︿负FA鏈綅鏄惁涓篎5
				String check1 =str.substring(0,2);
				String check11=str.substring(108,110);
				if(check1.equals("FA") && check11.equals("F5")){

					//鏍￠獙闀垮害
					int check2=str.length();
					if(check2==110){

						//鏍￠獙浣嶆牎楠�
						String check3=str.substring(2,104);
						String check5="";
						int check4=0;
						for (int i11 = 0; i11 < check3.length()/2; i11++)
						{
							String tstr1=check3.substring(i11*2, i11*2+2);
							check4+=Integer.valueOf(tstr1,16);
						}
						if((Integer.toHexString(check4)).toUpperCase().length()==2){
							check5 = ((Integer.toHexString(check4)).toUpperCase());
						}else{
							check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1,3);
						}
						String check6 = str.substring(104,106);
						if(check5.equals(check6)){

							for(int i=0;i<78;i+=26){

								BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(26+i, 30+i).toString(),16));
								BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(30+i, 34+i).toString(),16));
								long sensor_Num1 = Integer.valueOf(str.subSequence(34+i, 38+i).toString(),16);
								String sensor_Num = String.valueOf(sensor_Num1);
								if(sensor_Num.length()<4){
									int num=4-sensor_Num.length();
									for(int i1=0;i1<num;i1++){
										sensor_Num="0"+sensor_Num;
									}
								}
								long machine_id1 = Integer.valueOf(str.subSequence(10, 14).toString(),16);
								String machine_id = String.valueOf(machine_id1);
								if(machine_id.length()<4){
									int num=4-machine_id.length();
									for(int i1=0;i1<num;i1++){
										machine_id="0"+machine_id;
									}
								}
								long welder_id1 = Integer.valueOf(str.subSequence(14, 18).toString(),16);
								String welder_id = String.valueOf(welder_id1);
								if(welder_id.length()<4){
									int num=4-welder_id.length();
									for(int i1=0;i1<num;i1++){
										welder_id="0"+welder_id;
									}
								}
								long code1 = Integer.valueOf(str.subSequence(18, 26).toString(),16);
								String code = String.valueOf(code1);
								if(code.length()<8){
									int num=8-code.length();
									for(int i1=0;i1<num;i1++){
										code="0"+code;
									}
								}
								long year = Integer.valueOf(str.subSequence(40+i, 42+i).toString(),16);
								String yearstr = String.valueOf(year);
								long month = Integer.valueOf(str.subSequence(42+i, 44+i).toString(),16);
								String monthstr = String.valueOf(month);
								long day = Integer.valueOf(str.subSequence(44+i, 46+i).toString(),16);
								String daystr = String.valueOf(day);
								long hour = Integer.valueOf(str.subSequence(46+i, 48+i).toString(),16);
								String hourstr = String.valueOf(hour);
								long minute = Integer.valueOf(str.subSequence(48+i, 50+i).toString(),16);
								String minutestr = String.valueOf(minute);
								long second = Integer.valueOf(str.subSequence(50+i, 52+i).toString(),16);
								String secondstr = String.valueOf(second);
								int status = Integer.parseInt(str.subSequence(38+i, 40+i).toString(),16);

								String timestr = yearstr+"-"+monthstr+"-"+daystr+" "+hourstr+":"+minutestr+":"+secondstr;
								try {
									Date time = DateTools.parse("yy-MM-dd HH:mm:ss",timestr);
									//java.util.Date time1 = timeshow.parse(timestr);
									Timestamp timesql = new Timestamp(time.getTime());

									/*BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(26+i, 30+i).toString(),16));
	                             BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(30+i, 34+i).toString(),16));
	                             long sensor_Num = Integer.valueOf(str.subSequence(34+i, 38+i).toString(),16);
	                             long machine_id = Integer.valueOf(str.subSequence(10, 14).toString(),16);
	                             long welder_id = Integer.valueOf(str.subSequence(14, 18).toString(),16);
	                             long code = Integer.valueOf(str.subSequence(18, 26).toString(),16);
	                             long year = Integer.valueOf(str.subSequence(40+i, 42+i).toString(),16);
	                             long month = Integer.valueOf(str.subSequence(42+i, 44+i).toString(),16);
	                             long day = Integer.valueOf(str.subSequence(44+i, 46+i).toString(),16);
	                             long hour = Integer.valueOf(str.subSequence(46+i, 48+i).toString(),16);
	                             long minute = Integer.valueOf(str.subSequence(48+i, 50+i).toString(),16);
	                             long second = Integer.valueOf(str.subSequence(50+i, 52+i).toString(),16);
	                             int status = Integer.parseInt(str.subSequence(38+i, 40+i).toString());*/

									String fitemid = str.substring(106, 108);

									db.DB_Connectionmysqlrun(electricity,voltage,sensor_Num,machine_id,welder_id,code,status,fitemid,timesql,listarray1,listarray2,listarray3);
									//System.out.println(str);
								} catch (Exception e) {
									str="";
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							//System.out.println(str);
							//db=null;
							//System.gc();
							str="";
						}

						else{
							//校锟斤拷位锟斤拷锟斤拷
							System.out.print("鏁版嵁鎺ユ敹鏍￠獙浣嶉敊璇�");
							str="";
						}

					}

					else{
						//锟斤拷锟饺达拷锟斤拷
						System.out.print("鏁版嵁鎺ユ敹闀垮害閿欒");
						str="";
					}

				}
				else{
					//锟斤拷位锟斤拷锟斤拷FA
					System.out.println("11");
					System.out.print("鏁版嵁鎺ユ敹棣栨湯浣嶉敊璇�");
					str="";
				}

			}/*else if(str.length()>=300 && str.length()!= 118){

       	    	String [] stringArr = str.split("FD");

                for(int i =0;i < stringArr.length;i++)
		        {
	        	     //鏍￠獙绗竴浣嶆槸鍚︿负FE
		       	     String check1 =stringArr[i].substring(0,2);
		       	     if(check1.equals("FE")){

		       	    	 //鏍￠獙闀垮害
		           	     int check2=stringArr[i].length();
		           	     if(check2==54){

                        	 if(stringArr[i].length()>30){

                        		BigDecimal electricity = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(4, 8).toString(),16));
                                BigDecimal voltage = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(8, 12).toString(),16));
                                BigInteger sensor_Num = new BigInteger(stringArr[i].subSequence(12, 16).toString());
                                String machine_id = stringArr[i].subSequence(16, 20).toString();
                                String welder_id = stringArr[i].subSequence(20, 24).toString();
                                String code = stringArr[i].subSequence(24, 32).toString();
                                BigInteger year = new BigInteger(Integer.valueOf(stringArr[i].subSequence(32, 34).toString(),16).toString());
                                BigInteger month = new BigInteger(Integer.valueOf(stringArr[i].subSequence(34, 36).toString(),16).toString());
                                BigInteger day = new BigInteger(Integer.valueOf(stringArr[i].subSequence(36, 38).toString(),16).toString());
                                BigInteger hour = new BigInteger(Integer.valueOf(stringArr[i].subSequence(38, 40).toString(),16).toString());
                                BigInteger minute = new BigInteger(Integer.valueOf(stringArr[i].subSequence(40, 42).toString(),16).toString());
                                BigInteger second = new BigInteger(Integer.valueOf(stringArr[i].subSequence(42, 44).toString(),16).toString());
                                Integer status = Integer.valueOf(stringArr[i].subSequence(44, 46).toString());

                                DB_Connectionandroid a = new DB_Connectionandroid(electricity,voltage,sensor_Num,machine_id,welder_id,code,year,month,day,hour,minute,second,status,connet); 
                                str="";

                        	 } 

		           	     }   
		           	     else{
		           	    //锟斤拷锟饺达拷锟斤拷
		           	    	 System.out.print("鏁版嵁鎺ユ敹闀垮害閿欒");
		           	    	 str="";
		           	     }
	       	         }
		       	     else{
		       	    	 //锟斤拷位锟斤拷锟斤拷FE
		       	    	System.out.println("12");
		   	        	 System.out.print("鏁版嵁鎺ユ敹棣栨湯浣嶉敊璇�");
		   	        	 str="";
		       	     }
	       	     }

	           }else if(str.length() == 118){

	        	   str="";

	           }*/

		} catch (Exception e) {
			str="";
			System.out.println("S: Error 2");  
			e.printStackTrace();  
		} 
	}

	public void Mysqlohwh(String str) {
		// TODO Auto-generated method stub
		Date time;
		Timestamp timesql = null;
		long welderid = Integer.valueOf(str.substring(70, 74));
		long weldid = Integer.valueOf(str.substring(20, 24));
		long gatherid = Integer.valueOf(str.substring(16, 20));
		long itemid = Integer.valueOf(str.substring(316, 318));
		String weldmodel = Integer.valueOf(str.subSequence(12, 14).toString(),16).toString();

		for(int a=0;a<161;a+=80){
			try{
				long junctionid = Integer.valueOf(str.substring(106+a, 114+a));
				BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(86+a, 90+a).toString(),16));
				BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(90+a, 94+a).toString(),16));
				int status = Integer.parseInt(str.subSequence(114+a, 116+a).toString(),16);
				BigDecimal fwirefeedrate = new BigDecimal(Integer.valueOf(str.subSequence(94+a, 98+a).toString(),16));
				String year = Integer.valueOf(str.subSequence(74+a, 76+a).toString(),16).toString();
				String month = Integer.valueOf(str.subSequence(76+a, 78+a).toString(),16).toString();
				String day = Integer.valueOf(str.subSequence(78+a, 80+a).toString(),16).toString();
				String hour = Integer.valueOf(str.subSequence(80+a, 82+a).toString(),16).toString();
				String minute = Integer.valueOf(str.subSequence(82+a, 84+a).toString(),16).toString();
				String second = Integer.valueOf(str.subSequence(84+a, 86+a).toString(),16).toString();
				String strdate = year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;

				try {
					time = DateTools.parse("yy-MM-dd HH:mm:ss",strdate);
					timesql = new Timestamp(time.getTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				int channel = Integer.valueOf(str.subSequence(136+a, 138+a).toString(),16);
				BigDecimal maxelectricity = new BigDecimal(Integer.valueOf(str.subSequence(120+a, 124+a).toString(),16));
				BigDecimal minelectricity = new BigDecimal(Integer.valueOf(str.subSequence(124+a, 128+a).toString(),16));
				BigDecimal maxvoltage = new BigDecimal(Integer.valueOf(str.subSequence(128+a, 132+a).toString(),16));
				BigDecimal minvoltage = new BigDecimal(Integer.valueOf(str.subSequence(132+a, 136+a).toString(),16));
				BigDecimal frateofflow = new BigDecimal(Integer.valueOf(str.subSequence(138+a, 142+a).toString(),16));
				BigDecimal fwirediameter = new BigDecimal(Integer.valueOf(str.subSequence(116+a, 118+a).toString(),16));
				int fmaterialgas = Integer.parseInt(str.subSequence(118+a, 120+a).toString(),16);

				db.DB_Connectionmysqlrun(welderid,weldid,gatherid,itemid,weldid,weldmodel,junctionid,electricity,voltage,status,fwirefeedrate,timesql,channel,maxelectricity,minelectricity,maxvoltage,minvoltage,fwirediameter,fmaterialgas,listarray1,listarray2,listarray3,frateofflow);	 
			}catch(Exception e){
				System.out.println(str);
				System.out.println(str.substring(76+a, 84+a));
			}
		}
	}

	/*@Override
	public void taskResult(String str,String connet,ArrayList<String> listarray1,ArrayList<String> listarray2,ArrayList<String> listarray3,HashMap<String, Socket> websocket,String ip1) {
		// TODO Auto-generated method stub

		this.str=str;
        this.connet=connet;
        //this.listarray1=listarray1;

		try{

            if (str.length() == 110) {  

            //校锟斤拷锟揭晃伙拷欠锟轿狥A末位锟角凤拷为F5
       	     String check1 =str.substring(0,2);
       	     String check11=str.substring(108,110);
       	     if(check1.equals("FA") && check11.equals("F5")){

           	     //校锟介长锟斤拷
           	     int check2=str.length();
           	     if(check2==110){

               	     //校锟斤拷位校锟斤拷
               	     String check3=str.substring(2,104);
               	     String check5="";
               	     int check4=0;
               	     for (int i11 = 0; i11 < check3.length()/2; i11++)
               	     {
               	    	String tstr1=check3.substring(i11*2, i11*2+2);
               	    	check4+=Integer.valueOf(tstr1,16);
               	     }
               	     if((Integer.toHexString(check4)).toUpperCase().length()==2){
               	    	check5 = ((Integer.toHexString(check4)).toUpperCase());
               	     }else{
               	    	check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1,3);
               	     }
               	     String check6 = str.substring(104,106);
               	     if(check5.equals(check6)){

               	    	 for(int i=0;i<78;i+=26){

               	    		 BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(26+i, 30+i).toString(),16));
                             BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(30+i, 34+i).toString(),16));
                             long sensor_Num1 = Integer.valueOf(str.subSequence(34+i, 38+i).toString(),16);
                             String sensor_Num = String.valueOf(sensor_Num1);
                             if(sensor_Num.length()<4){
                            	 int num=4-sensor_Num.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 sensor_Num="0"+sensor_Num;
                            	 }
                             }
                             long machine_id1 = Integer.valueOf(str.subSequence(10, 14).toString(),16);
                             String machine_id = String.valueOf(machine_id1);
                             if(machine_id.length()<4){
                            	 int num=4-machine_id.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 machine_id="0"+machine_id;
                            	 }
                             }
                             long welder_id1 = Integer.valueOf(str.subSequence(14, 18).toString(),16);
                             String welder_id = String.valueOf(welder_id1);
                             if(welder_id.length()<4){
                            	 int num=4-welder_id.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 welder_id="0"+welder_id;
                            	 }
                             }
                             long code1 = Integer.valueOf(str.subSequence(18, 26).toString(),16);
                             String code = String.valueOf(code1);
                             if(code.length()<8){
                            	 int num=8-code.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 code="0"+code;
                            	 }
                             }
                             long year = Integer.valueOf(str.subSequence(40+i, 42+i).toString(),16);
                             String yearstr = String.valueOf(year);
                             long month = Integer.valueOf(str.subSequence(42+i, 44+i).toString(),16);
                             String monthstr = String.valueOf(month);
                             long day = Integer.valueOf(str.subSequence(44+i, 46+i).toString(),16);
                             String daystr = String.valueOf(day);
                             long hour = Integer.valueOf(str.subSequence(46+i, 48+i).toString(),16);
                             String hourstr = String.valueOf(hour);
                             long minute = Integer.valueOf(str.subSequence(48+i, 50+i).toString(),16);
                             String minutestr = String.valueOf(minute);
                             long second = Integer.valueOf(str.subSequence(50+i, 52+i).toString(),16);
                             String secondstr = String.valueOf(second);
                             int status = Integer.parseInt(str.subSequence(38+i, 40+i).toString());

                             String timestr = yearstr+"-"+monthstr+"-"+daystr+" "+hourstr+":"+minutestr+":"+secondstr;
                             try {
	                            Date time = DateTools.parse("yy-MM-dd HH:mm:ss",timestr);
                            	//java.util.Date time1 = timeshow.parse(timestr);
								Timestamp timesql = new Timestamp(time.getTime());

               	    		 BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(26+i, 30+i).toString(),16));
                             BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(30+i, 34+i).toString(),16));
                             long sensor_Num = Integer.valueOf(str.subSequence(34+i, 38+i).toString(),16);
                             long machine_id = Integer.valueOf(str.subSequence(10, 14).toString(),16);
                             long welder_id = Integer.valueOf(str.subSequence(14, 18).toString(),16);
                             long code = Integer.valueOf(str.subSequence(18, 26).toString(),16);
                             long year = Integer.valueOf(str.subSequence(40+i, 42+i).toString(),16);
                             long month = Integer.valueOf(str.subSequence(42+i, 44+i).toString(),16);
                             long day = Integer.valueOf(str.subSequence(44+i, 46+i).toString(),16);
                             long hour = Integer.valueOf(str.subSequence(46+i, 48+i).toString(),16);
                             long minute = Integer.valueOf(str.subSequence(48+i, 50+i).toString(),16);
                             long second = Integer.valueOf(str.subSequence(50+i, 52+i).toString(),16);
                             int status = Integer.parseInt(str.subSequence(38+i, 40+i).toString());

								String fitemid = str.substring(106, 108);

							//DB_Connectionmysql a = new DB_Connectionmysql(electricity,voltage,sensor_Num,machine_id,welder_id,code,status,fitemid,timesql,connet,listarray1);
							} catch (Exception e) {
								str="";
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

               	    	 }
 	                    //System.out.println(str);
                   	    str="";
               	     }

               	     else{
               	        //校锟斤拷位锟斤拷锟斤拷
               	    	 System.out.print("锟斤拷锟捷斤拷锟斤拷校锟斤拷位锟斤拷锟斤拷");
               	    	 str="";
               	     }

           	     }

           	     else{
           	        //锟斤拷锟饺达拷锟斤拷
           	    	 System.out.print("锟斤拷锟捷斤拷锟秸筹拷锟饺达拷锟斤拷");
           	    	 str="";
           	     }

   	        	}
   	        	else{
   	        		//锟斤拷位锟斤拷锟斤拷FA
   	        		System.out.println("11");
   	        		System.out.print("锟斤拷锟捷斤拷锟斤拷锟斤拷末位锟斤拷锟斤拷");
   	        		str="";
   	        	}

           }else if(str.length()>=300 && str.length()!= 118){

       	    	String [] stringArr = str.split("FD");

                for(int i =0;i < stringArr.length;i++)
		        {
	        	     //校锟斤拷锟揭晃伙拷欠锟轿狥E
		       	     String check1 =stringArr[i].substring(0,2);
		       	     if(check1.equals("FE")){

		       	    	 //校锟介长锟斤拷
		           	     int check2=stringArr[i].length();
		           	     if(check2==54){

                        	 if(stringArr[i].length()>30){

                        		BigDecimal electricity = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(4, 8).toString(),16));
                                BigDecimal voltage = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(8, 12).toString(),16));
                                BigInteger sensor_Num = new BigInteger(stringArr[i].subSequence(12, 16).toString());
                                String machine_id = stringArr[i].subSequence(16, 20).toString();
                                String welder_id = stringArr[i].subSequence(20, 24).toString();
                                String code = stringArr[i].subSequence(24, 32).toString();
                                BigInteger year = new BigInteger(Integer.valueOf(stringArr[i].subSequence(32, 34).toString(),16).toString());
                                BigInteger month = new BigInteger(Integer.valueOf(stringArr[i].subSequence(34, 36).toString(),16).toString());
                                BigInteger day = new BigInteger(Integer.valueOf(stringArr[i].subSequence(36, 38).toString(),16).toString());
                                BigInteger hour = new BigInteger(Integer.valueOf(stringArr[i].subSequence(38, 40).toString(),16).toString());
                                BigInteger minute = new BigInteger(Integer.valueOf(stringArr[i].subSequence(40, 42).toString(),16).toString());
                                BigInteger second = new BigInteger(Integer.valueOf(stringArr[i].subSequence(42, 44).toString(),16).toString());
                                Integer status = Integer.valueOf(stringArr[i].subSequence(44, 46).toString());

                                DB_Connectionandroid a = new DB_Connectionandroid(electricity,voltage,sensor_Num,machine_id,welder_id,code,year,month,day,hour,minute,second,status,connet); 
                                str="";

                        	 } 

		           	     }   
		           	     else{
		           	    //锟斤拷锟饺达拷锟斤拷
		           	    	 System.out.print("锟斤拷锟捷斤拷锟秸筹拷锟饺达拷锟斤拷");
		           	    	 str="";
		           	     }
	       	         }
		       	     else{
		       	    	 //锟斤拷位锟斤拷锟斤拷FE
		       	    	System.out.println("12");
		   	        	 System.out.print("锟斤拷锟捷斤拷锟斤拷锟斤拷末位锟斤拷锟斤拷");
		   	        	 str="";
		       	     }
	       	     }

	           }else if(str.length() == 118){

	        	   str="";

	           }

		} catch (Exception e) {
			str="";
            System.out.println("S: Error 2");  
            e.printStackTrace();  
        }  

	}*/

}
