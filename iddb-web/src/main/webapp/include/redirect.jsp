<%@page import="iddb.core.util.SystemProperties"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String build;
	try {
    	build = SystemProperties.applicationVersion().getBuild();
	} catch (Exception e) {
		build = "1";
	}
	pageContext.setAttribute("buildNumber", build);
%>
<head>
<title>IPDB Sudamericana</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<meta name="description" content="description"/>
<meta name="keywords" content="keywords"/> 
<meta name="author" content="author"/> 
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="-1" />
<meta http-equiv="cache-control" content="no-cache" />
<link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/default${buildNumber}.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/site${buildNumber}.css" media="screen"/>
</head>
<body>
<div class="container">
<div id="context-loader" class="context-loader" style="width: 225px;">Aguarda mientras te redirigimos.</div>
</div>
<script type="text/javascript">
	window.location = "<c:out value="${redirect}"/>";
</script>
</body>
</html>