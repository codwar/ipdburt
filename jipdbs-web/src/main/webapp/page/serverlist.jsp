<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/tld/urlresolver.tld" prefix="url"%>

<script type="text/javascript">
    dutils.conf.urls.serverinfo = "<url:clean name="serverinfo"/>";
</script>

<h1>Servidores Registrados</h1>
<br />
<table>
	<thead>
		<tr>
			<th>Servidor</th>
			<th style="width: 90px;">Conectados</th>
			<th style="width: 160px;">Actualizado</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${servers}" var="server">
			<tr>
				<td><a href="<url:url name="serverfilter"><url:param name="query" value="${server.keyString}"/></url:url>">${server.name}</a></td>
				<td style="text-align: right;">
				<c:choose>
				<c:when test="${server.dirty}">
				<input type="hidden" name="key" value="${server.keyString}"/>
 				<img class="fetch-server" alt="${server.keyString}" src='/media/images/loader.gif'/>
				</c:when>
				<c:otherwise>
				${server.onlinePlayers}
				</c:otherwise>
				</c:choose>
				</td>
				<td style="text-align: right;"><c:if test="${server.offline}"><span class="icon exclamation">&nbsp;</span></c:if><fmt:formatDate type="both"
					timeZone="GMT-3" pattern="dd-MM-yyyy HH:mm:ss"
					value="${server.updated}" /></td>
			</tr>
		</c:forEach>
	</tbody>
	<tfoot></tfoot>
</table>
