package com.dingfeng.qiniu;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 上传文件的servlet
 * @author Administrator
 *
 */
public class UploadServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		if(method.equals("upload")){
			this.upload(request, response);
		}
	}
	
	public void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding("utf-8");
		try {
			//1创建ItemFactory
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//2request解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			//List<FileItem>
			List itemList = upload.parseRequest(request);
			for(int i = 0; i < itemList.size(); i++){
				FileItem fileItem = (FileItem) itemList.get(i);  //file
				if(!fileItem.isFormField()){
					//获取文件的名称
					String fileName = fileItem.getName();
					if(fileName != null && !fileName.equals("")){
						//判断是否是图片格式
						if(ImageUtils.checkFileIsImage(fileItem)){
							//获取文件的后缀名
							String suffixName = fileName.substring(fileName.lastIndexOf("."));
							//获取UUID名称中间去掉“-”
							UUID id = UUID.randomUUID();
							String prefixName = String.valueOf(id).replace("-", "");
							//获取时间日期
							Date currentTime = new Date();
							SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
							String dateString = formatter.format(currentTime);
							//获取新的名称
							String newName = dateString + "-" + prefixName + suffixName;
							QiNiu qiNiu = new QiNiu();
							byte[] data = ImageUtils.bufferImageToArrayByte(ImageUtils.getBufferImage(fileItem), suffixName.substring(1, suffixName.length()));
							boolean flag = qiNiu.upload(data, newName);
							if(flag){
								//把数据写入数据库
								request.setAttribute("message", "上传成功！！");
							}
						}else{
							request.setAttribute("message", "请上传格式为：jpeg,jpg,gif,png的图片  ！！");
						}
					}else{
						request.setAttribute("message", "请添加上传文件！！");
					}
				}
			}
			request.getRequestDispatcher("upload.jsp").forward(request, response);
			
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
