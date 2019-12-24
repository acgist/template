# 静态资源

建议使用静态服务器访问本静态资源

#### Nginx配置

```
server {
	listen 443 ssl http2;
	server_name static.acgist.com;
	root /home/www/resources;
#	charset utf-8;

	access_log /var/log/nginx/static.acgist.com.log main buffer=32k flush=10s;

	error_page 404 /images/404.png;

	ssl on;
	ssl_certificate /home/ssl/static.acgist.com.crt;
	ssl_certificate_key /home/ssl/static.acgist.com.key;
	ssl_session_timeout 5m;
	ssl_ciphers TLS13-AES-128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-CBC-SHA256:ECDHE-ECDSA-AES128-CBC-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
	ssl_protocols TLSv1.2 TLSv1.3;
	ssl_prefer_server_ciphers on;

	add_header Strict-Transport-Security "max-age=15552000; includeSubdomains; preload";

	location = /robots.txt {
		alias /home/www/resources/static_robots.txt;
	}

	location ~* \.(eot|ttf|woff|svg)$ {
		add_header Access-Control-Allow-Origin *;
		add_header Access-Control-Allow-Headers X-Requested-With;
		add_header Access-Control-Allow-Methods GET,POST,OPTIONS;
	}

	location / {
		expires 30d;
		add_header Pragma public;
		add_header Cache-Control "public, must-revalidate, proxy-revalidate";
		log_not_found off;
		tcp_nodelay off;
		root /home/www/resources;
	}
}
```

## 注意事项