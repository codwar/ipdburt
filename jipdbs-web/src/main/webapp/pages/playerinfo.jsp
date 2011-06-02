<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/tld/ipdbs.tld" prefix="pag"%>

<jsp:include page="/playerinfo" />
<jsp:include page="/pages/flash.jsp" />

<script type="text/javascript">
	var clientKey = '${player.key}';
    function pagination(key, offset, hasMore, pages) {
        prev = $("#prev-"+key);
        $(prev).unbind('click');
    	if (offset == 0) {
			$(prev).removeClass('prev').addClass('prev-na');
    	} else {
    	    $(prev).removeClass('prev-na').addClass('prev');
    	    $(prev).click({'offset': offset-1, 'elem': key}, function(e) {getHTML(e.data.elem,e.data.offset);});
    	}
        next = $("#next-"+key);
        $(next).unbind('click');
    	if (hasMore) {
    		$(next).removeClass('next-na').addClass('next');
    		$(next).click({'offset': offset+1, 'elem': key}, function(e) {getHTML(e.data.elem,e.data.offset);});
    	} else {
    	    $(next).removeClass('next').addClass('next-na');
    	}
    	$("#curr-"+key).html("{0}-{1}".format(offset+1,pages));
    }
    function getHTML(key, offset) {
    	if (key == 'alias') {
    		getAlias(offset);
    	} else {
    	    getAliasIP(offset);
    	}
    }
	function getAlias(offset) {
		$.getJSON("/json/alias.jsp?id=" + clientKey + "&o=" + offset, function(data) {
			$("#tablealias").html("");
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
				$("#tablealias").append(html);
			});
			pagination('alias', data.offset, data.hasMore, data.pages);
		});
	}
	function getAliasIP(offset) {
		$.getJSON("/json/aliasip.jsp?type=ip&id=" + clientKey + "&o=" + offset, function(data) {
			$("#tableip").html("");
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
				$("#tableip").append(html);
			});
			pagination('ip', data.offset, data.hasMore, data.pages);
		});
	}
</script>

<fieldset style="width: 75%; margin-left: auto; margin-right: auto;">
<legend>${fn:escapeXml(player.name)}
<span class="icon
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
"></span>
</legend>
	<strong>Id:</strong> ${player.clientId}<br />
	<strong>Visto:</strong> <fmt:formatDate
	type="both" timeZone="GMT-3" pattern="dd-MM-yyyy HH:mm:ss"
	value="${player.updated}" /><br />
<strong>Servidor:</strong> <a
	href="/search.jsp?q=${player.server.keyString}&t=s">${player.server.name}</a><br />
<strong>IP:</strong> <a href="/search.jsp?q=${player.ip}">${player.ip}</a>&nbsp;<a target="_blank" href="http://whois.domaintools.com/${player.ipZero}" title="Whois" class="icon vcard"></a><br />
    <strong>Nivel:</strong> ${player.level}<br />
<c:if test="${not empty player.banInfo}">
	<strong>Estado:</strong> ${player.banInfo}<br />
</c:if></fieldset>
<br />
<h2>Aliases</h2>
<table>
	<thead>
		<tr>
			<th>Alias</th>
			<th style="width: 160px;">Visto</th>
			<th style="width: 40px;">Usos</th>			
		</tr>
	</thead>
	<tbody id="tablealias">
	</tbody>
	<tfoot>
		<tr>
			<td colspan="3"><div class='pagination'><span class='prev-na' id='prev-alias'><a>&laquo; Anterior</a></span><span class='curr' id='curr-alias'>-</span><span id='next-alias' class='next-na'><a>Siguiente &raquo;</a></span></td>
		</tr>
	</tfoot>
</table>
<br/>
<h2>IPs</h2>
<table>
	<thead>
		<tr>
			<th>IP</th>
			<th style="width: 160px;">Visto</th>
			<th style="width: 40px;">Usos</th>			
		</tr>
	</thead>
	<tbody id="tableip">
	</tbody>
	<tfoot>
		<tr>
			<td colspan="3"><div class='pagination'><span class='prev-na' id='prev-ip'><a>&laquo; Anterior</a></span><span class='curr' id='curr-ip'>-</span><span id='next-ip' class='next-na'><a>Siguiente &raquo;</a></span></td>
		</tr>
	</tfoot>
</table>
<script type="text/javascript">
$(document).ready(
	function() {
		getAlias(0);
		getAliasIP(0);
	}
)
</script>
