<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<%@ taglib uri="/WEB-INF/tld/ipdbs.tld" prefix="pag" %>

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

								html += "<tr style=\"cursor: pointer\";>";
								html += "<td>";
								html += value.nickname;
								html += "</td>";
								html += "<td>";
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

		$(".bound").each(function(key) {

			$(this).click(function() {

				var elem = $(this);
				var key = elem.attr("id").substring(3);
				var sibling = elem.next();

				getHTML(key, function(html) {
					sibling.html(html);
					sibling.show();
				});
			});
		});

		$(".sibling").each(function(key) {

			var elem = $(this);

			elem.click(function() {
				elem.hide();
			});
		});
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
			<tr class="${rowStyle} bound" id="id-${player.key}"
				style="cursor: pointer;">
				<td><span style="text-decoration: underline;">${fn:escapeXml(player.name)}</span></td>
				<td>${player.ip}</td>
				<td>${player.latest}</td>
				<td>${player.server}</td>
			</tr>
			<tr class="sibling" style="display: none;">
			</tr>
		</c:forEach>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="4">
				<pag:paginator totalPages="${pageLink.totalPages}"
								currentPage="${pageLink.pageNumber}"
								pageSize="${pageLink.pageSize}"
								url="/search.jsp?q=${query}&t=${type}"/>
			</td>
		</tr>
	</tfoot>
</table>