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
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
/**
 * 七牛上传的配置
 * @author Administrator
 *
 */

public class QiNiu {
	 //设置好账号的ACCESS_KEY和SECRET_KEY
    private static final String ACCESS_KEY = "Pq4wW5g2FpxeRoQGt3SL-8q75TGkAyAFzN9r3xo5";
    private static final String SECRET_KEY = "ZI9joqZULL4m7OcEiNGwQXPbMI35PO-lGFHkFRmQ";
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
	public static List<Map<String, Object>> getLocalFiles(String filePath) {
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
	
	
	/**
     * 下载文件到本地
     * @param fileName	七牛文件名称
     * @param domain	七牛域名
     * @throws Exception 
     */
    public static void uploadFromNet(String fileName, String domain) throws Exception {
		try {
			String encodedFileName = URLEncoder.encode(fileName, "utf-8");
			String finalUrl = String.format("%s/%s", domain, encodedFileName);
			//new一个URL对象  
	        URL url = new URL(finalUrl);  
	        //打开链接  
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        //设置请求方式为"GET"  
	        conn.setRequestMethod("GET");  
	        //超时响应时间为3秒  
	        conn.setConnectTimeout(3 * 1000);
	        if (conn.getResponseCode() == 200) {
	        	//通过输入流获取图片数据  
	        	InputStream inStream = conn.getInputStream();  
	        	//得到图片的二进制数据，以二进制封装得到数据，具有通用性  
	        	byte[] data = readInputStream(inStream);
	        	//上传七牛
	        	upload(data, fileName);
	        }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 获取空间下的文件列表
     * @param prefix	文件名前缀
     * @param delimiter	指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
     * @return	key：文件名称		
     */
    public static List<Map<String, Object>> getQiNiuBucketFileList(String prefix, String delimiter) {
    	List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
    	//构造一个带指定Zone对象的配置类
    	Configuration cfg = new Configuration(Zone.zone0());
    	//...其他参数参考类注释
    	Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    	BucketManager bucketManager = new BucketManager(auth, cfg);
    	//每次迭代的长度限制，最大1000，推荐值 1000
    	int limit = 1000;
    	//指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
    	BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucketname, prefix, limit, delimiter);
    	while (fileListIterator.hasNext()) {
    	    //处理获取的file list结果
    	    FileInfo[] items = fileListIterator.next();
    	    for (FileInfo item : items) {
    	    	Map<String, Object> fileMap = new HashMap<String, Object>();
    	    	fileMap.put("key", item.key);
    	    	fileMap.put("hash", item.hash);
    	    	fileList.add(fileMap);
    	    }
    	}
    	return fileList;
    }
    
    /**
     * 移动操作本身支持移动文件到相同，不同空间中，在移动的同时也可以支持文件重命名。唯一的限制条件是，移动的源空间和目标空间必须在同一个机房。
     * @param fromKey	原文件名
     * @param toKey		新文件名
     * @return
     */
    public static void removeAndRename(String fromKey, String toKey) {
    	//构造一个带指定Zone对象的配置类
    	Configuration cfg = new Configuration(Zone.zone0());
    	Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    	BucketManager bucketManager = new BucketManager(auth, cfg);
    	try {
    		bucketManager.move(bucketname, fromKey, bucketname, toKey);
    	} catch (QiniuException ex) {
    		 System.err.println(ex.code());
    		 System.err.println(ex.response.toString());
    	}
    }
    
    /**
     * 把七牛某个空间下的文件荡到本地指定文件夹
     * @param prefix		空间指定文件前缀
     * @param delimiter		指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
     * @param localUrl		本地指定文件夹
     * @param domain		访问七牛某空间下域名
     */
    public static void getQiNiuBucketFileToLocal(String prefix, String delimiter, String localUrl, String domain) {
    	try {
	    	//获取七牛下所有文件的文件列表
	    	List<Map<String , Object>> fileList = getQiNiuBucketFileList(prefix, delimiter);
	    	if (!fileList.isEmpty()) {
	    		//获取本机电脑指定文件夹下所有文件的文件内容
	    		List<Map<String, Object>> localList = getLocalFiles(localUrl);
	    		for(Map<String, Object> fileMap : fileList) {
	    			String fileName = fileMap.get("key").toString();
	    			boolean flag = true;
	    			for (Map<String, Object> localMap : localList) {
	    				String localFileName = localMap.get("fileName").toString();
	    				//转换文件的名称--转为/
	    				localFileName = localFileName.replace("--", "/");
	    				if (fileName.equals(localFileName)) {
	    					flag = false;
	    					break;
	    				}
	    			}
	    			if (flag) {
						downLoad(fileName, domain, localUrl);
	    			}
	    		}
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static void main(String[] args) {
    	getQiNiuBucketFileToLocal("", "", "F://yunShangImage//test", "http://ojlkk6k4w.bkt.clouddn.com/");
	}
}
