package com.dingfeng.qiniu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
/**
 * 七牛上传的配置
 * @author Administrator
 *
 */

public class QiNiu {
	 //设置好账号的ACCESS_KEY和SECRET_KEY
    private static final String ACCESS_KEY = "AiURfAqvv4MbxOOZlttkEZhGnwNZduFM_yOik2EH";
    private static final String SECRET_KEY = "5WfUmQfx8CYXNkxyJUAbwfnBPAmIoq4WxLPA2wgT";
    //要上传的空间
    private static final String bucketname = "image";
    
    /**
     * 上传文件到七牛
     * @param file 文件    fileName 要上传到七牛的文件名
     * @return flag  true上传成功    false上传失败
     */
    public static boolean upload(File file, String fileName){
    	boolean flag = true;
    	 //要上传的空间(bucket)的存储区域为华东时
    	Zone z = Zone.zone0();
    	//密钥配置
    	Configuration c = new Configuration(z);
    	Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    	UploadManager uploadManager = new UploadManager(c);
    	try {
			Response response = uploadManager.put(file, fileName, auth.uploadToken(bucketname));
		} catch (QiniuException e) {
			flag = false;
			e.printStackTrace();
		}
    	return flag;
    }
    
    /**
     * 上传文件到七牛
     * @param data 文件字节数组    fileName 要上传到七牛的文件名
     * @return flag  true上传成功    false上传失败
     */
    public static boolean upload(byte[] data, String fileName){
    	boolean flag = true;
    	 //要上传的空间(bucket)的存储区域为华东时
    	Zone z = Zone.zone0();
    	//密钥配置
    	Configuration c = new Configuration(z);
    	Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    	UploadManager uploadManager = new UploadManager(c);
    	try {
			Response response = uploadManager.put(data, fileName, auth.uploadToken(bucketname));
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
    	return flag;
    }
    
    /**
     * 下载文件到本地
     * @param fileName	七牛文件名称
     * @param domain	七牛域名
     * @param localUrl	文件写到本地的路径
     * @throws Exception 
     */
    public static void downLoad(String fileName, String domain, String localUrl) throws Exception {
		try {
			String encodedFileName = URLEncoder.encode(fileName, "utf-8");
			String finalUrl = String.format("%s/%s", domain, encodedFileName);
			//new一个URL对象  
	        URL url = new URL(finalUrl);  
	        //打开链接  
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        if (conn != null) {
	        	
	        }
	        //设置请求方式为"GET"  
	        conn.setRequestMethod("GET");  
	        //超时响应时间为5秒  
	        conn.setConnectTimeout(3 * 1000);
	        if (conn.getResponseCode() == 200) {
	        	//通过输入流获取图片数据  
	        	InputStream inStream = conn.getInputStream();  
	        	//得到图片的二进制数据，以二进制封装得到数据，具有通用性  
	        	byte[] data = readInputStream(inStream);
	        	//创建指定路径
	        	File file = new File(localUrl);
	        	if (!file.exists()) {
	        		file.mkdirs();
	        	}
	        	fileName = fileName.replace("/", "--");
	        	//new一个文件对象用来保存图片，默认保存当前工程根目录  
	        	File imageFile = new File(localUrl+"/"+fileName);
	        	//创建输出流  
	        	FileOutputStream outStream = new FileOutputStream(imageFile);  
	        	//写入数据  
	        	outStream.write(data);  
	        	//关闭输出流  
	        	outStream.close();
	        }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * io流
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        //创建一个Buffer字符串  
        byte[] buffer = new byte[1024];  
        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
        int len = 0;  
        //使用一个输入流从buffer里把数据读取出来  
        while( (len=inStream.read(buffer)) != -1 ){  
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
            outStream.write(buffer, 0, len);
            outStream.flush();
        }  
        //关闭输入流  
        inStream.close();  
        //把outStream里的数据写入内存  
        return outStream.toByteArray();  
    }
    
    /**
     * 获取文件夹下所有文件的路径和文件名
     * @param filePath	文件夹路径
     * @return
     */
	public static List<Map<String, Object>> getFiles(String filePath) {
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			Map<String, Object> pathMap = new HashMap<String, Object>();
			if (!file.isDirectory()) {
				pathMap.put("fileName", file.getName());
				pathMap.put("url", file.getAbsolutePath());
				pathMap.put("file", file);
				fileList.add(pathMap);
			} 
		}
		return fileList;
	}
}
