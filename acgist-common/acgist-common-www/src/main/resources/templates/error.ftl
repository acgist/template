<!DOCTYPE HTML>
<html>
	<head>
		<title>系统错误</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width" />
		<meta name="keywords" content="系统错误" />
		<meta name="description" content="系统错误" />
		
		<link rel="shortcut icon" href="/favicon.ico">
		<#include "/include/resources.ftl">
	</head>

	<body>
		<header class="container-fluid header error-header">
			<div class="text-center">系统错误</div>
		</header>
		<div class="container error">
			<p>错误代码：${code}</p>
			<p>错误描述：${message}</p>
			<p>系统时间：${.now?string("yyyy-MM-dd HH:mm:ss")}</p>
		</div>
		<footer class="container-fluid footer">
			<p>
				<a href="/">首页</a>
			</p>
			<p class="copyright">Copyright <span>&copy;</span> 2013-${.now?string("yyyy")} ACGIST.COM. All Rights Reserved.</p>
		</footer>
	</body>
</html>