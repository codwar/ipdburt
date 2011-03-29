<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/tld/ipdbs.tld" prefix="pag"%>

<jsp:include page="/playerinfo" />

<fieldset style="width: 75%; margin-left: auto; margin-right: auto;">
<legend>${fn:escapeXml(player.name)}</legend>
<strong>Visto:</strong> <fmt:formatDate type="both"
                    timeZone="GMT-3" pattern="dd-MM-yyyy HH:mm:ss"
                    value="${player.updated}" /><br/>
<strong>Servidor:</strong> <a href="/search.jsp?q=${player.server.keyString}&t=s">${player.server.name}</a><br/>
<strong>IP:</strong> <a href="/search.jsp?q=${player.ip}&t=ip">${player.ip}</a><br/>
<c:if test="${not empty player.banInfo}">
<strong>Estado:</strong> ${player.banInfo}<br/>
</c:if>
</fieldset>
<br />
<table>
	<thead>
		<tr>
			<th>Alias</th>
			<th>IP</th>
			<th>Visto</th>
			<th>Usos</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${player.aliases}" var="alias">
			<tr>
				<td><a href="/search.jsp?q=${fn:escapeXml(alias.nickname)}&t=alias">${fn:escapeXml(alias.nickname)}</a></td>
				<td><a href="/search.jsp?q=${alias.ip}&t=ip">${alias.ip}</a></td>
				<td><fmt:formatDate type="both"
                    timeZone="GMT-3" pattern="dd-MM-yyyy HH:mm:ss"
                    value="${alias.updated}" /></td>
				<td>${alias.count}</td>
			</tr>
		</c:forEach>
	</tbody>
	<tfoot>
        <tr>
            <td colspan="4"><span style="font-size: smaller;">Total:
            ${count} (${time} ms)</span><pag:paginator
                totalPages="${pageLink.totalPages}"
                currentPage="${pageLink.pageNumber}" pageSize="${pageLink.pageSize}"
                url="/playerinfo.jsp?id=${player.key}" /></td>
        </tr>
	</tfoot>
</table>
