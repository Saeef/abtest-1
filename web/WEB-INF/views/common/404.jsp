<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">

	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta name="description" content="Xenon Boostrap Admin Panel" />
	<meta name="author" content="" />
	<title>Vuclip - 404 Page</title>

	<%@include file="css.jsp"%>
	<%@include file="js.jsp"%>


</head>
<body class="page-body page-error-env">

<div class="page-error centered">

	<div class="error-symbol">
		<i class="fa-warning"></i>
	</div>

	<h2>
		Error 404
		<small>Page not found!</small>
	</h2>

	<p>We did not find the page you were looking for!</p>

</div>

<div class="page-error-search centered">
	<a href="javascript:void(0);" class="go-back" onclick="window.history.go(-1)">
		<i class="fa-angle-left"></i>
		Go Back
	</a>
</div>

</body>
</html>