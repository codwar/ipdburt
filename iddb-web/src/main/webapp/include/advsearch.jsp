<%@page import="iddb.core.model.Server"%>
<%@page import="java.util.List"%>
<%@page import="iddb.core.IDDBService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/urlresolver.tld" prefix="url"%>

<%
	IDDBService app = (IDDBService) pageContext.getServletContext().getAttribute("jipdbs");
	List<Server> servers = app.getActiveServers();
	pageContext.setAttribute("servers", servers);
%>

<div id="advsearch" style="display: none;">
<ul>
<c:forEach items="${servers}" var="server">
<li><input type="radio" value="${server.key}" name="server"><span style="padding-left: 8px;">${server.name}</span></li>
</c:forEach>
</ul>
<small><input type="button" value="Buscar" style="float: right;"></small>
</div>