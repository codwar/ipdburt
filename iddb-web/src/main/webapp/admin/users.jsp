<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tld/urlresolver.tld" prefix="url"%>

<jsp:include page="/include/jqueryui.jsp"/>

<table style="margin-top: 5px;">
	<thead>
		<tr>
			<th>Usuario</th>
			<th>Relaciones</th>
		</tr>
	</thead>
    <tbody>
        <c:forEach items="${users}" var="user">
        	<tr>
        		<td style="background-color: #F7F7F7;"><input type="checkbox" value="${user.key}" name="duserkey"> ${user.username}</td>
        		<td style="background-color: #EEEEEE;">
        			<ul>
       				<c:forEach items="${user.list}" var="list">
       					<li><a href="<url:url name="serverfilter"><url:param name="query" value="${list.server.key}"/></url:url>">${list.server.name}</a>
       					&nbsp;-&nbsp;
       					<a href="<url:url name="playerinfo"><url:param name="key" value="${list.player.key}"/></url:url>">${fn:escapeXml(list.player.nickname)}</a>
       					&nbsp;
       					(${list.player.level}) <span class="icon delete tip link confirm" title="Eliminar">&nbsp;</span></li>
       				</c:forEach>
       				</ul>
        		</td>
        	</tr>
        </c:forEach>
    </tbody>
</table>
<br/>