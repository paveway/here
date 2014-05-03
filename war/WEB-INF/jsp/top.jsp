<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta id="viewport" name="viewport" content="width=320; initial-scal=1.0; maximum-scale=1.0; user-scalable=0;" />
    <link rel="stylesheet" href="/stylesheets/iphone.css" />
    <title>ココにイル - トップ画面</title>
  </head>

  <body>
    <div id="header" class="pre">
      <p>
        <c:if test="${not empty user}">
          <c:out value="${nickname}" />
        </c:if>
        <c:if test="${empty user}">
          ログインしていません
        </c:if>
      </p>
      <h1>トップ画面</h1>
      <a href="/login">ログイン</a>
    </div>
  </body>
</html>
