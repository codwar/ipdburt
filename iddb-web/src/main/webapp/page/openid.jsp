<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<form method="post">
	<input type="text" name="openid_identifier" /> <input type="submit"
		value="Enviar" />
</form>

<table>
	<tr>
		<th>Nickname:</th>
		<td>${nickname}</td>
	</tr>
	<tr>
		<th>Email:</th>
		<td>${email}</td>
	</tr>
	<tr>
		<th>Fullname:</th>
		<td>${fullname}</td>
	</tr>
	<tr>
		<th>Date of birth:</th>
		<td>${dob}</td>
	</tr>
	<tr>
		<th>Gender:</th>
		<td>${gender}</td>
	</tr>
	<tr>
		<th>Postcode:</th>
		<td>${postcode}</td>
	</tr>
	<tr>
		<th>Country:</th>
		<td>${country}</td>
	</tr>
	<tr>
		<th>Language:</th>
		<td>${language}</td>
	</tr>
	<tr>
		<th>Timezone:</th>
		<td>${timezone}</td>
	</tr>
</table>
