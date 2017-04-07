package com.vuclip.abtesthttp.dao;

import java.util.List;
import java.util.Map;

import com.vuclip.abtesthttp.bean.FactorBean;
import com.vuclip.abtesthttp.bean.FactorConBean;
import com.vuclip.abtesthttp.util.Pagination;

public interface FactorDao {
	
	public Long addFactor(FactorBean bean);
    
    public int updateFactor(FactorBean bean) throws Exception;
    
    public int delFactor(int factorId) throws Exception;
    
    public Pagination<FactorBean> queryFactor(Integer currentPage, Integer pageSize, Map<String, String> params) throws Exception;

    public List<Map<String, Object>> getFactorByTargetId(int targetid) throws Exception;

    public int checkNameIsExist(String uname) throws Exception;

	public List<FactorBean> factorList(StringBuffer sql) throws Exception;

	public int factorListCount(StringBuffer sql) throws Exception;

    public int delFactorByTargetId(int targetId) throws Exception;
    public int delFactorByProId(int proId) throws Exception;

}