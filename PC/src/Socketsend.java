import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Socketsend implements Callback{

	private Socket socket;

	@Override
	public void taskResult(String str,String connet,ArrayList<String> listarray1,ArrayList<String> listarray2,ArrayList<String> listarray3,HashMap<String, Socket> websocket,String ip1) {
		// TODO Auto-generated method stub
		
		try{
				
            if (str.length() == 108) {  

            //У���һλ�Ƿ�ΪFAĩλ�Ƿ�ΪF5
       	     String check1 =str.substring(0,2);
       	     String check11=str.substring(106,108);
       	     if(check1.equals("FA") && check11.equals("F5")){
	        		
           	     //У�鳤��
           	     int check2=str.length();
           	     if(check2==108){
           	        			
               	     //У��λУ��
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
               	    	 
               	        OutputStream outputStream;
               	    	 
               	    	byte[] data=new byte[str.length()/2];

						for (int i = 0; i < data.length; i++)
						{
							String tstr1=str.substring(i*2, i*2+2);
							Integer k=Integer.valueOf(tstr1, 16);
							data[i]=(byte)k.byteValue();
						}
               	    	 
               	    	String strdata = "";
					for(int i=0;i<data.length;i++){
                         	
                         	//�ж�Ϊ���ֻ�����ĸ����Ϊ��ĸ+256ȡ����
                         	if(data[i]<0){
                         		String r = Integer.toHexString(data[i]+256);
                         		String rr=r.toUpperCase();
                             	//���ֲ�Ϊ��λ��
                             	if(rr.length()==1){
                         			rr='0'+rr;
                             	}
                             	//strdataΪ�ܽ�������
                         		strdata += rr;
                         		
                         	}
                         	else{
                         		String r = Integer.toHexString(data[i]);
                             	if(r.length()==1)
                         			r='0'+r;
                             	r=r.toUpperCase();
                         		strdata+=r;	
                         		
                         	}
                         }

                         
                        byte[] bb3=new byte[strdata.length()/2];
     					for (int i1 = 0; i1 < bb3.length; i1++)
     					{
     						String tstr1=strdata.substring(i1*2, i1*2+2);
     						Integer k=Integer.valueOf(tstr1, 16);
     						bb3[i1]=(byte)k.byteValue();
     					}
     					
     					try {
							socket = new Socket(ip1, 5555);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
                        
					
	                    try {
	                    	//������Ϣ
	                        // ����1����Socket ������������OutputStream
	                        // �ö������ã���������
	                        outputStream = socket.getOutputStream();
	
	                        // ����2��д����Ҫ���͵����ݵ������������
	                        outputStream.write(bb3);
	                        // �ر�ע�⣺���ݵĽ�β���ϻ��з��ſ��÷������˵�readline()ֹͣ����
	
	                        // ����3���������ݵ������
	                        outputStream.flush();
	
	                        socket.close();
	                        
	                        str="";
	     		           
	                    } catch (IOException e1) {
	                    	str="";
	                        e1.printStackTrace();
	                    }
               	    	 
               	     }
               	     else{
               	        //У��λ����
               	    	 System.out.print("���ݽ���У��λ����");
               	    	 str="";
               	     }
                               
           	     }
           	        		
           	     else{
           	        //���ȴ���
           	    	 System.out.print("���ݽ��ճ��ȴ���");
           	    	 str="";
           	     }
       	        		
   	        	}
   	        	else{
   	        		//��λ����FE
   	        		System.out.print("���ݽ�����ĩλ����");
   	        		str="";
   	        	}
       	     
           }else {
	        	   str="";
	               System.out.println("Not receiver anything from client!");  
	           }
            
		} catch (Exception e) {
			str="";
            System.out.println("S: Error 2");  
            e.printStackTrace();  
        }  
		
	}

}
