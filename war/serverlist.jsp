<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/serverlist" />

<table border="1">
	<c:forEach items="${servers}" var="server">
		<tr>
			<td>${server.name}</td>
			<td>${server.uid}</td>
			<td>${server.admin.email}</td>
		</tr>
	</c:forEach>
</table>

<form action="serversave.jsp" method="post">Name: <input
	type="text" value="" name="name" /><br />
Admin: <input type="text" value="" name="admin" /><br />
UID: <input type="text" value="" name="uid" /><br />
<input type="submit" /></form>

