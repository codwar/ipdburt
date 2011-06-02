<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/tld/ipdbs.tld" prefix="pag"%>

<jsp:include page="/playerinfo" />
<jsp:include page="/pages/flash.jsp" />

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
				<td>
				<c:url value="/search.jsp" var="url">
                    <c:param name="q" value="${alias.nickname}" />
                </c:url>
				<a href="${url}">${fn:escapeXml(alias.nickname)}</a></td>
				<td><a href="/search.jsp?q=${alias.ip}">${alias.ip}</a>&nbsp;<a target="_blank" href="http://whois.domaintools.com/${alias.ipZero}" title="Whois" class="icon vcard"></a></td>
				<td><fmt:formatDate type="both" timeZone="GMT-3"
					pattern="dd-MM-yyyy HH:mm:ss" value="${alias.updated}" /></td>
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
