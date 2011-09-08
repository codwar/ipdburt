<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/ipdbs.tld" prefix="pag"%>

<div id="openidbox">
<form action="examples/consumer/try_auth.php" method="post" id="openid_form">
    <input type="hidden" name="action" value="verify" />
    <fieldset>
        <legend>Iniciar Sesión</legend>
        <div id="openid_choice">
            <p>Seleccione el proveedor de su preferencia:</p>
            <div id="openid_btns"></div>
        </div>
        <div id="openid_input_area">
            <input id="openid_identifier" name="openid_identifier" type="text" value="http://" />
            <input id="openid_submit" type="submit" value="Sign-In"/>
        </div>
    </fieldset>
</form>
</div>