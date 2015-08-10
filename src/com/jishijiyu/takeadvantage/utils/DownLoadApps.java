package com.jishijiyu.takeadvantage.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;

public class DownLoadApps {

	//根据链接地址进行下载
	public File getData(String url,String fileName)
            throws Exception {
        /* 取得URL */
        URL myURL = new URL(url);
        /* 创建连接 */
        HttpURLConnection conn=(HttpURLConnection) myURL.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        int mAllSize = conn.getContentLength() / 1024;
        int mReaded = 0;
        System.out.println("ContentLength:" + mAllSize + "KB");
        /* InputStream 下载文件 */
        System.out.println("conn.getResponseCode"+conn.getResponseCode());
        if(conn.getResponseCode()==200){
        	System.out.println("连接成功");
        	  InputStream is = conn.getInputStream();
        	  return writeSD(fileName, is);
        }else{
        	 throw new RuntimeException("连接失败");
        }   
    }
	
	//将下载下来的app安装包进行保存
	 public File writeSD(String fileName, InputStream input) {
	        File file = null;
	        OutputStream output = null;

	        try {
	           
//	        	if(! creatSDFile(fileName).exists()){
	        	     file = new File(Environment.getExternalStorageDirectory(),fileName);
	 	            output = new FileOutputStream(file);
	 	            byte buffer[] = new byte[1024 * 10];
	 	            int size = 0;
	 	            long readed = 0;
	 	            while ((size = input.read(buffer)) != -1) {
	 	                output.write(buffer, 0, size);
	 	                readed = readed + size;
	 	             
	 	               long mReaded = readed / 1024/1024;
	 	               System.out.println("文件大小为："+mReaded);
	 	            }
	 	            output.flush();
//	 	            }else{
//	 	            	System.out.println("文件已经存在，不需要下载");
//	 	            }
	 	        } catch (Exception e) {
	 	           
	 	            e.printStackTrace();
	 	        } finally {
	 	            try {
	 	                output.close();
	 	            } catch (Exception e) {
	 	                e.printStackTrace();
	 	            }
	 	        }
	        	
	       
	        return file;
	    }
	 
	 //判断文件是否存在
	 
	 public File creatSDFile(String fileName) throws IOException {
	        File file = new File(Environment.getExternalStorageDirectory().toString() + "/"+fileName);
	        if (file.exists()) {
	            file.delete();
	        }
	        file.createNewFile();
	        return file;
	    }

}
