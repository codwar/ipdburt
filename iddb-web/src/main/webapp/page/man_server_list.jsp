<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/tld/urlresolver.tld" prefix="url"%>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/ui-darkness/jquery-ui-1.8.16.custom.css" media="screen"/>

<table>
	<tbody>
		<c:forEach items="${servers}" var="server">
			<tr>
				<td>${server.name}</td>
				<td><a class="button" style="color: #fff;" href="<url:url name="manage-server"><url:param name="key" value="${server.key}"/></url:url>">Administrar</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<script type="text/javascript">
$(document).ready(
	function() {
		$(".button").button();
	});

</script>

