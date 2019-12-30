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
				<form action="javascript:void(0)">
					<p>
						<label for="name">用户名称</label>
						<input id="name" name="name" type="text" placeholder="用户名称" minlength="4" maxlength="20" />
					</p>
					<p>
						<label for="password">用户密码</label>
						<input id="password" name="password" type="password" placeholder="用户名称" minlength="8" maxlength="20" />
					</p>
					<p>
						<button id="login" class="button" type="submit">登陆</button>
					</p>
				</form>
			</div>
		</div>
		<#include "/include/footer.ftl">
	    <script type="text/javascript">
	    	var token = "${token}";
			var encrypt = new JSEncrypt();
			$.get("/rsa/public/key", function(key) {
				encrypt.setPublicKey(key);
			});
			$(function() {
				$('#login').click(function() {
					var name = $("#name").val();
					var password = $("#password").val();
					if(
						!name || name.length < 4 ||
						!password || password.length < 8
					) {
						alert("登陆信息格式错误");
						return;
					}
					var encrypted = encrypt.encrypt(password);
					$.post("/login", {"name" : name, "password" : encrypted, "token" : token}, function(message) {
						if("0000" === message.code) {
							location.href = "${uri}";
						} else {
							token = message.token;
							alert(message.message);
						}
					});
				});
			});
	    </script>
	</body>
</html>