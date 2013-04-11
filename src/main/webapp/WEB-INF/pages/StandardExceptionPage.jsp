<%@ page language="java" contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isErrorPage="true"%>
{"${exception.errorId}":"${exception.errorMessage}"}
<c:set var="errorCode" value="${exception.errorId}" />
<%
	response.setStatus(Integer.parseInt(pageContext.getAttribute("errorCode").toString()));
%>
