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
				<td>
				<a href="<url:url name="serverfilter"><url:param name="query" value="${server.key}"/></url:url>">
				${server.name} 
				<c:choose>
				<c:when test="${not empty server.displayAddress}">
				- [${server.displayAddress}]
				</c:when>
				<c:when test="${not empty server.address}">
				- [${server.address}]
				</c:when>			
				</c:choose>
				</a></td>
				<td style="text-align: right;">
				<c:choose>
				<c:when test="${server.dirty}">
				<input type="hidden" name="key" value="${server.key}"/>
 				<img class="fetch-server" alt="${server.key}" src='/media/images/loader.gif' border="0"/>
				</c:when>
				<c:otherwise>
					<c:choose>
					<c:when test="${server.onlinePlayers}=='0'">
						${server.onlinePlayers}
					</c:when>
					<c:otherwise>
						<span style="font-weight: bold;">${server.onlinePlayers}</span>
					</c:otherwise>
					</c:choose>
				</c:otherwise>
				</c:choose>
				</td>
				<td style="text-align: right;">
				<c:choose>
				<c:when test="${empty server.updated}">
					<span style="font-weight: bold;">Nunca</span>
				</c:when>
				<c:otherwise>
					<c:if test="${server.offline}"><span class="icon exclamation">&nbsp;</span></c:if>
					<fmt:formatDate type="both"	timeZone="GMT-3" pattern="dd-MM-yyyy HH:mm:ss" value="${server.updated}" />
				</c:otherwise>
				</c:choose>
				</td>
			</tr>
		</c:forEach>
	</tbody>
	<tfoot></tfoot>
</table>
<br/>
