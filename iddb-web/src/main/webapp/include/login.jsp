<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/ipdbs.tld" prefix="pag"%>
<%@ taglib uri="/WEB-INF/tld/urlresolver.tld" prefix="url"%>

<script type="text/javascript">
 $(document).ready(function() {

     $("#signin-link").click(function(e) {
         e.preventDefault();
         $("fieldset#signin_menu").toggle();
         $("#signin-link").toggleClass("menu-open");
         $("#username").focus();
     });

     $("fieldset#signin_menu").mouseup(function() {
         return false
     });
     $(document).mouseup(function(e) {
         if($(e.target).parent("a#signin-link").length==0) {
             $("#signin-link").removeClass("menu-open");
             $("fieldset#signin_menu").hide();
         }
     });            

 });
</script>

<fieldset id="signin_menu">
	<form method="post" id="signin" action="<url:url name="login"/>">
		<p>
			<label for="username">Usuario</label>
			<input id="username" name="username" value="" title="username" tabindex="4" type="text">
		</p>
		<p>
			<label for="password">Contraseña</label>
			<input id="password" name="password" value="" title="password" tabindex="5"	type="password">
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
