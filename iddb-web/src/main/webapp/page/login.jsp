<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript">
$(document).ready(function() {
	$("#username").focus();
 });
</script>

<fieldset id="signin_form">
	<form method="post" id="signin">
		<h3>Iniciar sesi&oacute;n</h3>
		<div style='display:none'><input type="hidden" name="next" value="${next}"/>"/></div>
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
			<!--  <input id="remember" name="remember_me" value="1" tabindex="7" type="checkbox"> <label for="remember">Remember me</label> -->
		</p>
		<!-- 
		<p class="forgot">
			<a href="#" id="resend_password_link">Forgot your password?</a>
		</p>
		<p class="forgot-username">
			<A id=forgot_username_link
				title="If you remember your password, try logging in with your email"
				href="#">Forgot your username?</A>
		</p>-->
	</form>
</fieldset>
