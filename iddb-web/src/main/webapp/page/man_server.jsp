<%@page import="iddb.core.model.Server"%>
<%@page import="iddb.core.util.Functions"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collection"%>
<%@page import="iddb.api.RemotePermissions"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/tld/ipdbs.tld" prefix="iddb"%>
<%@ taglib uri="/WEB-INF/tld/urlresolver.tld" prefix="url"%>

<script type="text/javascript">
$(function() {
	$("input[name=ban]").bind("change", function() {
		var selected = parseInt($(this).val());
		toggle_mxinput(selected);
	});
	toggle_mxinput(parseInt($("input[name=ban]:checked").val()));
});
function toggle_mxinput(selected) {
	$("input[name^=maxban_lv_]").each(function() {
		var name = $(this).attr('name');
		var level = parseInt(name.substring(10));
		if (level < selected) {
			console.log("disable");
			$(this).prop('disabled', true);
		} else {
			$(this).prop('disabled', false);
		}
	});
}
</script>

<fieldset>
	<legend>${server.name}</legend>
	<strong>Versi&oacute;n:</strong> ${server.pluginVersion}<br/>
	<strong>Actualizado:</strong> <fmt:formatDate type="both" pattern="dd-MM-yyyy HH:mm:ss" value="${server.updated}" /><br/>
    <strong>IP:</strong> ${server.address}<br/>
    <strong>Jugadores online:</strong> ${server.onlinePlayers}<br/>
    <strong>Jugadores registrados:</strong> ${server.totalPlayers}<br/>
</fieldset>
<br/>

<%
/* 	Collection levels = new ArrayList();
	levels.add(20);
	levels.add(40);
	levels.add(60);
	levels.add(80);
	levels.add(100); */
	pageContext.setAttribute("levels", RemotePermissions.LEVELS);
%>

<form method="post">
<table style="width: 635px">
	<caption>Permisos</caption>
	<thead>
		<tr>
			<th>Funci&oacute;n</th>
			<th>Nivel</th>
		</tr>
	</thead>
	<tbody>
	<tr>
		<c:set var="addnlvl" value="${server.permissions[4]}"></c:set>
		<td>A&ntilde;adir Notas</td>
		<td>
			<c:forEach items="${levels}" var="level">
				<input name="addnote" type="radio" value="${level}" <c:if test="${level == addnlvl}">checked="checked"</c:if>>${level}&nbsp;
			</c:forEach>
		</td>
	</tr>
	<tr>
		<c:set var="delnlvl" value="${server.permissions[8]}"></c:set>
		<td>Borrar Notas</td>
		<td>
			<c:forEach items="${levels}" var="level">
				<input name="delnote" type="radio" value="${level}" <c:if test="${level == delnlvl}">checked="checked"</c:if>>${level}&nbsp;
			</c:forEach>
		</td>
	</tr>
	<tr>
		<c:set var="banlvl" value="${server.permissions[1]}"></c:set>
		<td>Banear</td>
		<td>
			<c:forEach items="${levels}" var="level">
				<input name="ban" type="radio" value="${level}" <c:if test="${level == banlvl}">checked="checked"</c:if>>${level}&nbsp;
			</c:forEach>
		</td>
	</tr>
	<tr>
		<c:set var="unbanlvl" value="${server.permissions[2]}"></c:set>
		<td>Quitar Ban</td>
		<td>
			<c:forEach items="${levels}" var="level">
				<input name="unban" type="radio" value="${level}" <c:if test="${level == unbanlvl}">checked="checked"</c:if>>${level}&nbsp;
			</c:forEach>
		</td>
	</tr>
	</tbody>
	<tfoot>
		<tr>
			<%
				Server server = (Server) pageContext.getRequest().getAttribute("server");
			%>
			<!-- td colspan="2">M&aacute;ximo ban permitido: <input style="width: 70px;" type="text" value="<%= Functions.minutes2Str(server.getMaxBanDuration()) %>" name="maxban"></td-->
			<td colspan="2">
				<span style="font-weight: bold;">M&aacute;ximo ban permitido:</span><br/>
				<c:forEach items="${levels}" var="level">
					<label style="font-weight:normal; font-size: 12px;" for="id_maxban_lvl_${level}">Nivel ${level}</label>
					<%
						Long v = server.getBanPermission((Long) pageContext.getAttribute("level"));
						String s = "0";
						if (v > 0) {
							s = Functions.minutes2Str(v);
						}
						pageContext.setAttribute("banValue", s);
					%>
					<input style="width: 40px;" type="text" name="maxban_lv_${level}" id="id_maxban_lvl_${level}" value="${banValue}">&nbsp;
				</c:forEach>
			</td>
		</tr>
		<tr>
			<td colspan="2" style="text-align: right;"><input type="submit" value="Guardar"></td>
		</tr>
	</tfoot>
</table>
</form>
<br/>