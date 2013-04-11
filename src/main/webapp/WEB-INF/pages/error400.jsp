<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page isErrorPage="true"%>
<%
   response.setStatus(400);
%>
{"400":"Invalid JSON POST Data"}