<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<jsp:include page="/alias" />

<json:object>
	<json:array name="items" var="item" items="${list}">
		<json:object>
			<json:property name="nickname" value="${item.nickname}" />
			<json:property name="ip" value="${item.ip}" />
			<json:property name="updated">
				<fmt:formatDate value="${item.updated}" type="both"
					timeZone="GMT-3:00" pattern="dd-MM-yyyy HH:mm:ss" />
			</json:property>
			<json:property name="count" value="${item.count}" />
		</json:object>
	</json:array>
</json:object>
