<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib uri='/WEB-INF/tld/template.tld' prefix='template'%>

<template:insert template='/layout.jsp'>
	<template:put name='flash' content='/pages/flash.jsp' />
	<template:put name='content' content='/pages/playerinfo.jsp' />
</template:insert>
