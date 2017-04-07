<%@ page import="java.util.Date" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/5/19
  Time: 14:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE HTML>
<html lang="zh-CN">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Vuclip - Abtest User Regist Page</title>
  <%@include file="../common/css.jsp"%>
  <link rel="stylesheet" href="/resources/css/user/user.css" />
  <%@include file="../common/js.jsp"%>
  <script type="text/javascript" src="/resources/js/user/user.js"></script>
</head>
<body>
<div id="main">
  <div id="loginDiv" style="line-height: 85px;/*margin-bottom:20px;margin-top:10px;*/">
    <a href="/"><img src="${images}/vuclip.png" style="margin-left:40px;" alt="Vuclip" />
    </a>
    <span style="margin-left: 20px; font-size:130%;">Partner Dashboard</span>
  </div>
  <!--header end-->
  <div id="navbar">
    <p style="text-align:center;background:#F9B331;color:#000;">
      <%--<c:choose>--%>
        <%--<c:when test="${doit==0}">Sign Up</c:when>--%>
        <%--<c:otherwise>用户注册</c:otherwise>--%>
      <%--</c:choose>--%>
        User Regist
    </p>
  </div>
  <!--navbar end-->
  <form id="registForm" class="form-signin">
    <span id="createstatus">&nbsp;</span>
    <label for="username" class="sr-only">Username:</label>
    <input type="text" name="username" id="username" class="form-control" placeholder="Username" required autocomplete="off">
    <div id = "usernameerr">&nbsp;</div>
    <label for="password" class="sr-only">Password</label>
    <input type="password" name="password" id="password" class="form-control" placeholder="Password" required>
    <div id = "passworderr">&nbsp;</div>
    <img src="/user/captcha?bg=0&deep=<%= new Date().getTime() %>" alt="captcha" id="captchaimg"/>
    <input type="text" name="captcha" id="captcha" class="form-control" placeholder="Enter the above word here" required>
    <div id = "captchaerr">&nbsp;</div>
    <%--<div class="checkbox">--%>
    <%--<label>--%>
    <%--<input type="checkbox" value="remember-me"> Remember me--%>
    <%--</label>--%>
    <%--</div>--%>
    <input type="hidden" value="0" name="isnew" id="isnew" />
    <button id="doRegist" class="btn btn-bg btn-primary" type="button">Sign up</button>
    <button id="doReset" class="btn btn-bg btn-primary" type="button">Reset</button>
  </form>


</div>
<!--main end-->
</body>
</html>
