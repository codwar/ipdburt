<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri='/WEB-INF/tlds/template.tld' prefix='template' %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1"/>
<meta name="description" content="description"/>
<meta name="keywords" content="keywords"/> 
<meta name="author" content="author"/> 
<link rel="stylesheet" type="text/css" href="media/styles.css" media="screen"/>
<title>IPDBS Sudamericana</title>
</head>

<body>

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

	<div class="navigation">
	    <a href="/">Inicio</a>
        <a href="serverlist.jsp">Servidores</a>
        <a href="#">Contacto</a>
		<div class="clearer"><span></span></div>
	</div>

	<div class="main">		
		 <template:get name='content'/>
	</div>

	<div class="footer">
	
		<div class="left">&copy; 2011 Shonaka & SGT. Based on the idea of lakebodom.</div>
		<div class="right"><a href="http://templates.arcsin.se/">Website template</a> by <a href="http://arcsin.se/">Arcsin</a></div>

		<div class="clearer"><span></span></div>

	</div>

</div>

</body>

</html>