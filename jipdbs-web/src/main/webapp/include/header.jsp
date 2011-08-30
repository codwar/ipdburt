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
<link rel="stylesheet" type="text/css" href="/media/nm.css" media="screen"/>
<link type="text/css" rel="stylesheet" href="/media/selector/css/openid-shadow.css" />
<link rel="stylesheet" type="text/css" href="/media/site.css" media="screen"/>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>
<script type="text/javascript" src="/media/js/jquery.nm.min.js"></script>
<script type="text/javascript" src="/media/js/jquery.measure.js"></script>
<script type="text/javascript" src="/media/js/jquery.place.js"></script>
<script type="text/javascript" src="/media/js/jquery.pulse.js"></script>
<script type="text/javascript" src="/media/js/jquery.loading.js"></script>
<script type="text/javascript" src="/media/js/jquery.tiptip.min.js"></script>
<script type="text/javascript" src="/media/js/jquery.floatobject-1.4.js"></script>
<script type="text/javascript" src="/media/styles/menu.js"></script>
<script type="text/javascript" src="/media/selector/js/openid-jquery.js"></script>
<script type="text/javascript" src="/media/selector/js/openid-es.js"></script>
<script type="text/javascript" src="/media/js/dutils.js"></script>
<script type="text/javascript" src="/media/js/base.js"></script>
<!--[if IE 6]>
    <script type="text/javascript" src="/media/js/jquery.nm-ie6.min.js"></script>
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
<img src='/media/images/metabox_loader.gif'/>
</div>
<jsp:include page="/include/navigation.jsp"/>
<%-- jsp:include page="/pages/login_box.jsp"/--%>
<div class="beta">
	<img src="/media/images/bimage.png" width="105" height="107"/>
</div>

<div class="container">	

<div class="clearer"><span></span></div>
<div id="context-loader" class="context-loader">Cargando&hellip;</div>
<div id="main" class="main" style="display: none;">
<jsp:include page="/include/flash.jsp" />