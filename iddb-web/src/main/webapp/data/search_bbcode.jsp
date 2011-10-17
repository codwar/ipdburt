<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div id="copycontent">
[table]<br/>
    [thead]
		[tr]
			[th]Id[/th]
			[th]Nombre[/th]
			[th]IP[/th]
			[th]Visto[/th]
			[th]Servidor[/th]
			[th]Estado[/th]
		[/tr]<br/>
	[/thead]<br/>
		<c:forEach items="${list}" var="player">
			[tr]
				[td]${player.clientId}[/td]
				[td]${fn:escapeXml(player.name)}[/td]
				[td]${player.ip}[/td]
				[td]<fmt:formatDate value="${player.latest}" type="both" timeZone="GMT-3:00" pattern="dd-MM-yyyy HH:mm:ss" />[/td]
				[td]${player.server.name}[/td]
				[td]
                <c:choose>
                    <c:when test="${player.banned}">
						<c:choose>
	                    <c:when test="${empty player.baninfo}">
	                        Baneado
	                    </c:when>
	                    <c:otherwise>
	                        ${player.baninfo}
	                    </c:otherwise>
	                	</c:choose>
                    </c:when>
                    <c:otherwise>
                        -
                    </c:otherwise>
                </c:choose>
                [/td]
            [/tr]<br/>
		</c:forEach>
[/table]
</div>
<!-- 
<script type="text/javascript">
$(function() {
	$("#copycontent").click(function() {SelectText("copycontent")});
});
</script>
 -->
