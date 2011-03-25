<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@page import="com.google.appengine.api.users.UserService"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@page import="com.google.appengine.api.users.User"%>

<%
if (request.getParameter("m") == null) {
%>
<fieldset>
<legend>Formulario de Contacto</legend>
<p>Si manejas un servidor y deseas incorporarlo al servicio, envíanos un mensaje.</p>
<%
if (request.getParameter("e") != null) {
    out.write("<p style='color: red;'>Completa todos los campos solicitados.</p>");
}

// UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();
%>
<form action="/contact" method="post">
<% if (user != null) { %>
<input type="text" name="m" value="<%= user.getEmail() %>" />
<% } else { %>
<input type="text" name="m" value="" />
<% } %>
<br/>
<textarea name="text" rows="5" cols="20"></textarea>
<br/>
<input type="submit" value="Enviar"/>
</form>
</fieldset>
<% } else { %>
<p>Su mensaje fue enviado.</p>
<% } %>