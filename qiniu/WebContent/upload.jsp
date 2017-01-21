<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>上传文件到七牛</title>
</head>
<body>
	<form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/UploadServlet?method=upload">
		<input type="file" id="atta" name="atta">
		<input type="submit" value="提交">
	</form>
</body>
</html>