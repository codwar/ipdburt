<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<form method="post">
<div style='display:none'><input type="hidden" value="${next}"/></div>
<label for="username">Usuario:</label><input type="text" name="username" value="" /><br/>
<label for="password">Contraseña:</label><input type="password" name="password" value="" /><br/>
<input type="submit" value="Enviar" />
</form>
