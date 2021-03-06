<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/urlresolver.tld" prefix="url"%>
<%@ taglib uri="/WEB-INF/tld/ipdbs.tld" prefix="util"%>
<%@ taglib uri="/WEB-INF/tld/geoip.tld" prefix="geo"%>

<json:object>
	<json:property name="hasMore" value="${hasMore}" />
	<json:property name="total" value="${total}" />
	<json:property name="offset" value="${offset}" />
	<json:property name="pages" value="${pages}" />
	<json:array name="items" var="item" items="${list}">
		<json:object>
            <c:choose>
                <c:when test="${not empty item.nickname}">
                    <url:url name="search" var="url"><url:param name="query" value="${item.nickname}"/><url:param name="match" value="exact"/></url:url>
                    <json:property name="nickname" value="${item.nickname}"/>
                    <json:property name="nickname_url" value="${url}"/>
                </c:when>
                <c:otherwise>
					<c:choose>
	                    <c:when test="${hasAdmin}">
	                        <url:url name="search" var="url"><url:param name="query" value="${item.ip}"/></url:url>
	                    </c:when>
	                    <c:otherwise>
	                        <url:url name="search" var="url"><url:param name="query" value="${item.ipSearch}"/></url:url>
	                    </c:otherwise>
	                </c:choose>
                    <util:maskip value="${item.ip}" var="ip"/>
                    <geo:geo ip="${item.ip}" var="geoimg"/>
                    <util:whois ip="${item.ip}" var="whoisip"/>
                    <json:property name="ip" value="${ip}" />
                    <json:property name="ip_url" value="${url}" />
                    <json:property name="geo_img" value="${geoimg}" />
                    <json:property name="whois" value="${whoisip}" />
                </c:otherwise>
            </c:choose>
			<json:property name="updated">
				<fmt:formatDate value="${item.updated}" type="both"
					timeZone="GMT-3:00" pattern="dd-MM-yyyy HH:mm:ss" />
			</json:property>
			<json:property name="count" value="${item.count}" />
		</json:object>
	</json:array>
</json:object>
