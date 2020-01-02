package com.acgist.core.gateway.pojo.vo;

import java.util.List;
import java.util.Map;

import com.acgist.core.pojo.Pojo;

/**
 * <p>vo - 权限</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class PermissionVo extends Pojo {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String path;
	private String token;
	private Map<String, String> meta;
	private List<PermissionVo> children;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Map<String, String> getMeta() {
		return meta;
	}

	public void setMeta(Map<String, String> meta) {
		this.meta = meta;
	}

	public List<PermissionVo> getChildren() {
		return children;
	}

	public void setChildren(List<PermissionVo> children) {
		this.children = children;
	}
	
}
