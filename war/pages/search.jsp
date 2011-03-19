<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<small>
<form action="" method="get"><input class="search" type="text" name="alias"
	value="${param.alias}" /> <input type="submit" value="Search" /></form>
</small>
<jsp:include page="/search" />

<table>
	<thead>
		<tr>
			<th></th>
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
            <c:set var="rowStyle" scope="page" value="odd"/>
          </c:when>
          <c:otherwise>
            <c:set var="rowStyle" scope="page" value="even"/>
          </c:otherwise>
        </c:choose>
        <tr class="<c:out value="${rowStyle}"/>">	
			<td><a href="alias.jsp?id=${player.key}">${player.key}</a></td>
			<td>${player.name}</td>
			<td>${player.ip}</td>
			<td>${player.latest}</td>
			<td>${player.server}</td>
		</tr>
	</c:forEach>
	</tbody>
	<tfoot></tfoot>
</table>