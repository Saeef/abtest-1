package com.vuclip.abtesthttp.dao;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/28
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public interface ReportDao {

    public List<Map<String,Object>> abtestHPane(String sql) throws Exception;

    public List<Map<String,Object>> abtestDPane(String sql) throws Exception;

    public List<Map<String,Object>> abtestDayToDay(String sql) throws Exception;

    public List<Map<String,Object>> trackHPane(String sql) throws Exception;

    public List<Map<String,Object>> trackDPane(String sql) throws Exception;

    public List<Map<String,Object>> trackDayToDay(String sql) throws Exception;

	public List<Map<String, Object>> getTargetCountry(String sql) throws Exception;
}
