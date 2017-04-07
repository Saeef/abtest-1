<%@ page import="java.util.Date" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/5/19
  Time: 9:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="common/taglibs.jsp"%>
<html lang="zh-CN">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Vuclip - Abtest Login Page</title>
  <%@include file="common/css.jsp"%>
  <link rel="stylesheet" href="${css}/main.css" />
  <%@include file="common/js.jsp"%>
  <script type="text/javascript" src="${js}/login/login.js"></script>
</head>

<body>
<div class="logoDiv">
  <a href="/"><img src="${images}/vuclip_big.png" alt="vuclip logo" width="250px" height="150px"></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Vuclip Abtest Manager
</div>
  <div id="main" style="height:60%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="border-bottom:1px solid #f9b331;height:100%;margin-top:100px;background-color: #F9B331">
      <tbody><tr>
        <td style="background: url(${images}/vuclip_bg.png) 100px 0px no-repeat #F9B331 ;border-right:none;background-size:250% 100%"></td>
        <td width="30%" align="center" style="border-right: 1px solid #fff;background:#F9B331 ;">
          <!-- img src="/images/vuclipLogin.png" alt="Vuclip" /-->
          <div class="loginContainer" style="margin-right:50px;">
            <div id="login" style="height:380px;">
              <h3 style="font-size:200%;">Welcome to Vuclip</h3>
              <h3 style="font-size:200%;margin-top:5px;padding-bottom:20px;border-bottom:1px solid #9A928B;">Abtest Manager!</h3>
            </div>
          </div>
        </td>
        <td width="30%" align="left" style="background:#F9B331 ;border-right:none;">
          <div class="loginContainer" style="margin-left:50px;">
              <form class="form-signin" action="/user/login" onsubmit="return checkLogin();">
                <span id="loginAlert" style="color: #FF0000"><c:if test="${loginAlert!=null}">${loginAlert}</c:if> </span>
                <label class="bg">
                  <input type="text" name="username" id="username" placeholder="Username" autocomplete="off">
                  <span id="unamestatus" class="textStyle">&nbsp;</span>
                </label>
                <label class="bg">
                  <input type="password" name="password" id="password" placeholder="Password" autocomplete="off">
                  <span id="passstatus" class="textStyle">&nbsp;</span>
                </label>
                <%--<img src="/user/captcha?bg=1&deep=<%= new Date().getTime() %>" alt="captcha" id="captchaimg"/>
                <label class="bg">
                  <input type="text" name="captcha" id="captcha" placeholder="Enter the above word here" autocomplete="off">
                  <span id="captchastatus" class="textStyle">&nbsp;</span>
                </label>--%>
                <%--<div class="checkbox">--%>
                <%--<label>--%>
                <%--<input type="checkbox" value="remember-me"> Remember me--%>
                <%--</label>--%>
                <%--</div>--%>
                <button id="doLogin" class="btn btn-primary btn-block" type="submit">Sign&nbsp;&nbsp;&nbsp;in</button>
                <button id="regist" class="btn btn-primary btn-block" type="button">Sign&nbsp;&nbsp;&nbsp;up</button>
                <a href="javascript:void(0);" style="float: right;text-decoration:none;" id="forgetpass" >forget password?</a>
              </form>
            <!--login end-->
          </div>
          <!--loginContainer end-->
        </td><td width="20%" style="border-right:none;"></td>
      </tr>
      </tbody></table>
  </div>
<script type="text/javascript">
  function checkLogin(){
    var uname = trim($('#username').val());
    var pass = trim($('#password').val());
    var captcha = trim($('#captcha').val());
    var checked = true;
    $('#unamestatus').html("&nbsp;");
    $('#passstatus').html("&nbsp;");
    $('#captchastatus').html("&nbsp;");
    if(uname==""||pass==""||captcha==""){$('#loginAlert').html("Username or Password or Captcha can't be empty.");checked = false;}
    return checked;
  }
</script>
</body></html>
