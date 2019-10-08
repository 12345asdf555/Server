package com.main.test;

import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import com.alibaba.fastjson.JSONArray;
import com.greatway.util.IsnullUtil;

import net.sf.json.JSONObject;

public class MainTest {

	public static void main(String[] args) {
		IsnullUtil iutil = new IsnullUtil();
		//客户端执行操作
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://localhost:8080/CIWJN_Service/cIWJNWebService?wsdl");
		iutil.Authority(client);
		String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"getWeldedJunctionAll\"}";
		
/*		String obj1 = "{\"CLASSNAME\":\"welderWebServiceImpl\",\"METHOD\":\"getWelderByNum\"}";
		String obj2 = "{\"WELDERNO\":\"0001\"}";*/
		
//		String obj1 = "{\"CLASSNAME\":\"weldingMachineWebServiceImpl\",\"METHOD\":\"getWeldingMachineAll\"}";
		try {
			Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterNoParamWs"),
					new Object[] { obj1 });
			
/*			Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"),
					new Object[] { obj1,obj2 });*/
			
/*		   Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterNoParamWs"),
					new Object[] { obj1 });*/
		   String restr = objects[0].toString();
	       JSONArray ary = JSONArray.parseArray(restr);
	       for(int i=0;i<ary.size();i++){
	       String str = ary.getString(i);
	       JSONObject js = JSONObject.fromObject(str);
	       }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
