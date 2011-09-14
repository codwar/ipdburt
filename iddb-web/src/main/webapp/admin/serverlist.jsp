<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table border="1">
    <thead>
        <tr>
            <th>Nombre</th>
            <th>Clave</th>
            <th>Contacto</th>
            <th>IP</th>
            <th>Versi&oacute;n</th>
            <th></th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${servers}" var="server">
            <tr>
                <td>${server.name}</td>
                <td>${server.uid}</td>
                <td>${server.adminEmail}</td>
                <td>${server.address}</td>
                <td>${server.pluginVersion}</td>
                <td><a href="/admin/server/${server.key}/get/" class="icon edit"></a></td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<fieldset>
<c:choose>
  <c:when test="${empty server}">
    <legend>Nuevo Servidor</legend>
   </c:when>
  <c:otherwise>
    <legend>Editando ${server.name}</legend>
  </c:otherwise>
</c:choose>
<form action="/admin/server/save/" method="post">
<input type="hidden" name="k" value="${server.key}"/>
<label for="name">Nombre:</label><input type="text" value="${server.name}" name="name" /><br/>      
<label for="admin">Contacto:</label><input type="text" value="${server.adminEmail}" name="admin" /><br/>
<label for="ip">IP</label><input type="text" value="${server.address}" name="ip"> <sub>(dejar en blanco para no verificar)</sub><br/>
<input type="submit" value="Guardar" />
</form>
</fieldset>
