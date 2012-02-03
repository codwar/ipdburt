<%@page import="iddb.web.security.service.UserPermission"%>
<%@page import="iddb.web.security.service.UserServiceFactory"%>
<%@page import="iddb.web.security.service.UserService"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/tld/urlresolver.tld" prefix="url"%>
<%@ taglib uri="/WEB-INF/tld/gravatar.tld" prefix="g"%>

<script type="text/javascript">
    dutils.conf.urls.search = "<url:clean name="search"/>";
</script>

<% UserService userService = UserServiceFactory.getUserService(); %>

<div id="topnavigation">
   <ul class="topnav">
        <li>
            <div id="session">
            <% if (userService.getCurrentUser().isAuthenticated()) { %>
	            <span class="subnav" id="signin-link"><g:gravatar email="<%= userService.getCurrentUser().getLoginId() %>" size="16"/> <em style="font-weight: bold; font-style: normal;"><%= userService.getCurrentUser().getScreenName() %></em></span>
	            <ul class="subnav">
	            	<li><a href="<url:url name="userplayerdetail"/>">Perfil</a></li>
	                <li><a href="<url:url name="change_password"/>">Cambiar contrase&ntilde;a</a></li>
	                <li><a href="<url:url name="logout"/>">Desconectar</a></li>
	            </ul>            
            <% } else { %>
				<c:if test="${empty next}"><!-- awful hack -->            
            	<a href="#" id="signin-link"><em>&iquest;Tienes una cuenta?</em><strong>Identificarse</strong><i></i></a>
            	<jsp:include page="/include/login.jsp"/>
            	</c:if>
            <% } %>
            </div>                
        </li>   
        <li>
        <small><input placeholder="Ingrese consulta" class="search focus" type="text" name="q" value="${queryValue}" style="margin-top: 8px;"/></small><span class="advsearch"><a href='<url:url name="advsearch"/>' class="icon tip clean_menu" title="Búsqueda avanzada"></a></span>
        </li>    
        <li><a href="<url:url name="banlist"/>">Baneados</a></li>
        <li><a href="<url:url name="serverlist"/>">Servidores</a></li>
        <%
        	if (userService.hasAnyServer(UserPermission.LEVEL_SUPERADMIN)) {
        %>
        <li>  
            <span class="subnav">Administrar</span>
            <ul class="subnav">
		        <%
		            if (userService.getCurrentUser().isSuperAdmin()) {
		        %>
		        <li><a href="<url:url name="admin-serverlist"/>">Sistema</a></li>
		        <li><a href="<url:url name="admin-users"/>">Usuarios</a></li>
		        <%      
		            }
		        %>
                <li><a href="<url:url name="manager"/>">Servidor</a></li>
            </ul>
        </li>
		<% } %>
        <li>  
            <span class="subnav">Ayuda</span>
            <ul class="subnav">
                <li><a href="<url:url name="contact"/>">Contacto</a></li>
                <li><a href="<url:url name="faq"/>">FAQ</a></li>
            </ul>
        </li>
    	<div id="logo" style="float: right; margin-right: 20px;">
	        <a href="<url:url name="home"/>">IPDB</a>
    	</div>
         <div id="donar" style="float: right;">
        	<a href="<url:url name="donation"/>"><img width="80" src="${pageContext.request.contextPath}/media/images/donar_es.gif" alt="Donar" border="0" /></a>
    	</div>
    </ul>
</div>