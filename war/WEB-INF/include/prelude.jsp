<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page import="com.google.appengine.api.users.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1"/>
<meta name="description" content="description"/>
<meta name="keywords" content="keywords"/> 
<meta name="author" content="author"/> 
<link rel="stylesheet" type="text/css" href="/media/styles.css" media="screen"/>
<title>IPDBS Sudamericana</title>
<script language="javascript" type="text/javascript" src="/scripts/jquery-1.5.1.min.js"></script>
</head>

<body>
<div class="beta">
	<img src="/media/images/bimage.png">
</div>
<div class="top">
				
	<div class="header">
		<div class="left">
			<a href="/">IPDBS Sudamericana</a>
		</div>
		 		
		<div class="right">
 		</div>

	</div>	

</div>

<div class="container">	
<div style="padding: 2px 10px 8px 10px; text-align: right">
<%
	UserService userService = UserServiceFactory.getUserService();

	if (request.getUserPrincipal() != null) {
		out.write(request.getUserPrincipal().getName());
		out.write(" | <a href=\"");
		out.write(userService.createLogoutURL(request.getRequestURI()));
		out.write("\">logout</a>");
	} else {
		out.write("<a href=\"");
		out.write(userService.createLoginURL(request.getRequestURI()));
		out.write("\">login</a>");
	}
%>
</div>


	<div class="navigation">
	    <a href="/">Inicio</a>
	    <a href="/serverlist.jsp">Servidores</a>
        <a href="/admin/serverlist.jsp">Administrar</a>
        <a href="#">Contacto</a>
		<div class="clearer"><span></span></div>
	</div>

	<div class="main">