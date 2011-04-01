<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fieldset><legend>Formulario de Contacto</legend>
<p>Si manejas un servidor y deseas incorporarlo al servicio,
envíanos un mensaje.</p>
<form action="/pages/contactpost.jsp" method="post"><c:if
	test="${not empty pageContext.request.userPrincipal}">
	<input type="text" name="m"
		value="${pageContext.request.userPrincipal.name}" />
</c:if> <c:if test="${empty pageContext.request.userPrincipal}">
	<input type="text" name="m" value="" />
</c:if> <br />
<textarea name="text" rows="5" cols="20"></textarea> <br />
${applicationScope.jipdbs.newRecaptchaCode} <input type="submit"
	value="Enviar" /></form>
</fieldset>
