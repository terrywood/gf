package com.gt.bmf.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class BmfAppToken implements Serializable {

	private static final long serialVersionUID = -1686320397097241613L;

	@Id
	private Long id;

	private String platform;

	private String token;
	
	private String language;

	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
