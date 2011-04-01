<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/flash" />
<c:if test="${flash.count gt 0}">
	<ul class="messages">
		<c:forEach items="${flash.infos}" var="info">
			<li class="info">${info}</li>
		</c:forEach>
		<c:forEach items="${flash.oks}" var="ok">
			<li class="success">${ok}</li>
		</c:forEach>
		<c:forEach items="${flash.warns}" var="warn">
			<li class="warning">${warn}</li>
		</c:forEach>
		<c:forEach items="${flash.errors}" var="error">
			<li class="error">${error}</li>
		</c:forEach>
	</ul>
</c:if>