package com.vuclip.abtesthttp.bean;

import java.util.List;

public class TargetBean{
	
	private Integer id;
	
	private Integer projectId;
	
	private String name;
	
	private String description;
	
	private String type;//0 tracking; 1 abtest
	
	private String active;//0 active 1:inactive
	
	private Integer countryControl;
	
	private Integer pageControl;
	
	private String createtime;
	
	private String updatetime;

	private List<TargetConBean> targetConBeanList;

	public List<TargetConBean> getTargetConBeanList() {
		return targetConBeanList;
	}

	public void setTargetConBeanList(List<TargetConBean> targetConBeanList) {
		this.targetConBeanList = targetConBeanList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public Integer getCountryControl() {
		return countryControl;
	}

	public void setCountryControl(Integer countryControl) {
		this.countryControl = countryControl;
	}

	public Integer getPageControl() {
		return pageControl;
	}

	public void setPageControl(Integer pageControl) {
		this.pageControl = pageControl;
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



}
