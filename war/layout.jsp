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
<link rel="search" type="application/opensearchdescription+xml" href="/opensearch.xml" title="Buscar en IPDB" />
<url rel="self" type="application/opensearchdescription+xml" template="/opensearch.xml"/>
<link rel="shortcut icon" href="/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="/media/default.css" media="screen"/>
<!-- link rel="stylesheet" type="text/css" href="/media/jquery.place.css" media="screen"/-->
<link rel="stylesheet" type="text/css" href="/media/message.place.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="/media/tipTip.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="/media/styles/menu.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="/media/site.css" media="screen"/>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>
<script type="text/javascript" src="/media/js/jquery.measure.js"></script>
<script type="text/javascript" src="/media/js/jquery.place.js"></script>
<script type="text/javascript" src="/media/js/jquery.pulse.js"></script>
<script type="text/javascript" src="/media/js/jquery.loading.js"></script>
<script type="text/javascript" src="/media/js/jquery.tiptip.min.js"></script>
<script type="text/javascript" src="/media/js/jquery.floatobject-1.4.js"></script>
<script type="text/javascript" src="/media/styles/menu.js"></script>
<script type="text/javascript">
function showContextLoader() {
    $.loading(true, {text: 'Enviando solicitud...', loadingClass: 'context-loader', update: {texts: ['Por favor, aguarde...', 'Por favor reintente.']}});
}
$(document).ready(
		function() {
		    $('body').ajaxStart(function() {
		        showContextLoader();
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
			$(".focus").each(
					function() {
						$(this).focus();
						$(this).select();
					}
			);
            $(window).unload( function () { showContextLoader(); } );
            $(".fetch-server").each(function() {
            	data = {key: $(this).attr("alt")};
            	$.post("/app/fetchserver",data, function(d) {
            		rs = ($.parseJSON(d));
            		if (rs.error) {
            			$(".fetch-server[alt="+rs.key+"]").attr("src","/media/images/exclamation.png");
            		} else {
            			$(".fetch-server[alt="+rs.server.key+"]").replaceWith(rs.server.count);
            		}
            	});
            });
});
</script>
</head>
<body>
<div style="display: none;">
<!-- preload image -->
<img src='/media/images/metabox_loader.gif'/>
</div>
<div id="topnavigation">
    <ul class="topnav">
    <div id="logo">
    <a href="/">IPDB</a>
    </div>
        <li><form id="search-form" method="get">
        <small><!-- span class="glass"><i></i></span--><input placeholder="Ingrese alias o IP a buscar" class="search focus" type="text" name="q" value="${queryValue}" style="margin-top: 8px;"/></small>
        </form>
        </li>    
        <li><a href="/">Inicio</a></li>
        <li><a href="/search.jsp?t=ban">Baneados</a></li>
        <li><a href="/serverlist.jsp">Servidores</a></li>
        <%
            UserService userService = UserServiceFactory.getUserService();
            if (request.getUserPrincipal() != null) {
        %>
        <li><a href="/admin/serverlist.jsp">Administrar</a></li>
        <%      
            }
        %>
        <li>  
            <span class="subnav">Ayuda</span>
            <ul class="subnav">
            	<li><a href="/contact.jsp">Contacto</a></li>
            	<li><a href="/faq.jsp">FAQ</a></li>
            	<li><a target="_blank" href="http://arg.urbanterror.com.ar">Libro de quejas</a></li>
            </ul>  
        </li>
        <li style="float: right; margin-right: 20px;">
            <div id="session">
            <%
            if (request.getUserPrincipal() != null) {
                out.write("<a href=\""+userService.createLogoutURL(request.getRequestURI())+"\" id=\"signin-link\"><em>"+request.getUserPrincipal().getName()+"</em><strong>Desconectar</strong><i class=\"signout\"></i></a>");
            } else {
                out.write("<a href=\""+userService.createLoginURL(request.getRequestURI())+"\" id=\"signin-link\"><em>&iquest;Tienes una cuenta?</em><strong>Identificarse</strong><i></i></a>");
            }
            %>
            </div>                
        </li>
    </div>
    </ul>
</div>

<div class="beta">
	<img src="/media/images/bimage.png" width="105" height="107"/>
</div>

<div id="donar">
<span style="padding-top: 4px;text-decoration: blink; color: red;">Consider&aacute; colaborar</span>
<div id="dineromail" style="text-align: center; padding-top: 5px;">
<a href="/donar.jsp"><img width="74" height="19" src='https://argentina.dineromail.com/imagenes/botones/donar_c.gif' border='0' name='submit' alt='Donar con DineroMail'/></a>
</div>
</div>

<div class="container">	

<div class="clearer"><span></span></div>
<div id="context-loader" class="context-loader">Cargando&hellip;</div>
<div id="main" class="main" style="display: none;">

<template:get name='content' />

<div class="footer">
     <jsp:include page="/infoservlet" />
     <div class="left">&copy; 2011 Shonaka & SGT. Based on the idea of lakebodom. v${app.version}. Times are displayed in UTC-3.</div>
     <div class="clearer"><span></span></div>
 </div>
</div>

<div style="width: 728px; height: 90px; margin: 25px auto 2px;">
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
</div>
<script type="text/javascript">
$("#context-loader").hide();
$("#main").fadeIn("slow");
</script>
</body>

</html>
