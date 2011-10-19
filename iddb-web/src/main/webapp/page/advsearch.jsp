<%@page import="iddb.web.security.service.UserServiceFactory"%>
<%@page import="iddb.web.security.service.UserService"%>
<%@page import="iddb.web.security.subject.Subject"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<form id="advsearch">
<fieldset><legend>Búsqueda avanzada</legend>
<label for="aq">Buscar palabras clave:</label><input placeholder="Ingrese consulta" class="focus" type="text" name="aq" value=""/>&nbsp;<input type="button" value="Buscar" />
<br/>
<span><small>Escribe + delante de una palabra a encontrar, utiliza - para excluirla. Usa * como comodín para coincidencias parciales.</small></span>
<br/>
<br/>
<label for="server">Buscar únicamente en el servidor seleccionado:</label>
<ul>
<c:forEach items="${servers}" var="server">
<li><input type="radio" value="${server.key}" name="server"><span style="padding-left: 8px;">${server.name}</span></li>
</c:forEach>
</ul>
</fieldset>
</form>

<script type="text/javascript">
$("#advsearch").find(":button").click(function() {
	var query = $("#advsearch").find('input[name=aq]').val();
	var server = $("#advsearch").find("input[name='server']:checked").val();
	if (query.length > 0) {
		var url = dutils.urls.resolve('search', {'query': query}) + "?mode=adv";
		if (server != undefined) {
			url = url + "&server=" + server; 	
		}
		window.location = url;
	}
});
</script>