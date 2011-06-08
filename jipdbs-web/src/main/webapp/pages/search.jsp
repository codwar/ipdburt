<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/ipdbs.tld" prefix="pag"%>

<jsp:include page="/search" />
<jsp:include page="/pages/flash.jsp" />
<script type="text/javascript">
<!--
$("[name=q]").val("<c:out value="${queryValue}"/>");
//-->
</script>

<script type="text/javascript">
	function getAlias(key, offset, callback) {

		$.getJSON("/json/alias.jsp?id=" + key + "&o=" + offset, function(data) {

			var rows = new Array();

			$.each(data.items, function(key, value) {

				var html = "";
				html += "<tr class=\"aliasrow\">";
				html += "<td><a href=\"/search.jsp?q=";
				html += encodeURIComponent(value.nickname);
				html += "\">";
				html += value.nickname;
				html += "</a></td>";
				html += "<td>";
				html += value.updated;
				html += "</td>";
				html += "<td style='text-align: right;'>";
				html += value.count;
				html += "</td>";
				html += "</tr>";

				rows[key] = $(html);
			});

			callback(offset, rows, data.hasMore);

		});
	}
	function getAliasIP(key, offset, callback) {

		$.getJSON("/json/aliasip.jsp?type=ip&id=" + key + "&o=" + offset, function(data) {
			var rows = new Array();
			$.each(data.items, function(key, value) {
				var html = "";
				html += "<tr class=\"aliasrow\">";
				html += "<td><a href=\"/search.jsp?q=";
				html += value.ipSearch;
				html += "\">";
				html += value.ip;
				html += "</td>";
				html += "<td>";
				html += value.updated;
				html += "</td>";
				html += "<td style='text-align: right;'>";
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
				var type = elem.attr("alt");
				if (type == "ip") {
					var key = elem.attr("id").substring("plus-ip-".length);
					var reminus = "minus-" + key;
				} else {
					var key = elem.attr("id").substring("plus-".length);
					var reminus = "minus-ip-" + key;
				}
				
				$("#"+reminus+":visible").click();
				
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
							if (type=="ip") {
								getAliasIP(key, offset + 1, updateFun);
							} else {
								getAlias(key, offset + 1, updateFun);
							}
						});
					} else
						more.hide();

					elem.hide();
					minus.show();
					sibling.show();
				};

				if (type=="ip") {
					getAliasIP(key, 1, updateFun);
				} else {
					getAlias(key, 1, updateFun);
				}

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

        $(".infoTip").tipTip({attribute: "alt"});

	});
</script>
<span id="copyall" rel="copydialog" class="button" style="visibility:hidden;">Copiar selección</span>
<table id="search-result">
	<thead>
		<tr>
		    <th style="width: 20px;"><input type="checkbox" id="multiselect"></th>
			<th>Id</th>
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
			<tr class="${rowStyle}" id="result-${rowCounter.count}">
			    <td copiable="false"><input type="checkbox" value="result-${rowCounter.count}" name="selector"></td>
				<td style="text-align: right;">
				<c:if test="${not empty player.note}">
				<span class="icon information infoTip" alt="${player.note}">&nbsp;</span>
				</c:if>
				<a title="Mostrar más información" href="/playerinfo.jsp?id=${player.key}">${player.clientId}</a>
				</td>
				<td class="icon
				<c:choose>
                    <c:when test="${not empty player.banInfo}">
                        banned infoTip
                    </c:when>
                    <c:when test="${player.playing}">
                        online
                    </c:when>
                    <c:otherwise>
                        offline
                    </c:otherwise>
                </c:choose>
				" alt="${player.banInfo}"><span class="plus" alt="alias" id="plus-${player.key}">[+]</span>
				<span class="minus"	id="minus-${player.key}" style="display: none;">[-]</span>
				<span>
				<c:url value="/search.jsp" var="url">
					<c:param name="q" value="${player.name}" />
				</c:url> <a href="${url}">${fn:escapeXml(player.name)}</a></span></td>
				<td>
				<span class="plus" alt="ip" id="plus-ip-${player.key}">[+]</span>
				<span class="minus"	id="minus-ip-${player.key}" style="display: none;">[-]</span>
				<span>
				<a href="/search.jsp?q=${player.ipSearch}">${player.ip}</a>&nbsp;<a
					target="_blank"
					href="http://whois.domaintools.com/${player.ipZero}" title="Whois"
					class="icon vcard"></a></td>
				<td style="text-align: right;">
				<fmt:formatDate value="${player.latest}" type="both"
                        timeZone="GMT-3:00" pattern="dd-MM-yyyy HH:mm:ss" />
				<%--  
				<c:if test="${not player.playing }">
					<fmt:formatDate value="${player.latest}" type="both"
						timeZone="GMT-3:00" pattern="dd-MM-yyyy HH:mm:ss" />
				</c:if><c:if test="${player.playing}">Conectado</c:if>--%></td>
				<td><a href="/search.jsp?q=${player.server.keyString}&t=s">${player.server.name}</a></td>
				<c:if test="${not empty player.banInfo}">
				<td style="display: none;">${player.banInfo}</td>
				</c:if>
			</tr>
			<tr style="display: none;">
				<td colspan="6" style="padding: 20px;">
				<table>
					<thead>
						<tr>
							<td colspan="4" style="text-align: right;"><a
								href="/playerinfo.jsp?id=${player.key}" class="icon details">Todo</a></td>
						</tr>
						<tr>
							<th></th>
							<th style="width: 160px;">Visto</th>
							<th style="width: 40px;">Usos</th>
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
				<td colspan="6"
					style="text-align: center; font-size: large; padding: 20px">La
				búsqueda no arrojó resultados.</td>
			</tr>
		</c:if>
	</tbody>
	<tfoot>
		<tr>
			<c:url value="/search.jsp" var="url">
				<c:param name="q" value="${query}" />
				<c:param name="t" value="${type}" />
			</c:url>
			<td colspan="6"><span style="font-size: smaller;">Total:
			${count} (${time} ms)</span><pag:paginator
				totalPages="${pageLink.totalPages}"
				currentPage="${pageLink.pageNumber}" pageSize="${pageLink.pageSize}"
				url="${url}" /></td>
		</tr>
	</tfoot>
</table>
<div style="display:none;" id="copydialog">
<span id="copycontent"></span>
<a href="#copydialog" id="copyTrigger" style="display: none;"></a>
</div>
