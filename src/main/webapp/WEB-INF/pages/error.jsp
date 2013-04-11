<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page isErrorPage="true"%>
{"500":"Internal Server Error"}
<%
	response.setStatus(500);
%>