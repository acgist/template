<!DOCTYPE HTML>
<html>
	<head>
		<title>用户中心</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width" />
		<meta name="keywords" content="ACGIST" />
		<meta name="description" content="ACGIST" />
		
		<#include "/include/resources.ftl">
	</head>

	<body>
		<#include "/include/header.ftl">
		<div class="main">
			<form method="post" action="/user/update">
				<input type="hidden" name="token" value="${SESSION_CSRF_TOKEN}" />
				<div class="user">
					<p>
						<label>用户名称</label>
						<span>${user.name}</span>
					</p>
					<p>
						<label>用户邮箱</label>
						<span>${user.mail}</span>
					</p>
					<p>
						<label>用户手机</label>
						<span>${user.mobile}</span>
					</p>
					<p>
						<label for="nick">用户昵称</label>
						<input value="${user.nick}" id="nick" name="nick" type="text" required="required" maxlength="20" placeholder="用户昵称" />
					</p>
					<p class="center">
						<button id="update" class="button" type="submit">修改</button>
					</p>
				</div>
			</form>
		</div>
		<#include "/include/footer.ftl">
	</body>
</html>