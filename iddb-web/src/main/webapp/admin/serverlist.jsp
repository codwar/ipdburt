<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/tld/urlresolver.tld" prefix="url"%>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/media/ui-darkness/jquery-ui-1.8.16.custom.css" media="screen"/>

<button id="create-server">Nuevo</button>
<table border="1" style="margin-top: 5px;">
    <thead>
        <tr>
            <th>Nombre</th>
            <th>Clave</th>
            <th>Contacto</th>
            <th>IP</th>
            <th>Versi&oacute;n</th>
            <th>Activo</th>
        </tr>
    </thead>
    <tbody class="server-list-edit">
        <c:forEach items="${servers}" var="server">
            <tr id="key-${server.key}">
            	<td style="display: none;">${server.key}</td>
                <td>${server.name}</td>
                <td>${server.uid}</td>
                <td>${server.adminEmail}</td>
                <td>${server.address}</td>
                <td>${server.pluginVersion}</td>
                <td>
	                <c:choose>
	                    <c:when test="${server.disabled}">
	                        No
	                    </c:when>
	                    <c:otherwise>
	                        Si
	                    </c:otherwise>
	                </c:choose>                
                </td>
                <!--<td><a href="<url:url name="admin-getserver"><url:param name="key" value="${server.key}"/></url:url>" class="icon edit"></a></td>-->
            </tr>
        </c:forEach>
    </tbody>
</table>

<script type="text/javascript">
$(document).ready(
	function() {
		$("button").button();
		$("#create-server").click(function() {
			$("#edit-dialog").find('[name="k"]').val('');
			$("#edit-dialog").find('[name="name"]').val('');
			$("#edit-dialog").find('[name="admin"]').val('');
			$("#edit-dialog").find('[name="ip"]').val('');
			$("#edit-dialog").find('[name="disable"]').attr('checked', false);
			open_dialog();
		});
		
		$(".server-list-edit").find("tr").dblclick(function() {
			var values = new Array();
			var arrayIndex = 0;
			$(this).find("td").each(function() {
				values[arrayIndex] = $(this).text();
				arrayIndex++;
			});
			$("#edit-dialog").find('[name="k"]').val(values[0]);
			$("#edit-dialog").find('[name="name"]').val(values[1]);
			$("#edit-dialog").find('[name="admin"]').val(values[3]);
			$("#edit-dialog").find('[name="ip"]').val(values[4]);
			if ($.trim(values[6]) == "No") {
				d = true;
			} else {
				d = false;
			}
			$("#edit-dialog").find('[name="disable"]').attr('checked', d);
			open_dialog();
		});
	}
)

function open_dialog() {
	$("#edit-dialog").dialog({
		hide: "explode",
		modal: true,
		buttons: {
			"Guardar": function() {
				$("#edit-dialog").find("form").submit();
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		},				
	});
}
</script>

<div id="edit-dialog" class="dialog" style="display: none;">
<form action="<url:url name="admin-saveserver"/>" method="post">
<fieldset>
	<input type="hidden" name="k"/>
	<label for="name">Name</label><br/>
	<input type="text" name="name" class="text ui-corner-all" /><br/>
	<label for="admin">Contacto</label><br/>
	<input type="text" name="admin" class="text ui-corner-all" /><br/>
	<label for="ip">IP</label><br/>
	<input type="text" name="ip" class="text ui-corner-all"><br/>&nbsp;<sub>(dejar en blanco para no verificar)</sub><br/>
	<label for="ip">Deshabilitar</label><input type="checkbox" name="disable" class="checkbox ui-corner-all"/>
</fieldset>
</form>
</div>
	
<!-- 
<form action="<url:url name="admin-saveserver"/>" method="post">

<label for="name">Nombre:</label><input type="text" value="${server.name}" name="name" /><br/>      
<label for="admin">Contacto:</label><input type="text" value="${server.adminEmail}" name="admin" /><br/>
<label for="ip">IP</label><input type="text" value="${server.address}" name="ip"> <sub>(dejar en blanco para no verificar)</sub><br/>
</form> -->