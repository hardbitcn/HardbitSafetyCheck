package com.hardbit.hbsc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;
import android.util.Log;

public class FileUtils {
	String res="";   
	//���ļ�
	public String readSDFileOld(String fileName) throws IOException {  
			//�ļ�·��
	        File file = new File(fileName);  

	        FileInputStream fis = new FileInputStream(file);  

	        int length = fis.available(); 

	         byte [] buffer = new byte[length]; 
	         fis.read(buffer);     

	         res = EncodingUtils.getString(buffer, "UTF-8"); 

	         fis.close();     
	         return res;  
	}  
	public File createSDDir(String dirName) {
		  File dir = new File(dirName);
		  dir.mkdir();
		  return dir;
		 }
	public static byte[] readSDFile(String fileName) throws IOException {  

        File file = new File(fileName);  

        FileInputStream fis = new FileInputStream(file);  

        int length = fis.available(); 

         byte [] buffer = new byte[length]; 
         fis.read(buffer);    

         

         fis.close();     
         return buffer;  
}  
	 public boolean writeFileSdcard(String fileName,byte[] bytes){ 

	     try{ 

	      //FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);

	     FileOutputStream fout = new FileOutputStream(fileName);

	      //byte [] bytes = message.getBytes(); 

	      fout.write(bytes); 
	       fout.close(); 
	       return true;

	      } 

	     catch(Exception e){ 
	    	 Log.println(3, "alan","save file error:"+e);
	      return false;
	     } 

	 }
	//д�ļ�
	public void writeSDFile(String fileName, String write_str) throws IOException{  
		    //�ļ�·��
	        File file = new File(fileName);  

	        FileOutputStream fos = new FileOutputStream(file);  

	        byte [] bytes = write_str.getBytes(); 

	        fos.write(bytes); 

	        fos.close(); 
	} 
	public String getSDPath(){
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState()
		.equals(android.os.Environment.MEDIA_MOUNTED); //�ж�sd���Ƿ����
		if (sdCardExist)
		{
		sdDir = Environment.getExternalStorageDirectory();//��ȡ��Ŀ¼
		}
		return sdDir.toString();
		}
	
	public static void sqldb(String[] args)
	{
	try
	{
	//����SQLite��JDBC
	Class.forName("org.sqlite.JDBC");
	//����һ����ݿ���zieckey.db�����ӣ������ھ��ڵ�ǰĿ¼�´���֮
	Connection conn =DriverManager.getConnection("jdbc:sqlite:zieckey.db");
	Statement stat = conn.createStatement();
	stat.executeUpdate("create table tbl1(name varchar(20), salary int);");//����һ���?����
	stat.executeUpdate("insert into tbl1values('ZhangSan',8000);");//�������
	stat.executeUpdate("insert into tbl1values('LiSi',7800);");
	stat.executeUpdate("insert into tbl1values('WangWu',5800);");
	stat.executeUpdate("insert into tbl1values('ZhaoLiu',9100);");
	ResultSet rs = stat.executeQuery("select * from tbl1;");//��ѯ���
	while(rs.next()){//����ѯ������ݴ�ӡ����
	System.out.print("name = "+ rs.getString("name")+" ");//������һ
	System.out.println("salary = "+ rs.getString("salary"));//�����Զ�
	}
	rs.close();
	conn.close();//������ݿ������
	}
	catch(Exception e )
	{
	e.printStackTrace();
	}
	}
}
