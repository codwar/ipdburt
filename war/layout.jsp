<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib uri='/WEB-INF/tld/template.tld' prefix='template' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.google.appengine.api.users.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>IPDB Sudamericana</title>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1"/>
<meta name="description" content="description"/>
<meta name="keywords" content="keywords"/> 
<meta name="author" content="author"/> 
<link rel="shortcut icon" href="/favicon.ico"/>
<link rel="search" type="application/opensearchdescription+xml" title="Buscar Alias" href="http://www.ipdburt.com.ar/searchbyalias.xml"/>
<url type="application/opensearchdescription+xml" rel="self" template="http://www.ipdburt.com.ar/searchbyalias.xml"/>
<link rel="stylesheet" type="text/css" href="/media/default.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="/media/jquery.place.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="/media/tipTip.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="/media/style.css" media="screen"/>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
<%-- script type="text/javascript" src="/media/js/jquery-1.5.1.min.js"></script--%>
<script type="text/javascript" src="/media/js/jquery.measure.js"></script>
<script type="text/javascript" src="/media/js/jquery.place.js"></script>
<script type="text/javascript" src="/media/js/jquery.pulse.js"></script>
<script type="text/javascript" src="/media/js/jquery.loading.js"></script>
<script type="text/javascript" src="/media/js/jquery.tiptip.min.js"></script>
<script type="text/javascript" src="/media/js/jquery.floatobject-1.4.js"></script>
<script type="text/javascript">
$(document).ready(
		function() {
		    $('body').ajaxStart(function() {
		        $.loading(true, {text: 'Cargando ...'});
		    }); 
		    $('body').ajaxStop(function() {
		        $.loading(false);      
		    });
			$('ul.messages li').each(
					function() {
						$(this).append(
								"<img class='close_button' src='/media/images/close.gif'/>");
					});
			$('.close_button').click(function() {
				$(this).parent().remove();
			});
			$("#donar").makeFloat({x: 'current', y: 'current', speed: 'fast'});
});
</script>
<style type="text/css">
#donar {
	position: absolute;
	top: 5px;
	left: 5px;
	padding: 5px;
	background-color: #B3C2C7;
	align: center;
	-webkit-box-shadow: 0 0 10px rgb(0,0,0);
	-moz-box-shadow: 0 0 10px rgb(0,0,0);
	box-shadow: 0 0 10px rgb(0,0,0);
}
</style>
</head>
<body>

<jsp:include page="/infoservlet" />

<div class="beta">
	<img src="/media/images/bimage.png" width="211" height="215"/>
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

<div id="donar" style="">
<span style="padding-top: 4px;text-decoration: blink; color: red;">Consider&aacute; colaborar</span>
<!--
<div id="paypal" style="text-align: center; padding-top: 5px;">
<span>usando PayPal</span>
<form action="https://www.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="L5SNT85KFQTJY">
<input type="image" src="https://www.paypalobjects.com/WEBSCR-640-20110306-1/es_XC/i/btn/btn_donate_SM.gif" border="0" name="submit" alt="PayPal. La forma rápida y segura de pagar en línea.">
<img alt="" border="0" src="https://www.paypalobjects.com/WEBSCR-640-20110401-1/en_US/i/scr/pixel.gif" width="1" height="1">
</form>
</div>
-->
<div id="dineromail" style="text-align: center; padding-top: 5px;">
<a href="/donar.jsp"><img width="74" height="19" src='https://argentina.dineromail.com/imagenes/botones/donar_c.gif' border='0' name='submit' alt='Donar con DineroMail'/></a>
</div>
</div>

<div class="container">	

<div style="padding: 2px 10px 8px 10px; text-align: right;">
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
<div class="clearer"><span></span></div>
	<div class="navigation">
	    <a href="/">Inicio</a>
	    <a href="/search.jsp?t=ban">Baneados</a>
	    <a href="/serverlist.jsp">Servidores</a>
        <a href="/admin/serverlist.jsp">Administrar</a>
        <a href="/contact.jsp">Contacto</a>
        <a href="/faq.jsp">FAQ</a>
		<div class="clearer"><span></span></div>
	</div>

<div class="main">
<template:get name='flash' />
<template:get name='content' />
</div>

<div style="width: 728px; height: 90px; margin: 5px auto 2px;">
<script type="text/javascript"><!--
google_ad_client = "ca-pub-6692965674688630";
/* IPDB */
google_ad_slot = "1813404437";
google_ad_width = 728;
google_ad_height = 90;
//-->
</script>
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>
</div>

<div class="footer">
    
        <div class="left">&copy; 2011 Shonaka & SGT. Based on the idea of lakebodom. v${app.version}</div>
        <div class="right"><a href="http://templates.arcsin.se/">Website template</a> by <a href="http://arcsin.se/">Arcsin</a></div>

        <div class="clearer"><span></span></div>

    </div>

</div>


</body>

</html>