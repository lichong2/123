package com.dingfeng.qiniu;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class Test {
	
	/*
	 *
	 	//数据库导出相应的数据从乐盈七牛上传到云商七牛
	 * public static void main(String[] args) {
		String sql = " select ACTIVITY_ID, icon from store_activity_info where   icon is not null and icon != '' ";
		Connection connection = DBUtil.getConn();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			try {
				int i = 1;
				while (rs.next()) {
					String icon = rs.getString("icon");
					icon =  icon.substring(1, icon.length());
					QiNiu.uploadFromNet(icon, "http://image.leyinglife.com/");
					System.out.println(rs.getLong("ACTIVITY_ID")+"_______"+ i++);
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBUtil.close(connection, preparedStatement, preparedStatement, null);
		}
	}*/
	
	/*
	 	//把本地件遍历上传到云商七牛
	 * public static void main(String[] args) {
		List<Map<String, Object>> fileList =  QiNiu.getFiles("F:\\yunShangImage\\wzCodeIcon");
		int n = 0;
		for (Map<String,Object> map : fileList) {
			File file = (File)map.get("file");
			String fileName = map.get("fileName").toString();
			fileName = fileName.replace("--", "/");
			fileName = "/" + "goodsTitleType" + fileName;
			boolean flag = QiNiu.upload(file, fileName);
			if (flag) {
				n++;
			}
		}
		System.out.println(n);
	}
	
	遍历空间七牛空间下的所有文件，修改文件名称
	public static void main(String[] args) {
		List<Map<String, Object>> list = QiNiu.getQiNiuBucketFileList("/", "");
		int n = 1;
		for (Map<String, Object> map : list) {
			String fromKey = map.get("key").toString();
			String toKey = fromKey.substring(1, fromKey.length());
			QiNiu.removeAndRename(fromKey, toKey);
			System.out.println(fromKey + "_________" + n++);
		}
		
	}*/
	
	
}
