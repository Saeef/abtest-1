package com.vuclip.abtesthttp.dao;

import java.util.List;
import java.util.Map;

import com.vuclip.abtesthttp.bean.TargetBean;
import com.vuclip.abtesthttp.util.Pagination;

public interface TargetDao {
	
	public Long addTarget(TargetBean target) throws Exception;
	
	public int updateTarget(TargetBean bean) throws Exception;
    
    public int delTarget(int id) throws Exception;
    
    public int updateTargetStatus(int id, int status);
	
	public Pagination<TargetBean> queryTarget(Integer currentPage, Integer numPerPage, Map<String,String> params);

	public List<Map<String, Object>> getTargetByTypeAndProid(int type, int proid) throws Exception;

	public int checkNameIsExist(String uname) throws Exception;

	public List<TargetBean> targetList(StringBuffer sql) throws Exception;

	public int targetListCount(StringBuffer sql) throws Exception;

	public int delTargetByProId(int proId) throws Exception;

}