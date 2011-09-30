<%@page language="java" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div style="text-align: center; margin: 80px 40px 80px 40px">
<div style="font-size: large;"><span style="color: red">You</span> just did the lemming thing.</div>

<% try { /* Google bug */ %>
<div style="font-size: xx-large;">
<c:choose>
	<c:when test="${pageContext.errorData.statusCode == 403 }">
		No tienes suficientes privilegios para acceder a esta secci&oacute;n
	</c:when>
	<c:otherwise>
		HTTP Error ${pageContext.errorData.statusCode}
	</c:otherwise>
</c:choose>
</div>
<% } catch(Exception e) { /*Ignore*/  } %>
</div>
