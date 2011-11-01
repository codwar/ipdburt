<%@page import="iddb.web.security.service.UserServiceFactory"%>
<%@page import="iddb.api.RemotePermissions"%>
<%@page import="iddb.web.viewbean.PlayerViewBean"%>
<%@page import="iddb.core.model.Server"%>
<%@page import="iddb.core.model.Penalty"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/tld/ipdbs.tld" prefix="iddb"%>
<%@ taglib uri="/WEB-INF/tld/urlresolver.tld" prefix="url"%>

<jsp:include page="/include/jqueryui.jsp"/>

<script type="text/javascript">
    dutils.conf.urls.alias = "<url:clean name="alias"/>";
    dutils.conf.urls.aliasip = "<url:clean name="alias-ip"/>";
</script>

<script type="text/javascript">
	var clientKey = '${player.key}';
    function pagination(key, offset, hasMore, pages, total) {
        prev = $("#prev-"+key);
        $(prev).unbind('click');
    	if (offset == 1) {
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
    	if (pages == 0) offset = 0;
    	$("#curr-"+key).html("{0}-{1}".format(offset,pages));
    	$("#total-"+key).html(total);
    }
    function getHTML(key, offset) {
    	if (key == 'alias') {
    		getAlias(offset);
    	} else {
    	    getAliasIP(offset);
    	}
    }
	function getAlias(offset) {
	    url = dutils.urls.resolve('alias', { key: clientKey}) + "?o=" + offset;
		$.getJSON(url , function(data) {
			$("#tablealias").html("");
			$.each(data.items, function(key, value) {
				var html = "";
				html += "<tr class=\"aliasrow\">";
				html += "<td><a href=\"" + value.nickname_url + "\">";
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
			pagination('alias', data.offset, data.hasMore, data.pages, data.total);
		});
	}
	function getAliasIP(offset) {
        url = dutils.urls.resolve('aliasip', { key: clientKey}) + "?o=" + offset;
		$.getJSON(url, function(data) {
			$("#tableip").html("");
			$.each(data.items, function(key, value) {
				var html = "";
				html += "<tr class=\"aliasrow\">";
				html += "<td><a href=\"";
				html += value.ip_url;
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
			pagination('ip', data.offset, data.hasMore, data.pages, data.total);
		});
	}
</script>

<%
Integer permission = (Integer) request.getAttribute("permission");
Server server = (Server) request.getAttribute("server");
PlayerViewBean player = (PlayerViewBean) request.getAttribute("player");

if (UserServiceFactory.getUserService().hasPermission(server.getKey(), server.getPermissions().get(RemotePermissions.REMOVE_NOTICE))) {
	request.setAttribute("canRemoveNotice", true);
}

if (player.getBanInfo() == null) {
	if ((permission & RemotePermissions.ADD_BAN) == RemotePermissions.ADD_BAN) {
		if (UserServiceFactory.getUserService().hasPermission(server.getKey(), server.getPermissions().get(RemotePermissions.ADD_BAN))) {
			request.setAttribute("addban", true);
		}
	}
} else {
	if ((permission & RemotePermissions.REMOVE_BAN) == RemotePermissions.REMOVE_BAN) {
		if (UserServiceFactory.getUserService().hasPermission(server.getKey(), server.getPermissions().get(RemotePermissions.REMOVE_BAN))) {
			request.setAttribute("delban", true);
		}
	}
}

%>

<fieldset class="playerheader shadowbox">
<c:if test="${hasServerAdmin}">
<div id="player_menu" class="player_menu">
<div class="player_menu_box">
	<ul>
		<li class="icon add link" id="addnote">Nueva nota</li>
		<c:if test="${addban}">
		<li class="icon ban link" id="addpenalty">Banear</li>		
		</c:if>
		<c:if test="${delban}">
		<li class="icon delete link confirm" alt="<url:url name="delete-penalty"><url:param name="key" value="${player.banInfo.key}"/></url:url>?k=${player.key}">Levantar Ban</li>
		</c:if>
	</ul>
</div>
</div>
</c:if>
<legend>${fn:escapeXml(player.name)}<span class="icon <c:choose>
                    <c:when test="${not empty player.banInfo}">
                        banned
                    </c:when>
                    <c:when test="${player.playing}">
                        online
                    </c:when>
                    <c:otherwise>
                        offline
                    </c:otherwise>
                </c:choose>"></span></legend>
	<strong>Id:</strong> ${player.clientId}<br />
	<strong>Visto:</strong> <fmt:formatDate	type="both" pattern="dd-MM-yyyy HH:mm:ss" value="${player.updated}" /><br/>
    <strong>Servidor:</strong> <a href="<url:url name="serverfilter"><url:param name="query" value="${player.server.key}"/></url:url>">${player.server.name}</a><br />
    <strong>IP:</strong> <a href="<url:url name="search"><url:param name="query" value="${player.ip}"/></url:url>">${player.ip}</a>&nbsp;<a target="_blank" href="http://whois.domaintools.com/${player.ipZero}" title="Whois" class="icon vcard"></a><br />
    <strong>Nivel:</strong> ${player.level}<br />
    <c:if test="${not empty player.banInfo}">
	   <strong>Estado:</strong> ${player.banInfo}<br />
    </c:if>
    <c:if test="${not empty player.guid}">
    <br/>
    	<iddb:choose>
			<iddb:whenvalidguid test="${player.guid}">
				<span class="icon guid-ok">Este jugador posee una GUID válida.</span>
			</iddb:whenvalidguid>   	
    		<iddb:otherwise>
    			<span class="icon guid-nok">La GUID (${player.guid}) de este jugador no es válida.</span>
    		</iddb:otherwise>
    	</iddb:choose>
    </c:if>
    </fieldset>
    <br />

<c:if test="${not empty notices}">
<table>
	<caption>Notas</caption>
	<thead>
		<tr>
			<th></th>
			<th style="width: 140px;">Admin</th>
			<th style="width: 160px;">Agregado</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${notices}" var="notice">
		<tr>
			<td>${notice.reason}</td>
			<td>
			<c:choose>
				<c:when test="${not empty notice.admin}">${notice.admin}</c:when>
				<c:otherwise>-</c:otherwise>
			</c:choose> </td>
			<td><fmt:formatDate	type="both" pattern="dd-MM-yyyy HH:mm:ss" value="${notice.created}" />
			<c:if test="${canRemoveNotice}">
			<span alt="<url:url name="delete-notice"><url:param name="key" value="${notice.key}"/></url:url>?k=${player.key}" class="icon delete tip link confirm" title="Eliminar"></span>
			</c:if>
			</td>
		</tr>
	</c:forEach>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
	</tfoot>
</table>
<br/>
</c:if>

<table>
	<caption>Aliases</caption>
	<thead>
		<tr>
			<th>Alias</th>
			<th style="width: 160px;">Visto</th>
			<th style="width: 40px;">Usos</th>			
		</tr>
	</thead>
	<tbody id="tablealias">
	<tr><td colspan="3" style="text-align: center; background-color: #EEEEEE; "><img src='${pageContext.request.contextPath}/media/images/metabox_loader.gif'/></td></tr>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="3">
			<span style="font-size: smaller;">Total: <span id="total-alias">0</span></span>
			<div class="pagination"><span class='prev-na' id='prev-alias'><a>&laquo; Anterior</a></span><span class='curr' id='curr-alias'>-</span><span id='next-alias' class='next-na'><a>Siguiente &raquo;</a></span></div>
			</td>
		</tr>
	</tfoot>
</table>
<br/>
<table>
	<caption>IPs</caption>
	<thead>
		<tr>
			<th>IP</th>
			<th style="width: 160px;">Visto</th>
			<th style="width: 40px;">Usos</th>			
		</tr>
	</thead>
	<tbody id="tableip">
		<tr><td colspan="3" style="text-align: center; background-color: #EEEEEE;"><img src='${pageContext.request.contextPath}/media/images/metabox_loader.gif'/></td></tr>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="3">
			<span style="font-size: smaller;">Total: <span id="total-ip">0</span></span>
			<div class='pagination'><span class='prev-na' id='prev-ip'><a>&laquo; Anterior</a></span><span class='curr' id='curr-ip'>-</span><span id='next-ip' class='next-na'><a>Siguiente &raquo;</a></span></div>
			</td>
		</tr>
	</tfoot>
</table>
<br/>
<c:if test="${not empty events}">
<table>
	<caption>Eventos</caption>
	<thead>
		<tr>
			<th>Tipo</th>
			<th>Estado</th>
			<th style="width: 160px;">Fecha</th>
			<th style="width: 140px;">Admin</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${events}" var="event">
		<tr>
			<td>${event.type}</td>
			<td>${event.status}</td>
			<td><fmt:formatDate	type="both" pattern="dd-MM-yyyy HH:mm:ss" value="${event.updated}" /></td>
			<td>
			<c:choose>
				<c:when test="${not empty event.admin}">${event.admin}</c:when>
				<c:otherwise>-</c:otherwise>
			</c:choose>
			</td>
			
		</tr>
	</c:forEach>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
	</tfoot>
</table>
<br/>
</c:if>

<script type="text/javascript">
$(document).ready(
	function() {
		setTimeout("getAlias(1)", 500);
		setTimeout("getAliasIP(1)", 1000);

		$("#addnote").click(function() {
			var dialog = $("#add-note-dialog");
			$(dialog).find('[name="reason"]').val('');
			open_dialog(dialog);
		});

		$("#addpenalty").click(function() {
			var dialog = $("#add-ban-dialog");
			$(dialog).find('[name="reason"]').val('');
			$(dialog).find('[name="duration"]').val('');
			$(dialog).find('[name="dt"]').val('d');
			open_dialog(dialog);
		});
		
		$("#player_menu").hover(function() {
			$(".player_menu_box").show();
		},
		function() {
			$(".player_menu_box").hide();
		});
		
		$(".confirm").click(function() {
			var url = $(this).attr('alt');
			$("#dialog-confirm").dialog({
				resizable: false,
				height:140,
				modal: true,
				title: 'Confirmar',
				buttons: {
					"Eliminar": function() {
						window.location = url;
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});			
		});
	}
)
function open_dialog(d) {
	$(d).dialog({
		hide: "explode",
		modal: true,
		buttons: {
			"Guardar": function() {
				$(d).find("form").submit();
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		},				
	});
}
</script>

<div id="dialog-confirm" style="display: none;">
Confirma eliminaci&oacute;n?
</div>

<div id="add-note-dialog" class="dialog" style="display: none;">
<form action="<url:url name="add-notice"/>" method="post">
<fieldset>
	<input type="hidden" name="k" value="${player.key}"/>
	<label for="name">Motivo</label><br/>
	<input type="text" name="reason" class="text ui-corner-all" /><br/>
</fieldset>
</form>
</div>
<div id="add-ban-dialog" class="dialog" style="display: none;">
<form action="<url:url name="add-penalty"/>" method="post">
<fieldset>
	<input type="hidden" name="k" value="${player.key}"/>
	<label for="name">Motivo</label><br/>
	<input type="text" name="reason" class="text ui-corner-all" /><br/>
	<label for="name">Duración</label><br/>
	<input type="text" name="duration" class="text ui-corner-all" /><br/>
	<label for="name">Periodo</label><br/>
	<select name="dt" class="text ui-corner-all">
		<option value="m">Minutos</option>
		<option value="d">Días</option>
		<option value="w">Semanas</option>
		<option value="M">Meses</option>
		<option value="y">Años</option>
	</select>
	<br/>		
</fieldset>
</form>
</div>