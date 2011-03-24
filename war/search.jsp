<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/ipdbs.tld" prefix="pag"%>

<jsp:include page="/search" />

<small>
<form method="get"><input class="search" type="text" name="q"
	value="${query}" /> <select name="t" style="width: 65px;">
	<option value="alias" <c:if test='${type == "alias"}'>selected</c:if>>Alias</option>
	<option value="ip" <c:if test='${type == "ip"}'>selected</c:if>>IP</option>
</select> <input type="submit" value="Buscar" /></form>
</small>

<script language="javascript">
	function getHTML(key, callback) {

		$
				.getJSON(
						"/json/alias.jsp?id=" + key,
						function(data) {

							var html = "<td colspan=\"4\" style=\"padding: 20px;\"><table><thead><tr><th>Nombre</th><th>IP</th>"
									+ "<th>Visto</th><th>Cantidad de Usos</th></tr></thead>";

							$.each(data.items, function(key, value) {

								html += "<tr>";
								html += "<td><a href=\"/search.jsp?q=";
								html += value.nickname;
								html += "&t=alias\">";
								html += value.nickname;
								html += "</a></td>";
								html += "<td><a href=\"/search.jsp?q=";
								html += value.ipSearch;
								html += "&t=ip\">";
								html += value.ip;
								html += "</td>";
								html += "<td>";
								html += value.updated;
								html += "</td>";
								html += "<td>";
								html += value.count;
								html += "</td>";
								html += "</tr>";

							});

							html += "</table></td>";

							callback(html);

						});
	}

	$(function() {
		$(".plus").each(function(key) {

			$(this).click(function() {

				var elem = $(this);
				var key = elem.attr("id").substring("plus-".length);

				var minus = elem.next();
				var sibling = elem.parent().parent().next();

				getHTML(key, function(html) {
					sibling.html(html);
					elem.hide();
					sibling.show();
					minus.show();
				});

			});
		});

		$(".minus").each(function(key) {

			$(this).click(function() {

				var elem = $(this);

				var plus = elem.prev();
				var sibling = elem.parent().parent().next();

				sibling.hide();
				elem.hide();
				plus.show();

			});
		});
		
		$(".exclamation").tipTip();
		
	});
</script>

<table>
	<thead>
		<tr>
			<th>Nombre</th>
			<th>IP</th>
			<th>Visto</th>
			<th>Servidor</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="player" varStatus="rowCounter">
			<c:choose>
				<c:when test="${rowCounter.count % 2 == 0}">
					<c:set var="rowStyle" scope="page" value="odd" />
				</c:when>
				<c:otherwise>
					<c:set var="rowStyle" scope="page" value="even" />
				</c:otherwise>
			</c:choose>
			<tr class="${rowStyle}">
				<td><span class="plus" id="plus-${player.key}"
					style="color: green; font-weight: bold; cursor: pointer; font-family: monospace;">[+]</span><span
					class="minus"
					style="display: none; color: red; font-weight: bold; cursor: pointer; font-family: monospace;">[-]</span>
				<span <c:if test="${not empty player.banInfo}">class="icon icon-right exclamation" title="${player.banInfo}"</c:if>> <a href="/search.jsp?q=${player.name}&t=alias">${fn:escapeXml(player.name)}</a></span></td>
				<td><a href="/search.jsp?q=${player.ipSearch}&t=ip">${player.ip}</a></td>
				<td><c:if test="${not player.playing }">
					<fmt:formatDate value="${player.latest}" type="both"
						timeZone="GMT-3:00" pattern="dd-MM-yyyy HH:mm:ss" />
				</c:if><c:if test="${player.playing}">Conectado</c:if></td>
				<td>${player.server}</td>
			</tr>
			<tr style="display: none;">
				<td>-</td>
			</tr>
		</c:forEach>
		<c:if test="${fn:length(list) eq 0}">
			<tr>
				<td colspan="4"
					style="text-align: center; font-size: large; padding: 20px">La
				búsqueda no arrojó resultados.</td>
			</tr>
		</c:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="4"><span style="font-size: smaller;">Total:
			${count} (${time} ms)</span><pag:paginator
				totalPages="${pageLink.totalPages}"
				currentPage="${pageLink.pageNumber}" pageSize="${pageLink.pageSize}"
				url="/search.jsp?q=${query}&t=${type}" /></td>
		</tr>
	</tfoot>
</table>
