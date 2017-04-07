<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Page View Log</title>
  <script src="http://code.jquery.com/jquery-1.8.3.js"></script>
  <script type="text/javascript">
    $(function(){
      $.ajax({
        type: "GET",
        url: "/log/userLogview",
//        async:true,
        success: function (resp) {
          resp = eval("("+resp+")");
          var html="";
          $.each(resp.data,function(i,record){
            html = html + '<tr> <td>'+record+'</td> </tr>';
          });
          $('#logtablebody').html(html);
        }
      });
    });
  </script>
</head>
<body>
<table border=2 width=70% align=center id="logtable">
  <caption>Staging Page View Log</caption>
  <thead>
  <tr>
    <th>Log Details</th>
  </tr>
  </thead>
  <tbody id="logtablebody">
  </tbody>
</table>
</body>
</html>
