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
			<div class="login">
				<form id="login" action="javascript:void(0);">
					<p>
						<label for="name">用户名称</label>
						<input id="name" name="name" type="text" required="required" maxlength="20" placeholder="用户名称" />
					</p>
					<p>
						<label for="password">用户密码</label>
						<input id="password" name="password" type="password" required="required" maxlength="20" placeholder="用户密码" />
					</p>
					<p class="center">
						<button class="button" type="submit">登陆</button>
					</p>
				</form>
				<p class="register"><a href="/register">注册</a></p>
			</div>
		</div>
		<#include "/include/footer.ftl">
	    <script type="text/javascript">
	    	var token = "${SESSION_CSRF_TOKEN}";
			var encrypt = new JSEncrypt();
			$.get("/rsa/public/key", function(key) {
				encrypt.setPublicKey(key);
			});
			$(function() {
				$("#login").submit(function(e) {
					var name = $("#name").val();
					var password = $("#password").val();
					var encrypted = encrypt.encrypt(password);
					$.post("/login", {"name" : name, "password" : encrypted, "token" : token}, function(message) {
						if("0000" === message.code) {
							location.href = "${uri}";
						} else {
							token = message.token;
							alert(message.message);
						}
					});
					return false;
				});
			});
	    </script>
	</body>
</html>