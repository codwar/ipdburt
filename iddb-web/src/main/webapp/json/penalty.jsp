<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<json:object>
	<c:if test="${not empty penalty}">
		<json:property name="key" value="${key}"/>
	    <json:property name="data" value="${penalty}"/>
	</c:if>
</json:object>