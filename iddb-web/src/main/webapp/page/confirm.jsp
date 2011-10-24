<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="context-message" style="margin-top: 25px; margin-bottom: 50px;">Aguarda mientras te redirigimos. Si no eres redirigido automáticamente en 10 segundos, haz click <a href="${redirect}">aqu&iacute;</a>.</div>

<script type="text/javascript">
    $(document).ready(function() {
    	setTimeout("redirect()", 8000);
    });
    function redirect() {
    	window.location = "<c:out value="${redirect}"/>";
    }
</script>