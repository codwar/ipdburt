<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/serverlist" />

<table style="width: 85%; margin-left: auto; margin-right: auto;">
    <thead>
        <tr>
            <th style="width: 90px;">Conectados</th>
            <th>Servidor</th>
            <th style="width: 150px;">Actualizado</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${servers}" var="server">
            <tr>
                <td style="text-align: right;">${server.onlinePlayers}</td>
                <td>${server.name}</td>
                <td style="text-align: right;"><fmt:formatDate type="both" pattern="dd-MM-yyyy HH:mm:ss" value="${server.updated}"/></td>
            </tr>
        </c:forEach>    
    </tbody>
    <tfoot></tfoot>
</table>