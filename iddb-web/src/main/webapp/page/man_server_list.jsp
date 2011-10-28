<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/tld/urlresolver.tld" prefix="url"%>

<jsp:include page="/include/jqueryui.jsp"/>

<c:if test="${not empty servers}">
<table>
	<tbody>
		<c:forEach items="${servers}" var="server">
			<tr>
				<td>${server.name}</td>
				<td style="width: 120px;"><a class="button" href="<url:url name="manage-server"><url:param name="key" value="${server.key}"/></url:url>">Administrar</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</c:if>

<script type="text/javascript">
$(document).ready(
	function() {
		$(".button").button();
	});

</script>

