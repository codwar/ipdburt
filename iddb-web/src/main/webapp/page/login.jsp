<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/urlresolver.tld" prefix="url"%>

<script type="text/javascript">
$(document).ready(function() {
	$("#signin-link").hide();
	$("#signin_menu").remove();
	$("#username").focus();
 });
</script>

<fieldset id="signin_form">
	<form method="post" id="signin">
		<h3>Iniciar sesi&oacute;n</h3>
		<div style='display:none'><input type="hidden" name="next" value="${next}"/></div>
		<p>
			<label for="username">
			<span>Usuario</span>
			<input id="username" name="username" value="" title="username" tabindex="4" type="text">
			</label>
		</p>
		<p>
			<label for="password">
			<span>Contraseña</span>
			<input id="password" name="password" value="" title="password" tabindex="5"	type="password">
			</label>
		</p>
		<p class="remember">
			<input id="signin_submit" value="Aceptar" tabindex="6" type="submit">
			<input id="remember" name="remember" value="1" tabindex="7" type="checkbox"> <label for="remember">Recordarme</label>
		</p>
		<p class="forgot">
			<a href="<url:url name="passwordrecovery"/>" id="resend_password_link">Olvid&eacute; mi contrase&ntilde;a</a>
		</p>
		<p class="forgot-username">
			<span>¿Olvidaste tu usuario? Si recuerdas tu contrase&ntilde;a, prueba usando tu direcci&oacute;n de correo</span>
		</p>
	</form>
</fieldset>
