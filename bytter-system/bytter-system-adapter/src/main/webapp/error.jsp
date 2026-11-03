<%@ page import="org.apache.log4j.Logger" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
    final Logger logger = Logger.getRootLogger();
    logger.error(exception.getMessage(), exception);
%>
<html>
<head>
    <title>异常</title>
</head>
<body>
系统发生异常，已记录异常信息！
</body>
</html>