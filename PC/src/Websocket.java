import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class Websocket {

	Timestamp timesql1;
	Timestamp timesql2;
	Timestamp timesql3;
	private String limit;
	private String connet;
	private String strdata;
	private SocketChannel chweb;
	private String websocketfail;
	public ArrayList<String> listarray1;
	public ArrayList<String> listarray2;
	public ArrayList<String> listarray3;
	private boolean datawritetype = false;
	private HashMap<String, Socket> websocket;
	private HashMap<String, SocketChannel> websocketlist = null;
	public ArrayList<String> dbdata = new ArrayList<String>();
	public MyMqttClient mqtt;

	public void Websocketbase(String str, ArrayList<String> listarray2, ArrayList<String> listarray3,HashMap<String, SocketChannel> websocketlist) {
		// TODO Auto-generated method stub
		Date time;
		Timestamp timesql = null;

		if(!(websocketlist==null || websocketlist.isEmpty())){
		}
		else
		{	
			if (str.length() == 290) {

				//校验第一位是否为FA末位是否为F5
				String check1 =str.substring(0,2);
				String check11=str.substring(288,290);
				if(check1.equals("7E") && check11.equals("7D")){

					String welderid = Integer.valueOf(str.substring(40,44)).toString();
					if(welderid.length()!=4){
						int lenth=4-welderid.length();
						for(int i=0;i<lenth;i++){
							welderid="0"+welderid;
						}
					}
					String weldid = Integer.valueOf(str.substring(20,24)).toString();
					if(weldid.length()!=4){
						int lenth=4-weldid.length();
						for(int i=0;i<lenth;i++){
							weldid="0"+weldid;
						}
					}
					String gatherid = Integer.valueOf(str.substring(16,20)).toString();
					if(gatherid.length()!=4){
						int lenth=4-gatherid.length();
						for(int i=0;i<lenth;i++){
							gatherid="0"+gatherid;
						}
					}
					String itemins = Integer.valueOf(str.substring(286,288)).toString();
					if(itemins.length()!=4){
						int lenth=4-itemins.length();
						for(int i=0;i<lenth;i++){
							itemins="0"+itemins;
						}
					}
					String weldmodel = Integer.valueOf(str.subSequence(12, 14).toString(),16).toString();
					if(weldmodel.length()!=4){
						int lenth=4-weldmodel.length();
						for(int i=0;i<lenth;i++){
							weldmodel="0"+weldmodel;
						}
					}

					String strsend = "";
					for(int a=0;a<161;a+=80){
						try{
							String welderins = "0000";
							String junctionins = "0000";
							String ins = "0000";

							String junctionid = Integer.valueOf(str.substring(76+a, 84+a)).toString();
							if(junctionid.length()!=4){
								int lenth=4-junctionid.length();
								for(int i=0;i<lenth;i++){
									junctionid="0"+junctionid;
								}
							}
							String electricity = Integer.valueOf(str.subSequence(56+a, 60+a).toString(),16).toString();
							if(electricity.length()!=4){
								int lenth=4-electricity.length();
								for(int i=0;i<lenth;i++){
									electricity="0"+electricity;
								}
							}
							String voltage = Integer.valueOf(str.subSequence(60+a, 64+a).toString(),16).toString();
							if(voltage.length()!=4){
								int lenth=4-voltage.length();
								for(int i=0;i<lenth;i++){
									voltage="0"+voltage;
								}
							}
							String speed = Integer.valueOf(str.subSequence(64+a, 68+a).toString(),16).toString();
							if(speed.length()!=4){
								int lenth=4-speed.length();
								for(int i=0;i<lenth;i++){
									speed="0"+speed;
								}
							}
							String setelectricity = Integer.valueOf(str.subSequence(68+a, 72+a).toString(),16).toString();
							if(setelectricity.length()!=4){
								int lenth=4-setelectricity.length();
								for(int i=0;i<lenth;i++){
									setelectricity="0"+setelectricity;
								}
							}
							String setvoltage = Integer.valueOf(str.subSequence(72+a, 76+a).toString(),16).toString();
							if(setvoltage.length()!=4){
								int lenth=4-setvoltage.length();
								for(int i=0;i<lenth;i++){
									setvoltage="0"+setvoltage;
								}
							}
							String status = Integer.valueOf(str.subSequence(84+a, 86+a).toString(),16).toString();
							if(status.length()!=2){
								int lenth=2-status.length();
								for(int i=0;i<lenth;i++){
									status="0"+status;
								}
							}

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

							String channel = Integer.valueOf(str.subSequence(106+a, 108+a).toString(),16).toString();
							if(channel.length()!=4){
								int lenth=4-channel.length();
								for(int i=0;i<lenth;i++){
									channel="0"+channel;
								}
							}
							String maxelectricity = Integer.valueOf(str.subSequence(90+a, 94+a).toString(),16).toString();
							if(maxelectricity.length()!=4){
								int lenth=4-maxelectricity.length();
								for(int i=0;i<lenth;i++){
									maxelectricity="0"+maxelectricity;
								}
							}
							String minelectricity = Integer.valueOf(str.subSequence(94+a, 98+a).toString(),16).toString();
							if(minelectricity.length()!=4){
								int lenth=4-minelectricity.length();
								for(int i=0;i<lenth;i++){
									minelectricity="0"+minelectricity;
								}
							}
							String maxvoltage = Integer.valueOf(str.subSequence(98+a, 102+a).toString(),16).toString();
							if(maxvoltage.length()!=4){
								int lenth=4-maxvoltage.length();
								for(int i=0;i<lenth;i++){
									maxvoltage="0"+maxvoltage;
								}
							}
							String minvoltage = Integer.valueOf(str.subSequence(102+a, 106+a).toString(),16).toString();
							if(minvoltage.length()!=4){
								int lenth=4-minvoltage.length();
								for(int i=0;i<lenth;i++){
									minvoltage="0"+minvoltage;
								}
							}

							for(int i=0;i<listarray1.size();i+=3){
								if(Integer.valueOf(welderid) == Integer.valueOf(listarray1.get(i))){
									welderins = listarray1.get(i+2);
									if(welderins.equals(null) || welderins.equals("null")){
										break;
									}else{
										if(welderins.length()!=4){
											int lenth=4-welderins.length();
											for(int i1=0;i1<lenth;i1++){
												welderins="0"+welderins;
											}
										}
										break;
									}
								}
							}

							for(int i=0;i<listarray3.size();i+=7){
								if(Integer.valueOf(junctionid) == Integer.valueOf(listarray3.get(i+5))){
									junctionins = listarray3.get(i+6);
									if(junctionins.equals(null) || junctionins.equals("null")){
										break;
									}else{
										if(junctionins.length()!=4){
											int lenth=4-junctionins.length();
											for(int i1=0;i1<lenth;i1++){
												junctionins="0"+junctionins;
											}
										}
										break;
									}
								}
							}

							for(int i=0;i<listarray2.size();i+=4){
								if(Integer.valueOf(gatherid) == Integer.valueOf(listarray2.get(i))){
									ins = listarray2.get(i+3);
									if(ins == null || ins.equals("null")){
										break;
									}else{
										if(ins.length()!=4){
											int lenth=4-ins.length();
											for(int i1=0;i1<lenth;i1++){
												ins="0"+ins;
											}
										}
										break;
									}
								}
							}

							if(ins == null || ins.equals("null")){
								ins = "0000";
							}
							/*if(junctionins.equals(null) || junctionins.equals("null")){
								junctionins = "0000";
							}*/
							if(welderins.equals(null) || welderins.equals("null")){
								welderins = "0000";
							}


							//工作模式是否为碳弧气刨
							String workmodel = Integer.valueOf(str.subSequence(86+a, 88+a).toString(),16).toString();
							if(workmodel.length()!=4){
								int lenth=4-workmodel.length();
								for(int i=0;i<lenth;i++){
									workmodel="0"+workmodel;
								}
							}

							//气体流量
							String gasfare = Integer.valueOf(str.subSequence(108+a, 112+a).toString(),16).toString();
							if(gasfare.length()!=4){
								int lenth=4-gasfare.length();
								for(int i=0;i<lenth;i++){
									gasfare="0"+gasfare;
								}
							}
							
							strsend  = strsend + welderid + weldid + gatherid + junctionid + welderins + junctionins + ins + itemins + weldmodel + status + electricity + voltage + setelectricity + setvoltage + timesql + maxelectricity + minelectricity + maxvoltage + minvoltage + channel + speed + workmodel + gasfare;
						}catch(Exception e){
							System.out.println(str);
							System.out.println(str.substring(76+a, 84+a));
						}
					}
					
					//MQTT处理
					mqtt.publishMessage("realdata", strsend, 0);
					strsend = "";
					
					//基本版2处理
					/*HashMap<String, SocketChannel> socketlist_cl;
					synchronized (websocketlist){
						socketlist_cl = (HashMap<String, SocketChannel>) websocketlist.clone();
					}
					ArrayList<String> listarraybuf = new ArrayList<String>();
					boolean ifdo = false;

					Iterator<Entry<String, SocketChannel>> webiter = socketlist_cl.entrySet().iterator();
					while(webiter.hasNext())
					{
						try{
							Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
							websocketfail = entry.getKey();
							SocketChannel websocketcon = entry.getValue();
							websocketcon.writeAndFlush(new TextWebSocketFrame(strsend)).sync();

						}catch (Exception e) {
							listarraybuf.add(websocketfail);
							ifdo = true;
						}
					}

					if(ifdo){
						synchronized (websocketlist){
							//socketlist_cl = (HashMap<String, SocketChannel>) socketlist.clone();
							for(int i=0;i<listarraybuf.size();i++){
								websocketlist.remove(listarraybuf.get(i));
							}
						}

					}*/
					
					
					//测试处理
					/*
					 * HashMap<String, SocketChannel> socketlist_cl; synchronized (websocketlist){
					 * socketlist_cl = (HashMap<String, SocketChannel>) websocketlist.clone(); }
					 * ArrayList<String> listarraybuf = new ArrayList<String>(); boolean ifdo =
					 * false;
					 * 
					 * Iterator<Entry<String, SocketChannel>> webiter =
					 * socketlist_cl.entrySet().iterator(); while(webiter.hasNext()) { try{
					 * Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>)
					 * webiter.next(); websocketfail = entry.getKey(); SocketChannel websocketcon =
					 * entry.getValue(); if(websocketcon.isActive() && websocketcon.isOpen()){
					 * websocketcon.writeAndFlush(new TextWebSocketFrame(strsend)).sync(); }else{
					 * listarraybuf.add(websocketfail); }
					 * 
					 * }catch (Exception e) { listarraybuf.add(websocketfail); ifdo = true; } }
					 * 
					 * if(ifdo){ synchronized (websocketlist){ //socketlist_cl = (HashMap<String,
					 * SocketChannel>) socketlist.clone(); for(int i=0;i<listarraybuf.size();i++){
					 * websocketlist.remove(listarraybuf.get(i)); } }
					 * 
					 * }
					 */
					
					/*synchronized (websocketlist) {

						ArrayList<String> listarraybuf = new ArrayList<String>();
						boolean ifdo= false;

						Iterator<Entry<String, SocketChannel>> webiter = websocketlist.entrySet().iterator();
						while(webiter.hasNext()){
							try{
								Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
								websocketfail = entry.getKey();
								SocketChannel websocketcon = entry.getValue();
								websocketcon.writeAndFlush(new TextWebSocketFrame(strsend)).sync();
							}catch (Exception e) {
								listarraybuf.add(websocketfail);
								ifdo = true;
							}
						}

						if(ifdo){
							for(int i=0;i<listarraybuf.size();i++){
								websocketlist.remove(listarraybuf.get(i));
							}
						}
						strsend = "";
					}*/
				}
			}
		}
	}

	public void Websocketohwh(String str, ArrayList<String> listarray22, ArrayList<String> listarray32,
			HashMap<String, SocketChannel> websocketlist2) {
		// TODO Auto-generated method stub
		Date time;
		Timestamp timesql = null;

		if(websocketlist2==null || websocketlist2.isEmpty()){
		}
		else
		{
			String welderid = Integer.valueOf(str.substring(70,74)).toString();
			if(welderid.length()!=4){
				int lenth=4-welderid.length();
				for(int i=0;i<lenth;i++){
					welderid="0"+welderid;
				}
			}
			String weldid = Integer.valueOf(str.substring(20,24)).toString();
			if(weldid.length()!=4){
				int lenth=4-weldid.length();
				for(int i=0;i<lenth;i++){
					weldid="0"+weldid;
				}
			}
			String gatherid = Integer.valueOf(str.substring(16, 20)).toString();
			if(gatherid.length()!=4){
				int lenth=4-gatherid.length();
				for(int i=0;i<lenth;i++){
					gatherid="0"+gatherid;
				}
			}
			String itemins = Integer.valueOf(str.substring(316,318)).toString();
			if(itemins.length()!=4){
				int lenth=4-itemins.length();
				for(int i=0;i<lenth;i++){
					itemins="0"+itemins;
				}
			}
			String weldmodel = Integer.valueOf(str.subSequence(42, 44).toString(),16).toString();
			if(weldmodel.length()!=4){
				int lenth=4-weldmodel.length();
				for(int i=0;i<lenth;i++){
					weldmodel="0"+weldmodel;
				}
			}

			String strsend = "";
			for(int a=0;a<161;a+=80){
				try{
					String welderins = "0000";
					String junctionins = "0000";
					String ins = "0000";

					String junctionid = Integer.valueOf(str.substring(106+a, 114+a)).toString();
					if(junctionid.length()!=4){
						int lenth=4-junctionid.length();
						for(int i=0;i<lenth;i++){
							junctionid="0"+junctionid;
						}
					}
					String electricity = Integer.valueOf(str.subSequence(86+a, 90+a).toString(),16).toString();
					if(electricity.length()!=4){
						int lenth=4-electricity.length();
						for(int i=0;i<lenth;i++){
							electricity="0"+electricity;
						}
					}
					String voltage = Integer.valueOf(str.subSequence(90+a, 94+a).toString(),16).toString();
					if(voltage.length()!=4){
						int lenth=4-voltage.length();
						for(int i=0;i<lenth;i++){
							voltage="0"+voltage;
						}
					}
					String speed = Integer.valueOf(str.subSequence(94+a, 98+a).toString(),16).toString();
					if(speed.length()!=4){
						int lenth=4-speed.length();
						for(int i=0;i<lenth;i++){
							speed="0"+speed;
						}
					}
					String setelectricity = Integer.valueOf(str.subSequence(98+a, 102+a).toString(),16).toString();
					if(setelectricity.length()!=4){
						int lenth=4-setelectricity.length();
						for(int i=0;i<lenth;i++){
							setelectricity="0"+setelectricity;
						}
					}
					String setvoltage = Integer.valueOf(str.subSequence(102+a, 106+a).toString(),16).toString();
					if(setvoltage.length()!=4){
						int lenth=4-setvoltage.length();
						for(int i=0;i<lenth;i++){
							setvoltage="0"+setvoltage;
						}
					}
					String status = Integer.valueOf(str.subSequence(114+a, 116+a).toString(),16).toString();
					if(status.length()!=2){
						int lenth=2-status.length();
						for(int i=0;i<lenth;i++){
							status="0"+status;
						}
					}

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

					String channel = Integer.valueOf(str.subSequence(136+a, 138+a).toString(),16).toString();
					if(channel.length()!=4){
						int lenth=4-channel.length();
						for(int i=0;i<lenth;i++){
							channel="0"+channel;
						}
					}
					String maxelectricity = Integer.valueOf(str.subSequence(120+a, 124+a).toString(),16).toString();
					if(maxelectricity.length()!=4){
						int lenth=4-maxelectricity.length();
						for(int i=0;i<lenth;i++){
							maxelectricity="0"+maxelectricity;
						}
					}
					String minelectricity = Integer.valueOf(str.subSequence(124+a, 128+a).toString(),16).toString();
					if(minelectricity.length()!=4){
						int lenth=4-minelectricity.length();
						for(int i=0;i<lenth;i++){
							minelectricity="0"+minelectricity;
						}
					}
					String maxvoltage = Integer.valueOf(str.subSequence(128+a, 132+a).toString(),16).toString();
					if(maxvoltage.length()!=4){
						int lenth=4-maxvoltage.length();
						for(int i=0;i<lenth;i++){
							maxvoltage="0"+maxvoltage;
						}
					}
					String minvoltage = Integer.valueOf(str.subSequence(132+a, 136+a).toString(),16).toString();
					if(minvoltage.length()!=4){
						int lenth=4-minvoltage.length();
						for(int i=0;i<lenth;i++){
							minvoltage="0"+minvoltage;
						}
					}

					for(int i=0;i<listarray1.size();i+=3){
						if(Integer.valueOf(welderid) == Integer.valueOf(listarray1.get(i))){
							welderins = listarray1.get(i+2);
							if(welderins.equals(null) || welderins.equals("null")){
								break;
							}else{
								if(welderins.length()!=4){
									int lenth=4-welderins.length();
									for(int i1=0;i1<lenth;i1++){
										welderins="0"+welderins;
									}
								}
								break;
							}
						}
					}

					for(int i=0;i<listarray32.size();i+=7){
						if(Integer.valueOf(junctionid) == Integer.valueOf(listarray32.get(i+5))){
							junctionins = listarray32.get(i+6);
							if(junctionins.equals(null) || junctionins.equals("null")){
								break;
							}else{
								if(junctionins.length()!=4){
									int lenth=4-junctionins.length();
									for(int i1=0;i1<lenth;i1++){
										junctionins="0"+junctionins;
									}
								}
								break;
							}
						}
					}

					for(int i=0;i<listarray22.size();i+=4){
						if(Integer.valueOf(gatherid) == Integer.valueOf(listarray22.get(i))){
							ins = listarray22.get(i+3);
							if(ins == null || ins.equals("null")){
								break;
							}else{
								if(ins.length()!=4){
									int lenth=4-ins.length();
									for(int i1=0;i1<lenth;i1++){
										ins="0"+ins;
									}
								}
								break;
							}
						}
					}

					if(ins == null || ins.equals("null")){
						ins = "0000";
					}
					if(junctionins.equals(null) || junctionins.equals("null")){
						junctionins = "0000";
					}
					if(welderins.equals(null) || welderins.equals("null")){
						welderins = "0000";
					}

					//南通去除报警
					/*if(status.equals("98") || status.equals("99")){
            	status = "03";
            }*/

					//strsend = strsend + welderid + weldid + gatherid + junctionid + welderins + junctionins + ins + itemins + weldmodel + status + electricity + voltage + setelectricity + setvoltage + timesql + maxelectricity + minelectricity + maxvoltage + minvoltage + channel;
					strsend  = strsend + welderid + weldid + gatherid + junctionid + welderins + junctionins + ins + itemins + weldmodel + status + electricity + voltage + setelectricity + setvoltage + timesql + maxelectricity + minelectricity + maxvoltage + minvoltage + channel + speed;
				}catch(Exception e){
					System.out.println(str);
					System.out.println(str.substring(76+a, 84+a));
				}
			}
			synchronized (websocketlist2) {

				ArrayList<String> listarraybuf = new ArrayList<String>();
				boolean ifdo= false;

				Iterator<Entry<String, SocketChannel>> webiter = websocketlist2.entrySet().iterator();
				while(webiter.hasNext()){
					try{
						Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
						websocketfail = entry.getKey();
						SocketChannel websocketcon = entry.getValue();
						websocketcon.writeAndFlush(new TextWebSocketFrame(strsend)).sync();
					}catch (Exception e) {
						listarraybuf.add(websocketfail);
						ifdo = true;
					}
				}

				if(ifdo){
					for(int i=0;i<listarraybuf.size();i++){
						websocketlist2.remove(listarraybuf.get(i));
					}
				}
				strsend = "";
			}
		}
	}

}
