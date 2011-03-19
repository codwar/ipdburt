<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<jsp:include page="/alias" />

<json:object>
	<json:array name="items" var="item" items="${list}">
		<json:object>
			<json:property name="nickame" value="${item.nickname}" />
			<json:property name="ip" value="${item.ip}" />
			<json:property name="updated" value="${item.updated}" />
			<json:property name="count" value="${item.count}" />
		</json:object>
	</json:array>
</json:object>
