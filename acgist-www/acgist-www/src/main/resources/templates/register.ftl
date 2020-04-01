<!DOCTYPE HTML>
<html>
	<head>
		<title>用户注册</title>
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
			<form id="register" action="javascript:void(0);">
				<input value="${SESSION_CSRF_TOKEN}" id="token" name="token" type="hidden" />
				<div class="user">
					<p>
						<label for="name">用户名称</label>
						<input value="${name}" id="name" name="name" type="text" required="required" maxlength="20" placeholder="用户名称" />
					</p>
					<p>
						<label for="nick">用户昵称</label>
						<input value="${nick}" id="nick" name="nick" type="text" required="required" maxlength="20" placeholder="用户昵称" />
					</p>
					<p>
						<label for="mail">用户邮箱</label>
						<input value="${mail}" id="mail" name="mail" type="email" required="required" maxlength="40" placeholder="用户邮箱" />
						<button id="sendCode" class="button" type="button">获取验证码</button>
					</p>
					<p>
						<label for="mobile">用户手机</label>
						<input value="${mobile}" id="mobile" name="mobile" type="text" maxlength="20" placeholder="用户手机" />
					</p>
					<p>
						<label for="password">用户密码</label>
						<input value="" id="password" name="password" type="password" required="required" maxlength="20" placeholder="用户密码" />
					</p>
					<p>
						<label for="code">验证码</label>
						<input value="" id="code" name="code" type="text" maxlength="6" placeholder="验证码" />
					</p>
					<p class="center">
						<button id="register" class="button" type="submit">注册</button>
					</p>
				</div>
			</form>
		</div>
		<#include "/include/footer.ftl">
	    <script type="text/javascript">
	    	// 如果注册失败自己刷新Token
			var encrypt = new JSEncrypt();
			$.get("/rsa/public/key", function(key) {
				encrypt.setPublicKey(key);
			});
			$(function() {
				$("#sendCode").click(function() {
					var success = true;
					var mail = $("#mail").val();
					$.ajax({
						url : "/check/user/mail",
						async : false,
						method : "GET",
						data : {"mail" : mail},
						success : function(message) {
							if("0000" !== message.code) {
								success = false;
								alert(message.message);
							}
						}
					});
					if(success) {
						$.get("/send/mail/code", {"mail" : mail}, function(message) {
							alert(message.message);
						});
					}
				});
				$("#register").submit(function() {
					var success = true;
					var name = $("#name").val();
					$.ajax({
						url : "/check/user/name",
						async : false,
						method : "GET",
						data : {"name" : name},
						success : function(message) {
							if("0000" !== message.code) {
								success = false;
								alert(message.message);
							}
						}
					});
					if(!success) {
						return false;
					}
					var mail = $("#mail").val();
					$.ajax({
						url : "/check/user/mail",
						async : false,
						method : "GET",
						data : {"mail" : mail},
						success : function(message) {
							if("0000" !== message.code) {
								success = false;
								alert(message.message);
							}
						}
					});
					if(!success) {
						return false;
					}
					var password = $("#password").val();
					var encrypted = encrypt.encrypt(password);
					$("#password").val(encrypted);
					$.post("/register", $("#register").serialize(), function(message) {
						if("0000" === message.code) {
							location.href = "/login";
						} else {
							$("#password").val(password);
							$("#token").val(message.token);
							alert(message.message);
						}
					});
					return false;
				});
			});
	    </script>
	</body>
</html>