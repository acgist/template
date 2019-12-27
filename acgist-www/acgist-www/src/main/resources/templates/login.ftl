<!DOCTYPE HTML>
<html>
	<head>
		<title>登陆</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width" />
		<meta name="keywords" content="ACGIST" />
		<meta name="description" content="ACGIST" />
		
		<#include "/include/resources.ftl">
		<script type="text/javascript" src="${staticUrl}/resources/js/jsencrypt.min.js"></script>
	</head>

	<body>
		<#include "/include/header.ftl">
		<div class="main">
		首页
		</div>
		<#include "/include/footer.ftl">
	    <script type="text/javascript">
			$(function() {
				$('#testme').click(function() {
					var encrypt = new JSEncrypt();
					encrypt.setPublicKey("PublicKey 1024");
					var encrypted = encrypt.encrypt("value");
				});
			});
	    </script>
	</body>
</html>