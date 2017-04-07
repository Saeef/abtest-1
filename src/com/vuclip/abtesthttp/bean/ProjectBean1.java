package com.vuclip.abtesthttp.bean;


public class ProjectBean1 {
	
	private Integer id;
	
	private String name;
	
	private String active;//0 active 1:inactive
	
	private String description;
	
	private String createtime;
	
	private String updatetime;
	
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


	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
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
