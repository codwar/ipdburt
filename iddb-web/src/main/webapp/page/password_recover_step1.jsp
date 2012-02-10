<%@page import="iddb.web.security.service.UserServiceFactory"%>
<%@page import="iddb.web.security.service.UserService"%>
<%@page import="iddb.web.security.subject.Subject"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript">
$(document).ready(function() {
	$("#email").focus();
 });
</script>

<fieldset id="signin_form">
	<form method="post">
		<h3>Recuperaci&oacute;n de contrase&ntilde;a</h3>
		<p>
			<label for="email">
			<span>E-mail:</span>
			<input id="email" name="email" value="" type="text">
			</label>
		</p>
		<p>
			${applicationScope.jipdbs.newRecaptchaCode}
		</p>		
		<p>
			<input type="submit" value="Enviar" />
		</p>
	</form>
</fieldset>