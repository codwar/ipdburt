<%@page import="iddb.web.security.service.UserPermission"%>
<%@page import="iddb.web.security.service.UserServiceFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/urlresolver.tld" prefix="url"%>
<%@ taglib uri="/WEB-INF/tld/ipdbs.tld" prefix="iddb"%>
<%@ taglib uri="/WEB-INF/tld/geoip.tld" prefix="geo"%>

<script type="text/javascript">
    dutils.conf.urls.alias = "<url:clean name="alias"/>";
    dutils.conf.urls.aliasip = "<url:clean name="alias-ip"/>";
</script>


<% if (UserServiceFactory.getUserService().hasAnyServer(UserPermission.LEVEL_MOD)) { %>
<%-- THIS IS INSIDE SCRIPTLET IF --%>
<script type="text/javascript">
$(function() {
	$(".banned").tipTip({attribute: "alt", defaultPosition: "right"});
});
<% } %>
</script>

<table id="search-result">
	<thead>
		<tr>
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
				<td style="text-align: right;">
				<a title="Mostrar más información" href="<url:url name="playerinfo"><url:param name="key" value="${player.key}"/></url:url>">${player.clientId}</a>
				</td>
				<td class="icon
				<c:choose>
                    <c:when test="${player.banned}">
                        banned
                    </c:when>
                    <c:when test="${player.playing}">
                        online
                    </c:when>
                    <c:otherwise>
                        offline
                    </c:otherwise>
                </c:choose>
				" alt="${player.baninfo}"><span class="plus" alt="alias" id="plus-${player.key}">[+]</span>
				<span class="minus"	id="minus-${player.key}" style="display: none;">[-]</span>
				<span>
				<a href="<url:url name="search"><url:param name="query" value="${player.name}"/><url:param name="match" value="exact"/></url:url>">${fn:escapeXml(player.name)}</a>
				</span>
				<geo:geo ip="${player.ip}"/>
			    <c:if test="${not empty player.guid}">
			    	<iddb:choose>
						<iddb:whenvalidguid test="${player.guid}">
						</iddb:whenvalidguid>   	
			    		<iddb:otherwise>
			    			<span class="icon guid-nok">&nbsp;</span>
			    		</iddb:otherwise>
			    	</iddb:choose>
			    </c:if>
				</td>
				<td>
				<span class="plus" alt="ip" id="plus-ip-${player.key}">[+]</span>
				<span class="minus"	id="minus-ip-${player.key}" style="display: none;">[-]</span>
				<c:choose>
                    <c:when test="${hasAdmin}">
                        <url:url name="search" var="ipurl"><url:param name="query" value="${player.ip}"/></url:url>
                    </c:when>
                    <c:otherwise>
                        <url:url name="search" var="ipurl"><url:param name="query" value="${player.ipSearch}"/></url:url>
                    </c:otherwise>
                </c:choose>				
				<a href="${ipurl}"><iddb:maskip value="${player.ip}"/></a>&nbsp;<a
					target="_blank"
					href="http://whois.domaintools.com/${player.ipZero}" title="Whois"
					class="icon vcard"></a>
					</td>
				<td style="text-align: right;">
                <fmt:formatDate value="${player.latest}" type="both" timeZone="GMT-3:00" pattern="dd-MM-yyyy HH:mm:ss" />
                </td>
				<td><a href="<url:url name="serverfilter"><url:param name="query" value="${player.server.key}"/></url:url>">${player.server.name}</a></td>
			</tr>
			<tr style="display: none;">
				<td colspan="6" style="padding: 20px;">
				<table>
					<thead>
						<tr>
							<td colspan="4" style="text-align: right;">
							<a href="<url:url name="playerinfo"><url:param name="key" value="${player.key}"/></url:url>" class="icon details">Todo</a></td>
						</tr>
						<tr>
							<th></th>
							<th style="width: 160px;">Visto</th>
							<th style="width: 40px;">Usos</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="3">
							<span style="font-size: smaller;">Total: <span id="total-alias">0</span></span>
							<div class='pagination'><span class='prev-na' id='prev-alias'><a>&laquo; Anterior</a></span><span class='curr' id='curr-alias'>-</span><span id='next-alias' class='next-na'><a>Siguiente &raquo;</a></span></td>
						</tr>
					</tfoot>
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
			<td colspan="6">
			<span style="font-size: smaller;">Total:
			${count} (${time} ms)</span>
			<c:choose>
			<c:when test="${not empty current_rule}">
                <c:choose>
                    <c:when test="${not empty query}">
                        <url:url name="${current_rule}" var="url">
                            <url:param name="query" value="${query}"/>
                        </url:url>
                    </c:when>
                    <c:otherwise>
                        <url:url name="${current_rule}" var="url"/>
                    </c:otherwise>
                </c:choose>
            </c:when>  
            <c:otherwise>
                <c:set var="url" value="/"/>
            </c:otherwise>
            </c:choose>
            <iddb:paginator
                totalPages="${pageLink.totalPages}"
                currentPage="${pageLink.pageNumber}" pageSize="${pageLink.pageSize}"
                url="${url}" />            
            </td>
		</tr>
	</tfoot>
</table>
<a href="#bbcode" class="button icon code bbcode">C&oacute;digo para foros</a>
<div style="display: none;" id="bbcode">
<jsp:include page="/data/search_bbcode.jsp"></jsp:include>
</div>