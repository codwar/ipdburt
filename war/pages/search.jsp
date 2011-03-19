<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form action="" method="get"><input type="text" name="alias"
	value="${param.alias}" /> <input type="submit" value="Search" /></form>

<jsp:include page="/search" />

<table border="1">
	<c:forEach items="${list}" var="player">
		<tr>
			<td>${player.key}</td>
			<td>${player.name}</td>
			<td>${player.ip}</td>
			<td>${player.latest}</td>
			<td>${player.server}</td>
		</tr>
	</c:forEach>
</table>