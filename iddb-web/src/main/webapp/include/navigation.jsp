<%@page import="iddb.core.security.UserServiceFactory"%>
<%@page import="iddb.core.security.UserService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib uri='/WEB-INF/tld/template.tld' prefix='template' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/tld/urlresolver.tld" prefix="url"%>

<script type="text/javascript">
    dutils.conf.urls.search = "<url:clean name="search"/>";
</script>

<div id="topnavigation">
    <ul class="topnav">
    <div id="donar">
        <a href="<url:url name="donation"/>"><img width="80" src="${pageContext.request.contextPath}/media/images/donar_es.gif" alt="Donar"/></a>
    </div>
    <div id="logo">
        <a href="${pageContext.request.contextPath}">IPDB</a>
    </div>
        <li><!-- form id="search-form" method="get" action="/search/"-->
        <small><input placeholder="Ingrese consulta" class="search focus" type="text" name="q" value="${queryValue}" style="margin-top: 8px;"/></small>
        <!-- /form-->
        </li>    
        <li><a href="<url:url name="banlist"/>">Baneados</a></li>
        <li><a href="<url:url name="serverlist"/>">Servidores</a></li>
        <%
            UserService userService = UserServiceFactory.getUserService();
            if (userService.getCurrentUser().isSuperAdmin()) {
        %>
        <li><a href="https://ipdburt.appspot.com/<url:url name="admin-serverlist"/>">Administrar</a></li>
        <%      
            }
        %>
        <li>  
            <span class="subnav">Ayuda</span>
            <ul class="subnav">
                <li><a href="<url:url name="contact"/>">Contacto</a></li>
                <li><a href="<url:url name="faq"/>">FAQ</a></li>
            </ul>  
        </li>
        <li style="float: right; margin-right: 20px;">
            <div id="session">
            <%
            if (userService.getCurrentUser().isAuthenticated()) {
                out.write("<a href=\"logout\" id=\"signin-link\"><em>"+userService.getCurrentUser().getUsername()+"</em><strong>Desconectar</strong><i class=\"signout\"></i></a>");
            } else { 
            	out.write("<a href=\"login\" id=\"signin-link\"><em>&iquest;Tienes una cuenta?</em><strong>Identificarse</strong><i></i></a>");
            } %>
            <!-- span id="loginbox"><a href="#" id="signin-link"><strong>Identificarse</strong><i></i></a></span-->
            </div>                
        </li>
    </div>
    </ul>
</div>