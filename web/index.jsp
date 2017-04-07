<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/5/15
  Time: 11:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body>
test
  <%
    String aa = request.getParameter("param");
    if(aa!=null && aa.equals("1")){
      response.getWriter().write("type is 1");
    }else{
      response.getWriter().write("i don't know the type.."+ aa);
    }
  %>
  </body>
</html>
