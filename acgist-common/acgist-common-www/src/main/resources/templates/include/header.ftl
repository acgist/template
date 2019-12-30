<header class="header">
	<nav>
		<a href="/">首页</a>
		<a href="/product">产品</a>
		<a href="/article">文章</a>
		<#if SESSION_USER??>
		<a class="user" href="/user">${SESSION_USER.nick}</a>
		<#else>
		<a class="user" href="/login">登陆</a>
		</#if>
	</nav>
</header>
