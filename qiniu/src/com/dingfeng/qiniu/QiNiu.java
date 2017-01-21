package com.dingfeng.qiniu;

import java.io.File;

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
    private final String ACCESS_KEY = "Pq4wW5g2FpxeRoQGt3SL-8q75TGkAyAFzN9r3xo5";
    private final String SECRET_KEY = "ZI9joqZULL4m7OcEiNGwQXPbMI35PO-lGFHkFRmQ";
    //要上传的空间
    private final String bucketname = "image";
    
    /**
     * 上传文件到七牛
     * @param file 文件    fileName 要上传到七牛的文件名
     * @return flag  true上传成功    false上传失败
     */
    public boolean upload(File file, String fileName){
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
    public boolean upload(byte[] data, String fileName){
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
}
