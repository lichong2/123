package com.dingfeng.qiniu;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class Test {
	public static void main(String[] args) {
		String sql = "SELECT icon FROM `wz_code` where icon is not null and icon != '' ";
		Connection connection = DBUtil.getConn();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			try {
				while (rs.next()) {
					String icon = rs.getString("icon");
					QiNiu.downLoad(icon, "http://image.leyinglife.com/goodsTitleType", "F:\\yunShangImage\\wzCodeIcon");
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBUtil.close(connection, preparedStatement, preparedStatement, null);
		}
	}
	
	/*public static void main(String[] args) {
		List<Map<String, Object>> fileList =  QiNiu.getFiles("F:\\yunShangImage\\propertiy");
		int n = 0;
		for (Map<String,Object> map : fileList) {
			File file = (File)map.get("file");
			String fileName = map.get("fileName").toString();
			fileName = fileName.replace("--", "/");
			boolean flag = QiNiu.upload(file, fileName);
			if (flag) {
				n++;
			}
		}
		System.out.println(n);
	}*/
}
