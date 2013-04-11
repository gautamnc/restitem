<%@ page language="java" contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isErrorPage="true"%>
{"404":"Not Found, Invalid REST Item URI"}
<%
	response.setStatus(404);
%>
