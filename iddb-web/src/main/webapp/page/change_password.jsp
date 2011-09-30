<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript">
$(document).ready(function() {
	$("#current").focus();
 });
</script>

<fieldset id="signin_form">
	<form method="post">
		<h3>Cambiar contrase&ntilde;a</h3>
		<p>
			<label for="current">
			<span>Contrase&ntilde;a actual</span>
			<input id="current" name="current" value="" tabindex="4" type="password">
			</label>
		</p>
		<p>
			<label for="password">
			<span>Nueva contrase&ntilde;a</span>
			<input id="password" name="password" value="" tabindex="5" type="password">
			</label>
		</p>
		<p>
			<label for="password2">
			<span>Repita su nueva contrase&ntilde;a</span>
			<input id="password2" name="password2" value="" tabindex="6" type="password">
			</label>
		</p>		
		<p class="remember">
			<input id="signin_submit" value="Aceptar" tabindex="6" type="submit">
		</p>
	</form>
</fieldset>
