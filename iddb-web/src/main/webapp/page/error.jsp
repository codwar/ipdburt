<%@page language="java" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div style="text-align: center; margin: 80px 40px 80px 40px">
<div style="font-size: large;"><span style="color: red">You</span> just did the lemming thing.</div>

<% try { /* Google bug */ %><div style="font-size: xx-large;">HTTP
Error ${pageContext.errorData.statusCode}</div>
<% } catch(Exception e) { /*Ignore*/  } %>
</div>
