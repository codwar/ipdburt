<%@page import="iddb.core.security.User"%>
<%@page import="iddb.core.security.UserServiceFactory"%>
<%@page import="iddb.core.security.UserService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fieldset><legend>Formulario de Contacto</legend>
<p>Si manejas un servidor y deseas incorporarlo al servicio,
envíanos un mensaje.</p>
<form method="post">
<div>
<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
%>

<label for="m">E-mail:</label>
<% if (user.isAuthenticated()) { %>
<input type="text" name="m" value="<%= user.getEmail() %>" />
<% } else { %>
<input type="text" name="m" value="" />
<% } %>
<br/>
<textarea name="text" rows="5" cols="20" style="float: left;"></textarea>
${applicationScope.jipdbs.newRecaptchaCode}
</div>
<input type="submit" value="Enviar" /></form>
</fieldset>