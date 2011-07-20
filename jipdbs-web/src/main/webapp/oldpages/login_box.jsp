<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="logincontainer" style="display: none;">
  <fieldset id="signin_menu">
	<form action="examples/consumer/try_auth.php" method="post" id="openid_form">
	<input type="hidden" name="action" value="verify" />
	<div id="openid_btns"></div>
	<div id="openid_input_area">
       <input id="openid_identifier" name="openid_identifier" type="text" value="http://" />
       <input id="openid_submit" type="submit" value="Sign-In"/>
	</div>
	</form>  
  </fieldset>
</div>

            <%--
            if (request.getUserPrincipal() != null) {
                out.write("<a href=\""+userService.createLogoutURL(request.getRequestURI())+"\" id=\"signin-link\"><em>"+request.getUserPrincipal().getName()+"</em><strong>Desconectar</strong><i class=\"signout\"></i></a>");
            } else {
                out.write("<a href=\""+userService.createLoginURL(request.getRequestURI())+"\" id=\"signin-link\"><em>&iquest;Tienes una cuenta?</em><strong>Identificarse</strong><i></i></a>");
            }
            --%>


