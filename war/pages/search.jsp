<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/ipdbs.tld" prefix="pag"%>

<jsp:include page="/search" />
<jsp:include page="/pages/flash.jsp" />

<small>
<form method="get"><input class="search" type="text" name="q"
	value="${queryValue}" /> <select name="t" style="width: 65px;">
	<option value="alias" <c:if test='${type == "alias"}'>selected</c:if>>Alias</option>
	<option value="ip" <c:if test='${type == "ip"}'>selected</c:if>>IP</option>
</select> <input type="submit" value="Buscar" /></form>
</small>

<script language="javascript">
	function getHTML(key, offset, callback) {

		$.getJSON("/json/alias.jsp?id=" + key + "&o=" + offset, function(data) {

			var rows = new Array();

			$.each(data.items, function(key, value) {

				var html = "";
				html += "<tr class=\"aliasrow\">";
				html += "<td><a href=\"/search.jsp?q=";
				html += encodeURIComponent(value.nickname);
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

				rows[key] = $(html);
			});

			callback(offset, rows, data.hasMore);

		});
	}

	$(function() {
		$(".plus").each(function(key) {

			$(this).click(function() {

				var elem = $(this);
				var key = elem.attr("id").substring("plus-".length);

				var minus = elem.next();
				var sibling = elem.parent().parent().next();

				var updateFun = function(offset, rows, hasMore) {

					var table = sibling.find("table");

					var more = table.find("#more");

					$.each(rows, function(key, value) {
						more.before(value);
					});

					if (hasMore) {
						more.show();
						more.unbind();
						more.click(function() {
							getHTML(key, offset + 20, updateFun);
						});
					} else
						more.hide();

					elem.hide();
					minus.show();
					sibling.show();
				};

				getHTML(key, 0, updateFun);

			});
		});

		$(".minus").each(function(key) {

			$(this).click(function() {

				var elem = $(this);

				var plus = elem.prev();
				var sibling = elem.parent().parent().next();

				sibling.hide();
				sibling.find(".aliasrow").remove();
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
				<span
					<c:if test="${not empty player.banInfo}">class="icon icon-right exclamation" title="${player.banInfo}"</c:if>>
				<c:url value="/search.jsp" var="url">
					<c:param name="q" value="${player.name}" />
					<c:param name="t" value="alias" />
				</c:url> <a href="${url}">${fn:escapeXml(player.name)}</a></span></td>
				<td><a href="/search.jsp?q=${player.ipSearch}&t=ip">${player.ip}</a>&nbsp;<a
					target="_blank"
					href="http://whois.domaintools.com/${player.ipZero}" title="Whois"
					class="icon vcard"></a></td>
				<td><c:if test="${not player.playing }">
					<fmt:formatDate value="${player.latest}" type="both"
						timeZone="GMT-3:00" pattern="dd-MM-yyyy HH:mm:ss" />
				</c:if><c:if test="${player.playing}">Conectado</c:if></td>
				<td><a href="/search.jsp?q=${player.server.keyString}&t=s">${player.server.name}</a></td>
			</tr>
			<tr style="display: none;">
				<td colspan="4" style="padding: 20px;">
				<table>
					<thead>
						<tr>
							<td colspan="4" style="text-align: right;"><a
								href="/playerinfo.jsp?id=${player.key}" class="icon details">Todo</a></td>
						</tr>
						<tr>
							<th>Nombre</th>
							<th>IP</th>
							<th>Visto</th>
							<th>Cantidad de Usos</th>
						</tr>
					</thead>
					<tr id="more" style="cursor: pointer;">
						<td colspan="4" style="text-align: center;"><span
							class="icon refresh">Más...</span></td>
					</tr>
				</table>
				</td>
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
