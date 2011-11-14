<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/tld/urlresolver.tld" prefix="url"%>

<h1>Jugadores asociados</h1>
<br />
<table>
	<thead>
		<tr>
			<th>Servidor</th>
			<th>Jugador</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="item">
			<c:set var="server" value="${item.server}"/>
			<c:set var="player" value="${item.player}"/>
			<tr>
				<td>
				<a href="<url:url name="serverfilter"><url:param name="query" value="${server.key}"/></url:url>">
				${server.name} 
				</a></td>
				<td>
				<a href="<url:url name="playerinfo"><url:param name="key" value="${player.key}"/></url:url>">
				${player.nickname} 
				</a></td>
			</tr>
		</c:forEach>
	</tbody>
	<tfoot></tfoot>
</table>
<br/>
