package com.vuclip.abtesthttp.bean;

import java.sql.Date;

public class ProjectBean {
	
	private Integer id;
	
	private String name;
	
	private Integer active;//0 active 1:inactive
	
	private String description;
	
	private Date createtime;
	
	private Date updatetime;
	
	private Integer userId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "ProjectBean{" +
				"id=" + id +
				", name='" + name + '\'' +
				", active=" + active +
				", description='" + description + '\'' +
				", createtime=" + createtime +
				", updatetime=" + updatetime +
				", userId=" + userId +
				'}';
	}
}
