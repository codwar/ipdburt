<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib uri='/WEB-INF/tld/template.tld' prefix='template' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>IPDB Sudamericana</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<meta name="description" content="description"/>
<meta name="keywords" content="keywords"/> 
<meta name="author" content="author"/> 
<link rel="search" type="application/opensearchdescription+xml" href="${pageContext.request.contextPath}/opensearch.xml" title="Buscar en IPDB" />
<url rel="self" type="application/opensearchdescription+xml" template="${pageContext.request.contextPath}/opensearch.xml"/>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/default.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/message.place.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/tipTip.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/styles/menu.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/nm.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/selector/css/openid-shadow.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/site.css" media="screen"/>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.nm.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.measure.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.place.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.pulse.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.loading.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.tiptip.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.floatobject-1.4.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/styles/menu.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/selector/js/openid-jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/selector/js/openid-es.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/dutils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/media/js/base.js"></script>
<!--[if IE 6]>
    <script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.nm-ie6.min.js"></script>
<![endif]-->
<script type="text/javascript">
    $(document).ready(function() {
        openid.init('openid_identifier');
        openid.setDemoMode(true); //Stops form submission for client javascript-only test purposes
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
<%-- jsp:include page="/pages/login_box.jsp"/--%>
<!-- 
<div class="beta">
	<img src="${pageContext.request.contextPath}/media/images/bimage.png" width="105" height="107"/>
</div>
 -->
<div class="container">	

<div class="clearer"><span></span></div>
<div id="context-loader" class="context-loader">Cargando&hellip;</div>
<div id="main" class="main" style="display: none;">
<jsp:include page="/include/flash.jsp" />