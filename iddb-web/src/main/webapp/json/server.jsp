<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<json:object>
	<json:array name="list" var="item" items="${list}">
		<json:object>
            <json:property name="key" value="${item.keyString}"/>
            <json:property name="count" value="${item.onlinePlayers}"/>
            <json:property name="name" value="${item.name}"/>
		</json:object>
	</json:array>
</json:object>