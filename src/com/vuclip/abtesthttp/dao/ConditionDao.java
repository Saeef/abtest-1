package com.vuclip.abtesthttp.dao;

import java.util.List;
import java.util.Map;

public interface ConditionDao {
	public List<Map<String,Object>> getConditions() throws Exception;

	public List<Map<String, Object>> getGroupBys(String conName) throws Exception;

	public List<Map<String,Object>> getRateConditions() throws Exception;

	public List<Map<String,Object>> getCommonConditions() throws Exception;

	public List<Map<String,Object>> getRateHidConditions() throws Exception;

	public List<Map<String,Object>> getConditionValue(String conditionName) throws Exception;

	public List<Map<String,Object>> getPagePaths(int pid) throws Exception;
}