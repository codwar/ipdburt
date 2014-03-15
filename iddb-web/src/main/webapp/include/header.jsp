<%@page import="iddb.core.util.SystemProperties"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<%
	String build;
	try {
    	build = SystemProperties.applicationVersion().getBuild();
	} catch (Exception e) {
		build = "1";
	}
	pageContext.setAttribute("buildNumber", build);
%>
<title>IPDB Sudamericana</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<meta name="description" content="description"/>
<meta name="keywords" content="keywords"/> 
<meta name="author" content="author"/>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="-1" />
<meta http-equiv="cache-control" content="no-cache" /> 
<link rel="search" type="application/opensearchdescription+xml" href="${pageContext.request.contextPath}/opensearch.xml" title="Buscar en IPDB" />
<url rel="self" type="application/opensearchdescription+xml" template="${pageContext.request.contextPath}/opensearch.xml"/>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico?${buildNumber}"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/default${buildNumber}.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/message.place${buildNumber}.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/tipTip${buildNumber}.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/styles/menu${buildNumber}.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/nm${buildNumber}.css" media="screen"/>
<!-- 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/selector/css/openid-shadow.css" />
 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/site${buildNumber}.css" media="screen"/>
<!-- script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script-->
<!-- script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script-->
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.nm.min${buildNumber}.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.measure${buildNumber}.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.place${buildNumber}.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.pulse${buildNumber}.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.loading${buildNumber}.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.tiptip.min${buildNumber}.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.floatobject-1.4${buildNumber}.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/styles/menu${buildNumber}.js"></script>
<!--
<script type="text/javascript" src="${pageContext.request.contextPath}/media/selector/js/openid-jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/selector/js/openid-es.js"></script>
-->
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/css_browser_selector${buildNumber}.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/encoder${buildNumber}.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/dutils${buildNumber}.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/base${buildNumber}.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/main${buildNumber}.js"></script>

<!--[if IE 6]>
    <script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.nm-ie6.min${buildNumber}.js"></script>
<![endif]-->
<style type="text/css">
.clean_menu {
    display: inline !important;
    float: none !important;
}
.clean_menu a:hover {
	background: inherit !important;
}
</style>
<script type="text/javascript">
    $(document).ready(function() {
        //openid.init('openid_identifier');
        //openid.setDemoMode(true); //Stops form submission for client javascript-only test purposes
        $("#loginbox").click(function() { 
            $("#logincontainer").toggle();
            $(this).find("i").toggleClass("signout");
        });
        $("fieldset#signin_menu").mouseup(function() {
            return false
        });
        $(document).mouseup(function(e) {
        	$("#logincontainer").hide();
            $("#loginbox").find("i").removeClass("signout");
        });        
    });
</script>
</head>
<body>
<div style="display: none;">
<!-- preload image -->
<img src='${pageContext.request.contextPath}/media/images/metabox_loader.gif'/>
</div>
<jsp:include page="/include/navigation.jsp"/>

<div class="container">	

<div class="clearer"><span></span></div>
<div id="context-loader" class="context-loader">Cargando&hellip;</div>
<div id="main" class="main" style="display: none;">
<jsp:include page="/include/flash.jsp" />
