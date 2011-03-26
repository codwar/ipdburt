<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri='/WEB-INF/tld/template.tld' prefix='template' %>
<%@ page import="com.google.appengine.api.users.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>IPDB Sudamericana</title>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1"/>
<meta name="description" content="description"/>
<meta name="keywords" content="keywords"/> 
<meta name="author" content="author"/> 
<link rel="stylesheet" type="text/css" href="/media/styles.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="/media/jquery.place.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="/media/tipTip.css" media="screen"/>
<script type="text/javascript" src="/media/js/jquery-1.5.1.min.js"></script>
<script type="text/javascript" src="/media/js/jquery.measure.js"></script>
<script type="text/javascript" src="/media/js/jquery.place.js"></script>
<script type="text/javascript" src="/media/js/jquery.pulse.js"></script>
<script type="text/javascript" src="/media/js/jquery.loading.js"></script>
<script type="text/javascript" src="/media/js/jquery.tiptip.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
    $('body').ajaxStart(function() {
        $.loading(true, {text: 'Cargando ...'});
    }); 
    $('body').ajaxStop(function() {
        $.loading(false);      
    });
});
</script>
</head>
<body>

<jsp:include page="/infoservlet" />

<div class="beta">
	<img src="/media/images/bimage.png">
</div>
<div class="top">
				
	<div class="header">
		<div class="left">
			<a href="/">IPDB Sudamericana</a>
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
        <a href="/contact.jsp">Contacto</a>
		<div class="clearer"><span></span></div>
	</div>

	<div class="main">
	   <template:get name='content'/>
	</div>
	
    <div class="footer">
    
        <div class="left">&copy; 2011 Shonaka & SGT. Based on the idea of lakebodom. v${app.version}</div>
        <div class="right"><a href="http://templates.arcsin.se/">Website template</a> by <a href="http://arcsin.se/">Arcsin</a></div>

        <div class="clearer"><span></span></div>

    </div>

</div>

</body>

</html>